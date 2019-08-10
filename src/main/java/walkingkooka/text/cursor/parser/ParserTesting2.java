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

package walkingkooka.text.cursor.parser;

import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;

/**
 * Mixin that includes numerous helpers to assist parsing and verifying the outcome for success and failures.
 */
public interface ParserTesting2<P extends Parser<C>,
        C extends ParserContext>
        extends ParserTesting {

    /**
     * Provides or creates the {@link Parser} being tested.
     */
    P createParser();

    /**
     * Provides the required {@link ParserContext} used by the {@link Parser}
     */
    C createContext();

    // parseAndCheck....................................................................................................

    default TextCursor parseAndCheck(final String cursorText,
                                     final ParserToken token,
                                     final String text) {
        return this.parseAndCheck(cursorText, token, text, "");
    }

    default TextCursor parseAndCheck(final String cursorText,
                                     final ParserToken token,
                                     final String text,
                                     final String textAfter) {
        return this.parseAndCheck(TextCursors.charSequence(cursorText), token, text, textAfter);
    }

    default TextCursor parseAndCheck(final TextCursor cursor,
                                     final ParserToken token,
                                     final String text) {
        return this.parseAndCheck(cursor, token, text, "");
    }

    default TextCursor parseAndCheck(final TextCursor cursor,
                                     final ParserToken token,
                                     final String text,
                                     final String textAfter) {
        return this.parseAndCheck(this.createParser(), this.createContext(), cursor, token, text, textAfter);
    }

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final String cursorText,
                                     final ParserToken token,
                                     final String text) {
        return this.parseAndCheck(parser, cursorText, token, text, "");
    }

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final String cursorText,
                                     final ParserToken token,
                                     final String text,
                                     final String textAfter) {
        return this.parseAndCheck(parser, this.createContext(), cursorText, token, text, textAfter);
    }

    // parseFailAndCheck................................................................................................

    default TextCursor parseFailAndCheck(final String cursorText) {
        return this.parseFailAndCheck(TextCursors.charSequence(cursorText));
    }

    default TextCursor parseFailAndCheck(final TextCursor cursor) {
        return this.parseFailAndCheck(this.createParser(), this.createContext(), cursor);
    }

    default TextCursor parseFailAndCheck(final Parser<C> parser,
                                         final String cursorText) {
        return this.parseFailAndCheck(parser, this.createContext(), cursorText);
    }

    // parseThrows......................................................................................................

    default void parseThrows(final String cursorText) {
        this.parseThrows(cursorText, "");
    }

    default void parseThrows(final String cursorText,
                             final char c,
                             final String column,
                             final int row) {
        this.parseThrows(cursorText, c, column.length(), row);
    }

    default void parseThrows(final String cursorText,
                             final char c,
                             final int column,
                             final int row) {
        // Message format from BasicParserReporter
        this.parseThrows(cursorText, "Unrecognized character " + CharSequences.quoteAndEscape(c) + " at (" + column + "," + row + ")");
    }

    default void parseThrowsEndOfText(final String cursorText) {
        this.parseThrowsEndOfText(cursorText, cursorText.length() + 1, 1);
    }

    default void parseThrowsEndOfText(final String cursorText,
                                      final int column,
                                      final int row) {
        // Message format from BasicParserReporter
        this.parseThrows(cursorText, endOfText(column, row));
    }

    default void parseThrowsEndOfText(final Parser<C> parser,
                                      final String cursorText,
                                      final int column,
                                      final int row) {
        this.parseThrowsEndOfText(parser, this.createContext(), cursorText, column, row);
    }

    default void parseThrows(final String cursorText, final String messagePart) {
        this.parseThrows(TextCursors.charSequence(cursorText), messagePart);
    }

    default void parseThrows(final TextCursor cursor, final String messagePart) {
        this.parseThrows(this.createParser(), this.createContext(), cursor, messagePart);
    }

    default void parseThrows(final Parser<C> parser, final String cursor) {
        this.parseThrows(parser, cursor, "");
    }

    default void parseThrows(final Parser<C> parser, final String cursor, final String messagePart) {
        this.parseThrows(parser, TextCursors.charSequence(cursor), messagePart);
    }

    default void parseThrows(final Parser<C> parser, final TextCursor cursor, final String messagePart) {
        this.parseThrows(parser, this.createContext(), cursor, messagePart);
    }
}
