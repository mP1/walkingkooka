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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A {@link Converter} that handles converting {@link Number} to another {@link Number} type.
 */
abstract class NumberConverter<T> extends FixedTargetTypeConverter<T> {

    NumberConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return value instanceof Number && this.targetType() == type;
    }

    /**
     * Accepts an assumed {@link Number} and dispatches to one of the sub classes of {@link Number} which then
     * call one of four abstract methods.
     */
    @Override
    final T convert0(final Object value, final Class<T> type, final ConverterContext context) {
        try {
            return NumberConverterNumberVisitor.convert(this,
                    Number.class.cast(value),
                    type);
        } catch (final ArithmeticException | NumberFormatException fail) {
            return this.failConversion(value, type, fail);
        }
    }

    abstract T bigDecimal(final BigDecimal value);

    abstract T bigInteger(final BigInteger value);

    final T floatValue(final Float value) {
        return this.doubleValue(value.doubleValue());
    }

    abstract T doubleValue(final Double value);

    final T number(final Number value) {
        return this.longValue(value.longValue());
    }

    abstract T longValue(final Long value);

    @Override
    public final String toString() {
        return this.toStringPrefix() +
                "BigDecimal|BigInteger|Byte|Short|Integer|Long|Float|Double->" +
                this.targetType().getSimpleName() +
                this.toStringSuffix();
    }

    abstract String toStringPrefix();

    abstract String toStringSuffix();
}
