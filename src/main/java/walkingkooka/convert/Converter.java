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

/**
 * Converts an object instance to a requested target {@link Class type}.
 */
public interface Converter {

    /**
     * Queries whether this {@link Converter} supports converting to the requested {@link Class type}.
     */
    boolean canConvert(final Object value,
                       final Class<?> type,
                       final ConverterContext context);

    /**
     * Converts the object to the request type.
     */
    <T> T convert(final Object value,
                  final Class<T> type,
                  final ConverterContext context);

    default void failIfUnsupportedType(final Object value,
                                       final Class<?> target,
                                       final ConverterContext context) {
        if (!this.canConvert(value, target, context)) {
            this.failConversion(value, target);
        }
    }

    default <TT> TT failConversion(final Object value,
                                   final Class<TT> target) {
        throw new FailedConversionException(value, target);
    }

    default <TT> TT failConversion(final Object value,
                                   final Class<TT> target,
                                   final Throwable cause) {
        throw new FailedConversionException(value, target, cause);
    }

    default Converter setToString(final String toString) {
        return Converters.customToString(this, toString);
    }

    /**
     * Chains a successful convert from this {@link Converter} to another {@link Converter} to complete conversion using two steps.
     */
    default Converter then(final Class<?> intermediateTargetType,
                           final Converter last) {
        return Converters.chain(this, intermediateTargetType, last);
    }
}
