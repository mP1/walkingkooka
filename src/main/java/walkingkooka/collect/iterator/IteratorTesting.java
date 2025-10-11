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

package walkingkooka.collect.iterator;

import walkingkooka.collect.list.Lists;
import walkingkooka.test.Testing;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link Iterator}.
 */
public interface IteratorTesting extends Testing {

    default void hasNextCheckFalse(final Iterator<?> iterator) {
        this.checkEquals(
            false,
            iterator.hasNext(),
            "iterator hasNext should have returned true: " + iterator
        );
    }

    default void hasNextCheckTrue(final Iterator<?> iterator) {
        this.hasNextCheckTrue(
            iterator,
            "iterator hasNext should have returned true: " + iterator
        );
    }

    default void hasNextCheckTrue(final Iterator<?> iterator,
                                  final String message) {
        this.checkEquals(
            true,
            iterator.hasNext(),
            message
        );
    }

    default void nextFails(final Iterator<?> iterator) {
        this.nextFails(
            "iterator.next must throw NoSuchElementException",
            iterator
        );
    }

    default void nextFails(final String message, final Iterator<?> iterator) {
        assertThrows(
            NoSuchElementException.class,
            iterator::next
        );
    }

    default void removeWithoutNextFails(final Iterator<?> iterator) {
        assertThrows(
            IllegalStateException.class,
            iterator::remove
        );
    }

    default void removeUnsupportedFails(final Iterator<?> iterator) {
        assertThrows(
            UnsupportedOperationException.class,
            iterator::remove
        );
    }

    @SuppressWarnings("unchecked")
    default <T> void iterateUsingHasNextAndCheck(final Iterator<T> iterator,
                                                 final T... expected) {
        this.iterateUsingHasNextAndCheck(
            iterator,
            Lists.of(expected)
        );
    }

    @SuppressWarnings("unchecked")
    default <T> void iterateUsingHasNextAndCheck(final Iterator<T> iterator,
                                                 final List<T> expected) {
        Objects.requireNonNull(iterator, "iterator");

        int i = 0;
        final List<T> consumed = Lists.array();

        while (iterator.hasNext()) {
            final T next = iterator.next();

            if (i < expected.size()) {
                final int ii = i;
                this.checkEquals(
                    expected.get(i),
                    next,
                    () -> "element " + ii
                );
            }
            consumed.add(next);
            i++;
        }
        this.checkEquals(
            expected,
            consumed,
            iterator::toString
        );
        this.nextFails(iterator);
    }

    @SuppressWarnings("unchecked")
    default <T> void iterateAndCheck(final Iterator<T> iterator,
                                     final T... expected) {

        this.iterateAndCheck(
            iterator,
            Lists.of(expected)
        );
    }

    default <T> void iterateAndCheck(final Iterator<T> iterator,
                                     final List<T> expected) {

        int i = 0;
        final List<T> consumed = Lists.array();
        final int expectedCount = expected.size();

        while (i < expectedCount) {
            final T next = iterator.next();

            final int ii = i;
            this.checkEquals(
                expected.get(i),
                next,
                () -> "element " + ii
            );

            consumed.add(next);
            i++;
        }

        this.checkEquals(
            expected,
            consumed,
            iterator::toString
        );
        this.nextFails(iterator);
    }
}
