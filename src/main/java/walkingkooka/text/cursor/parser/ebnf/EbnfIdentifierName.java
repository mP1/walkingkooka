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

package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicateBuilder;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefined;

/**
 * An identifier.
 */
final public class EbnfIdentifierName implements Name, Comparable<EbnfIdentifierName>, HashCodeEqualsDefined {

    /**
     * <pre>
     *     letter = "A" | "B" | "C" | "D" | "E" | "F" | "G"
     *             | "H" | "I" | "J" | "K" | "L" | "M" | "N"
     *             | "O" | "P" | "Q" | "R" | "S" | "T" | "U"
     *             | "V" | "W" | "X" | "Y" | "Z" | "a" | "b"
     *             | "c" | "d" | "e" | "f" | "g" | "h" | "i"
     *             | "j" | "k" | "l" | "m" | "n" | "o" | "p"
     *             | "q" | "r" | "s" | "t" | "u" | "v" | "w"
     *             | "x" | "y" | "z" ;
     * </pre>
     */
    final static CharPredicate INITIAL = CharPredicates.range('A', 'Z').or(CharPredicates.range('a', 'z'));

    /**
     * <pre>
     * digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
     * underscore = "_"
     * character = letter | digit |  "_" ;
     * </pre>
     */
    final static CharPredicate PART = CharPredicateBuilder.create()
            .any("0123456789")
            .any("_")
            .or(INITIAL)
            .build();

    /**
     * Factory that creates a {@link EbnfIdentifierName}
     */
    public static EbnfIdentifierName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, "Identifier", INITIAL, PART);

        return new EbnfIdentifierName(name);
    }

    /**
     * Private constructor
     */
    private EbnfIdentifierName(final String name) {
        super();
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    final String name;

    // Object

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
                other instanceof EbnfIdentifierName &&
                this.equals0((EbnfIdentifierName) other);
    }

    private boolean equals0(final EbnfIdentifierName other) {
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(final EbnfIdentifierName other) {
        return this.name.compareTo(other.name);
    }
}
