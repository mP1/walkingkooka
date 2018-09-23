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

public final class LocalDateBigDecimalConverterTest extends LocalDateConverterTestCase<LocalDateBigDecimalConverter, BigDecimal> {

    @Override
    LocalDateBigDecimalConverter createConverter(final long offset) {
        return LocalDateBigDecimalConverter.with(offset);
    }

    @Override
    protected Class<BigDecimal> onlySupportedType() {
        return BigDecimal.class;
    }

    @Override
    final BigDecimal value(final long value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    protected Class<LocalDateBigDecimalConverter> type() {
        return LocalDateBigDecimalConverter.class;
    }
}
