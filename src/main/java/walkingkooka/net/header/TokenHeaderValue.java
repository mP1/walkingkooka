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
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Holds a simple header value, and any accompanying parameters. Parameter values will be of the type
 * compatible with each parameter name.
 */
public final class TokenHeaderValue extends HeaderValueWithParameters2<TokenHeaderValue,
        TokenHeaderValueParameterName<?>,
        String>
        implements HasQFactorWeight {

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
        super(value, parameters);
    }

    // value.........................................................................................

    public TokenHeaderValue setValue(final String value) {
        checkValue(value);

        return this.value.equals(value) ?
                this :
                this.replace(value, this.parameters);
    }

    private static void checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", CharPredicates.rfc2045Token());
    }

    // replace ...........................................................................................................

    @Override
    TokenHeaderValue replace(final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        return this.replace(this.value, parameters);
    }

    private TokenHeaderValue replace(final String value,
                                     final Map<TokenHeaderValueParameterName<?>, Object> parameters) {
        return new TokenHeaderValue(value, parameters);
    }

    // HasQFactorWeight................................................................................................

    @Override
    public Optional<Float> qFactorWeight() {
        return this.qFactorWeight(TokenHeaderValueParameterName.Q);
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
    int hashCode0(final String value) {
        return CaseSensitivity.INSENSITIVE.hash(value);
    }

    @Override
    boolean equals1(final String value, final String otherValue) {
        return value.equalsIgnoreCase(otherValue);
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof TokenHeaderValue;
    }
}
