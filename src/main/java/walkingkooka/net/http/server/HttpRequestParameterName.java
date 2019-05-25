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
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Name} of a parameter within a {@link HttpRequest}.
 */
final public class HttpRequestParameterName implements Name,
        Comparable<HttpRequestParameterName>,
        HttpRequestAttribute<List<String>> {

    /**
     * Factory that creates a {@link HttpRequestParameterName}
     */
    public static HttpRequestParameterName with(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");

        return new HttpRequestParameterName(name);
    }

    /**
     * Private constructor use factory.
     */
    private HttpRequestParameterName(final String name) {
        this.name = name;
    }

    // Name

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // HttpRequestAttribute..............................................................................................

    /**
     * A typed getter that retrieves a value from a {@link HttpRequest}
     */
    @Override
    public Optional<List<String>> parameterValue(final HttpRequest request) {
        return Optional.ofNullable(request.parameterValues(this));
    }

    // Comparable...............................................................................................

    @Override
    public int compareTo(final HttpRequestParameterName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // Object...............................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpRequestParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpRequestParameterName other) {
        return 0 == this.compareTo(other);
    }

    /**
     * Dumps the parameter name.
     */
    @Override
    public String toString() {
        return this.name;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
