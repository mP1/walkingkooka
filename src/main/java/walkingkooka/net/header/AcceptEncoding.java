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
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * The accept encoding header.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Encoding"></a>
 */
public abstract class AcceptEncoding extends HeaderValueWithParameters2<AcceptEncoding,
        AcceptEncodingParameterName<?>,
        String>
        implements Comparable<AcceptEncoding>,
        Predicate<ContentEncoding> {

    /**
     * A map holding no parameters.
     */
    public final static Map<AcceptEncodingParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * {@see CaseSensitivity}
     */
    final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Holds all constants.
     */
    final static Map<String, AcceptEncodingNonWildcard> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    /**
     * Holds a {@link AcceptEncoding} for br.
     */
    public final static AcceptEncoding BR = registerConstant("br");

    /**
     * Holds a {@link AcceptEncoding} for deflate
     */
    public final static AcceptEncoding COMPRESS = registerConstant("compress");

    /**
     * Holds a {@link AcceptEncoding} for deflate.
     */
    public final static AcceptEncoding DEFLATE = registerConstant("deflate");

    /**
     * Holds a {@link AcceptEncoding} for gzip.
     */
    public final static AcceptEncoding GZIP = registerConstant("gzip");

    /**
     * Holds a {@link AcceptEncoding} for identity.
     */
    public final static AcceptEncoding IDENTITY = registerConstant("identity");

    /**
     * Holds a {@link AcceptEncoding wildcard}
     */
    public final static AcceptEncoding WILDCARD_ACCEPT_ENCODING = AcceptEncodingWildcard.with(NO_PARAMETERS);

    /**
     * Creates and then registers the constant.
     */
    static private AcceptEncoding registerConstant(final String text) {
        final AcceptEncodingNonWildcard contentType = nonWildcard(text, NO_PARAMETERS);
        CONSTANTS.put(text, contentType);
        return contentType;
    }

    /**
     * Parses text into a list of one or more {@link AcceptEncoding}
     */
    public static List<AcceptEncoding> parse(final String text) {
        return AcceptEncodingListHeaderValueParser.parseAcceptEncodingList(text);
    }

    /**
     * {@see AcceptEncoding}
     */
    static AcceptEncoding with(final String value) {
        return "*".equals(value) ?
                wildcard(NO_PARAMETERS) :
                nonWildcard(checkValue(value), NO_PARAMETERS);
    }

    private static String checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value,
                "value",
                AcceptEncodingListHeaderValueParser.RFC2045TOKEN);
        return value;
    }

    /**
     * {@see AcceptEncodingNonWildcard}
     */
    static AcceptEncodingNonWildcard nonWildcard(final String value,
                                                 final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        return AcceptEncodingNonWildcard.with(value, checkParameters(parameters));
    }

    /**
     * {@see AcceptEncodingWildcard}
     */
    static AcceptEncodingWildcard wildcard(final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        return AcceptEncodingWildcard.with(checkParameters(parameters));
    }

    /**
     * Package private to limit sub classing.
     */
    AcceptEncoding(final String value,
                   final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        super(value, parameters);
    }

    @Override
    final String toHeaderTextValue() {
        return this.value;
    }

    @Override
    final String toHeaderTextParameterSeparator() {
        return TO_HEADERTEXT_PARAMETER_SEPARATOR;
    }

    // isXXX...........................................................................................................

    /**
     * Returns true if this accept-encoding is a wildcard.
     */
    public abstract boolean isWildcard();

    // HasHeaderScope .................................................................................................

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
        return false;
    }

    // Predicate .....................................................................................................

    @Override
    public boolean test(final ContentEncoding contentEncoding) {
        Objects.requireNonNull(contentEncoding, "contentEncoding");

        return this.test0(contentEncoding);
    }

    abstract boolean test0(final ContentEncoding contentEncoding);

    // Object..........................................................................................................

    @Override
    final int hashCode0(final String value) {
        return CASE_SENSITIVITY.hash(value);
    }


    @Override
    final boolean equals1(final String value, final String otherValue) {
        return CASE_SENSITIVITY.equals(value, otherValue);
    }

    // Comparable......................................................................................................

    @Override
    public final int compareTo(final AcceptEncoding other) {
        return CASE_SENSITIVITY.comparator().compare(this.value, other.value);
    }
}
