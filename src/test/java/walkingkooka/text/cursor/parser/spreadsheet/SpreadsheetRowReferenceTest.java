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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetRowReferenceTest extends SpreadsheetColumnOrRowReferenceTestCase<SpreadsheetRowReference> {

    @Test
    public void testSetColumnNullFails() {
        assertThrows(NullPointerException.class, () -> {
            SpreadsheetReferenceKind.ABSOLUTE.row(23).setColumn(null);
        });
    }

    @Test
    public void testSetColumn() {
        final SpreadsheetColumnReference column = SpreadsheetReferenceKind.ABSOLUTE.column(1);
        final SpreadsheetRowReference row = SpreadsheetReferenceKind.ABSOLUTE.row(23);

        final SpreadsheetCellReference cell = row.setColumn(column);
        assertEquals(column, cell.column(), "column");
        assertEquals(row, cell.row(), "row");
    }

    @Test
    public void testEqualReferenceKindIgnored() {
        this.compareToAndCheckEqual(
                SpreadsheetReferenceKind.ABSOLUTE.row(VALUE),
                SpreadsheetReferenceKind.RELATIVE.row(VALUE));
    }

    @Test
    public void testLess() {
        this.compareToAndCheckLess(
                SpreadsheetReferenceKind.ABSOLUTE.row(VALUE),
                SpreadsheetReferenceKind.ABSOLUTE.row(VALUE + 999));
    }

    @Test
    public void testLess2() {
        this.compareToAndCheckLess(
                SpreadsheetReferenceKind.ABSOLUTE.row(VALUE),
                SpreadsheetReferenceKind.RELATIVE.row(VALUE + 999));
    }

    @Test
    public void testToStringRelative() {
        this.checkToString(0, SpreadsheetReferenceKind.RELATIVE, "1");
    }

    @Test
    public void testToStringRelative2() {
        this.checkToString(123, SpreadsheetReferenceKind.RELATIVE, "124");
    }

    @Test
    public void testToStringAbsolute() {
        this.checkToString(0, SpreadsheetReferenceKind.ABSOLUTE,"$1");
    }

    @Override
    SpreadsheetRowReference createReference(final int value, final SpreadsheetReferenceKind kind) {
        return SpreadsheetRowReference.with(value, kind);
    }

    @Override
    int maxValue() {
        return SpreadsheetRowReference.MAX;
    }

    @Override
    protected Class<SpreadsheetRowReference> type() {
        return SpreadsheetRowReference.class;
    }
}
