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
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.visit.Visiting;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class LocalDateParserTokenTest extends ParserTokenTestCase<LocalDateParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final LocalDateParserToken token = this.createToken();

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
            protected void visit(final LocalDateParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }

    @Override
    public LocalDateParserToken createToken(final String text) {
        return LocalDateParserToken.with(LocalDate.parse(text), text);
    }

    @Override
    public String text() {
        return "2000-12-31";
    }

    @Override
    public LocalDateParserToken createDifferentToken() {
        return this.createToken("1999-12-31");
    }

    @Override
    public Class<LocalDateParserToken> type() {
        return LocalDateParserToken.class;
    }
}
