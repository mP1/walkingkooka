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
import walkingkooka.compare.ComparableTesting2Test.TestComparable;

public final class ComparableTesting2Test implements ComparableTesting2<TestComparable> {

    // compareToAndCheckLess............................................................................................

    @Test
    public void testCompareToAndCheckLess() {
        this.compareToAndCheckLess(this.more());
    }

    @Test
    public void testCompareToAndCheckLessFails() {
        this.mustFail(() -> this.compareToAndCheckLess(this.less()));
    }

    // compareToAndCheckEquals...........................................................................................

    @Test
    public void testCompareToAndCheckEquals() {
        this.compareToAndCheckEquals(this.createComparable());
    }

    @Test
    public void testCompareToAndCheckEqualsFails() {
        this.mustFail(() -> this.compareToAndCheckEquals(this.more()));
    }

    // compareToAndCheckMore............................................................................................

    @Test
    public void testCompareToAndCheckMore() {
        this.compareToAndCheckMore(this.less());
    }

    @Test
    public void testCompareToAndCheckMoreFails() {
        this.mustFail(() -> this.compareToAndCheckMore(this.more()));
    }

    // compareToAndCheckNotEquals.......................................................................................

    @Test
    public void testCompareToAndCheckNotEquals() {
        this.compareToAndCheckNotEquals(
            this.less()
        );
    }

    @Test
    public void testCompareToAndCheckNotEqualsFails() {
        this.mustFail(
            () -> this.compareToAndCheckNotEquals(this.createComparable())
        );
    }

    // helpers..........................................................................................................

    @Override
    public TestComparable createComparable() {
        return new TestComparable("jkl456");
    }

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
