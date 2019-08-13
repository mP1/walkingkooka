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
 * A base {@link Converter} that assumes a single source value type.
 */
abstract class FixedSourceTypeConverter<S> implements Converter {

    /**
     * Package private to limit sub classing.
     */
    FixedSourceTypeConverter() {
        super();
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return this.sourceType().isInstance(value) && this.isTargetType(type);
    }

    abstract Class<S> sourceType();

    abstract boolean isTargetType(final Class<?> type);

    @Override
    public final <T> T convert(final Object value,
                               final Class<T> type,
                               final ConverterContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        if (!this.canConvert(value, type, context)) {
            failConversion(value, type);
        }

        return type.cast(this.convert0(this.sourceType().cast(value),
                type,
                context));
    }

    abstract <T> T convert0(final S value,
                            final Class<T> type,
                            final ConverterContext context);

    @Override
    public final String toString() {
        return this.sourceType().getSimpleName() + "->" + this.toStringSuffix();
    }

    abstract String toStringSuffix();
}
