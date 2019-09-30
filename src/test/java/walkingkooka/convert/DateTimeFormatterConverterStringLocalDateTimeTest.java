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
import walkingkooka.datetime.DateTimeContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterConverterStringLocalDateTimeTest extends DateTimeFormatterConverterTestCase2<DateTimeFormatterConverterStringLocalDateTime, String, LocalDateTime> {

    @Test
    public void testConvert2() {
        this.convertAndCheck(this.createConverter(DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss")),
                "2000 01 31 12 58 59",
                LocalDateTime.class,
                LocalDateTime.of(LocalDate.of(2000, 1, 31), LocalTime.of(12, 58, 59)));
    }

    @Test
    public void testLocaleChange() {
        final DateTimeFormatterConverterStringLocalDateTime converter = this.createConverter();

        this.convertAndCheck2(converter,
                this.source(),
                this.createContext(),
                this.converted());

        this.convertAndCheck2(converter,
                "12 58 59 2000 Dezember 31",
                this.createContext2(),
                this.converted());
    }

    @Override
    protected DateTimeFormatterConverterStringLocalDateTime createConverter(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterStringLocalDateTime.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("HH mm ss yyyy MMMM dd");
    }

    @Override
    protected Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    String source() {
        return "12 58 59 2000 December 31";
    }

    @Override
    LocalDateTime converted() {
        return LocalDateTime.of(LocalDate.of(2000, 12, 31), LocalTime.of(12, 58, 59));
    }

    @Override
    public Class<DateTimeFormatterConverterStringLocalDateTime> type() {
        return DateTimeFormatterConverterStringLocalDateTime.class;
    }
}
