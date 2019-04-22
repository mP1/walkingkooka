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
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicateBuilder;
import walkingkooka.predicate.character.CharPredicates;

/**
 * The name of a search node.
 */
public final class SearchNodeName implements Name,
        Comparable<SearchNodeName> {

    public static SearchNodeName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, "attributeName", INITIAL, PART);
        return new SearchNodeName(name);
    }

    private final static CharPredicate INITIAL = CharPredicateBuilder.empty()
            .or(Character::isJavaIdentifierStart)
            .andNot(CharPredicates.asciiControl()) // necessary because nul is also valid java identifier
            .build();
    private final static CharPredicate PART = CharPredicateBuilder.empty()
            .or(Character::isJavaIdentifierPart)
            .any("-")
            .andNot(CharPredicates.asciiControl()) // necessary because nul is also valid java identifier
            .build();

    static SearchNodeName fromClass(final Class<? extends SearchNode> klass) {
        final String name = klass.getSimpleName();
        return new SearchNodeName(name.substring("Search".length(), name.length() - Name.class.getSimpleName().length()));
    }

    // @VisibleForTesting
    private SearchNodeName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Object..................................................................................................

    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof SearchNodeName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final SearchNodeName other) {
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final SearchNodeName other) {
        return this.name.compareTo(other.name);
    }
}
