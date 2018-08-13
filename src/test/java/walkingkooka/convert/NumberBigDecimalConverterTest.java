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

import org.junit.Ignore;
import org.junit.Test;

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
    public void testDouble() {
        this.convertAndCheck(Double.valueOf(123), BigDecimal.valueOf(123));
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertAndCheck(Double.valueOf(123.75), BigDecimal.valueOf(123.75));
    }

    @Test
    @Ignore
    public void testDoubleMaxFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
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

    @Test
    public void testLong() {
        this.convertAndCheck(Long.valueOf(123), BigDecimal.valueOf(123));
    }

    @Override
    protected NumberBigDecimalConverter createConverter() {
        return NumberBigDecimalConverter.INSTANCE;
    }

    @Override
    protected Class<BigDecimal> onlySupportedType() {
        return BigDecimal.class;
    }

    @Override
    protected Class<NumberBigDecimalConverter> type() {
        return NumberBigDecimalConverter.class;
    }
}
