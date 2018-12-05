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
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.HeaderValueToken;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.test.Latch;

import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class AutoGzipEncodingHttpResponseTest extends WrapperHttpResponseTestCase<AutoGzipEncodingHttpResponse> {

    private final static String GZIP = "gzip";

    @Test
    public void testAddHeaderContentEncoding() {
        this.addHeaderAndCheck(HttpRequests.fake(),
                HttpHeaderName.CONTENT_ENCODING,
                HeaderValueToken.parse("br"));
    }

    @Test
    public void testAddHeaderContentEncodingGzip() {
        this.addHeaderAndCheck(HttpRequests.fake(),
                HttpHeaderName.CONTENT_ENCODING,
                HeaderValueToken.parse(GZIP));
    }

    @Test(expected = HeaderValueException.class)
    public void testSetBodyRequestMissingAcceptEncodingFails() {
        this.createResponse().setBody(new byte[0]);
    }

    @Test
    public void testSetBodyRequestAcceptEncodingNotGzip() {
        final byte[] bytes = new byte[]{1, 2, 3};
        this.setBodyRequestWithAcceptEncodingAndCheck("X",
                bytes,
                Maps.empty(),
                bytes);
    }

    @Test
    public void testSetBodyAcceptEncodingNotGzip() {
        final byte[] bytes = new byte[]{1, 2, 3};
        this.setBodyRequestWithAcceptEncodingAndCheck("X",
                bytes,
                Maps.empty(),
                bytes);
    }

    @Test
    public void testSetBodyRequestAcceptEncodingWildcard() {
        final byte[] bytes = new byte[]{1, 2, 3};
        this.setBodyRequestWithAcceptEncodingAndCheck("*",
                bytes,
                this.headersContentEncoding(GZIP),
                this.gzip(bytes));
    }

    @Test
    public void testSetBodyRequestAcceptEncodingGzip() {
        final byte[] bytes = new byte[]{1, 2, 3};
        this.setBodyRequestWithAcceptEncodingAndCheck("*",
                bytes,
                this.headersContentEncoding(GZIP),
                this.gzip(bytes));
    }

    @Test
    public void testSetBodyRequestAcceptEncodingGzipResponseContentEncodingNotGzip() {
        final byte[] bytes = new byte[]{1, 2, 3};
        this.setBodyRequestWithAcceptEncodingAndCheck("*",
                bytes,
                "X",
                this.headersContentEncoding("X"),
                bytes);
    }

    @Test
    public void testSetBodyRequestAcceptEncodingGzipResponseContentEncodingGzip() {
        final byte[] bytes = new byte[]{1, 2, 3, 4};
        this.setBodyRequestWithAcceptEncodingAndCheck("*",
                bytes,
                GZIP,
                this.headersContentEncoding(GZIP),
                this.gzip(bytes));
    }

    private Map<HttpHeaderName<?>, Object> headersContentEncoding(final String headerValue) {
        return Maps.one(HttpHeaderName.CONTENT_ENCODING, HeaderValueToken.parse(headerValue));
    }

    private byte[] gzip(final byte[] bytes) {
        return AutoGzipEncodingHttpResponse.gzip(bytes);
    }

    private void setBodyRequestWithAcceptEncodingAndCheck(final String acceptCharset,
                                                          final byte[] bytes,
                                                          final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                                          final byte[] expectedBytes) {
        this.setBodyRequestWithAcceptEncodingAndCheck(acceptCharset,
                bytes,
                null,
                expectedHeaders,
                expectedBytes);
    }

    private void setBodyRequestWithAcceptEncodingAndCheck(final String acceptCharset,
                                                          final byte[] bytes,
                                                          final String contentEncoding,
                                                          final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                                          final byte[] expectedBytes) {
        final Latch set = Latch.create();
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        final HttpResponse response = this.createResponse(
                acceptCharset,
                new FakeHttpResponse() {

                    @Override
                    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
                        headers.put(name, name.checkValue(value));
                    }

                    public Map<HttpHeaderName<?>, Object> headers() {
                        return Maps.readOnly(headers);
                    }

                    @Test
                    public void setBody(final byte[] b) {
                        assertArrayEquals("bytes", expectedBytes, b);
                        set.set("setBody");
                    }
                });
        if (null != contentEncoding) {
            response.addHeader(HttpHeaderName.CONTENT_ENCODING,
                    HeaderValueToken.parse(contentEncoding));
        }
        response.setBody(bytes);
        assertTrue("wrapped response setBody(bytes) not called", set.value());
        assertEquals("headers", expectedHeaders, response.headers());
    }

    @Test
    public void testSetBodyText() {
        final Latch set = Latch.create();
        final String text = "anc123";
        this.createResponse(new FakeHttpResponse() {
            @Test
            public void setBodyText(final String t) {
                assertSame("text", text, t);
                set.set("setBodyText");
            }
        }).setBodyText(text);
        assertTrue("wrapped response setBodyText(text) not called", set.value());
    }

    private AutoGzipEncodingHttpResponse createResponse(final String acceptCharset, final HttpResponse response) {
        return AutoGzipEncodingHttpResponse.with(
                this.request(acceptCharset),
                response);
    }

    @Override
    AutoGzipEncodingHttpResponse createResponse(final HttpRequest request, final HttpResponse response) {
        return AutoGzipEncodingHttpResponse.with(request, response);
    }

    @Override
    HttpRequest request() {
        return this.request(Maps.ordered());
    }

    private HttpRequest request(final String acceptEncoding) {
        return this.request(Maps.one(HttpHeaderName.ACCEPT_ENCODING, HeaderValueToken.parseList(acceptEncoding)));
    }

    private HttpRequest request(final Map<HttpHeaderName<?>, Object> headers) {
        return new FakeHttpRequest() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.readOnly(headers);
            }
        };
    }

    @Override
    protected Class<AutoGzipEncodingHttpResponse> type() {
        return AutoGzipEncodingHttpResponse.class;
    }
}
