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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * An individual encoding belonging to a {@link AcceptEncoding}.
 */
public abstract class Encoding extends HeaderValueWithParameters2<Encoding,
        EncodingParameterName<?>,
        String>
        implements Comparable<Encoding>,
        HasQFactorWeight,
        Predicate<ContentEncoding> {

    /**
     * A map holding no parameters.
     */
    public final static Map<EncodingParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * {@see CaseSensitivity}
     */
    final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Holds all constants.
     */
    final static Map<String, EncodingNonWildcard> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    /**
     * Holds a {@link Encoding} for br.
     */
    public final static Encoding BR = registerConstant("br");

    /**
     * Holds a {@link Encoding} for deflate
     */
    public final static Encoding COMPRESS = registerConstant("compress");

    /**
     * Holds a {@link Encoding} for deflate.
     */
    public final static Encoding DEFLATE = registerConstant("deflate");

    /**
     * Holds a {@link Encoding} for gzip.
     */
    public final static Encoding GZIP = registerConstant("gzip");

    /**
     * Holds a {@link Encoding} for identity.
     */
    public final static Encoding IDENTITY = registerConstant("identity");

    /**
     * Holds a {@link Encoding wildcard}
     */
    public final static Encoding WILDCARD_ENCODING = EncodingWildcard.with(NO_PARAMETERS);

    /**
     * Creates and then registers the constant.
     */
    static private Encoding registerConstant(final String text) {
        final EncodingNonWildcard contentType = nonWildcard(text, NO_PARAMETERS);
        CONSTANTS.put(text, contentType);
        return contentType;
    }

    /**
     * Parses text into a {@link Encoding}.
     */
    public static Encoding parse(final String text) {
        return EncodingHeaderValueParser.parseEncoding(text);
    }

    /**
     * {@see Encoding}
     */
    public static Encoding with(final String value) {
        return "*".equals(value) ?
                wildcard(NO_PARAMETERS) :
                nonWildcard(checkValue(value), NO_PARAMETERS);
    }

    private static String checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value,
                "value",
                AcceptEncodingHeaderValueParser.RFC2045TOKEN);
        return value;
    }

    /**
     * {@see EncodingNonWildcard}
     */
    static EncodingNonWildcard nonWildcard(final String value,
                                           final Map<EncodingParameterName<?>, Object> parameters) {
        return EncodingNonWildcard.with(value, checkParameters(parameters));
    }

    /**
     * {@see EncodingWildcard}
     */
    static EncodingWildcard wildcard(final Map<EncodingParameterName<?>, Object> parameters) {
        return EncodingWildcard.with(checkParameters(parameters));
    }

    /**
     * Package private to limit sub classing.
     */
    Encoding(final String value,
             final Map<EncodingParameterName<?>, Object> parameters) {
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

    // HasQFactorWeight................................................................................................

    @Override
    public Optional<Float> qFactorWeight() {
        return EncodingParameterName.Q_FACTOR.parameterValue(this);
    }

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
    public final int compareTo(final Encoding other) {
        return CASE_SENSITIVITY.comparator().compare(this.value, other.value);
    }
}
