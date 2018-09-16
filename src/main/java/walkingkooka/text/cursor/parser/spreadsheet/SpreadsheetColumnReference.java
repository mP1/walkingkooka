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

import walkingkooka.Cast;

import java.util.Objects;

/**
 * Represents a column reference
 */
public final class SpreadsheetColumnReference extends SpreadsheetColumnOrRowReference implements Comparable<SpreadsheetColumnReference>{

    // https://support.office.com/en-us/article/excel-specifications-and-limits-1672b34d-7043-467e-8e27-269d656771c3
    final static int MAX = 16384;
    final static int RADIX = 26;

    final static String MAX_ROW_NAME = toString0(MAX, SpreadsheetReferenceKind.RELATIVE);

    /**
     * Factory that creates a new row.
     */
    public static SpreadsheetColumnReference with(final int value, final SpreadsheetReferenceKind referenceKind) {
        checkValue(value);
        Objects.requireNonNull(referenceKind, "referenceKind");

        return new SpreadsheetColumnReference(value, referenceKind);
    }

    private SpreadsheetColumnReference(final int value, final SpreadsheetReferenceKind referenceKind) {
        super(value, referenceKind);
    }

    @Override
    public SpreadsheetColumnReference add(final int value) {
        return Cast.to(this.add0(value));
    }

    /**
     * Would be setter that returns a {@link SpreadsheetColumnReference} with the given value creating a new
     * instance if it is different.
     */
    public SpreadsheetColumnReference setValue(final int value) {
        checkValue(value);
        return this.value == value ?
                this :
                new SpreadsheetColumnReference(value, this.referenceKind());
    }

    private static void checkValue(final int value) {
        if(value < 0 || value >= MAX) {
            throw new IllegalArgumentException(invalidColumnValue(value));
        }
    }

    static String invalidColumnValue(final int value) {
        return "Invalid column value " + value + " expected between 0 and " + MAX;
    }

    /**
     * Creates a {@link SpreadsheetCellReference} fromt this column and the new row.
     */
    public SpreadsheetCellReference setRow(final SpreadsheetRowReference row) {
        return SpreadsheetCellReference.with(this, row);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetColumnReference;
    }

    @Override
    public String toString() {
        return toString0(this.value, this.referenceKind());
    }

    private static String toString0(final int value, final SpreadsheetReferenceKind referenceKind){
        // 0=A, 1=B, AA = 26 * 1
        final StringBuilder b = new StringBuilder();
        b.append(referenceKind.prefix());

        toString1(value, b);

        return b.toString();
    }

    private static void toString1(final int value, final StringBuilder b) {
        final int v = (value / RADIX);
        if(v > 0) {
            toString1(v-1, b);
        }
        final int c = (value % RADIX) + 'A';
        b.append((char)c);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final SpreadsheetColumnReference other) {
        return this.value - other.value;
    }
}