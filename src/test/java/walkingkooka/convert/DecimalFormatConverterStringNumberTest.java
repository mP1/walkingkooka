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

package walkingkooka.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.function.Function;

public final class DecimalFormatConverterStringNumberTest extends DecimalFormatConverterTestCase<DecimalFormatConverterStringNumber> {

    @Test
    public void testInvalidLocaleFails() {
        this.convertFails(this.createConverter(),
                "0",
                Number.class,
                this.createContext(Locale.ENGLISH));
    }

    @Test
    public void testByte() {
        this.convertAndCheck3(Byte.MAX_VALUE);
    }

    @Test
    public void testShort() {
        this.convertAndCheck3(Short.MAX_VALUE);
    }

    @Test
    public void testInteger() {
        this.convertAndCheck3(Integer.MAX_VALUE);
    }

    @Test
    public void testLong() {
        this.convertAndCheck3(Long.MAX_VALUE);
    }

    @Test
    public void testFloat() {
        this.convertAndCheck3(1.5f);
    }

    @Test
    public void testDouble() {
        this.convertAndCheck3(1.5);
    }

    @Test
    public void testBigDecimal() {
        this.convertAndCheck3(BigDecimal.valueOf(123.5));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck3(BigInteger.valueOf(123));
    }

    private void convertAndCheck3(final Number number) {
        this.convertAndCheck2("#", number.toString(), number);
    }

    @Test
    public void testNumber() {
        final String text = "1234567890123456789012345678901234567890.5";
        this.convertAndCheck(text,
                Number.class,
                new BigDecimal(text));
    }

    @Test
    public void testNumber2() {
        this.convertAndCheck("123.5",
                Number.class,
                new BigDecimal(123.5));
    }

    @Test
    public void testCurrency() {
        this.convertAndCheck2("$ #",
                "$ 123.5",
                BigDecimal.class,
                Locale.UK,
                BigDecimal.valueOf(123.5));
    }

    @Test
    public void testPercentage() {
        this.convertAndCheck2("#%",
                "123.5%",
                BigDecimal.class,
                BigDecimal.valueOf(1.235));
    }

    // ConverterTesting..................................................................................................

    @Override
    DecimalFormatConverterStringNumber createConverter(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        return DecimalFormatConverterStringNumber.with(decimalFormat);
    }

    @Override
    public Class<DecimalFormatConverterStringNumber> type() {
        return DecimalFormatConverterStringNumber.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNameSuffix() {
        return String.class.getSimpleName() + Number.class.getSimpleName();
    }
}
