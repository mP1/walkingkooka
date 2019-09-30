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
import java.time.LocalTime;

public final class ConverterLocalTimeNumberTest extends ConverterLocalTimeTestCase<ConverterLocalTimeNumber> {

    private final static byte VALUE = 123;
    private final static double WITH_NANOS = 123.5;

    @Test
    public void testLocalTimeToBigDecimal() {
        this.convertAndCheck2(BigDecimal.valueOf(VALUE));
    }

    @Test
    public void testLocalTimeToBigDecimal2() {
        this.convertAndCheck3(BigDecimal.valueOf(WITH_NANOS));
    }

    @Test
    public void testLocalTimeToBigInteger() {
        this.convertAndCheck2(BigInteger.valueOf(VALUE));
    }

    @Test
    public void testLocalTimeWithNanosToBigIntegerFails() {
        this.convertFails2(BigInteger.class);
    }

    @Test
    public void testLocalTimeToByte() {
        this.convertAndCheck2(VALUE);
    }

    @Test
    public void testLocalTimeWithNanosToByteFails() {
        this.convertFails2(Byte.class);
    }

    @Test
    public void testLocalTimeToShort() {
        this.convertAndCheck2((short) VALUE);
    }

    @Test
    public void testLocalTimeWithNanosToShortFails() {
        this.convertFails2(Short.class);
    }

    @Test
    public void testLocalTimeToInteger() {
        this.convertAndCheck2((int) VALUE);
    }

    @Test
    public void testLocalTimeWithNanosToIntegerFails() {
        this.convertFails2(Integer.class);
    }

    @Test
    public void testLocalTimeToLong() {
        this.convertAndCheck2((long) VALUE);
    }

    @Test
    public void testLocalTimeWithNanosToLongFails() {
        this.convertFails2(Long.class);
    }

    @Test
    public void testLocalTimeToFloat() {
        this.convertAndCheck2((float) VALUE);
    }

    @Test
    public void testLocalTimeToDouble() {
        this.convertAndCheck2((double) VALUE);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalTime->Number");
    }

    @Override
    public ConverterLocalTimeNumber createConverter() {
        return ConverterLocalTimeNumber.INSTANCE;
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    private void convertAndCheck2(final Number expected) {
        this.convertAndCheck(LocalTime.ofSecondOfDay(VALUE),
                expected.getClass(),
                expected);
    }

    private void convertAndCheck3(final Number expected) {
        this.convertAndCheck(this.withNanos(),
                expected.getClass(),
                expected);
    }

    private void convertFails2(final Class<?> target) {
        this.convertFails(this.withNanos(), target);
    }

    private LocalTime withNanos() {
        return LocalTime.ofSecondOfDay(VALUE).plusNanos(500000000);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterLocalTimeNumber> type() {
        return ConverterLocalTimeNumber.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNameSuffix() {
        return Number.class.getSimpleName();
    }
}
