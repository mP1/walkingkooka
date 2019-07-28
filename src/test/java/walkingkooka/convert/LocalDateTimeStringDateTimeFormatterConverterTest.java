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

public final class LocalDateTimeStringDateTimeFormatterConverterTest extends LocalDateLocalDateTimeLocaleTimeStringDateTimeFormatterConverterTestCase<LocalDateTimeStringDateTimeFormatterConverter, LocalDateTime> {

    @Test
    public void testConvert() {
        this.convertAndCheck(LocalDateTime.of(LocalDate.of(2000, 1, 31), LocalTime.of(12, 58, 59)), "2000-01-31T12:58:59");
    }

    @Override
    protected LocalDateTimeStringDateTimeFormatterConverter createConverter(final DateTimeFormatter formatter) {
        return LocalDateTimeStringDateTimeFormatterConverter.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    @Override
    public Class<LocalDateTimeStringDateTimeFormatterConverter> type() {
        return LocalDateTimeStringDateTimeFormatterConverter.class;
    }
}
