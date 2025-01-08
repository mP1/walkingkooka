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
import walkingkooka.HashCodeEqualsDefinedTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * An interface for testing {@link Comparable comparables}. Many compareTo methods are
 * available that compare andcheck the result.
 */
public interface ComparableTesting extends HashCodeEqualsDefinedTesting {

    default <C extends Comparable<C>> void compareToAndCheckLess(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparators.LESS);
    }

    default <C extends Comparable<C>> void compareToAndCheckEquals(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparators.EQUAL);
    }

    default <C extends Comparable<C>> void compareToAndCheckMore(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparators.MORE);
    }

    default <C extends Comparable<C>> void compareToAndCheckNotEquals(final C value1,
                                                                      final C value2) {
        final int result = value1.compareTo(value2);
        if (Comparators.EQUAL == Comparators.normalize(result)) {
            this.checkNotEquals(
                result,
                result,
                () -> CharSequences.quoteIfChars(value1) + " should not be EQUAL to " + CharSequences.quoteIfChars(value2)
            );
        }
    }

    default <C extends Comparable<C>> void compareToAndCheck(final C comparable1,
                                                             final C comparable2,
                                                             final int expected) {
        this.compareResultCheck(comparable1 + " " + comparable2,
            expected,
            comparable1.compareTo(comparable2));
        if (Comparators.EQUAL != expected) {
            this.compareResultCheck("Swapping parameters and comparing did not return an inverted result.",
                -expected,
                comparable2.compareTo(comparable1));
        }
    }

    @SuppressWarnings("unchecked")
    default <C extends Comparable<C>> void compareToArraySortAndCheck(final C... values) {
        if (values.length % 2 != 0) {
            Assertions.fail("Expected even number of values " + Arrays.toString(values));
        }

        final List<C> list = Lists.of(values);

        final List<C> unsorted = new ArrayList<>(list.subList(0, values.length / 2));
        final List<C> sorted = list.subList(values.length / 2, values.length);
        unsorted.sort(Comparator.naturalOrder());

        this.checkEquals(sorted,
            unsorted,
            () -> "sort " + unsorted);
    }

    default void compareResultCheck(final String message, final int expected, final int actual) {
        if (Comparators.normalize(expected) != Comparators.normalize(actual)) {
            this.checkEquals(expected, actual, message);
        }
    }
}
