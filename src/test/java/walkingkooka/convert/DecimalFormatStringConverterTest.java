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

import org.junit.Test;
import walkingkooka.DecimalNumberContexts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public final class DecimalFormatStringConverterTest extends FixedTypeConverterTestCase<DecimalFormatStringConverter, Number> {

    private final static String PATTERN = "##00.00";

    @Test(expected = NullPointerException.class)
    public void testWithNullPatternFails() {
        DecimalFormatStringConverter.with(null);
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
    public void testBigInteger3() {
        this.convertAndCheck2(BigInteger.valueOf(-9), "M09D00");
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
    public void testBigDecimal3() {
        this.convertAndCheck2(BigDecimal.valueOf(-9), "M09D00");
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
    public void testDouble3() {
        this.convertAndCheck2(-9.0, "M09D00");
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
    public void testInteger3() {
        this.convertAndCheck2(-9, "M09D00");
    }

    private void convertAndCheck2(final Number number, final String expected) {
        final DecimalFormatStringConverter converter = this.createConverter();
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
                this.createContext('d', 'x', 'm', 'p'),
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
        return createContext('D', 'X', 'M', 'P');
    }

    private ConverterContext createContext(final char decimalPoint, final char exponentSymbol, final char minusSign, final char plusSign) {
        return ConverterContexts.basic(DecimalNumberContexts.basic(decimalPoint, exponentSymbol, minusSign, plusSign));
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
