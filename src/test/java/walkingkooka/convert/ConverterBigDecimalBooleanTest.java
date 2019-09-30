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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public final class ConverterBigDecimalBooleanTest extends ConverterTestCase2<ConverterBigDecimalBoolean> {

    @Test
    public void testTrue() {
        this.convertAndCheck2(BigDecimal.ONE, true);
    }

    @Test
    public void testFalse() {
        this.convertAndCheck2(BigDecimal.ZERO, false);
    }

    @Test
    public void testExtraZeroesFalse() {
        this.convertAndCheck2(new BigDecimal("000000"), false);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "BigDecimal->Boolean");
    }

    @Override
    public ConverterBigDecimalBoolean createConverter() {
        return ConverterBigDecimalBoolean.INSTANCE;
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    private void convertAndCheck2(final BigDecimal value,
                                  final Boolean expected) {
        this.convertAndCheck(value, Boolean.class, expected);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterBigDecimalBoolean> type() {
        return ConverterBigDecimalBoolean.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return Converter.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return BigDecimal.class.getSimpleName() + Boolean.class.getSimpleName();
    }
}
