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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.TokenHeaderValue;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.test.Latch;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AutoGzipEncodingHttpResponseTest extends WrapperHttpRequestHttpResponseTestCase<AutoGzipEncodingHttpResponse> {

    private final static String GZIP = "gzip";

    @Test
    public void testAddEntityRequestMissingAcceptEncodingFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.createResponse().addEntity(HttpEntity.with(HttpEntity.NO_HEADERS, new byte[0]));
        });
    }

    @Test
    public void testAddEntityRequestAcceptEncodingNotGzip() {
        final byte[] body = new byte[]{1, 2, 3};
        this.addEntityRequestWithAcceptEncodingAndCheck("X",
                body,
                Maps.empty(),
                body);
    }

    @Test
    public void testAddEntityAcceptEncodingNotGzip() {
        final byte[] body = new byte[]{1, 2, 3};
        this.addEntityRequestWithAcceptEncodingAndCheck("X",
                body,
                Maps.empty(),
                body);
    }

    @Test
    public void testAddEntityRequestAcceptEncodingWildcard() {
        final byte[] body = new byte[]{1, 2, 3};
        this.addEntityRequestWithAcceptEncodingAndCheck("*",
                body,
                this.headersContentEncoding(GZIP),
                this.gzip(body));
    }

    @Test
    public void testAddEntityRequestAcceptEncodingGzip() {
        final byte[] body = new byte[]{1, 2, 3};
        this.addEntityRequestWithAcceptEncodingAndCheck("*",
                body,
                this.headersContentEncoding(GZIP),
                this.gzip(body));
    }

    @Test
    public void testAddEntityRequestAcceptEncodingGzipResponseContentEncodingNotGzip() {
        final byte[] body = new byte[]{1, 2, 3};
        this.addEntityRequestWithAcceptEncodingAndCheck("*",
                body,
                "X",
                this.headersContentEncoding("X"),
                body);
    }

    @Test
    public void testAddEntityRequestAcceptEncodingGzipResponseContentEncodingGzip() {
        final byte[] body = new byte[]{1, 2, 3, 4};
        this.addEntityRequestWithAcceptEncodingAndCheck("*",
                body,
                GZIP,
                this.headersContentEncoding(GZIP),
                this.gzip(body));
    }

    private Map<HttpHeaderName<?>, Object> headersContentEncoding(final String headerValue) {
        return Maps.one(HttpHeaderName.CONTENT_ENCODING, TokenHeaderValue.parse(headerValue));
    }

    private byte[] gzip(final byte[] body) {
        return AutoGzipEncodingHttpResponse.gzip(body);
    }

    private void addEntityRequestWithAcceptEncodingAndCheck(final String acceptCharset,
                                                          final byte[] body,
                                                          final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                                          final byte[] expectedBody) {
        this.addEntityRequestWithAcceptEncodingAndCheck(acceptCharset,
                body,
                null,
                expectedHeaders,
                expectedBody);
    }

    private void addEntityRequestWithAcceptEncodingAndCheck(final String acceptCharset,
                                                          final byte[] body,
                                                          final String contentEncoding,
                                                          final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                                          final byte[] expectedBody) {
        final Latch set = Latch.create();
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        final HttpResponse response = this.createResponse(
                acceptCharset,
                new FakeHttpResponse() {
                    
                    @Test
                    public void addEntity(final HttpEntity e) {
                        assertEquals(HttpEntity.with(expectedHeaders, expectedBody),
                                e,
                                "entity");
                        set.set("addEntity");
                    }
                });
        if (null != contentEncoding) {
            headers.put(HttpHeaderName.CONTENT_ENCODING, TokenHeaderValue.parse(contentEncoding));
        }
        response.addEntity(HttpEntity.with(headers, body));
        assertTrue(set.value(), "wrapped response addEntity(body) not called");
    }

    private AutoGzipEncodingHttpResponse createResponse(final String acceptCharset, final HttpResponse response) {
        return AutoGzipEncodingHttpResponse.with(
                this.createRequest(acceptCharset),
                response);
    }

    @Override
    AutoGzipEncodingHttpResponse createResponse(final HttpRequest request, final HttpResponse response) {
        return AutoGzipEncodingHttpResponse.with(request, response);
    }

    @Override
    HttpRequest createRequest() {
        return this.createRequest(Maps.ordered());
    }

    private HttpRequest createRequest(final String acceptEncoding) {
        return this.createRequest(Maps.one(HttpHeaderName.ACCEPT_ENCODING, TokenHeaderValue.parseList(acceptEncoding)));
    }

    private HttpRequest createRequest(final Map<HttpHeaderName<?>, Object> headers) {
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
