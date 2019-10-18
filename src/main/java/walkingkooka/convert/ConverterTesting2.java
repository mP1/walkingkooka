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
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.test.ToStringTesting;

import static org.junit.jupiter.api.Assertions.assertSame;

public interface ConverterTesting2<C extends Converter> extends ConverterTesting,
        ToStringTesting<C>,
        TypeNameTesting<C> {

    C createConverter();

    ConverterContext createContext();

    default void convertAndCheck(final Object value) {
        assertSame(value, this.convertAndCheck(value, Cast.to(value.getClass()), value));
    }

    default <T> T convertAndCheck(final T value,
                                  final Class<T> target) {
        final T result = this.convertAndCheck(this.createConverter(), value, target, value);
        assertSame(value, result);
        return result;
    }

    default <T> T convertAndCheck(final Object value,
                                  final Class<T> target,
                                  final T expected) {
        return this.convertAndCheck(this.createConverter(), value, target, expected);
    }

    default <T> T convertAndCheck(final Object value,
                                  final Class<T> target,
                                  final ConverterContext context,
                                  final T expected) {
        return this.convertAndCheck(this.createConverter(), value, target, context, expected);
    }

    default <T> T convertAndCheck(final Converter converter,
                                  final Object value,
                                  final Class<T> target,
                                  final T expected) {
        return this.convertAndCheck(converter, value, target, this.createContext(), expected);
    }

    default void convertFails(final Object value, final Class<?> type) {
        this.convertFails(this.createConverter(), value, type);
    }

    default void convertFails(final Converter converter, final Object value, final Class<?> type) {
        this.convertFails(converter, value, type, this.createContext());
    }

    default <T> Either<T, String> convert(final T value) {
        return this.convert(value, Cast.to(value.getClass()));
    }

    default <T> Either<T, String> convert(final Object value,
                                          final Class<T> type) {
        return this.createConverter()
                .convert(value, type, this.createContext());
    }

    // TypeNameTesting .................................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Converter.class.getSimpleName();
    }
}
