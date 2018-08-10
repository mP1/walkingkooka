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

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class SpreadsheetCellEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<SpreadsheetCell> {

    private final static int COLUMN = 12;
    private final static int ROW = 34;

    @Test
    public void testDifferentColumn() {
        this.checkNotEquals(this.createSpreadsheetCell(COLUMN + 100, ROW));
    }

    @Test
    public void testDifferentRow() {
        this.checkNotEquals(this.createSpreadsheetCell(COLUMN, ROW + 100));
    }

    @Override
    protected SpreadsheetCell createObject() {
        return this.createSpreadsheetCell(COLUMN, ROW);
    }

    private  SpreadsheetCell createSpreadsheetCell(final int column, final int row) {
        return SpreadsheetCell.with(
                SpreadsheetColumn.with(column, SpreadsheetReferenceKind.RELATIVE),
                SpreadsheetRow.with(row, SpreadsheetReferenceKind.RELATIVE));
    }
}
