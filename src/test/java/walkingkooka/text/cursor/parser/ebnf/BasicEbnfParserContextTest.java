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

package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.math.MathContext;
import java.util.Locale;

public final class BasicEbnfParserContextTest implements ClassTesting2<BasicEbnfParserContext>,
        EbnfParserContextTesting<BasicEbnfParserContext> {

    @Override
    public void testCurrencySymbol() {
    }

    @Override
    public void testGroupingSeparator() {
    }

    @Override
    public void testPercentageSymbol() {
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(),
                "decimalPoint='.' exponentSymbol='E' minusSign='-' plusSign='+'");
    }

    @Override
    public BasicEbnfParserContext createContext() {
        return BasicEbnfParserContext.instance();
    }

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext().currencySymbol();
    }

    @Override
    public char decimalPoint() {
        return this.decimalNumberContext().decimalPoint();
    }

    @Override
    public char exponentSymbol() {
        return this.decimalNumberContext().exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.decimalNumberContext().groupingSeparator();
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public char minusSign() {
        return this.decimalNumberContext().minusSign();
    }

    @Override
    public char percentageSymbol() {
        return this.decimalNumberContext().percentageSymbol();
    }

    @Override
    public char plusSign() {
        return this.decimalNumberContext().plusSign();
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(Locale.ENGLISH, this.mathContext());
    }

    @Override
    public Class<BasicEbnfParserContext> type() {
        return BasicEbnfParserContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
