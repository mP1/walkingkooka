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

package walkingkooka.net;

import walkingkooka.naming.Name;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@link Name} of a query string parameter.
 */
public final class UrlParameterName implements Name,
        Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * Factory that creates a {@link UrlParameterName}
     */
    public static UrlParameterName with(final String name) {
        Objects.requireNonNull(name, "name");

        return new UrlParameterName(name);
    }

    /**
     * Private constructor
     */
    private UrlParameterName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Object

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof UrlParameterName && this.equals0((UrlParameterName) other);
    }

    private boolean equals0(final UrlParameterName other) {
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
