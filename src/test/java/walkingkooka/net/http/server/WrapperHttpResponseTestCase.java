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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.test.Latch;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public abstract class WrapperHttpResponseTestCase<R extends WrapperHttpResponse> extends HttpResponseTestCase<R> {

    WrapperHttpResponseTestCase() {
        super();
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullResponseFails() {
        this.createResponse(null);
    }

    @Test
    public void testSetStatus() {
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

    final <T> void addHeaderAndCheck(final HttpHeaderName<T> header,
                                     final T value) {
        this.addHeaderAndCheck(header,
                value,
                null,
                null);
    }

    final <T, U> void addHeaderAndCheck(final HttpHeaderName<T> header,
                                        final T value,
                                        final HttpHeaderName<U> header2,
                                        final U value2) {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        final Map<HttpHeaderName<?>, Object> expectedHeaders = Maps.ordered();

        final R response = this.createResponse(new FakeHttpResponse() {

            public <T> void addHeader(final HttpHeaderName<T> n, final T v) {
                headers.put(n, v);
            }
        });
        response.addHeader(header, value);
        expectedHeaders.put(header, value);

        if (null != header2 && null != value2) {
            response.addHeader(header2, value2);
            expectedHeaders.put(header2, value2);
        }

        assertEquals("headers", expectedHeaders, headers);
    }

    // helpers..................................................................................................

    @Override
    protected final R createResponse() {
        return this.createResponse(this.wrappedHttpResponse());
    }

    abstract R createResponse(final HttpResponse response);

    HttpResponse wrappedHttpResponse() {
        return HttpResponses.fake();
    }
}
