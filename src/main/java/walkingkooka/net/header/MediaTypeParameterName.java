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

import walkingkooka.naming.Name;


/**
 * The {@link Name} of an optional parameter accompanying a {@link MediaType}. Note the name may not contain the whitespace, equals sign, semi colons
 * or commas.
 */
final public class MediaTypeParameterName<T> extends HeaderParameterName<T> implements Comparable<MediaTypeParameterName<?>> {

    /**
     * A read only cache of already prepared {@link MediaTypeParameterName names}. These constants are incomplete.
     */
    private final static HeaderParameterNameConstants<MediaTypeParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            MediaTypeParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    MediaTypeHeaderParser.QUOTED_PARAMETER_VALUE,
                    true,
                    MediaTypeHeaderParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * Holds the boundary parameter name.
     */
    public final static MediaTypeParameterName<MediaTypeBoundary> BOUNDARY = CONSTANTS.register("boundary", MediaTypeBoundaryHeaderValueConverter.INSTANCE);

    /**
     * Holds the charset parameter name.
     */
    public final static MediaTypeParameterName<CharsetName> CHARSET = CONSTANTS.register("charset", HeaderValueConverter.charsetName());

    /**
     * The q factor weight parameter.
     */
    public final static MediaTypeParameterName<Float> Q_FACTOR = CONSTANTS.register("q", HeaderValueConverter.qWeight());

    /**
     * Factory that creates a {@link MediaTypeParameterName}
     */
    public static MediaTypeParameterName<?> with(final String value) {
        return CONSTANTS.lookup(value);
    }

    /**
     * Private ctor use factory.
     */
    private MediaTypeParameterName(final String value,
                                   final HeaderValueConverter<T> converter) {
        super(value, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final MediaTypeParameterName<?> other) {
        return this.compareTo0(other);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof MediaTypeParameterName;
    }
}
