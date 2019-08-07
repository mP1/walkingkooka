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

public final class NumberConverterBigIntegerTest extends NumberConverterTestCase<NumberConverterBigInteger, BigInteger> {

    @Test
    public void testBigDecimal() {
        this.convertAndCheck(BigDecimal.valueOf(123), BigInteger.valueOf(123));
    }

    @Test
    public void testBigDecimalWithFraction() {
        this.convertFails(BigDecimal.valueOf(123.5));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck(BigInteger.valueOf(123));
    }

    @Test
    public void testFloat() {
        this.convertAndCheck(123f, BigInteger.valueOf(123));
    }

    @Test
    public void testDouble() {
        this.convertAndCheck(123.0, BigInteger.valueOf(123));
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertFails(Double.valueOf(123.75));
    }

    @Override
    public void testDoubleMaxFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testByte() {
        this.convertAndCheck((byte) 123, BigInteger.valueOf(123));
    }

    @Test
    public void testShort() {
        this.convertAndCheck((short) 123, BigInteger.valueOf(123));
    }

    @Test
    public void testInteger() {
        this.convertAndCheck(123, BigInteger.valueOf(123));
    }

    @Test
    public void testLong() {
        this.convertAndCheck(123L, BigInteger.valueOf(123));
    }

    @Override
    public NumberConverterBigInteger createConverter() {
        return NumberConverterBigInteger.INSTANCE;
    }

    @Override
    protected Class<BigInteger> onlySupportedType() {
        return BigInteger.class;
    }

    @Override
    public Class<NumberConverterBigInteger> type() {
        return NumberConverterBigInteger.class;
    }
}
