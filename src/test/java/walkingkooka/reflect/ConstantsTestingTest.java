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

package walkingkooka.reflect;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConstantsTestingTest implements ConstantsTesting<ConstantsTestingTest> {

    // fieldPublicStaticCheck...........................................................................................

    @Test
    public void testFieldPublicStaticCheck() {
        this.fieldPublicStaticCheck(ConstantsTestingTest.class, "publicField", Object.class);
    }

    public final static Object publicField = "publicFieldValue";

    @Test
    public void testFieldPublicStaticUnknownFieldFails() {
        assertThrows(AssertionError.class, () -> this.fieldPublicStaticCheck(ConstantsTestingTest.class, "unknownField", Object.class));
    }

    // testConstantsAreUnique...........................................................................................

    @Test
    public void testConstantsAreUniqueNotStaticFails() {
        assertThrows(AssertionError.class, () -> new testConstantsAreUniqueNotStaticFails("").testConstantsAreUnique());
    }

    static class testConstantsAreUniqueNotStaticFails extends TestConstantsTestingTest<testConstantsAreUniqueNotStaticFails> {

        public final testConstantsAreUniqueNotStaticFails NOT_STATIC = this;

        private testConstantsAreUniqueNotStaticFails(final String value) {
            super(value);
        }

        @Override
        public Set<testConstantsAreUniqueNotStaticFails> intentionalDuplicateConstants() {
            return Sets.empty();
        }
    }

    @Test
    public void testConstantsAreUniqueUnique() throws Exception {
        new TestConstantsAreUniqueUnique("").testConstantsAreUnique();
    }

    static class TestConstantsAreUniqueUnique extends TestConstantsTestingTest<TestConstantsAreUniqueUnique> {

        public final static TestConstantsAreUniqueUnique CONSTANT1 = new TestConstantsAreUniqueUnique("constant-1");
        public final static TestConstantsAreUniqueUnique CONSTANT2 = new TestConstantsAreUniqueUnique("constant-2");

        private TestConstantsAreUniqueUnique(final String value) {
            super(value);
        }

        @Override
        public Set<TestConstantsAreUniqueUnique> intentionalDuplicateConstants() {
            return Sets.empty();
        }
    }

    @Test
    public void testConstantsAreUniqueNotFails() {
        assertThrows(AssertionError.class, () -> new TestConstantsAreUniqueNotFails("").testConstantsAreUnique());
    }

    static class TestConstantsAreUniqueNotFails extends TestConstantsTestingTest<TestConstantsAreUniqueNotFails> {

        public final static TestConstantsAreUniqueNotFails CONSTANT1 = new TestConstantsAreUniqueNotFails("constant-1");
        public final static TestConstantsAreUniqueNotFails CONSTANT2 = CONSTANT1;
        public final static TestConstantsAreUniqueNotFails CONSTANT3 = new TestConstantsAreUniqueNotFails("constant-3");

        private TestConstantsAreUniqueNotFails(final String value) {
            super(value);
        }

        @Override
        public Set<TestConstantsAreUniqueNotFails> intentionalDuplicateConstants() {
            return Sets.empty();
        }
    }

    @Test
    public void testConstantsAreUniqueFilter() throws Exception {
        new TestConstantsAreUniqueFilter("").testConstantsAreUnique();
    }

    static class TestConstantsAreUniqueFilter extends TestConstantsTestingTest<TestConstantsAreUniqueFilter> {

        public final static TestConstantsAreUniqueFilter CONSTANT1 = new TestConstantsAreUniqueFilter("constant-1");
        public final static TestConstantsAreUniqueFilter CONSTANT2 = CONSTANT1;
        public final static TestConstantsAreUniqueFilter CONSTANT3 = new TestConstantsAreUniqueFilter("constant-3");

        private TestConstantsAreUniqueFilter(final String value) {
            super(value);
        }

        @Override
        public Set<TestConstantsAreUniqueFilter> intentionalDuplicateConstants() {
            return Sets.of(CONSTANT1);
        }
    }

    @Test
    public void testConstantsAreUniqueFilterAndNotFails() {
        assertThrows(AssertionError.class, () -> new TestConstantsAreUniqueFilterAndNotFails("").testConstantsAreUnique());
    }

    static class TestConstantsAreUniqueFilterAndNotFails extends TestConstantsTestingTest<TestConstantsAreUniqueFilterAndNotFails> {

        public final static TestConstantsAreUniqueFilterAndNotFails CONSTANT1 = new TestConstantsAreUniqueFilterAndNotFails("constant-1");
        public final static TestConstantsAreUniqueFilterAndNotFails CONSTANT2 = CONSTANT1;
        public final static TestConstantsAreUniqueFilterAndNotFails CONSTANT3 = new TestConstantsAreUniqueFilterAndNotFails("constant-3");
        public final static TestConstantsAreUniqueFilterAndNotFails CONSTANT4 = CONSTANT3;

        private TestConstantsAreUniqueFilterAndNotFails(final String value) {
            super(value);
        }

        @Override
        public Set<TestConstantsAreUniqueFilterAndNotFails> intentionalDuplicateConstants() {
            return Sets.of(CONSTANT1);
        }
    }

    static abstract class TestConstantsTestingTest<T extends TestConstantsTestingTest> implements ConstantsTesting<T> {

        TestConstantsTestingTest(final String value) {
            this.value = value;
        }

        // @Override stops JUNIT from discovering this class as a test.
        @Override
        public void testConstantsAreUnique() throws Exception {
            ConstantsTesting.super.testConstantsAreUnique();
        }

        @Override
        public final int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public final boolean equals(final Object other) {
            return other instanceof TestConstantsTestingTest && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestConstantsTestingTest other) {
            return this.value.equals(other.value);
        }

        private final String value;

        @Override
        public String toString() {
            return this.value;
        }

        @Override
        public final Class<T> type() {
            return Cast.to(this.getClass());
        }
    }

    // ConstantsTesting.................................................................................................

    @Override
    public Class<ConstantsTestingTest> type() {
        return ConstantsTestingTest.class;
    }

    @Override
    public Set<ConstantsTestingTest> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}
