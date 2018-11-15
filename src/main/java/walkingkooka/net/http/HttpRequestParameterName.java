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

package walkingkooka.net.http;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

/**
 * The {@link Name} of a parameter within a {@link HttpRequest}.
 */
final public class HttpRequestParameterName implements Name, HashCodeEqualsDefined {

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

    // Object...............................................................................................

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpRequestParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpRequestParameterName other) {
        return this.name.equals(other.name);
    }

    /**
     * Dumps the parameter name.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
