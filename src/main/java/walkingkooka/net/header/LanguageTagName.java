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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Holds a LanguageTagName only when combined with parameters creates a {@link LanguageTag} header value.
 */
public abstract class LanguageTagName implements Name, Comparable<LanguageTagName> {

    /**
     * No {@link Locale}.
     */
    public final static Optional<Locale> NO_LOCALE = Optional.empty();

    /**
     * Returns a wildcard {@link LanguageTagName}
     */
    public static LanguageTagName WILDCARD = LanguageTagNameWildcard.INSTANCE;

    /**
     * Factory that creates a new {@link LanguageTagName}
     */
    public static LanguageTagName with(final String value) {
        CharSequences.failIfNullOrEmpty(value, "value");

        return HeaderValue.WILDCARD.string().equals(value) ?
                WILDCARD :
                LanguageTagNameNonWildcard.nonWildcard(value);
    }

    /**
     * Package private to limit sub classing.
     */
    LanguageTagName(final String value) {
        super();
        this.value = value;
    }

    @Override
    public final String value() {
        return this.value;
    }

    final String value;


    /**
     * Factory that creates a {@link LanguageTag} with the given parameters.
     */
    public LanguageTag setParameters(final Map<LanguageTagParameterName<?>, Object> parameters) {
        return LanguageTag.with(this)
                .setParameters(parameters);
    }

    abstract boolean isMatch(final LanguageTag languageTag);

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
        return this.value;
    }

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return CASE_SENSITIVITY.hash(this.value);
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final LanguageTagName other) {
        return this.compareTo(other) == 0;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    @Override
    public final String toString() {
        return this.toHeaderText();
    }

    // Comparable..........................................................................................................

    @Override
    public int compareTo(final LanguageTagName other) {
        return this.value.compareToIgnoreCase(other.value);
    }
}


