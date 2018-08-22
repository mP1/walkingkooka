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

import java.math.BigInteger;

/**
 * Converts {@link java.time.LocalDate} to {@link BigInteger}
 */
final class LocalDateBigIntegerConverter extends LocalDateConverter<BigInteger> {

    /**
     * Singleton
     */
    static final LocalDateBigIntegerConverter INSTANCE = new LocalDateBigIntegerConverter();

    /**
     * Private ctor use singleton
     */
    private LocalDateBigIntegerConverter() {
    }

    @Override
    Class<BigInteger> targetType() {
        return BigInteger.class;
    }

    @Override
    BigInteger convert3(final long value) {
        return BigInteger.valueOf(value);
    }
}
