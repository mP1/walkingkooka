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
import walkingkooka.build.Builder;
import walkingkooka.build.BuilderException;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlFragment;
import walkingkooka.net.UrlQueryString;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.Router;
import walkingkooka.tree.Node;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A builder which collects all resource and relation mappings producing a {@link Router}.
 * The {@link Router} assumes that the transport and content-type have already been tested and filtered.
 */
public final class HateosHandlerBuilder<N extends Node<N, ?, ?, ?>, V>
        extends HateosHandlerBuilder2<N, V>
        implements
        Builder<Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> {

    /**
     * Factory that creates a new {@link HateosHandlerBuilder} with the provided path being used when building links.
     */
    public static <N extends Node<N, ?, ?, ?>, V> HateosHandlerBuilder<N, V> with(final AbsoluteUrl base,
                                                                                  final HateosContentType<N, V> contentType) {
        Objects.requireNonNull(base, "base");
        if (!base.query().equals(UrlQueryString.EMPTY)) {
            throw new IllegalArgumentException("Base should have no query string/query parameters but was " + base);
        }
        if (!base.fragment().equals(UrlFragment.EMPTY)) {
            throw new IllegalArgumentException("Base should have no fragment but was " + base);
        }

        Objects.requireNonNull(contentType, "contentType");

        return new HateosHandlerBuilder<N, V>(base, contentType);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilder(final AbsoluteUrl base,
                                 final HateosContentType<N, V> contentType) {
        super(base, contentType, Maps.sorted());
    }

    /**
     * Registers a new GET handler for the given hateos resource and relation combination.
     * It is assumed and required that the same id parser is given to all get/post/put/delete calls so ids can be parsed consistently.
     */
    public <I extends Comparable<I>> HateosHandlerBuilder<N, V> get(final HateosResourceName name,
                                                                    final Function<String, I> id,
                                                                    final LinkRelation<?> relation,
                                                                    final HateosGetHandler<I, N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        check(HttpMethod.GET, name, id, relation, handler, handlers, handlers.get);
        handlers.get = handler;

        return this;
    }

    /**
     * Registers a new POST handler for the given hateos resource and relation combination.
     * It is assumed and required that the same id parser is given to all get/post/put/delete calls so ids can be parsed consistently.
     */
    public <I extends Comparable<I>> HateosHandlerBuilder<N, V> post(final HateosResourceName name,
                                                                     final Function<String, I> id,
                                                                     final LinkRelation<?> relation,
                                                                     final HateosPostHandler<I, N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        check(HttpMethod.POST, name, id, relation, handler, handlers, handlers.post);
        handlers.post = handler;

        return this;
    }

    /**
     * Registers a new PUT handler for the given hateos resource and relation combination.
     * It is assumed and required that the same id parser is given to all get/post/put/delete calls so ids can be parsed consistently.
     */
    public <I extends Comparable<I>> HateosHandlerBuilder<N, V> put(final HateosResourceName name,
                                                                    final Function<String, I> id,
                                                                    final LinkRelation<?> relation,
                                                                    final HateosPutHandler<I, N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        check(HttpMethod.PUT, name, id, relation, handler, handlers, handlers.put);
        handlers.put = handler;

        return this;
    }

    /**
     * Registers a new handler for the given hateos resource and relation combination.
     * It is assumed and required that the same id parser is given to all get/post/put/delete calls so ids can be parsed consistently.
     */
    public <I extends Comparable<I>> HateosHandlerBuilder<N, V> delete(final HateosResourceName name,
                                                                       final Function<String, I> id,
                                                                       final LinkRelation<?> relation,
                                                                       final HateosDeleteHandler<I, N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        check(HttpMethod.DELETE, name, id, relation, handler, handlers, handlers.delete);
        handlers.delete = handler;

        return this;
    }

    /**
     * Gets or creates a {@link HateosHandlerBuilderRouterHandlers} for the key.
     */
    private <I extends Comparable<I>> HateosHandlerBuilderRouterHandlers<N> getOrCreateHandlers(final HateosResourceName name,
                                                                                                final LinkRelation<?> relation) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(relation, "relation");

        final HateosHandlerBuilderRouterKey key = HateosHandlerBuilderRouterKey.with(name, relation);
        HateosHandlerBuilderRouterHandlers<N> value = this.handlers.get(key);
        if (null == value) {
            value = HateosHandlerBuilderRouterHandlers.with();
            this.handlers.put(key, value);
        }

        return value;
    }

    /**
     * Verifies the new handler is not overwriting an existing one.
     */
    private <H extends HateosHandler<?, N>, I extends Comparable<I>> void check(final HttpMethod method,
                                                                                final HateosResourceName resourceName,
                                                                                final Function<String, I> id,
                                                                                final LinkRelation<?> relation,
                                                                                final H handler,
                                                                                final HateosHandlerBuilderRouterHandlers<N> handlers,
                                                                                final H previous) {
        Objects.requireNonNull(handler, "handler");
        Objects.requireNonNull(id, "id");

        if (null != previous) {
            throw new IllegalArgumentException("Attempt to replace " + method + " handler for " + resourceName + " " + relation + "=" + handler);
        }
        if (null != handlers.id && !handlers.id.equals(id)) {
            throw new IllegalArgumentException("Attempt to replace " + method + " different id parser for " + resourceName + " " + relation + "=" + id);
        }
        handlers.id = Cast.to(id);
    }

    /**
     * Builds a {@link Router} that will accept a {@link BiConsumer} with the request and response. These will then
     * examine the request and route to the appropriate if any handler for the identified resource, id if present and relation if present.
     */
    @Override
    public Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>> build() throws BuilderException {
        final Map<HateosHandlerBuilderRouterKey, HateosHandlerBuilderRouterHandlers<N>> copy = Maps.sorted();

        for (Entry<HateosHandlerBuilderRouterKey, HateosHandlerBuilderRouterHandlers<N>> resourceAndRelationToHandler : this.handlers.entrySet()) {
            copy.put(resourceAndRelationToHandler.getKey(), resourceAndRelationToHandler.getValue().copy());
        }

        return HateosHandlerBuilderRouter.with(this.base,
                this.contentType,
                copy);
    }
}
