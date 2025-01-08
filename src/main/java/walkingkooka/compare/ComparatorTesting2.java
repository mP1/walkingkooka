
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

import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Comparator;

/**
 * Mixin that includes methods to help testing of a {@link Comparator}.
 */
public interface ComparatorTesting2<C extends Comparator<T>, T>
    extends ComparatorTesting,
    ToStringTesting<C>,
    TypeNameTesting<C> {

    C createComparator();

    default int compare(final T value,
                        final T otherValue) {
        return this.createComparator().compare(value, otherValue);
    }

    default void compareAndCheckLess(final T value1,
                                     final T value2) {
        this.compareAndCheckLess(
            this.createComparator(),
            value1,
            value2
        );
    }

    default void compareAndCheckEquals(final T value) {
        this.compareAndCheckEquals(
            this.createComparator(),
            value
        );
    }

    default void compareAndCheckEquals(final T value1,
                                       final T value2) {
        this.compareAndCheckEquals(
            this.createComparator(),
            value1,
            value2
        );
    }

    default void compareAndCheckMore(final T value1,
                                     final T value2) {
        this.compareAndCheckMore(
            this.createComparator(),
            value1,
            value2
        );
    }

    default void compareAndCheck(final T value1,
                                 final T value2,
                                 final int expected) {
        final Comparator<T> comparator = this.createComparator();

        this.compareAndCheck0(comparator, value1, value2, expected);
        this.compareAndCheck0(comparator, value2, value1, -expected);
    }

    @SuppressWarnings("unchecked")
    default void comparatorArraySortAndCheck(final T... values) {
        this.comparatorArraySortAndCheck(
            this.createComparator(),
            values
        );
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
