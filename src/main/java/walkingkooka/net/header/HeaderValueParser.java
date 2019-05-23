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

import walkingkooka.InvalidCharacterException;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Base parser for any parser in this package.
 */
abstract class HeaderValueParser {

    /**
     * Package private to limit sub classing.
     */
    HeaderValueParser(final String text) {
        //CharSequences.failIfNullOrEmpty(text, "Text");
        Objects.requireNonNull(text, "Text");
        if (text.isEmpty()) {
            throw new HeaderValueException("Text is empty");
        }

        this.text = text;
        this.position = 0;
    }

    private final static char BACKSLASH = '\\';
    private final static char DOUBLE_QUOTE = '"';
    private final static char TOKEN_SEPARATOR = ';';
    private final static char MULTIVALUE_SEPARATOR = ',';
    private final static char KEYVALUE_SEPARATOR = '=';
    final static char WILDCARD = '*';
    private final static char SLASH = '/';

    final static CharPredicate RFC2045TOKEN = CharPredicates.rfc2045Token();
    final static CharPredicate RFC2045SPECIAL = CharPredicates.rfc2045TokenSpecial();

    // tokenizer ..............................................................................

    /**
     * Consumes all the text firing events for each of the symbols or tokens encountered.
     */
    final void parse() {
        while (this.hasMoreCharacters()) {
            switch (this.character()) {
                case '\t':
                case '\r':
                case '\n':
                case ' ':
                    this.whitespace();
                    break;
                case TOKEN_SEPARATOR:
                    this.tokenSeparator();
                    this.position++;
                    break;

                case KEYVALUE_SEPARATOR:
                    this.keyValueSeparator();
                    this.position++;
                    break;

                case MULTIVALUE_SEPARATOR:
                    this.multiValueSeparator();
                    this.position++;
                    break;

                case WILDCARD:
                    this.wildcard();
                    break;

                case SLASH:
                    this.slash();
                    this.position++;
                    break;

                case DOUBLE_QUOTE:
                    this.quotedText();
                    break;

                case HeaderComments.BEGIN:
                    this.comment();
                    break;

                default:
                    this.token();
                    break;
            }
        }
        this.endOfText();
    }

    abstract void whitespace();

    abstract void tokenSeparator();

    abstract void keyValueSeparator();

    abstract void multiValueSeparator();

    abstract void wildcard();

    abstract void slash();

    abstract void quotedText();

    abstract void comment();

    abstract void token();

    abstract void endOfText();

    /**
     * Uses the predicate to match characters, and then passes that text providing its not empty to the factory.
     */
    private <T> Optional<T> tokenOptional(final CharPredicate predicate,
                                          final Function<String, T> factory) {
        final int start = this.position;
        final String tokenText = this.token(predicate);

        return tokenText.isEmpty() ?
                Optional.empty() :
                Optional.of(this.token0(tokenText, factory, start));
    }

    /**
     * Uses the predicate to match characters, and then passes that text providing its not empty to the factory.
     */
    final <T> T token(final CharPredicate predicate,
                      final Function<String, T> factory) {
        return this.token(predicate, false, factory);
    }

    /**
     * Uses the predicate to match characters and possibly a trailing star.
     */
    final <N extends HeaderParameterName<?>> N parameterName(final CharPredicate predicate,
                                                             final Function<String, N> factory) {
        return this.token(predicate, true, factory);
    }

    /**
     * Uses the predicate to match characters, and then passes that text providing its not empty to the factory.
     */
    private <T> T token(final CharPredicate predicate,
                        final boolean star,
                        final Function<String, T> factory) {
        final int start = this.position;
        String tokenText = this.token(predicate);
        if (tokenText.isEmpty()) {
            this.failInvalidCharacter();
        }
        if (star && this.hasMoreCharacters()) {
            if (HeaderParameterName.STAR.character() == this.character()) {
                tokenText = tokenText.concat(HeaderParameterName.STAR.string());
                this.position++;
            }
        }

        return this.token0(tokenText, factory, start);
    }

    private <T> T token0(final String tokenText,
                         final Function<String, T> factory,
                         final int start) {
        try {
            return factory.apply(tokenText);
        } catch (final InvalidCharacterException cause) {
            throw cause.setTextAndPosition(this.text, start + cause.position());
        }
    }

    // quoted ...............................................................................................

    final static boolean ESCAPING_SUPPORTED = true;
    final static boolean ESCAPING_UNSUPPORTED = false;

    /**
     * Returns the quoted string in its raw form which will include the surrounding double quotes.
     *
     * <a href="https://tools.ietf.org/html/rfc2616#section-3.11"></a>
     * <pre>
     * A string of text is parsed as a single word if it is quoted using
     * double-quote marks.
     *
     *      quoted-string  = ( <"> *(qdtext | quoted-pair ) <"> )
     *      qdtext         = <any TEXT except <">>
     *
     * The backslash character ("\") MAY be used as a single-character
     * quoting mechanism only within quoted-string and comment constructs.
     *
     *      quoted-pair    = "\" CHAR
     * </pre>
     */
    final String quotedText(final CharPredicate predicate, final boolean supportEscaping) {
        final StringBuilder unescaped = new StringBuilder();
        int start = this.position;
        this.position++;

        boolean escaping = false;

        for (; ; this.position++) {
            if (!this.hasMoreCharacters()) {
                fail(missingClosingQuote(this.text));
            }
            final char c = this.character();

            if (supportEscaping && escaping) {
                unescaped.append(c);
                escaping = false;
                continue;
            }

            if (supportEscaping && BACKSLASH == c) {
                escaping = true;
                continue;
            }

            if (DOUBLE_QUOTE == c) {
                this.position++;
                break;
            }
            if (!predicate.test(c)) {
                this.failInvalidCharacter();
            }
            unescaped.append(c);
        }

        return this.text.substring(start, this.position);
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc5987"></a>
     */
    final EncodedText encodedText() {
        final CharsetName charset = this.token(MIME_CHARSETC, CharsetName::with);
        if (!this.hasMoreCharacters()) {
            this.incompleteEncodedAsciiText();
        }
        this.languageQuoteCharacter();

        final Optional<LanguageTagName> languageTagName = this.tokenOptional(MIME_CHARSETC,
                LanguageTagName::with);

        this.languageQuoteCharacter();

        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        while (this.hasMoreCharacters()) {
            final char c = this.character();
            switch (c) {
                case EncodedText.ENCODE:
                    this.position++;
                    if (!this.hasMoreCharacters()) {
                        this.incompleteEncodedAsciiText();
                    }
                    final int hi = digit(this.character());
                    this.position++;

                    if (!this.hasMoreCharacters()) {
                        this.incompleteEncodedAsciiText();
                    }
                    final int lo = digit(this.character()); // below will advance position

                    bytes.write((hi << 4) + lo);
                    break;
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
                    bytes.write(c);
                    break;
                default:
                    this.incompleteEncodedAsciiText();
            }
            this.position++;
        }

        return EncodedText.with(charset,
                languageTagName,
                new String(bytes.toByteArray(), charset.charset().get()));
    }

    private void languageQuoteCharacter() {
        if (!this.hasMoreCharacters()) {
            this.incompleteEncodedAsciiText();
        }
        if (LANGUAGE_QUOTE != this.character()) {
            this.failInvalidCharacter();
        }
        this.position++;
    }

    private final static char LANGUAGE_QUOTE = '\'';

    /**
     * <a href="https://tools.ietf.org/html/rfc5987"></a>
     * <pre>
     *  mime-charsetc = ALPHA / DIGIT
     *                    / "!" / "#" / "$" / "%" / "&"
     *                    / "+" / "-" / "^" / "_" / "`"
     *                    / "{" / "}" / "~"
     *                    ; as <mime-charset> in Section 2.3 of [RFC2978]
     *                    ; except that the single quote is not included
     *                    ; SHOULD be registered in the IANA charset registry
     * </pre>
     */
    private final static CharPredicate MIME_CHARSETC = CharPredicates.builder()
            .range('A', 'Z')
            .range('a', 'z')
            .range('0', '9')
            .any("!#$%&+-^_`{}~")
            .build()
            .setToString("MIME CHARSETC");

    private static int digit(final char c) {
        return Character.digit(c, 16);
    }

    /**
     * Reports an invalid non ascii encoded text.
     */
    private void incompleteEncodedAsciiText() {
        throw new InvalidEncodedTextHeaderException("Invalid encoded text " + CharSequences.quoteAndEscape(this.text) + " at " + this.position);
    }

    /**
     * Assumes currently positioned at the start of a comment and consumes the entire comment including the closing right parenthesis.
     * The returned text does not include the surround parens.
     * <a href="https://tools.ietf.org/html/rfc7230#section-3.2.6">Field Value Comments</a>
     * <br>
     * <pre>
     * 3.2.6.  Field Value Components
     *
     *    Most HTTP header field values are defined using common syntax
     *    components (token, quoted-string, and comment) separated by
     *    whitespace or specific delimiting characters.  Delimiters are chosen
     *    from the set of US-ASCII visual characters not allowed in a token
     *    (DQUOTE and "(),/:;<=>?@[\]{}").
     *
     *      token          = 1*tchar
     *
     *      tchar          = "!" / "#" / "$" / "%" / "&" / "'" / "*"
     *                     / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
     *                     / DIGIT / ALPHA
     *                     ; any VCHAR, except delimiters
     *
     *    A string of text is parsed as a single value if it is quoted using
     *    double-quote marks.
     *
     *      quoted-string  = DQUOTE *( qdtext / quoted-pair ) DQUOTE
     *      qdtext         = HTAB / SP /%x21 / %x23-5B / %x5D-7E / obs-text
     *      obs-text       = %x80-FF
     *
     *    Comments can be included in some HTTP header fields by surrounding
     *    the comment text with parentheses.  Comments are only allowed in
     *    fields containing "comment" as part of their field value definition.
     *
     *      comment        = "(" *( ctext / quoted-pair / comment ) ")"
     *      ctext          = HTAB / SP / %x21-27 / %x2A-5B / %x5D-7E / obs-text
     *
     *    The backslash octet ("\") can be used as a single-octet quoting
     *    mechanism within quoted-string and comment constructs.  Recipients
     *    that process the value of a quoted-string MUST handle a quoted-pair
     *    as if it were replaced by the octet following the backslash.
     *
     *      quoted-pair    = "\" ( HTAB / SP / VCHAR / obs-text )
     *
     *    A sender SHOULD NOT generate a quoted-pair in a quoted-string except
     *    where necessary to quote DQUOTE and backslash octets occurring within
     *    that string.  A sender SHOULD NOT generate a quoted-pair in a comment
     *    except where necessary to quote parentheses ["(" and ")"] and
     *    backslash octets occurring within that comment.
     * </pre>
     */
    final void commentText() {
        this.position = HeaderComments.COMMENT_TEXT.consume(this.text, this.position);
    }

    // helpers ..................................................................................

    /**
     * Tests if there is at least one more character.
     */
    final boolean hasMoreCharacters() {
        return this.position < this.text.length();
    }

    /**
     * Retrieves the current character.
     */
    final char character() {
        final char c = this.text.charAt(this.position);
        if (!ASCII.test(c)) {
            this.failInvalidCharacter();
        }
        return c;
    }

    /**
     * Used to match valid ascii characters.
     */
    final static CharPredicate ASCII = CharPredicates.asciiPrintable()
            .or(CharPredicates.any("\t\r\n "));

    /**
     * Consumes the token text with characters matched by the given {@link CharPredicate}.
     */
    final String token(final CharPredicate predicate) {
        final int start = this.position;

        while (this.hasMoreCharacters()) {
            if (!predicate.test(this.character())) {
                break;
            }
            this.position++;
        }

        return this.text.substring(start, this.position);
    }

    // whitespace.................................................................................................

    /**
     * Consumes any optional whitespace, including support for CRNLSP<br>
     * <a href="https://en.wikipedia.org/wiki/Augmented_Backus%E2%80%93Naur_form"></a>
     * <pre>
     * HS | SP
     * </pre>
     * or the new line continuation combination.
     * <pre>
     * CR, NL, HS | SP
     * </pre>
     */
    final void whitespace0() {
        while (this.hasMoreCharacters()) {
            final char c = this.character();
            if ('\t' == c || ' ' == c) {
                this.position++;
                continue;
            }

            // expect CR NL and then HS or SP.
            if ('\r' == c) {
                this.position++;

                if (!this.hasMoreCharacters()) {
                    this.position--;
                    this.failInvalidCharacter();
                }
                if ('\n' != this.character()) {
                    this.failInvalidCharacter();
                }
                this.position++;

                if (!this.hasMoreCharacters()) {
                    this.position--;
                    this.failInvalidCharacter();
                }
                if (spaceOrHorizontalTab(this.character())) {
                    this.position++;
                    continue;
                }

                this.failInvalidCharacter();
            }

            // exit end of whitespace
            break;
        }
    }

    /**
     * Returns true if the character is a space or tab.
     */
    static boolean spaceOrHorizontalTab(final char c) {
        return ' ' == c || '\t' == c;
    }

    // error reporting.................................................................................................

    /**
     * Reports an invalid character within the unparsed text.
     */
    final <T> T failInvalidCharacter() {
        final InvalidCharacterException cause = new InvalidCharacterException(this.text, this.position);
        throw new HeaderValueException(cause.getMessage(), cause);
    }

    abstract void missingValue();

    final void failMissingValue(final String label) {
        fail(emptyToken(label, this.position, this.text));
    }

    /**
     * Reports a missing closing quote.
     */
    static String missingClosingQuote(final String text) {
        return "Missing closing '\"' in " + CharSequences.quoteAndEscape(text);
    }

    final void failMissingParameterName() {
        fail(missingParameterName(this.position, this.text));
    }

    static String missingParameterName(final int start, final String text) {
        return emptyToken("parameter name", start, text);
    }

    final void failMissingParameterValue() {
        fail(missingParameterValue(this.position, this.text));
    }

    static String missingParameterValue(final int start, final String text) {
        return emptyToken("parameter value", start, text);
    }

    /**
     * Reports a missing right parents (the closing comment).
     */
    static String missingClosingParens(final String text) {
        return "Missing closing ')' in " + CharSequences.quoteAndEscape(text);
    }

    /**
     * Reports an empty token.
     */
    final void failEmptyToken(final String token) {
        fail(emptyToken(token, this.position, this.text));
    }

    /**
     * The message when a token is empty.
     */
    static String emptyToken(final String token, final int i, final String text) {
        return "Missing " + token + " at " + i + " in " + CharSequences.quoteAndEscape(text);
    }

    /**
     * Reports a failure.
     */
    static <T> T fail(final String message) {
        throw new HeaderValueException(message);
    }

    /**
     * The text being parsed.
     */
    final String text;

    /**
     * The position of the current character being parsed.
     */
    int position;

    @Override
    public final String toString() {
        return this.position + " in " + CharSequences.quoteAndEscape(this.text);
    }
}
