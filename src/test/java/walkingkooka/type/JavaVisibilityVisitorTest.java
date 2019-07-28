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

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JavaVisibilityVisitorTest implements ClassTesting2<JavaVisibilityVisitor> {

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
    }

    // JavaVisibilityVisitor............................................................................................

    @Test
    public void testVisitorPublic() {
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
    protected void testVisitorProtected() {
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
        assertEquals(true, this.visited, () -> "" + visibility);

        new JavaVisibilityVisitor().accept(visibility);
    }

    private boolean visited;

    // ClassTesting.....................................................................................................

    @Override
    public Class<JavaVisibilityVisitor> type() {
        return JavaVisibilityVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
