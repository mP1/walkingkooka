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
import walkingkooka.text.CharSequences;

/**
 * Converts an object instance to a requested target {@link Class reflect}.
 */
public interface Converter {

    /**
     * Queries whether this {@link Converter} supports converting to the requested {@link Class reflect}.
     */
    boolean canConvert(final Object value,
                       final Class<?> type,
                       final ConverterContext context);

    /**
     * Converts the given value to the requested reflect returning an {@link Either} with {@link Either#leftValue()} holding
     * the result or {@link Either#rightValue()} holding an failure message.
     */
    <T> Either<T, String> convert(final Object value,
                                  final Class<T> type,
                                  final ConverterContext context);

    /**
     * Converts the given value to the {@link Class target type} or throws a {@link ConversionException}
     */
    default <T> T convertOrFail(final Object value,
                                final Class<T> target,
                                final ConverterContext context) {
        final Either<T, String> converted = this.convert(value, target, context);
        if (converted.isRight()) {
            throw new ConversionException(converted.rightValue());
        }

        return converted.leftValue();
    }

    default <T> Either<T, String> failConversion(final Object value,
                                                 final Class<T> target) {
        return Either.right("Failed to convert " + CharSequences.quoteIfChars(value) + " to " + target.getName());
    }

    default <T> Either<T, String> failConversion(final Object value,
                                                 final Class<T> target,
                                                 final Throwable cause) {
        return Either.right("Failed to convert " + CharSequences.quoteIfChars(value) + " to " + target.getName() + " " + cause.getMessage());
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
