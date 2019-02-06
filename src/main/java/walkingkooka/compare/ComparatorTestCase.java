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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract public class ComparatorTestCase<C extends Comparator<T>, T>
        extends ClassTestCase<C>
        implements ToStringTesting<C>{

    protected ComparatorTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Comparator.class);
    }

    // helpers

    abstract protected C createComparator();

    final protected int compare(final T value, final T otherValue) {
        return this.createComparator().compare(value, otherValue);
    }

    final protected void compareAndCheckLess(final T value1, final T value2) {
        this.compareAndCheckLess(this.createComparator(), value1, value2);
    }

    final protected void compareAndCheckLess(final Comparator<T> comparator, final T value1,
                                             final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparables.LESS);
    }

    final protected void compareAndCheckEqual(final T value) {
        this.compareAndCheckEqual(this.createComparator(), value);
    }

    final protected void compareAndCheckEqual(final Comparator<T> comparator, final T value) {
        this.compareAndCheckEqual(comparator, value, value);
    }

    final protected void compareAndCheckEqual(final T value1, final T value2) {
        this.compareAndCheckEqual(this.createComparator(), value1, value2);
    }

    final protected void compareAndCheckEqual(final Comparator<T> comparator, final T value1,
                                              final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparables.EQUAL);
    }

    final protected void compareAndCheckMore(final T value1, final T value2) {
        this.compareAndCheckMore(this.createComparator(), value1, value2);
    }

    final protected void compareAndCheckMore(final Comparator<T> comparator, final T value1,
                                             final T value2) {
        this.compareAndCheck(comparator, value1, value2, Comparables.MORE);
    }

    private void compareAndCheck(final Comparator<T> comparator, final T value1, final T value2,
                                 final int expected) {
        this.compareAndCheck0(comparator, value1, value2, expected);
        this.compareAndCheck0(comparator, value2, value1, -expected);
    }

    private void compareAndCheck0(final Comparator<T> comparator, final T value1, final T value2,
                                  final int expected) {
        final int result = comparator.compare(value1, value2);
        if (false == ComparatorTestCase.isEqual(expected, result)) {
            assertEquals(expected,
                    result,
                    () -> "comparing " + this.toString(value1) + " with " + this.toString(value2) + " returned wrong result using " + comparator);
        }
    }

    private CharSequence toString(final T value) {
        return CharSequences.quoteIfChars(value);
    }

    public static void checkEquals(final int expected, final int actual) {
        checkEquals(null, expected, actual);
    }

    public static void checkEquals(final String message, final int expected, final int actual) {
        if (false == ComparatorTestCase.isEqual(expected, actual)) {
            assertEquals(expected, actual, message);
        }
    }

    private static boolean isEqual(final int expected, final int actual) {
        return Comparables.normalize(expected) == Comparables.normalize(actual);
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
