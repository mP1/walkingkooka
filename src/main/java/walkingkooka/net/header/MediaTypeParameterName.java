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
import walkingkooka.compare.Comparables;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;


/**
 * The {@link Name} of an optional parameter accompanying a {@link MediaType}. Note the name may not contain the whitespace, equals sign, semi colons
 * or commas.
 */
final public class MediaTypeParameterName<T> implements HeaderName<T>,
        Comparable<MediaTypeParameterName<?>>,
        Serializable {

    private final static long serialVersionUID = 1L;

    // constants

    /**
     * A read only cache of already prepared {@link MediaTypeParameterName names}. These constants are incomplete.
     */
    final static Map<String, MediaTypeParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link MediaTypeParameterName} to the cache being built.
     */
    private static <T> MediaTypeParameterName<T> registerConstant(final String name,
                                                                  final HeaderValueConverter<T> valueConverter) {
        final MediaTypeParameterName<T> parameterName = new MediaTypeParameterName<T>(name,
                valueConverter);
        MediaTypeParameterName.CONSTANTS.put(name, parameterName);
        return parameterName;
    }

    /**
     * Holds the charset parameter name.
     */
    public final static MediaTypeParameterName<String> CHARSET = registerConstant("charset",
            HeaderValueConverters.string());

    /**
     * The q factor weight parameter.
     */
    public final static MediaTypeParameterName<Float> Q_FACTOR = registerConstant("q",
            HeaderValueConverters.floatConverter());

    /**
     * Factory that creates a {@link MediaTypeParameterName}
     */
    public static MediaTypeParameterName<?> with(final String value) {
        MediaType.check(value, "value");

        final MediaTypeParameterName<?> parameterName = CONSTANTS.get(value);
        return null != parameterName ?
                parameterName :
                new MediaTypeParameterName<String>(value, HeaderValueConverters.mediaTypeString());
    }

    /**
     * Private ctor use factory.
     */
    private MediaTypeParameterName(final String value,
                                   final HeaderValueConverter<T> valueConverter) {
        this.value = value;
        this.valueConverter = valueConverter;
    }

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

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

    final transient HeaderValueConverter<T> valueConverter;

    // Comparable

    @Override
    public int compareTo(final MediaTypeParameterName name) {
        return this.value.compareToIgnoreCase(name.value());
    }

    // Object

    @Override
    public int hashCode() {
        return CaseSensitivity.INSENSITIVE.hash(this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof MediaTypeParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final MediaTypeParameterName other) {
        return this.compareTo(other) == Comparables.EQUAL;
    }

    /**
     * Dumps the request raw name
     */
    @Override
    public String toString() {
        return this.value;
    }

    // Serializable .................................................................................................

    private Object readResolve() {
        return with(this.value);
    }
}
