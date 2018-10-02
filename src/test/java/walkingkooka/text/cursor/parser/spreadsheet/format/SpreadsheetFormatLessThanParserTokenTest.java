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

package walkingkooka.text.cursor.parser.spreadsheet.format;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SpreadsheetFormatLessThanParserTokenTest extends SpreadsheetFormatConditionParserTokenTestCase<SpreadsheetFormatLessThanParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final SpreadsheetFormatConditionParserToken token = this.createToken();
        final SpreadsheetFormatParserToken right = token.right();
        final SpreadsheetFormatParserToken symbol = operatorSymbol();

        new FakeSpreadsheetFormatParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final SpreadsheetFormatParserToken n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SpreadsheetFormatParserToken n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final SpreadsheetFormatLessThanParserToken t) {
                assertSame(token, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SpreadsheetFormatLessThanParserToken t) {
                assertSame(token, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final SpreadsheetFormatBigDecimalParserToken t) {
                assertSame(right, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final SpreadsheetFormatLessThanSymbolParserToken t) {
                assertEquals(symbol, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("7");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                b.append("8");
                visited.add(t);
            }
        }.accept(token);
        assertEquals("7137162871528428", b.toString());
        assertEquals("visited",
                Lists.of(token, token, token,
                        symbol, symbol, symbol, symbol, symbol,
                        right, right, right, right, right,
                        token, token, token),
                visited);
    }

    @Override
    SpreadsheetFormatLessThanParserToken createToken(final String text, final List<ParserToken> tokens) {
        return SpreadsheetFormatParserToken.lessThan(tokens, text);
    }

    @Override
    SpreadsheetFormatSymbolParserToken operatorSymbol() {
        return SpreadsheetFormatParserToken.lessThanSymbol("<", "<");
    }

    @Override
    protected Class<SpreadsheetFormatLessThanParserToken> type() {
        return SpreadsheetFormatLessThanParserToken.class;
    }
}
