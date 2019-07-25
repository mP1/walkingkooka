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
import walkingkooka.collect.set.Sets;
import walkingkooka.type.JavaVisibility;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ClassTestingTest {

    @Test
    public void testWithoutMathContextParameter() {
        new TestWithoutMathContextParameter().publicStaticMethodParametersTypeCheck(Sets.of(Void.class, MathContext.class));
    }

    static class TestWithoutMathContextParameter implements ClassTesting<TestWithoutMathContextParameter> {

        public static void publicStaticMethod(final Object param1) {
        }

        @Override
        public void testAllMethodsVisibility() {
        }

        @Override
        public void testClassVisibility() {
        }

        @Override
        public void testTestNaming() {
        }

        @Override
        public Class<TestWithoutMathContextParameter> type() {
            return TestWithoutMathContextParameter.class;
        }

        @Override
        public JavaVisibility typeVisibility() {
            throw new UnsupportedOperationException();
        }
    }


    @Test
    public void testWithMathContextParameter() {
        boolean failed = false;
        try {
            new TestWithMathContextParameter().publicStaticMethodParametersTypeCheck(Sets.of(Void.class, MathContext.class));
        } catch (final AssertionError expected) {
            failed = true;
        }
        assertEquals(true, failed);
    }

    static class TestWithMathContextParameter implements ClassTesting<TestWithMathContextParameter> {

        public static void publicStaticMethod(final MathContext param1) {
        }

        @Override
        public void testAllMethodsVisibility() {
        }

        @Override
        public void testClassVisibility() {
        }

        @Override
        public void testTestNaming() {
        }

        @Override
        public Class<TestWithMathContextParameter> type() {
            return TestWithMathContextParameter.class;
        }

        @Override
        public JavaVisibility typeVisibility() {
            throw new UnsupportedOperationException();
        }
    }
}
