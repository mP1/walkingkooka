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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ConverterTemporalLocalDateTimeLocalDateTest extends ConverterTemporalLocalDateTimeTestCase<ConverterTemporalLocalDateTimeLocalDate, LocalDate> {

    @Test
    public void testMidnightTime() {
        final LocalDate date = LocalDate.of(2000, 1, 31);
        this.convertAndCheck2(LocalDateTime.of(date, LocalTime.MIDNIGHT), date);
    }

    @Test
    public void testNonMidnightTimeLost() {
        final LocalDate date = LocalDate.of(2000, 1, 31);
        this.convertAndCheck2(LocalDateTime.of(date, LocalTime.of(12, 58, 59)), date);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalDateTime->LocalDate");
    }

    // ConverterTesting.................................................................................................

    @Override
    public ConverterTemporalLocalDateTimeLocalDate createConverter() {
        return ConverterTemporalLocalDateTimeLocalDate.INSTANCE;
    }

    @Override
    Class<LocalDate> targetType() {
        return LocalDate.class;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterTemporalLocalDateTimeLocalDate> type() {
        return ConverterTemporalLocalDateTimeLocalDate.class;
    }
}
