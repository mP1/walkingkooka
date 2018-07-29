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
public final class SpreadsheetRow extends SpreadsheetColumnOrRow {

    // https://support.office.com/en-us/article/excel-specifications-and-limits-1672b34d-7043-467e-8e27-269d656771c3
    final static int MAX = 1_048_576;

    /**
     * Factory that creates a new column.
     */
    public static SpreadsheetRow with(final int value, final SpreadsheetReferenceKind referenceKind) {
        if(value < 0 || value >= MAX) {
            throw new IllegalArgumentException("Invalid column value " + value + " expected between 0 and " + MAX);
        }
        Objects.requireNonNull(referenceKind, "referenceKind");

        return new SpreadsheetRow(value, referenceKind);
    }

    private SpreadsheetRow(final int value, final SpreadsheetReferenceKind referenceKind) {
        super(value, referenceKind);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetRow;
    }

    @Override
    public String toString() {
        // in text form columns start at 1 but internally are zero based.
        return this.referenceKind().prefix() + (this.value + 1);
    }
}
