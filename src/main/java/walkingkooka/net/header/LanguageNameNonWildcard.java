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

package walkingkooka.net.header;

import walkingkooka.InvalidTextLengthException;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Holds a language.<br>
 * <a href="https://tools.ietf.org/html/bcp47"></a>
 */
final class LanguageNameNonWildcard extends LanguageName {

    private final static Map<String, LanguageNameNonWildcard> CONSTANTS = registerConstants();

    /**
     * Creates constants for all the available {@link Locale locales}
     */
    private static Map<String, LanguageNameNonWildcard> registerConstants() {
        final Map<String, LanguageNameNonWildcard> constants = Maps.sorted(CASE_SENSITIVITY.comparator());

        for (Locale locale : Locale.getAvailableLocales()) {
            final String languageTag = locale.toLanguageTag();
            constants.put(languageTag, new LanguageNameNonWildcard(languageTag, Optional.of(locale)));
        }

        return constants;
    }

    /**
     * Factory that creates a new {@link LanguageNameNonWildcard}
     */
    static LanguageNameNonWildcard nonWildcard(final String value) {
        final LanguageNameNonWildcard constant = CONSTANTS.get(value);
        return null != constant ?
                constant :
                nonWildcard0(value);
    }

    /**
     * Only allow printable ascii language names.
     */
    private static LanguageNameNonWildcard nonWildcard0(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "language", PREDICATE);

        final Locale locale = Locale.forLanguageTag(value);
        if (locale.toString().isEmpty()) {
            throw new InvalidTextLengthException("language", value, 0, Integer.MAX_VALUE);
        }
        return new LanguageNameNonWildcard(value, Optional.of(locale));
    }

    private final static CharPredicate PREDICATE = CharPredicates.range('A', 'Z')
            .or(CharPredicates.range('a', 'z'))
            .or(CharPredicates.is('_'));

    /**
     * Private ctor use factory
     */
    private LanguageNameNonWildcard(final String value, final Optional<Locale> locale) {
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
     * True if the languages are equal.
     */
    @Override
    public boolean test(final Language language) {
        return this.equals(language.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageNameNonWildcard;
    }
}
