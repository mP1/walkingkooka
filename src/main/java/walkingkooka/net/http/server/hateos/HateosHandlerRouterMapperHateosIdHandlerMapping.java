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

import walkingkooka.net.header.LinkRelation;
import walkingkooka.tree.Node;

abstract class HateosHandlerRouterMapperHateosIdHandlerMapping<H extends HateosHandler<I, R, S>,
        I extends Comparable<I>,
        R extends HateosResource<?>,
        S extends HateosResource<?>>
        extends HateosHandlerRouterMapperHateosHandlerMapping<H, I, R, S> {

    /**
     * Package private to limit sub classing.
     */
    HateosHandlerRouterMapperHateosIdHandlerMapping(final H handler) {
        super(handler);
    }

    /**
     * Handles a request for a single resource with the given parameters.
     */
    abstract <N extends Node<N, ?, ?, ?>> void handleId(final HateosResourceName resourceName,
                                                        final I id,
                                                        final LinkRelation<?> linkRelation,
                                                        final String requestText,
                                                        final Class<R> resourceType,
                                                        final HateosHandlerRouterHttpRequestHttpResponseBiConsumerRequest<N> request);
}
