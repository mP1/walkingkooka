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

/**
 * A builder which collects all resource and relation mappings producing a {@link Router}.
 * The {@link Router} assumes that the transport and content-type have already been tested and filtered.
 */
public final class HateosHandlerBuilder<N extends Node<N, ?, ?, ?>>
        extends HateosHandlerBuilder2<N>
        implements
        Builder<Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> {

    /**
     * Factory that creates a new {@link HateosHandlerBuilder} with the provided path being used when building links.
     */
    public static <N extends Node<N, ?, ?, ?>> HateosHandlerBuilder<N> with(final AbsoluteUrl base,
                                                                            final HateosContentType<N> contentType) {
        Objects.requireNonNull(base, "base");
        if (!base.query().equals(UrlQueryString.EMPTY)) {
            throw new IllegalArgumentException("Base should have no query string/query parameters but was " + base);
        }
        if (!base.fragment().equals(UrlFragment.EMPTY)) {
            throw new IllegalArgumentException("Base should have no fragment but was " + base);
        }

        Objects.requireNonNull(contentType, "contentType");

        return new HateosHandlerBuilder<N>(base, contentType);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilder(final AbsoluteUrl base,
                                 final HateosContentType<N> contentType) {
        super(base, contentType, Maps.sorted());
    }

    /**
     * Registers a new GET handler for the given hateos resource and relation combination.
     */
    public HateosHandlerBuilder<N> get(final HateosResourceName name,
                                       final LinkRelation<?> relation,
                                       final HateosGetHandler<N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        checkHandler(handler, handlers.get, HttpMethod.GET, name, relation);
        handlers.get = handler;

        return this;
    }

    /**
     * Registers a new POST handler for the given hateos resource and relation combination.
     */
    public HateosHandlerBuilder<N> post(final HateosResourceName name,
                                        final LinkRelation<?> relation,
                                        final HateosPostHandler<N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        checkHandler(handler, handlers.post, HttpMethod.POST, name, relation);
        handlers.post = handler;

        return this;
    }

    /**
     * Registers a new PUT handler for the given hateos resource and relation combination.
     */
    public HateosHandlerBuilder<N> put(final HateosResourceName name,
                                       final LinkRelation<?> relation,
                                       final HateosPutHandler<N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        checkHandler(handler, handlers.put, HttpMethod.PUT, name, relation);
        handlers.put = handler;

        return this;
    }

    /**
     * Registers a new handler for the given hateos resource and relation combination.
     */
    public HateosHandlerBuilder<N> delete(final HateosResourceName name,
                                          final LinkRelation<?> relation,
                                          final HateosDeleteHandler<N> handler) {
        final HateosHandlerBuilderRouterHandlers<N> handlers = this.getOrCreateHandlers(name, relation);
        checkHandler(handler, handlers.delete, HttpMethod.DELETE, name, relation);
        handlers.delete = handler;

        return this;
    }

    /**
     * Gets or creates a {@link HateosHandlerBuilderRouterHandlers} for the key.
     */
    private HateosHandlerBuilderRouterHandlers<N> getOrCreateHandlers(final HateosResourceName name,
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
    private <H extends HateosHandler<N>> void checkHandler(final H handler,
                                                           final H previous,
                                                           final HttpMethod method,
                                                           final HateosResourceName resourceName,
                                                           final LinkRelation<?> relation) {
        Objects.requireNonNull(handler, "handler");

        if (null != previous && !handler.equals(previous)) {
            throw new IllegalArgumentException("Attempt to replace " + method + " handler for " + resourceName + " " + relation + "=" + handler);
        }
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
