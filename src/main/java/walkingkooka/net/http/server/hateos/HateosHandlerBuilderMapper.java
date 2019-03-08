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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Holds all the mappers for a router mapping.
 */
public final class HateosHandlerBuilderMapper<I extends Comparable<I>, R extends HateosResource<I>> {

    /**
     * Created by the builder with one for each resource name.
     */
    public static <I extends Comparable<I>, R extends HateosResource<I>> HateosHandlerBuilderMapper<I, R> with(final Function<String, I> stringToId,
                                                                                                               final Class<R> resourceType) {
        Objects.requireNonNull(stringToId, "stringToId");
        Objects.requireNonNull(resourceType, "resourceType");

        return new HateosHandlerBuilderMapper(stringToId, resourceType);
    }

    private HateosHandlerBuilderMapper(final Function<String, I> stringToId,
                                       final Class<R> resourceType) {
        super();
        this.stringToId = stringToId;
        this.resourceType = resourceType;
    }

    /**
     * Adds or replaces a GET handler.
     */
    public HateosHandlerBuilderMapper<I, R> get(final HateosHandler<I, R> handler) {
        checkHandler(handler);
        this.get = handler;
        return this;
    }

    /**
     * Adds or replaces a POST handler.
     */
    public HateosHandlerBuilderMapper<I, R> post(final HateosHandler<I, R> handler) {
        checkHandler(handler);
        this.post = handler;
        return this;
    }

    /**
     * Adds or replaces a PUT handler.
     */
    public HateosHandlerBuilderMapper<I, R> put(final HateosHandler<I, R> handler) {
        checkHandler(handler);
        this.put = handler;
        return this;
    }

    /**
     * Adds or replaces a DELETE handler.
     */
    public HateosHandlerBuilderMapper<I, R> delete(final HateosHandler<I, R> handler) {
        checkHandler(handler);
        this.delete = handler;
        return this;
    }

    private void checkHandler(final HateosHandler<I, R> handler) {
        Objects.requireNonNull(handler, "handler");
    }

    /**
     * Makes a defensive copy of this mappers. This is necessary when the router is created and the map copied from the builder.
     */
    HateosHandlerBuilderMapper copy() {
        final HateosHandlerBuilderMapper handlers = new HateosHandlerBuilderMapper(this.stringToId, this.resourceType);

        handlers.get = this.get;
        handlers.post = this.post;
        handlers.put = this.put;
        handlers.delete = this.delete;

        return handlers;
    }

    /**
     * Handles a request for a single resource with the given parameters.
     */
    <N extends Node<N, ?, ?, ?>, H extends HateosContentType<N>> void handleId(final HateosResourceName resourceName,
                                                                               final String idText,
                                                                               final LinkRelation<?> linkRelation,
                                                                               final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<N, H> request) {
        final I id = this.idOrBadRequest(idText, request);
        if (null != id) {
            final HateosHandler<I, R> handler = this.handlerOrResponseMethodNotAllowed(resourceName,
                    linkRelation,
                    request);
            if (null != handler) {
                final String requestText = request.resourceTextOrBadRequest();
                if (null!=request) {
                    final HateosContentType<N> hateosContentType = request.hateosContentType();
                    Optional<R> requestResource;

                    if(requestText.isEmpty()) {
                        requestResource = Optional.empty();
                    } else {
                        final Class<R> resourceType = this.resourceType;
                        try {
                            requestResource = Optional.of(hateosContentType.fromNode(requestText, null, resourceType));
                        } catch (final Exception cause) {
                            request.badRequest("Invalid " + hateosContentType + ": " + cause.getMessage());
                            requestResource = null;
                        }
                    }

                    if (null != requestResource) {
                        final HttpRequest httpRequest = request.request;
                        final HttpMethod method = httpRequest.method();
                        String responseText = null;
                        Optional<R> maybeResponseResource = handler.handle(id,
                                requestResource,
                                request.parameters);
                        if (maybeResponseResource.isPresent()) {
                            final R responseResource = maybeResponseResource.get();
                            responseText = hateosContentType.toText(responseResource,
                                            null,
                                            method,
                                            request.router.base,
                                            resourceName,
                                            request.router.linkRelations(resourceName));
                        }

                        request.setStatusAndBody(method + " resource successful", responseText);
                    }
                }
            }
        }
    }

    /**
     * Handles a request for ALL of a collection.
     */
    <N extends Node<N, ?, ?, ?>, H extends HateosContentType<N>> void handleCollection(final HateosResourceName resourceName,
                                                                                       final LinkRelation<?> linkRelation,
                                                                                       final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<N, H> request) {

        final HateosHandler<I, R> handler = this.handlerOrResponseMethodNotAllowed(resourceName,
                linkRelation,
                request);
        if (null != handler) {
            this.collection0(resourceName, Range.all(), handler, request);
        }
    }

    /**
     * Handles a request for a collection but with the range ends still requiring to be parsed.
     */
    <N extends Node<N, ?, ?, ?>, H extends HateosContentType<N>> void handleCollection(final HateosResourceName resourceName,
                                                                                       final String begin,
                                                                                       final String end,
                                                                                       final String rangeText,
                                                                                       final LinkRelation<?> linkRelation,
                                                                                       final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<N, H> request) {
        final Range<I> range = this.rangeOrBadRequest(begin, end, rangeText, request);
        if (null != range) {
            final HateosHandler<I, R> handler = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, request);
            if (null != handler) {
                this.collection0(resourceName, range, handler, request);
            }
        }
    }

    private <N extends Node<N, ?, ?, ?>, H extends HateosContentType<N>> void collection0(final HateosResourceName resourceName,
                                                                                          final Range<I> ids,
                                                                                          final HateosHandler<I, R> handler,
                                                                                          final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<N, H> request) {
        final String requestText = request.resourceTextOrBadRequest();
        if (null != requestText) {
            final HateosContentType<N> hateosContentType = request.hateosContentType();
            List<R> requestResources;

            if(requestText.isEmpty()) {
                requestResources = Lists.empty();
            } else {
                final Class<R> resourceType = this.resourceType;

                try {
                    requestResources = hateosContentType.fromNodeList(requestText, null, resourceType);
                } catch (final Exception cause) {
                    request.badRequest("Invalid " + hateosContentType + ": " + cause.getMessage());
                    requestResources = null;
                }
            }

            if (null != requestResources) {
                final HttpRequest httpRequest = request.request;
                final HttpMethod method = httpRequest.method();
                String responseText = null;
                final List<R> responseResources = handler.handleCollection(ids,
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

    // helpers.........................................................................................................

    /**
     * Parses or converts the id text, reporting a bad request if this fails.
     */
    private I idOrBadRequest(final String id,
                             final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<?, ?> request) {
        return this.idOrBadRequest(id, "id", id, request);
    }

    /**
     * Parses or converts the id text, reporting a bad request if this fails.
     */
    private I idOrBadRequest(final String id,
                             final String label, // id, range begin, range end
                             final String text,
                             final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<?, ?> request) {
        I parsed = null;
        try {
            parsed = this.stringToId.apply(id);
        } catch (final RuntimeException failed) {
            request.badRequest("Invalid " + label + " " + CharSequences.quote(text));
        }
        return parsed;
    }

    /**
     * Parses the range if any portion of that fails a bad request is set to the response.
     */
    private Range<I> rangeOrBadRequest(final String begin,
                                       final String end,
                                       final String rangeText,
                                       final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<?, ?> request) {
        Range<I> range = null;

        final I beginComparable = this.idOrBadRequest(begin, "range begin", rangeText, request);
        if (null != beginComparable) {
            final I endComparable = this.idOrBadRequest(end, "range end", rangeText, request);
            if (null != endComparable) {
                range = Range.greaterThanEquals(beginComparable)
                        .and(Range.lessThanEquals(endComparable));
            }
        }

        return range;
    }

    /**
     * A parser function that converts a {@link String} from the url path and returns the {@link Comparable id}.
     */
    final Function<String, I> stringToId;

    /**
     * Locates the {@link HateosHandler} for the given request method or sets the response to method not allowed.
     */
    private HateosHandler<I, R> handlerOrResponseMethodNotAllowed(final HateosResourceName resourceName,
                                                                  final LinkRelation<?> linkRelation,
                                                                  final HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumerRequest<?, ?> request) {
        HateosHandler<I, R> handler = null;

        do {
            final HttpMethod method = request.request.method();
            if (HttpMethod.GET.equals(method)) {
                handler = this.get;
                break;
            }
            if (HttpMethod.POST.equals(method)) {
                handler = this.post;
                break;
            }
            if (HttpMethod.PUT.equals(method)) {
                handler = this.put;
                break;
            }
            if (HttpMethod.DELETE.equals(method)) {
                handler = this.delete;
                break;
            }
        } while (false);

        if (null == handler) {
            request.methodNotAllowed(resourceName, linkRelation);
        }
        return handler;
    }

    /**
     * These mappers will be null to indicate the method is not supported, otherwise the handler is invoked.
     */
    HateosHandler<I, R> get;
    HateosHandler<I, R> post;
    HateosHandler<I, R> put;
    HateosHandler<I, R> delete;

    final Class<R> resourceType;

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();

        b.label(HttpMethod.GET.toString()).value(this.get);
        b.label(HttpMethod.POST.toString()).value(this.post);
        b.label(HttpMethod.PUT.toString()).value(this.put);
        b.label(HttpMethod.DELETE.toString()).value(this.delete);

        return b.build();
    }
}
