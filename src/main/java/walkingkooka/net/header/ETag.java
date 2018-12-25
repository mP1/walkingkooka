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

package walkingkooka.net.header;

import walkingkooka.Value;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharacterConstant;

import java.util.List;
import java.util.Objects;

/**
 * Holds a ETAG.
 * <a href="https://en.wikipedia.org/wiki/HTTP_ETag"></a>
 */
public abstract class ETag implements HeaderValue,
        Value<String> {

    /**
     * Returns a wildcard {@link ETag}
     */
    public static ETag wildcard() {
        return ETagWildcard.instance();
    }

    /**
     * Factory that creates a new {@link ETag}
     */
    public static ETag with(final String value, final ETagValidator validator) {
        checkValue(value);
        checkValidator(validator);

        return ETagNonWildcard.with0(value, validator);
    }

    /**
     * Parsers a header value holding a single tag.
     */
    public static ETag parseOne(final String text) {
        return ETagOneHeaderParser.parseOne(text);
    }

    /**
     * Parsers a header value which may hold one or more tags.
     */
    public static List<ETag> parseList(final String text) {
        return ETagListHeaderParser.parseList(text);
    }

    /**
     * Package private to limit sub classing.
     */
    ETag() {
        super();
    }

    /**
     * Only returns true if etag is matched by the given etag. If this is a wildcard it matches any other etag.
     * If the argument is a wildcard a false is always returned even if this is a wildcard.
     */
    public final boolean isMatch(final ETag etag) {
        Objects.requireNonNull(etag, "etag");
        return !etag.isWildcard() && this.isMatch0(etag);
    }

    abstract boolean isMatch0(final ETag etag);

    // value.....................................................................................................

    /**
     * The wildcard character.
     */
    public final static CharacterConstant WILDCARD_VALUE = CharacterConstant.with('*');

    /**
     * Would be setter that returns a {@link ETag} with the given value creating a new instance as necessary.
     */
    public final ETag setValue(final String value) {
        checkValue(value);

        return this.value().equals(value) ?
                this :
                this.replace(value, this.validator());
    }

    static void checkValue(final String value) {
        CharPredicates.failIfNullOrFalse(value, "value", ETagHeaderParser.ETAG_VALUE);
    }

    // weak...........................................................................................................

    /**
     * The optional weak attribute
     */
    public abstract ETagValidator validator();

    /**
     * Would be setter that returns a {@link ETag} with the given {@link ETagValidator}.
     */
    public final ETag setValidator(final ETagValidator validator) {
        checkValidator0(validator);

        return this.validator().equals(validator) ?
                this :
                this.replace(this.value(), validator);
    }

    /**
     * Abstract because wildcard will also complain if a validator indicator is present, which is an invalid combination.
     */
    abstract void checkValidator0(final ETagValidator validator);

    static void checkValidator(final ETagValidator validator) {
        Objects.requireNonNull(validator, "validator");
    }

    // replace ........................................................................................................

    private ETag replace(final String value, final ETagValidator validator) {
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
    public final String toHeaderText() {
        return this.toString();
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }
}


