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

package walkingkooka.net.http.server.hateos;

import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

/**
 * The {@link Name} of an resource
 */
public final class HateosResourceName implements Name, Comparable<HateosResourceName> {

    /**
     * Factory that creates a {@link HateosResourceName}
     */
    public static HateosResourceName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, "name", INITIAL, PART);

        return new HateosResourceName(name);
    }

    private final static CharPredicate INITIAL = CharPredicates.letter();

    private final static CharPredicate PART = CharPredicates.letterOrDigit().or(CharPredicates.any("-"));

    /**
     * Private constructor
     */
    private HateosResourceName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Object

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HateosResourceName && this.equals0((HateosResourceName) other);
    }

    private boolean equals0(final HateosResourceName other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Dumps the resource name.
     */
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(final HateosResourceName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
