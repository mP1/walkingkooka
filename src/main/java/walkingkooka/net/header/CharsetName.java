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
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

/**
 * Holds a charset header value, with a property available to retrieve a JDK charset implementation if one exists.<br>
 * <a href="https://tools.ietf.org/html/rfc7231#section-3.1.1.2"></a>
 * <pre>
 * Charset
 *
 *    HTTP uses charset names to indicate or negotiate the character
 *    encoding scheme of a textual representation [RFC6365].  A charset is
 *    identified by a case-insensitive token.
 *
 *      charset = token
 *
 *    Charset names ought to be registered in the IANA "Character Sets"
 *    registry (<http://www.iana.org/assignments/character-sets>) according
 *    to the procedures defined in [RFC2978].
 * </pre>
 */
public final class CharsetName implements Name, HeaderValue, Value<String>, Comparable<CharsetName> {

    /**
     * Constant when there is no charset.
     */
    public final static Optional<Charset> NO_CHARSET = Optional.empty();

    /**
     * A special name that identifies a wildcard selection.
     */
    private final static String WILDCARD_VALUE = "*";

    /**
     * The {@link CharsetName} holding a wildcard.
     */
    public final static CharsetName WILDCARD = new CharsetName(WILDCARD_VALUE, NO_CHARSET);

    /**
     * Holds all constants.
     */
    private final static Map<String, CharsetName> CONSTANTS = registerConstants();

    /**
     * Creates constants from each of the available charsets.
     */
    private static Map<String, CharsetName> registerConstants() {
        final Map<String, CharsetName> constants = Maps.sorted(String.CASE_INSENSITIVE_ORDER);

        for(Charset charset : Charset.availableCharsets().values()) {
            final String name = charset.name();
            constants.put(name, new CharsetName(name, Optional.of(charset)));
        }

        constants.put(WILDCARD_VALUE, WILDCARD);

        return constants;
    }

    private static CharsetName constantOrFail(final String name) {
        final CharsetName charsetName = CONSTANTS.get(name);
        if(null==charsetName) {
            throw new NeverError("Standard charset " + CharSequences.quoteAndEscape(name) + " missing");
        }
        return charsetName;
    }

    /**
     * US-ASCII	Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
     */
    public final static CharsetName US_ASCII = constantOrFail("US-ASCII");

    /**
     * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     */
    public final static CharsetName ISO_8859_1 = constantOrFail("ISO-8859-1");

    /**
     * UTF-8	Eight-bit UCS Transformation Format
     */
    public final static CharsetName UTF_8 = constantOrFail("UTF-8");

    /**
     * Sixteen-bit UCS Transformation Format, big-endian byte order
     */
    public final static CharsetName UTF_16BE = constantOrFail("UTF-16BE");

    /**
     * Sixteen-bit UCS Transformation Format, little-endian byte order
     */
    public final static CharsetName UTF_16LE = constantOrFail("UTF-16LE");

    /**
     * Sixteen-bit UCS Transformation Format, byte order identified by an optional byte-order mark
     */
    public final static CharsetName UTF_16 = constantOrFail("UTF-16");

    /**
     * <pre>
     * Charsets are named by strings composed of the following characters:
     *
     * The uppercase letters 'A' through 'Z' ('\u0041' through '\u005a'),
     * The lowercase letters 'a' through 'z' ('\u0061' through '\u007a'),
     * The digits '0' through '9' ('\u0030' through '\u0039'),
     * The dash character '-' ('\u002d', HYPHEN-MINUS),
     * The plus character '+' ('\u002b', PLUS SIGN),
     * The period character '.' ('\u002e', FULL STOP),
     * The colon character ':' ('\u003a', COLON), and
     * The underscore character '_' ('\u005f', LOW LINE).
     * A charset name must begin with either a letter or a digit. The empty string is not a legal charset name. Charset names are not case-sensitive; that is, case is always ignored when comparing charset names. Charset names generally follow the conventions documented in RFC 2278: IANA Charset Registration Procedures.
     * </pre>
     */
    static final CharPredicate INITIAL_CHAR_PREDICATE = CharPredicates.builder()
            .range('A', 'Z')
            .range('a', 'z')
            .range('0', '9')
            .build()
            .setToString("Charset initial");
    static final CharPredicate PART_CHAR_PREDICATE = CharPredicates.builder()
            .range('A', 'Z')
            .range('a', 'z')
            .range('0', '9')
            .any("-+.:_")
            .build()
            .setToString("Charset part");

    /**
     * Returns a {@link CharsetName}
     */
    public static CharsetName with(final String value) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(value,
                "value",
                INITIAL_CHAR_PREDICATE,
                PART_CHAR_PREDICATE);

        final CharsetName mediaType = CONSTANTS.get(value);
        return null != mediaType ?
                mediaType :
                new CharsetName(value, NO_CHARSET);
    }

    /**
     * Private constructor use factory.
     */
    private CharsetName(final String value, final Optional<Charset> charset) {
        super();
        this.value = value.toLowerCase();
        this.charset = charset;
    }

    /**
     * Will be empty if the charset is not supported by this JVM.
     */
    public Optional<Charset> charset() {
        return this.charset;
    }

    private final Optional<Charset> charset;

    // Value ..........................................................................................................

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    /**
     * Returns true if the name is a wildcard selection.
     */
    public boolean isWildcard() {
        return WILDCARD == this;
    }

    // HeaderValue ....................................................................................................

    @Override
    public String headerValue() {
        return this.value();
    }

    // Comparable...........................................................................................................

    @Override
    public int compareTo(final CharsetName other) {
        return this.value.compareToIgnoreCase(other.value);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof CharsetName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final CharsetName other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
