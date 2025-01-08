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

package walkingkooka.compare;

import org.junit.jupiter.api.Assertions;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Mixin that includes methods to help testing of a {@link Comparator}.
 */
public interface ComparatorTesting extends Testing {

    default <T> void compareAndCheckLess(final Comparator<T> comparator,
                                         final T value1,
                                         final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparators.LESS);
    }

    default <T> void compareAndCheckEquals(final Comparator<T> comparator,
                                           final T value) {
        this.compareAndCheckEquals(
            comparator,
            value,
            value
        );
    }

    default <T> void compareAndCheckEquals(final Comparator<T> comparator,
                                           final T value1,
                                           final T value2) {
        this.compareAndCheck(
            comparator,
            value1,
            value2,
            Comparators.EQUAL
        );
    }

    default <T> void compareAndCheckMore(final Comparator<T> comparator,
                                         final T value1,
                                         final T value2) {
        this.compareAndCheck(
            comparator,
            value1,
            value2,
            Comparators.MORE
        );
    }

    default <T> void compareAndCheck(final Comparator<T> comparator,
                                     final T value1,
                                     final T value2,
                                     final int expected) {
        this.compareAndCheck0(comparator, value1, value2, expected);
        this.compareAndCheck0(comparator, value2, value1, -expected);
    }

    default <T> void compareAndCheck0(final Comparator<T> comparator,
                                      final T value1,
                                      final T value2,
                                      final int expected) {
        final int result = comparator.compare(value1, value2);
        if (Comparators.normalize(expected) != Comparators.normalize(result)) {
            this.checkEquals(
                expected,
                result,
                () -> "comparing " + CharSequences.quoteIfChars(value1) + " with " + CharSequences.quoteIfChars(value2) + " returned wrong result using " + comparator
            );
        }
    }

    @SuppressWarnings("unchecked")
    default <T> void comparatorArraySortAndCheck(final Comparator<T> comparator,
                                                 final T... values) {
        if (values.length % 2 != 0) {
            Assertions.fail("Expected even number of values " + Arrays.toString(values));
        }

        final List<T> list = Lists.of(values);

        final List<T> unsorted = new ArrayList<>(list.subList(0, values.length / 2));
        final List<T> sorted = list.subList(values.length / 2, values.length);
        unsorted.sort(comparator);

        this.checkEquals(
            sorted,
            unsorted,
            () -> "sort " + unsorted
        );
    }
}
