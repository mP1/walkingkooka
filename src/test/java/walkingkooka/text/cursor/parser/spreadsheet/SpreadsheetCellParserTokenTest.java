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
import static org.junit.Assert.assertSame;

public final class SpreadsheetCellParserTokenTest extends SpreadsheetParentParserTokenTestCase<SpreadsheetCellParserToken> {

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
        final SpreadsheetColumnParserToken column = this.column();
        final SpreadsheetRowParserToken row = this.row();
        final String text = ROW_TEXT + ":" + COLUMN_TEXT;
        final SpreadsheetCellParserToken cell = this.createToken(text, row, column);
        this.checkText(cell, text);
        this.checkValue(cell, row, column);
        assertEquals("cell", SpreadsheetCell.with(column.value(), row.value()), cell.cell());

        assertSame(cell, cell.withoutSymbolsOrWhitespace().get());
    }

    @Test
    public void testToExpressionNode() {
        this.toExpressionNodeAndCheck(ExpressionNode.reference(SpreadsheetCell.with(column().value(), row().value())));
    }

    @Override
    SpreadsheetCellParserToken createToken(final String text, final List<ParserToken> tokens) {
        return SpreadsheetParserToken.cell(tokens, text);
    }

    private SpreadsheetColumnParserToken column() {
        return column(COLUMN_VALUE);
    }

    private SpreadsheetColumnParserToken column(final int value) {
        return SpreadsheetParserToken.column(SpreadsheetColumn.with(value, SpreadsheetReferenceKind.RELATIVE), String.valueOf(value));
    }

    private SpreadsheetRowParserToken row() {
        return row(ROW_VALUE, ROW_TEXT);
    }

    private SpreadsheetRowParserToken row(final int value, final String text) {
        return SpreadsheetParserToken.row(SpreadsheetRow.with(value, SpreadsheetReferenceKind.RELATIVE), text);
    }

    @Override
    String text() {
        return ROW_TEXT + COLUMN_TEXT;
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(this.row(), this.column());
    }

    @Override
    protected SpreadsheetCellParserToken createDifferentToken() {
        return this.createToken("D9", Lists.of(this.column(9), this.row(3, "D")));
    }

    @Override
    protected Class<SpreadsheetCellParserToken> type() {
        return SpreadsheetCellParserToken.class;
    }
}
