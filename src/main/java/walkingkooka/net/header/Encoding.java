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
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;

/**
 * An individual encoding belonging to a {@link ContentEncoding}.
 */
public final class Encoding extends HeaderValue2<String>
        implements Comparable<Encoding> {

    /**
     * {@see CaseSensitivity}
     */
    final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Holds all constants.
     */
    final static Map<String, Encoding> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

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
     * Creates and then registers the constant.
     */
    static private Encoding registerConstant(final String text) {
        final Encoding contentType = new Encoding(text);
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
        Objects.requireNonNull(value, "value");

        final Encoding encoding = CONSTANTS.get(value);
        return null != encoding ?
                encoding :
                new Encoding(checkValue(value));
    }

    private static String checkValue(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value,
                "value",
                EncodingHeaderValueParser.RFC2045TOKEN);
        return value;
    }

    /**
     * Private use factory.
     */
    Encoding(final String value) {
        super(value);
    }

    @Override
    public String toHeaderText() {
        return this.value;
    }

    // isXXX...........................................................................................................

    /**
     * Content-Encoding encodings cannot include wildcards.
     */
    @Override
    public boolean isWildcard() {
        return false;
    }

    // HasHeaderScope ..................................................................................................

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return false;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Encoding;
    }

    // Comparable......................................................................................................

    @Override
    public final int compareTo(final Encoding other) {
        return CASE_SENSITIVITY.comparator().compare(this.value, other.value);
    }
}