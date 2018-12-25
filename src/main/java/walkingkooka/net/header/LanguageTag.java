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

import walkingkooka.collect.map.Maps;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a LanguageTag which is the value of a accept-language and similar headers.
 */
public abstract class LanguageTag extends HeaderValueWithParameters2<LanguageTag,
        LanguageTagParameterName<?>,
        String> {

    /**
     * No {@link Locale}.
     */
    public final static Optional<Locale> NO_LOCALE = Optional.empty();

    /**
     * No parameters constant.
     */
    public final static Map<LanguageTagParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * Returns a wildcard {@link LanguageTag}
     */
    public static LanguageTag wildcard() {
        return LanguageTagWildcard.INSTANCE;
    }

    /**
     * Factory that creates a new {@link LanguageTag}
     */
    public static LanguageTag with(final String value) {
        checkValue(value);

        return WILDCARD.string().equals(value) ?
                wildcard() :
                LanguageTagNonWildcard.nonWildcard(value, NO_PARAMETERS);
    }

    /**
     * Parsers a header value holding a single tag.
     */
    public static LanguageTag parse(final String text) {
        return LanguageTagOneHeaderParser.parseOne(text);
    }

    /**
     * Parsers a header value which may hold one or more tags.
     */
    public static List<LanguageTag> parseList(final String text) {
        return LanguageTagListHeaderParser.parseList(text);
    }

    /**
     * Package private to limit sub classing.
     */
    LanguageTag(final String value, final Map<LanguageTagParameterName<?>, Object> parameters) {
        super(value, parameters);
    }

    /**
     * Only returns true if languageTag is matched by the given languageTag. If this is a wildcard it matches any other languageTag.
     * If the argument is a wildcard a false is always returned even if this is a wildcard.
     */
    public final boolean isMatch(final LanguageTag languageTag) {
        Objects.requireNonNull(languageTag, "languageTag");
        return !languageTag.isWildcard() && this.isMatch0(languageTag);
    }

    abstract boolean isMatch0(final LanguageTag languageTag);

    // value.....................................................................................................

    /**
     * The wildcard character.
     */
    public final static CharacterConstant WILDCARD_VALUE = CharacterConstant.with('*');

    /**
     * Would be setter that returns a {@link LanguageTag} with the given value creating a new instance as necessary.
     */
    public final LanguageTag setValue(final String value) {
        checkValue(value);

        return this.value().equals(value) ?
                this :
                this.replace(value);
    }

    static void checkValue(final String value) {
        CharSequences.failIfNullOrEmpty(value, "value");
    }

    // Locale ........................................................................................................

    /**
     * {@link Locale} getter.
     */
    abstract public Optional<Locale> locale();

    // replace ........................................................................................................

    private LanguageTag replace(final String value) {
        return with(value);
    }

    // isXXX........................................................................................................

    /**
     * Returns true if this LanguageTag is a wildcard.
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

    // Object..........................................................................................................

    @Override
    int hashCode0(final String value) {
        return CaseSensitivity.INSENSITIVE.hash(value);
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof LanguageTag;
    }

    @Override
    boolean equals1(final String value, final String otherValue) {
        return CaseSensitivity.INSENSITIVE.equals(value, otherValue);
    }
}


