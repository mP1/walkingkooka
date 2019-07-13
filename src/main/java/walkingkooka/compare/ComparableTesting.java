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
import walkingkooka.test.HashCodeEqualsDefinedTesting;

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
public interface ComparableTesting<C extends Comparable<C> & HashCodeEqualsDefined> extends HashCodeEqualsDefinedTesting<C> {

    @Test
    default void testCompareToNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createComparable().compareTo(null);
        });
    }

    @Test
    default void testCompareToSelfGivesZero() {
        final C comparable = this.createComparable();
        this.compareToAndCheckEqual(comparable, comparable);
    }

    // helpers


    @Override
    default C createObject() {
        return this.createComparable();
    }

    C createComparable();

    default void compareToAndCheckLess(final C comparable) {
        this.compareToAndCheck(comparable, Comparables.LESS);
    }

    default void compareToAndCheckLess(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparables.LESS);
    }

    default void compareToAndCheckEqual(final C comparable) {
        this.compareToAndCheck(comparable, Comparables.EQUAL);
    }

    default void compareToAndCheckEqual(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparables.EQUAL);
    }

    default void compareToAndCheckMore(final C comparable) {
        this.compareToAndCheck(comparable, Comparables.MORE);
    }

    default void compareToAndCheckMore(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparables.MORE);
    }

    default void compareToAndCheck(final C comparable, final int expected) {
        this.compareToAndCheck(this.createComparable(), comparable, expected);
    }

    default void compareToAndCheck(final C comparable1,
                                   final C comparable2,
                                   final int expected) {
        compareResultCheck(comparable1 + " " + comparable2,
                expected,
                comparable1.compareTo(comparable2));
        if (Comparables.EQUAL != expected) {
            compareResultCheck(
                    "Exchanging parameters and comparing did not return an inverted result.",
                    -expected,
                    comparable2.compareTo(comparable1));
            if (this.compareAndEqualsMatch()) {
                this.checkNotEquals(comparable1, comparable2);
            }
        } else {
            if (this.compareAndEqualsMatch()) {
                this.checkEquals(comparable1, comparable2);
            }
        }
    }

    default void compareToArraySortAndCheck(final C... values) {
        if (values.length % 2 != 0) {
            Assertions.fail("Expected even number of values " + Arrays.toString(values));
        }

        final List<C> list = Lists.of(values);

        final List<C> unsorted = new ArrayList<>(list.subList(0, values.length / 2));
        final List<C> sorted = list.subList(values.length / 2, values.length);
        unsorted.sort(Comparator.naturalOrder());

        assertEquals(sorted,
                unsorted,
                () -> "sort " + unsorted);
    }

    static void compareResultCheck(final int expected, final int actual) {
        compareResultCheck(null, expected, actual);
    }

    static void compareResultCheck(final String message, final int expected, final int actual) {
        if (Comparables.normalize(expected) != Comparables.normalize(actual)) {
            assertEquals(expected, actual, message);
        }
    }

    /**
     * When true indicates that comparison equality matches object equality.
     */
    default boolean compareAndEqualsMatch() {
        return true;
    }
}
