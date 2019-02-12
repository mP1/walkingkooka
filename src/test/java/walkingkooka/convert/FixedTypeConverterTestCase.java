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

import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class FixedTypeConverterTestCase<C extends Converter, T> extends ClassTestCase<C>
        implements ConverterTesting<C> {

    FixedTypeConverterTestCase() {
        super();
    }

    @Override
    public void convertAndCheck(final Object value) {
        assertSame(value, this.convertAndCheck(value, this.onlySupportedType(), value));
    }

    final Object convertAndCheck(final Object value, final Object expected) {
        return this.convertAndCheck(this.createConverter(), value, this.onlySupportedType(), expected);
    }

    final void convertFails(final Object value) {
        this.convertFails(value, this.onlySupportedType());
    }

    abstract Class<T> onlySupportedType();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
