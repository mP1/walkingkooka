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

public final class SpreadsheetGroupParserTokenTest extends SpreadsheetParentParserTokenTestCase<SpreadsheetGroupParserToken> {

    @Test
    public void testWithZeroTokensFails() {
        this.createToken(" k ");
    }

    @Test
    public void testWithTwoTokensFails() {
        this.createToken(NUMBER1 + NUMBER2, this.number1(), this.number2());
    }

    @Test
    public void testWith() {
        final String text = "(" + NUMBER1 + ")";
        final SpreadsheetGroupParserToken token = this.createToken(text, this.number1());
        this.checkText(token, text);
        assertSame(token, token.withoutSymbolsOrWhitespace());
    }

    @Test
    public void testWithSymbols() {
        final String text = "(" + NUMBER1 + ")";

        final SpreadsheetSymbolParserToken left = symbol("(");
        final SpreadsheetParserToken number = this.number1();
        final SpreadsheetSymbolParserToken right = symbol(")");

        final SpreadsheetGroupParserToken token = this.createToken(text, left, number, right);
        this.checkText(token, text);
        this.checkValue(token, left, number, right);

        final SpreadsheetGroupParserToken token2 = Cast.to(token.withoutSymbolsOrWhitespace().get());
        assertNotSame(token, token2);
        this.checkText(token2, text);
        this.checkValue(token2, number);
    }

    @Test
    public void testWithSymbols2() {
        final String text = "( " + NUMBER1 + " )";

        final SpreadsheetSymbolParserToken left = symbol("(");
        final SpreadsheetWhitespaceParserToken whitespace1 = this.whitespace();
        final SpreadsheetParserToken number = this.number1();
        final SpreadsheetWhitespaceParserToken whitespace2 = this.whitespace();
        final SpreadsheetSymbolParserToken right = symbol(")");

        final SpreadsheetGroupParserToken token = this.createToken(text, left, whitespace1, number, whitespace2, right);
        this.checkText(token, text);
        this.checkValue(token, left, whitespace1, number, whitespace2, right);

        final SpreadsheetGroupParserToken token2 = Cast.to(token.withoutSymbolsOrWhitespace().get());
        assertNotSame(token, token2);
        this.checkText(token2, text);
        this.checkValue(token2, number);
    }

    @Override
    SpreadsheetGroupParserToken createToken(final String text, final List<SpreadsheetParserToken> tokens) {
        return SpreadsheetParserToken.group(tokens, text);
    }

    @Override
    String text() {
        return "(" + NUMBER1 + ")";
    }

    @Override
    List<SpreadsheetParserToken> tokens() {
        return Lists.of(this.number1());
    }

    @Override
    protected SpreadsheetGroupParserToken createDifferentToken() {
        return this.createToken(NUMBER2, this.number2());
    }

    @Override
    protected Class<SpreadsheetGroupParserToken> type() {
        return SpreadsheetGroupParserToken.class;
    }
}
