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

public final class NumberBigDecimalConverterTest extends NumberConverterTestCase<NumberBigDecimalConverter, BigDecimal> {

    @Test
    public void testBigDecimal() {
        this.convertAndCheck(BigDecimal.valueOf(123.5));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck(BigInteger.valueOf(123), BigDecimal.valueOf(123));
    }

    @Test
    public void testByte() {
        this.convertAndCheck((byte)123, BigDecimal.valueOf(123));
    }

    @Test
    public void testShort() {
        this.convertAndCheck((short)123, BigDecimal.valueOf(123));
    }

    @Test
    public void testInteger() {
        this.convertAndCheck(123, BigDecimal.valueOf(123));
    }

    @Test
    public void testLong() {
        this.convertAndCheck(123L, BigDecimal.valueOf(123));
    }

    @Test
    public void testFloat() {
        this.convertAndCheck(123.5f, BigDecimal.valueOf(123.5));
    }

    @Test
    public void testDouble() {
        this.convertAndCheck(123.5, BigDecimal.valueOf(123.5));
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertAndCheck(123.75, BigDecimal.valueOf(123.75));
    }

    @Override
    public void testDoubleMaxFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDoubleMinFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testDoubleMax() {
        this.convertAndCheck(Double.MAX_VALUE, BigDecimal.valueOf(Double.MAX_VALUE));
    }

    @Test
    public void testDoubleMin() {
        this.convertAndCheck(Double.MIN_VALUE, BigDecimal.valueOf(Double.MIN_VALUE));
    }

    @Override public NumberBigDecimalConverter createConverter() {
        return NumberBigDecimalConverter.INSTANCE;
    }

    @Override
    protected Class<BigDecimal> onlySupportedType() {
        return BigDecimal.class;
    }

    @Override
    public Class<NumberBigDecimalConverter> type() {
        return NumberBigDecimalConverter.class;
    }
}
