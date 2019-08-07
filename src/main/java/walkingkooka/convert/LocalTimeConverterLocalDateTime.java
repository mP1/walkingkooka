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
 * A {@link Converter} that converts {@link LocalTime} into {@link LocalDateTime}.
 */
final class LocalTimeConverterLocalDateTime extends LocalTimeConverter<LocalDateTime> {

    final static LocalTimeConverterLocalDateTime INSTANCE = new LocalTimeConverterLocalDateTime();

    private LocalTimeConverterLocalDateTime() {
        super();
    }

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    LocalDateTime convert2(final long seconds,
                           final long nano,
                           final LocalTime time) {
        return LocalDateTime.of(DATE, time);
    }

    private final static LocalDate DATE = LocalDate.ofEpochDay(0);

    @Override
    String toStringSuffix() {
        return "";
    }
}
