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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.Assert.assertNotSame;

public final class SpreadsheetCellParserTokenTest extends SpreadsheetParentParserTokenTestCase<SpreadsheetCellParserToken> {

    private final static String ROW = "B";
    private final static String COLUMN = "3";

    @Test
    public void testWithZeroTokensFails() {
        this.createToken(" k ");
    }

    @Test
    public void testWithoutColumnFails() {
        this.createToken(ROW, this.row());
    }

    @Test
    public void testWithoutRowFails() {
        this.createToken(COLUMN, this.column());
    }

    @Test
    public void testWith() {
        final SpreadsheetColumnParserToken column = this.column();
        final SpreadsheetRowParserToken row = this.row();
        final String text = ROW + ":" + COLUMN;
        final SpreadsheetCellParserToken cell = this.createToken(text, row, column);
        this.checkText(cell, text);
        this.checkValue(cell, row, column);

        assertSame(cell, cell.withoutSymbolsOrWhitespace());
    }

    @Test
    public void testWithSymbolsAndWhitespace() {
        final SpreadsheetWhitespaceParserToken whitespace1 = this.whitespace();
        final SpreadsheetColumnParserToken column = this.column();
        final SpreadsheetSymbolParserToken symbol = SpreadsheetParserToken.symbol(":", ":");
        final SpreadsheetRowParserToken row = this.row();
        final SpreadsheetWhitespaceParserToken whitespace2 = this.whitespace();

        final String text = whitespace1.text() + ROW + ":" + COLUMN + whitespace2.text();

        final SpreadsheetCellParserToken cell = this.createToken(text, whitespace1, row, symbol, column, whitespace2);
        this.checkText(cell, text);
        this.checkValue(cell, whitespace1, row, symbol, column, whitespace2);

        final SpreadsheetCellParserToken cell2 = Cast.to(cell.withoutSymbolsOrWhitespace().get());
        assertNotSame(cell, cell2);
        this.checkText(cell, text);
        this.checkValue(cell2, row, column);
    }

    @Override
    SpreadsheetCellParserToken createToken(final String text, final List<SpreadsheetParserToken> tokens) {
        return SpreadsheetParserToken.cell(tokens, text);
    }

    private SpreadsheetColumnParserToken column() {
        return SpreadsheetParserToken.column(SpreadsheetColumn.with(2, SpreadsheetReferenceKind.RELATIVE), COLUMN);
    }

    private SpreadsheetRowParserToken row() {
        return SpreadsheetParserToken.row(SpreadsheetRow.with(1, SpreadsheetReferenceKind.RELATIVE), ROW);
    }
    
    @Override
    String text() {
        return ROW+COLUMN;
    }

    @Override
    List<SpreadsheetParserToken> tokens() {
        return Lists.of(this.number1());
    }

    @Override
    protected SpreadsheetCellParserToken createDifferentToken() {
        return this.createToken(NUMBER2, this.number2());
    }

    @Override
    protected Class<SpreadsheetCellParserToken> type() {
        return SpreadsheetCellParserToken.class;
    }
}
