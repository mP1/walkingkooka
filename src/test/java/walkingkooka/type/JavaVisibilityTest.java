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

package walkingkooka.type;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class JavaVisibilityTest implements ClassTesting2<JavaVisibility>,
        ToStringTesting<JavaVisibility> {

    @Test
    public void testClassPublic() {
        check(JavaVisibility.PUBLIC, this.getClass());
    }

    @Test
    public void testClassProtected() {
        check(JavaVisibility.PROTECTED, ProtectedClass.class);
    }

    @Test
    public void testClassPackagePrivate() {
        check(JavaVisibility.PACKAGE_PRIVATE, PackagePrivateClass.class);
    }

    private void check(final JavaVisibility visibility, final Class<?> klass) {
        assertEquals(visibility, JavaVisibility.get(klass), klass.getName());
    }

    protected class ProtectedClass {
    }

    class PackagePrivateClass {
    }

    // JavaVisibilityVisitor............................................................................................

    @Test
    public void testVisitorPublic() {
        final StringBuilder b = new StringBuilder();

        final JavaVisibility visibility = JavaVisibility.PUBLIC;
        new FakeJavaVisibilityVisitor() {
            @Override
            protected Visiting startVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("2");
            }

            @Override
            protected void visitPublic() {
                b.append("3");
            }
        }.accept(visibility);

        assertEquals("132", b.toString());
    }

    @Test
    public void testVisitorProtected() {
        final StringBuilder b = new StringBuilder();

        final JavaVisibility visibility = JavaVisibility.PROTECTED;
        new FakeJavaVisibilityVisitor() {
            @Override
            protected Visiting startVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("2");
            }

            @Override
            protected void visitProtected() {
                b.append("3");
            }
        }.accept(visibility);

        assertEquals("132", b.toString());
    }

    @Test
    public void testVisitorPackagePrivate() {
        final StringBuilder b = new StringBuilder();

        final JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;
        new FakeJavaVisibilityVisitor() {
            @Override
            protected Visiting startVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("2");
            }

            @Override
            protected void visitPackagePrivate() {
                b.append("3");
            }
        }.accept(visibility);

        assertEquals("132", b.toString());
    }

    @Test
    public void testVisitorPrivate() {
        final StringBuilder b = new StringBuilder();

        final JavaVisibility visibility = JavaVisibility.PRIVATE;
        new FakeJavaVisibilityVisitor() {
            @Override
            protected Visiting startVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JavaVisibility v) {
                assertSame(visibility, v, "visibility");
                b.append("2");
            }

            @Override
            protected void visitPrivate() {
                b.append("3");
            }
        }.accept(visibility);

        assertEquals("132", b.toString());
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<JavaVisibility> type() {
        return JavaVisibility.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
