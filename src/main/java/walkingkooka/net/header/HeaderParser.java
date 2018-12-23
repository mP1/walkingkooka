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

import java.util.function.Function;

/**
 * Base parser for any parser in this package.
 */
abstract class HeaderParser {

    /**
     * Package private to limit sub classing.
     */
    HeaderParser(final String text) {
        CharSequences.failIfNullOrEmpty(text, "Text");
        this.text = text;
        this.position = 0;
    }

    final static char BACKSLASH = '\\';
    final static char DOUBLE_QUOTE = '"';
    final static char TOKEN_SEPARATOR = ';';
    final static char MULTIVALUE_SEPARATOR = ',';
    final static char KEYVALUE_SEPARATOR = '=';
    final static char WILDCARD = '*';
    final static char SLASH = '/';

    final static CharPredicate RFC2045TOKEN = CharPredicates.rfc2045Token();
    final static CharPredicate RFC2045SPECIAL = CharPredicates.rfc2045TokenSpecial();

    // tokenizer ..............................................................................

    /**
     * Consumes all the text firing events for each of the symbols or tokens encountered.
     */
    final void parse() {
        while(this.hasMoreCharacters()) {
            final char c = this.character();
            switch(this.character()) {
                case '\t':
                case '\r':
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

    abstract void token();

    abstract void endOfText();

    /**
     * Uses the predicate to match characters, and then passes that text providing its not empty to the factory.
     */
    final <T> T token(final CharPredicate predicate,
                      final Function<String, T> factory) {
        final int start = this.position;
        final String tokenText = this.token(predicate);
        if(tokenText.isEmpty()) {
            this.failInvalidCharacter();
        }

        try {
            return factory.apply(tokenText);
        } catch (final InvalidCharacterException cause) {
            throw cause.setTextAndPosition(this.text, start + cause.position());
        }
    }

    // quoted ...............................................................................................

    final static boolean ALLOW_ESCAPING = true;
    final static boolean DISALLOW_ESCAPING = false;

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
    String quotedText(final CharPredicate predicate, final boolean supportEscaping) {
        final StringBuilder unescaped = new StringBuilder();
        int start = this.position;
        this.position++;

        boolean escaping = false;

        for(;;this.position++) {
            if(!this.hasMoreCharacters()) {
                fail(missingClosingQuote(this.text));
            }
            final char c = this.character();

            if(supportEscaping && escaping) {
                unescaped.append(c);
                escaping = false;
                continue;
            }

            if(supportEscaping && BACKSLASH == c) {
                escaping = true;
                continue;
            }

            if(DOUBLE_QUOTE == c) {
                this.position++;
                break;
            }
            if(!predicate.test(c)){
                this.failInvalidCharacter();
            }
            unescaped.append(c);
        }

        //return unescaped.toString();
        return this.text.substring(start, this.position);
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
        if(!ASCII.test(c)) {
            this.failInvalidCharacter();
        }
        return c;
    }

    /**
     * Used to match valid ascii characters.
     */
    private final static CharPredicate ASCII = CharPredicates.asciiPrintable()
            .or(CharPredicates.any("\t\r\n "));

    /**
     * Consumes the token text with characters matched by the given {@link CharPredicate}.
     */
    final String token(final CharPredicate predicate) {
        final int start = this.position;

        while(this.hasMoreCharacters()) {
            if(!predicate.test(this.character())) {
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
        while(this.hasMoreCharacters()){
            final char c = this.character();
            if('\t' == c || ' ' == c) {
                this.position++;
                continue;
            }

            // expect CR NL and then HS or SP.
            if('\r' ==c) {
                this.position++;

                if(!this.hasMoreCharacters()) {
                    this.position--;
                    this.failInvalidCharacter();
                }
                if('\n' != this.character()) {
                    this.failInvalidCharacter();
                }
                this.position++;

                if(!this.hasMoreCharacters()) {
                    this.position--;
                    this.failInvalidCharacter();
                }
                if(spaceOrHorizontalTab(this.character())) {
                    this.position++;
                    continue;
                }

                this.failInvalidCharacter();
            }

            // exit end of whitespace
            break;
        }
    }

    private static boolean spaceOrHorizontalTab(final char c) {
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
