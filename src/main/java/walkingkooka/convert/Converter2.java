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

import java.util.Objects;

/**
 * A base {@link Converter} template which most sub classes will require.
 */
abstract class Converter2 implements Converter {

    /**
     * Package private to limit sub classing.
     */
    Converter2() {
        super();
    }

    @Override
    public final <T> Either<T, String> convert(final Object value,
                                               final Class<T> type,
                                               final ConverterContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        return this.canConvert(value, type, context) ?
                this.convert0(value, type, context) :
                this.failConversion(value, type);
    }

    /**
     * Template method that is only called with a value that has already passed a {@link #canConvert(Object, Class, ConverterContext)
     * test and should convert successfully.
     */
    abstract <T> Either<T, String> convert0(final Object value,
                                            final Class<T> type,
                                            final ConverterContext context);

    /**
     * Helper that performs the last step by converting a {@link Number} to another {@link Number sub class}.
     */
    final <N> Either<N, String> convertToNumber(final Number number,
                                                final Class<N> type,
                                                final ConverterContext context,
                                                final Object value) {
        return ConverterNumberNumber.INSTANCE
                    .convert(number,
                            type,
                            context);
    }
}
