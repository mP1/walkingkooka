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
 */

package walkingkooka.compare;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.TestCase;

import static org.junit.Assert.assertEquals;

/**
 * A {@link TestCase} for testing {@link Comparable comparables}. Many compareTo methods are
 * available that compare andassert the result.
 */
abstract public class ComparableTestCase<C extends Comparable<C>> extends ClassTestCase {

    protected ComparableTestCase() {
        super();
    }

    @Test
    public void testNullFails() {
        this.compareToFails(null, NullPointerException.class);
    }

    @Test final public void testSelfGivesZero() {
        final C comparable = this.createComparable();
        this.compareToAndCheckEqual(comparable, comparable);
    }

    // helpers

    protected abstract C createComparable();

    final protected void compareToAndCheckLess(final C comparable) {
        this.compareToAndCheck(comparable, Comparables.LESS);
    }

    final protected void compareToAndCheckLess(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparables.LESS);
    }

    final protected void compareToAndCheckEqual(final C comparable) {
        this.compareToAndCheck(comparable, Comparables.EQUAL);
    }

    final protected void compareToAndCheckEqual(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparables.EQUAL);
    }

    final protected void compareToAndCheckMore(final C comparable) {
        this.compareToAndCheck(comparable, Comparables.MORE);
    }

    final protected void compareToAndCheckMore(final C comparable1, final C comparable2) {
        this.compareToAndCheck(comparable1, comparable2, Comparables.MORE);
    }

    final protected void compareToAndCheck(final C comparable, final int expected) {
        this.compareToAndCheck(this.createComparable(), comparable, expected);
    }

    final protected void compareToAndCheck(final C comparable1, final C comparable2,
                                           final int expected) {
        ComparableTestCase.checkResult(expected, comparable1.compareTo(comparable2));
        if (Comparables.EQUAL != expected) {
            ComparableTestCase.checkResult(
                    "Exchanging parameters and comparing did not return an inverted result.",
                    -expected,
                    comparable2.compareTo(comparable1));
        }
    }

    private static void checkResult(final int expected, final int actual) {
        ComparableTestCase.checkResult(null, expected, actual);
    }

    private static void checkResult(final String message, final int expected, final int actual) {
        if (Comparables.normalize(expected) != Comparables.normalize(actual)) {
            assertEquals(message, expected, actual);
        }
    }

    // compareFails

    final protected void compareToFails(final C comparable) {
        this.compareToFails(this.createComparable(), comparable);
    }

    final protected void compareToFails(final C comparable,
                                        final Class<? extends Throwable> expected) {
        this.compareToFails(this.createComparable(), comparable, expected);
    }

    final protected void compareToFails(final C comparable1, final C comparable2) {
        this.compareToFails(comparable1, comparable2, null);
    }

    final protected void compareToFails(final C comparable1, final C comparable2,
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
