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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A {@link Converter} that formats a {@link LocalDate} into a {@link String}
 */
final class LocalDateStringDateTimeFormatterConverter extends DateTimeFormatterConverter<LocalDate, String> {

    static LocalDateStringDateTimeFormatterConverter with(final DateTimeFormatter formatter) {
        return new LocalDateStringDateTimeFormatterConverter(formatter);
    }

    private LocalDateStringDateTimeFormatterConverter(final DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    Class<LocalDate> sourceType() {
        return LocalDate.class;
    }

    @Override
    Class<String> targetType() {
        return String.class;
    }

    @Override
    String convert2(final LocalDate value) throws DateTimeException {
        return value.format(this.formatter);
    }
}
