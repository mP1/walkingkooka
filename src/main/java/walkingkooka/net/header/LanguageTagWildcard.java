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

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Holds a Wildcard language tag.
 */
final class LanguageTagWildcard extends LanguageTag {

    /**
     * Wildcard with no parameters instance.
     */
    final static LanguageTagWildcard INSTANCE = new LanguageTagWildcard(NO_PARAMETERS);

    /**
     * Factory that creates a new {@link LanguageTagWildcard}
     */
    static LanguageTagWildcard wildcard(final Map<LanguageTagParameterName<?>, Object> parameters) {
        return new LanguageTagWildcard(parameters);
    }

    /**
     * Private ctor use factory
     */
    private LanguageTagWildcard(final Map<LanguageTagParameterName<?>, Object> parameters) {
        super(WILDCARD_VALUE.string(), parameters);
    }

    @Override
    public Optional<Locale> locale() {
        return NO_LOCALE;
    }

    /**
     * Matches any other languageTag except for another wildcard.
     */
    @Override
    boolean isMatch0(final LanguageTag languageTag) {
        return true;
    }

    // isXXX........................................................................................................

    @Override
    public boolean isWildcard() {
        return true;
    }

    @Override
    LanguageTag replace(final Map<LanguageTagParameterName<?>, Object> parameters) {
        return parameters.isEmpty() ?
                INSTANCE :
                new LanguageTagWildcard(parameters);
    }
}
