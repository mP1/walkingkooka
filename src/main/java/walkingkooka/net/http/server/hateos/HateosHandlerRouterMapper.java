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
import walkingkooka.collect.list.Lists;
import walkingkooka.compare.Range;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * Adds or replaces a GET {@link HateosIdRangeResourceCollectionResourceCollectionHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> get(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.getIdRange = HateosHandlerRouterMapperHateosHandlerMapping.idRangeResourceCollectionResourceCollection(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosIdRangeResourceCollectionResourceCollectionHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> post(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.postIdRange = HateosHandlerRouterMapperHateosHandlerMapping.idRangeResourceCollectionResourceCollection(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosIdRangeResourceCollectionResourceCollectionHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> put(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.putIdRange = HateosHandlerRouterMapperHateosHandlerMapping.idRangeResourceCollectionResourceCollection(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosIdRangeResourceCollectionResourceCollectionHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> delete(final HateosIdRangeResourceCollectionResourceCollectionHandler<I, R, S> handler) {
        checkHandler(handler);
        this.deleteIdRange = HateosHandlerRouterMapperHateosHandlerMapping.idRangeResourceCollectionResourceCollection(handler);
        return this;
    }

    // HateosIdResourceValueHandler.............................................................................................

    /**
     * Adds or replaces a GET {@link HateosIdResourceValueHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> get(final HateosIdResourceValueHandler<I, R, S> handler) {
        checkHandler(handler);
        this.getId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceValue(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosIdResourceValueHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> post(final HateosIdResourceValueHandler<I, R, S> handler) {
        checkHandler(handler);
        this.postId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceValue(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosIdResourceValueHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> put(final HateosIdResourceValueHandler<I, R, S> handler) {
        checkHandler(handler);
        this.putId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceValue(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosIdResourceValueHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> delete(final HateosIdResourceValueHandler<I, R, S> handler) {
        checkHandler(handler);
        this.deleteId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceValue(handler);
        return this;
    }

    // HateosIdResourceResourceHandler.............................................................................................

    /**
     * Adds or replaces a GET {@link HateosIdResourceResourceHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> get(final HateosIdResourceResourceHandler<I, R, S> handler) {
        checkHandler(handler);
        this.getId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceResource(handler);
        return this;
    }

    /**
     * Adds or replaces a POST {@link HateosIdResourceResourceHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> post(final HateosIdResourceResourceHandler<I, R, S> handler) {
        checkHandler(handler);
        this.postId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceResource(handler);
        return this;
    }

    /**
     * Adds or replaces a PUT {@link HateosIdResourceResourceHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> put(final HateosIdResourceResourceHandler<I, R, S> handler) {
        checkHandler(handler);
        this.putId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceResource(handler);
        return this;
    }

    /**
     * Adds or replaces a DELETE {@link HateosIdResourceResourceHandler}.
     */
    public HateosHandlerRouterMapper<I, R, S> delete(final HateosIdResourceResourceHandler<I, R, S> handler) {
        checkHandler(handler);
        this.deleteId = HateosHandlerRouterMapperHateosHandlerMapping.idResourceResource(handler);
        return this;
    }

    // helpers.................................................................................................

    private void checkHandler(final Object handler) {
        Objects.requireNonNull(handler, "handler");
    }

    /**
     * Makes a defensive copy of this mappers. This is necessary when the router is created and the map copied from the builder.
     */
    HateosHandlerRouterMapper<I, R, S> copy() {
        final HateosHandlerRouterMapper<I, R, S> handlers = new HateosHandlerRouterMapper<>(this.stringToId,
                this.inputResourceType);

        handlers.getId = this.getId;
        handlers.postId = this.postId;
        handlers.putId = this.putId;
        handlers.deleteId = this.deleteId;

        handlers.getIdRange = this.getIdRange;
        handlers.postIdRange = this.postIdRange;
        handlers.putIdRange = this.putIdRange;
        handlers.deleteIdRange = this.deleteIdRange;

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
            final HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> mapping = this.mappingIdOrResponseMethodNotAllowed(resourceName,
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
    <N extends Node<N, ?, ?, ?>> void handleIdRange(final HateosResourceName resourceName,
                                                    final LinkRelation<?> linkRelation,
                                                    final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> mapping = this.mappingIdRangeOrResponseMethodNotAllowed(resourceName,
                linkRelation,
                request);
        if (null != mapping) {
            this.handleIdRange0(resourceName, Range.all(), mapping, request);
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
            final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> mapping = this.mappingIdRangeOrResponseMethodNotAllowed(resourceName, linkRelation, request);
            if (null != mapping) {
                this.handleIdRange0(resourceName, range, mapping, request);
            }
        }
    }

    private <N extends Node<N, ?, ?, ?>> void handleIdRange0(final HateosResourceName resourceName,
                                                             final Range<I> ids,
                                                             final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> mapping,
                                                             final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request) {
        final String requestText = request.resourceTextOrBadRequest();
        if (null != requestText) {
            mapping.handleIdRange(resourceName,
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
     * Locates the {@link HateosHandlerRouterMapperHateosIdHandlerMapping} for the given request method or sets the response to method not allowed.
     */
    private HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> mappingIdOrResponseMethodNotAllowed(final HateosResourceName resourceName,
                                                                                                            final LinkRelation<?> linkRelation,
                                                                                                            final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
        HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> mapping = null;

        do {
            final HttpMethod method = request.request.method();
            if (HttpMethod.GET.equals(method)) {
                mapping = this.getId;
                break;
            }
            if (HttpMethod.POST.equals(method)) {
                mapping = this.postId;
                break;
            }
            if (HttpMethod.PUT.equals(method)) {
                mapping = this.putId;
                break;
            }
            if (HttpMethod.DELETE.equals(method)) {
                mapping = this.deleteId;
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
    HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> getId;
    HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> postId;
    HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> putId;
    HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> deleteId;
    
    //.................................................................................................................

    /**
     * Locates the {@link HateosHandlerRouterMapperHateosIdRangeHandlerMapping} for the given request method or sets the response to method not allowed.
     */
    private HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> mappingIdRangeOrResponseMethodNotAllowed(final HateosResourceName resourceName,
                                                                                                                      final LinkRelation<?> linkRelation,
                                                                                                                      final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<?> request) {
        HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> mapping = null;

        do {
            final HttpMethod method = request.request.method();
            if (HttpMethod.GET.equals(method)) {
                mapping = this.getIdRange;
                break;
            }
            if (HttpMethod.POST.equals(method)) {
                mapping = this.postIdRange;
                break;
            }
            if (HttpMethod.PUT.equals(method)) {
                mapping = this.putIdRange;
                break;
            }
            if (HttpMethod.DELETE.equals(method)) {
                mapping = this.deleteIdRange;
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
    HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> getIdRange;
    HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> postIdRange;
    HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> putIdRange;
    HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> deleteIdRange;

    final Class<R> inputResourceType;

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        b.label(HttpMethod.GET.toString()).value(listWithoutNulls(this.getId, this.getIdRange));
        b.label(HttpMethod.POST.toString()).value(listWithoutNulls(this.postId, this.postIdRange));
        b.label(HttpMethod.PUT.toString()).value(listWithoutNulls(this.putId, this.putIdRange));
        b.label(HttpMethod.DELETE.toString()).value(listWithoutNulls(this.deleteId, this.deleteIdRange));

        return b.build();
    }

    private List<?> listWithoutNulls(final HateosHandlerRouterMapperHateosIdHandlerMapping<?, I, R, S> id,
                                     final HateosHandlerRouterMapperHateosIdRangeHandlerMapping<?, I, R, S> range) {
        return Lists.of(id, range)
                .stream()
                .filter(e -> null != e)
                .collect(Collectors.toList());
    }
}
