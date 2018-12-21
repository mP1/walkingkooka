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
import walkingkooka.NeverError;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.CharSequences;
import walkingkooka.text.CharacterConstant;

import java.util.Map;
import java.util.function.Function;

/**
 * A parser that translates tokens into abstract event methods.
 */
abstract class HeaderParser2<N extends HeaderParameterName<?>> extends HeaderParser {

    static void checkText(final String text, final String label) {
        CharSequences.failIfNullOrEmpty(text, label);
    }

    final static char PARAMETER_SEPARATOR = ';';
    final static char PARAMETER_NAME_VALUE_SEPARATOR = '=';
    final static String PARAMETER_NAME = "parameter name";
    final static String PARAMETER_VALUE = "parameter value";

    static {
        check(SEPARATOR, HeaderValueWithParameters.SEPARATOR, "SEPARATOR");
        check(PARAMETER_SEPARATOR, HeaderValueWithParameters.PARAMETER_SEPARATOR, "PARAMETER_SEPARATOR");
        check(PARAMETER_NAME_VALUE_SEPARATOR, HeaderValueWithParameters.PARAMETER_NAME_VALUE_SEPARATOR, "PARAMETER_NAME_VALUE_SEPARATOR");
    }

    /**
     * Verifies that a static final char value matches a {@link CharacterConstant} defined in {@link HeaderValue}.
     */
    private static void check(final char c, final CharacterConstant constant, final String name) {
        if(constant.character() != c) {
            throw new NeverError(name + "=" + CharSequences.quoteIfChars(c) + " should be " + constant);
        }
    }

    HeaderParser2(final String text) {
        super(text);
        this.mode = HeaderParser2Mode.WHITESPACE;
    }

    final void parse() {
        try {
            final int length = this.text.length();
            while (this.position < length) {
                this.mode.accept(this);
            }
            this.mode.endOfText(this);
        } catch (final InvalidCharacterException cause) {
            throw new HeaderValueException(cause.getMessage(), cause);
        }
    }

    /**
     * Uses the given predicate to parse the value token. The only valid trailing character is LWSP or
     * {@link #PARAMETER_SEPARATOR} others will be reported as an invalid character.
     */
    final <V> V parseValue(final CharPredicate predicate,
                           final String tokenName,
                           final Function<String, V> factory) {
        final int start = this.position;
        final String value = this.tokenText(predicate);

        this.failNotIfWhitespaceOrParameterSeparatorOrSeparator();

        try {
            return factory.apply(value);
        } catch (final InvalidCharacterException cause) {
            throw cause.setTextAndPosition(this.text, start + cause.position());
        }
    }

    /**
     * Uses the given predicate to parse the {@link HeaderParameterName}. The only valid trailing character is LWSP or
     * {@link #PARAMETER_NAME_VALUE_SEPARATOR} others will be reported as an invalid character.
     */
    final void parseParameterName(final CharPredicate predicate, final Function<String, N> factory) {
        final String parameterName = this.tokenText(predicate);

        final int start = this.position;
        if(this.hasMoreCharacters()) {
            switch(this.character()) {
                case PARAMETER_NAME_VALUE_SEPARATOR:
                case '\t':
                case ' ':
                case '\r':
                    break;

                default:
                    this.failInvalidCharacter();
            }
        }

        if(parameterName.isEmpty()) {
            this.failEmptyToken(PARAMETER_NAME);
        }

        try {
            this.parameterName = factory.apply(parameterName);
        } catch (final InvalidCharacterException cause) {
            throw cause.setTextAndPosition(this.text, start + cause.position());
        }
    }

    /**
     * Uses the given predicate to parse the parameter value. The only valid trailing character is WHITESPACE or
     * {@link #PARAMETER_SEPARATOR} or {@link #SEPARATOR} others will be reported as an invalid character.
     */
    final void parseParameterValue(final CharPredicate predicate) {
        final String parameterValue = this.tokenText(predicate);

        this.failNotIfWhitespaceOrParameterSeparatorOrSeparator();

        this.addParameter(parameterValue);
    }

    /**
     * The current mode.
     */
    HeaderParser2Mode mode;

    /**
     * Sub classes must consumes the value portion of a header value.
     */
    abstract void value();

    /**
     * Called when a value is missing. Typically called when the text end is reached, just after a separator with/without
     * whitespace.
     */
    abstract void failMissingValue();

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

    /**
     * Consumes any optional whitespace and then updates the current mode to next.
     */
    final void consumeWhitespace(final HeaderParser2Mode next) {
        this.consumeWhitespace();
        this.mode = next;
    }

    final void failNotIfWhitespaceOrParameterSeparatorOrSeparator() {
        if(this.hasMoreCharacters()) {

            switch(this.character()) {
                case '\t':
                case ' ':
                case '\r':
                    break;
                case PARAMETER_SEPARATOR:
                    break;
                case SEPARATOR:
                    this.separator();
                    break;
                default:
                    this.failInvalidCharacter();
            }
        }
    }
}
