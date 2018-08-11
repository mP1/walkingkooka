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

import static org.junit.Assert.assertEquals;

public final class SpreadsheetRowParserTokenTest extends SpreadsheetLeafParserTokenTestCase<SpreadsheetRowParserToken, SpreadsheetRow> {

    @Test
    public void testToStringAbsolute() {
        assertEquals("$ABC", this.createToken(SpreadsheetReferenceKind.ABSOLUTE.row(555), "$ABC").toString());
    }

    @Test
    public void testToStringRelative() {
        assertEquals("1", this.createToken().toString());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SpreadsheetRowParserToken token = this.createToken();

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
            protected void visit(final SpreadsheetRowParserToken t) {
                assertSame(token, t);
                b.append("5");
            }
        }.accept(token);
        assertEquals("13542", b.toString());
    }

    @Override
    String text() {
        return "1";
    }

    @Override
    SpreadsheetRow value() {
        return SpreadsheetReferenceKind.RELATIVE.row(Integer.parseInt(this.text()));
    }

    @Override
    protected SpreadsheetRowParserToken createToken(final SpreadsheetRow value, final String text) {
        return SpreadsheetRowParserToken.with(value, text);
    }

    @Override
    protected SpreadsheetRowParserToken createDifferentToken() {
        return SpreadsheetRowParserToken.with(SpreadsheetReferenceKind.RELATIVE.row(999), "ABC");
    }

    @Override
    protected Class<SpreadsheetRowParserToken> type() {
        return SpreadsheetRowParserToken.class;
    }
}
