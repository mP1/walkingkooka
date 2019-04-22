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

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A {@link Converter} that formats a {@link LocalDateTime} into a {@link String}
 */
final class LocalDateTimeStringDateTimeFormatterConverter extends StringDateTimeFormatterConverter<LocalDateTime> {

    static LocalDateTimeStringDateTimeFormatterConverter with(final DateTimeFormatter formatter) {
        return new LocalDateTimeStringDateTimeFormatterConverter(formatter);
    }

    private LocalDateTimeStringDateTimeFormatterConverter(final DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    Class<LocalDateTime> sourceType() {
        return LocalDateTime.class;
    }

    @Override
    String convert3(final LocalDateTime value) throws DateTimeException {
        return value.format(this.formatter);
    }
}
