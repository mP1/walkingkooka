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

package walkingkooka.test;

import org.junit.jupiter.api.Test;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixin that contains a variety of helpers that test class structure related items.
 */
public interface ClassTesting<T> extends TestSuiteNameTesting<T> {

    @Test
    default void testClassVisibility() {
        final Class<?> type = this.type();
        final MemberVisibility visibility = Fake.class.isAssignableFrom(type)  ?
                MemberVisibility.PUBLIC :
                this.typeVisibility();

        assertEquals(visibility,
                MemberVisibility.get(type),
                () -> type.getName() + " visibility");
    }

    @Test
    default void testAllMethodsVisibility() {
        ClassMethodTesting.testAllMethodsVisibility(this.type());
    }

    MemberVisibility typeVisibility();
}
