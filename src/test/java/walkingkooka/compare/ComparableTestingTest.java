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

public final class ComparableTestingTest implements ComparableTesting {

    // compareToAndCheckLess............................................................................................

    @Test
    public void testCompareToAndCheckLess2() {
        this.compareToAndCheckLess("A", "B");
    }

    @Test
    public void testCompareToAndCheckLess2Fails() {
        this.mustFail(() -> this.compareToAndCheckLess("B", "A"));
    }

    // compareToAndCheckEquals...........................................................................................

    @Test
    public void testCompareToAndCheckEquals2() {
        this.compareToAndCheckEquals("abc", "abc");
    }

    @Test
    public void testCompareToAndCheckEquals2Fails() {
        this.mustFail(() -> this.compareToAndCheckEquals("xyz", "abc"));
    }

    // compareToAndCheckMore............................................................................................

    @Test
    public void testCompareToAndCheckMore2() {
        this.compareToAndCheckMore("B", "A");
    }

    @Test
    public void testCompareToAndCheckMore2Fails() {
        this.mustFail(() -> this.compareToAndCheckMore("A", "B"));
    }

    // compareToAndCheckNotEquals...........................................................................................

    @Test
    public void testCompareToAndCheckNotEquals() {
        this.compareToAndCheckNotEquals(
            "abc",
            "xyz"
        );
    }

    @Test
    public void testCompareToAndCheckNotEqualsFails() {
        this.mustFail(
            () -> this.compareToAndCheckNotEquals(
                "xyz",
                "xyz"
            )
        );
    }

    // compareToArraySortAndCheck.......................................................................................

    @Test
    public void testCompareToArraySortAndCheckUnevenParameterCount() {
        this.mustFail(() -> this.compareToArraySortAndCheck("A"));
    }

    @Test
    public void testCompareToArraySortAndCheckUnevenParameterCount2() {
        this.mustFail(() -> this.compareToArraySortAndCheck("A", "B", "C"));
    }

    @Test
    public void testCompareToArraySortAndCheck() {
        this.compareToArraySortAndCheck("A", "Z", "B", "D", "C",
            "A", "B", "C", "D", "Z");
    }

    @Test
    public void testCompareToArraySortAndCheckFails() {
        this.mustFail(() -> this.compareToArraySortAndCheck("A", "Z", "B", "D", "C",
            "Z", "D", "C", "B", "A"));
    }

    // compareResultCheck ..............................................................................................

    @Test
    public void testCompareResultCheckMore() {
        this.compareResultCheck("message", 1, 2);
    }

    @Test
    public void testCompareResultCheckMore2() {
        this.compareResultCheck("message", 2, 2);
    }

    @Test
    public void testCompareResultCheckLess() {
        this.compareResultCheck("message", -11, -2);
    }

    @Test
    public void testCompareResultCheckLess2() {
        this.compareResultCheck("message", -1, -1);
    }

    @Test
    public void testCompareResultCheckEqual() {
        this.compareResultCheck("message", 0, 0);
    }

    @Test
    public void testCompareResultCheckMoreFail() {
        this.mustFail(() -> this.compareResultCheck("message", 1, 0));
    }

    @Test
    public void testCompareResultCheckMoreFail2() {
        this.mustFail(() -> this.compareResultCheck("message", 1, -2));
    }

    @Test
    public void testCompareResultCheckEqualFail() {
        this.mustFail(() -> this.compareResultCheck("message", 0, +2));
    }

    @Test
    public void testCompareResultCheckEqualFail2() {
        this.mustFail(() -> this.compareResultCheck("message", 0, -2));
    }

    @Test
    public void testCompareResultCheckLessFail() {
        this.mustFail(() -> this.compareResultCheck("message", -1, 0));
    }

    @Test
    public void testCompareResultCheckLessFail2() {
        this.mustFail(() -> this.compareResultCheck("message", -1, +2));
    }

    // helpers..........................................................................................................

    public TestComparable less() {
        return new TestComparable("abc123");
    }

    public TestComparable more() {
        return new TestComparable("xyz789");
    }

    private void mustFail(final Runnable run) {
        boolean fail = false;
        try {
            run.run();
        } catch (final AssertionFailedError expected) {
            fail = true;
        }
        this.checkEquals(true, fail);
    }

    static class TestComparable implements Comparable<TestComparable> {

        TestComparable(final String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        public boolean equals(final Object other) {
            return this == other || other instanceof TestComparable && equals0((TestComparable) other);
        }

        private boolean equals0(final TestComparable other) {
            return this.compareTo(other) == 0;
        }

        @Override
        public int compareTo(final TestComparable other) {
            return this.value.compareTo(other.value);
        }

        private final String value;

        @Override
        public String toString() {
            return this.value;
        }
    }
}
