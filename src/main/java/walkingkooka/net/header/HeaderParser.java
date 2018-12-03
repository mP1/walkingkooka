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

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.function.Function;

/**
 * A parser that translates tokens into abstract event methods.
 */
abstract class HeaderParser<N extends HeaderParameterName<?>> {

    static void checkText(final String text, final String label) {
        CharSequences.failIfNullOrEmpty(text, label);
    }

    final static char EQUALS_SIGN = '=';
    final static char SEPARATOR = ',';
    final static char PARAMETER_SEPARATOR = ';';
    final static String PARAMETER_NAME = "parameter name";
    final static String PARAMETER_VALUE = "parameter value";

    final static CharPredicate RFC2045TOKEN = CharPredicates.rfc2045Token();

    HeaderParser(final String text) {
        super();

        this.text = text;
        this.position = 0;
        this.mode = HeaderParserMode.VALUE;
    }

    final void parse() {
        final int length = this.text.length();
        while(this.position < length) {
            final char c = this.character();
            this.mode.accept(this);
        }
        this.mode.endOfText(this);
    }

    /**
     * Uses the given predicate to parse the value token. The only valid trailing character is {@link #WHITESPACE} or
     * {@link #PARAMETER_SEPARATOR} others will be reported as an invalid character.
     */
    final <V> V parseValue(final CharPredicate predicate,
                           final String tokenName,
                           final Function<String, V> factory) {
        final String value = this.tokenText(predicate);

        this.failNotIfWhitespaceOrParameterSeparatorOrSeparator();

        if(value.isEmpty()) {
            this.failEmptyToken(tokenName);
        }

        return factory.apply(value);
    }

    /**
     * Uses the given predicate to parse the {@link HeaderParameterName}. The only valid trailing character is {@link #WHITESPACE} or
     * {@link #EQUALS_SIGN} others will be reported as an invalid character.
     */
    final void parseParameterName(final CharPredicate predicate, final Function<String, N> factory) {
        final String parameterName = this.tokenText(predicate);

        if(this.hasMoreCharacters()) {
            for(;;) {
                final char c = this.character();
                if(c == EQUALS_SIGN) {
                    break;
                }
                if(WHITESPACE.test(c)) {
                    break;
                }
                this.failInvalidCharacter();
            }
        }

        if(parameterName.isEmpty()) {
            this.failEmptyToken(PARAMETER_NAME);
        }

        this.parameterName = factory.apply(parameterName);
    }

    /**
     * Uses the given predicate to parse the parameter value. The only valid trailing character is {@link #WHITESPACE} or
     * {@link #PARAMETER_SEPARATOR} or {@link #SEPARATOR} others will be reported as an invalid character.
     */
    final void parseParameterValue(final CharPredicate predicate) {
        final String parameterValue = this.tokenText(predicate);

        this.failNotIfWhitespaceOrParameterSeparatorOrSeparator();

        if(parameterValue.isEmpty()) {
            this.failEmptyToken(PARAMETER_VALUE);
        }

        this.addParameter(parameterValue);
    }

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
    final String tokenText(final CharPredicate predicate) {
        final int start = this.position;
        this.consume(predicate);
        return this.text.substring(start, this.position);
    }

    /**
     * Consumes all characters that match the given {@link CharPredicate}.
     */
    final void consume(final CharPredicate predicate) {
        while(this.hasMoreCharacters()) {
            if(!predicate.test(this.character())) {
                break;
            }
            this.position++;
        }
    }

    /**
     * The text being parsed.
     */
    final String text;

    /**
     * The position of the current character being parsed.
     */
    int position;

    /**
     * The current mode.
     */
    HeaderParserMode mode;

    /**
     * Sub classes must consumes the value portion of a header value.
     */
    abstract void value();

    /**
     * Sub classes must consume the parameter name.
     */
    abstract void parameterName();

    /**
     * Sub classes must consume the parameter value.
     */
    abstract void parameterValue();

    /**
     * Sub classes must handle any separator.
     */
    abstract void separator();

    /**
     * Sub classes must handle an empty parameter value.
     */
    abstract void missingParameterValue();

    /**
     * Sub classes must consume the finished token.
     */
    abstract void tokenEnd();

    /**
     * Aggregates parameters for the current token.
     */
    Map<N, Object> parameters;

    /**
     * The parameter name of the current parameter.
     */
    N parameterName;

    /**
     * Adds a new parameter to the parameters map.
     */
    final void addParameter(final String valueText) {
        this.parameters.put(this.parameterName, this.parameterName.toValue(valueText));
    }

    final void failNotIfWhitespaceOrParameterSeparatorOrSeparator() {
        while(this.hasMoreCharacters()) {
            final char c = this.character();
            if(WHITESPACE.test(c)){
                break;
            }
            if(PARAMETER_SEPARATOR == c){
                break;
            }
            if(SEPARATOR == c){
                this.separator();
                break;
            }
            this.failInvalidCharacter();
        }
    }

    // whitespace.................................................................................................

    /**
     * Consumes any optional whitespace and then updates the current mode to next.
     */
    final void consumeWhitespace(final HeaderParserMode next) {
        this.consume(WHITESPACE);
        this.mode = next;
    }

    private final static CharPredicate WHITESPACE = CharPredicates.any("\u0009\u0020")
            .setToString("SP|HTAB");

    // error reporting.................................................................................................

    /**
     * Reports an invalid character within the unparsed text.
     */
    final void failInvalidCharacter() {
        fail(invalidCharacter(this.position, this.text));
    }

    /**
     * Builds a message to report an invalid or unexpected character.
     */
    static String invalidCharacter(final int position, final String text) {
        return "Invalid character " + CharSequences.quoteIfChars(text.charAt(position)) + " at " + position + " in " + CharSequences.quoteAndEscape(text);
    }

    final void failEmptyParameterValue() {
        failEmptyToken("parameter value");
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

    static void fail(final String message) {
        throw new HeaderValueException(message);
    }

    @Override
    public final String toString() {
        return this.position + " in " + CharSequences.quoteAndEscape(this.text);
    }
}
