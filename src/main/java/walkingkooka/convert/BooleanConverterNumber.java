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

/**
 * Handles converting {@link Number} to {@link Boolean}.
 */
final class BooleanConverterNumber extends FixedSourceTypeConverter<Boolean> {

    /**
     * Singleton
     */
    final static BooleanConverterNumber INSTANCE = new BooleanConverterNumber();

    /**
     * Private ctor use singleton.
     */
    private BooleanConverterNumber() {
        super();
    }

    @Override
    Class<Boolean> sourceType() {
        return Boolean.class;
    }

    @Override
    boolean isTargetType(final Class<?> type) {
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
    <T> T convert1(final Boolean value,
                   final Class<T> type,
                   final ConverterContext context) {
        return type.cast(BooleanConverterNumberNumberTypeVisitor.convert(value, type));
    }

    @Override
    String toStringSuffix() {
        return Number.class.getSimpleName();
    }
}
