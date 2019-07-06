/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.http.server.hateos;

import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.http.server.HttpRequestAttribute;

import java.util.Map;
import java.util.Optional;

/**
 * Handles any request involving a hateos request. Two methods are available and support 4 different url patterns.
 * <ol>
 * <li>A url without an ID, this is mapped to handle and POST to create an entity</li>
 * <li>A url with a ID, this is mapped to handle and used to GET (fetch), POST (update), DELETE (update) an existing entity</li>
 * <li>A url with a wildcard, this is mapped to handleCollection and used to GET and DELETE everything.</li>
 * <li>A url with a range, this is mapped to handle and used to GET (fetch), DELETE the selected entities</li>
 * </ol>
 * For the handleCollection methods POST and PUT typically dont make sense, but are supported.
 */
public interface HateosHandler<I extends Comparable<I>, R extends HateosResource<Optional<I>>, S extends HateosResource<Range<I>>> {

    /**
     * An empty {@link Map} with no parameters.
     */
    Map<HttpRequestAttribute<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * Handles a request resource identified by the ID.
     */
    Optional<R> handle(final Optional<I> id,
                       final Optional<R> resource,
                       final Map<HttpRequestAttribute<?>, Object> parameters);

    /**
     * Handles a request resource identified by a range of ids
     */
    Optional<S> handleCollection(final Range<I> id,
                                 final Optional<S> resource,
                                 final Map<HttpRequestAttribute<?>, Object> parameters);
}
