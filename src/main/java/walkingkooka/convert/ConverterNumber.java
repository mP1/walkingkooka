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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A {@link Converter} that handles converting {@link Number} to another {@link Number} type.
 */
abstract class ConverterNumber<T> extends Converter2 {

    ConverterNumber() {
        super();
    }

    @Override
    public final boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return value instanceof Number && this.targetType() == type;
    }

    abstract Class<T> targetType();

    /**
     * Accepts an assumed {@link Number} and dispatches to one of the sub classes of {@link Number} which then
     * call one of four abstract methods.
     */
    @Override
    final <U> Either<U, String> convert0(final Object value,
                                         final Class<U> type,
                                         final ConverterContext context) {
        // T and U should be the same...
        Either<U, String> result;
        try {
            result = ConverterNumberNumberVisitor.convert(Cast.to(this),
                    (Number) value,
                    type);
        } catch (final Exception cause) {
            result = Either.right(cause.getMessage());
        }
        return result;
    }

    abstract Either<T, String> bigDecimal(final BigDecimal value);

    abstract Either<T, String> bigInteger(final BigInteger value);

    final Either<T, String> floatValue(final Float value) {
        return this.doubleValue(value.doubleValue());
    }

    abstract Either<T, String> doubleValue(final Double value);

    final Either<T, String> number(final Number value) {
        return this.longValue(value.longValue());
    }

    abstract Either<T, String> longValue(final Long value);

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.toStringPrefix() +
                "BigDecimal|BigInteger|Byte|Short|Integer|Long|Float|Double->" +
                this.targetType().getSimpleName() +
                this.toStringSuffix();
    }

    abstract String toStringPrefix();

    abstract String toStringSuffix();

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
