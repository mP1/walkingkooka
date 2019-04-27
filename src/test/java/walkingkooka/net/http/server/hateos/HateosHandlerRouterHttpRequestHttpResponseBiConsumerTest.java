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

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.FakeHttpRequest;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.net.http.server.RecordingHttpResponse;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HateosHandlerRouterHttpRequestHttpResponseBiConsumerTest extends HateosHandlerRouterTestCase<HateosHandlerRouterHttpRequestHttpResponseBiConsumer<JsonNode, HateosContentTypeJsonNode>> {

    @Test
    public void testMethodNotSupported() {
        final FakeHttpRequest request = new FakeHttpRequest() {
            @Override
            public HttpMethod method() {
                return HttpMethod.with("XYZ");
            }

            @Override
            public String toString() {
                return this.method().toString();
            }
        };
        final RecordingHttpResponse response = HttpResponses.recording();
        HateosHandlerRouterHttpRequestHttpResponseBiConsumer.with(null)
                .accept(request, response);
        this.checkResponse(response,
                request,
                HttpStatusCode.METHOD_NOT_ALLOWED.setMessage("XYZ"));
    }

    private void checkResponse(final RecordingHttpResponse response,
                               final HttpRequest request,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final HttpResponse expected = HttpResponses.recording();
        expected.setStatus(status);

        Arrays.stream(entities)
                .forEach(expected::addEntity);

        assertEquals(expected,
                response,
                () -> request.toString());
    }

    @Override
    public Class<HateosHandlerRouterHttpRequestHttpResponseBiConsumer<JsonNode, HateosContentTypeJsonNode>> type() {
        return Cast.to(HateosHandlerRouterHttpRequestHttpResponseBiConsumer.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    String typeNamePrefix2() {
        return "HttpRequestHttpResponse";
    }

    @Override
    public String typeNameSuffix() {
        return BiConsumer.class.getSimpleName();
    }
}
