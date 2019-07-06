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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Holds a LanguageTagName only when combined with parameters creates a {@link LanguageTag} header value.
 */
public abstract class LanguageTagName extends HeaderNameValue implements Comparable<LanguageTagName>,
        Predicate<LanguageTag> {

    /**
     * No {@link Locale}.
     */
    public final static Optional<Locale> NO_LOCALE = Optional.empty();

    /**
     * Returns a wildcard {@link LanguageTagName}
     */
    public final static LanguageTagName WILDCARD = LanguageTagNameWildcard.INSTANCE;

    /**
     * Factory that creates a new {@link LanguageTagName}
     */
    public static LanguageTagName with(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", PREDICATE);

        return HeaderValue.WILDCARD.string().equals(value) ?
                WILDCARD :
                LanguageTagNameNonWildcard.nonWildcard(value);
    }

    private final static CharPredicate PREDICATE = CharPredicates.asciiPrintable();

    /**
     * Package private to limit sub classing.
     */
    LanguageTagName(final String value) {
        super(value);
    }

    /**
     * Factory that creates a {@link LanguageTag} with the given parameters.
     */
    public final LanguageTag setParameters(final Map<LanguageTagParameterName<?>, Object> parameters) {
        return LanguageTag.with(this)
                .setParameters(parameters);
    }

    // Locale ........................................................................................................

    /**
     * {@link Locale} getter.
     */
    abstract public Optional<Locale> locale();

    // isXXX........................................................................................................

    /**
     * Returns true if this LanguageTagName is a wildcard.
     */
    public abstract boolean isWildcard();

    // HeaderValue........................................................................................................

    /**
     * Returns the text or header value form.
     */
    public final String toHeaderText() {
        return this.name;
    }

    // HeaderNameValue..............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageTagName;
    }

    // Comparable..........................................................................................................

    @Override
    public int compareTo(final LanguageTagName other) {
        return this.compareTo0(other);
    }

    // Object..........................................................................................................

    @Override
    public final String toString() {
        return this.toHeaderText();
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;
}


