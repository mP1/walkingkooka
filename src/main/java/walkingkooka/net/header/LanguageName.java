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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Holds a LanguageName only when combined with parameters creates a {@link LanguageWithParameters} header value.
 */
public abstract class LanguageName extends HeaderNameValue implements Comparable<LanguageName>,
        Predicate<LanguageName> {

    /**
     * No {@link Locale}.
     */
    public final static Optional<Locale> NO_LOCALE = Optional.empty();

    /**
     * Returns a wildcard {@link LanguageName}
     */
    public final static LanguageName WILDCARD = LanguageNameWildcard.INSTANCE;

    /**
     * Factory that creates a new {@link LanguageName}
     */
    public static LanguageName with(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", PREDICATE);

        return HeaderValue.WILDCARD.string().equals(value) ?
                WILDCARD :
                LanguageNameNonWildcard.nonWildcard(value);
    }

    private final static CharPredicate PREDICATE = CharPredicates.asciiPrintable();

    /**
     * Package private to limit sub classing.
     */
    LanguageName(final String value) {
        super(value);
    }

    /**
     * Factory that creates a {@link LanguageWithParameters} with the given parameters.
     */
    public final LanguageWithParameters setParameters(final Map<LanguageParameterName<?>, Object> parameters) {
        return LanguageWithParameters.with(this)
                .setParameters(parameters);
    }

    // Locale ........................................................................................................

    /**
     * {@link Locale} getter.
     */
    abstract public Optional<Locale> locale();

    // isXXX........................................................................................................

    /**
     * Returns true if this LanguageName is a wildcard.
     */
    public abstract boolean isWildcard();

    // HeaderValue........................................................................................................

    /**
     * Returns the text or header value form.
     */
    public final String toHeaderText() {
        return this.name;
    }

    // Predicate........................................................................................................

    /**
     * True if the languages are equal.
     */
    @Override
    public boolean test(final LanguageName language) {
        Objects.requireNonNull(language, "language");

        language.testFailIfWildcard();
        return this.test0(language);
    }

    abstract void testFailIfWildcard();

    abstract boolean test0(final LanguageName language);

    // HeaderNameValue..............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageName;
    }

    // Comparable..........................................................................................................

    @Override
    public int compareTo(final LanguageName other) {
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


