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

import java.util.List;
import java.util.Map;

/**
 * Handles a request with a range of ids and a collection of resources.
 */
public interface HateosIdRangeResourceCollectionResourceCollectionHandler<I extends Comparable<I>, R extends HateosResource<?>, S extends HateosResource<?>>
        extends HateosHandler<I, R, S> {

    /**
     * Handles a request which involves a collection of resources.
     */
    List<S> handle(final Range<I> ids,
                   final List<R> resources,
                   final Map<HttpRequestAttribute<?>, Object> parameters);
}
