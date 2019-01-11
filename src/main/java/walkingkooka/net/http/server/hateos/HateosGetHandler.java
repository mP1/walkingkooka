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
import walkingkooka.net.http.server.HttpRequestParameterName;
import walkingkooka.tree.Node;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Handles GET requests for an resource.
 */
public interface HateosGetHandler<N extends Node<N, ?, ?, ?>> extends HateosHandler<N> {

    /**
     * Perform the GET operation.
     */
    Optional<N> get(final BigInteger id,
                    final Map<HttpRequestParameterName, List<String>> parameters,
                    final HateosHandlerContext<N> context);

    /**
     * Perform a batch operation on the requested ids.
     */
    Optional<N> getCollection(final Range<BigInteger> ids,
                              final Map<HttpRequestParameterName, List<String>> parameters,
                              final HateosHandlerContext<N> context);
}
