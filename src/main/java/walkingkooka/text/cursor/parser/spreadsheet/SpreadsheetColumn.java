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

import java.util.Objects;

/**
 * Represents a single row.
 */
public final class SpreadsheetColumn extends SpreadsheetColumnOrRow {

    // https://support.office.com/en-us/article/excel-specifications-and-limits-1672b34d-7043-467e-8e27-269d656771c3
    final static int MAX = 16384;
    final static int RADIX = 26;

    final static String MAX_ROW_NAME = toString0(MAX, SpreadsheetReferenceKind.RELATIVE);

    /**
     * Factory that creates a new row.
     */
    public static SpreadsheetColumn with(final int value, final SpreadsheetReferenceKind referenceKind) {
        if(value < 0 || value >= MAX) {
            throw new IllegalArgumentException("Invalid row value " + value + " expected between 0 and " + MAX);
        }
        Objects.requireNonNull(referenceKind, "referenceKind");

        return new SpreadsheetColumn(value, referenceKind);
    }

    private SpreadsheetColumn(final int value, final SpreadsheetReferenceKind referenceKind) {
        super(value, referenceKind);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetColumn;
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
}