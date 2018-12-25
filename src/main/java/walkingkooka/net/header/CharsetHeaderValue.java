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
import walkingkooka.net.HasQFactorWeight;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * A {@link HeaderValueWithParameters} that represents a single charset name with optional parameters.
 */
final public class CharsetHeaderValue extends HeaderValueWithParameters2<CharsetHeaderValue,
        CharsetHeaderValueParameterName<?>,
        CharsetName> implements HasQFactorWeight {

    /**
     * No parameters.
     */
    public final static Map<CharsetHeaderValueParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    // MediaType constants.................................................................................................

    /**
     * Holds all constants.
     */
    private final static Map<CharsetName, CharsetHeaderValue> CONSTANTS = Maps.sorted();

    /**
     * Holds a {@link CharsetHeaderValue} that matches all {@link CharsetHeaderValue text types}.
     */
    public final static CharsetHeaderValue WILDCARD_VALUE = registerConstant(CharsetName.WILDCARD_CHARSET);

    /**
     * Creates and then registers the constant.
     */
    static private CharsetHeaderValue registerConstant(final CharsetName charsetName) {
        final CharsetHeaderValue charsetHeaderValue = new CharsetHeaderValue(charsetName, NO_PARAMETERS);
        CONSTANTS.put(charsetName, charsetHeaderValue);

        return charsetHeaderValue;
    }

    /**
     * Creates a {@link List} of {@link CharsetHeaderValue}.
     */
    public static List<CharsetHeaderValue> parse(final String text) {
        return CharsetHeaderValueListHeaderParser.parseCharsetHeaderValueList(text);
    }

    /**
     * Creates a {@link CharsetHeaderValue} using the already broken type and sub types. It is not possible to pass parameters with or without values.
     */
    public static CharsetHeaderValue with(final CharsetName charsetName) {
        checkValue(charsetName);

        final CharsetHeaderValue charsetHeaderValue = CONSTANTS.get(charsetName);
        return null != charsetHeaderValue ?
                charsetHeaderValue :
                new CharsetHeaderValue(charsetName, NO_PARAMETERS);
    }

    /**
     * Factory method called by various setters and parsers, that tries to match constants before creating an actual new
     * instance.
     */
    static CharsetHeaderValue withParameters(final CharsetName charset,
                                             final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        final CharsetHeaderValue result = parameters.isEmpty() ?
                CONSTANTS.get(charset) :
                null;
        return null != result ?
                result :
                new CharsetHeaderValue(charset, parameters);
    }

    // ctor ...................................................................................................

    /**
     * Private constructor
     */
    private CharsetHeaderValue(final CharsetName charsetName,
                               final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        super(charsetName, parameters);
    }

    // charsetName .......................................................................................................

    /**
     * Would be setter that returns an instance with the new {@link CharsetName}, creating a new instance if required.
     */
    public CharsetHeaderValue setValue(final CharsetName charsetName) {
        checkValue(charsetName);

        return this.value.equals(charsetName) ?
                this :
                this.replace(charsetName, this.parameters);
    }

    static void checkValue(final CharsetName charsetName) {
        Objects.requireNonNull(charsetName, "charsetName");
    }

    // parameters ...............................................................................................

    /**
     * Retrieves the q-weight for this value.
     */
    public final Optional<Float> qFactorWeight() {
        return this.qFactorWeight(CharsetHeaderValueParameterName.Q_FACTOR);
    }

    // replace .................................................................................................

    @Override
    CharsetHeaderValue replace(final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        return this.replace(this.value, parameters);
    }

    private CharsetHeaderValue replace(final CharsetName charsetName,
                                       final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        return CharsetHeaderValue.withParameters(charsetName, parameters);
    }

    // HeaderValue................................................................................................................

    @Override
    public String toHeaderText() {
        return this.toString();
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    @Override
    int hashCode0(final CharsetName value) {
        return value.hashCode();
    }

    @Override
    boolean equals1(final CharsetName value, final CharsetName otherValue) {
        return value.equals(otherValue);
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof CharsetHeaderValue;
    }
}
