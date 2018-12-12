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

import java.util.Map;
import java.util.Objects;


/**
 * The {@link Name} of an optional parameter accompanying a {@link MediaType}. Note the name may not contain the whitespace, equals sign, semi colons
 * or commas.
 */
final public class MediaTypeParameterName<T> implements HeaderParameterName<T>,
        Comparable<MediaTypeParameterName<?>>{

    // constants

    /**
     * A read only cache of already prepared {@link MediaTypeParameterName names}. These constants are incomplete.
     */
    final static Map<String, MediaTypeParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link MediaTypeParameterName} to the cache being built.
     */
    private static <T> MediaTypeParameterName<T> registerConstant(final String name,
                                                                  final CharPredicate charPredicate,
                                                                  final HeaderValueConverter<T> valueConverter) {
        final MediaTypeParameterName<T> parameterName = new MediaTypeParameterName<T>(name,
                charPredicate,
                valueConverter);
        MediaTypeParameterName.CONSTANTS.put(name, parameterName);
        return parameterName;
    }

    /**
     * Holds the boundary parameter name.
     */
    public final static MediaTypeParameterName<MediaTypeBoundary> BOUNDARY = registerConstant("boundary",
            MediaTypeBoundary.PREDICATE, // part includes initial.
            MediaTypeBoundaryHeaderValueConverter.INSTANCE);

    /**
     * Holds the charset parameter name.
     */
    public final static MediaTypeParameterName<CharsetName> CHARSET = registerConstant("charset",
            CharsetName.PART_CHAR_PREDICATE, // part includes initial.
            HeaderValueConverters.charsetName());


    private final static CharPredicate DIGITS = CharPredicates.digit()
            .or(CharPredicates.any("+-."))
            .setToString("Q factor");

    /**
     * The q factor weight parameter.
     */
    public final static MediaTypeParameterName<Float> Q_FACTOR = registerConstant("q",
            DIGITS,
            HeaderValueConverters.floatConverter());

    /**
     * Factory that creates a {@link MediaTypeParameterName}
     */
    public static MediaTypeParameterName<?> with(final String value) {
        MediaType.check(value, "value");

        final MediaTypeParameterName<?> parameterName = CONSTANTS.get(value);
        return null != parameterName ?
                parameterName :
                new MediaTypeParameterName<String>(value, MediaTypeHeaderParser.RFC2045TOKEN, HeaderValueConverters.mediaTypeAutoQuotingString());
    }

    /**
     * Private ctor use factory.
     */
    private MediaTypeParameterName(final String value,
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
    public T checkValue(final Object value) {
        return this.valueConverter.check(value);
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
}
