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

import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import java.math.MathContext;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin that includes numerous helpers to assist parsing and verifying the outcome for success and failures.
 */
public interface ParserTesting<P extends Parser<C>,
        C extends ParserContext>
        extends Testing {

    /**
     * Provides or creates the {@link Parser} being tested.
     */
    P createParser();

    /**
     * Provides the required {@link ParserContext} used by the {@link Parser}
     */
    C createContext();

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

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final C context,
                                     final String cursorText,
                                     final ParserToken token,
                                     final String text) {
        return this.parseAndCheck(parser, context, cursorText, token, text, "");
    }

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final C context,
                                     final String cursorText,
                                     final ParserToken token,
                                     final String text,
                                     final String textAfter) {
        return this.parseAndCheck(parser, context, TextCursors.charSequence(cursorText), token, text, textAfter);
    }

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final C context,
                                     final TextCursor cursor,
                                     final ParserToken token,
                                     final String text) {
        return this.parseAndCheck(parser, context, cursor, Optional.of(token), text, "");
    }

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final C context,
                                     final TextCursor cursor,
                                     final ParserToken token,
                                     final String text,
                                     final String textAfter) {
        return this.parseAndCheck(parser, context, cursor, Optional.of(token), text, textAfter);
    }

    default TextCursor parseAndCheck(final Parser<C> parser,
                                     final C context,
                                     final TextCursor cursor,
                                     final Optional<ParserToken> token,
                                     final String text,
                                     final String textAfter) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(text, "text");

        final TextCursorSavePoint before = cursor.save();
        final Optional<ParserToken> result = this.parse(parser, cursor, context);

        final CharSequence consumed = before.textBetween();

        final TextCursorSavePoint after = cursor.save();
        cursor.end();

        CharSequence all = consumed;
        if (all.length() == 0) {
            all = after.textBetween();
        }

        final String textRemaining = after.textBetween().toString();
        if (!token.equals(result)) {
            final CharSequence all2 = all;
            assertEquals(token.isPresent() ? this.parserTokenToString(token.get()) : "",
                    token.isPresent() ? this.parserTokenToString(token.get()) : "",
                    () -> "Incorrect result returned by parser: " + parser + "\ntext:\n" + CharSequences.quoteAndEscape(all2) + "\nunconsumed text:\n" + textRemaining);
        }
        assertEquals(consumed, text, "incorrect consumed text");
        assertEquals(text, result.isPresent() ? result.get().text() : "", "token consume text is incorrect");
        assertEquals(textAfter, textRemaining, "Incorrect text after match");

        after.restore();
        return cursor;
    }

    default String parserTokenToString(final ParserToken token) {
        return token.toString();
    }

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

    default TextCursor parseFailAndCheck(final Parser<C> parser,
                                         final C context,
                                         final String cursorText) {
        return this.parseFailAndCheck(parser, context, TextCursors.charSequence(cursorText));
    }

    default TextCursor parseFailAndCheck(final Parser<C> parser,
                                         final C context,
                                         final TextCursor cursor) {
        final TextCursorSavePoint before = cursor.save();
        final Optional<ParserToken> result = this.parse(parser, cursor, context);
        assertEquals(Optional.<ParserToken>empty(),
                result,
                "Incorrect result returned by " + parser + " from text " + CharSequences.quoteAndEscape(before.textBetween()));
        return cursor;
    }

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

    default void parseThrowsEndOfText(final Parser<C> parser,
                                      final C context,
                                      final String cursorText,
                                      final int column,
                                      final int row) {
        final TextCursor cursor = TextCursors.charSequence(cursorText);
        cursor.end();

        this.parseThrows(parser, context, cursor, endOfText(column, row));
    }

    default String endOfText(final int column, final int row) {
        return "End of text at (" + column + "," + row + ")";
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

    default void parseThrows(final Parser<C> parser,
                             final C context,
                             final TextCursor cursor,
                             final String messagePart) {
        final ParserException expected = assertThrows(ParserException.class, () -> {
            this.parse(parser, cursor, context);
        });

        final String message = expected.getMessage();
        assertEquals(true, message.contains(messagePart), () -> "Message: " + message + " missing " + messagePart);
    }

    default Optional<ParserToken> parse(final Parser<C> parser, final TextCursor cursor, final C context) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(cursor, "cursor");

        final Optional<ParserToken> result = parser.parse(cursor, context);
        assertNotNull(result, () -> "parser " + parser + " returned null result");
        return result;
    }

    default DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(MathContext.DECIMAL32);
    }
}
