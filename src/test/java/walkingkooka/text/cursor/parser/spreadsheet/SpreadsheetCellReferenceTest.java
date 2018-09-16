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
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SpreadsheetCellReferenceTest extends PublicClassTestCase<SpreadsheetCellReference> {

    private final static int COLUMN = 12;
    private final static int ROW = 34;

    @Test(expected = NullPointerException.class)
    public void testWithNullColumnFails() {
        SpreadsheetCellReference.with(null, row());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullRowFails() {
        SpreadsheetCellReference.with(column(), null);
    }

    @Test
    public void testWith() {
        final SpreadsheetColumnReference column = this.column();
        final SpreadsheetRowReference row = this.row();
        final SpreadsheetCellReference cell = SpreadsheetCellReference.with(column, row);
        this.checkColumn(cell, column);
        this.checkRow(cell, row);
    }
    
    // setColumn..................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetColumnNullFails() {
        this.create().setColumn(null);
    }

    @Test
    public void testSetColumnSame() {
        final SpreadsheetCellReference cell = this.create();
        assertSame(cell, cell.setColumn(this.column(COLUMN)));
    }

    @Test
    public void testSetColumnDifferent() {
        final SpreadsheetCellReference cell = this.create();
        final SpreadsheetColumnReference differentColumn = this.column(99);
        final SpreadsheetCellReference different = cell.setColumn(differentColumn);
        this.checkRow(different, this.row());
        this.checkColumn(different, differentColumn);
    }

    // setRow..................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetRowNullFails() {
        this.create().setRow(null);
    }

    @Test
    public void testSetRowSame() {
        final SpreadsheetCellReference cell = this.create();
        assertSame(cell, cell.setRow(this.row(ROW)));
    }

    @Test
    public void testSetRowDifferent() {
        final SpreadsheetCellReference cell = this.create();
        final SpreadsheetRowReference differentRow = this.row(99);
        final SpreadsheetCellReference different = cell.setRow(differentRow);
        this.checkColumn(different, this.column());
        this.checkRow(different, differentRow);
    }

    // add .............................................................................................

    @Test
    public void testAddColumnZeroAndRowZero() {
        final SpreadsheetCellReference cell = this.create();
        assertSame(cell, cell.add(0, 0));
    }

    @Test
    public void testAddColumnNonZeroAndRowNonZero() {
        final SpreadsheetCellReference cell = this.create();
        final int column= 10;
        final int row = 100;

        final SpreadsheetCellReference different = cell.add(column, row);
        this.checkColumn(different, this.column().setValue(COLUMN + column));
        this.checkRow(different, this.row().setValue(ROW + row));
    }

    @Test
    public void testAddColumnNonZero() {
        final SpreadsheetCellReference cell = this.create();
        final int column= 10;

        final SpreadsheetCellReference different = cell.add(column, 0);
        this.checkColumn(different, this.column().setValue(COLUMN + column));
        this.checkRow(different, this.row());
    }

    @Test
    public void testAddRowNonZero() {
        final SpreadsheetCellReference cell = this.create();
        final int row = 100;

        final SpreadsheetCellReference different = cell.add(0, row);
        this.checkColumn(different, this.column());
        this.checkRow(different, this.row().setValue(ROW + row));
    }

    // toString..................................................................................................

    @Test
    public void testToString() {
        assertEquals("$M$35", this.create().toString());
    }

    private SpreadsheetCellReference create() {
        return SpreadsheetCellReference.with(column(), row());
    }
    
    private SpreadsheetColumnReference column() {
        return this.column(COLUMN);
    }

    private SpreadsheetColumnReference column(final int value) {
        return SpreadsheetColumnReference.with(value, SpreadsheetReferenceKind.ABSOLUTE);
    }

    private SpreadsheetRowReference row() {
        return this.row(ROW);
    }

    private SpreadsheetRowReference row(final int value) {
        return SpreadsheetRowReference.with(value, SpreadsheetReferenceKind.ABSOLUTE);
    }

    private void checkColumn(final SpreadsheetCellReference cell, final SpreadsheetColumnReference column) {
        assertEquals("column", column, cell.column());
    }

    private void checkRow(final SpreadsheetCellReference cell, final SpreadsheetRowReference row) {
        assertEquals("row", row, cell.row());
    }
    
    @Override
    protected Class<SpreadsheetCellReference> type() {
        return SpreadsheetCellReference.class;
    }
}
