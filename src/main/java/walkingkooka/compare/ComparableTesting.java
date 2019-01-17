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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting;

import static org.junit.Assert.assertEquals;

/**
 * An interface for testing {@link Comparable comparables}. Many compareTo methods are
 * available that compare andassert the result.
 */
public interface ComparableTesting<C extends Comparable<C> & HashCodeEqualsDefined> extends HashCodeEqualsDefinedTesting<C> {

    @Test
    default void testCompareToNullFails() {
        this.compareToFails(null, NullPointerException.class);
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

    static void compareResultCheck(final int expected, final int actual) {
        compareResultCheck(null, expected, actual);
    }

    static void compareResultCheck(final String message, final int expected, final int actual) {
        if (Comparables.normalize(expected) != Comparables.normalize(actual)) {
            assertEquals(message, expected, actual);
        }
    }

    /**
     * When true indicates that comparison equality matches object equality.
     */
    default boolean compareAndEqualsMatch() {
        return true;
    }

    // compareFails

    default void compareToFails(final C comparable) {
        this.compareToFails(this.createComparable(), comparable);
    }

    default void compareToFails(final C comparable,
                                final Class<? extends Throwable> expected) {
        this.compareToFails(this.createComparable(), comparable, expected);
    }

    default void compareToFails(final C comparable1, final C comparable2) {
        this.compareToFails(comparable1, comparable2, null);
    }

    default void compareToFails(final C comparable1, final C comparable2,
                                final Class<? extends Throwable> expected) {
        try {
            comparable1.compareTo(comparable2);
            Assert.fail();
        } catch (final Exception cause) {
            if (null != expected) {
                if (false == expected.equals(cause.getClass())) {
                    assertEquals("expected " + comparable1 + " when compared with " + comparable2
                            + " to fail", expected, cause);
                }
            }
        }
    }
}
