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
import java.util.Optional;

/**
 * Represents a mapping between a request and a {@link HateosHandler}.
 */
final class HateosHandlerMapperHateosMappingHandlerMapping<I extends Comparable<I>, R extends HateosResource<?>> extends HateosHandlerMapperMapping<I,R>{

    static <I extends Comparable<I>, R extends HateosResource<?>> HateosHandlerMapperHateosMappingHandlerMapping<I, R> with(final HateosMappingHandler<I, R> handler) {
        return new HateosHandlerMapperHateosMappingHandlerMapping<>(handler);
    }

    private HateosHandlerMapperHateosMappingHandlerMapping(final HateosMappingHandler<I, R> handler) {
        super();
        this.handler = handler;
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

    /**
     * Handles a request for a range of ids.
     */
    @Override
    <N extends Node<N, ?, ?, ?>> void handleCollection(final HateosResourceName resourceName,
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
            final List<R> responseResources = this.handler.handleCollection(ids,
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

    @Override
    public String toString() {
        return this.handler.toString();
    }

    private final HateosMappingHandler<I, R> handler;
}
