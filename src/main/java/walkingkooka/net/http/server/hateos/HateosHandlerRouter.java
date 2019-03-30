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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpRequestAttributes;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.RouteException;
import walkingkooka.routing.Router;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Router which accepts a request and returns a {@link BiConsumer} that operates on the base {@link UrlPath}.
 */
final class HateosHandlerRouter<N extends Node<N, ?, ?, ?>>
        extends HateosHandler2<N>
        implements Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>> {

    /**
     * Factory called by {@link HateosHandlerBuilder#build()}
     */
    static <N extends Node<N, ?, ?, ?>> HateosHandlerRouter<N> with(final AbsoluteUrl base,
                                                                    final HateosContentType<N> contentType,
                                                                    final Map<HateosHandlerRouterKey, HateosHandlerMapper<?, ?, ?>> handlers) {
        return new HateosHandlerRouter<N>(base,
                contentType,
                handlers);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerRouter(final AbsoluteUrl base,
                                final HateosContentType<N> contentType,
                                final Map<HateosHandlerRouterKey, HateosHandlerMapper<?, ?, ?>> handlers) {
        super(base, contentType, handlers);

        final Set<HateosHandlerRouterKey> nameAndLinkRelations = handlers.keySet();
        final Map<HateosResourceName, List<LinkRelation<?>>> nameToLinkRelations = Maps.ordered();
        for (HateosHandlerRouterKey key : nameAndLinkRelations) {
            final HateosResourceName name = key.resourceName;
            nameToLinkRelations.put(name,
                    nameAndLinkRelations.stream()
                            .filter(e -> e.resourceName.equals(name))
                            .map(e -> e.linkRelation)
                            .collect(Collectors.toList()));
        }

        this.nameToLinkRelations = nameToLinkRelations;
    }

    /**
     * Returns a {@link BiConsumer} which will dispatch matching hateos mappers if the base path is matched.
     */
    @Override
    public Optional<BiConsumer<HttpRequest, HttpResponse>> route(final Map<HttpRequestAttribute<?>, Object> parameters) throws RouteException {
        Objects.requireNonNull(parameters, "parameters");

        return Optional.ofNullable(-1 != consumeBasePath(parameters) ?
                HateosHandlerRouterHttpRequestHttpResponseBiConsumer.with(this) :
                null);
    }

    /**
     * Attempts to consume the {@link #base} completely returning the index to the {@link HateosResourceName} component within the path or -1.
     */
    int consumeBasePath(final Map<HttpRequestAttribute<?>, Object> parameters) {
        // base path must be matched..................................................................................
        int pathIndex = 0;
        for (UrlPathName name : this.base.path()) {
            if (!name.equals(parameters.get(HttpRequestAttributes.pathComponent(pathIndex)))) {
                pathIndex = -1;
                break;
            }
            pathIndex++;
        }
        return pathIndex;
    }

    /**
     * Returns the link relations for the given {@link HateosResourceName}
     */
    List<LinkRelation<?>> linkRelations(final HateosResourceName name) {
        return this.nameToLinkRelations.get(name);
    }

    private final Map<HateosResourceName, List<LinkRelation<?>>> nameToLinkRelations;
}
