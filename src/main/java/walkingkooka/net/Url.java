/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.net;

import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.net.header.MediaType;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visitable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Base class with getters that return the common components of a {@link Url}.
 */
public abstract class Url implements HashCodeEqualsDefined,
        HasJsonNode,
        Serializable,
        Value<String>,
        Visitable {

    // constants.......................................................................................................

    /**
     * The character that separates a host name or address from the following port number.
     */
    public final static CharacterConstant HOST_PORT_SEPARATOR = CharacterConstant.with(':');

    /**
     * The character within a URL that marks the beginning of a path.
     */
    public final static CharacterConstant PATH_START = CharacterConstant.with(UrlPath.SEPARATOR.character());

    /**
     * The character within a URL that marks the start of a query string.
     */
    public final static CharacterConstant QUERY_START = CharacterConstant.with('?');

    /**
     * The character within a URL that proceeds the query string.
     */
    public final static CharacterConstant QUERY_PARAMETER_SEPARATOR = CharacterConstant.with('&');

    /**
     * The alternate character within a URL that proceeds the query string.
     */
    public final static CharacterConstant QUERY_PARAMETER_SEPARATOR2 = CharacterConstant.with(';');

    /**
     * The character within a query string that separates name value pairs.
     */
    public final static CharacterConstant QUERY_NAME_VALUE_SEPARATOR = CharacterConstant.with('=');

    /**
     * The character proceeding an anchor within a URL.
     */
    public final static CharacterConstant FRAGMENT_START = CharacterConstant.with('#');

    // factories..................................................................................................

    /**
     * Examines the URL and attempts to parse it as a relative or absolute url.
     */
    public static Url parse(final String url) {
        Objects.requireNonNull(url, "url");

        final int colon = url.indexOf(':');
        return -1 != colon ?
                CaseSensitivity.INSENSITIVE.startsWith(url, DataUrl.SCHEME) ?
                        parseData(url) :
                        parseAbsolute(url) :
                parseRelative(url);
    }

    /**
     * Parses a {@link String url} into a {@link AbsoluteUrl}
     */
    public static AbsoluteUrl parseAbsolute(final String url) {
        return AbsoluteUrl.parseAbsolute0(url);
    }

    /**
     * Parses a {@link String url} into a {@link DataUrl}
     */
    public static DataUrl parseData(final String url) {
        return DataUrl.parseData0(url);
    }

    /**
     * Parses a {@link String url} into a {@link RelativeUrl}
     */
    public static RelativeUrl parseRelative(final String url) {
        return RelativeUrl.parseRelative0(url);
    }

    /**
     * Creates a {@link AbsoluteUrl}.
     */
    public static AbsoluteUrl absolute(final UrlScheme scheme,
                                       final Optional<UrlCredentials> credentials,
                                       final HostAddress host,
                                       final Optional<IpPort> port,
                                       final UrlPath path,
                                       final UrlQueryString query,
                                       final UrlFragment fragment) {
        return AbsoluteUrl.with(scheme, credentials, host, port, path, query, fragment);
    }

    /**
     * Creates a {@link DataUrl}.
     */
    public static DataUrl data(final Optional<MediaType> mediaType,
                               final Binary binary) {
        return DataUrl.with(mediaType, binary);
    }

    /**
     * Creates an {@link RelativeUrl}.
     */
    public static RelativeUrl relative(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return RelativeUrl.with(path, query, fragment);
    }

    // ctor.............................................................................................................

    /**
     * Package private constructor to limit sub classing.
     */
    Url() {
        super();
    }

    // isXXX............................................................................................................

    /**
     * Only {@link AbsoluteUrl} returns true.
     */
    public abstract boolean isAbsolute();

    /**
     * Only {@link DataUrl} returns true.
     */
    public abstract boolean isData();

    /**
     * Only {@link RelativeUrl} returns true
     */
    public abstract boolean isRelative();

    // UrlVisitor........................................................................................................

    abstract void accept(final UrlVisitor visitor);

    // helper...........................................................................................................

    /**
     * Convenient cast method used by would be public setters.
     */
    final <U extends Url> U cast() {
        return Cast.to(this);
    }

    // Serializable

    private final static long serialVersionUID = 1L;

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json string holding an {@link Url}.
     */
    static Url fromJsonNode(final JsonNode node) {
        return fromJsonNode0(node, Url::parse);
    }

    /**
     * Accepts a json string holding an {@link AbsoluteUrl}.
     */
    static AbsoluteUrl fromJsonNodeAbsolute(final JsonNode node) {
        return fromJsonNode0(node, Url::parseAbsolute);
    }

    /**
     * Accepts a json string holding an {@link DataUrl}.
     */
    static DataUrl fromJsonNodeData(final JsonNode node) {
        return fromJsonNode0(node, Url::parseData);
    }

    /**
     * Accepts a json string holding an {@link RelativeUrl}.
     */
    static RelativeUrl fromJsonNodeRelative(final JsonNode node) {
        return fromJsonNode0(node, Url::parseRelative);
    }

    private static <U extends Url> U fromJsonNode0(final JsonNode node,
                                                   final Function<String, U> parse) {
        Objects.requireNonNull(node, "node");

        return parse.apply(node.stringValueOrFail());
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.value());
    }

    static {
        HasJsonNode.register("url",
                Url::fromJsonNode,
                Url.class, AbsoluteUrl.class, DataUrl.class, RelativeUrl.class);
    }
}
