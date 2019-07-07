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

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpMethodVisitorTesting;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequests;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.net.http.server.RecordingHttpResponse;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitorTest implements HttpMethodVisitorTesting<HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<?, ?>> {

    @Test
    public void testTraceFails() {
        this.acceptFails(HttpMethod.TRACE);
    }

    @Test
    public void testOptionsFails() {
        this.acceptFails(HttpMethod.OPTIONS);
    }

    @Test
    public void testConnectFails() {
        this.acceptFails(HttpMethod.CONNECT);
    }

    @Test
    public void testPatchFails() {
        this.acceptFails(HttpMethod.PATCH);
    }

    @Test
    public void testCustomMethodFails() {
        this.acceptFails(HttpMethod.with("Custom"));
    }

    private void acceptFails(final HttpMethod method) {
        final HttpRequest request = HttpRequests.fake();
        final RecordingHttpResponse response = HttpResponses.recording();
        final HateosHandlerRouter<?> router = null; // too hard to mock

        new HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<>(request, response, router).accept(method);

        assertEquals(Optional.of(HttpStatusCode.METHOD_NOT_ALLOWED.setMessage(method.toString())),
                response.status(),
                () -> method + " " + response);
    }

    @Test
    public void testToString() {
        final HttpRequest request = HttpRequests.fake();
        final HttpResponse response = HttpResponses.fake();
        final HateosHandlerRouter<?> router = null; // too hard to mock

        this.toStringAndCheck(new HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<>(request, response, router),
                request + " " + response);
    }

    @Override
    public HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<?, ?> createVisitor() {
        return new HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<>(null, null, null);
    }

    // TypeNameTesting...................................................................................................

    @Override
    public String typeNamePrefix() {
        return HateosHandlerRouterHttpRequestHttpResponseBiConsumer.class.getSimpleName();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor<?, ?>> type() {
        return Cast.to(HateosHandlerRouterHttpRequestHttpResponseBiConsumerHttpMethodVisitor.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
