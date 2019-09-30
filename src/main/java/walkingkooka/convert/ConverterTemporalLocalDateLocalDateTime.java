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
final class ConverterTemporalLocalDateLocalDateTime extends ConverterTemporalLocalDate<LocalDateTime> {

    /**
     * Singleton
     */
    final static ConverterTemporalLocalDateLocalDateTime INSTANCE = new ConverterTemporalLocalDateLocalDateTime();

    private ConverterTemporalLocalDateLocalDateTime() {
        super(0);
    }

    @Override
    boolean isTargetType(final Class<?> type) {
        return LocalDateTime.class == type;
    }

    @Override
    <T> T convert1(final LocalDate date,
                   final Class<T> type,
                   final ConverterContext context) {
        return type.cast(LocalDateTime.of(date, TIME));
    }

    private final static LocalTime TIME = LocalTime.MIDNIGHT;

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }
}
