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

/**
 * Converts a {@link LocalDateTime} to another reflect.
 */
abstract class ConverterTemporalLocalDateTime<D> extends ConverterTemporal<LocalDateTime, D> {

    /**
     * Package private to limit sub classing.
     */
    ConverterTemporalLocalDateTime(final long offset) {
        super(offset);
    }

    @Override
    final Class<LocalDateTime> sourceType() {
        return LocalDateTime.class;
    }

    @Override
    final <T> Either<T, String> convert1(final LocalDateTime value,
                                         final Class<T> type,
                                         final ConverterContext context) {
        return this.convertFromLocalDateTime(value.toLocalDate().toEpochDay() + this.offset,
                (double) value.toLocalTime().toNanoOfDay() / Converters.NANOS_PER_DAY,
                value,
                type,
                context);
    }

    abstract <T> Either<T, String> convertFromLocalDateTime(final long days,
                                            final double time,
                                            final LocalDateTime localDateTime,
                                            final Class<T> type,
                                            final ConverterContext context);
}
