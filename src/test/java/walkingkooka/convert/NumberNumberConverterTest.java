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

public final class NumberNumberConverterTest extends ConverterTestCase2<NumberNumberConverter> {

    // BigDecimal..................................................

    @Test
    public void testBigDecimalNumber() {
        this.convertAndCheck(this.bigDecimal(), Number.class);
    }

    @Test
    public void testBigDecimalBigDecimal() {
        this.convertAndCheck(this.bigDecimal(), BigDecimal.class);
    }

    @Test
    public void testBigDecimalBigInteger() {
        this.convertAndCheck(this.bigDecimal(), BigInteger.class, this.bigInteger());
    }

    @Test
    public void testBigDecimalDouble() {
        this.convertAndCheck(this.bigDecimal(), Double.class, this.doubleValue());
    }

    @Test
    public void testBigDecimalLong() {
        this.convertAndCheck(this.bigDecimal(), Long.class, this.longValue());
    }

    // BigInteger..............................................................................

    @Test
    public void testBigIntegerNumber() {
        this.convertAndCheck(this.bigInteger(), Number.class);
    }

    @Test
    public void testBigIntegerBigDecimal() {
        this.convertAndCheck(this.bigInteger(), BigDecimal.class, this.bigDecimal());
    }

    @Test
    public void testBigIntegerBigInteger() {
        this.convertAndCheck(this.bigInteger(), BigInteger.class);
    }

    @Test
    public void testBigIntegerDouble() {
        this.convertAndCheck(this.bigInteger(), Double.class, this.doubleValue());
    }

    @Test
    public void testBigIntegerLong() {
        this.convertAndCheck(this.bigInteger(), Long.class, this.longValue());
    }

    // Double..............................................................................

    @Test
    public void testDoubleNumber() {
        this.convertAndCheck(this.doubleValue(), Number.class);
    }

    @Test
    public void testDoubleBigDecimal() {
        this.convertAndCheck(this.doubleValue(), BigDecimal.class, this.bigDecimal());
    }

    @Test
    public void testDoubleBigInteger() {
        this.convertAndCheck(this.doubleValue(), BigInteger.class, this.bigInteger());
    }

    @Test
    public void testDoubleDouble() {
        this.convertAndCheck(this.doubleValue(), Double.class, this.doubleValue());
    }

    @Test
    public void testDoubleLong() {
        this.convertAndCheck(this.doubleValue(), Long.class, this.longValue());
    }

    // Double..............................................................................

    @Test
    public void testLongNumber() {
        this.convertAndCheck(this.longValue(), Number.class);
    }

    @Test
    public void testLongBigDecimal() {
        this.convertAndCheck(this.longValue(), BigDecimal.class, this.bigDecimal());
    }

    @Test
    public void testLongBigInteger() {
        this.convertAndCheck(this.longValue(), BigInteger.class, this.bigInteger());
    }

    @Test
    public void testLongDouble() {
        this.convertAndCheck(this.longValue(), Double.class, this.doubleValue());
    }

    @Test
    public void testLongLong() {
        this.convertAndCheck(this.longValue(), Long.class, this.longValue());
    }

    // helper............................................................................................................

    @Override
    public NumberNumberConverter createConverter() {
        return NumberNumberConverter.with(Converters.numberBigDecimal(),
                Converters.numberBigInteger(),
                Converters.numberDouble(),
                Converters.numberLong());
    }

    private BigDecimal bigDecimal() {
        return BigDecimal.valueOf(123);
    }

    private Double doubleValue() {
        return Double.valueOf(123);
    }

    private BigInteger bigInteger() {
        return BigInteger.valueOf(123);
    }

    private Long longValue() {
        return Long.valueOf(123);
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    public Class<NumberNumberConverter> type() {
        return NumberNumberConverter.class;
    }
}
