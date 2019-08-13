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
import java.time.LocalTime;

/**
 * Converts a {@link LocalTime} into the requested {@link Number} type.
 */
final class LocalTimeConverterNumber extends LocalTimeConverter {

    /**
     * Singleton
     */
    final static LocalTimeConverterNumber INSTANCE = new LocalTimeConverterNumber();

    /**
     * Private ctor use singleton
     */
    private LocalTimeConverterNumber() {
    }

    @Override
    boolean isTargetType(final Class<?> type) {
        return BigDecimal.class == type ||
                BigInteger.class == type ||
                Byte.class == type ||
                Double.class == type ||
                Float.class == type ||
                Integer.class == type ||
                Long.class == type ||
                Number.class == type ||
                Short.class == type;
    }

    @Override <T>
    T convertFromLocalTime(final long seconds,
                           final long nano,
                           final LocalTime localTime,
                           final Class<T> type,
                           final ConverterContext context) {
        try {
            return BIGDECIMAL_TO_NUMBER.convert(BigDecimal.valueOf(seconds).add(BigDecimal.valueOf(1.0 * nano / Converters.NANOS_PER_SECOND)),
                    type,
                    context);
        } catch (final FailedConversionException cause) {
            // necessary so the exception has the correct value and type.
            throw new FailedConversionException(localTime, type, cause);
        }
    }

    /**
     * Used to perform the final conversion part.
     */
    private final static Converter BIGDECIMAL_TO_NUMBER = Converters.numberNumber();

    @Override
    String toStringSuffix() {
        return Number.class.getSimpleName();
    }
}
