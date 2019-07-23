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
public abstract class EncodingWithParameters extends HeaderValueWithParameters2<EncodingWithParameters,
        EncodingParameterName<?>,
        String>
        implements Comparable<EncodingWithParameters>,
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
    final static Map<String, EncodingWithParametersNonWildcard> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    /**
     * Holds a {@link EncodingWithParameters} for br.
     */
    public final static EncodingWithParameters BR = registerConstant("br");

    /**
     * Holds a {@link EncodingWithParameters} for deflate
     */
    public final static EncodingWithParameters COMPRESS = registerConstant("compress");

    /**
     * Holds a {@link EncodingWithParameters} for deflate.
     */
    public final static EncodingWithParameters DEFLATE = registerConstant("deflate");

    /**
     * Holds a {@link EncodingWithParameters} for gzip.
     */
    public final static EncodingWithParameters GZIP = registerConstant("gzip");

    /**
     * Holds a {@link EncodingWithParameters} for identity.
     */
    public final static EncodingWithParameters IDENTITY = registerConstant("identity");

    /**
     * Holds a {@link EncodingWithParameters wildcard}
     */
    public final static EncodingWithParameters WILDCARD_ENCODING = EncodingWithParametersWildcard.with(NO_PARAMETERS);

    /**
     * Creates and then registers the constant.
     */
    static private EncodingWithParameters registerConstant(final String text) {
        final EncodingWithParametersNonWildcard contentType = nonWildcard(text, NO_PARAMETERS);
        CONSTANTS.put(text, contentType);
        return contentType;
    }

    /**
     * Parses text into a {@link EncodingWithParameters}.
     */
    public static EncodingWithParameters parse(final String text) {
        return EncodingWithParametersHeaderValueParser.parseEncoding(text);
    }

    /**
     * {@see EncodingWithParameters}
     */
    public static EncodingWithParameters with(final String value) {
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
     * {@see EncodingWithParametersNonWildcard}
     */
    static EncodingWithParametersNonWildcard nonWildcard(final String value,
                                                         final Map<EncodingParameterName<?>, Object> parameters) {
        return EncodingWithParametersNonWildcard.with(value, checkParameters(parameters));
    }

    /**
     * {@see EncodingWithParametersWildcard}
     */
    static EncodingWithParametersWildcard wildcard(final Map<EncodingParameterName<?>, Object> parameters) {
        return EncodingWithParametersWildcard.with(checkParameters(parameters));
    }

    /**
     * Package private to limit sub classing.
     */
    EncodingWithParameters(final String value,
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
    public final int compareTo(final EncodingWithParameters other) {
        return CASE_SENSITIVITY.comparator().compare(this.value, other.value);
    }
}
