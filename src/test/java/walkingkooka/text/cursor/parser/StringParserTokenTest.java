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
 */
package walkingkooka.text.cursor.parser;

import org.junit.Test;
import walkingkooka.tree.visit.Visiting;

public final class StringParserTokenTest extends ParserTokenTestCase<StringParserToken> {

    @Test(expected = NullPointerException.class)
    public void testWithNullContentFails() {
        StringParserToken.with(null, "\"abc\"");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        StringParserToken.with("abc", null);
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final StringParserToken token = this.createToken();

        new FakeParserTokenVisitor() {
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
            protected void visit(final StringParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }
    
    @Override
    protected StringParserToken createToken() {
        return StringParserToken.with("abc", "abc");
    }

    @Override
    protected StringParserToken createDifferentToken() {
        return StringParserToken.with("xyz", "xyz");
    }

    @Override
    protected Class<StringParserToken> type() {
        return StringParserToken.class;
    }
}
