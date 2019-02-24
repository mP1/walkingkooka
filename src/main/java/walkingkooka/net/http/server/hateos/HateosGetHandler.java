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

import walkingkooka.compare.Range;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.tree.Node;

import java.util.Map;
import java.util.Optional;

/**
 * Handles GET requests for an resource.
 */
public interface HateosGetHandler<I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandler<I, N> {

    /**
     * Perform the GET operation.
     */
    Optional<N> get(final I id,
                    final Map<HttpRequestAttribute<?>, Object> parameters,
                    final HateosHandlerContext<N> context);

    /**
     * Perform a batch operation on the requested ids.
     */
    Optional<N> getCollection(final Range<I> ids,
                              final Map<HttpRequestAttribute<?>, Object> parameters,
                              final HateosHandlerContext<N> context);
}
