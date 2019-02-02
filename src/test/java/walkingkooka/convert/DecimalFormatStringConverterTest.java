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

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContexts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DecimalFormatStringConverterTest extends FixedTypeConverterTestCase<DecimalFormatStringConverter, Number> {

    private final static String PATTERN = "##00.00";

    @Test
    public void testWithNullPatternFails() {
        assertThrows(NullPointerException.class, () -> {
            DecimalFormatStringConverter.with(null);
        });
    }

    @Test
    public void testWrongTypeFails() {
        this.convertFails("string-must-fail!");
    }

    @Test
    public void testWrongTypeFails2() {
        this.convertFails(LocalDate.now(), BigDecimal.class);
    }
    // BigInteger........................................................................................

    @Test
    public void testBigInteger() {
        this.convertAndCheck2(BigInteger.valueOf(123), "123D00");
    }

    @Test
    public void testBigInteger2() {
        this.convertAndCheck2(BigInteger.valueOf(9), "09D00");
    }

    @Test
    public void testBigIntegerMinus() {
        this.convertAndCheck2(BigInteger.valueOf(-9), "M09D00");
    }

    @Test
    public void testBigIntegerPercentage() {
        this.convertAndCheck2("####%", BigInteger.valueOf(9), "900R");
    }

    // BigDecimal........................................................................................

    @Test
    public void testBigDecimal() {
        this.convertAndCheck2(BigDecimal.valueOf(123.567), "123D57");
    }

    @Test
    public void testBigDecimal2() {
        this.convertAndCheck2(BigDecimal.valueOf(9), "09D00");
    }

    @Test
    public void testBigDecimalMinus() {
        this.convertAndCheck2(BigDecimal.valueOf(-9), "M09D00");
    }

    @Test
    public void testBigDecimalCurrency() {
        this.convertAndCheck2("#\u00A4", BigDecimal.valueOf(9), "9C");
    }

    @Test
    public void testBigDecimalGrouping() {
        this.convertAndCheck2("#,###", BigDecimal.valueOf(1234), "1G234");
    }

    @Test
    public void testBigDecimalPercentage() {
        this.convertAndCheck2("#%", BigDecimal.valueOf(9), "900R");
    }

    // Double........................................................................................

    @Test
    public void testDouble() {
        this.convertAndCheck2(123.0, "123D00");
    }

    @Test
    public void testDouble2() {
        this.convertAndCheck2(9.0, "09D00");
    }

    @Test
    public void testDoubleMinus() {
        this.convertAndCheck2(-9.0, "M09D00");
    }

    @Test
    public void testDoublePercentage() {
        this.convertAndCheck2("#%", 9.0, "900R");
    }

    @Test
    public void testDoubleCurrency() {
        this.convertAndCheck2("#\u00A4", 9.0, "9C");
    }

    @Test
    public void testDoubleGrouping() {
        this.convertAndCheck2("#,###", 1234.0, "1G234");
    }

    // Integer........................................................................................

    @Test
    public void testInteger() {
        this.convertAndCheck2(123, "123D00");
    }

    @Test
    public void testInteger2() {
        this.convertAndCheck2(9, "09D00");
    }

    @Test
    public void testIntegerMinus() {
        this.convertAndCheck2(-9, "M09D00");
    }

    @Test
    public void testIntegerCurrency() {
        this.convertAndCheck2("#\u00A4", 9, "9C");
    }

    @Test
    public void testIntegerGrouping() {
        this.convertAndCheck2("#,###", 1234, "1G234");
    }

    @Test
    public void testIntegerPercentage() {
        this.convertAndCheck2("#%", 9, "900R");
    }

    private void convertAndCheck2(final String pattern, final Number number, final String expected) {
        this.convertAndCheck2(DecimalFormatStringConverter.with(pattern), number, expected);
    }

    private void convertAndCheck2(final Number number, final String expected) {
        this.convertAndCheck2(this.createConverter(), number, expected);
    }

    private void convertAndCheck2(final DecimalFormatStringConverter converter, final Number number, final String expected) {
        this.convertAndCheck(converter, number, String.class, expected);
        this.convertAndCheck(converter, number, String.class, expected);
        this.convertAndCheck(converter, number, String.class, expected);
    }

    // Different Context.......................................................

    @Test
    public void testDifferentContext() {
        final DecimalFormatStringConverter converter = this.createConverter();
        final ConverterContext context = this.createContext();

        this.convertAndCheck(converter,
                -9,
                String.class,
                context,
                "M09D00");

        this.convertAndCheck(converter, -9,
                String.class,
                this.createContext("C", 'd', 'x', 'g', 'm', 'r', 'p'),
                "m09d00");

        this.convertAndCheck(converter,
                -9,
                String.class,
                context,
                "M09D00");
    }

    // toString........................................................................................

    @Test
    public void testToString() {
        assertEquals(PATTERN, this.createConverter().toString());
    }

    // helpers........................................................................................

    @Override
    protected DecimalFormatStringConverter createConverter() {
        return DecimalFormatStringConverter.with(PATTERN);
    }

    @Override
    protected ConverterContext createContext() {
        return createContext("C", 'D', 'X', 'G', 'M', 'R', 'P');
    }

    private ConverterContext createContext(final String currencySymbol,
                                           final char decimalPoint,
                                           final char exponentSymbol,
                                           final char groupingSeparator,
                                           final char minusSign,
                                           final char percentageSymbol,
                                           final char plusSign) {
        return ConverterContexts.basic(DecimalNumberContexts.basic(currencySymbol,
                decimalPoint,
                exponentSymbol,
                groupingSeparator,
                minusSign,
                percentageSymbol,
                plusSign));
    }

    @Override
    protected Class<Number> onlySupportedType() {
        return Number.class;
    }

    @Override
    protected Class<DecimalFormatStringConverter> type() {
        return DecimalFormatStringConverter.class;
    }
}
