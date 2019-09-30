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

import java.util.Objects;

/**
 * A base {@link Converter} template.
 */
abstract class Converter2 implements Converter {

    /**
     * Package private to limit sub classing.
     */
    Converter2() {
        super();
    }

    @Override
    public final <T> T convert(final Object value,
                               final Class<T> type,
                               final ConverterContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        if (false == this.canConvert(value, type, context)) {
            failConversion(value, type);
        }

        return this.convert0(value, type, context);
    }

    abstract <T> T convert0(final Object value,
                            final Class<T> type,
                            final ConverterContext context);

    /**
     * Helper that performs the last step by converting a {@link Number} to another {@link Number sub class}.
     */
    final <N> N convertToNumber(final Number number,
                                final Class<N> type,
                                final ConverterContext context,
                                final Object value) {
        try {
            return ConverterNumberNumber.INSTANCE
                    .convert(number,
                            type,
                            context);
        } catch (final FailedConversionException cause) {
            // necessary so the exception has the correct value and type.
            throw new FailedConversionException(value, type, cause);
        }
    }
}
