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

import java.util.function.BiConsumer;

/**
 * Router which accepts a request and then dispatches after testing the {@link HttpMethod}. This is the product of
 * {@link HateosHandlerBuilder}.
 */
final class HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer<N extends Node<N, ?, ?, ?>, V> implements BiConsumer<HttpRequest, HttpResponse> {

    /**
     * Factory called by {@link HateosHandlerBuilder#build()}
     */
    static <N extends Node<N, ?, ?, ?>, V> HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer<N, V> with(final HateosHandlerBuilderRouter<N, V> router) {
        return new HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer<N, V>(router);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer(final HateosHandlerBuilderRouter<N, V> router) {
        super();
        this.router = router;
    }

    /**
     * Tests the method and calls a sub class of {@link HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerMethod}.
     */
    @Override
    public void accept(final HttpRequest request, final HttpResponse response) {
        do {
            final HttpMethod httpMethod = request.method();
            if (httpMethod.equals(HttpMethod.GET)) {
                HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerGet.with(this.router, request, response)
                        .execute();
                break;
            }
            if (httpMethod.equals(HttpMethod.POST)) {
                HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerPost.with(this.router, request, response)
                        .execute();
                break;
            }
            if (httpMethod.equals(HttpMethod.PUT)) {
                HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerPut.with(this.router, request, response)
                        .execute();
                break;
            }
            if (httpMethod.equals(HttpMethod.DELETE)) {
                HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerDelete.with(this.router, request, response)
                        .execute();
                break;
            }
            methodNotAllowed(httpMethod.value(), response);
            break;
        } while (false);
    }

    static void methodNotAllowed(final String message,
                                 final HttpResponse response) {
        response.setStatus(HttpStatusCode.METHOD_NOT_ALLOWED.setMessage(message));
    }

    final HateosHandlerBuilderRouter<N, V> router;

    @Override
    public String toString() {
        return this.router.toString();
    }
}
