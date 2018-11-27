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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharacterConstant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Holds a ETAG.
 */
public abstract class HttpETag implements HashCodeEqualsDefined, Value<String> {

    /**
     * A constant holding no WEAK attribute.
     */
    public final static Optional<HttpETagWeak> NO_WEAK = Optional.empty();


    /**
     * A constant holding an {@link Optional} with a {@link HttpETagWeak}.
     */
    public final static Optional<HttpETagWeak> WEAK = Optional.of(HttpETagWeak.WEAK);

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
    public static HttpETag with(final String value, final Optional<HttpETagWeak> weak) {
        checkValue(value);
        checkWeak(weak);

        return HttpETagNonWildcard.with0(value, weak);
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
    public static List<HttpETag> parseMany(final String text) {
        return HttpETagManyParser.parseMany(text);
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
     * Protected to limit sub classing.
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
                this.replace(value, this.weak());
    }

    static void checkValue(final String value) {
        CharPredicates.failIfNullOrFalse(value, "value", ETAG_VALUE);
    }

    /**
     * A {@link CharPredicate} used to validate etag tokens.
     */
    private final static CharPredicate ETAG_VALUE = HttpCharPredicates.etagQuotedCharacter();

    // weak...........................................................................................................

    /**
     * The optional weak attribute
     */
    public abstract Optional<HttpETagWeak> weak();

    /**
     * Would be setter that returns a {@link HttpETag} with the given {@link Optional weak}.
     */
    public final HttpETag setWeak(final Optional<HttpETagWeak> weak) {
        checkWeak0(weak);

        return this.weak().equals(weak) ?
                this :
                this.replace(this.value(), weak);
    }

    /**
     * Abstract because wildcard will also complain if a weak indicator is present, which is an invalid combination.
     */
    abstract void checkWeak0(final Optional<HttpETagWeak> weak);

    static void checkWeak(final Optional<HttpETagWeak> weak) {
        Objects.requireNonNull(weak, "weak");
    }

    // replace ........................................................................................................

    private HttpETag replace(final String value, final Optional<HttpETagWeak> weak) {
        return with(value, weak);
    }

    // isXXX........................................................................................................

    /**
     * Returns true if this etag is a wildcard.
     */
    public abstract boolean isWildcard();
}


