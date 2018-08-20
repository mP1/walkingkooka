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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public final class LocalDateTimeLongConverterTest extends LocalDateTimeConverterTestCase2<LocalDateTimeLongConverter, Long> {

    private final static int VALUE = 123;
    private final static LocalDate DAY_123 = LocalDate.ofEpochDay(VALUE);
    private final static LocalTime MIDNIGHT = LocalTime.ofSecondOfDay(0);
    private final static LocalTime QUARTER_DAY = LocalTime.of(6, 0);

    @Test
    public void testLocalDateTime() {
        this.convertAndCheck(LocalDateTime.of(DAY_123, MIDNIGHT), Long.valueOf(VALUE));
    }

    @Test
    public void testLocalDateTimeNonMidnightTimeFails() {
        this.convertFails(LocalDateTime.of(DAY_123, QUARTER_DAY));
    }

    @Test
    public void testConverterRoundTrip() {
        final LocalDateTime localDateTime = LocalDateTime.of(DAY_123, MIDNIGHT);
        final Long longValue = Cast.to(this.convertAndCheck(localDateTime, Long.valueOf(VALUE)));
        this.convertAndCheck(Converters.numberLocalDateTime(), longValue, LocalDateTime.class, localDateTime);
    }

    @Test
    public void testToString() {
        assertEquals("LocalDateTime->Long", this.createConverter().toString());
    }

    @Override
    protected LocalDateTimeLongConverter createConverter() {
        return LocalDateTimeLongConverter.INSTANCE;
    }

    @Override
    protected Class<Long> onlySupportedType() {
        return Long.class;
    }

    @Override
    protected Class<LocalDateTimeLongConverter> type() {
        return LocalDateTimeLongConverter.class;
    }
}
