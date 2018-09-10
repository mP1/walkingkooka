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
        assertSame("column", column, cell.column());
        assertSame("row", row, cell.row());
    }

    @Test
    public void testToString() {
        assertEquals("$M$35", this.create().toString());
    }

    private SpreadsheetCellReference create() {
        return SpreadsheetCellReference.with(column(), row());
    }
    
    private SpreadsheetColumnReference column() {
        return SpreadsheetColumnReference.with(12, SpreadsheetReferenceKind.ABSOLUTE);
    }

    private SpreadsheetRowReference row() {
        return SpreadsheetRowReference.with(34, SpreadsheetReferenceKind.ABSOLUTE);
    }
    
    @Override
    protected Class<SpreadsheetCellReference> type() {
        return SpreadsheetCellReference.class;
    }
}
