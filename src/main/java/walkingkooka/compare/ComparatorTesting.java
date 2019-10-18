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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixin that includes methods to help testing of a {@link Comparator}.
 */
public interface ComparatorTesting<C extends Comparator<T>, T>
        extends ToStringTesting<C>,
        TypeNameTesting<C> {

    C createComparator();

    default int compare(final T value,
                        final T otherValue) {
        return this.createComparator().compare(value, otherValue);
    }

    default void compareAndCheckLess(final T value1,
                                     final T value2) {
        this.compareAndCheckLess(this.createComparator(), value1, value2);
    }

    default <TT> void compareAndCheckLess(final Comparator<TT> comparator,
                                          final TT value1,
                                          final TT value2) {
        this.compareAndCheck(comparator, value1, value2, Comparators.LESS);
    }

    default void compareAndCheckEquals(final T value) {
        this.compareAndCheckEquals(this.createComparator(), value);
    }

    default <TT> void compareAndCheckEquals(final Comparator<TT> comparator,
                                            final TT value) {
        this.compareAndCheckEquals(comparator, value, value);
    }

    default void compareAndCheckEquals(final T value1,
                                       final T value2) {
        this.compareAndCheckEquals(this.createComparator(), value1, value2);
    }

    default <TT> void compareAndCheckEquals(final Comparator<TT> comparator,
                                            final TT value1,
                                            final TT value2) {
        this.compareAndCheck(comparator, value1, value2, Comparators.EQUAL);
    }

    default void compareAndCheckMore(final T value1,
                                     final T value2) {
        this.compareAndCheckMore(this.createComparator(), value1, value2);
    }

    default <TT> void compareAndCheckMore(final Comparator<TT> comparator,
                                          final TT value1,
                                          final TT value2) {
        this.compareAndCheck(comparator, value1, value2, Comparators.MORE);
    }

    default <TT> void compareAndCheck(final Comparator<TT> comparator,
                                      final TT value1,
                                      final TT value2,
                                      final int expected) {
        this.compareAndCheck0(comparator, value1, value2, expected);
        this.compareAndCheck0(comparator, value2, value1, -expected);
    }

    default <TT> void compareAndCheck0(final Comparator<TT> comparator,
                                       final TT value1,
                                       final TT value2,
                                       final int expected) {
        final int result = comparator.compare(value1, value2);
        if (Comparators.normalize(expected) != Comparators.normalize(result)) {
            assertEquals(expected,
                    result,
                    () -> "comparing " + CharSequences.quoteIfChars(value1) + " with " + CharSequences.quoteIfChars(value2) + " returned wrong result using " + comparator);
        }
    }

    @SuppressWarnings("unchecked")
    default void comparatorArraySortAndCheck(final T... values) {
        this.comparatorArraySortAndCheck(this.createComparator(), values);
    }

    @SuppressWarnings("unchecked")
    default <TT> void comparatorArraySortAndCheck(final Comparator<TT> comparator,
                                                  final TT... values) {
        if (values.length % 2 != 0) {
            Assertions.fail("Expected even number of values " + Arrays.toString(values));
        }

        final List<TT> list = Lists.of(values);

        final List<TT> unsorted = new ArrayList<>(list.subList(0, values.length / 2));
        final List<TT> sorted = list.subList(values.length / 2, values.length);
        unsorted.sort(comparator);

        assertEquals(sorted,
                unsorted,
                () -> "sort " + unsorted);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Comparator.class.getSimpleName();
    }
}
