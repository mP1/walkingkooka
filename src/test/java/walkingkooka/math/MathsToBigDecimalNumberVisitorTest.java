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

package walkingkooka.math;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class MathsToBigDecimalNumberVisitorTest implements NumberVisitorTesting<MathsToBigDecimalNumberVisitor> {

    @Test
    public void testBigDecimal() {
        this.toBigDecimalAndCheck(BigDecimal.valueOf(123.5), BigDecimal.valueOf(123.5));
    }

    @Test
    public void testBigInteger() {
        this.toBigDecimalAndCheck(BigInteger.valueOf(123), BigDecimal.valueOf(123));
    }

    @Test
    public void testByte() {
        this.toBigDecimalAndCheck(Byte.MAX_VALUE, BigDecimal.valueOf(127));
    }

    @Test
    public void testDouble() {
        this.toBigDecimalAndCheck(123.5, BigDecimal.valueOf(123.5));
    }

    @Test
    public void testFloat() {
        this.toBigDecimalAndCheck(123.5f, BigDecimal.valueOf(123.5));
    }

    @Test
    public void testInteger() {
        this.toBigDecimalAndCheck(123, BigDecimal.valueOf(123));
    }

    @Test
    public void testLong() {
        this.toBigDecimalAndCheck(123L, BigDecimal.valueOf(123));
    }

    @Test
    public void testShort() {
        this.toBigDecimalAndCheck((short) 123, BigDecimal.valueOf(123));
    }

    @Test
    public void testOtherNumberFails() {
        this.toBigDecimalAndCheck(new Number() {
            @Override
            public int intValue() {
                throw new UnsupportedOperationException();
            }

            @Override
            public long longValue() {
                throw new UnsupportedOperationException();
            }

            @Override
            public float floatValue() {
                throw new UnsupportedOperationException();
            }

            @Override
            public double doubleValue() {
                throw new UnsupportedOperationException();
            }

            private static final long serialVersionUID = 1;
        }, null);
    }

    private void toBigDecimalAndCheck(final Number value, final BigDecimal expected) {
        assertEquals(Optional.ofNullable(expected),
                MathsToBigDecimalNumberVisitor.toBigDecimal(value),
                () -> "MathsToBigDecimalNumberVisitor.toBigDecimal " + value);
        assertEquals(Optional.ofNullable(expected),
                Maths.toBigDecimal(value),
                () -> "Maths.toBigDecimal " + value);
    }

    @Test
    public void testToString() {
        final MathsToBigDecimalNumberVisitor visitor = new MathsToBigDecimalNumberVisitor();
        visitor.accept(123L);
        this.toStringAndCheck(visitor, "123");
    }

    @Override
    public MathsToBigDecimalNumberVisitor createVisitor() {
        return new MathsToBigDecimalNumberVisitor();
    }

    @Override
    public String typeNamePrefix() {
        return Maths.class.getSimpleName();
    }

    @Override
    public Class<MathsToBigDecimalNumberVisitor> type() {
        return MathsToBigDecimalNumberVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
