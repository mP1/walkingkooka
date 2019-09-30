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

public final class DateTimeFormatterConverterLocalDateTimeStringTest extends DateTimeFormatterConverterTestCase2<DateTimeFormatterConverterLocalDateTimeString, LocalDateTime, String> {

    @Test
    public void testLocaleChange() {
        final DateTimeFormatterConverterLocalDateTimeString converter = this.createConverter();
        final LocalDateTime source = this.source();

        this.convertAndCheck2(converter,
                source,
                this.createContext(),
                this.converted());

        this.convertAndCheck2(converter,
                source,
                this.createContext2(),
                "2000-Januar-31T12:58:59");
    }

    @Override
    protected DateTimeFormatterConverterLocalDateTimeString createConverter(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterLocalDateTimeString.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("yyyy-MMMM-dd'T'hh:mm:ss");
    }

    @Override
    Class targetType() {
        return String.class;
    }

    @Override
    LocalDateTime source() {
        return LocalDateTime.of(LocalDate.of(2000, 1, 31), LocalTime.of(12, 58, 59));
    }

    @Override
    String converted() {
        return "2000-January-31T12:58:59";
    }

    @Override
    public Class<DateTimeFormatterConverterLocalDateTimeString> type() {
        return DateTimeFormatterConverterLocalDateTimeString.class;
    }
}
