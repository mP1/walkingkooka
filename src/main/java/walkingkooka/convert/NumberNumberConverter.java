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
import java.util.Objects;

/**
 * A {@link Converter} which handles converting {@link Number} to other number types or nothing at all if the target is number.
 */
final class NumberNumberConverter implements Converter{

    static NumberNumberConverter with(final Converter bigDecimal,
                                      final Converter bigInteger,
                                      final Converter doubleConverter,
                                      final Converter longConverter) {
        Objects.requireNonNull(bigDecimal, "bigDecimal");
        Objects.requireNonNull(bigInteger, "bigInteger");
        Objects.requireNonNull(doubleConverter, "doubleConverter");
        Objects.requireNonNull(longConverter, "longConverter");

        return new NumberNumberConverter(bigDecimal, bigInteger, doubleConverter, longConverter);
    }

    private NumberNumberConverter(Converter bigDecimal, Converter bigInteger, Converter doubleConverter, Converter longConverter) {
        this.bigDecimal = bigDecimal;
        this.bigInteger = bigInteger;
        this.doubleConverter = doubleConverter;
        this.longConverter = longConverter;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return value instanceof Number && (
                type == BigDecimal.class ||
                type == BigInteger.class ||
                type == Double.class ||
                type == Long.class ||
                type == Number.class);
    }

    @Override
    public <T> T convert(final Object value, final Class<T> type, final ConverterContext context) {
        return type == Number.class && value instanceof Number ?
               type.cast(value) :
               type == BigDecimal.class ?
               this.bigDecimal.convert(value, type, context) :
               type == BigInteger.class ?
               this.bigInteger.convert(value, type, context) :
               type == Double.class ?
               this.doubleConverter.convert(value, type, context) :
               type == Long.class ?
               this.longConverter.convert(value, type, context) :
               this.failConversion(value, type);
    }

    private final Converter bigDecimal;
    private final Converter bigInteger;
    private final Converter doubleConverter;
    private final Converter longConverter;

    @Override
    public String toString() {
        return "Number->Number";
    }
}
