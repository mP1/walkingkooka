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
import walkingkooka.compare.Range;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

import java.util.Objects;
import java.util.function.Function;

/**
 * Holds all the mappers for a router mapping.
 */
public final class HateosHandlerMapper<I extends Comparable<I>, R extends HateosResource<?>, S extends HateosResource<?>> {

    /**
     * Created by the builder with one for each resource name.
     */
    public static <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>> HateosHandlerMapper<I, R, S> with(final Function<String, I> stringToId,
                                                                           final Class<R> inputResourceType,
                                                                           final Class<S> outputResourceType) {
        Objects.requireNonNull(stringToId, "stringToId");
        Objects.requireNonNull(inputResourceType, "inputResourceType");
        Objects.requireNonNull(outputResourceType, "outputResourceType");

        return new HateosHandlerMapper<>(stringToId, inputResourceType);
    }

    private HateosHandlerMapper(final Function<String, I> stringToId,
                                final Class<R> inputResourceType) {
        super();
        this.stringToId = stringToId;
        this.inputResourceType = inputResourceType;
    }

    // HateosHandler.............................................................................................

    /**
     * Adds or replaces a GET {@link HateosHandler}.
     */
    public HateosHandlerMapper<I, R, S> get(final HateosHandler<I, R, S> handler) {
        checkHandler(handler);
        this.get = HateosHandlerMapperMapping.hateosHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosHandler}.
     */
    public HateosHandlerMapper<I, R, S> post(final HateosHandler<I, R, S> handler) {
        checkHandler(handler);
        this.post = HateosHandlerMapperMapping.hateosHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosHandler}.
     */
    public HateosHandlerMapper<I, R, S> put(final HateosHandler<I, R, S> handler) {
        checkHandler(handler);
        this.put = HateosHandlerMapperMapping.hateosHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosHandler}.
     */
    public HateosHandlerMapper<I, R, S> delete(final HateosHandler<I, R, S> handler) {
        checkHandler(handler);
        this.delete = HateosHandlerMapperMapping.hateosHandler(handler);
        return this;
    }

    // HateosCollectionHandler.............................................................................................

    /**
     * Adds or replaces a GET {@link HateosCollectionHandler}.
     */
    public HateosHandlerMapper<I, R, S> get(final HateosCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.get = HateosHandlerMapperMapping.hateosCollectionHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosCollectionHandler}.
     */
    public HateosHandlerMapper<I, R, S> post(final HateosCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.post = HateosHandlerMapperMapping.hateosCollectionHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosCollectionHandler}.
     */
    public HateosHandlerMapper<I, R, S> put(final HateosCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.put = HateosHandlerMapperMapping.hateosCollectionHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosCollectionHandler}.
     */
    public HateosHandlerMapper<I, R, S> delete(final HateosCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.delete = HateosHandlerMapperMapping.hateosCollectionHandler(handler);
        return this;
    }

    // HateosMappingHandler.............................................................................................

    /**
     * Adds or replaces a GET {@link HateosMappingHandler}.
     */
    public HateosHandlerMapper<I, R, S> get(final HateosMappingHandler<I, R, S> handler) {
        checkHandler(handler);
        this.get = HateosHandlerMapperMapping.hateosMappingHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosMappingHandler}.
     */
    public HateosHandlerMapper<I, R, S> post(final HateosMappingHandler<I, R, S> handler) {
        checkHandler(handler);
        this.post = HateosHandlerMapperMapping.hateosMappingHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosMappingHandler}.
     */
    public HateosHandlerMapper<I, R, S> put(final HateosMappingHandler<I, R, S> handler) {
        checkHandler(handler);
        this.put = HateosHandlerMapperMapping.hateosMappingHandler(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosMappingHandler}.
     */
    public HateosHandlerMapper<I, R, S> delete(final HateosMappingHandler<I, R, S> handler) {
        checkHandler(handler);
        this.delete = HateosHandlerMapperMapping.hateosMappingHandler(handler);
        return this;
    }

    // helpers.................................................................................................

    private void checkHandler(final Object handler) {
        Objects.requireNonNull(handler, "handler");
    }

    /**
     * Makes a defensive copy of this mappers. This is necessary when the router is created and the map copied from the builder.
     */
    HateosHandlerMapper<I, R, S> copy() {
        final HateosHandlerMapper<I, R, S> handlers = new HateosHandlerMapper<>(this.stringToId,
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
            final HateosHandlerMapperMapping<I, R, S> mapping = this.mappingOrResponseMethodNotAllowed(resourceName,
                    linkRelation,
                    request);
            if (null != mapping) {
                final String requestText = request.resourceTextOrBadRequest();
                if (null != request) {
                    mapping.handleId(resourceName,
                            id,
                            linkRelation,
                            requestText,
                            this.inputResourceType,
                            request);
                }
            }
        }
    }

    /**
     * Handles a request for ALL of a collection.
     */
    <N extends Node<N, ?, ?, ?>> void handleCollection(final HateosResourceName resourceName,
                                                       final LinkRelation<?> linkRelation,
                                                       final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {

        final HateosHandlerMapperMapping<I, R, S> mapping = this.mappingOrResponseMethodNotAllowed(resourceName,
                linkRelation,
                request);
        if (null != mapping) {
            this.collection0(resourceName, Range.all(), mapping, request);
        }
    }

    /**
     * Handles a request for a collection but with the range ends still requiring to be parsed.
     */
    <N extends Node<N, ?, ?, ?>> void handleCollection(final HateosResourceName resourceName,
                                                       final String begin,
                                                       final String end,
                                                       final String rangeText,
                                                       final LinkRelation<?> linkRelation,
                                                       final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final Range<I> range = this.rangeOrBadRequest(begin, end, rangeText, request);
        if (null != range) {
            final HateosHandlerMapperMapping<I, R, S> mapping = this.mappingOrResponseMethodNotAllowed(resourceName, linkRelation, request);
            if (null != mapping) {
                this.collection0(resourceName, range, mapping, request);
            }
        }
    }

    private <N extends Node<N, ?, ?, ?>> void collection0(final HateosResourceName resourceName,
                                                          final Range<I> ids,
                                                          final HateosHandlerMapperMapping<I, R, S> mapping,
                                                          final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final String requestText = request.resourceTextOrBadRequest();
        if (null != requestText) {
            mapping.handleCollection(resourceName,
                    ids,
                    requestText,
                    this.inputResourceType,
                    request);
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
     * Locates the {@link HateosHandlerMapperMapping} for the given request method or sets the response to method not allowed.
     */
    private HateosHandlerMapperMapping<I, R, S> mappingOrResponseMethodNotAllowed(final HateosResourceName resourceName,
                                                                               final LinkRelation<?> linkRelation,
                                                                               final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
        HateosHandlerMapperMapping<I, R, S> mapping = null;

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
    HateosHandlerMapperMapping<I, R, S> get;
    HateosHandlerMapperMapping<I, R, S> post;
    HateosHandlerMapperMapping<I, R, S> put;
    HateosHandlerMapperMapping<I, R, S> delete;

    final Class<R> inputResourceType;

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
