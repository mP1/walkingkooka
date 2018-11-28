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
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of header parameter value.
 */
final public class HttpHeaderParameterName<T> implements Name, HashCodeEqualsDefined, Comparable<HttpHeaderParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    // constants

    /**
     * A read only cache of already prepared {@link HttpHeaderParameterName names}. These constants are incomplete.
     */
    final static Map<String, HttpHeaderParameterName> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link HttpHeaderParameterName} to the cache being built that handles float header values.
     */
    private static HttpHeaderParameterName<Float> registerFloatConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.floatConverter());
    }

    /**
     * Creates and adds a new {@link HttpHeaderParameterName} to the cache being built.
     */
    private static <T> HttpHeaderParameterName<T> registerConstant(final String name, final HttpHeaderValueConverter<T> headerValue) {
        final HttpHeaderParameterName<T> httpHeader = new HttpHeaderParameterName<T>(name, headerValue);
        HttpHeaderParameterName.CONSTANTS.put(name, httpHeader);
        return httpHeader;
    }

    /**
     * A {@link HttpHeaderParameterName} holding <code>Q</code>
     */
    public final static HttpHeaderParameterName<Float> Q = HttpHeaderParameterName.registerFloatConstant("Q");

    /**
     * Factory that creates a {@link HttpHeaderParameterName}. If the {@link #parameterValue} is not a constant
     * it will return {@link String}.
     */
    public static HttpHeaderParameterName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final HttpHeaderParameterName httpHeaderValueParameterName = CONSTANTS.get(name);
        return null != httpHeaderValueParameterName ?
                httpHeaderValueParameterName :
                new HttpHeaderParameterName<String>(name, HttpHeaderValueConverter.string());
    }

    /**
     * Private constructor use factory.
     */
    private HttpHeaderParameterName(final String name, final HttpHeaderValueConverter<T> headerValueConverter) {
        this.name = name;
        this.headerValueConverter = headerValueConverter;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;


    /**
     * Type safe getter that retrieves a parameter value.
     */
    public Optional<T> parameterValue(final Map<HttpHeaderParameterName<?>, String> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        final String value = parameters.get(this);
        return null != value ?
                Optional.of(this.headerValueConverter.parse(value, this)) :
                Optional.empty();
    }

    private final HttpHeaderValueConverter<T> headerValueConverter;

    // Comparable

    @Override
    public int compareTo(final HttpHeaderParameterName<?> other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    // Object

    @Override
    public int hashCode() {
        return CaseSensitivity.INSENSITIVE.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpHeaderParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpHeaderParameterName name) {
        return this.name.equalsIgnoreCase(name.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
