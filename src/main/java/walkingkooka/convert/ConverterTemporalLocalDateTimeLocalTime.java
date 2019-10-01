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

import walkingkooka.Either;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Converts {@link LocalDateTime} into {@link LocalTime} by simply returning the {@link LocalTime} component.
 */
final class ConverterTemporalLocalDateTimeLocalTime extends ConverterTemporalLocalDateTime<LocalTime> {

    /**
     * Singleton
     */
    final static ConverterTemporalLocalDateTimeLocalTime INSTANCE = new ConverterTemporalLocalDateTimeLocalTime();

    /**
     * Private ctor use singleton
     */
    private ConverterTemporalLocalDateTimeLocalTime() {
        super(0);
    }

    @Override
    boolean isTargetType(final Class<?> type) {
        return LocalTime.class == type;
    }

    @Override
    <T> Either<T, String> convertFromLocalDateTime(final long days,
                                                   final double time,
                                                   final LocalDateTime localDateTime,
                                                   final Class<T> type,
                                                   final ConverterContext context) {
        return Either.left(type.cast(localDateTime.toLocalTime()));
    }

    @Override
    Class<LocalTime> targetType() {
        return LocalTime.class;
    }
}
