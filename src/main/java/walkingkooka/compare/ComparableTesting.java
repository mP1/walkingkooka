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
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An interface for testing {@link Comparable comparables}. Many compareTo methods are
 * available that compare andassert the result.
 */
public interface ComparableTesting<C extends Comparable<C> & HashCodeEqualsDefined> extends HashCodeEqualsDefinedTesting2<C> {

    @Test
    default void testCompareToNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createComparable().compareTo(null);
        });
    }

    @Test
    default void testCompareToSelfGivesZero() {
        final C comparable = this.createComparable();
        this.compareToAndCheckEquals(comparable, comparable);

        if (this.compareAndEqualsMatch()) {
            this.checkEquals(comparable, comparable);
        }
    }

    // helpers..........................................................................................................

    @Override
    default C createObject() {
        return this.createComparable();
    }

    C createComparable();

    default void compareToAndCheckLess(final C comparable) {
        this.compareToAndCheck(comparable, Comparators.LESS);

        if (this.compareAndEqualsMatch()) {
            this.checkNotEquals(this.createComparable(), comparable);
        }
    }

    default <CC extends Comparable<CC>> void compareToAndCheckLess(final CC comparable1, final CC comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparators.LESS);
    }

    default void compareToAndCheckEquals(final C comparable) {
        this.compareToAndCheck(comparable, Comparators.EQUAL);

        if (this.compareAndEqualsMatch()) {
            this.checkEquals(comparable, comparable);
        }
    }

    default <CC extends Comparable<CC>> void compareToAndCheckEquals(final CC comparable1, final CC comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparators.EQUAL);
    }

    default void compareToAndCheckMore(final C comparable) {
        this.compareToAndCheck(comparable, Comparators.MORE);

        if (this.compareAndEqualsMatch()) {
            this.checkNotEquals(this.createComparable(), comparable);
        }
    }

    default <CC extends Comparable<CC>> void compareToAndCheckMore(final CC comparable1, final CC comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparators.MORE);
    }

    default void compareToAndCheck(final C comparable, final int expected) {
        this.compareToAndCheck(this.createComparable(), comparable, expected);

        if (Comparators.EQUAL == expected && this.compareAndEqualsMatch()) {
            this.checkEquals(this.createComparable(), comparable);
        }
    }

    default <CC extends Comparable<CC>> void compareToAndCheck(final CC comparable1,
                                                               final CC comparable2,
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

    default <CC extends Comparable<CC>> void compareToArraySortAndCheck(final CC... values) {
        if (values.length % 2 != 0) {
            Assertions.fail("Expected even number of values " + Arrays.toString(values));
        }

        final List<CC> list = Lists.of(values);

        final List<CC> unsorted = new ArrayList<>(list.subList(0, values.length / 2));
        final List<CC> sorted = list.subList(values.length / 2, values.length);
        unsorted.sort(Comparator.naturalOrder());

        assertEquals(sorted,
                unsorted,
                () -> "sort " + unsorted);
    }

    default void compareResultCheck(final String message, final int expected, final int actual) {
        if (Comparators.normalize(expected) != Comparators.normalize(actual)) {
            assertEquals(expected, actual, message);
        }
    }

    /**
     * When true indicates that {@link Object#equals(Object)} and {@link Comparable#compareTo(Object)} == 0 must match.
     */
    default boolean compareAndEqualsMatch() {
        return true;
    }
}
