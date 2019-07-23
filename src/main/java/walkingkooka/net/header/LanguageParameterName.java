/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

/**
 * The name of any parameter that accompanies a language-tag.
 */
final public class LanguageParameterName<V> extends HeaderParameterName<V> implements Comparable<LanguageParameterName<?>> {

    private final static HeaderParameterNameConstants<LanguageParameterName<?>> CONSTANTS = HeaderParameterNameConstants.empty(
            LanguageParameterName::new,
            HeaderValueHandler.quotedUnquotedString(
                    LanguageHeaderValueParser.QUOTED_PARAMETER_VALUE,
                    true,
                    LanguageHeaderValueParser.UNQUOTED_PARAMETER_VALUE)
    );

    /**
     * The q factor weight parameter.
     */
    public final static LanguageParameterName<Float> Q_FACTOR = CONSTANTS.register("q",
            HeaderValueHandler.qWeight());

    /**
     * Factory that creates a {@link LanguageParameterName}
     */
    public static LanguageParameterName<?> with(final String name) {
        return CONSTANTS.lookup(name);
    }

    /**
     * Private ctor use factory.
     */
    private LanguageParameterName(final String value,
                                  final HeaderValueHandler<V> handler) {
        super(value, handler);
    }

    // Comparable......................................................................................................

    @Override
    public int compareTo(final LanguageParameterName<?> other) {
        return this.compareTo0(other);
    }

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LanguageParameterName;
    }
}
