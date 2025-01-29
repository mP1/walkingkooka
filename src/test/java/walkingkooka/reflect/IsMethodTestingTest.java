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
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethod.TestApple;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodFalse.TestGolf;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodFilter.TestKetchup;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodMissing.TestMoon;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodMultipleTrue.TestHope;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodPrefixCheck.TestCarrot;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodSuffixCheck.TestEggplant;
import walkingkooka.reflect.IsMethodTestingTest.TestIsMethodThrows.TestJigsaw;
import walkingkooka.test.Testing;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class IsMethodTestingTest implements Testing {

    @Test
    public void testIsMethod() throws Exception {
        call = 0;
        new TestIsMethod().testIsMethods();
        this.checkCall(2);
    }

    static class TestIsMethod extends TestIsMethodTestingTest<TestApple> {

        @Override
        public final TestApple createIsMethodObject() {
            return new TestApple();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestApple {

            public boolean isTestApple() {
                call++;
                return true;
            }

            public boolean isTestBanana() {
                call++;
                return false;
            }
        }
    }

    @Test
    public void testIsMethodPrefix() throws Exception {
        call = 0;
        new TestIsMethodPrefixCheck().testIsMethods();
        this.checkCall(2);
    }

    static class TestIsMethodPrefixCheck extends TestIsMethodTestingTest<TestCarrot> {

        @Override
        public final TestCarrot createIsMethodObject() {
            return new TestCarrot();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "Test",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestCarrot {

            public boolean isCarrot() {
                call++;
                return true;
            }

            public boolean isDog() {
                call++;
                return false;
            }
        }
    }

    @Test
    public void testIsMethodSuffix() throws Exception {
        call = 0;
        new TestIsMethodSuffixCheck().testIsMethods();
        this.checkCall(2);
    }

    static class TestIsMethodSuffixCheck extends TestIsMethodTestingTest<TestEggplant> {

        @Override
        public final TestEggplant createIsMethodObject() {
            return new TestEggplant();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "Test",
                "plant"
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestEggplant {

            public boolean isEgg() {
                call++;
                return true;
            }

            public boolean isFruit() {
                call++;
                return false;
            }
        }
    }

    @Test
    public void testIsMethodFalse() {
        call = 0;
        assertThrows(AssertionError.class, () -> new TestIsMethodFalse().testIsMethods());
        this.checkCall(1);
    }

    static class TestIsMethodFalse extends TestIsMethodTestingTest<TestGolf> {

        @Override
        public final TestGolf createIsMethodObject() {
            return new TestGolf();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestGolf {

            public boolean isTestGolf() {
                call++;
                return false;
            }
        }
    }

    @Test
    public void testIsMethodMultipleTrue() {
        call = 0;
        assertThrows(AssertionError.class, () -> new TestIsMethodMultipleTrue().testIsMethods());
        this.checkCall(2);
    }

    static class TestIsMethodMultipleTrue extends TestIsMethodTestingTest<TestHope> {

        @Override
        public final TestHope createIsMethodObject() {
            return new TestHope();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestHope {

            public boolean isTestHope() {
                call++;
                return true;
            }

            public boolean isIgloo() {
                call++;
                return true;
            }
        }
    }

    @Test
    public void testIsMethodThrows() {
        call = 0;
        assertThrows(AssertionError.class, () -> new TestIsMethodFalse().testIsMethods());
        this.checkCall(1);
    }

    static class TestIsMethodThrows extends TestIsMethodTestingTest<TestJigsaw> {

        @Override
        public final TestJigsaw createIsMethodObject() {
            return new TestJigsaw();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestJigsaw {

            public boolean isTestJigsaw() {
                call++;
                throw new RuntimeException("123");
            }
        }
    }

    @Test
    public void testIsMethodFilter() {
        call = 0;
        assertThrows(AssertionError.class, () -> new TestIsMethodFilter().testIsMethods());
        this.checkCall(1);
    }

    static class TestIsMethodFilter extends TestIsMethodTestingTest<TestKetchup> {

        @Override
        public final TestKetchup createIsMethodObject() {
            return new TestKetchup();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.is("isLima");
        }

        static class TestKetchup {

            public boolean isTestKetchup() {
                call++;
                return false;
            }

            public boolean isLima() {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Test
    public void testIsMethodMissing() {
        assertThrows(NoSuchMethodException.class, () -> new TestIsMethodMissing().testIsMethods());
    }

    static class TestIsMethodMissing extends TestIsMethodTestingTest<TestMoon> {

        @Override
        public final TestMoon createIsMethodObject() {
            return new TestMoon();
        }

        @Override
        public String toIsMethodName(final String typeName) {
            return this.toIsMethodNameWithPrefixSuffix(
                typeName,
                "",
                ""
            );
        }

        @Override
        public Predicate<String> isMethodIgnoreMethodFilter() {
            return Predicates.never();
        }

        static class TestMoon {
        }
    }

    // helper...........................................................................................................

    static abstract class TestIsMethodTestingTest<T> implements IsMethodTesting<T> {

        TestIsMethodTestingTest() {
            super();
        }

        // @Override stops JUNIT from discovering this class as a test.
        @Override
        public void testIsMethods() throws Exception {
            IsMethodTesting.super.testIsMethods();
        }
    }

    private void checkCall(final int expected) {
        this.checkEquals(expected, call, "not all is methods called");
    }

    static int call = 0;
}
