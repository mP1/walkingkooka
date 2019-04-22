/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.net;

import walkingkooka.text.CharSequences;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link RelativeUrl} holds a URL with getters available to retrieve components.
 */
public final class RelativeUrl extends Url {

    /**
     * Parses a {@link String url} into a {@link RelativeUrl}
     */
    public static RelativeUrl parse(final String url) {
        return parseRelative0(url);
    }

    private static RelativeUrl parseRelative0(final String url) {
        Objects.requireNonNull(url, "url");

        if (url.contains("://")) {
            throw new IllegalArgumentException("Relative url contains protocol=" + CharSequences.quote(url));
        }

        try {
            // protocol added but will be ignored when other components are picked.
            return parseRelative1(new URL("http://example" + url));
        } catch (final MalformedURLException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static RelativeUrl parseRelative1(final URL url) {
        return RelativeUrl.with(
                UrlPath.parse(url.getPath()),
                UrlQueryString.with(nullToEmpty(url.getQuery())),
                UrlFragment.with(nullToEmpty(url.getRef())));
    }

    /**
     * Factory.
     */
    static RelativeUrl with(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(query, "query");
        Objects.requireNonNull(fragment, "fragment");

        return new RelativeUrl(path, query, fragment);
    }

    /**
     * Private constructor
     */
    private RelativeUrl(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        super(path, query, fragment);
    }

    // Url

    @Override
    public RelativeUrl setPath(final UrlPath path) {
        return this.setPath0(path).cast();
    }

    @Override
    public RelativeUrl setQuery(final UrlQueryString query) {
        return this.setQuery0(query).cast();
    }

    @Override
    public RelativeUrl setFragment(final UrlFragment fragment) {
        return this.setFragment0(fragment).cast();
    }

    @Override final RelativeUrl replace(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return new RelativeUrl(path, query, fragment);
    }

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    /**
     * Would be setter that attempts to set or replace the absolute url only properties. This has the added benefit
     * of being useful and able to convert a relative url to an absolute url.
     */
    @Override
    public AbsoluteUrl set(final UrlScheme scheme,
                           final Optional<UrlCredentials> credentials,
                           final HostAddress host,
                           final Optional<IpPort> port) {
        return AbsoluteUrl.with(scheme, credentials, host, port, this.path, this.query, this.fragment);
    }

    /**
     * Returns this
     */
    @Override
    public RelativeUrl relativeUrl() {
        return this;
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.path,
                this.query,
                this.fragment);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof RelativeUrl &&
                        this.equals0((RelativeUrl) other);
    }

    private boolean equals0(final RelativeUrl other) {
        return this.path.equals(other.path) && //
                this.query.equals(other.query) && //
                this.fragment.equals(other.fragment);
    }

    @Override
    void toString0(final StringBuilder b) {
        // no additional properties to add.
    }

    // Serializable

    private final static long serialVersionUID = 1L;
}
