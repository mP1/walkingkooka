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
    LocalDate bigDecimal(final BigDecimal value) {
        return this.localDate(value.longValueExact());
    }

    @Override
    LocalDate bigInteger(final BigInteger value) {
        return this.localDate(value.longValueExact());
    }

    @Override
    LocalDate doubleValue(final Double value) {
        final double doubleValue = value;
        if (value != (long) doubleValue) {
            this.failConversion(value);
        }
        return this.localDate((long) doubleValue);
    }

    @Override
    LocalDate longValue(final Long value) {
        return this.localDate(value);
    }

    private LocalDate localDate(final long value) {
        return LocalDate.ofEpochDay(value + this.offset);
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
