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
import walkingkooka.Cast;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public final class LocalDateTimeBigIntegerConverterTest extends LocalDateTimeConverterTestCase2<LocalDateTimeBigIntegerConverter, BigInteger> {

    private final static int VALUE = 123;
    private final static LocalDate DAY_123 = LocalDate.ofEpochDay(VALUE);
    private final static LocalTime MIDNIGHT = LocalTime.ofSecondOfDay(0);
    private final static LocalTime QUARTER_DAY = LocalTime.of(6, 0);

    @Test
    public void testLocalDateTime() {
        this.convertAndCheck(LocalDateTime.of(DAY_123, MIDNIGHT), BigInteger.valueOf(VALUE));
    }

    @Test
    public void testLocalDateTimeWithNonMidnightTimeFails() {
        this.convertFails(LocalDateTime.of(DAY_123, QUARTER_DAY));
    }

    @Test
    public void testConverterRoundTrip() {
        final LocalDateTime localDateTime = LocalDateTime.of(DAY_123, MIDNIGHT);
        final BigInteger bigInteger = Cast.to(this.convertAndCheck(localDateTime, BigInteger.valueOf(VALUE)));
        this.convertAndCheck(Converters.numberLocalDateTime(), bigInteger, LocalDateTime.class, localDateTime);
    }
    
    @Test
    public void testToString() {
        assertEquals("LocalDateTime->BigInteger", this.createConverter().toString());
    }

    @Override
    protected LocalDateTimeBigIntegerConverter createConverter() {
        return LocalDateTimeBigIntegerConverter.INSTANCE;
    }

    @Override
    protected Class<BigInteger> onlySupportedType() {
        return BigInteger.class;
    }

    @Override
    protected Class<LocalDateTimeBigIntegerConverter> type() {
        return LocalDateTimeBigIntegerConverter.class;
    }
}
