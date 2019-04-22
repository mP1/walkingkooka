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
 * Handles converting one of {@link BigDecimal}, {@link BigInteger}, {@link Double} or {@link Long} to {@link BigInteger}
 * without any loss.
 */
final class NumberBigIntegerConverter extends NumberConverter<BigInteger> {

    final static NumberBigIntegerConverter INSTANCE = new NumberBigIntegerConverter();

    private NumberBigIntegerConverter() {
        super();
    }

    @Override
    BigInteger bigDecimal(final BigDecimal value) {
        return value.toBigIntegerExact();
    }

    @Override
    BigInteger bigInteger(final BigInteger value) {
        return value;
    }

    @Override
    BigInteger doubleValue(final Double value) {
        return this.bigDecimal(new BigDecimal(value));
    }

    @Override
    BigInteger longValue(final Long value) {
        return BigInteger.valueOf(value);
    }

    @Override
    Class<BigInteger> targetType() {
        return BigInteger.class;
    }

    @Override
    String toStringPrefix() {
        return "";
    }

    @Override
    String toStringSuffix() {
        return "";
    }
}
