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
import walkingkooka.tree.Node;

import java.util.Map;

/**
 * Base class for both the builder and router implementation.
 */
abstract class HateosHandlerBuilder2<N extends Node<N, ?, ?, ?>> {

    /**
     * Package private to limit sub classing.
     */
    HateosHandlerBuilder2(final AbsoluteUrl base,
                          final HateosContentType<N> contentType,
                          final Map<HateosHandlerBuilderRouterKey, HateosHandlerBuilderRouterHandlers<N>> handlers) {
        super();
        this.base = base;
        this.contentType = contentType;

        this.handlers = handlers;
    }

    /**
     * The base path where hateos urls live.
     */
    final AbsoluteUrl base;

    /**
     * The content type for all handler processing.
     */
    final HateosContentType<N> contentType;

    /**
     * A map of resource and relations to handlers.
     */
    final Map<HateosHandlerBuilderRouterKey, HateosHandlerBuilderRouterHandlers<N>> handlers;


    // Object .........................................................................................................

    @Override
    public final String toString() {
        return this.base + " " + this.contentType + " " + this.handlers;
    }
}
