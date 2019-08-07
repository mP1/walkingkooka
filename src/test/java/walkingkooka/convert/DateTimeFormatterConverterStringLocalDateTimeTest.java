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
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatterConverterStringLocalDateTimeTest extends DateTimeFormatterConverterTestCase<DateTimeFormatterConverterStringLocalDateTime, String, LocalDateTime> {

    @Test
    public void testConvert() {
        this.convertAndCheck("12 58 59 2000 01 31", LocalDateTime.of(LocalDate.of(2000, 1, 31), LocalTime.of(12, 58, 59)));
    }

    @Test
    public void testConvert2() {
        this.convertAndCheck(DateTimeFormatterConverterStringLocalDateTime.with(DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss")),
                "2000 01 31 12 58 59",
                LocalDateTime.class,
                LocalDateTime.of(LocalDate.of(2000, 1, 31), LocalTime.of(12, 58, 59)));
    }

    @Override
    protected DateTimeFormatterConverterStringLocalDateTime createConverter(final DateTimeFormatter formatter) {
        return DateTimeFormatterConverterStringLocalDateTime.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("HH mm ss yyyy MM dd");
    }

    @Override
    protected Class<LocalDateTime> onlySupportedType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<DateTimeFormatterConverterStringLocalDateTime> type() {
        return DateTimeFormatterConverterStringLocalDateTime.class;
    }
}
