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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A {@link Converter} that handles converting any {@link Number} to a {@link LocalDateTime}.
 * The integer value becomes the days, and the fraction is a value of a whole day.
 */
final class ConverterNumberLocalDateTime extends ConverterNumber<LocalDateTime> {

    /**
     * Creates a new instance with the given date offset.
     * A value of zero is 1/1/1970.
     */
    static ConverterNumberLocalDateTime with(final long offset) {
        return new ConverterNumberLocalDateTime(offset);
    }

    /**
     * Private ctor
     */
    private ConverterNumberLocalDateTime(final long offset) {
        super();
        this.offset = offset;
    }

    @Override
    Either<LocalDateTime, String> bigDecimal(final BigDecimal value) {
        final double doubleValue = value.doubleValue();
        return 0 != BigDecimal.valueOf(doubleValue).compareTo(value) ?
                this.failConversion(value, LocalDateTime.class) :
                this.localDateTime(doubleValue);
    }

    @Override
    Either<LocalDateTime, String> bigInteger(final BigInteger value) {
        return this.localDateTime(value.longValueExact(), value);
    }

    @Override
    Either<LocalDateTime, String> doubleValue(final Double value) {
        return this.localDateTime(value.doubleValue());
    }

    @Override
    Either<LocalDateTime, String> longValue(final Long value) {
        return this.localDateTime(value, value);
    }

    private Either<LocalDateTime, String> localDateTime(final double value) {
        return !Double.isFinite(value) ?
                this.failConversion(value, LocalDateTime.class) :
                this.localDateTime0(value);
    }

    private Either<LocalDateTime, String> localDateTime0(final double value) {
        final double days = Math.floor(value);

        return localDateTime((int) days, value - days, value);
    }

    private Either<LocalDateTime, String> localDateTime(final long longValue, final Number value) {
        return localDateTime(longValue, 0, value);
    }

    private Either<LocalDateTime, String> localDateTime(final long day, final double fraction, final Object value) {
        final double doubleNano = fraction * Converters.NANOS_PER_DAY;
        final long nano = (long) doubleNano;
        return nano != doubleNano ?
                this.failConversion(value, LocalDateTime.class) :
                Either.left(LocalDateTime.of(
                        LocalDate.ofEpochDay(day + this.offset),
                        LocalTime.ofNanoOfDay(nano)));
    }

    private final long offset;

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    String toStringPrefix() {
        return "";
    }

    @Override
    String toStringSuffix() {
        return toStringOffset(this.offset);
    }
}
