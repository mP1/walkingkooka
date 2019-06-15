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

package walkingkooka.collect.enumeration;

import walkingkooka.collect.list.Lists;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link Enumeration}
 */
public interface EnumerationTesting<E extends Enumeration<T>, T> extends ToStringTesting<E>, TypeNameTesting<E> {

    E createEnumeration();

    default void checkDoesntHasMoreElements(final Enumeration<?> enumeration) {
        assertEquals(false, enumeration.hasMoreElements(), () -> enumeration.toString());
    }

    default void checkHasMoreElements(final Enumeration<T> enumeration) {
        assertEquals(true, enumeration.hasMoreElements(), () -> enumeration.toString());
    }

    default void checkNextElementFails(final Enumeration<?> enumeration) {
        assertThrows(NoSuchElementException.class, () -> {
            enumeration.nextElement();
        });
    }

    default void enumerateUsingHasMoreElementAndCheck(final T... expected) {
        this.enumerateUsingHasMoreElementsAndCheck(this.createEnumeration(), expected);
    }

    default <U> void enumerateUsingHasMoreElementsAndCheck(final Enumeration<U> enumeration,
                                                           final U... expected) {
        int i = 0;
        final List<U> consumed = Lists.array();
        while (enumeration.hasMoreElements()) {
            final U next = enumeration.nextElement();
            assertEquals(expected[i], next, "element " + i);
            consumed.add(next);
            i++;
        }
        assertEquals(Lists.of(expected), consumed);

        this.checkNextElementFails(enumeration);
    }

    default void enumerateAndCheck(final T... expected) {
        this.enumerateAndCheck(this.createEnumeration(), expected);
    }

    default <U> void enumerateAndCheck(final Enumeration<U> enumeration,
                                       final U... expected) {
        int i = 0;
        final List<U> consumed = Lists.array();
        final int expectedCount = expected.length;
        while (i < expectedCount) {
            final U next = enumeration.nextElement();
            assertEquals(expected[i], next, "element " + i);
            consumed.add(next);
            i++;
        }
        this.checkNextElementFails(enumeration);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Enumeration.class.getSimpleName();
    }
}
