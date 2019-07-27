/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import org.junit.jupiter.api.Test;
import walkingkooka.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class CharacterParserTokenTest extends ParserTokenTestCase<CharacterParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final CharacterParserToken token = this.createToken();

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
            protected void visit(final CharacterParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createToken(), this.text());
    }

    @Override
    public CharacterParserToken createToken(final String text) {
        return CharacterParserToken.with(null != text ? text.charAt(1) : '*', text);
    }

    @Override
    public String text() {
        return "'A'";
    }

    @Override
    public CharacterParserToken createDifferentToken() {
        return this.createToken("'Z'");
    }

    @Override
    public Class<CharacterParserToken> type() {
        return CharacterParserToken.class;
    }
}
