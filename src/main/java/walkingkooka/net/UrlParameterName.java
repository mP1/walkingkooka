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
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Name} of a query string parameter.
 */
public final class UrlParameterName extends NetName
        implements Comparable<UrlParameterName>,
        HttpRequestAttribute<List<String>> {

    private final static long serialVersionUID = 1L;

    /**
     * Factory that creates a {@link UrlParameterName}
     */
    public static UrlParameterName with(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");

        return new UrlParameterName(name);
    }

    /**
     * Private constructor
     */
    private UrlParameterName(final String name) {
        super(name);
    }

    // HttpRequestAttribute..............................................................................................

    /**
     * A typed getter that retrieves a value from a {@link HttpRequest}
     */
    @Override
    public Optional<List<String>> parameterValue(final HttpRequest request) {
        return Optional.ofNullable(request.url().query().parameters().get(this));
    }

    // Comparable..............................................................................................

    @Override
    public int compareTo(final UrlParameterName other) {
        return this.compareTo0(other);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof UrlParameterName;
    }

    @Override
    CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public final String toString() {
        return this.name;
    }
}
