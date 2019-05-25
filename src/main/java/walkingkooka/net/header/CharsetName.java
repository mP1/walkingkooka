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

import walkingkooka.NeverError;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
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
public abstract class CharsetName extends HeaderNameValue
        implements HeaderValue,
        Comparable<CharsetName> {

    /**
     * Constant when there is no charset.
     */
    public final static Optional<Charset> NO_CHARSET = Optional.empty();

    /**
     * The {@link CharsetName} holding a wildcard.
     */
    public final static CharsetName WILDCARD_CHARSET = CharsetNameWildcard.INSTANCE;

    /**
     * {@link CaseSensitivity} for charset names.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Holds all constants.
     */
    private final static Map<String, CharsetName> CONSTANTS = registerConstants();

    /**
     * Creates constants from each of the available charsets.
     */
    private static Map<String, CharsetName> registerConstants() {
        final Map<String, CharsetName> constants = Maps.sorted(CASE_SENSITIVITY.comparator());

        for (Charset charset : Charset.availableCharsets().values()) {
            final String name = charset.name();
            constants.put(name, CharsetNameSupportedCharset.with(name, charset));

            for (String alias : charset.aliases()) {
                constants.put(alias, CharsetNameSupportedCharset.with(alias, charset));
            }
        }

        constants.put(WILDCARD.string(), WILDCARD_CHARSET);

        return constants;
    }

    private static CharsetName constantOrFail(final String name) {
        final CharsetName charsetName = CONSTANTS.get(name);
        if (null == charsetName) {
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
                CharsetNameUnsupportedCharset.unsupportedCharset(value);
    }

    /**
     * Package private to limit sub classing, use a constant or factory.
     */
    CharsetName(final String name) {
        super(name);
    }

    /**
     * Will be empty if the charset is not supported by this JVM.
     */
    public abstract Optional<Charset> charset();

    /**
     * Returns true if this charset name is a wildcard.
     */
    public abstract boolean isWildcard();

    /**
     * Tests if this {@link CharsetName} is a match for the given, including wildcard matches.
     * Typically all values of {@link HttpHeaderName#ACCEPT_CHARSET} are called until a match
     * against a content-type {@link CharsetName}.
     */
    public final boolean matches(final CharsetName contentType) {
        Objects.requireNonNull(contentType, "contentType");

        return contentType.matches0(this);
    }

    /**
     * Sub classes must include the following statement which will result in a double dispatch.
     * <pre>
     * possible.matches1(this)
     * </pre>
     */
    abstract boolean matches0(final CharsetName possible);

    abstract boolean matches1(final CharsetNameSupportedCharset contentType);

    abstract boolean matches1(final CharsetNameUnsupportedCharset contentType);

    final boolean matches1(final CharsetNameWildcard contentType) {
        return NeverError.unhandledCase(contentType);
    }

    // setParameters.......................................................................................

    /**
     * Factory that creates a {@link CharsetHeaderValue} combining this charset and the given parameters.
     */
    public final CharsetHeaderValue setParameters(final Map<CharsetHeaderValueParameterName<?>, Object> parameters) {
        return CharsetHeaderValue.with(this).setParameters(parameters);
    }

    // HeaderValue ....................................................................................................

    @Override
    public final String toHeaderText() {
        return this.value();
    }

    // HasHeaderScope ....................................................................................................

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
        return true;
    }

    // Comparable...........................................................................................................

    @Override
    public final int compareTo(final CharsetName other) {
        return this.compareTo0(other);
    }

    // HeaderNameValue..............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof CharsetName;
    }

    @Override
    public String toString() {
        return this.value();
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }
}
