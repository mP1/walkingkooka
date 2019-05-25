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

package walkingkooka.tree.search;

import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

/**
 * The attributeName of an attribute for a node.
 */
public final class SearchNodeAttributeName implements Name,
        Comparable<SearchNodeAttributeName> {

    private final static CharPredicate LETTER = CharPredicates.letter();

    final static CharPredicate INITIAL = LETTER;

    private final static CharPredicate DIGIT = CharPredicates.range('0', '9');

    final static CharPredicate PART = INITIAL.or(DIGIT.or(CharPredicates.is('-')).or(CharPredicates.is('.')));

    public static SearchNodeAttributeName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, "name", INITIAL, PART);

        final int index = CharSequences.indexOf(name, "..");
        if (-1 != index) {
            throw new InvalidCharacterException(name, index);
        }

        return new SearchNodeAttributeName(name);
    }

    // @VisibleForTesting
    private SearchNodeAttributeName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Object..................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SearchNodeAttributeName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final SearchNodeAttributeName other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final SearchNodeAttributeName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
