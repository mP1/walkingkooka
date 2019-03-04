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

import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpRequestAttributes;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.RouteException;
import walkingkooka.routing.Router;
import walkingkooka.tree.Node;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Router which accepts a request and returns a {@link BiConsumer} that operates on the base {@link UrlPath}.
 */
final class HateosHandlerBuilderRouter<N extends Node<N, ?, ?, ?>, V>
        extends HateosHandlerBuilder2<N, V>
        implements Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>> {

    /**
     * Factory called by {@link HateosHandlerBuilder#build()}
     */
    static <N extends Node<N, ?, ?, ?>, V> HateosHandlerBuilderRouter<N, V> with(final AbsoluteUrl base,
                                                                           final HateosContentType<N, V> contentType,
                                                                           final Map<HateosHandlerBuilderRouterKey, HateosHandlerBuilderRouterHandlers<N>> handlers) {
        return new HateosHandlerBuilderRouter<N, V>(base,
                contentType,
                handlers);
    }

    /**
     * Private ctor use factory.
     */
    private HateosHandlerBuilderRouter(final AbsoluteUrl base,
                                       final HateosContentType<N, V> contentType,
                                       Map<HateosHandlerBuilderRouterKey, HateosHandlerBuilderRouterHandlers<N>> handlers) {
        super(base, contentType, handlers);

        final Set<HateosHandlerBuilderRouterKey> nameAndLinkRelations = handlers.keySet();

        this.getContext = HateosHandlerBuilderRouterHateosHandlerContext.with(HttpMethod.GET, contentType, base, nameAndLinkRelations);
        this.postContext = HateosHandlerBuilderRouterHateosHandlerContext.with(HttpMethod.POST, contentType, base, nameAndLinkRelations);
        this.putContext = HateosHandlerBuilderRouterHateosHandlerContext.with(HttpMethod.PUT, contentType, base, nameAndLinkRelations);
        this.deleteContext = HateosHandlerBuilderRouterHateosHandlerContext.with(HttpMethod.DELETE, contentType, base, nameAndLinkRelations);
    }

    @Override
    public Optional<BiConsumer<HttpRequest, HttpResponse>> route(final Map<HttpRequestAttribute<?>, Object> parameters) throws RouteException {
        Objects.requireNonNull(parameters, "parameters");

        return Optional.ofNullable(-1 != consumeBasePath(parameters) ?
                HateosHandlerBuilderRouterHttpRequestHttpResponseBiConsumer.with(this) :
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

    final HateosHandlerBuilderRouterHateosHandlerContext<N, V> getContext;
    final HateosHandlerBuilderRouterHateosHandlerContext<N, V> postContext;
    final HateosHandlerBuilderRouterHateosHandlerContext<N, V> putContext;
    final HateosHandlerBuilderRouterHateosHandlerContext<N, V> deleteContext;
}
