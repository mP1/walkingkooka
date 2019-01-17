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
final public class CharsetHeaderValueParameterName<V> extends HeaderParameterName<V>
        implements Comparable<CharsetHeaderValueParameterName<?>> {

    // constants

    final static HeaderParameterNameConstants<CharsetHeaderValueParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            CharsetHeaderValueParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    CharsetHeaderValueListHeaderParser.QUOTED_PARAMETER_VALUE,
                    false,
                    CharsetHeaderValueListHeaderParser.UNQUOTED_PARAMETER_VALUE));

    /**
     * The q factor weight parameter.
     */
    public final static CharsetHeaderValueParameterName<Float> Q_FACTOR = CONSTANTS.register("q", HeaderValueConverter.qWeight());

    /**
     * Factory that creates a {@link CharsetHeaderValueParameterName}
     */
    public static CharsetHeaderValueParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private ctor use factory.
     */
    private CharsetHeaderValueParameterName(final String value,
                                            final HeaderValueConverter<V> converter) {
        super(value, converter);
    }

    // Comparable..................................................................................................

    @Override
    public int compareTo(final CharsetHeaderValueParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderValue2.................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof CharsetHeaderValueParameterName;
    }
}
