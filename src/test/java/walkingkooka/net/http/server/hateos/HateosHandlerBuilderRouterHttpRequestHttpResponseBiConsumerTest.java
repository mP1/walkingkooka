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
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.FakeHttpRequest;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.net.http.server.TestRecordingHttpResponse;
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

public final class HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerTest implements ClassTesting2<HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer<JsonNode, HasJsonNode>> {

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
        final TestRecordingHttpResponse response = HttpResponses.testRecording();
        HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer.with(null)
                .accept(request, response);
        response.check(request, HttpStatusCode.METHOD_NOT_ALLOWED.setMessage("XYZ"));
    }

    @Override
    public Class<HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer<JsonNode, HasJsonNode>> type() {
        return Cast.to(HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
