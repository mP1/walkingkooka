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

package walkingkooka.net.http;

import walkingkooka.Value;
import walkingkooka.net.header.HeaderValue;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharacterConstant;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Holds a ETAG.
 * <a href="https://en.wikipedia.org/wiki/HTTP_ETag"></a>
 */
public abstract class HttpETag implements HeaderValue,
        Value<String> {

    /**
     * The separator between multiple tags.
     */
    final static CharacterConstant SEPARATOR = CharacterConstant.with(',');

    /**
     * Returns a wildcard {@link HttpETag}
     */
    public static HttpETag wildcard() {
        return HttpETagWildcard.instance();
    }

    /**
     * Factory that creates a new {@link HttpETag}
     */
    public static HttpETag with(final String value, final HttpETagValidator validator) {
        checkValue(value);
        checkValidator(validator);

        return HttpETagNonWildcard.with0(value, validator);
    }

    /**
     * Parsers a header value holding a single tag.
     */
    public static HttpETag parseOne(final String text) {
        return HttpETagOneParser.parseOne(text);
    }

    /**
     * Parsers a header value which may hold one or more tags.
     */
    public static List<HttpETag> parseList(final String text) {
        return HttpETagListParser.parseList(text);
    }

    /**
     * Builds a header value representation of a list of tags.
     */
    public static String toString(final List<HttpETag> tags) {
        Objects.requireNonNull(tags, "tags");

        return tags.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(TOSTRING_SEPARATOR));
    }

    private final static String TOSTRING_SEPARATOR = SEPARATOR.string().concat(" ");

    /**
     * Package private to limit sub classing.
     */
    HttpETag() {
        super();
    }

    // value.....................................................................................................

    /**
     * The wildcard character.
     */
    public final static CharacterConstant WILDCARD_VALUE = CharacterConstant.with('*');

    /**
     * Would be setter that returns a {@link HttpETag} with the given value creating a new instance as necessary.
     */
    public final HttpETag setValue(final String value) {
        checkValue(value);

        return this.value().equals(value) ?
                this :
                this.replace(value, this.validator());
    }

    static void checkValue(final String value) {
        CharPredicates.failIfNullOrFalse(value, "value", HttpETagParser.ETAG_VALUE);
    }

    // weak...........................................................................................................

    /**
     * The optional weak attribute
     */
    public abstract HttpETagValidator validator();

    /**
     * Would be setter that returns a {@link HttpETag} with the given {@link HttpETagValidator}.
     */
    public final HttpETag setValidator(final HttpETagValidator validator) {
        checkValidator0(validator);

        return this.validator().equals(validator) ?
                this :
                this.replace(this.value(), validator);
    }

    /**
     * Abstract because wildcard will also complain if a validator indicator is present, which is an invalid combination.
     */
    abstract void checkValidator0(final HttpETagValidator validator);

    static void checkValidator(final HttpETagValidator validator) {
        Objects.requireNonNull(validator, "validator");
    }

    // replace ........................................................................................................

    private HttpETag replace(final String value, final HttpETagValidator validator) {
        return with(value, validator);
    }

    // isXXX........................................................................................................

    /**
     * Returns true if this etag is a wildcard.
     */
    public abstract boolean isWildcard();

    // HeaderValue........................................................................................................

    /**
     * Returns the text or header value form.
     */
    public final String headerValue() {
        return this.toString();
    }
}


