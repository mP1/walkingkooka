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

/**
 * Handles converting {@link Number} to {@link Boolean}.
 */
final class ConverterBooleanNumber extends Converter2 {

    /**
     * Singleton
     */
    final static ConverterBooleanNumber INSTANCE = new ConverterBooleanNumber();

    /**
     * Private ctor use singleton.
     */
    private ConverterBooleanNumber() {
        super();
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return value instanceof Boolean && this.isTargetType(type);
    }

    private boolean isTargetType(final Class<?> type) {
        return type == BigDecimal.class ||
                type == BigInteger.class ||
                type == Byte.class ||
                type == Double.class ||
                type == Float.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Short.class;
    }

    @Override
    <T> Either<T, String> convert0(final Object value, Class<T> type, ConverterContext context) {
        return Either.left(type.cast(ConverterBooleanNumberNumberTypeVisitor.convert((Boolean) value, type)));
    }

    @Override
    public String toString() {
        return "Boolean->Number";
    }
}
