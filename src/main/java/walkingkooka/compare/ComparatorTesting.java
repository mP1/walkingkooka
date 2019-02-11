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

package walkingkooka.compare;

import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixin that inclues methods to help testing of a {@link Comparator}.
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

    default void compareAndCheckLess(final Comparator<T> comparator,
                                     final T value1,
                                     final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparables.LESS);
    }

    default void compareAndCheckEqual(final T value) {
        this.compareAndCheckEqual(this.createComparator(), value);
    }

    default void compareAndCheckEqual(final Comparator<T> comparator,
                                      final T value) {
        this.compareAndCheckEqual(comparator, value, value);
    }

    default void compareAndCheckEqual(final T value1,
                                      final T value2) {
        this.compareAndCheckEqual(this.createComparator(), value1, value2);
    }

    default void compareAndCheckEqual(final Comparator<T> comparator,
                                      final T value1,
                                      final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparables.EQUAL);
    }

    default void compareAndCheckMore(final T value1,
                                     final T value2) {
        this.compareAndCheckMore(this.createComparator(), value1, value2);
    }

    default void compareAndCheckMore(final Comparator<T> comparator,
                                     final T value1,
                                     final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparables.MORE);
    }

    default void compareAndCheck(final Comparator<T> comparator,
                                 final T value1,
                                 final T value2,
                                 final int expected) {
        this.compareAndCheck0(comparator, value1, value2, expected);
        this.compareAndCheck0(comparator, value2, value1, -expected);
    }

    default void compareAndCheck0(final Comparator<T> comparator,
                                  final T value1,
                                  final T value2,
                                  final int expected) {
        final int result = comparator.compare(value1, value2);
        if (false == ComparatorTesting.isEqual(expected, result)) {
            assertEquals(expected,
                    result,
                    () -> "comparing " + CharSequences.quoteIfChars(value1) + " with " + CharSequences.quoteIfChars(value2) + " returned wrong result using " + comparator);
        }
    }

    static void checkEquals(final int expected,
                            final int actual) {
        checkEquals(null, expected, actual);
    }

    static void checkEquals(final String message,
                            final int expected,
                            final int actual) {
        if (false == ComparatorTesting.isEqual(expected, actual)) {
            assertEquals(expected, actual, message);
        }
    }

    static boolean isEqual(final int expected,
                           final int actual) {
        return Comparables.normalize(expected) == Comparables.normalize(actual);
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
