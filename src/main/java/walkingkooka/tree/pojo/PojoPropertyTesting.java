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

package walkingkooka.tree.pojo;

import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixin interface to assist testing of {@link PojoProperty
 */
public interface PojoPropertyTesting<P extends PojoProperty> extends ToStringTesting<P>,
        TypeNameTesting<P> {

    default void getAndCheck(final Object instance, final Object value) {
        this.getAndCheck(this.createPojoProperty(), instance, value);
    }

    default void getAndCheck(final P property, final Object instance, final Object value) {
        assertEquals(value, property.get(instance), () -> "wrong value returned when calling " + property + " get");
    }

    default Object setAndCheck(final Object instance, final Object value) {
        return this.setAndCheck(this.createPojoProperty(), instance, value);
    }

    default Object setAndCheck(final P property, final Object instance, final Object value) {
        return property.set(instance, value);
    }

    P createPojoProperty();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return PojoProperty.class.getSimpleName();
    }
}
