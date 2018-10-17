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
package walkingkooka.convert;

import walkingkooka.ContextTestCase;

import static org.junit.Assert.assertEquals;

public abstract class ConverterContextTestCase<C extends ConverterContext> extends ContextTestCase<C> {

    @Override
    protected String requiredNameSuffix() {
        return ConverterContext.class.getSimpleName();
    }
    
    protected void checkDecimalPoint(final ConverterContext context, final char decimalPoint) {
        assertEquals("decimalPoint", decimalPoint, context.decimalPoint());
    }

    protected void checkExponentSymbol(final ConverterContext context, final char exponentSymbol) {
        assertEquals("exponentSymbol", exponentSymbol, context.exponentSymbol());
    }

    protected void checkMinusSign(final ConverterContext context, final char minusSign) {
        assertEquals("minusSign", minusSign, context.minusSign());
    }

    protected void checkPlusSign(final ConverterContext context, final char plusSign) {
        assertEquals("plusSign", plusSign, context.plusSign());
    }
}
