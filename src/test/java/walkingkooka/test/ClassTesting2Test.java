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

package walkingkooka.test;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClassTesting2Test {

    // testIfClassIsFinalIfAllConstructorsArePrivate....................................................................

    @Test
    public void testIfClassIsFinalIfAllConstructorsArePublic() {
        assertThrows(AssertionError.class, () -> new FinalClassPublicConstructor().testIfClassIsFinalIfAllConstructorsArePrivate());
    }

    static final class FinalClassPublicConstructor extends ClassTesting2TestTest<FinalClassPublicConstructor> {

        public FinalClassPublicConstructor() {
        }

        @Override
        public Class<FinalClassPublicConstructor> type() {
            return FinalClassPublicConstructor.class;
        }
    }

    @Test
    public void testIfClassIsFinalIfAllConstructorsAreProtected() {
        assertThrows(AssertionError.class, () -> new FinalClassProtectedConstructor().testIfClassIsFinalIfAllConstructorsArePrivate());
    }

    static final class FinalClassProtectedConstructor extends ClassTesting2TestTest<FinalClassProtectedConstructor> {

        protected FinalClassProtectedConstructor() {
        }

        @Override
        public Class<FinalClassProtectedConstructor> type() {
            return FinalClassProtectedConstructor.class;
        }
    }

    @Test
    public void testIfClassIsFinalIfAllConstructorsArePackagePrivate() {
        assertThrows(AssertionError.class, () -> new FinalClassPackagePrivateConstructor().testIfClassIsFinalIfAllConstructorsArePrivate());
    }

    static final class FinalClassPackagePrivateConstructor extends ClassTesting2TestTest<FinalClassPackagePrivateConstructor> {

        FinalClassPackagePrivateConstructor() {
        }

        @Override
        public Class<FinalClassPackagePrivateConstructor> type() {
            return FinalClassPackagePrivateConstructor.class;
        }
    }

    @Test
    public void testIfClassIsFinalIfAllConstructorsArePrivate() throws Exception {
        final Constructor<FinalClassPrivateConstructor> ctor = FinalClassPrivateConstructor.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        ctor.newInstance().testIfClassIsFinalIfAllConstructorsArePrivate();
    }

    static final class FinalClassPrivateConstructor extends ClassTesting2TestTest<FinalClassPrivateConstructor> {

        private FinalClassPrivateConstructor() {
        }

        @Override
        public Class<FinalClassPrivateConstructor> type() {
            return FinalClassPrivateConstructor.class;
        }
    }

    // testAllConstructorsVisibility....................................................................................

    // Final...

    @Test
    public void testAllConstructorsVisibilityFinalPublic() {
        assertThrows(AssertionError.class, () -> new FinalClassPublicConstructor().testAllConstructorsVisibility());
    }

    @Test
    public void testAllConstructorsVisibilityFinalProtected() {
        assertThrows(AssertionError.class, () -> new FinalClassProtectedConstructor().testIfClassIsFinalIfAllConstructorsArePrivate());
    }

    @Test
    public void testAllConstructorsVisibilityFinalPackagePrivate() {
        assertThrows(AssertionError.class, () -> new FinalClassPackagePrivateConstructor().testIfClassIsFinalIfAllConstructorsArePrivate());
    }

    @Test
    public void testAllConstructorsVisibilityFinalPrivate() throws Exception {
        final Constructor<FinalClassPrivateConstructor> ctor = FinalClassPrivateConstructor.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        ctor.newInstance().testIfClassIsFinalIfAllConstructorsArePrivate();
    }

    // NonFinal...

    @Test
    public void testAllConstructorsVisibilityNonFinalPublic() {
        assertThrows(AssertionError.class, () -> new NonFinalClassPublicConstructor().testAllConstructorsVisibility());
    }

    static class NonFinalClassPublicConstructor extends ClassTesting2TestTest<NonFinalClassPublicConstructor> {

        public NonFinalClassPublicConstructor() {
        }

        @Override
        public Class<NonFinalClassPublicConstructor> type() {
            return NonFinalClassPublicConstructor.class;
        }
    }

    @Test
    public void testAllConstructorsVisibilityNonFinalProtected() {
        assertThrows(AssertionError.class, () -> new NonFinalClassProtectedConstructor().testAllConstructorsVisibility());
    }

    static class NonFinalClassProtectedConstructor extends ClassTesting2TestTest<NonFinalClassProtectedConstructor> {

        protected NonFinalClassProtectedConstructor() {
        }

        @Override
        public Class<NonFinalClassProtectedConstructor> type() {
            return NonFinalClassProtectedConstructor.class;
        }
    }

    @Test
    public void testAllConstructorsVisibilityNonFinalPackagePrivate() {
        new NonFinalClassPackagePrivateConstructor().testAllConstructorsVisibility();
    }

    static class NonFinalClassPackagePrivateConstructor extends ClassTesting2TestTest<NonFinalClassPackagePrivateConstructor> {

        NonFinalClassPackagePrivateConstructor() {
        }

        @Override
        public Class<NonFinalClassPackagePrivateConstructor> type() {
            return NonFinalClassPackagePrivateConstructor.class;
        }
    }

    @Test
    public void testAllConstructorsVisibilityNonFinalPrivate() throws Exception {
        final Constructor<NonFinalClassPrivateConstructor> ctor = NonFinalClassPrivateConstructor.class.getDeclaredConstructor();
        ctor.setAccessible(true);

        ctor.newInstance().testAllConstructorsVisibility();
    }

    static class NonFinalClassPrivateConstructor extends ClassTesting2TestTest<NonFinalClassPrivateConstructor> {

        private NonFinalClassPrivateConstructor() {
        }

        @Override
        public Class<NonFinalClassPrivateConstructor> type() {
            return NonFinalClassPrivateConstructor.class;
        }
    }

    @Test
    public void testAllConstructorsVisibilityFake() {
        new TestFakePublicConstructor().testAllConstructorsVisibility();
    }

    static class TestFakePublicConstructor extends ClassTesting2TestTest<TestFakePublicConstructor> implements Fake {

        public TestFakePublicConstructor() {
        }

        @Override
        public Class<TestFakePublicConstructor> type() {
            return TestFakePublicConstructor.class;
        }
    }

    // helper...........................................................................................................

    static abstract class ClassTesting2TestTest<T> implements ClassTesting2<T> {

        ClassTesting2TestTest() {
            super();
        }

        @Override
        public final void testIfClassIsFinalIfAllConstructorsArePrivate() {
            ClassTesting2.super.testIfClassIsFinalIfAllConstructorsArePrivate();
        }

        @Override
        public final void testAllConstructorsVisibility() {
            ClassTesting2.super.testAllConstructorsVisibility();
        }

        @Override
        public void testClassVisibility() {
        }

        @Override
        public void testAllMethodsVisibility() {
        }

        @Override
        public void testTestNaming() {
        }

        @Override
        public JavaVisibility typeVisibility() {
            throw new UnsupportedOperationException();
        }
    }
}
