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

import walkingkooka.compare.Range;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.tree.Node;

import java.util.List;

/**
 * A {@link HateosHandlerRouterMapperHateosIdHandlerMapping} for {@link HateosIdRangeResourceCollectionResourceCollectionHandler}.
 */
final class HateosHandlerRouterMapperHateosIdRangeResourceCollectionResourceCollectionHandlerMapping<I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends HateosHandlerRouterMapperHateosIdRangeHandlerMapping<HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S>, I, R, S> {

    static <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>>
    HateosHandlerRouterMapperHateosIdRangeResourceCollectionResourceCollectionHandlerMapping<I, R, S> with(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        return new HateosHandlerRouterMapperHateosIdRangeResourceCollectionResourceCollectionHandlerMapping<>(handler);
    }

    private HateosHandlerRouterMapperHateosIdRangeResourceCollectionResourceCollectionHandlerMapping(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        super(handler);
    }

    /**
     * Handles a request for a single resource with the given parameters.
     */
    final <N extends Node<N, ?, ?, ?>> void handleId(final HateosResourceName resourceName,
                                                     final I id,
                                                     final LinkRelation<?> linkRelation,
                                                     final String requestText,
                                                     final Class<R> resourceType,
                                                     final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Handles a request for a range of ids returning a list of resources.
     */
    final <N extends Node<N, ?, ?, ?>> void handleIdRange(final HateosResourceName resourceName,
                                                          final Range<I> ids,
                                                          final String requestText,
                                                          final Class<R> resourceType,
                                                          final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final HateosContentType<N> hateosContentType = request.hateosContentType();
        final List<R> requestResources = request.resourcesListOrBadRequest(requestText,
                hateosContentType,
                resourceType,
                request);

        if (null != requestResources) {
            final HttpRequest httpRequest = request.request;
            final HttpMethod method = httpRequest.method();
            String responseText = null;
            final List<S> responseResources = this.handler.handle(ids,
                    requestResources,
                    request.parameters);
            if (!responseResources.isEmpty()) {
                final AbsoluteUrl base = request.router.base;
                final List<LinkRelation<?>> linkRelations = request.router.linkRelations(resourceName);

                responseText = hateosContentType.toTextList(responseResources,
                        null,
                        method,
                        base,
                        resourceName,
                        linkRelations);
            }

            request.setStatusAndBody(method + " resource collection successful",
                    responseText);
        }
    }
}
