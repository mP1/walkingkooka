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
import walkingkooka.type.JavaVisibility;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public final class ClassTestingTest extends TestingTestCase {

    // publicStaticMethodParametersTypeCheck............................................................................

    @Test
    public void testWithoutInvalidTypes() {
        new TestWithoutInvalidTypes().publicStaticMethodParametersTypeCheck(Void.class, MathContext.class);
    }

    static class TestWithoutInvalidTypes extends ClassTestingTestTest<TestWithoutInvalidTypes> {

        public static void publicStaticMethod(final Object param1) {
        }

        @Override
        public Class<TestWithoutInvalidTypes> type() {
            return TestWithoutInvalidTypes.class;
        }
    }

    @Test
    public void testWithInvalidType() {
        this.mustFail(() -> new TestWithInvalidType().publicStaticMethodParametersTypeCheck(Void.class, MathContext.class));
    }

    static class TestWithInvalidType extends ClassTestingTestTest<TestWithInvalidType> {

        public static void publicStaticMethod(final MathContext param1) {
        }

        @Override
        public Class<TestWithInvalidType> type() {
            return TestWithInvalidType.class;
        }
    }

    @Test
    public void testWithSubClassParameter() {
        this.mustFail(() -> new TestWithSubClassParameter().publicStaticMethodParametersTypeCheck(Void.class, Map.class));
    }

    static class TestWithSubClassParameter extends ClassTestingTestTest<TestWithSubClassParameter> {

        public static void publicStaticMethod(final HashMap param1) {
        }

        @Override
        public Class<TestWithSubClassParameter> type() {
            return TestWithSubClassParameter.class;
        }
    }

    @Test
    public void testWithSuperParameter() {
        new TestWithSuperClassParameter().publicStaticMethodParametersTypeCheck(Void.class, HashMap.class);
    }

    static class TestWithSuperClassParameter extends ClassTestingTestTest<TestWithSuperClassParameter> {

        public static void publicStaticMethod(final Map param1) {
        }

        @Override
        public Class<TestWithSuperClassParameter> type() {
            return TestWithSuperClassParameter.class;
        }
    }

    static abstract class ClassTestingTestTest<T> implements ClassTesting<T> {

        ClassTestingTestTest() {

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
