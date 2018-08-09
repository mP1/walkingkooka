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
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

public final class SpreadsheetLessThanEqualsSymbolParserTokenTest extends SpreadsheetLeafParserTokenTestCase<SpreadsheetLessThanEqualsSymbolParserToken, String> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SpreadsheetSymbolParserToken token = this.createToken();

        new FakeSpreadsheetParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("2");
            }

            @Override
            protected Visiting startVisit(final SpreadsheetParserToken t) {
                assertSame(token, t);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SpreadsheetParserToken t) {
                assertSame(token, t);
                b.append("4");
            }

            @Override
            protected void visit(final SpreadsheetLessThanEqualsSymbolParserToken t) {
                assertSame(token, t);
                b.append("5");
            }
        }.accept(token);
        assertEquals("13542", b.toString());
    }

    @Override
    String text() {
        return "..";
    }

    @Override
    String value() {
        return this.text();
    }

    @Override
    protected SpreadsheetLessThanEqualsSymbolParserToken createToken(final String value, final String text) {
        return SpreadsheetLessThanEqualsSymbolParserToken.with(value, text);
    }

    @Override
    protected SpreadsheetLessThanEqualsSymbolParserToken createDifferentToken() {
        return SpreadsheetLessThanEqualsSymbolParserToken.with(this.text(), "different");
    }

    @Override
    protected Class<SpreadsheetLessThanEqualsSymbolParserToken> type() {
        return SpreadsheetLessThanEqualsSymbolParserToken.class;
    }
}
