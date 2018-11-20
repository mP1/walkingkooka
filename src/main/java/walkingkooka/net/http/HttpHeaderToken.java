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

package walkingkooka.net.http;

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
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds a simple header value, and any accompanying parameters.
 */
public final class HttpHeaderToken implements HashCodeEqualsDefined, Value<String>, HasQFactorWeight, UsesToStringBuilder {

    /**
     * A constants with no parameters.
     */
    public final static Map<HttpHeaderParameterName<?>, String> NO_PARAMETERS = Maps.empty();

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
     * Parses a header value into tokens.
     * <pre>
     * Accept-Charset: utf-8, iso-8859-1;q=0.5
     * </pre>
     */
    public static List<HttpHeaderToken> parse(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");


        final char parameterSeparator = PARAMETER_SEPARATOR.character();
        final char parameterNameValueSeparator = PARAMETER_NAME_VALUE_SEPARATOR.character();
        final char separator = SEPARATOR.character();

        final List<HttpHeaderToken> tokens = Lists.array();

        int mode = MODE_VALUE;
        int start = 0;

        String value = null;
        HttpHeaderParameterName<?> parameterName = null;
        Map<HttpHeaderParameterName<?>, String> parameters = NO_PARAMETERS;

        int i = 0;
        for (char c : text.toCharArray()) {

            switch (mode) {
                case MODE_VALUE:
                    if (isTokenChar(c)) {
                        break;
                    }
                    if (parameterSeparator == c) {
                        value = token(VALUE, text, start, i);
                        parameterName = null;
                        parameters = Maps.ordered();
                        mode = MODE_PARAMETER_SEPARATOR;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        tokens.add(HttpHeaderToken.with(token(VALUE, text, start, i), HttpHeaderToken.NO_PARAMETERS));
                        mode = MODE_SEPARATOR;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_SEPARATOR:
                    if (' ' == c) {
                        break;
                    }
                    if (separator == c) {
                        tokens.add(HttpHeaderToken.with(token(VALUE, text, start, i), HttpHeaderToken.NO_PARAMETERS));
                        mode = MODE_VALUE;
                        start = i + 1;
                        break;
                    }

                    mode = MODE_PARAMETER_NAME;
                    start = i;
                    parameterName = null;
                    parameters = Maps.ordered();
                case MODE_PARAMETER_NAME:
                    if (isTokenChar(c)) {
                        break;
                    }
                    if (parameterNameValueSeparator == c) {
                        parameterName = HttpHeaderParameterName.with(token(PARAMETER_NAME, text, start, i));
                        mode = MODE_PARAMETER_VALUE;
                        start = i + 1;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_VALUE:
                    if (isTokenChar(c)) {
                        break;
                    }
                    if (' ' == c) {
                        parameters.put(parameterName, token(PARAMETER_VALUE, text, start, i));
                        mode = MODE_PARAMETER_VALUE_WHITESPACE;
                        start = i + 1;
                        break;
                    }
                    if (parameterSeparator == c) {
                        parameters.put(parameterName, token(PARAMETER_VALUE, text, start, i));
                        mode = MODE_PARAMETER_NAME;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        parameters.put(parameterName, token(PARAMETER_VALUE, text, start, i));
                        tokens.add(HttpHeaderToken.with(value, parameters));
                        mode = MODE_SEPARATOR;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_PARAMETER_VALUE_WHITESPACE:
                    if (' ' == c) {
                        break;
                    }
                    if (parameterSeparator == c) {
                        mode = MODE_PARAMETER_NAME;
                        start = i + 1;
                        break;
                    }
                    if (separator == c) {
                        mode = MODE_VALUE;
                        start = i + 1;
                        break;
                    }
                    failInvalidCharacter(i, text);
                case MODE_SEPARATOR:
                    if (' ' == c) {
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
                tokens.add(HttpHeaderToken.with(token(VALUE, text, start, i), HttpHeaderToken.NO_PARAMETERS));
                break;
            case MODE_PARAMETER_VALUE:
                parameters.put(parameterName, token(PARAMETER_VALUE, text, start, i));
                tokens.add(HttpHeaderToken.with(value, parameters));
                break;
            case MODE_SEPARATOR:
                failEmptyToken(VALUE, i, text);
            default:
                break;
        }

        return Lists.readOnly(tokens);
    }

    private final static int MODE_VALUE = 1;
    private final static int MODE_PARAMETER_SEPARATOR = MODE_VALUE + 1;
    private final static int MODE_PARAMETER_NAME = MODE_PARAMETER_SEPARATOR + 1;
    private final static int MODE_PARAMETER_VALUE = MODE_PARAMETER_NAME + 1;
    private final static int MODE_PARAMETER_VALUE_WHITESPACE = MODE_PARAMETER_VALUE + 1;
    private final static int MODE_SEPARATOR = MODE_PARAMETER_VALUE_WHITESPACE + 1;

    private static boolean isTokenChar(final char c) {
        return TOKEN.test(c);
    }

    private final static CharPredicate TOKEN = HttpCharPredicates.rf2045Token();

    /**
     * Reports an invalid character within the unparsed text.
     */
    private static void failInvalidCharacter(final int i, final String text) {
        throw new IllegalArgumentException(invalidCharacter(i, text));
    }

    /**
     * Builds a message to report an invalid or unexpected character.
     */
    static String invalidCharacter(final int i, final String text) {
        return "Invalid character " + CharSequences.quoteIfChars(text.charAt(i)) + " at " + i + " in " + CharSequences.quoteAndEscape(text);
    }

    private final static String VALUE = "value";
    private final static String PARAMETER_NAME = "parameter name";
    private final static String PARAMETER_VALUE = "parameter value";

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
     * Reports an invalid character within the unparsed media type.
     */
    private static void failEmptyToken(final String token, final int i, final String text) {
        throw new IllegalArgumentException("Missing " + token + " at " + i + " in " + CharSequences.quoteAndEscape(text));
    }


    ///////////

    /**
     * Parsers an individual token including its parameters.
     */
    private static HttpHeaderToken parseToken(final String text) {
        final String[] tokens = text.split(",");
        if (tokens.length == 0) {
            throw new IllegalArgumentException("Missing value=" + CharSequences.quote(text));
        }

        final Map<HttpHeaderParameterName<?>, String> parameters = Maps.ordered();
        for (String token : tokens) {
            final int equalsSign = token.indexOf(PARAMETER_NAME_VALUE_SEPARATOR.character());
            if (-1 == equalsSign) {
                throw new IllegalArgumentException("Parameter value missing " + CharSequences.quoteIfChars(PARAMETER_NAME_VALUE_SEPARATOR) +
                        " from parameter " + CharSequences.quote(token) +
                        " in " + CharSequences.quote(text));
            }
            if (0 == equalsSign) {
                throw new IllegalArgumentException("Parameter name missing from parameter " + CharSequences.quote(token) +
                        " in " + CharSequences.quote(text));
            }
            parameters.put(HttpHeaderParameterName.with(token.substring(0, equalsSign)), token.substring(equalsSign + 1));
        }

        return new HttpHeaderToken(tokens[0], parameters);
    }

    /**
     * Factory that creates a new {@link HttpHeaderToken}
     */
    public static HttpHeaderToken with(final String value, final Map<HttpHeaderParameterName<?>, String> parameters) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "token value", HttpCharPredicates.rf2045Token());

        return new HttpHeaderToken(value, checkParameters(parameters));
    }

    /**
     * Private ctor use factory
     */
    private HttpHeaderToken(final String value, final Map<HttpHeaderParameterName<?>, String> parameters) {
        super();
        this.value = value;
        this.parameters = parameters;
    }

    // value.........................................................................................

    @Override
    public String value() {
        return this.value;
    }

    public HttpHeaderToken setValue(final String value) {
        checkValue(value);

        return this.value.equals(value) ?
                this :
                this.replace(value, this.parameters);
    }

    private final String value;

    private static void checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", HttpCharPredicates.rf2045Token());
    }

    // parameters.........................................................................................

    /**
     * A map view of all parameters to their text or string value.
     *
     * @return
     */
    public Map<HttpHeaderParameterName<?>, String> parameters() {
        return this.parameters;
    }

    public HttpHeaderToken setParameters(final Map<HttpHeaderParameterName<?>, String> parameters) {
        final Map<HttpHeaderParameterName<?>, String> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.value, copy);
    }

    private final Map<HttpHeaderParameterName<?>, String> parameters;

    private static Map<HttpHeaderParameterName<?>, String> checkParameters(final Map<HttpHeaderParameterName<?>, String> parameters) {
        final Map<HttpHeaderParameterName<?>, String> copy = Maps.ordered();
        copy.putAll(parameters);
        return copy;
    }

    // replace ...........................................................................................................

    private HttpHeaderToken replace(final String value, final Map<HttpHeaderParameterName<?>, String> parameters) {
        return new HttpHeaderToken(value, parameters);
    }

    // HasQFactorWeight................................................................................................

    @Override
    public Optional<Float> qFactorWeight() {
        return HttpHeaderParameterName.Q.parameterValue(this.parameters);
    }

    // Object

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpHeaderToken &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpHeaderToken other) {
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
