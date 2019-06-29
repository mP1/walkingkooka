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

/**
 * A {@link Converter} that handles converting {@link BigDecimal} to {@link Boolean}. It is not possible to use
 * {@link BooleanConverter} with a {@link BigDecimal#ZERO} because equality testing will fail with {@link BigDecimal} values
 * with extra zeroes.
 */
final class BigDecimalBooleanConverter extends FixedTargetTypeConverter<Boolean> {

    /**
     * Singleton
     */
    final static BigDecimalBooleanConverter INSTANCE = new BigDecimalBooleanConverter();

    /**
     * Private ctor use singleton
     */
    private BigDecimalBooleanConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return value instanceof BigDecimal;
    }

    @Override
    Boolean convert1(final Object value, final Class<Boolean> type, final ConverterContext context) {
        return BigDecimal.class.cast(value).signum() != 0;
    }

    @Override
    Class<Boolean> targetType() {
        return Boolean.class;
    }

    @Override
    public String toString() {
        return "BigDecimal->Boolean";
    }
}
