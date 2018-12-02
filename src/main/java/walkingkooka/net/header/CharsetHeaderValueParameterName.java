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
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;


/**
 * The {@link Name} of an optional parameter accompanying a {@link MediaType}. Note the name may not contain the whitespace, equals sign, semi colons
 * or commas.
 */
final public class CharsetHeaderValueParameterName<T> implements HeaderParameterName<T>,
        Comparable<CharsetHeaderValueParameterName<?>>,
        Serializable {

    private final static long serialVersionUID = 1L;

    // constants

    /**
     * A read only cache of already prepared {@link CharsetHeaderValueParameterName names}. These constants are incomplete.
     */
    final static Map<String, CharsetHeaderValueParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link CharsetHeaderValueParameterName} to the cache being built.
     */
    private static <T> CharsetHeaderValueParameterName<T> registerConstant(final String name,
                                                                           final CharPredicate charPredicate,
                                                                           final HeaderValueConverter<T> valueConverter) {
        final CharsetHeaderValueParameterName<T> parameterName = new CharsetHeaderValueParameterName<T>(name,
                charPredicate,
                valueConverter);
        CharsetHeaderValueParameterName.CONSTANTS.put(name, parameterName);
        return parameterName;
    }

    private final static CharPredicate DIGITS = CharPredicates.digit()
            .or(CharPredicates.any("+-."))
            .setToString("Q factor");

    /**
     * The q factor weight parameter.
     */
    public final static CharsetHeaderValueParameterName<Float> Q_FACTOR = registerConstant("q",
            DIGITS,
            HeaderValueConverters.floatConverter());

    /**
     * Factory that creates a {@link CharsetHeaderValueParameterName}
     */
    public static CharsetHeaderValueParameterName<?> with(final String value) {
        MediaType.check(value, "value");

        final CharsetHeaderValueParameterName<?> parameterName = CONSTANTS.get(value);
        return null != parameterName ?
                parameterName :
                new CharsetHeaderValueParameterName<String>(value, MediaTypeHeaderParser.RFC2045TOKEN, HeaderValueConverters.mediaTypeString());
    }

    /**
     * Private ctor use factory.
     */
    private CharsetHeaderValueParameterName(final String value,
                                            final CharPredicate valueCharPredicate,
                                            final HeaderValueConverter<T> valueConverter) {
        this.value = value;
        this.valueCharPredicate = valueCharPredicate;
        this.valueConverter = valueConverter;
    }

    @Override
    public String value() {
        return this.value;
    }

    private final String value;


    final transient CharPredicate valueCharPredicate;

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
    public int compareTo(final CharsetHeaderValueParameterName name) {
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
                other instanceof CharsetHeaderValueParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final CharsetHeaderValueParameterName other) {
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
