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
    final static char SEPARATOR = ',';

    final static CharPredicate RFC2045TOKEN = CharPredicates.rfc2045Token();
    final static CharPredicate RFC2045SPECIAL = CharPredicates.rfc2045TokenSpecial();

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
        return this.text.charAt(this.position);
    }

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
    final void whitespace() {
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

    // fail .......................................................................................

    /**
     * Reports an invalid character within the unparsed text.
     */
    final void failInvalidCharacter() {
        final InvalidCharacterException cause = new InvalidCharacterException(this.text, this.position);
        throw new HeaderValueException(cause.getMessage(), cause);
    }

    /**
     * Reports a missing closing quote.
     */
    static String missingClosingQuote(final String text) {
        return "Missing closing '\"' in " + CharSequences.quoteAndEscape(text);
    }

    final void failMissingParameterValue() {
        fail(missingParameterValue(this.position, this.text));
    }

    static String missingParameterValue(final int start, final String text) {
        return emptyToken("parameter value", start, text);
    }

    // error reporting.................................................................................................

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
