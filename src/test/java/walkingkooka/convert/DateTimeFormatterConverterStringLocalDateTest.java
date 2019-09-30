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
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterConverterStringLocalDateTest extends DateTimeFormatterConverterTestCase2<DateTimeFormatterConverterStringLocalDate, String, LocalDate> {

    @Test
    public void testConvert2() {
        this.convertAndCheck(this.createConverter(DateTimeFormatter.ofPattern("yyyy MM dd")),
                "2000 12 31",
                LocalDate.class,
                LocalDate.of(2000, 12, 31));
    }

    @Test
    public void testLocaleChange() {
        final DateTimeFormatterConverterStringLocalDate converter = this.createConverter();

        this.convertAndCheck2(converter,
                this.source(),
                this.createContext(),
                this.converted());

        this.convertAndCheck2(converter,
                "31 Dezember 2000",
                this.createContext2(),
                this.converted());
    }

    @Override
    protected DateTimeFormatterConverterStringLocalDate createConverter(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterStringLocalDate.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("dd MMMM yyyy");
    }

    @Override
    protected Class<LocalDate> targetType() {
        return LocalDate.class;
    }

    @Override
    String source() {
        return "31 December 2000";
    }

    @Override
    LocalDate converted() {
        return LocalDate.of(2000, 12, 31);
    }

    @Override
    public Class<DateTimeFormatterConverterStringLocalDate> type() {
        return DateTimeFormatterConverterStringLocalDate.class;
    }
}
