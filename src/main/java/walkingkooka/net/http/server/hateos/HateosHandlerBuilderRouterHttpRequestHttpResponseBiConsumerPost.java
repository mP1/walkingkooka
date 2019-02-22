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
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.tree.Node;

import java.util.Optional;

/**
 * Router which accepts a request and then dispatches after testing the {@link HttpMethod}. This is the product of
 * {@link HateosHandlerBuilder}.
 */
final class HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerPost<N extends Node<N, ?, ?, ?>, V>
        extends HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerMethod<N, V> {

    /**
     * Factory called by {@link HateosHandlerBuilder#build()}
     */
    static <N extends Node<N, ?, ?, ?>, V> HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerPost<N, V> with(final HateosHandlerBuilderRouter<N, V> router,
                                                                                                                final HttpRequest request,
                                                                                                                final HttpResponse response) {
        return new HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerPost<N, V>(router,
                request,
                response);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerPost(final HateosHandlerBuilderRouter<N, V> router,
                                                                            final HttpRequest request,
                                                                            final HttpResponse response) {
        super(router, request, response);
    }


    @Override
    HttpMethod method() {
        return HttpMethod.POST;
    }

    @Override
    void idMissing(final HateosResourceName resourceName,
                   final LinkRelation<?> linkRelation) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.handlersOrResponseNotFound(resourceName, linkRelation);
        if (null != handlers) {
            final HateosPostHandler<?, N> post = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.post);
            if (null != post) {
                final Optional<N> resource = this.resourceOrBadRequest();
                if (null != resource) {
                    this.setStatusAndBody("Post resource successful",
                            post.post(Cast.to(Optional.empty()),
                                    resource.get(),
                                    this.router.postContext));
                }
            }
        }
    }

    @Override
    void wildcard(final HateosResourceName resourceName,
                  final LinkRelation<?> linkRelation) {
        this.badRequestCollectionsUnsupported();
    }

    @Override
    void id(final HateosResourceName resourceName,
            final String idText,
            final LinkRelation<?> linkRelation) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.handlersOrResponseNotFound(resourceName, linkRelation);
        if (null != handlers) {
            final Comparable<?> id = this.idOrBadRequest(idText, handlers);
            if (null != id) {
                final Optional<N> resource = this.resourceOrBadRequest();
                if(null!=resource) {
                    final HateosPostHandler<?, N> post = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, handlers.post);
                    if (null != post) {
                        this.setStatusAndBody("Post resource successful",
                                post.post(Cast.to(Optional.of(id)),
                                        resource.get(),
                                        this.router.postContext));
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
        this.badRequestCollectionsUnsupported();
    }
}
