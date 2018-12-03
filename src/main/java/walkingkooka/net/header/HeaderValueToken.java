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
import walkingkooka.text.CharacterConstant;

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
public final class HeaderValueToken implements HeaderValueWithParameters<HeaderValueTokenParameterName<?>>,
        Value<String>,
        HasQFactorWeight,
        UsesToStringBuilder {

    /**
     * A constants with no parameters.
     */
    public final static Map<HeaderValueTokenParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * The separator between parameter name and value.
     */
    public final static CharacterConstant PARAMETER_NAME_VALUE_SEPARATOR = CharacterConstant.with('=');

    /**
     * The separator character that separates multiple parameters.
     */
    public final static CharacterConstant PARAMETER_SEPARATOR = CharacterConstant.with(';');

    /**
     * The separator character that separates values within a header value.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with(',');

    /**
     * Constant holding the wildcard value (without any parameters).
     */
    public final static String WILDCARD = "*";

    /**
     * Parses a header value into tokens, which aill also be sorted using their q factor weights.
     * <pre>
     * Accept-Charset: utf-8, iso-8859-1;q=0.5
     * </pre>
     */
    public static List<HeaderValueToken> parse(final String text) {
        return HeaderValueTokenHeaderParser.parseHeaderValueTokenList(text);
    }

    /**
     * Formats a list of tokens, basically the inverse of {@link #parse(String)}
     */
    public static String format(final List<HeaderValueToken> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        return tokens.stream()
                .map(t -> t.toString())
                .collect(Collectors.joining(", "));
    }

    /**
     * Factory that creates a new {@link HeaderValueToken}
     */
    public static HeaderValueToken with(final String value, final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "token value", CharPredicates.rfc2045Token());

        return new HeaderValueToken(value, checkParameters(parameters));
    }

    /**
     * Private ctor use factory
     */
    private HeaderValueToken(final String value, final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        super();
        this.value = value;
        this.parameters = parameters;
    }

    // value.........................................................................................

    @Override
    public String value() {
        return this.value;
    }

    public HeaderValueToken setValue(final String value) {
        checkValue(value);

        return this.value.equals(value) ?
                this :
                this.replace(value, this.parameters);
    }

    private final String value;

    private static void checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", CharPredicates.rfc2045Token());
    }

    /**
     * Only returns true if the value is WILDCARD.
     */
    public boolean isWildcard() {
        return WILDCARD.equals(this.value());
    }

    // parameters.........................................................................................

    /**
     * A map view of all parameters to their text or string value.
     */
    @Override
    public Map<HeaderValueTokenParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    @Override
    public HeaderValueToken setParameters(final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        final Map<HeaderValueTokenParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.value, copy);
    }

    private final Map<HeaderValueTokenParameterName<?>, Object> parameters;

    private static Map<HeaderValueTokenParameterName<?>, Object> checkParameters(final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        final Map<HeaderValueTokenParameterName<?>, Object> copy = Maps.ordered();
        for (Entry<HeaderValueTokenParameterName<?>, Object> nameAndValue : parameters.entrySet()) {
            final HeaderValueTokenParameterName<?> name = nameAndValue.getKey();
            final Object value = nameAndValue.getValue();
            name.checkValue(value);
            copy.put(name, value);
        }
        return copy;
    }

    // replace ...........................................................................................................

    private HeaderValueToken replace(final String value, final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        return new HeaderValueToken(value, parameters);
    }

    // HasQFactorWeight................................................................................................

    @Override
    public Optional<Float> qFactorWeight() {
        return Optional.ofNullable(Float.class.cast(this.parameters.get(HeaderValueTokenParameterName.Q)));
    }

    // HeaderValue .............................................................................................

    @Override
    public String headerValue() {
        return this.toString();
    }

    // Object .............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HeaderValueToken &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HeaderValueToken other) {
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
