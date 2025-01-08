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

import java.util.Comparator;

public final class ComparatorTestingTest implements ComparatorTesting {

    private final static String LESS = "abc";
    private final static String VALUE = "jkl";
    private final static String MORE = "vwx";

    private final static Comparator<String> COMPARATOR = String.CASE_INSENSITIVE_ORDER;

    // compareAndCheckLess..............................................................................................

    @Test
    public void testCompareAndCheckLess() {
        this.compareAndCheckLess(COMPARATOR, VALUE, MORE);
    }

    @Test
    public void testCompareAndCheckLessFails() {
        this.mustFail(() -> this.compareAndCheckLess(COMPARATOR, VALUE, LESS));
    }

    // compareAndCheckEquals..............................................................................................

    @Test
    public void testCompareAndCheckEquals() {
        this.compareAndCheckEquals(COMPARATOR, VALUE, VALUE);
    }

    @Test
    public void testCompareAndCheckEqualsFails() {
        this.mustFail(() -> this.compareAndCheckEquals(COMPARATOR, VALUE, MORE));
    }

    // compareAndCheckMore..............................................................................................

    @Test
    public void testCompareAndCheckMore() {
        this.compareAndCheckMore(COMPARATOR, VALUE, LESS);
    }

    @Test
    public void testCompareAndCheckMoreFails() {
        this.mustFail(() -> this.compareAndCheckMore(COMPARATOR, VALUE, MORE));
    }

    // comparatorArraySortAndCheck.......................................................................................

    @Test
    public void testArraySortAndCheckUnevenParameterCount() {
        this.mustFail(() -> this.comparatorArraySortAndCheck(COMPARATOR, "A"));
    }

    @Test
    public void testArraySortAndCheckUnevenParameterCount2() {
        this.mustFail(() -> this.comparatorArraySortAndCheck(COMPARATOR, "A", "B", "C"));
    }

    @Test
    public void testArraySortAndCheckComparator() {
        this.comparatorArraySortAndCheck(
            COMPARATOR,
            "A", "Z", "B", "D", "C", "A", "B", "C", "D", "Z"
        );
    }

    @Test
    public void testArraySortAndCheckFails() {
        this.mustFail(
            () -> this.comparatorArraySortAndCheck(
                COMPARATOR,
                "A", "Z", "B", "D", "C", "Z", "D", "C", "B", "A"
            )
        );
    }

    // compareAndCheck ..............................................................................................

    @Test
    public void testCompareAndCheckAndMore() {
        this.compareAndCheck(
            COMPARATOR,
            VALUE,
            MORE,
            -1
        );
    }

    @Test
    public void testCompareAndCheckAndMore2() {
        this.compareAndCheck(
            COMPARATOR,
            VALUE,
            MORE,
            -2
        );
    }

    @Test
    public void testCompareAndCheckAndLess() {
        this.compareAndCheck(
            COMPARATOR,
            VALUE,
            LESS,
            1
        );
    }

    @Test
    public void testCompareAndCheckAndLess2() {
        this.compareAndCheck(
            COMPARATOR,
            VALUE,
            LESS,
            123
        );
    }

    @Test
    public void testCompareAndCheckAndEqual() {
        this.compareAndCheck(
            COMPARATOR,
            VALUE,
            VALUE,
            0
        );
    }

    @Test
    public void testCompareAndCheckAndMoreFail() {
        this.mustFail(
            () -> this.compareAndCheck(
                COMPARATOR,
                VALUE,
                LESS,
                0)
        );
    }

    @Test
    public void testCompareAndCheckAndMoreFail2() {
        this.mustFail(
            () -> this.compareAndCheck(
                COMPARATOR,
                VALUE,
                LESS,
                -2
            )
        );
    }

    @Test
    public void testCompareAndCheckAndEqualFail() {
        this.mustFail(
            () -> this.compareAndCheck(
                COMPARATOR,
                VALUE,
                LESS,
                -2
            )
        );
    }

    @Test
    public void testCompareAndCheckAndEqualFail2() {
        this.mustFail(
            () -> this.compareAndCheck(
                COMPARATOR,
                VALUE,
                LESS,
                -1
            )
        );
    }

    @Test
    public void testCompareAndCheckAndLessFail() {
        this.mustFail(
            () -> this.compareAndCheck(
                COMPARATOR,
                VALUE,
                MORE,
                0
            )
        );
    }

    @Test
    public void testCompareAndCheckLessFail2() {
        this.mustFail(
            () -> this.compareAndCheck(
                COMPARATOR,
                VALUE,
                MORE,
                1
            )
        );
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
}
