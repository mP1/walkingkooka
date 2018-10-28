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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SpreadsheetWhitespaceParserTokenTest extends SpreadsheetSymbolParserTokenTestCase<SpreadsheetWhitespaceParserToken> {

    @Test
    @Ignore
    public void testWithWhitespaceTextFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    public void testSetTextWhitespaceFails() {
        this.createToken().setText("   ");
    }

    @Test
    public void testWithWhitespace() {
        final SpreadsheetWhitespaceParserToken token = this.createToken("   ");
        this.checkText(token, "   ");
    }

    @Test
    public void testSetTextWhitespace() {
        final SpreadsheetWhitespaceParserToken token = this.createToken().setText("   ");
        this.checkText(token, "   ");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SpreadsheetWhitespaceParserToken token = this.createToken();

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
            protected void visit(final SpreadsheetWhitespaceParserToken t) {
                assertSame(token, t);
                b.append("5");
            }
        }.accept(token);
        assertEquals("13542", b.toString());
    }

    @Override
    protected String text() {
        return " ";
    }

    @Override
    String value() {
        return this.text();
    }

    @Override
    protected SpreadsheetWhitespaceParserToken createToken(final String value, final String text) {
        return SpreadsheetWhitespaceParserToken.with(value, text);
    }

    @Override
    protected SpreadsheetWhitespaceParserToken createDifferentToken() {
        return SpreadsheetWhitespaceParserToken.with("\n\r", "\n\r");
    }

    @Override
    protected Class<SpreadsheetWhitespaceParserToken> type() {
        return SpreadsheetWhitespaceParserToken.class;
    }
}
