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
final public class HeaderValueTokenParameterName<T> implements HeaderName<T>,
        Comparable<HeaderValueTokenParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    // constants

    /**
     * A read only cache of already prepared {@link HeaderValueTokenParameterName names}. These constants are incomplete.
     */
    final static Map<String, HeaderValueTokenParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link HeaderValueTokenParameterName} to the cache being built that handles float header values.
     */
    private static HeaderValueTokenParameterName<Float> registerFloatConstant(final String header) {
        return registerConstant(header, HeaderValueConverters.floatConverter());
    }

    /**
     * Creates and adds a new {@link HeaderValueTokenParameterName} to the cache being built.
     */
    private static <T> HeaderValueTokenParameterName<T> registerConstant(final String name, final HeaderValueConverter<T> headerValue) {
        final HeaderValueTokenParameterName<T> httpHeader = new HeaderValueTokenParameterName<T>(name, headerValue);
        HeaderValueTokenParameterName.CONSTANTS.put(name, httpHeader);
        return httpHeader;
    }

    /**
     * A {@link HeaderValueTokenParameterName} holding <code>Q</code>
     */
    public final static HeaderValueTokenParameterName<Float> Q = HeaderValueTokenParameterName.registerFloatConstant("Q");

    /**
     * Factory that creates a {@link HeaderValueTokenParameterName}. If the parameter is not a constant it will
     * assume all values are a {@link String}.
     */
    public static HeaderValueTokenParameterName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final HeaderValueTokenParameterName httpHeaderValueParameterName = CONSTANTS.get(name);
        return null != httpHeaderValueParameterName ?
                httpHeaderValueParameterName :
                new HeaderValueTokenParameterName<String>(name, HeaderValueConverters.string());
    }

    /**
     * Private constructor use factory.
     */
    private HeaderValueTokenParameterName(final String name, final HeaderValueConverter<T> valueConverter) {
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
     * Validates the value.
     */
    @Override
    public void checkValue(final Object value) {
        this.valueConverter.check(value);
    }

    private final HeaderValueConverter<T> valueConverter;

    // Comparable

    @Override
    public int compareTo(final HeaderValueTokenParameterName<?> other) {
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
                other instanceof HeaderValueTokenParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HeaderValueTokenParameterName name) {
        return this.name.equalsIgnoreCase(name.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
