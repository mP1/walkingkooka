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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A {@link Converter} which handles converting {@link Number} to other number types or nothing at all if the target is number.
 */
final class NumberNumberConverter extends Converter2 {

    /**
     * Singleton
     */
    final static NumberNumberConverter INSTANCE = new NumberNumberConverter();

    private NumberNumberConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        return value instanceof Number && (
                type == BigDecimal.class ||
                        type == BigInteger.class ||
                        type == Byte.class ||
                        type == Float.class ||
                        type == Double.class ||
                        type == Integer.class ||
                        type == Long.class ||
                        type == Number.class ||
                        type == Short.class);
    }

    @Override
    <T> T convert0(final Object value,
                   final Class<T> type,
                   final ConverterContext context) {
        try {
            return type == Number.class ?
                    type.cast(value) :
                    this.convertNonNumber(value, type);
        } catch (final ConversionException rethrow) {
            throw rethrow;
        } catch (final RuntimeException cause) {
            this.failConversion(value, type, cause);
            return null;
        }
    }

    private <T> T convertNonNumber(final Object value,
                                   final Class<T> type) {
        final NumberNumberConverterNumberTypeVisitorNumberVisitor<?> visitor = NumberNumberConverterNumberTypeVisitor.visitor(type);
        if (null == visitor) {
            this.failConversion(value, type);
        }
        return type.cast(visitor.convert(Cast.to(value)));
    }

    @Override
    public String toString() {
        return "Number->Number";
    }
}
