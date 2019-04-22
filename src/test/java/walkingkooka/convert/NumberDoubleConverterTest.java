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

import java.math.BigDecimal;
import java.math.BigInteger;

public final class NumberDoubleConverterTest extends NumberConverterTestCase<NumberDoubleConverter, Double> {

    @Test
    public void testBigDecimal() {
        this.convertAndCheck(BigDecimal.valueOf(123), 123.0);
    }

    @Test
    public void testBigDecimalWithFraction() {
        this.convertAndCheck(BigDecimal.valueOf(123.5), 123.5);
    }

    @Test
    public void testBigDecimalPrecisionLoss() {
        this.convertFails(new BigDecimal("12345678901234567890"));
    }

    @Test
    public void testBigDecimalPrecisionLoss2() {
        this.convertFails(new BigDecimal("12345678901234567890.123456789"));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck(BigInteger.valueOf(123), 123.0);
    }

    @Test
    public void testByte() {
        this.convertAndCheck((byte) 123, 123.0);
    }

    @Test
    public void testShort() {
        this.convertAndCheck((short) 123, 123.0);
    }

    @Test
    public void testInteger() {
        this.convertAndCheck(123, 123.0);
    }

    @Test
    public void testLongMaxValue() {
        this.convertAndCheck(Long.MAX_VALUE, (double) Long.MAX_VALUE);
    }

    @Test
    public void testLongMinValue() {
        this.convertAndCheck(Long.MIN_VALUE, (double) Long.MIN_VALUE);
    }

    @Test
    public void testLong() {
        this.convertAndCheck(123L, 123.0);
    }

    @Test
    public void testFloat() {
        this.convertAndCheck(123.5f, 123.5);
    }

    @Test
    public void testDouble() {
        this.convertAndCheck(123.0);
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertAndCheck(123.5);
    }

    @Test
    public void testDoubleNan() {
        this.convertAndCheck(Double.NaN);
    }

    @Test
    public void testDoublePositiveInfinity() {
        this.convertAndCheck(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testDoubleNegativeInfinity() {
        this.convertAndCheck(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testDoubleMax() {
        this.convert(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleMin() {
        this.convert(Double.MIN_VALUE);
    }

    @Override
    public void testDoubleNanFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDoublePositiveInfinityFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDoubleNegativeInfinityFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDoubleMaxFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDoubleMinFails() {
        throw new UnsupportedOperationException();
    }

    @Override public NumberDoubleConverter createConverter() {
        return NumberDoubleConverter.INSTANCE;
    }

    @Override
    protected Class<Double> onlySupportedType() {
        return Double.class;
    }

    @Override
    public Class<NumberDoubleConverter> type() {
        return NumberDoubleConverter.class;
    }
}
