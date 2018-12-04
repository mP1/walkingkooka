/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.net.http.server;

import org.junit.Test;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.test.Latch;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public abstract class WrapperHttpResponseTestCase<R extends WrapperHttpResponse> extends HttpResponseTestCase<R> {

    WrapperHttpResponseTestCase() {
        super();
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullRequestFails() {
        this.createResponse(null, HttpResponses.fake());
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullResponseFails() {
        this.createResponse(HttpRequests.fake(), null);
    }

    @Test
    public final void testSetStatus() {
        final Latch set = Latch.create();
        final HttpStatus status = HttpStatusCode.OK.status();
        this.createResponse(new FakeHttpResponse() {
            @Test
            public void setStatus(final HttpStatus s) {
                assertSame(status, s);
                set.set("Status set to " + s);
            }
        }).setStatus(status);
        assertTrue("wrapped response setStatus not called", set.value());
    }

    final <T> void addHeaderAndCheck(final HttpRequest request,
                                     final HttpHeaderName<T> header,
                                     final T value) {
        final Latch set = Latch.create();
        this.createResponse(request,
                new FakeHttpResponse() {

                    public <T> void addHeader(final HttpHeaderName<T> n, final T v) {
                        assertSame("header", header, n);
                        assertSame("value", value, v);
                        set.set("header " + n + "=" + v + " added.");
                    }
                }).addHeader(header, value);
        assertTrue("wrapped response addHeader not called", set.value());
    }

    // helpers..................................................................................................

    @Override
    protected final R createResponse() {
        return this.createResponse(HttpResponses.fake());
    }

    final R createResponse(final HttpResponse response) {
        return this.createResponse(this.request(), response);
    }

    abstract R createResponse(final HttpRequest request,
                              final HttpResponse response);

    abstract HttpRequest request();
}
