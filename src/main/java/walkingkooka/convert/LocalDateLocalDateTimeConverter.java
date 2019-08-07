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
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A {@link Converter} that converts {@link LocalDate} into {@link LocalDateTime}.
 */
final class LocalDateLocalDateTimeConverter extends FixedSourceTypeTargetTypeConverter<LocalDate, LocalDateTime> {

    final static LocalDateLocalDateTimeConverter INSTANCE = new LocalDateLocalDateTimeConverter();

    private LocalDateLocalDateTimeConverter() {
        super();
    }

    @Override
    Class<LocalDate> sourceType() {
        return LocalDate.class;
    }

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    LocalDateTime convert1(final LocalDate date, final ConverterContext context) {
        return LocalDateTime.of(date, TIME);
    }

    private final static LocalTime TIME = LocalTime.ofSecondOfDay(0);

    @Override
    String toStringSuffix() {
        return "";
    }
}
