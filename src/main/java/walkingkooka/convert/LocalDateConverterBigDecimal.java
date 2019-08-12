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
 * Converts {@link java.time.LocalDate} to {@link BigDecimal}
 */
final class LocalDateConverterBigDecimal extends LocalDateConverter<BigDecimal> {

    /**
     * Factory that creates an instance with the given date offset.
     * A value of zero = 1/1/1970,.
     */
    static LocalDateConverterBigDecimal with(final long offset) {
        return new LocalDateConverterBigDecimal(offset);
    }

    /**
     * Private ctor use factory
     */
    private LocalDateConverterBigDecimal(final long offset) {
        super(offset);
    }

    @Override
    Class<BigDecimal> targetType() {
        return BigDecimal.class;
    }

    @Override
    BigDecimal fromLongValue(final long value) {
        return BigDecimal.valueOf(value);
    }
}