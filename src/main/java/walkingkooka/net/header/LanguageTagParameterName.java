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

import walkingkooka.collect.map.Maps;

import java.util.Map;


/**
 * The name of any parameter that accompanies a language-tag.
 */
final public class LanguageTagParameterName<T> extends HeaderParameterName<T> implements Comparable<LanguageTagParameterName<?>> {

    // constants

    /**
     * A read only cache of already prepared {@link LanguageTagParameterName names}. These constants are incomplete.
     */
    final static Map<String, LanguageTagParameterName> CONSTANTS = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

    /**
     * Creates and adds a new {@link LanguageTagParameterName} to the cache being built.
     */
    private static <T> LanguageTagParameterName<T> registerConstant(final String name,
                                                                    final HeaderValueConverter<T> converter) {
        final LanguageTagParameterName<T> parameterName = new LanguageTagParameterName<T>(name, converter);
        LanguageTagParameterName.CONSTANTS.put(name, parameterName);
        return parameterName;
    }

    /**
     * The q factor weight parameter.
     */
    public final static LanguageTagParameterName<Float> Q_FACTOR = registerConstant("q",
            HeaderValueConverter.qWeight());

    /**
     * Factory that creates a {@link LanguageTagParameterName}
     */
    public static LanguageTagParameterName<?> with(final String value) {
        MediaType.check(value, "value");

        final LanguageTagParameterName<?> parameterName = CONSTANTS.get(value);
        return null != parameterName ?
                parameterName :
                new LanguageTagParameterName<String>(value, QUOTED_UNQUOTED_STRING);
    }

    private final static HeaderValueConverter<String> QUOTED_UNQUOTED_STRING = HeaderValueConverter.quotedUnquotedString(
            MediaTypeHeaderParser.QUOTED_PARAMETER_VALUE,
            true,
            MediaTypeHeaderParser.UNQUOTED_PARAMETER_VALUE);

    /**
     * Private ctor use factory.
     */
    private LanguageTagParameterName(final String value,
                                     final HeaderValueConverter<T> converter) {
        super(value, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final LanguageTagParameterName<?> other) {
        return this.compareTo0(other);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageTagParameterName;
    }
}
