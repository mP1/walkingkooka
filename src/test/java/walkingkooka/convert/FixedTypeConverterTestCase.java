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

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class FixedTypeConverterTestCase<C extends Converter, T> extends ConverterTestCase<C> {

    FixedTypeConverterTestCase() {
        super();
    }

    @Override
    public void convertAndCheck(final Object value) {
        assertSame(value, this.convertAndCheck(value, this.onlySupportedType(), value));
    }

    final T convertAndCheck(final Object value, final T expected) {
        final Class<T> targetType = this.onlySupportedType();
        return targetType.cast(this.convertAndCheck(this.createConverter(),
                value,
                targetType,
                expected));
    }

    final void convertFails(final Object value) {
        this.convertFails(value, this.onlySupportedType());
    }

    abstract Class<T> onlySupportedType();
}
