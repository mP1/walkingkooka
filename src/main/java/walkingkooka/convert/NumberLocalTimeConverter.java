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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;

/**
 * A {@link Converter} that takes a {@link Number} and converts it to a {@link LocalTime}.
 * The value is the number of seconds in a day.
 */
final class NumberLocalTimeConverter extends NumberConverter<LocalTime> {

    /**
     * Singleton
     */
    final static NumberLocalTimeConverter INSTANCE = new NumberLocalTimeConverter();

    /**
     * Private ctor
     */
    private NumberLocalTimeConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return value instanceof Number && LocalTime.class == type;
    }

    @Override
    LocalTime bigDecimal(final BigDecimal value) {
        final double doubleValue = value.doubleValue();
        if(0 != BigDecimal.valueOf(doubleValue).compareTo(value)) {
            this.failConversion(value);
        }

        return this.localTime(doubleValue);
    }

    @Override
    LocalTime bigInteger(final BigInteger value) {
        return this.localTime(value.longValueExact());
    }

    @Override
    LocalTime doubleValue(final Double value) {
        final double doubleValue = value;
        if(value != (long)doubleValue) {
            this.failConversion(value);
        }
        return this.localTime((long)doubleValue);
    }

    @Override
    LocalTime longValue(final Long value) {
        return this.localTime(value);
    }

    private LocalTime localTime(final double value) {
        final double nanos = value * Converters.NANOS_PER_SECOND;
        if(value!=nanos / Converters.NANOS_PER_SECOND) {
            this.failConversion(value);
        }

        return LocalTime.ofNanoOfDay((long)nanos);
    }

    private LocalTime localTime(final long value) {
        return LocalTime.ofSecondOfDay(value);
    }

    @Override
    String toStringPrefix() {
        return "";
    }

    @Override
    Class<LocalTime> targetType() {
        return LocalTime.class;
    }
}
