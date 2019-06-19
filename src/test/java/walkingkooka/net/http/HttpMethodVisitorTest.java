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

package walkingkooka.net.http;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class HttpMethodVisitorTest implements HttpMethodVisitorTesting<HttpMethodVisitor> {

    @Override
    public void testAllConstructorsVisibility() {
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Test
    public void testHead() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.HEAD;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitHead() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testGet() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.GET;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitGet() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testPost() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.POST;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitPost() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testPut() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.PUT;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitPut() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testDelete() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.DELETE;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitDelete() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testTrace() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.TRACE;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitTrace() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testOption() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.OPTIONS;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitOptions() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testConnect() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.CONNECT;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitConnect() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testPatch() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.PATCH;
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitPatch() {
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Test
    public void testUnknown() {
        final StringBuilder b = new StringBuilder();

        final HttpMethod method = HttpMethod.with("Unknown");
        new FakeHttpMethodVisitor() {
            @Override
            protected Visiting startVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("2");
            }

            @Override
            protected void visitUnknown(final HttpMethod m) {
                assertSame(method, m, "method");
                b.append("3");
            }
        }.accept(method);

        assertEquals("132", b.toString());
    }

    @Override
    public HttpMethodVisitor createVisitor() {
        return new HttpMethodVisitor();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HttpMethodVisitor> type() {
        return HttpMethodVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }
}
