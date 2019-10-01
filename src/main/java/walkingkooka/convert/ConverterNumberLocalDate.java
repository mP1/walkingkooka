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

/**
 * A {@link Converter} that handles converting any {@link Number} to a {@link LocalDate}.
 */
final class ConverterNumberLocalDate extends ConverterNumber<LocalDate> {

    /**
     * Creates a new instance with the given date offset.
     * A value of zero is 1/1/1970.
     */
    static ConverterNumberLocalDate with(final long offset) {
        return new ConverterNumberLocalDate(offset);
    }

    /**
     * Private ctor
     */
    private ConverterNumberLocalDate(final long offset) {
        super();
        this.offset = offset;
    }

    @Override
    Either<LocalDate, String> bigDecimal(final BigDecimal value) {
        return this.localDate(value.longValueExact());
    }

    @Override
    Either<LocalDate, String> bigInteger(final BigInteger value) {
        return this.localDate(value.longValueExact());
    }

    @Override
    Either<LocalDate, String> doubleValue(final Double value) {
        final double doubleValue = value;
        return value != (long) doubleValue ?
                this.failConversion(value, LocalDate.class) :
                this.localDate((long) doubleValue);
    }

    @Override
    Either<LocalDate, String> longValue(final Long value) {
        return this.localDate(value);
    }

    private Either<LocalDate, String> localDate(final long value) {
        return Either.left(LocalDate.ofEpochDay(value + this.offset));
    }

    private final long offset;

    @Override
    Class<LocalDate> targetType() {
        return LocalDate.class;
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
