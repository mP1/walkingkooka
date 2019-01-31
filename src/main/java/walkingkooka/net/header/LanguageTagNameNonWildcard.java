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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Holds a language tag.<br>
 * <a href="https://tools.ietf.org/html/bcp47"></a>
 */
final class LanguageTagNameNonWildcard extends LanguageTagName {

    private final static Map<String, LanguageTagNameNonWildcard> CONSTANTS = registerConstants();

    /**
     * Creates constants for all the available {@link Locale locales}
     */
    private final static Map<String, LanguageTagNameNonWildcard> registerConstants() {
        final Map<String, LanguageTagNameNonWildcard> constants = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

        for (Locale locale : Locale.getAvailableLocales()) {
            final String languageTag = locale.toLanguageTag();
            constants.put(languageTag, new LanguageTagNameNonWildcard(languageTag, Optional.of(locale)));
        }

        return constants;
    }

    /**
     * Factory that creates a new {@link LanguageTagNameNonWildcard}
     */
    static LanguageTagNameNonWildcard nonWildcard(final String value) {
        final LanguageTagNameNonWildcard constant = CONSTANTS.get(value);
        return null != constant ?
                constant :
                nonWildcard0(value);
    }

    /**
     * Only allow printable ascii language tag names.
     */
    private static LanguageTagNameNonWildcard nonWildcard0(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "language tag", PREDICATE);

        final Locale locale = Locale.forLanguageTag(value);
        if (locale.toString().isEmpty()) {
            throw new IllegalArgumentException("Invalid language tag " + CharSequences.quoteAndEscape(value));
        }
        return new LanguageTagNameNonWildcard(value, Optional.of(locale));
    }

    private final static CharPredicate PREDICATE = CharPredicates.range('A', 'Z')
            .or(CharPredicates.range('a', 'z'))
            .or(CharPredicates.is('_'));

    /**
     * Private ctor use factory
     */
    private LanguageTagNameNonWildcard(final String value, final Optional<Locale> locale) {
        super(value);
        this.locale = locale;
    }

    @Override
    public Optional<Locale> locale() {
        return this.locale;
    }

    private final Optional<Locale> locale;

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return false;
    }

    /**
     * Matches if the languages are equal.
     */
    @Override
    boolean isMatch(final LanguageTag languageTag) {
        return this.equals(languageTag.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageTagNameWildcard;
    }
}
