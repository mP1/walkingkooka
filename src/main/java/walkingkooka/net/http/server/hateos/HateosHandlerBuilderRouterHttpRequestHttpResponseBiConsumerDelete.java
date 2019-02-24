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
final class HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerDelete<N extends Node<N, ?, ?, ?>, V>
        extends HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerMethod<N, V> {

    /**
     * Factory called by {@link HateosHandlerBuilder#build()}
     */
    static <N extends Node<N, ?, ?, ?>, V> HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerDelete<N, V> with(final HateosHandlerBuilderRouter<N, V> router,
                                                                                                                  final HttpRequest request,
                                                                                                                  final HttpResponse response) {
        return new HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerDelete<N, V>(router,
                request,
                response);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerDelete(final HateosHandlerBuilderRouter<N, V> router,
                                                                              final HttpRequest request,
                                                                              final HttpResponse response) {
        super(router, request, response);
    }

    @Override
    HttpMethod method() {
        return HttpMethod.DELETE;
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
            final HateosDeleteHandler<?, N> delete = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.delete);
            if (null != delete) {
                this.collection1(Range.all(), delete);
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
                final HateosDeleteHandler<?, N> delete = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.delete);
                if (null != delete) {
                    final Optional<N> resource = this.resourceOrBadRequest();
                    if (null != resource) {
                        delete.delete(Cast.to(id),
                                resource,
                                this.parameters,
                                this.router.deleteContext);
                        this.setStatusDeleted("resource");
                    }
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
                final HateosDeleteHandler<?, N> delete = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.delete);
                if (null != delete) {
                    this.collection1(range, delete);
                }
            }
        }
    }

    private void collection1(final Range<Comparable<?>> range,
                             final HateosDeleteHandler<?, N> delete) {
        final Optional<N> resource = this.resourceOrBadRequest();
        if (null != resource) {
            delete.deleteCollection(Cast.to(range),
                    resource,
                    this.parameters,
                    this.router.deleteContext);
            this.setStatusDeleted("collection");
        }
    }

    private void setStatusDeleted(final String label) {
        this.setStatus(HttpStatusCode.NO_CONTENT, "Delete " + label + " successful");
    }
}
