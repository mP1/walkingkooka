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

import walkingkooka.type.PublicStaticHelper;

import java.util.List;

/**
 * Factory methods for numerous {@link Converter converters}.
 */
public final class Converters implements PublicStaticHelper {

    /**
     * [@see BooleanStringConverter}
     */
    public static Converter booleanString(final String trueValue, final String falseValue) {
        return BooleanStringConverter.with(trueValue, falseValue);
    }

    /**
     * {@see ConverterCollection}
     */
    public static Converter collection(final List<Converter> converters) {
        return ConverterCollection.with(converters);
    }

    /**
     * [@see NumberBigDecimalConverter}
     */
    public static Converter numberBigDecimal() {
        return NumberBigDecimalConverter.INSTANCE;
    }

    /**
     * [@see NumberBigIntegerConverter}
     */
    public static Converter numberBigInteger() {
        return NumberBigIntegerConverter.INSTANCE;
    }

    /**
     * [@see NumberDoubleConverter}
     */
    public static Converter numberDouble() {
        return NumberDoubleConverter.INSTANCE;
    }

    /**
     * [@see NumberLongConverter}
     */
    public static Converter numberLong() {
        return NumberLongConverter.INSTANCE;
    }

    /**
     * {@see NumberNumberConverter}
     */
    public static Converter numberNumber(final Converter bigDecimal,
                                      final Converter bigInteger,
                                      final Converter doubleConverter,
                                      final Converter longConverter) {
        return NumberNumberConverter.with(bigDecimal, bigInteger, doubleConverter, longConverter);
    }

    /**
     * [@see StringConverter}
     */
    public static Converter string() {
        return StringConverter.INSTANCE;
    }

    /**
     * [@see StringBooleanConverter}
     */
    public static Converter stringBoolean() {
        return StringBooleanConverter.INSTANCE;
    }

    /**
     * Stop creation
     */
    private Converters() {
        throw new UnsupportedOperationException();
    }
}
