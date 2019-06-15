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
package walkingkooka.tree.pojo;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicateBuilder;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

/**
 * Holds the name of a node within the tree. The name will be either a field/property name or an index.
 */
public final class PojoName implements Name,
        Comparable<PojoName> {

    private final static int INDEX_CACHE_SIZE = 128;

    static PojoName index(final int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index " + index + " must not be negative");
        }
        return index < INDEX_CACHE_SIZE ? INDEX_CACHE[index] : new PojoName(index);
    }

    private final static PojoName[] INDEX_CACHE = fillIndexCache();

    private static PojoName[] fillIndexCache() {
        final PojoName[] cache = new PojoName[INDEX_CACHE_SIZE];
        for (int i = 0; i < INDEX_CACHE_SIZE; i++) {
            cache[i] = new PojoName(i);
        }
        return cache;
    }

    static PojoName property(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, "name", INITIAL, PART);

        return new PojoName(name, -1);
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

    private PojoName(final int index) {
        this(String.valueOf(index), index);
    }

    private PojoName(final String name, final int index) {
        this.name = name;
        this.index = index;
    }

    void check(final PojoProperty property) {
        if (!property.name().equals(this)) {
            throw new IllegalArgumentException("Invalid child name mismatch expected " + CharSequences.quote(property.name().value()) + " got " + CharSequences.quote(this.name));
        }
    }

    // Name ......................................................................................

    @Override
    public String value() {
        return this.name;
    }

    CharSequence inQuotes() {
        return CharSequences.quote(this.value());
    }

    private final String name;

    private final int index;

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof PojoName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final PojoName other) {
        return CASE_SENSITIVITY.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ......................................................................................................

    @Override
    public int compareTo(final PojoName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
