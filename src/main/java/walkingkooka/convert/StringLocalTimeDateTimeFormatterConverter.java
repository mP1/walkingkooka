/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.convert;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A {@link Converter} that parses a {@link String} into a {@link LocalTime}.
 */
final class StringLocalTimeDateTimeFormatterConverter extends DateTimeFormatterConverter<String, LocalTime>{

    static StringLocalTimeDateTimeFormatterConverter with(final DateTimeFormatter formatter) {
        return new StringLocalTimeDateTimeFormatterConverter(formatter);
    }

    /**
     * Private ctor use static factory
     */
    StringLocalTimeDateTimeFormatterConverter(final DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    Class<String> sourceType() {
        return String.class;
    }

    @Override Class<LocalTime> targetType() {
        return LocalTime.class;
    }

    @Override
    LocalTime convert3(final String value) throws DateTimeParseException {
        return LocalTime.parse(value);
    }
}
