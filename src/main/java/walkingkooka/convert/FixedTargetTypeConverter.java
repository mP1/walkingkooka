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

import java.util.Objects;

/**
 * A base {@link Converter} that only accepts one target type.
 */
abstract class FixedTargetTypeConverter<T> implements Converter {

    /**
     * Package private to limit sub classing.
     */
    FixedTargetTypeConverter() {
        super();
    }

    @Override
    public final <T> T convert(final Object value, final Class<T> type, final ConverterContext context) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        if (!this.canConvert(value, type, context)) {
            failConversion(value, type);
        }

        return this.convert0(value, type, context);
    }

    final <TT> TT convert0(final Object value, final Class<TT> type, final ConverterContext context) {
        return Cast.to(this.targetType() == value.getClass() ?
                value :
                this.convert1(value, Cast.to(type), context));
    }

    abstract T convert1(final Object value, final Class<T> type, final ConverterContext context);

    final T failConversion(final Object value) {
        return this.failConversion(value, this.targetType());
    }

    final T failConversion(final Object value, final Throwable cause) {
        return this.failConversion(value, this.targetType(), cause);
    }

    abstract Class<T> targetType();

    /**
     * Returns the {@link String} as a signed offset including a plus or minus when the value is non zero.
     */
    static String toStringOffset(final long offset) {
        return 0 == offset ?
                "" :
                toStringOffset0(offset);
    }

    private static String toStringOffset0(final long offset) {
        final StringBuilder b = new StringBuilder();
        b.append('(');

        if (offset > 0) {
            b.append('+');
        }
        b.append(offset);
        b.append(')');

        return b.toString();
    }
}
