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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HeaderValueToken;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.assertSame;

public final class DefaultHeadersHttpResponseTest extends WrapperHttpResponseTestCase<DefaultHeadersHttpResponse> {

    @Test(expected = NullPointerException.class)
    public void testWithNullHeadersFails() {
        DefaultHeadersHttpResponse.with(null, HttpResponses.fake());
    }

    @Test
    public void testEmptyHeadersNotWrapped() {
        final HttpResponse wrapped = HttpResponses.fake();
        assertSame(wrapped, DefaultHeadersHttpResponse.with(Maps.empty(), wrapped));
    }

    @Test
    public void testOnlyAddsDefaultsForMissingHeadersForFirstEntity() {
        final HttpStatus status = HttpStatusCode.OK.status();

        final int contentLength = 26;

        final Map<HttpHeaderName<?>, Object> responseHeaders = Maps.ordered();
        responseHeaders.put(HttpHeaderName.CONTENT_LENGTH, 0L + contentLength);
        responseHeaders.put(HttpHeaderName.LAST_MODIFIED, LocalDateTime.of(2000, 12, 31, 6, 28, 29));
        responseHeaders.put(HttpHeaderName.SERVER, "Replaces original value Server 123");

        final byte[] body = new byte[contentLength];

        final TestHttpResponse wrapped = new TestHttpResponse();
        final HttpRequest request = HttpRequests.fake();
        DefaultHeadersHttpResponse response = this.createResponse(request, wrapped);
        response.setStatus(status);
        response.addEntity(HttpEntity.with(responseHeaders, body));

        final HttpEntity second = HttpEntity.with(Maps.one(HttpHeaderName.SERVER, "Server2"), new byte[2]);
        response.addEntity(second);

        final Map<HttpHeaderName<?>, Object> finalHeaders = Maps.ordered();
        finalHeaders.putAll(this.headers());
        finalHeaders.putAll(responseHeaders);

        wrapped.check(request,
                status,
                HttpEntity.with(finalHeaders, body),
                second);
    }

    @Override
    HttpRequest createRequest() {
        return HttpRequests.fake();
    }

    @Override
    DefaultHeadersHttpResponse createResponse(final HttpRequest request, final HttpResponse response) {
        return Cast.to(this.createResponse(this.headers(), response));
    }

    private Map<HttpHeaderName<?>, Object> headers() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_ENCODING, HeaderValueToken.parse("EN"));
        headers.put(HttpHeaderName.SERVER, "Server 123");
        return headers;
    }

    private DefaultHeadersHttpResponse createResponse(final Map<HttpHeaderName<?>, Object> headers,
                                                      final HttpResponse response) {
        return Cast.to(DefaultHeadersHttpResponse.with(headers, response));
    }

    @Override
    protected Class<DefaultHeadersHttpResponse> type() {
        return DefaultHeadersHttpResponse.class;
    }
}
