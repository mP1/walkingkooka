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

package walkingkooka.text.cursor.parser.spreadsheet;

import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionReference;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A label or {@link Name} is a name to a cell reference, range and so on.
 * <pre>
 * A Defined Name must begin with a letter or an underscore ( _ ) and consist of only letters, numbers, or underscores.
 * Spaces are not permitted in a Defined Name. Moreover, a Defined Name may not be the same as a valid cell reference.
 * For example, the name AB11 is invalid because AB11 is a valid cell reference. Names are not case sensitive.
 * </pre>
 */
final public class SpreadsheetLabelName implements Name, HashCodeEqualsDefined, Comparable<SpreadsheetLabelName>, ExpressionReference {

    private final static CharPredicate LETTER = CharPredicates.range('A', 'Z').or(CharPredicates.range('a', 'z'));

    final static CharPredicate INITIAL = LETTER;

    private final static CharPredicate DIGIT = CharPredicates.range('0', '9');

    final static CharPredicate PART = INITIAL.or(DIGIT.or(CharPredicates.is('_')));

    final static Predicate<CharSequence> PREDICATE = Predicates.initialAndPart(INITIAL, PART);

    final static int MAX_LENGTH = 255;

    /**
     * Factory that creates a {@link SpreadsheetLabelName}
     */
    public static SpreadsheetLabelName with(final String name) {
        Objects.requireNonNull(name, "name");

        if(!isAcceptableLength(name)){
            throw new IllegalArgumentException("Label length " + name.length() + " is greater than allowed " + MAX_LENGTH);
        }

        if(!PREDICATE.test(name)){
            throw new IllegalArgumentException("Label contains invalid character(s)=" + CharSequences.quote(name));
        }
        if(isCellReference(name)){
            throw new IllegalArgumentException("Label is a valid cell reference=" + CharSequences.quote(name));
        }

        return new SpreadsheetLabelName(name);
    }

    static boolean isAcceptableLength(final String name) {
        return name.length() < MAX_LENGTH;
    }

    static boolean isCellReference(final String name) {
        int mode = 0; // -1 too long or contains invalid char
        int column = 0;
        int row = 0;

        // AB11 max row, max column
        final int length = name.length();
        for (int i = 0; i < length; i++) {
            final char c = name.charAt(i);

            // try and parse into column + row
            if (0 == mode) {
                final int digit = SpreadsheetColumnParser.valueFromDigit0(c);
                if(-1 != digit){
                    column = column * SpreadsheetColumn.RADIX + digit;
                    continue;
                }
                mode = 1;
            }
            if (1 == mode) {
                final int digit = Character.digit(c, SpreadsheetRow.RADIX);
                if (-1 != digit) {
                    row = SpreadsheetRow.RADIX * row + digit;
                    continue;
                }
                mode = 2;
            }
        }

        return 0 == mode ||
                (1 == mode &&
                column < SpreadsheetColumn.MAX &&
                row < SpreadsheetRow.MAX);
    }

    /**
     * Private constructor
     */
    private SpreadsheetLabelName(final String name) {
        super();
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    final String name;

    @Override
    public int compareTo(final SpreadsheetLabelName other) {
        return this.name.compareTo(other.name);
    }

    // Object

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || other instanceof SpreadsheetLabelName && this.equals0((SpreadsheetLabelName) other);
    }

    private boolean equals0(final SpreadsheetLabelName other) {
        return this.name.equalsIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
