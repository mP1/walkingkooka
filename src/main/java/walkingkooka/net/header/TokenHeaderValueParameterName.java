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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of header parameter value.
 */
final public class TokenHeaderValueParameterName<T> implements HeaderParameterName<T>,
        Comparable<TokenHeaderValueParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    // constants

    /**
     * A read only cache of already prepared {@link TokenHeaderValueParameterName names}. These constants are incomplete.
     */
    final static Map<String, TokenHeaderValueParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link TokenHeaderValueParameterName} to the cache being built that handles float header values.
     */
    private static TokenHeaderValueParameterName<Float> registerFloatConstant(final String header) {
        return registerConstant(header, HeaderValueConverters.floatConverter());
    }

    /**
     * Creates and adds a new {@link TokenHeaderValueParameterName} to the cache being built.
     */
    private static <T> TokenHeaderValueParameterName<T> registerConstant(final String name, final HeaderValueConverter<T> headerValue) {
        final TokenHeaderValueParameterName<T> httpHeader = new TokenHeaderValueParameterName<T>(name, headerValue);
        TokenHeaderValueParameterName.CONSTANTS.put(name, httpHeader);
        return httpHeader;
    }

    /**
     * A {@link TokenHeaderValueParameterName} holding <code>Q</code>
     */
    public final static TokenHeaderValueParameterName<Float> Q = TokenHeaderValueParameterName.registerFloatConstant("Q");

    /**
     * Factory that creates a {@link TokenHeaderValueParameterName}. If the parameter is not a constant it will
     * assume all values are a {@link String}.
     */
    public static TokenHeaderValueParameterName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final TokenHeaderValueParameterName httpHeaderValueParameterName = CONSTANTS.get(name);
        return null != httpHeaderValueParameterName ?
                httpHeaderValueParameterName :
                new TokenHeaderValueParameterName<String>(name, RFC2045);
    }

    private final static HeaderValueConverter<String> RFC2045 = HeaderValueConverters.string(CharPredicates.rfc2045Token());

    /**
     * Private constructor use factory.
     */
    private TokenHeaderValueParameterName(final String name, final HeaderValueConverter<T> valueConverter) {
        this.name = name;
        this.valueConverter = valueConverter;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Accepts text and converts it into its value.
     */
    @Override
    public T toValue(final String text) {
        Objects.requireNonNull(text, "text");

        return this.valueConverter.parse(text, this);
    }

    /**
     * Validates the value and casts it to its correct type.
     */
    @Override
    public T checkValue(final Object value) {
        return this.valueConverter.check(value);
    }

    private final HeaderValueConverter<T> valueConverter;

    // Comparable

    @Override
    public int compareTo(final TokenHeaderValueParameterName<?> other) {
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
                other instanceof TokenHeaderValueParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final TokenHeaderValueParameterName name) {
        return this.name.equalsIgnoreCase(name.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
