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
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.HasQFactorWeight;
import walkingkooka.net.http.HttpHeaderScope;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * A {@link HeaderValueWithParameters} that represents a single charset name with optional parameters.
 */
final public class CharsetHeaderValue implements Value<CharsetName>,
        HeaderValueWithParameters<CharsetHeaderValueParameterName<?>>,
        HasQFactorWeight {

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
                null :
                CONSTANTS.get(charset);
        return null != result ?
                result :
                new CharsetHeaderValue(charset, parameters);
    }

    /**
     * Formats or converts a list of media types back to a String. Basically
     * an inverse of {@link #parse(String)}.
     */
    public static String toHeaderTextList(final List<CharsetHeaderValue> charsetHeaderValues) {
        Objects.requireNonNull(charsetHeaderValues, "charsetHeaderValues");

        return charsetHeaderValues.stream()
                .map(m -> m.toString())
                .collect(Collectors.joining(TOSTRING_CHARSET_SEPARATOR));
    }

    private final static String TOSTRING_CHARSET_SEPARATOR = SEPARATOR + " ";

    // ctor ...................................................................................................

    /**
     * Private constructor
     */
    private CharsetHeaderValue(final CharsetName charsetName,
                               final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        super();

        this.charsetName = charsetName;
        this.parameters = parameters;
    }

    // charsetName .......................................................................................................

    /**
     * Getter that returns the {@link CharsetName} component.
     */
    @Override
    public CharsetName value() {
        return this.charsetName;
    }

    /**
     * Would be setter that returns an instance with the new {@link CharsetName}, creating a new instance if required.
     */
    public CharsetHeaderValue setValue(final CharsetName charsetName) {
        checkValue(charsetName);

        return this.charsetName.equals(charsetName) ?
                this :
                this.replace(charsetName, this.parameters);
    }

    private final CharsetName charsetName;

    static void checkValue(final CharsetName charsetName) {
        Objects.requireNonNull(charsetName, "charsetName");
    }

    // parameters ...............................................................................................

    /**
     * Retrieves the parameters.
     */
    @Override
    public Map<CharsetHeaderValueParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    @Override
    public CharsetHeaderValue setParameters(final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        final Map<CharsetHeaderValueParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.charsetName, copy);
    }

    /**
     * Package private for testing.
     */
    private transient final Map<CharsetHeaderValueParameterName<?>, Object> parameters;

    /**
     * While checking the parameters (name and value) makes a defensive copy.
     */
    private static Map<CharsetHeaderValueParameterName<?>, Object> checkParameters(final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        final Map<CharsetHeaderValueParameterName<?>, Object> copy = Maps.sorted();
        for (Entry<CharsetHeaderValueParameterName<?>, Object> nameAndValue : parameters.entrySet()) {
            final CharsetHeaderValueParameterName name = nameAndValue.getKey();
            copy.put(name,
                    name.checkValue(nameAndValue.getValue()));
        }
        return copy;
    }

    // replace .................................................................................................

    private CharsetHeaderValue replace(final CharsetName charsetName,
                                       final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        return CharsetHeaderValue.withParameters(charsetName, parameters);
    }

    // qWeight ...................................................................

    /**
     * Retrieves the q-weight for this value. If the value is not a number a {@link IllegalStateException} will be thrown.
     */
    public Optional<Float> qFactorWeight() {
        return Optional.ofNullable(Float.class.cast(this.parameters()
                .get(CharsetHeaderValueParameterName.Q_FACTOR)));
    }

    // HeaderValue................................................................................................................

    @Override
    public String toHeaderText() {
        return this.toString();
    }

    // HasHttpHeaderScope ....................................................................................................

    @Override
    public HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    // Object................................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.charsetName, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof CharsetHeaderValue &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final CharsetHeaderValue other) {
        return this.charsetName.equals(other.charsetName) && //
                this.parameters.equals(other.parameters);
    }

    /**
     * Rebuilds a string with the charset name and any parameters.
     */
    @Override
    public String toString() {
        return "" + this.charsetName + this.parameters.entrySet()
                .stream()
                .map(CharsetHeaderValue::toStringParameter)
                .collect(Collectors.joining());
    }

    private static String toStringParameter(final Entry<CharsetHeaderValueParameterName<?>, Object> nameAndValue) {
        final CharsetHeaderValueParameterName<?> name = nameAndValue.getKey();
        return TO_STRING_PARAMETER_SEPARATOR +
                name.value() +
                PARAMETER_NAME_VALUE_SEPARATOR.character() +
                name.valueConverter.toText(Cast.to(nameAndValue.getValue()), name);
    }

    private final static String TO_STRING_PARAMETER_SEPARATOR = PARAMETER_SEPARATOR + " ";
}
