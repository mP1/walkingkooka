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
import walkingkooka.datetime.DateTimeContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.math.MathContext;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicParserContextTest implements ClassTesting2<BasicParserContext>,
        ParserContextTesting<BasicParserContext> {

    private final static String CURRENCY = "$$";
    private final static char DECIMAL = 'D';
    private final static char EXPONENT = 'X';
    private final static char GROUPING = 'G';
    private final static char MINUS = 'M';
    private final static char PERCENTAGE = 'R';
    private final static char PLUS = 'P';

    private final static Locale LOCALE = Locale.ENGLISH;
    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL32;

    @Test
    public void testWithNullDateTimeContextFails() {
        assertThrows(NullPointerException.class, () -> BasicParserContext.with(null, DecimalNumberContexts.fake()));
    }

    @Test
    public void testWithNullDecimalNumberContextFails() {
        assertThrows(NullPointerException.class, () -> BasicParserContext.with(DateTimeContexts.fake(), null));
    }

    @Test
    public void testLocale() {
        this.hasLocaleAndCheck(this.createContext(), LOCALE);
    }

    @Test
    public void testMathContext() {
        this.hasMathContextAndCheck(this.createContext(), MATH_CONTEXT);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), this.dateTimeContext() + " " + this.decimalNumberContext());
    }

    @Override
    public BasicParserContext createContext() {
        return BasicParserContext.with(
                this.dateTimeContext(),
                this.decimalNumberContext());
    }

    private DateTimeContext dateTimeContext() {
        return DateTimeContexts.locale(Locale.ENGLISH, 50);
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.basic(CURRENCY,
                DECIMAL,
                EXPONENT,
                GROUPING,
                MINUS,
                PERCENTAGE,
                PLUS,
                LOCALE,
                MATH_CONTEXT);
    }

    @Override
    public String currencySymbol() {
        return CURRENCY;
    }

    @Override
    public char decimalSeparator() {
        return DECIMAL;
    }

    @Override
    public char exponentSymbol() {
        return EXPONENT;
    }

    @Override
    public char groupingSeparator() {
        return GROUPING;
    }

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public char negativeSign() {
        return MINUS;
    }

    @Override
    public char percentageSymbol() {
        return PERCENTAGE;
    }

    @Override
    public char positiveSign() {
        return PLUS;
    }

    @Override
    public Class<BasicParserContext> type() {
        return BasicParserContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
