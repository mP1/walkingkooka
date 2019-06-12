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
final public class EncodingParameterName<V> extends HeaderParameterName<V> implements Comparable<EncodingParameterName<?>> {

    private final static HeaderParameterNameConstants<EncodingParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            EncodingParameterName::new,
            HeaderValueHandler.quotedUnquotedString(
                    AcceptEncodingHeaderValueParser.QUOTED_PARAMETER_VALUE,
                    true,
                    AcceptEncodingHeaderValueParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * The q factor weight parameter.
     */
    public final static EncodingParameterName<Float> Q_FACTOR = CONSTANTS.register("q",
            HeaderValueHandler.qWeight());

    /**
     * Factory that creates a {@link EncodingParameterName}
     */
    public static EncodingParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private ctor use factory.
     */
    private EncodingParameterName(final String value,
                                  final HeaderValueHandler<V> handler) {
        super(value, handler);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final EncodingParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EncodingParameterName;
    }
}
