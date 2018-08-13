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

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class BigDecimalParserTokenTest extends ParserTokenTestCase<BigDecimalParserToken> {

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        BigDecimalParserToken.with(null, "123");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        BigDecimalParserToken.with(BigDecimal.ZERO, null);
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final BigDecimalParserToken token = this.createToken();

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
            protected void visit(final BigDecimalParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }
    
    @Test
    public void testIgnoresPrefix() {
        BigDecimalParserToken.with(BigDecimal.valueOf(123), "+123");
    }

    @Test
    public void testWithFractions() {
        BigDecimalParserToken.with(BigDecimal.valueOf(12.5), "12.5 nonsense");
    }

    @Override
    protected BigDecimalParserToken createToken() {
        return BigDecimalParserToken.with(BigDecimal.valueOf(123), "123");
    }

    @Override
    protected BigDecimalParserToken createDifferentToken() {
        return BigDecimalParserToken.with(BigDecimal.valueOf(987), "987");
    }

    @Override
    protected Class<BigDecimalParserToken> type() {
        return BigDecimalParserToken.class;
    }
}
