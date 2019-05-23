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

/**
 * The name of any parameter that accompanies a accept-encoding header
 */
final public class AcceptEncodingParameterName<V> extends HeaderParameterName<V> implements Comparable<AcceptEncodingParameterName<?>> {

    private final static HeaderParameterNameConstants<AcceptEncodingParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            AcceptEncodingParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    AcceptEncodingListHeaderValueParser.QUOTED_PARAMETER_VALUE,
                    true,
                    AcceptEncodingListHeaderValueParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * The q factor weight parameter.
     */
    public final static AcceptEncodingParameterName<Float> Q_FACTOR = CONSTANTS.register("q",
            HeaderValueConverter.qWeight());

    /**
     * Factory that creates a {@link AcceptEncodingParameterName}
     */
    public static AcceptEncodingParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private ctor use factory.
     */
    private AcceptEncodingParameterName(final String value,
                                        final HeaderValueConverter<V> converter) {
        super(value, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final AcceptEncodingParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AcceptEncodingParameterName;
    }
}
