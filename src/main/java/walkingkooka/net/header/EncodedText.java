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
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Objects;
import java.util.Optional;

/**
 * Holds the parsed result of a RFC5987 token.
 * <a href="https://tools.ietf.org/html/rfc5987"></a>
 * <pre>
 *  In order to include character set and language information, this
 *    specification modifies the RFC 2616 grammar to be:
 *
 *      parameter     = reg-parameter / ext-parameter
 *
 *      reg-parameter = parmname LWSP "=" LWSP value
 *
 *      ext-parameter = parmname "*" LWSP "=" LWSP ext-value
 *
 *      parmname      = 1*attr-char
 *
 *      ext-value     = charset  "'" [ language ] "'" value-chars
 *                    ; like RFC 2231's <extended-initial-value>
 *                    ; (see [RFC2231], Section 7)
 *
 *      charset       = "UTF-8" / "ISO-8859-1" / mime-charset
 *
 *      mime-charset  = 1*mime-charsetc
 *      mime-charsetc = ALPHA / DIGIT
 *                    / "!" / "#" / "$" / "%" / "&"
 *                    / "+" / "-" / "^" / "_" / "`"
 *                    / "{" / "}" / "~"
 *                    ; as <mime-charset> in Section 2.3 of [RFC2978]
 *                    ; except that the single quote is not included
 *                    ; SHOULD be registered in the IANA charset registry
 *
 *      language      = <Language-Tag, defined in [RFC5646], Section 2.1>
 *
 *      value-chars   = *( pct-encoded / attr-char )
 *
 *      pct-encoded   = "%" HEXDIG HEXDIG
 *                    ; see [RFC3986], Section 2.1
 *
 *      attr-char     = ALPHA / DIGIT
 *                    / "!" / "#" / "$" / "&" / "+" / "-" / "."
 *                    / "^" / "_" / "`" / "|" / "~"
 *                    ; token except ( "*" / "'" / "%" )
 * </pre>
 */
public final class EncodedText implements HashCodeEqualsDefined, Value<String>, HeaderValue {

    /**
     * The value part of an extended parameter (ext-value) is a token that
     * consists of three parts: the REQUIRED character set name (charset),
     * the OPTIONAL language information (language), and a character
     * sequence representing the actual value (value-chars), separated by
     * single quote characters.  Note that both character set names and
     * language tags are restricted to the US-ASCII character set, and are
     * matched case-insensitively (see [RFC2978], Section 2.3 and [RFC5646],
     * Section 2.1.1).
     */
    final static Optional<LanguageTagName> NO_LANGUAGE = Optional.empty();

    /**
     * Factory that creates a new {@link EncodedText}
     */
    public static EncodedText with(final CharsetName charset,
                            final Optional<LanguageTagName> language,
                            final String value) {
        Objects.requireNonNull(charset, "charset");
        if(charset.isWildcard()) {
            throw new IllegalArgumentException("Encoded text charset must not be a wildcard");
        }

        Objects.requireNonNull(language, "language");
        Objects.requireNonNull(value, "value");

        return new EncodedText(charset, language, value);
    }

    /**
     * Private ctor
     */
    private EncodedText(final CharsetName charset,
                        final Optional<LanguageTagName> language,
                        final String value) {
        this.charset = charset;
        this.language = language;
        this.value = value;
    }

    CharsetName charset() {
        return this.charset;
    }

    private final CharsetName charset;

    Optional<LanguageTagName> language() {
        return this.language;
    }

    private String languageHeaderText() {
        return this.language.isPresent() ?
                this.language.get().toHeaderText() :
                "";
    }

    private final Optional<LanguageTagName> language;

    // Value ............................................................................................

    @Override
    public String value() {
        return value;
    }

    private final String value;

    // HeaderValue ............................................................................................

    /**
     * Returns the header text as would appear in a header prior to parsing.
     * <pre>
     * value-chars   = *( pct-encoded / attr-char )
     *
     * pct-encoded   = "%" HEXDIG HEXDIG
     *               ; see [RFC3986], Section 2.1
     *
     * attr-char     = ALPHA / DIGIT
     *               / "!" / "#" / "$" / "&" / "+" / "-" / "."
     *               / "^" / "_" / "`" / "|" / "~"
     *               ; token except ( "*" / "'" / "%" )
     * </pre>
     */
    @Override
    public String toHeaderText() {
        final CharsetName charset = this.charset;
        final StringBuilder encodedText = new StringBuilder();

        for (byte c : this.value.getBytes(charset.charset().get())) {
            switch (c) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '!':
                case '#':
                case '$':
                case '&':
                case '+':
                case '-':
                case '.':
                case '^':
                case '_':
                case '`':
                case '|':
                case '~':
                    encodedText.append((char) c);
                    break;
                default:
                    encodedText.append(ENCODE)
                            .append(hex(c >> 4))
                            .append(hex(c));
            }
        }

        return charset.value() + '\'' + this.languageHeaderText() + '\'' + encodedText;
    }

    private static char hex(final int c) {
        return HEXDIGITS[c & 0xf];
    }

    private final static char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    static final char ENCODE = '%';

    @Override
    public boolean isWildcard() {
        return false;
    }

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

    // Object ............................................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof EncodedText &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final EncodedText other) {
        return this.charset.equals(other.charset) &&
                this.language.equals(other.language) &&
                this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.charset)
                .value(this.language)
                .value(this.value)
                .build();
    }
}
