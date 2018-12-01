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
import walkingkooka.NeverError;
import walkingkooka.Value;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.HasQFactorWeight;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;
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
public final class HeaderValueToken implements HeaderValue,
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
     * Parses a header value into tokens, which aill also be sorted using their q factor weights.
     * <pre>
     * Accept-Charset: utf-8, iso-8859-1;q=0.5
     * </pre>
     */
    public static List<HeaderValueToken> parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");

        final char parameterSeparator = PARAMETER_SEPARATOR.character();
        final char parameterNameValueSeparator = PARAMETER_NAME_VALUE_SEPARATOR.character();
        final char separator = SEPARATOR.character();

        final List<HeaderValueToken> tokens = Lists.array();

        int mode = MODE_VALUE;
        int start = 0;

        String value = null;
        HeaderValueTokenParameterName<?> parameterName = null;
        Map<HeaderValueTokenParameterName<?>, Object> parameters = NO_PARAMETERS;

        int i = 0;
        final int length = text.length();
        while(i < length) {
            final char c = text.charAt(i);

            switch (mode) {
                case MODE_VALUE:
                    if (TOKEN.test(c)) {
                        break;
                    }
                    if (parameterSeparator == c) {
                        value = value(text, start, i);
                        parameterName = null;
                        parameters = Maps.ordered();
                        mode = MODE_PARAMETER_SEPARATOR;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        tokens.add(new HeaderValueToken(value(text, start, i), NO_PARAMETERS));
                        mode = MODE_SEPARATOR;
                        break;
                    }
                    if(WHITESPACE.test(c)) {
                        value = value(text, start, i);
                        parameters = Maps.ordered();
                        mode = MODE_VALUE_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_VALUE_WHITESPACE:
                    if(WHITESPACE.test(c)) {
                        break;
                    }
                    if(separator==c) {
                        tokens.add(new HeaderValueToken(value, NO_PARAMETERS));
                        mode = MODE_SEPARATOR;
                        start = i;
                        break;
                    }
                    mode = MODE_PARAMETER_SEPARATOR;
                    start = i;
                case MODE_PARAMETER_SEPARATOR:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (separator == c) {
                        tokens.add(new HeaderValueToken(value(text, start, i), NO_PARAMETERS));
                        mode = MODE_VALUE;
                        start = i + 1;
                        break;
                    }
                    if (parameterSeparator == c) {
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    mode = MODE_PARAMETER_NAME;
                    start = i;
                    i--; // try char again as NAME
                    break;
                case MODE_PARAMETER_SEPARATOR_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    mode = MODE_PARAMETER_NAME;
                    start = i;
                    parameterName = null;
                    // intentional fall thru...
                case MODE_PARAMETER_NAME:
                    if (TOKEN.test(c)) {
                        break;
                    }
                    if (WHITESPACE.test(c)) {
                        parameterName = parameterName(text, start, i);
                        mode = MODE_PARAMETER_NAME_WHITESPACE;
                        break;
                    }
                    if (parameterNameValueSeparator == c) {
                        parameterName = parameterName(text, start, i);
                        mode = MODE_PARAMETER_EQUALS_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_NAME_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (parameterNameValueSeparator == c) {
                        mode = MODE_PARAMETER_EQUALS_WHITESPACE;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_EQUALS_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    mode = MODE_PARAMETER_VALUE;
                    start = i;
                    // fall thru intentional
                case MODE_PARAMETER_VALUE:
                    if (TOKEN.test(c)) {
                        break;
                    }
                    if (WHITESPACE.test(c)) {
                        addParameterValue(text, start, i, parameterName, parameters);
                        mode = MODE_PARAMETER_VALUE_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    if (parameterSeparator == c) {
                        addParameterValue(text, start, i, parameterName, parameters);
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        addParameterValue(text, start, i, parameterName, parameters);
                        tokens.add(new HeaderValueToken(value, parameters));
                        mode = MODE_SEPARATOR;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_VALUE_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (parameterSeparator == c) {
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        tokens.add(new HeaderValueToken(value, parameters));
                        mode = MODE_VALUE;
                        start = i + 1;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_SEPARATOR:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    ;
                    mode = MODE_VALUE;
                    start = i;
                    break;

                default:
                    NeverError.unhandledCase(mode);
            }

            i++;
        }

        switch (mode) {
            case MODE_VALUE:
                tokens.add(new HeaderValueToken(value(text, start, i), NO_PARAMETERS));
                break;
            case MODE_VALUE_WHITESPACE:
                tokens.add(new HeaderValueToken(value, NO_PARAMETERS));
                break;
            case MODE_PARAMETER_NAME_WHITESPACE:
            case MODE_PARAMETER_EQUALS_WHITESPACE:
                failEmptyToken(PARAMETER_VALUE, i-1, text);
            case MODE_PARAMETER_VALUE:
                addParameterValue(text, start, i, parameterName, parameters);
                tokens.add(new HeaderValueToken(value, parameters));
                break;
            case MODE_PARAMETER_VALUE_WHITESPACE:
                tokens.add(new HeaderValueToken(value, parameters));
                break;
            case MODE_SEPARATOR:
                failEmptyToken(VALUE, i, text);
            default:
                break;
        }

        tokens.sort(HasQFactorWeight.qFactorDescendingComparator());
        return Lists.readOnly(tokens);
    }

    private final static int MODE_VALUE = 1;
    private final static int MODE_VALUE_WHITESPACE = MODE_VALUE + 1;
    private final static int MODE_PARAMETER_SEPARATOR = MODE_VALUE_WHITESPACE + 1;
    private final static int MODE_PARAMETER_SEPARATOR_WHITESPACE = MODE_PARAMETER_SEPARATOR + 1;
    private final static int MODE_PARAMETER_NAME = MODE_PARAMETER_SEPARATOR_WHITESPACE + 1;
    private final static int MODE_PARAMETER_NAME_WHITESPACE = MODE_PARAMETER_NAME + 1;
    private final static int MODE_PARAMETER_EQUALS_WHITESPACE = MODE_PARAMETER_NAME_WHITESPACE + 1;
    private final static int MODE_PARAMETER_VALUE = MODE_PARAMETER_EQUALS_WHITESPACE + 1;
    private final static int MODE_PARAMETER_VALUE_WHITESPACE = MODE_PARAMETER_VALUE + 1;
    private final static int MODE_SEPARATOR = MODE_PARAMETER_VALUE_WHITESPACE + 1;

    private final static CharPredicate TOKEN = CharPredicates.rfc2045Token();
    private final static CharPredicate WHITESPACE = CharPredicates.any("\u0009\u0020")
            .setToString("SP|HTAB");

    private final static String VALUE = "value";
    private final static String PARAMETER_NAME = "parameter name";
    private final static String PARAMETER_VALUE = "parameter value";

    /**
     * Reports an invalid character within the unparsed text.
     */
    private static void failInvalidCharacter(final int i, final String text) {
        throw new HeaderValueException(invalidCharacter(i, text));
    }

    /**
     * Builds a message to report an invalid or unexpected character.
     */
    static String invalidCharacter(final int i, final String text) {
        return "Invalid character " + CharSequences.quoteIfChars(text.charAt(i)) + " at " + i + " in " + CharSequences.quoteAndEscape(text);
    }

    /**
     * Creates a parameter name from the token
     */
    private static String value(final String text, final int start, final int end) {
        return token(VALUE, text, start, end);
    }

    /**
     * Creates a parameter name from the token
     */
    private static HeaderValueTokenParameterName<?> parameterName(final String text, final int start, final int end) {
        return HeaderValueTokenParameterName.with(token(PARAMETER_NAME, text, start, end));
    }

    /**
     * Extracts the parameter value, converts it, and then adds it to the parameters.
     */
    private static void addParameterValue(final String text,
                                          final int start,
                                          final int end,
                                          final HeaderValueTokenParameterName<?> name,
                                          final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        parameters.put(name,
                name.toValue(
                        token(PARAMETER_VALUE, text, start, end)));
    }

    /**
     * Extracts the token failing if it is empty.
     */
    private static String token(final String tokenName, final String text, final int start, final int end) {
        if (start == end) {
            failEmptyToken(tokenName, end, text);
        }
        return text.substring(start, end);
    }

    /**
     * Reports an invalid character within the unparsed header type.
     */
    private static void failEmptyToken(final String token, final int i, final String text) {
        throw new HeaderValueException("Missing " + token + " at " + i + " in " + CharSequences.quoteAndEscape(text));
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

    // parameters.........................................................................................

    /**
     * A map view of all parameters to their text or string value.
     *
     * @return
     */
    public Map<HeaderValueTokenParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    public HeaderValueToken setParameters(final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        final Map<HeaderValueTokenParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.value, copy);
    }

    private final Map<HeaderValueTokenParameterName<?>, Object> parameters;

    private static Map<HeaderValueTokenParameterName<?>, Object> checkParameters(final Map<HeaderValueTokenParameterName<?>, Object> parameters) {
        final Map<HeaderValueTokenParameterName<?>, Object> copy = Maps.ordered();
        for(Entry<HeaderValueTokenParameterName<?>, Object> nameAndValue  : parameters.entrySet()) {
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
