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

import walkingkooka.ContextTestCase;

import static org.junit.Assert.assertEquals;

public abstract class ParserContextTestCase<C extends ParserContext> extends ContextTestCase<C> {

    @Override
    protected String requiredNameSuffix() {
        return ParserContext.class.getSimpleName();
    }
    
    protected void checkDecimalPoint(final ParserContext context, final char decimalPoint) {
        assertEquals("decimalPoint", decimalPoint, context.decimalPoint());
    }

    protected void checkExponentSymbol(final ParserContext context, final char exponentSymbol) {
        assertEquals("exponentSymbol", exponentSymbol, context.exponentSymbol());
    }

    protected void checkMinusSign(final ParserContext context, final char minusSign) {
        assertEquals("minusSign", minusSign, context.minusSign());
    }

    protected void checkPlusSign(final ParserContext context, final char plusSign) {
        assertEquals("plusSign", plusSign, context.plusSign());
    }
}
