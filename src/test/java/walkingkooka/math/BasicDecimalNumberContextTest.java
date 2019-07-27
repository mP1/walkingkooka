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
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicDecimalNumberContextTest implements ClassTesting2<BasicDecimalNumberContext>,
        DecimalNumberContextTesting2<BasicDecimalNumberContext> {

    private final static Locale LOCALE = Locale.FRANCE;
    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL32;

    @Test
    public void testWithNullCurrencySymbol() {
        assertThrows(NullPointerException.class, () -> {
            BasicDecimalNumberContext.with(null,
                    '.',
                    'E',
                    ',',
                    '-',
                    '%',
                    '+',
                    LOCALE,
                    MATH_CONTEXT);
        });
    }

    @Test
    public void testWithNullLocale() {
        assertThrows(NullPointerException.class, () -> {
            BasicDecimalNumberContext.with("$",
                    '.',
                    'E',
                    ',',
                    '-',
                    '%',
                    '+',
                    null,
                    MATH_CONTEXT);
        });
    }

    @Test
    public void testWithNullMathContext() {
        assertThrows(NullPointerException.class, () -> {
            BasicDecimalNumberContext.with("$",
                    '.',
                    'E',
                    ',',
                    '-',
                    '%',
                    '+',
                    LOCALE,
                    null);
        });
    }

    @Test
    public void testWith() {
        final BasicDecimalNumberContext context = this.createContext();
        this.checkCurrencySymbol(context, "$");
        this.checkDecimalPoint(context, '.');
        this.checkExponentSymbol(context, 'E');
        this.checkGroupingSeparator(context, ',');
        this.checkMinusSign(context, '-');
        this.checkPercentageSymbol(context, '%');
        this.checkPlusSign(context, '+');

        this.hasLocaleAndCheck(context, LOCALE);
        this.hasMathContextAndCheck(context, MATH_CONTEXT);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), "\"$\" '.' 'E' ',' '-' '%' '+' " + LOCALE + " " + MATH_CONTEXT);
    }

    @Override
    public BasicDecimalNumberContext createContext() {
        return BasicDecimalNumberContext.with("$",
                '.',
                'E',
                ',',
                '-',
                '%',
                '+',
                LOCALE,
                MATH_CONTEXT);
    }

    @Override
    public String currencySymbol() {
        return "$";
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
        return MathContext.DECIMAL32;
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
    public Class<BasicDecimalNumberContext> type() {
        return BasicDecimalNumberContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
