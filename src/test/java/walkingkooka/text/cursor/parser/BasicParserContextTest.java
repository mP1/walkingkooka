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

import org.junit.Test;
import walkingkooka.DecimalNumberContexts;

import static org.junit.Assert.assertEquals;

public final class BasicParserContextTest extends ParserContextTestCase<BasicParserContext> {

    private final static String CURRENCY = "$$";
    private final static char DECIMAL = 'D';
    private final static char EXPONENT = 'X';
    private final static char GROUPING = 'G';
    private final static char MINUS = 'M';
    private final static char PERCENTAGE = 'R';
    private final static char PLUS = 'P';

    @Test(expected = NullPointerException.class)
    public void testWithNullBasicFails() {
        BasicParserContext.with(null);
    }

    @Test
    public void testWith() {
        final BasicParserContext context = this.createContext();
        this.checkCurrencySymbol(context, CURRENCY);
        this.checkDecimalPoint(context, DECIMAL);
        this.checkExponentSymbol(context, EXPONENT);
        this.checkGroupingSeparator(context, GROUPING);
        this.checkMinusSign(context, MINUS);
        this.checkPlusSign(context, PLUS);
    }

    @Test
    public void testToString() {
        assertEquals(this.basic().toString(), this.createContext().toString());
    }

    @Override
    protected BasicParserContext createContext() {
        return BasicParserContext.with(this.basic());
    }

    private BasicParserContext basic() {
        return BasicParserContext.with(DecimalNumberContexts.basic(CURRENCY, DECIMAL, EXPONENT, GROUPING, MINUS, PERCENTAGE, PLUS));
    }

    @Override
    protected Class<BasicParserContext> type() {
        return BasicParserContext.class;
    }
}
