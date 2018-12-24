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
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.HasQFactorWeight;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Holds a simple header value, and any accompanying parameters. Parameter values will be of the type
 * compatible with each parameter name.
 */
public final class TokenHeaderValue implements HeaderValueWithParameters<TokenHeaderValueParameterName<?>>,
        Value<String>,
        HasQFactorWeight,
        UsesToStringBuilder {

    /**
     * A constants with no parameters.
     */
    public final static Map<TokenHeaderValueParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * Parses a header value expecting a single token.
     * <pre>
     * Content-Encoding: gzip;
     * </pre>
     */
    public static TokenHeaderValue parse(final String text) {
        return TokenHeaderValueOneHeaderParser.parseTokenHeaderValue(text);
    }

    /**
     * Parses a header value into tokens, which all also be sorted using their q factor weights.
     * <pre>
     * Accept-Encoding: br;q=1.0, gzip;q=0.8, *;q=0.1
     * </pre>
     */
    public static List<TokenHeaderValue> parseList(final String text) {
        return TokenHeaderValueListHeaderParser.parseTokenHeaderValueList(text);
    }

    /**
     * Formats a charsets of tokens, basically the inverse of {@link #parseList(String)}
     */
    public static String toHeaderTextList(final List<TokenHeaderValue> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        return tokens.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(", "));
    }

    /**
     * Factory that creates a new {@link TokenHeaderValue}
     */
    public static TokenHeaderValue with(final String value) {
        checkValue(value);

        return new TokenHeaderValue(value, NO_PARAMETERS);
    }

    /**
     * Private ctor use factory
     */
    private TokenHeaderValue(final String value, final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        super();
        this.value = value;
        this.parameters = parameters;
    }

    // value.........................................................................................

    @Override
    public String value() {
        return this.value;
    }

    public TokenHeaderValue setValue(final String value) {
        checkValue(value);

        return this.value.equals(value) ?
                this :
                this.replace(value, this.parameters);
    }

    private final String value;

    private static void checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", CharPredicates.rfc2045Token());
    }

    // parameters.........................................................................................

    /**
     * A map view of all parameters to their text or string value.
     */
    @Override
    public Map<TokenHeaderValueParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    @Override
    public TokenHeaderValue setParameters(final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        final Map<TokenHeaderValueParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.value, copy);
    }

    private final Map<TokenHeaderValueParameterName<?>, Object> parameters;

    private static Map<TokenHeaderValueParameterName<?>, Object> checkParameters(final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        final Map<TokenHeaderValueParameterName<?>, Object> copy = Maps.ordered();
        for (Entry<TokenHeaderValueParameterName<?>, Object> nameAndValue : parameters.entrySet()) {
            final TokenHeaderValueParameterName<?> name = nameAndValue.getKey();
            copy.put(name,
                    name.checkValue(nameAndValue.getValue()));
        }
        return copy;
    }

    // replace ...........................................................................................................

    private TokenHeaderValue replace(final String value,
                                     final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        return new TokenHeaderValue(value, parameters);
    }

    // HasQFactorWeight................................................................................................

    @Override
    public Optional<Float> qFactorWeight() {
        return Optional.ofNullable(Float.class.cast(this.parameters.get(TokenHeaderValueParameterName.Q)));
    }

    // HeaderValue .............................................................................................

    @Override
    public String toHeaderText() {
        return this.toString();
    }

    /**
     * Only returns true if the value is WILDCARD.
     */
    public boolean isWildcard() {
        return WILDCARD.equals(this.value());
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // Object .............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof TokenHeaderValue &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final TokenHeaderValue other) {
        return this.value.equals(other.value) &&
                this.parameters.equals(other.parameters);
    }

    /**
     * Dumps the raw header value without quotes.
     */
    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.value(this.value);

        builder.separator(TO_STRING_PARAMETER_SEPARATOR);
        builder.valueSeparator(TO_STRING_PARAMETER_SEPARATOR);
        builder.labelSeparator(PARAMETER_NAME_VALUE_SEPARATOR.string());
        builder.value(this.parameters);
    }

    /**
     * Separator between parameters used by {@link #toString()}.
     */
    private final static String TO_STRING_PARAMETER_SEPARATOR = PARAMETER_SEPARATOR.string().concat(" ");
}
