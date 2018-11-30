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
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of header parameter value.
 */
final public class HeaderParameterName<T> implements Name, HashCodeEqualsDefined, Comparable<HeaderParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    // constants

    /**
     * A read only cache of already prepared {@link HeaderParameterName names}. These constants are incomplete.
     */
    final static Map<String, HeaderParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link HeaderParameterName} to the cache being built that handles float header values.
     */
    private static HeaderParameterName<Float> registerFloatConstant(final String header) {
        return registerConstant(header, HeaderValueConverters.floatConverter());
    }

    /**
     * Creates and adds a new {@link HeaderParameterName} to the cache being built.
     */
    private static <T> HeaderParameterName<T> registerConstant(final String name, final HeaderValueConverter<T> headerValue) {
        final HeaderParameterName<T> httpHeader = new HeaderParameterName<T>(name, headerValue);
        HeaderParameterName.CONSTANTS.put(name, httpHeader);
        return httpHeader;
    }

    /**
     * A {@link HeaderParameterName} holding <code>Q</code>
     */
    public final static HeaderParameterName<Float> Q = HeaderParameterName.registerFloatConstant("Q");

    /**
     * Factory that creates a {@link HeaderParameterName}. If the parameter is not a constant it will
     * assume all values are a {@link String}.
     */
    public static HeaderParameterName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final HeaderParameterName httpHeaderValueParameterName = CONSTANTS.get(name);
        return null != httpHeaderValueParameterName ?
                httpHeaderValueParameterName :
                new HeaderParameterName<String>(name, HeaderValueConverters.string());
    }

    /**
     * Private constructor use factory.
     */
    private HeaderParameterName(final String name, final HeaderValueConverter<T> valueConverter) {
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
    public T parameterValue(final String text) {
        Objects.requireNonNull(text, "text");

        return this.valueConverter.parse(text, this);
    }

    /**
     * Validates the value.
     */
    public void checkValue(final Object value) {
        this.valueConverter.check(value);
    }

    private final HeaderValueConverter<T> valueConverter;

    // Comparable

    @Override
    public int compareTo(final HeaderParameterName<?> other) {
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
                other instanceof HeaderParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HeaderParameterName name) {
        return this.name.equalsIgnoreCase(name.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
