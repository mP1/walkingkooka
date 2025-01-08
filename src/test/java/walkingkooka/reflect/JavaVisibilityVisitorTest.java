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
import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;
import walkingkooka.visit.VisitorTesting;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class JavaVisibilityVisitorTest implements VisitorTesting<JavaVisibilityVisitor, JavaVisibility> {

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

        this.checkEquals("132", b.toString());
    }

    @Test
    public void testVisitorPublic2() {
        this.visitAndCheck(JavaVisibility.PUBLIC,
            new JavaVisibilityVisitor() {

                @Override
                protected void visitPublic() {
                    visited = true;
                }

                @Override
                protected void visitProtected() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitPackagePrivate() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitPrivate() {
                    throw new UnsupportedOperationException();
                }
            });
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

        this.checkEquals("132", b.toString());
    }

    @Test
    protected void testVisitorProtected2() {
        this.visitAndCheck(JavaVisibility.PROTECTED,
            new JavaVisibilityVisitor() {

                @Override
                protected void visitPublic() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitProtected() {
                    visited = true;
                }

                @Override
                protected void visitPackagePrivate() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitPrivate() {
                    throw new UnsupportedOperationException();
                }
            });
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

        this.checkEquals("132", b.toString());
    }

    @Test
    public void testVisitorPackagePrivate2() {
        this.visitAndCheck(JavaVisibility.PACKAGE_PRIVATE,
            new JavaVisibilityVisitor() {

                @Override
                protected void visitPublic() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitProtected() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitPackagePrivate() {
                    visited = true;
                }

                @Override
                protected void visitPrivate() {
                    throw new UnsupportedOperationException();
                }
            });
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

        this.checkEquals("132", b.toString());
    }

    @Test
    public void testVisitorPrivate2() {
        this.visitAndCheck(JavaVisibility.PRIVATE,
            new JavaVisibilityVisitor() {

                @Override
                protected void visitPublic() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitProtected() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitPackagePrivate() {
                    throw new UnsupportedOperationException();
                }

                @Override
                protected void visitPrivate() {
                    visited = true;
                }
            });
    }

    private void visitAndCheck(final JavaVisibility visibility,
                               final JavaVisibilityVisitor visitor) {
        this.visited = false;
        visitor.accept(visibility);
        this.checkEquals(true, this.visited, () -> "" + visibility);

        new JavaVisibilityVisitor().accept(visibility);
    }

    private boolean visited;

    @Override
    public void testCheckToStringOverridden() {
    }

    // ClassTesting.....................................................................................................

    @Override
    public JavaVisibilityVisitor createVisitor() {
        return new JavaVisibilityVisitor();
    }

    @Override
    public String typeNamePrefix() {
        return JavaVisibility.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Visitor.class.getSimpleName();
    }

    @Override
    public Class<JavaVisibilityVisitor> type() {
        return JavaVisibilityVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
