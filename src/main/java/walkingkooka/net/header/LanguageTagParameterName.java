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
 * The name of any parameter that accompanies a language-tag.
 */
final public class LanguageTagParameterName<V> extends HeaderParameterName<V> implements Comparable<LanguageTagParameterName<?>> {

    private final static HeaderParameterNameConstants<LanguageTagParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            LanguageTagParameterName::new,
            HeaderValueConverter.quotedUnquotedString(
                    LanguageTagHeaderValueParser.QUOTED_PARAMETER_VALUE,
                    true,
                    LanguageTagHeaderValueParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * The q factor weight parameter.
     */
    public final static LanguageTagParameterName<Float> Q_FACTOR = CONSTANTS.register("q",
            HeaderValueConverter.qWeight());

    /**
     * Factory that creates a {@link LanguageTagParameterName}
     */
    public static LanguageTagParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private ctor use factory.
     */
    private LanguageTagParameterName(final String value,
                                     final HeaderValueConverter<V> converter) {
        super(value, converter);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final LanguageTagParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageTagParameterName;
    }
}
