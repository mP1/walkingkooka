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
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.routing.Router;
import walkingkooka.tree.Node;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * A builder which collects all resource and relation mappings producing a {@link Router}.
 * The {@link Router} assumes that the transport and content-type have already been tested and filtered.
 */
public final class HateosHandlerBuilder<N extends Node<N, ?, ?, ?>>
        extends HateosHandler2<N>
        implements Builder<Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>>> {

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
     * Adds a mapper for the given hateos resource and relation combination.
     */
    public <I extends Comparable<I>,
            R extends HateosResource<?>,
            S extends HateosResource<?>> HateosHandlerBuilder<N> add(final HateosResourceName name,
                                                                                              final LinkRelation<?> relation,
                                                                                              final HateosHandlerMapper<I, R, S> mapper) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(relation, "relation");
        Objects.requireNonNull(mapper, "mapper");

        final HateosHandlerRouterKey key = HateosHandlerRouterKey.with(name, relation);
        final HateosHandlerMapper<I, R, S> value = mapper.copy();

        if(this.mappers.containsKey(key)) {
            throw new IllegalArgumentException("Mapping " + key + " already used");
        } else {
            this.mappers.put(key, value);
        }

        return this;
    }

    /**
     * Builds a {@link Router} that will accept a {@link BiConsumer} with the request and response. These will then
     * examine the request and route to the appropriate if any handler for the identified resource, id if present and relation if present.
     */
    @Override
    public Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>> build() throws BuilderException {
        final Map<HateosHandlerRouterKey, HateosHandlerMapper<?, ?, ?>> copy = Maps.sorted();
        copy.putAll(this.mappers);

        return HateosHandlerRouter.with(this.base,
                this.contentType,
                copy);
    }
}
