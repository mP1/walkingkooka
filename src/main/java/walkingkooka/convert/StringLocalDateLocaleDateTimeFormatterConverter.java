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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * A {@link Converter} that attempts to parse a {@link String} into a {@link LocalDate} using ALL the given {@link DateTimeFormatter formatter}
 * until success or fails.
 */
final class StringLocalDateLocaleDateTimeFormatterConverter extends StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverter<LocalDate> {

    static StringLocalDateLocaleDateTimeFormatterConverter with(final List<DateTimeFormatter> formatters) {
        return new StringLocalDateLocaleDateTimeFormatterConverter(checkFormatters(formatters));
    }

    private StringLocalDateLocaleDateTimeFormatterConverter(final List<DateTimeFormatter> formatters) {
        super(formatters);
    }

    @Override
    Class<LocalDate> targetType() {
        return LocalDate.class;
    }

    @Override
    LocalDate parse(final String value, final DateTimeFormatter formatter) throws DateTimeParseException {
        return LocalDate.parse(value, formatter);
    }
}
