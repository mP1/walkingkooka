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

import java.math.BigDecimal;
import java.math.BigInteger;

public final class NumberConverterIntegerTest extends NumberConverterTestCase<NumberConverterInteger, Integer> {

    private final static byte VALUE = 123;

    @Test
    public void testBigDecimal() {
        this.convertAndCheck(BigDecimal.valueOf(123), 123);
    }

    @Test
    public void testBigDecimalWithFraction() {
        this.convertFails(BigDecimal.valueOf(123.5));
    }

    @Test
    public void testBigDecimalPrecisionLoss() {
        this.convertFails(new BigDecimal("123456789012345678901234567890123456789012345678901234567890"));
    }

    @Test
    public void testBigDecimalPrecisionLoss2() {
        this.convertFails(new BigDecimal("1.23456789012345678901234567890123456789012345678901234567890"));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck(BigInteger.valueOf(123), 123);
    }

    @Test
    public void testFloat() {
        this.convertAndCheck2((float) VALUE);
    }

    @Test
    public void testDouble() {
        this.convertAndCheck2((double) VALUE);
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertFails(Double.valueOf(123.75));
    }

    @Test
    public void testDoubleWithFraction2() {
        this.convertFails(Double.valueOf(0.000001));
    }

    @Test
    public void testByte() {
        this.convertAndCheck2((byte) VALUE);
    }

    @Test
    public void testShort() {
        this.convertAndCheck2((short) VALUE);
    }

    @Test
    public void testInteger() {
        this.convertAndCheck2((int) VALUE);
    }

    @Test
    public void testLongPrecisionLoss() {
        this.convertFails(Long.MAX_VALUE);
    }

    @Test
    public void testLongPrecisionLoss2() {
        this.convertFails(Long.MIN_VALUE);
    }

    @Test
    public void testLong() {
        this.convertAndCheck2((long) VALUE);
    }

    private void convertAndCheck2(final Object value) {
        this.convertAndCheck(value, (int)VALUE);
    }

    @Override
    public NumberConverterInteger createConverter() {
        return NumberConverterInteger.INSTANCE;
    }

    @Override
    protected Class<Integer> onlySupportedType() {
        return Integer.class;
    }

    @Override
    public Class<NumberConverterInteger> type() {
        return NumberConverterInteger.class;
    }
}
