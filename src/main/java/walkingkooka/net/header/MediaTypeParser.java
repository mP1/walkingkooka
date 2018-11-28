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
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;

/**
 * Base class that contains the core of the parsing of header type logic. Two sub classes exist that handle commas
 * differently.
 */
abstract class MediaTypeParser {

    static MediaType one(final String text) {
        return OneMediaTypeParser.parseOneOrFail(text);
    }

    static List<MediaType> many(final String text) {
        return ManyMediaTypeParser.parseMany(text);
    }

    /**
     * Package private to limit sub classing.
     */
    MediaTypeParser(final String text) {
        super();
        this.text = text;
    }

    // parse ...........................................................................................................

    /**
     * Creates a {@link MediaType} breaking up the {@link String text} into type and sub types, ignoring any optional
     * parameters if they are present.
     * Depending on whether a single or many header types separated by commas are desired, sub classes will
     * try this method again or simply fail if a comma is encountered.
     */
    final MediaType parse(final int initialMode) {
        final String text = this.text;
        final int length = text.length();

        int mode = initialMode;
        int start = this.position;
        String type = null;
        String subType = null;
        MediaTypeParameterName parameterName = null;
        Map<MediaTypeParameterName, String> parameters = Maps.ordered();

        while (this.position < length) {
            final char c = text.charAt(this.position);

            switch (mode) {
                case MODE_INITIAL_WHITESPACE:
                    if(WHITESPACE.test(c)) {
                        break;
                    }
                    // fall thru intentional...
                    mode = MODE_TYPE;
                    start = this.position;
                case MODE_TYPE:
                    if (MediaType.isTokenCharacter(c)) {
                        break;
                    }
                    if (MediaType.TYPE_SUBTYPE_SEPARATOR == c) {
                        type = token(TYPE, start);
                        start = this.position + 1;
                        mode = MODE_SUBTYPE;
                        break;
                    }
                    if(MediaType.MEDIATYPE_SEPARATOR == c) {
                        this.mediaTypeSeparator();
                        mode = MODE_FINISHED;
                        break;
                    }
                    this.failInvalidCharacter();
                    break;
                case MODE_SUBTYPE:
                    if (MediaType.isTokenCharacter(c)) {
                        break;
                    }
                    if (MediaType.PARAMETER_SEPARATOR == c) {
                        subType = token(SUBTYPE, start);
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    if(MediaType.MEDIATYPE_SEPARATOR == c) {
                        this.mediaTypeSeparator();
                        subType = token(SUBTYPE, start);
                        mode = MODE_FINISHED;
                        break;
                    }
                    if(WHITESPACE.test(c)) {
                        subType = token(SUBTYPE, start);
                        mode = MODE_SUBTYPE_WHITESPACE;
                        break;
                    }
                    this.failInvalidCharacter();
                case MODE_SUBTYPE_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    if (MediaType.PARAMETER_SEPARATOR == c) {
                        mode = MODE_PARAMETER_NAME;
                        start = this.position;
                        break;
                    }
                    if(MediaType.MEDIATYPE_SEPARATOR == c) {
                        this.mediaTypeSeparator();
                        mode = MODE_FINISHED;
                        break;
                    }
                    this.failInvalidCharacter();
                case MODE_PARAMETER_SEPARATOR_WHITESPACE:
                    if (WHITESPACE.test(c)) {
                        break;
                    }
                    // end of (optional) leading whitespace must be parameter name.
                    mode = MODE_PARAMETER_NAME;
                    start = this.position;
                case MODE_PARAMETER_NAME:
                    if (MediaType.isTokenCharacter(c)) {
                        break;
                    }
                    if (MediaType.PARAMETER_NAME_VALUE_SEPARATOR == c) {
                        parameterName = MediaTypeParameterName.with(token(PARAMETER_NAME, start));
                        start = this.position + 1;
                        mode = MODE_PARAMETER_VALUE_INITIAL;
                        break;
                    }
                    this.failInvalidCharacter();
                    break;
                case MODE_PARAMETER_VALUE_INITIAL:
                    if (DOUBLE_QUOTE == c) {
                        mode = MODE_PARAMETER_QUOTES;
                        break;
                    }
                    // deliberate fall thru must be a normal parameter value token character.
                case MODE_PARAMETER_VALUE:
                    if (MediaType.isTokenCharacter(c)) {
                        break;
                    }
                    if (MediaType.PARAMETER_SEPARATOR == c) {
                        parameters.put(parameterName, token(PARAMETER_VALUE, start));
                        parameterName = null;
                        start = this.position + 1;
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    if(MediaType.MEDIATYPE_SEPARATOR == c) {
                        this.mediaTypeSeparator();

                        parameters.put(parameterName, token(PARAMETER_VALUE, start));
                        parameterName = null;
                        start = this.position + 1;

                        mode = MODE_FINISHED;
                        break;
                    }
                    if(WHITESPACE.test(c)) {
                        parameters.put(parameterName, token(PARAMETER_VALUE, start));
                        parameterName = null;
                        start = this.position + 1;

                        mode = MODE_PARAMETER_VALUE_WHITESPACE;
                        break;
                    }
                    this.failInvalidCharacter();
                    break;
                case MODE_PARAMETER_QUOTES:
                    if (BACKSLASH == c) {
                        mode = MODE_PARAMETER_ESCAPE;
                        break;
                    }
                    if (DOUBLE_QUOTE == c) {
                        parameters.put(parameterName, text.substring(start + 1, this.position)); // allow empty quoted strings!
                        parameterName = null;
                        start = this.position + 1;
                        mode = MODE_PARAMETER_VALUE_WHITESPACE;
                        break;
                    }
                    break;
                case MODE_PARAMETER_ESCAPE:
                    // only accept proper escaping...
                    if (BACKSLASH == c || DOUBLE_QUOTE == c) {
                        mode = MODE_PARAMETER_QUOTES;
                        break;
                    }
                    this.failInvalidCharacter();
                    break;
                case MODE_PARAMETER_VALUE_WHITESPACE:
                    if(WHITESPACE.test(c)){
                        break;
                    }
                case MODE_PARAMETER_SEPARATOR:
                    if (MediaType.PARAMETER_SEPARATOR == c) {
                        mode = MODE_PARAMETER_SEPARATOR_WHITESPACE;
                        break;
                    }
                    if(MediaType.MEDIATYPE_SEPARATOR == c) {
                        this.mediaTypeSeparator();
                        mode = MODE_FINISHED;
                        break;
                    }
                    this.failInvalidCharacter();
                default:
                    NeverError.unhandledCase(mode);
            }

            this.position++;

            // the end of a header type was found, stop trying extra characters...
            if(MODE_FINISHED == mode) {
                break;
            }
        }

        // only modes that would be an error state are included in the switch below.
        switch (mode) {
            case MODE_INITIAL_WHITESPACE:
                failEmptyToken(TYPE, this.position, text);
            case MODE_TYPE:
                failEmptyToken(SUBTYPE, this.position, text);
            case MODE_SUBTYPE:
                subType = token(SUBTYPE, start);
                break;
            case MODE_PARAMETER_NAME:
                failEmptyToken(PARAMETER_VALUE, this.position, text);
            case MODE_PARAMETER_VALUE_INITIAL:
            case MODE_PARAMETER_VALUE:
                parameters.put(parameterName, token(PARAMETER_VALUE, start));
                break;
            case MODE_PARAMETER_QUOTES:
            case MODE_PARAMETER_ESCAPE:
                this.position--; // point to last character.
                failInvalidCharacter();
            default:
                break;
        }

        return MediaType.with(type, subType, parameters, text);
    }

    private final static char BACKSLASH = '\\';
    private final static char DOUBLE_QUOTE = '"';
    private final static CharPredicate WHITESPACE = CharPredicates.any("\u0009\u0020");

    /**
     * Called when a comma is encountered when a type was expected.
     */
    abstract void mediaTypeSeparator();

    /**
     * Reports an invalid character within the unparsed header type.
     */
    final void failInvalidCharacter() {
        final String text = this.text;
        final int pos = this.position;

        throw new IllegalArgumentException(MediaType.invalidCharacter(text.charAt(pos), pos, text));
    }

    /**
     * Extracts the token failing if it is empty.
     */
    private String token(final String tokenName, final int start) {
        final int end = this.position;
        final String text = this.text;
        if (start == end) {
            failEmptyToken(tokenName, end, text);
        }
        return text.substring(start, end);
    }

    /**
     * Reports an invalid character within the unparsed header type.
     */
    private static void failEmptyToken(final String token, final int i, final String text) {
        throw new IllegalArgumentException("Missing " + token + " at " + i + " in " + CharSequences.quoteAndEscape(text));
    }

    /**
     * Constants used in the {@link #parse(int)} state machine.
     */
    final static int MODE_INITIAL_WHITESPACE = 1;
    final static int MODE_TYPE = MODE_INITIAL_WHITESPACE + 1;
    private final static int MODE_SUBTYPE = MODE_TYPE + 1;
    private final static int MODE_SUBTYPE_WHITESPACE = MODE_SUBTYPE + 1;
    private final static int MODE_PARAMETER_SEPARATOR_WHITESPACE = MODE_SUBTYPE_WHITESPACE + 1;
    private final static int MODE_PARAMETER_NAME = MODE_PARAMETER_SEPARATOR_WHITESPACE + 1;
    private final static int MODE_PARAMETER_VALUE_INITIAL = MODE_PARAMETER_NAME + 1;
    private final static int MODE_PARAMETER_VALUE = MODE_PARAMETER_VALUE_INITIAL + 1;
    private final static int MODE_PARAMETER_QUOTES = MODE_PARAMETER_VALUE + 1;
    private final static int MODE_PARAMETER_ESCAPE = MODE_PARAMETER_QUOTES + 1;
    private final static int MODE_PARAMETER_VALUE_WHITESPACE = MODE_PARAMETER_ESCAPE + 1;
    private final static int MODE_PARAMETER_SEPARATOR = MODE_PARAMETER_VALUE_WHITESPACE + 1;
    private final static int MODE_FINISHED = MODE_PARAMETER_SEPARATOR + 1;

    final static String TYPE = "type";
    final static String SUBTYPE = "sub type";
    final static String PARAMETER_NAME = "parameter name";
    final static String PARAMETER_VALUE = "parameter value";

    /**
     * The current position with the text
     */
    int position;

    /**
     * The text being parsed.
     */
    final String text;

    @Override
    public final String toString() {
        return CharSequences.quoteAndEscape(this.text).toString();
    }
}
