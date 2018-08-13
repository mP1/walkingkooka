/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.convert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A {@link Converter} that knows how to convert {@link String} to {@link BigDecimal}.
 * Requests for all other types will fail.
 */
abstract class NumberConverter<T extends Number> extends FixedTypeConverter<T> {

    NumberConverter() {
        super();
    }

    @Override final T convert1(final Object from) {
        try {
            return from instanceof BigDecimal ?
                    this.bigDecimal((BigDecimal) from) :
                    from instanceof BigInteger ?
                            this.bigInteger((BigInteger) from) :
                            from instanceof Double ?
                                    this.doubleValue((Double) from) :
                                    from instanceof Long ?
                                            this.longValue((Long) from) :
                                            this.failConversion(from);
        } catch(final ArithmeticException | NumberFormatException fail) {
            return this.failConversion(from);
        }
    }

    abstract T bigDecimal(final BigDecimal value);

    abstract T bigInteger(final BigInteger value);

    abstract T doubleValue(final double value);

    abstract T longValue(final long value);

    @Override
    public final String toString() {
        return "BigDecimal|BigInteger|Double|Long->" + this.onlySupportedType().getSimpleName();
    }
}
