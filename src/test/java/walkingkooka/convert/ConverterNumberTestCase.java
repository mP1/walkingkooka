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

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class ConverterNumberTestCase<C extends ConverterNumber<T>, T> extends ConverterTestCase2<C> {

    ConverterNumberTestCase() {
        super();
    }

    @Test
    public void testDoubleNanFails() {
        this.convertFails2(Double.NaN);
    }

    @Test
    public void testDoublePositiveInfinityFails() {
        this.convertFails2(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testDoubleNegativeInfinityFails() {
        this.convertFails2(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testDoubleMaxFails() {
        this.convertFails2(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleMinFails() {
        this.convertFails2(Double.MIN_VALUE);
    }

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    public void convertAndCheck(final Object value) {
        assertSame(value, this.convertAndCheck(value, this.targetType(), value));
    }

    final T convertAndCheck2(final Object value, final T expected) {
        final Class<T> targetType = this.targetType();
        return targetType.cast(this.convertAndCheck(this.createConverter(),
                value,
                targetType,
                expected));
    }

    final void convertFails2(final Object value) {
        this.convertFails(value, this.targetType());
    }

    abstract Class<T> targetType();

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return Converter.class.getSimpleName() + Number.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return this.targetType().getSimpleName();
    }
}
