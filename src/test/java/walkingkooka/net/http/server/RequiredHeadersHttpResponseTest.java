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
import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpStatusCode;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RequiredHeadersHttpResponseTest extends BufferingHttpResponseTestCase<RequiredHeadersHttpResponse> {

    // test.........................................................................................

    @Test
    public void testWithRequestNullFails() {
        assertThrows(NullPointerException.class, () -> {
            RequiredHeadersHttpResponse.with(null,
                    HttpResponses.fake());
        });
    }

    @Test
    public void testWithRequest10() {
        final HttpResponse response = HttpResponses.fake();
        assertSame(response, this.createResponse(HttpProtocolVersion.VERSION_1_0, response));
    }

    @Test
    public void testWithRequest2() {
        final HttpResponse response = HttpResponses.fake();
        assertSame(response, this.createResponse(HttpProtocolVersion.VERSION_2, response));
    }

    @Test
    public void testWithRequest11Response1xxServerMissing() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.CONTINUE.status(), this.entityWithoutServerHeader());
    }

    @Test
    public void testWithRequest11Response2xxServerMissing() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(), this.entityWithoutServerHeader(),
                HttpStatusCode.INTERNAL_SERVER_ERROR.status());
    }

    @Test
    public void testWithRequest11Response2xxServerPresent() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(), this.entityWithServerHeader());
    }

    @Test
    public void testWithRequest11Response2xxServerPresentMultipart() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                this.entityWithServerHeader(),
                this.entityWithoutServerHeader());
    }

    @Test
    public void testWithRequest11Response3xxServerMissing() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.TEMPORARY_REDIRECT.status(),
                this.entityWithoutServerHeader(),
                HttpStatusCode.INTERNAL_SERVER_ERROR.status());
    }

    @Test
    public void testWithRequest11Response3xxServerPresent() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.TEMPORARY_REDIRECT.status(),
                this.entityWithServerHeader());
    }

    @Test
    public void testWithRequest11Response3xxServerPresentMultipart() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.TEMPORARY_REDIRECT.status(),
                this.entityWithServerHeader(),
                this.entityWithoutServerHeader());
    }

    @Test
    public void testWithRequest11Response4xxServerMissing() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.NOT_FOUND.status(),
                this.entityWithoutServerHeader());
    }

    @Test
    public void testWithRequest11Response5xxServerMissing() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.INTERNAL_SERVER_ERROR.status(),
                this.entityWithoutServerHeader());
    }

    private HttpEntity entityWithoutServerHeader() {
        return HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_TYPE, MediaType.BINARY),
                Binary.with(new byte[]{'a'}));
    }

    private HttpEntity entityWithServerHeader() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.BINARY);
        headers.put(HttpHeaderName.SERVER, "Server 123");

        return HttpEntity.with(headers, Binary.with(new byte[]{'a'}));
    }
    

    @Override
    RequiredHeadersHttpResponse createResponse(final HttpResponse response) {
        return this.createResponse(this.createRequest(), response);
    }

    private HttpResponse createResponse(final HttpProtocolVersion version,
                                        final HttpResponse response) {
        return RequiredHeadersHttpResponse.with(this.createRequest(version), response);
    }

    @Override
    RequiredHeadersHttpResponse createResponse(final HttpRequest request,
                                               final HttpResponse response) {
        return Cast.to(RequiredHeadersHttpResponse.with(request, response));
    }

    @Override
    HttpRequest createRequest() {
        return this.createRequest(HttpProtocolVersion.VERSION_1_1);
    }

    private HttpRequest createRequest(final HttpProtocolVersion version) {
        return new FakeHttpRequest() {
            @Override
            public HttpProtocolVersion protocolVersion() {
                return version;
            }

            @Override
            public String toString() {
                return this.protocolVersion().toString();
            }
        };
    }

    @Override
    public Class<RequiredHeadersHttpResponse> type() {
        return RequiredHeadersHttpResponse.class;
    }
}
