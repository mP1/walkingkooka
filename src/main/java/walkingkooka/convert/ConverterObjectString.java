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

/**
 * A {@link Converter} that invokes {@link Object#toString()} to convert any value into a {@link String}
 */
final class ConverterObjectString extends Converter2 {

    /**
     * Singleton
     */
    final static ConverterObjectString INSTANCE = new ConverterObjectString();

    private ConverterObjectString() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return String.class == type;
    }

    @Override
    <T> Either<T, String> convert0(final Object value,
                                   final Class<T> type,
                                   final ConverterContext context) {
        return Either.left(type.cast(value.toString()));
    }

    @Override
    public String toString() {
        return "*->String";
    }
}
