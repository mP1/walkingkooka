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

import walkingkooka.datetime.DateTimeContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

/**
 * A {@link Converter} that parses a {@link String} into a {@link LocalDate}.
 */
final class DateTimeFormatterConverterStringLocalDateTime extends DateTimeFormatterConverter<String, LocalDateTime> {

    static DateTimeFormatterConverterStringLocalDateTime with(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return new DateTimeFormatterConverterStringLocalDateTime(formatter);
    }

    /**
     * Private ctor use static factory
     */
    private DateTimeFormatterConverterStringLocalDateTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        super(formatter);
    }

    @Override
    Class<String> sourceType() {
        return String.class;
    }

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    LocalDateTime parseOrFormat(final String text,
                                final DateTimeFormatter formatter) throws DateTimeParseException {
        return LocalDateTime.parse(text, formatter);
    }
}
