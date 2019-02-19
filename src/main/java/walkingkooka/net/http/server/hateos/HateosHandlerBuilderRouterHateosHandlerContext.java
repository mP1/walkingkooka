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
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.tree.Node;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link HateosHandlerContext} that is ued by {@link HateosHandlerBuilderRouter}.
 */
final class HateosHandlerBuilderRouterHateosHandlerContext<N extends Node<N, ?, ?, ?>>
        implements HateosHandlerContext<N> {

    /**
     * Factory only called by {@link HateosHandlerBuilderRouter}
     */
    static <N extends Node<N, ?, ?, ?>> HateosHandlerBuilderRouterHateosHandlerContext<N> with(final HttpMethod method,
                                                                                               final HateosContentType<N> contentType,
                                                                                               final AbsoluteUrl base,
                                                                                               final Set<HateosHandlerBuilderRouterKey> nameAndLinkRelations) {
        return new HateosHandlerBuilderRouterHateosHandlerContext<>(method, contentType, base, nameAndLinkRelations);
    }

    /**
     * Private ctor
     */
    private HateosHandlerBuilderRouterHateosHandlerContext(final HttpMethod method,
                                                           final HateosContentType<N> contentType,
                                                           final AbsoluteUrl base,
                                                           final Set<HateosHandlerBuilderRouterKey> nameAndLinkRelations) {
        super();
        this.method = method;
        this.base = base;
        this.contentType = contentType;
        this.nameAndLinkRelations = nameAndLinkRelations;
    }

    /**
     * Adds links for this node.
     */
    @Override
    public N addLinks(final HateosResourceName name,
                      final Comparable<?> id,
                      final N node) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(node, "node");

        return this.contentType.addLinks(id,
                node,
                this.method,
                this.base,
                name,
                this.linkRelations(name));
    }

    private List<LinkRelation<?>> linkRelations(final HateosResourceName name) {
        return this.nameAndLinkRelations.stream()
                .filter(e -> e.resourceName.equals(name))
                .map(e -> e.linkRelation)
                .collect(Collectors.toList());
    }

    /**
     * The base url of all handlers.
     */
    private final AbsoluteUrl base;

    /**
     * The method used by the request.
     */
    private final HttpMethod method;

    /**
     * The content type for all handler processing.
     */
    private final HateosContentType<N> contentType;

    /**
     * Hateos resource name to link relations.
     */
    private final Set<HateosHandlerBuilderRouterKey> nameAndLinkRelations;

    @Override
    public String toString() {
        return this.base.toString();
    }
}
