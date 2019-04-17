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
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.compare.Range;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Holds all the mappers for a router mapping.
 */
public final class HateosHandlerRouterMapper<I extends Comparable<I>, R extends HateosResource<?>, S extends HateosResource<?>> {

    /**
     * Created by the builder with one for each resource name.
     */
    public static <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>> HateosHandlerRouterMapper<I, R, S> with(final Function<String, I> stringToId,
                                                                                 final Class<R> inputResourceType,
                                                                                 final Class<S> outputResourceType) {
        Objects.requireNonNull(stringToId, "stringToId");
        Objects.requireNonNull(inputResourceType, "inputResourceType");
        Objects.requireNonNull(outputResourceType, "outputResourceType");

        return new HateosHandlerRouterMapper<>(stringToId, inputResourceType);
    }

    private HateosHandlerRouterMapper(final Function<String, I> stringToId,
                                      final Class<R> inputResourceType) {
        super();
        this.stringToId = stringToId;
        this.inputResourceType = inputResourceType;
    }

    // HateosIdResourceResourceHandler.............................................................................................

    /**
     * Adds or replaces a GET {@link HateosHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> get(final HateosHandler<I, R, S> handler) {
        this.get = this.checkHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> post(final HateosHandler<I, R, S> handler) {
        this.post = this.checkHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> put(final HateosHandler<I, R, S> handler) {
        this.put = this.checkHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> delete(final HateosHandler<I, R, S> handler) {
        this.delete = this.checkHandler(handler);
        return this;
    }

    private HateosHandler<I, R, S> checkHandler(final HateosHandler<I, R, S> handler) {
        Objects.requireNonNull(handler, "handler");
        return handler;
    }

    // helpers.................................................................................................

    /**
     * Makes a defensive copy of this mappers. This is necessary when the router is created and the map copied from the builder.
     */
    HateosHandlerRouterMapper<I, R, S> copy() {
        final HateosHandlerRouterMapper<I, R, S> handlers = new HateosHandlerRouterMapper<>(this.stringToId,
                this.inputResourceType);

        handlers.get = this.get;
        handlers.post = this.post;
        handlers.put = this.put;
        handlers.delete = this.delete;

        return handlers;
    }

    /**
     * Handles a request for a single resource with the given parameters.
     */
    <N extends Node<N, ?, ?, ?>> void handleId(final HateosResourceName resourceName,
                                               final String idText,
                                               final LinkRelation<?> linkRelation,
                                               final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final I id = this.idOrBadRequest(idText, request);
        if (null != id) {
            final HateosHandler<I, R, S> handler = this.handlerOrResponseMethodNotAllowed(resourceName,
                    linkRelation,
                    request);
            if (null != handler) {
                final String requestText = request.resourceTextOrBadRequest();
                if (null != request) {
                    final HateosContentType<N> hateosContentType = request.hateosContentType();
                    final Optional<R> requestResource = request.resourceOrBadRequest(requestText,
                            hateosContentType,
                            this.inputResourceType,
                            request);

                    if (null != requestResource) {
                        final HttpRequest httpRequest = request.request;
                        final HttpMethod method = httpRequest.method();
                        String responseText = null;
                        Optional<S> maybeResponseResource = handler.handle(id,
                                requestResource,
                                request.parameters);
                        if (maybeResponseResource.isPresent()) {
                            final S responseResource = maybeResponseResource.get();
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
    <N extends Node<N, ?, ?, ?>> void handleIdRange(final HateosResourceName resourceName,
                                                    final LinkRelation<?> linkRelation,
                                                    final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final HateosHandler<I, R, S> handler = this.handlerOrResponseMethodNotAllowed(resourceName,
                linkRelation,
                request);
        if (null != handler) {
            this.handleIdRange0(resourceName, Range.all(), handler, request);
        }
    }

    /**
     * Handles a request for a collection but with the range ends still requiring to be parsed.
     */
    <N extends Node<N, ?, ?, ?>> void handleIdRange(final HateosResourceName resourceName,
                                                    final String begin,
                                                    final String end,
                                                    final String rangeText,
                                                    final LinkRelation<?> linkRelation,
                                                    final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final Range<I> range = this.rangeOrBadRequest(begin, end, rangeText, request);
        if (null != range) {
            final HateosHandler<I, R, S> handler = this.handlerOrResponseMethodNotAllowed(resourceName, linkRelation, request);
            if (null != handler) {
                this.handleIdRange0(resourceName, range, handler, request);
            }
        }
    }

    private <N extends Node<N, ?, ?, ?>> void handleIdRange0(final HateosResourceName resourceName,
                                                             final Range<I> ids,
                                                             final HateosHandler<I, R, S> handler,
                                                             final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final String requestText = request.resourceTextOrBadRequest();
        if (null != requestText) {
            final HateosContentType<N> hateosContentType = request.hateosContentType();
            final Optional<R> requestResource = request.resourceOrBadRequest(requestText,
                    hateosContentType,
                    this.inputResourceType,
                    request);

            if (null != requestResource) {
                final HttpRequest httpRequest = request.request;
                final HttpMethod method = httpRequest.method();
                String responseText = null;
                Optional<S> maybeResponseResource = handler.handleCollection(ids,
                        requestResource,
                        request.parameters);
                if (maybeResponseResource.isPresent()) {
                    final S responseResource = maybeResponseResource.get();
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

    // helpers.........................................................................................................

    /**
     * Parses or converts the id text, reporting a bad request if this fails.
     */
    private I idOrBadRequest(final String id,
                             final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
        return this.idOrBadRequest(id, "id", id, request);
    }

    /**
     * Parses or converts the id text, reporting a bad request if this fails.
     */
    private I idOrBadRequest(final String id,
                             final String label, // id, range begin, range end
                             final String text,
                             final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
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
                                       final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
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
    private HateosHandler<I, R, S> handlerOrResponseMethodNotAllowed(final HateosResourceName resourceName,
                                                                     final LinkRelation<?> linkRelation,
                                                                     final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
        HateosHandler<I, R, S> mapping = null;

        do {
            final HttpMethod method = request.request.method();
            if (HttpMethod.GET.equals(method)) {
                mapping = this.get;
                break;
            }
            if (HttpMethod.POST.equals(method)) {
                mapping = this.post;
                break;
            }
            if (HttpMethod.PUT.equals(method)) {
                mapping = this.put;
                break;
            }
            if (HttpMethod.DELETE.equals(method)) {
                mapping = this.delete;
                break;
            }
        } while (false);

        if (null == mapping) {
            request.methodNotAllowed(resourceName, linkRelation);
        }
        return mapping;
    }

    /**
     * These mappers will be null to indicate the method is not supported, otherwise the handler is invoked.
     */
    HateosHandler<I, R, S> get;
    HateosHandler<I, R, S> post;
    HateosHandler<I, R, S> put;
    HateosHandler<I, R, S> delete;

    final Class<R> inputResourceType;

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        b.label(HttpMethod.GET.toString()).value(this.get);
        b.label(HttpMethod.POST.toString()).value(this.post);
        b.label(HttpMethod.PUT.toString()).value(this.put);
        b.label(HttpMethod.DELETE.toString()).value(this.delete);

        return b.build();
    }
}
