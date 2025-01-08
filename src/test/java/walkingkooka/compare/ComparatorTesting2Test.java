
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

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import walkingkooka.Cast;

import java.util.Comparator;

public final class ComparatorTesting2Test implements ComparatorTesting2<Comparator<String>, String> {

    private final static String LESS = "abc";
    private final static String VALUE = "jkl";
    private final static String MORE = "vwx";

    private final static Comparator<String> COMPARATOR = String.CASE_INSENSITIVE_ORDER;

    // compare..........................................................................................................

    @Test
    public void testCompare() {
        this.checkEquals(
            COMPARATOR.compare(VALUE, MORE),
            this.compare(VALUE, MORE)
        );
    }

    // compareAndCheckLess..............................................................................................

    @Test
    public void testCompareAndCheckLess() {
        this.compareAndCheckLess(VALUE, MORE);
    }

    @Test
    public void testCompareAndCheckLessFails() {
        this.mustFail(() -> this.compareAndCheckLess(VALUE, LESS));
    }

    @Test
    public void testComparatorCompareAndCheckLessFails() {
        this.mustFail(
            () -> this.compareAndCheckLess(VALUE, LESS)
        );
    }

    // compareAndCheckEquals..............................................................................................

    @Test
    public void testCompareAndCheckEquals() {
        this.compareAndCheckEquals(VALUE);
    }

    @Test
    public void testCompareAndCheckEquals2() {
        this.compareAndCheckEquals(VALUE, VALUE);
    }

    @Test
    public void testCompareAndCheckEqualsFails() {
        this.mustFail(
            () -> this.compareAndCheckEquals(VALUE, MORE)
        );
    }

    @Test
    public void testComparatorCompareAndCheckEquals() {
        this.compareAndCheckEquals(VALUE, VALUE);
    }

    @Test
    public void testComparatorCompareAndCheckEqualsFails() {
        this.mustFail(
            () -> this.compareAndCheckEquals(VALUE, MORE)
        );
    }

    // compareAndCheckMore..............................................................................................

    @Test
    public void testCompareAndCheckMore() {
        this.compareAndCheckMore(VALUE, LESS);
    }

    @Test
    public void testCompareAndCheckMoreFails() {
        this.mustFail(
            () -> this.compareAndCheckMore(VALUE, MORE)
        );
    }

    @Test
    public void testComparatorCompareAndCheckMoreFails() {
        this.mustFail(
            () -> this.compareAndCheckMore(VALUE, MORE)
        );
    }

    // comparatorArraySortAndCheck.......................................................................................

    @Test
    public void testComparatorArraySortAndCheckUnevenParameterCount() {
        this.mustFail(() -> this.comparatorArraySortAndCheck(COMPARATOR, "A"));
    }

    @Test
    public void testComparatorArraySortAndCheckUnevenParameterCount2() {
        this.mustFail(() -> this.comparatorArraySortAndCheck(COMPARATOR, "A", "B", "C"));
    }

    @Test
    public void testComparatorArraySortAndCheck() {
        this.comparatorArraySortAndCheck("A", "Z", "B", "D", "C",
            "A", "B", "C", "D", "Z");
    }

    @Test
    public void testComparatorArraySortAndCheckComparator() {
        this.comparatorArraySortAndCheck(COMPARATOR,
            "A", "Z", "B", "D", "C",
            "A", "B", "C", "D", "Z");
    }

    @Test
    public void testComparatorArraySortAndCheckFails() {
        this.mustFail(() -> this.comparatorArraySortAndCheck(COMPARATOR,
            "A", "Z", "B", "D", "C",
            "Z", "D", "C", "B", "A"));
    }

    // compareAndCheck ..............................................................................................

    @Test
    public void testCompareAndCheckAndMore() {
        this.compareAndCheck(
            VALUE,
            MORE,
            -1);
    }

    @Test
    public void testCompareAndCheckAndMore2() {
        this.compareAndCheck(
            VALUE,
            MORE,
            -2
        );
    }

    @Test
    public void testCompareAndCheckAndLess() {
        this.compareAndCheck(
            VALUE,
            LESS,
            1);
    }

    @Test
    public void testCompareAndCheckAndLess2() {
        this.compareAndCheck(
            VALUE,
            LESS,
            123);
    }

    @Test
    public void testCompareAndCheckAndEqual() {
        this.compareAndCheck(
            VALUE,
            VALUE,
            0
        );
    }

    @Override
    public void testCheckToStringOverridden() {

    }

    // helper...........................................................................................................

    private void mustFail(final Runnable run) {
        boolean fail = false;
        try {
            run.run();
        } catch (final AssertionFailedError expected) {
            fail = true;
        }
        this.checkEquals(true, fail);
    }

    // ComparatorTesting................................................................................................

    @Override
    public Comparator<String> createComparator() {
        return String.CASE_INSENSITIVE_ORDER;
    }

    @Override
    public Class<Comparator<String>> type() {
        return Cast.to(Comparator.class);
    }
}
