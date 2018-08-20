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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A {@link Converter} that takes a {@link Number} and converts it to a {@link LocalDateTime}.
 * The integer value becomes the days, and the fraction is a value of a whole day.
 */
final class NumberLocalDateTimeConverter extends NumberConverter<LocalDateTime> {

    /**
     * Singleton
     */
    final static NumberLocalDateTimeConverter INSTANCE = new NumberLocalDateTimeConverter();

    /**
     * Private ctor
     */
    private NumberLocalDateTimeConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return value instanceof Number && LocalDateTime.class == type;
    }

    @Override
    LocalDateTime bigDecimal(final BigDecimal value) {
        final double doubleValue = value.doubleValue();
        if(0 != BigDecimal.valueOf(doubleValue).compareTo(value)) {
            this.failConversion(value);
        }

        return this.localDateTime(doubleValue);
    }

    @Override
    LocalDateTime bigInteger(final BigInteger value) {
        return this.localDateTime(value.longValueExact());
    }

    @Override
    LocalDateTime doubleValue(final Double value) {
        final double doubleValue = value;
        if(value != (long)doubleValue) {
            this.failConversion(value);
        }
        return this.localDateTime((long)doubleValue);
    }

    @Override
    LocalDateTime longValue(final Long value) {
        return this.localDateTime(value);
    }

    private LocalDateTime localDateTime(final double value) {
        final double days = Math.floor(value);

        return localDateTime((int)days, value - days);
    }

    private LocalDateTime localDateTime(final long value) {
        return localDateTime(value, 0);
    }

    private LocalDateTime localDateTime(final long day, final double fraction) {
        return LocalDateTime.of(
                LocalDate.ofEpochDay(day),
                LocalTime.ofNanoOfDay((long)(fraction * Converters.NANOS_PER_DAY)));
    }

    @Override
    String toStringPrefix() {
        return "";
    }

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }
}
