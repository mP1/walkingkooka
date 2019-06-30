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

package walkingkooka.math;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.math.MathContext;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DecimalFormatSymbolsDecimalNumberContextTest implements ClassTesting2<DecimalFormatSymbolsDecimalNumberContext>,
        DecimalNumberContextTesting<DecimalFormatSymbolsDecimalNumberContext> {

    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL32;

    @Test
    public void testWithNullSymbolFails() {
        assertThrows(NullPointerException.class, () -> {
            DecimalFormatSymbolsDecimalNumberContext.with(null, 'E', '+', MATH_CONTEXT);
        });
    }

    @Test
    public void testWithExponentAndPlusSameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            DecimalFormatSymbolsDecimalNumberContext.with(this.decimalFormatSymbols(), '?', '?', MATH_CONTEXT);
        });
    }

    @Test
    public void testWithNullMathContextFails() {
        assertThrows(NullPointerException.class, () -> {
            DecimalFormatSymbolsDecimalNumberContext.with(this.decimalFormatSymbols(), 'E', '+', null);
        });
    }

    @Test
    public void testWith() {
        final DecimalFormatSymbolsDecimalNumberContext context = this.createContext();

        this.checkCurrencySymbol(context, "¤");
        this.checkDecimalPoint(context, '.');
        this.checkExponentSymbol(context, 'E');
        this.checkGroupingSeparator(context, ',');
        this.checkMinusSign(context, '-');
        this.checkPercentageSymbol(context, '%');
        this.checkPlusSign(context, '+');
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), "\"¤\" '.' 'E' ',' '-' '%' '+' precision=7 roundingMode=HALF_EVEN");
    }

    @Override
    public DecimalFormatSymbolsDecimalNumberContext createContext() {
        return DecimalFormatSymbolsDecimalNumberContext.with(this.decimalFormatSymbols(), 'E', '+', MATH_CONTEXT);
    }

    private DecimalFormatSymbols decimalFormatSymbols() {
        return DecimalFormatSymbols.getInstance(Locale.ENGLISH);
    }

    @Override
    public String currencySymbol() {
        return "¤";
    }

    @Override
    public char decimalPoint() {
        return '.';
    }

    @Override
    public char exponentSymbol() {
        return 'E';
    }

    @Override
    public char groupingSeparator() {
        return ',';
    }

    @Override
    public MathContext mathContext() {
        return MATH_CONTEXT;
    }

    @Override
    public char minusSign() {
        return '-';
    }

    @Override
    public char percentageSymbol() {
        return '%';
    }

    @Override
    public char plusSign() {
        return '+';
    }

    @Override
    public Class<DecimalFormatSymbolsDecimalNumberContext> type() {
        return DecimalFormatSymbolsDecimalNumberContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}