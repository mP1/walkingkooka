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
import walkingkooka.reflect.PublicStaticHelperTestingTest.TestPublicStaticHelperTesting;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PublicStaticHelperTestingTest implements PublicStaticHelperTesting<TestPublicStaticHelperTesting> {

    public final static Object PUBLIC = 1;

    // testClassIsFinal.................................................................................................

    @Test
    public final void testClassIsFinalFail() {
        assertThrows(AssertionError.class, () -> new TestClassIsFinalFail().testClassIsFinal());
    }

    public static final class TestClassIsFinalFail extends TestPublicStaticHelperTestingTest<TestClassNotFinal> {

        @Override
        public Class<TestClassNotFinal> type() {
            return TestClassNotFinal.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public static class TestClassNotFinal implements PublicStaticHelper {

        private TestClassNotFinal() {
            throw new UnsupportedOperationException();
        }
    }

    // testOnlyConstructorIsPrivate.....................................................................................

    @Test
    public final void testOnlyConstructorIsPrivateFail() {
        assertThrows(AssertionError.class, () -> new TestOnlyConstructorIsPrivateFail().testOnlyConstructorIsPrivate());
    }

    public static final class TestOnlyConstructorIsPrivateFail extends TestPublicStaticHelperTestingTest<TestOnlyConstructorNotPrivate> {

        @Override
        public Class<TestOnlyConstructorNotPrivate> type() {
            return TestOnlyConstructorNotPrivate.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public final static class TestOnlyConstructorNotPrivate implements PublicStaticHelper {

        TestOnlyConstructorNotPrivate() {
            throw new UnsupportedOperationException();
        }
    }

    // testDefaultConstructorThrowsUnsupportedOperationException.........................................................

    @Test
    public final void testDefaultConstructorThrowsUnsupportedOperationExceptionFail() {
        assertThrows(AssertionError.class, () -> new TestDefaultConstructorThrowsUnsupportedOperationExceptionFail().testDefaultConstructorThrowsUnsupportedOperationException());
    }

    public static final class TestDefaultConstructorThrowsUnsupportedOperationExceptionFail extends TestPublicStaticHelperTestingTest<TestCtorDoesntThrowUOE> {

        @Override
        public Class<TestCtorDoesntThrowUOE> type() {
            return TestCtorDoesntThrowUOE.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public static final class TestCtorDoesntThrowUOE implements PublicStaticHelper {

        private TestCtorDoesntThrowUOE() {
        }
    }

    // testAllMethodsAreStatic..........................................................................................

    @Test
    public final void testAllMethodsAreStaticFail() {
        assertThrows(AssertionError.class, () -> new TestAllMethodsAreStaticFail().testAllMethodsAreStatic());
    }

    public static final class TestAllMethodsAreStaticFail extends TestPublicStaticHelperTestingTest<TestHasNonStaticMethods> {

        @Override
        public Class<TestHasNonStaticMethods> type() {
            return TestHasNonStaticMethods.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public static final class TestHasNonStaticMethods implements PublicStaticHelper {

        void instanceMethod() {
        }
    }

    // testCheckVisibilityOfAllStaticMethods............................................................................

    @Test
    public final void testCheckVisibilityOfAllStaticMethodsFail() {
        assertThrows(AssertionError.class, () -> new TestCheckVisibilityOfAllStaticMethodsFail().testCheckVisibilityOfAllStaticMethods());
    }

    public static final class TestCheckVisibilityOfAllStaticMethodsFail extends TestPublicStaticHelperTestingTest<TestProtectedStaticMethods> {

        @Override
        public Class<TestProtectedStaticMethods> type() {
            return TestProtectedStaticMethods.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public static final class TestProtectedStaticMethods implements PublicStaticHelper {

        protected static void protectedStaticMethod() {
        }
    }

    @Test
    public final void testCheckVisibilityOfAllStaticMethodsPrivate() {
        new TestCheckVisibilityOfAllStaticMethodsPrivate().testCheckVisibilityOfAllStaticMethods();
    }

    public static final class TestCheckVisibilityOfAllStaticMethodsPrivate extends TestPublicStaticHelperTestingTest<TestPrivateStaticMethods> {

        @Override
        public Class<TestPrivateStaticMethods> type() {
            return TestPrivateStaticMethods.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public static final class TestPrivateStaticMethods implements PublicStaticHelper {

        @SuppressWarnings("unused")
        private static void privateStaticMethod() {
        }
    }

    // testPublicStaticMethodsParameterAndReturnTypesArePublic..........................................................

    @Test
    public final void testPublicStaticMethodsParameterAndReturnTypesArePublicFail() {
        this.checkEquals(JavaVisibility.PACKAGE_PRIVATE, JavaVisibility.of(TestNonPublic.class));

        assertThrows(AssertionError.class, () -> new TestPublicStaticMethodsParameterAndReturnTypesArePublicFail().testPublicStaticMethodsParameterAndReturnTypesArePublic());
    }

    public static final class TestPublicStaticMethodsParameterAndReturnTypesArePublicFail extends TestPublicStaticHelperTestingTest<TestHasNonPublicReturnType> {

        @Override
        public Class<TestHasNonPublicReturnType> type() {
            return TestHasNonPublicReturnType.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public final static class TestHasNonPublicReturnType implements PublicStaticHelper {

        TestNonPublic returnNonPublicType() {
            return null;
        }
    }

    @Test
    public final void testPublicStaticMethodsParameterAndReturnTypesArePublicFail2() {
        this.checkEquals(JavaVisibility.PACKAGE_PRIVATE, JavaVisibility.of(TestNonPublic.class));

        assertThrows(AssertionError.class, () -> new TestPublicStaticMethodsParameterAndReturnTypesArePublicFail2().testPublicStaticMethodsParameterAndReturnTypesArePublic());
    }

    public static final class TestPublicStaticMethodsParameterAndReturnTypesArePublicFail2 extends TestPublicStaticHelperTestingTest<TestHasNonPublicParameterType> {

        @Override
        public Class<TestHasNonPublicParameterType> type() {
            return TestHasNonPublicParameterType.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public final static class TestHasNonPublicParameterType implements PublicStaticHelper {

        void returnNonPublicType(final Object param1, TestNonPublic param2) {
        }
    }

    static class TestNonPublic {
    }

    @Override
    public final void testTestNaming() {
    }

    // testContainsPublicStaticMethodsOrField..........................................................................................

    @Test
    public final void testContainsZeroInstanceFields() {
        assertThrows(AssertionError.class, () -> new TestContainsZeroInstanceFields().testContainsZeroInstanceFields());
    }

    public static final class TestContainsZeroInstanceFields extends TestPublicStaticHelperTestingTest<TestHasInstanceField> {

        @Override
        public Class<TestHasInstanceField> type() {
            return TestHasInstanceField.class;
        }

        @Override
        public boolean canHavePublicTypes(final Method method) {
            return false;
        }
    }

    public static final class TestHasInstanceField implements PublicStaticHelper {

        final Object publicInstanceField = 1;
    }

    // PublicStaticHelperTesting........................................................................................

    @Override
    public Class<TestPublicStaticHelperTesting> type() {
        return TestPublicStaticHelperTesting.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    public static final class TestPublicStaticHelperTesting implements PublicStaticHelper {

        public static Object publicStaticMethod(final Object param) {
            return null;
        }

        private TestPublicStaticHelperTesting() {
            throw new UnsupportedOperationException();
        }
    }

    static abstract class TestPublicStaticHelperTestingTest<T extends PublicStaticHelper> implements PublicStaticHelperTesting<T> {

        TestPublicStaticHelperTestingTest() {
            super();
        }

        // Overrides stop JUNIT from discovering the "tests" on this inner class

        @Override
        public final void testClassIsFinal() {
            PublicStaticHelperTesting.super.testClassIsFinal();
        }

        @Override
        public final void testOnlyConstructorIsPrivate() throws Exception {
            PublicStaticHelperTesting.super.testOnlyConstructorIsPrivate();
        }

        @Override
        public final void testDefaultConstructorThrowsUnsupportedOperationException() throws Exception {
            PublicStaticHelperTesting.super.testDefaultConstructorThrowsUnsupportedOperationException();
        }

        @Override
        public final void testAllMethodsAreStatic() {
            PublicStaticHelperTesting.super.testAllMethodsAreStatic();
        }

        @Override
        public final void testCheckVisibilityOfAllStaticMethods() {
            PublicStaticHelperTesting.super.testCheckVisibilityOfAllStaticMethods();
        }

        @Override
        public final void testPublicStaticMethodsParameterAndReturnTypesArePublic() {
            PublicStaticHelperTesting.super.testPublicStaticMethodsParameterAndReturnTypesArePublic();
        }

        @Override
        public final void testContainsZeroInstanceFields() {
            PublicStaticHelperTesting.super.testContainsZeroInstanceFields();
        }

        @Override
        public final void testClassVisibility() {
        }

        @Override
        public final void testAllMethodsVisibility() {
        }

        @Override
        public final void testTestNaming() {
        }

        @Override
        public final void testAllConstructorsVisibility() {
        }

        @Override
        public final void testIfClassIsFinalIfAllConstructorsArePrivate() {
        }

        @Override
        public final JavaVisibility typeVisibility() {
            return JavaVisibility.PUBLIC;
        }
    }
}
