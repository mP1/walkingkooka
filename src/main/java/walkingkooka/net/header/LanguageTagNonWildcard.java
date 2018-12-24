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

import walkingkooka.text.CharSequences;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Holds a language tag.<br>
 * <a href="https://tools.ietf.org/html/bcp47"></a>
 */
final class LanguageTagNonWildcard extends LanguageTag {

    /**
     * Factory that creates a new {@link LanguageTagNonWildcard}
     */
    static LanguageTagNonWildcard nonWildcard(final String value, final Map<LanguageTagParameterName<?>, Object> parameters) {
        return new LanguageTagNonWildcard(value,
                Optional.of(locale(value)),
                parameters);
    }

    private static Locale locale(final String value) {
        final Locale locale = Locale.forLanguageTag(value);
        if(locale.toString().isEmpty()) {
            throw new IllegalArgumentException("Invalid language tag " + CharSequences.quoteAndEscape(value));
        }
        return locale;
    }

    /**
     * Private ctor use factory
     */
    private LanguageTagNonWildcard(final String value, final Optional<Locale> locale, final Map<LanguageTagParameterName<?>, Object> parameters) {
        super(value, parameters);
        this.locale = locale;
    }

    @Override
    public Optional<Locale> locale() {
        return this.locale;
    }

    private final Optional<Locale> locale;

    /**
     * Ignores the validator and only compares the value for equality.
     */
    @Override
    boolean isMatch0(final LanguageTag languageTag) {
        return this.value.equals(languageTag.value());
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    LanguageTag replace(final Map<LanguageTagParameterName<?>, Object> parameters) {
        return new LanguageTagNonWildcard(this.value, this.locale, parameters);
    }
}
