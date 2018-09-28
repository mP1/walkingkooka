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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.ExpressionNode;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class SpreadsheetCellReferenceParserTokenTest extends SpreadsheetParentParserTokenTestCase<SpreadsheetCellReferenceParserToken> {

    private final static String ROW_TEXT = "B";
    private final static int ROW_VALUE = 2;
    private final static int COLUMN_VALUE = 3;
    private final static String COLUMN_TEXT = String.valueOf(COLUMN_VALUE);

    @Test
    public void testWithZeroTokensFails() {
        this.createToken(" k ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithoutColumnFails() {
        this.createToken(ROW_TEXT, this.row());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithoutRowFails() {
        this.createToken(COLUMN_TEXT, this.column());
    }

    @Test
    public void testWith() {
        final SpreadsheetColumnReferenceParserToken column = this.column();
        final SpreadsheetRowReferenceParserToken row = this.row();
        final String text = ROW_TEXT + ":" + COLUMN_TEXT;
        final SpreadsheetCellReferenceParserToken cell = this.createToken(text, row, column);
        this.checkText(cell, text);
        this.checkValue(cell, row, column);
        this.checkCell(cell, row, column);

        assertSame(cell, cell.withoutSymbolsOrWhitespace().get());
    }

    @Test
    public void testSetValueDifferent() {
        final SpreadsheetColumnReferenceParserToken column = this.column();
        final SpreadsheetRowReferenceParserToken row = this.row();
        final String text = ROW_TEXT + ":" + COLUMN_TEXT;
        final SpreadsheetCellReferenceParserToken cell = this.createToken(text, row, column);

        final SpreadsheetColumnReferenceParserToken differentColumn = this.column(1 + COLUMN_VALUE);
        final SpreadsheetRowReferenceParserToken differentRow = this.row(1+ROW_VALUE, "C");
        final SpreadsheetCellReferenceParserToken differentCell = cell.setValue(Lists.of(differentRow, differentColumn));

        assertNotSame(cell, differentCell);

        this.checkText(differentCell, text);// text not updated even when though child tokens are different.
        this.checkValue(differentCell, differentRow, differentColumn);
        this.checkCell(differentCell, differentRow, differentColumn);

        assertSame(cell, cell.withoutSymbolsOrWhitespace().get());
    }

    @Test
    public void testToExpressionNode() {
        this.toExpressionNodeAndCheck(ExpressionNode.reference(column().value().setRow(row().value())));
    }

    @Override
    SpreadsheetCellReferenceParserToken createToken(final String text, final List<ParserToken> tokens) {
        return SpreadsheetParserToken.cellReference(tokens, text);
    }

    private SpreadsheetColumnReferenceParserToken column() {
        return column(COLUMN_VALUE);
    }

    private SpreadsheetColumnReferenceParserToken column(final int value) {
        return SpreadsheetParserToken.columnReference(SpreadsheetColumnReference.with(value, SpreadsheetReferenceKind.RELATIVE), String.valueOf(value));
    }

    private SpreadsheetRowReferenceParserToken row() {
        return row(ROW_VALUE, ROW_TEXT);
    }

    private SpreadsheetRowReferenceParserToken row(final int value, final String text) {
        return SpreadsheetParserToken.rowReference(SpreadsheetRowReference.with(value, SpreadsheetReferenceKind.RELATIVE), text);
    }

    private void checkCell(final SpreadsheetCellReferenceParserToken cell,
                           final SpreadsheetRowReferenceParserToken row,
                           final SpreadsheetColumnReferenceParserToken column) {
        assertEquals("cell", SpreadsheetCellReference.with(column.value(), row.value()), cell.cell());
    }

    @Override
    protected String text() {
        return ROW_TEXT + COLUMN_TEXT;
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(this.row(), this.column());
    }

    @Override
    protected SpreadsheetCellReferenceParserToken createDifferentToken() {
        return this.createToken("D9", Lists.of(this.column(9), this.row(3, "D")));
    }

    @Override
    protected Class<SpreadsheetCellReferenceParserToken> type() {
        return SpreadsheetCellReferenceParserToken.class;
    }
}
