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

import walkingkooka.Cast;
import walkingkooka.Either;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;

/**
 * Converts a {@link LocalTime} into the requested {@link Number} reflect.
 */
final class ConverterLocalTimeNumber extends ConverterLocalTime {

    /**
     * Singleton
     */
    final static ConverterLocalTimeNumber INSTANCE = new ConverterLocalTimeNumber();

    /**
     * Private ctor use singleton
     */
    private ConverterLocalTimeNumber() {
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

    @Override
    <T> Either<T, String> convertFromLocalTime(final long seconds,
                                               final long nano,
                                               final LocalTime localTime,
                                               final Class<T> type,
                                               final ConverterContext context) {
        return this.convertToNumber(BigDecimal.valueOf(seconds).add(BigDecimal.valueOf(1.0 * nano / Converters.NANOS_PER_SECOND)),
                Cast.to(type),
                context,
                localTime);
    }

    @Override
    public String toString() {
        return "LocalTime->Number";
    }
}
