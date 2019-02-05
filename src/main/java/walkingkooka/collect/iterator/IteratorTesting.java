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

package walkingkooka.collect.iterator;

import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link Iterator}.
 */
public interface IteratorTesting {

    default void checkHasNextFalse(final Iterator<?> iterator) {
        assertEquals(false,
                iterator.hasNext(),
                () -> "iterator hasNext should have returned true: " + iterator);
    }

    default void checkHasNextTrue(final Iterator<?> iterator) {
        this.checkHasNextTrue(iterator,
                "iterator hasNext should have returned true: " + iterator);
    }

    default void checkHasNextTrue(final Iterator<?> iterator, final String message) {
        assertEquals(true,
                iterator.hasNext(),
                message);
    }

    default void checkNextFails(final Iterator<?> iterator) {
        this.checkNextFails("iterator.next must throw NoSuchElementException", iterator);
    }

    default void checkNextFails(final String message, final Iterator<?> iterator) {
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    default void checkRemoveWithoutNextFails(final Iterator<?> iterator) {
        assertThrows(IllegalStateException.class, () -> {
            iterator.remove();
        });
    }

    default void checkRemoveUnsupportedFails(final Iterator<?> iterator) {
        assertThrows(UnsupportedOperationException.class, () -> {
            iterator.remove();
        });
    }

    default <T> void iterateUsingHasNextAndCheck(final Iterator<T> iterator,
                                                 final T... expected) {
        Objects.requireNonNull(iterator, "iterator");

        int i = 0;
        final List<T> consumed = Lists.array();

        while (iterator.hasNext()) {
            final T next = iterator.next();
            assertEquals(expected[i], next, "element " + i);
            consumed.add(next);
            i++;
        }
        assertEquals(Lists.of(expected),
                consumed,
                ()-> iterator.toString());
        this.checkNextFails(iterator);
    }

    default <T> void iterateAndCheck(final Iterator<T> iterator,
                                     final T... expected) {

        int i = 0;
        final List<T> consumed = Lists.array();
        final int expectedCount = expected.length;

        while (i < expectedCount) {
            final T next = iterator.next();
            assertEquals(expected[i], next, "element " + i);
            consumed.add(next);
            i++;
        }

        assertEquals(Lists.of(expected),
                consumed,
                ()-> iterator.toString());
        this.checkNextFails(iterator);
    }
}
