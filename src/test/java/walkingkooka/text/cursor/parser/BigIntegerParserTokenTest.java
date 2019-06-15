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
import walkingkooka.tree.visit.Visiting;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BigIntegerParserTokenTest extends ParserTokenTestCase<BigIntegerParserToken> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            BigIntegerParserToken.with(null, "123");
        });
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final BigIntegerParserToken token = this.createToken();

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
            protected void visit(final BigIntegerParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }

    @Test
    public void testIgnoresPrefix() {
        BigIntegerParserToken.with(BigInteger.valueOf(123), "+123");
    }

    @Test
    public void testHex() {
        BigIntegerParserToken.with(BigInteger.valueOf(0x1234), "0x1234");
    }

    @Override
    public BigIntegerParserToken createToken(final String text) {
        return BigIntegerParserToken.with(new BigInteger(text), text);
    }

    @Override
    public String text() {
        return "123";
    }

    @Override
    public BigIntegerParserToken createDifferentToken() {
        return this.createToken("890");
    }

    @Override
    public Class<BigIntegerParserToken> type() {
        return BigIntegerParserToken.class;
    }
}
