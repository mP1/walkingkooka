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

public abstract class NumberConverterTestCase<C extends NumberConverter<T>, T> extends FixedTypeConverterTestCase<C, T> {

    @Test
    public void testDoubleNanFails() {
        this.convertFails(Double.NaN);
    }

    @Test
    public void testDoublePositiveInfinityFails() {
        this.convertFails(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testDoubleNegativeInfinityFails() {
        this.convertFails(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testDoubleMaxFails() {
        this.convertFails(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleMinFails() {
        this.convertFails(Double.MIN_VALUE);
    }

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return Number.class.getSimpleName() + Converter.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return this.onlySupportedType().getSimpleName();
    }
}
