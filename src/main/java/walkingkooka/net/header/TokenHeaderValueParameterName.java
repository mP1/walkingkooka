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

import java.util.Optional;

/**
 * The {@link Name} of token header value parameter.
 */
final public class TokenHeaderValueParameterName<T> extends HeaderParameterName<T> implements Comparable<TokenHeaderValueParameterName<?>> {

    /**
     * Constant returned when a parameter value is absent.
     */
    public final static Optional<?> VALUE_ABSENT = Optional.empty();

    /**
     * A read only cache of already prepared {@link TokenHeaderValueParameterName names}. These constants are incomplete.
     */
    private final static HeaderParameterNameConstants<TokenHeaderValueParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            TokenHeaderValueParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    TokenHeaderValueHeaderParser.QUOTED_PARAMETER_VALUE,
                    true,
                    TokenHeaderValueHeaderParser.UNQUOTED_PARAMETER_VALUE));

    /**
     * A {@link TokenHeaderValueParameterName} holding <code>Q</code>
     */
    public final static TokenHeaderValueParameterName<Float> Q = CONSTANTS.register("Q", HeaderValueConverter.qWeight());

    /**
     * Factory that creates a {@link TokenHeaderValueParameterName}. If the parameter is not a constant it will
     * assume all values are a {@link String}.
     */
    public static TokenHeaderValueParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private constructor use factory.
     */
    private TokenHeaderValueParameterName(final String name, final HeaderValueConverter<T> converter) {
        super(name, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final TokenHeaderValueParameterName<?> other) {
        return this.compareTo0(other);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TokenHeaderValueParameterName;
    }
}
