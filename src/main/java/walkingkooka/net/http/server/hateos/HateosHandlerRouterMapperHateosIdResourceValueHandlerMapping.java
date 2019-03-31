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

import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.tree.Node;

import java.util.Optional;

/**
 * A {@link HateosHandlerRouterMapperHateosIdHandlerMapping} for {@link HateosIdResourceValueHandler}.
 */
final class HateosHandlerRouterMapperHateosIdResourceValueHandlerMapping<I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends HateosHandlerRouterMapperHateosIdHandlerMapping<HateosIdResourceValueHandler<I, R, S>, I, R, S> {

    static <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>>
    HateosHandlerRouterMapperHateosIdResourceValueHandlerMapping<I, R, S> with(final HateosIdResourceValueHandler<I, R, S> handler) {
        return new HateosHandlerRouterMapperHateosIdResourceValueHandlerMapping<>(handler);
    }

    private HateosHandlerRouterMapperHateosIdResourceValueHandlerMapping(final HateosIdResourceValueHandler<I, R, S> handler) {
        super(handler);
    }

    /**
     * Handles a request for a single resource with the given parameters.
     */
    @Override
    <N extends Node<N, ?, ?, ?>> void handleId(final HateosResourceName resourceName,
                                               final I id,
                                               final LinkRelation<?> linkRelation,
                                               final String requestText,
                                               final Class<R> resourceType,
                                               final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final HateosContentType<N> hateosContentType = request.hateosContentType();
        final Optional<R> requestResource = request.resourceOrBadRequest(requestText,
                hateosContentType,
                resourceType,
                request);

        if (null != requestResource) {
            final HttpRequest httpRequest = request.request;
            final HttpMethod method = httpRequest.method();
            final Object value = this.handler.handle(id,
                    requestResource,
                    request.parameters);
            final String responseText = hateosContentType.toTextValue(value, null);

            request.setStatusAndBody(method + " resource successful", responseText);
        }
    }
}
