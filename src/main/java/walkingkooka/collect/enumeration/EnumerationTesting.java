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
import walkingkooka.test.Testing;

import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The {@link Enumeration} equivalent of {@link walkingkooka.collect.iterator.IteratorTesting}.
 */
public interface EnumerationTesting extends Testing {

    default void hasMoreElementsAndCheck(final Enumeration<?> enumeration,
                                         final boolean expected) {
        this.checkEquals(
            expected,
            enumeration.hasMoreElements(),
            () -> "hasMoreElements " + enumeration
        );
    }

    default <T> void nextElementAndCheck(final Enumeration<T> enumeration,
                                         final T expected) {
        this.checkEquals(
            expected,
            enumeration.nextElement(),
            () -> "nextElement " + enumeration
        );
    }

    default void nextElementFails(final Enumeration<?> enumeration) {
        assertThrows(
            NoSuchElementException.class,
            enumeration::nextElement
        );
    }

    default <T> void enumerateUsingHasMoreElementsAndCheck(final Enumeration<T> enumeration,
                                                           final T... expected) {
        Objects.requireNonNull(enumeration, "enumeration");

        int i = 0;
        final List<T> consumed = Lists.array();

        while (enumeration.hasMoreElements()) {
            final T next = enumeration.nextElement();

            if (i < expected.length) {
                final int ii = i;
                this.checkEquals(
                    expected[i],
                    next,
                    () -> "element " + ii
                );
            }
            consumed.add(next);
            i++;
        }
        this.checkEquals(
            Lists.of(expected),
            consumed,
            enumeration::toString
        );
        this.nextElementFails(enumeration);
    }

    @SuppressWarnings("unchecked")
    default <T> void enumerateAndCheck(final Enumeration<T> enumeration,
                                       final T... expected) {

        int i = 0;
        final List<T> consumed = Lists.array();
        final int expectedCount = expected.length;

        while (i < expectedCount) {
            final T next = enumeration.nextElement();

            final int ii = i;
            this.checkEquals(
                expected[i],
                next,
                () -> "element " + ii
            );

            consumed.add(next);
            i++;
        }

        this.checkEquals(
            Lists.of(expected),
            consumed,
            enumeration::toString
        );
        this.nextElementFails(enumeration);
    }
}
