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

import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.tree.Node;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Router which accepts a request and then dispatches after testing the {@link HttpMethod}. This is the product of
 * {@link HateosHandlerBuilder}.
 */
final class HateosHandlerRouterHttpRequestHttpResponseBiConsumer<N extends Node<N, ?, ?, ?>,
        H extends HateosContentType<N>>
        implements BiConsumer<HttpRequest, HttpResponse> {

    /**
     * Factory called by {@link HateosHandlerRouter#route}
     */
    static <N extends Node<N, ?, ?, ?>,
            H extends HateosContentType<N>> HateosHandlerRouterHttpRequestHttpResponseBiConsumer<N, H> with(final HateosHandlerRouter<N> router) {
        return new HateosHandlerRouterHttpRequestHttpResponseBiConsumer<N, H>(router);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerRouterHttpRequestHttpResponseBiConsumer(final HateosHandlerRouter<N> router) {
        super();
        this.router = router;
    }

    /**
     * Tests the method and calls the matching {@link HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest}.
     */
    @Override
    public void accept(final HttpRequest request, final HttpResponse response) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(response, "response");

        try {
            final HttpMethod httpMethod = request.method();
            if (httpMethod.equals(HttpMethod.GET) ||
                    httpMethod.equals(HttpMethod.POST) ||
                    httpMethod.equals(HttpMethod.PUT) ||
                    httpMethod.equals(HttpMethod.DELETE)) {
                HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest.with(this.router, request, response)
                        .execute();
            } else {
                methodNotAllowed(httpMethod.value(), response);
            }
        } catch (final UnsupportedOperationException unsupported) {
            response.setStatus(HttpStatusCode.NOT_IMPLEMENTED.setMessageOrDefault(unsupported.getMessage()));
        } catch (final IllegalArgumentException badRequest) {
            response.setStatus(HttpStatusCode.BAD_REQUEST.setMessageOrDefault(badRequest.getMessage()));
        }
    }

    static void methodNotAllowed(final String message,
                                 final HttpResponse response) {
        response.setStatus(HttpStatusCode.METHOD_NOT_ALLOWED.setMessage(message));
    }

    private final HateosHandlerRouter<N> router;

    @Override
    public String toString() {
        return this.router.toString();
    }
}
