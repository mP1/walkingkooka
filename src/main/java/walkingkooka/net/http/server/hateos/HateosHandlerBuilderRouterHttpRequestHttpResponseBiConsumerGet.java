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

import walkingkooka.Cast;
import walkingkooka.compare.Range;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.tree.Node;

import java.util.Optional;

/**
 * Router which accepts a request and then dispatches after testing the {@link HttpMethod}. This is the product of
 * {@link HateosHandlerBuilder}.
 */
final class HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerGet<N extends Node<N, ?, ?, ?>, V>
        extends HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerMethod<N, V> {

    /**
     * Factory called by {@link HateosHandlerBuilder#build()}
     */
    static <N extends Node<N, ?, ?, ?>, V> HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerGet<N, V> with(final HateosHandlerBuilderRouter<N, V> router,
                                                                                                               final HttpRequest request,
                                                                                                               final HttpResponse response) {
        return new HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerGet<N, V>(router,
                request,
                response);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerGet(final HateosHandlerBuilderRouter<N, V> router,
                                                                           final HttpRequest request,
                                                                           final HttpResponse response) {
        super(router, request, response);
    }

    @Override
    HttpMethod method() {
        return HttpMethod.GET;
    }

    @Override
    void idMissing(final HateosResourceName resourceName,
                   final LinkRelation<?> linkRelation) {
        this.collection0(resourceName, linkRelation);
    }

    @Override
    void wildcard(final HateosResourceName resourceName,
                  final LinkRelation<?> linkRelation) {
        this.collection0(resourceName, linkRelation);
    }

    private void collection0(final HateosResourceName resourceName,
                             final LinkRelation<?> linkRelation) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.handlersOrResponseNotFound(resourceName, linkRelation);
        if (null != handlers) {
            final HateosGetHandler<?, N> get = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.get);
            if (null != get) {
                this.collection1(Range.all(), get);
            }
        }
    }

    @Override
    void id(final HateosResourceName resourceName,
            final String idText,
            final LinkRelation<?> linkRelation) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.handlersOrResponseNotFound(resourceName, linkRelation);
        if (null != handlers) {
            final Comparable<?> id = this.idOrBadRequest(idText, handlers);
            if (null != id) {
                final HateosGetHandler<?, N> get = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.get);
                if (null != get) {
                    this.setStatusAndBody("Get resource successful",
                            get.get(Cast.to(id),
                                    this.parameters,
                                    this.router.getContext));
                }
            }
        }
    }

    @Override
    void collection(final HateosResourceName resourceName,
                    final String beginText,
                    final String endText,
                    final String rangeText,
                    final LinkRelation<?> linkRelation) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.handlersOrResponseNotFound(resourceName, linkRelation);
        if (null != handlers) {
            final Range<Comparable<?>> range = this.rangeOrBadRequest(beginText, endText, handlers, rangeText);
            if (null != range) {
                final HateosGetHandler<?, N> get = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.get);
                if (null != get) {
                    this.collection1(range, get);
                }
            }
        }
    }

    private void collection1(final Range<Comparable<?>> range,
                             final HateosGetHandler<?, N> get) {
        this.setStatusAndBody("Get collection successful",
                get.getCollection(Cast.to(range),
                        this.parameters,
                        this.router.getContext));
    }

    final void setStatusAndBody(final String message, final Optional<N> node) {
        if (node.isPresent()) {
            this.setStatusAndBody(message, node.get());
        } else {
            this.setStatus(HttpStatusCode.NO_CONTENT, "No content available");
        }
    }
}
