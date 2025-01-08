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

package walkingkooka.visit;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public final class VisitorTestingTest implements ClassTesting2<VisitorTesting> {

    @Test
    public void testInstanceCheckStaticFails() {
        boolean failed = false;
        try {
            VisitorTesting2.instanceCheck("visit", TestInstanceCheckStaticFailsVisitor.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestInstanceCheckStaticFailsVisitor extends Visitor<TestVisitable> {
        @Override
        public void accept(final TestVisitable object) {
        }

        protected static void ignored(final TestVisitable object) {
        }

        protected static void visit(final TestVisitable object) {
        }
    }

    @Test
    public void testProtectedMethodCheckPublicFails() {
        boolean failed = false;
        try {
            VisitorTesting2.protectedMethodCheck("visit", TestProtectedMethodCheckPublicFailsVisitor.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestProtectedMethodCheckPublicFailsVisitor extends Visitor<TestVisitable> {
        @Override
        public void accept(final TestVisitable object) {
        }

        public void ignored(final TestVisitable object) {
        }

        public void visit(final TestVisitable object) {
        }
    }

    @Test
    public void testProtectedMethodCheckPackagePrivateFails() {
        boolean failed = false;
        try {
            VisitorTesting2.protectedMethodCheck("visit", TestProtectedMethodCheckPackagePrivateFailsVisitor.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestProtectedMethodCheckPackagePrivateFailsVisitor extends Visitor<TestVisitable> {
        @Override
        public void accept(final TestVisitable object) {
        }

        void ignored(final TestVisitable object) {
        }

        void visit(final TestVisitable object) {
        }
    }

    @Test
    public void testSingleParameterCheckFails0() {
        boolean failed = false;
        try {
            VisitorTesting2.singleParameterCheck("visit", TestSingleParameterCheckFailsVisitor0.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestSingleParameterCheckFailsVisitor0 extends Visitor<TestVisitable> {
        @Override
        public void accept(final TestVisitable object) {
        }

        protected void ignored() {
        }

        protected void visit() {
        }
    }

    @Test
    public void testSingleParameterCheckFails2() {
        boolean failed = false;
        try {
            VisitorTesting2.singleParameterCheck("visit", TestSingleParameterCheckFailsVisitor2.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestSingleParameterCheckFailsVisitor2 extends Visitor<TestVisitable> {
        @Override
        public void accept(final TestVisitable object) {
        }

        protected void ignored(final TestVisitable object, final TestVisitable extra) {
        }

        protected void visit(final TestVisitable object, final TestVisitable extra) {
        }
    }

    @Test
    public void testMethodParameterTypesPublicCheckFails() {
        boolean failed = false;
        try {
            VisitorTesting2.methodParameterTypesPublicCheck("visit", TestMethodParameterTypesPublicCheckFailsVisitor.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestMethodParameterTypesPublicCheckFailsVisitor extends Visitor<TestPackagePrivateVisitable> {
        @Override
        public void accept(final TestPackagePrivateVisitable object) {
        }

        protected void ignored(final TestPackagePrivateVisitable visitable) {
        }

        protected void visit(final TestPackagePrivateVisitable visitable) {
        }
    }

    static class TestPackagePrivateVisitable implements Visitable {
    }

    @Test
    public void testMethodReturnTypeCheckFails() {
        boolean failed = false;
        try {
            VisitorTesting2.methodReturnTypeVoidCheck("visit", TestMethodReturnTypeCheckFailsVisitor.class, Void.class);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    public static class TestMethodReturnTypeCheckFailsVisitor extends Visitor<TestPackagePrivateVisitable> {
        @Override
        public void accept(final TestPackagePrivateVisitable object) {
        }

        protected Object ignored(final TestVisitable visitable) {
            return null;
        }

        protected Object visit(final TestVisitable visitable) {
            return null;
        }
    }


    public static class TestVisitable implements Visitable {
    }

    @Override
    public Class<VisitorTesting> type() {
        return VisitorTesting.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
