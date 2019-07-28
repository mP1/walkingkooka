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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * A {@link Converter} that attempts to parse a {@link String} into a {@link LocalDateTime} using ALL the given {@link DateTimeFormatter formatter}
 * until success or fails.
 */
final class StringLocalDateTimeLocaleDateTimeFormatterConverter extends StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverter<LocalDateTime> {

    static StringLocalDateTimeLocaleDateTimeFormatterConverter with(final List<DateTimeFormatter> formatters) {
        return new StringLocalDateTimeLocaleDateTimeFormatterConverter(checkFormatters(formatters));
    }

    private StringLocalDateTimeLocaleDateTimeFormatterConverter(final List<DateTimeFormatter> formatters) {
        super(formatters);
    }

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    LocalDateTime parse(final String value, final DateTimeFormatter formatter) throws DateTimeParseException {
        return LocalDateTime.parse(value, formatter);
    }
}
