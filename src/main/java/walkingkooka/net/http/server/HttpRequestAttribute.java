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

package walkingkooka.net.http.server;

import walkingkooka.Cast;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A tag interface that all http request attributes such as headers implement.
 */
public interface HttpRequestAttribute<T> {

    /**
     * A typed getter that retrieves an attribute's value.
     */
    default Optional<T> parameterValue(final Map<? extends HttpRequestAttribute<?>, ? extends Object> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        return Optional.ofNullable(Cast.to(parameters.get(this)));
    }

    /**
     * A typed getter that retrieves a value from a {@link HttpRequest}
     */
    Optional<T> parameterValue(final HttpRequest request);
}
