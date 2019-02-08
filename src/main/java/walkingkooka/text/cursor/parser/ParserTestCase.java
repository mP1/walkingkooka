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
 */

package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.type.MemberVisibility;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class ParserTestCase<P extends Parser<T, C>, T extends ParserToken, C extends ParserContext>
        extends ClassTestCase<P>
        implements ToStringTesting<P>,
        TypeNameTesting<P> {

    @Test
    public void testNullCursorFail() {
        assertThrows(NullPointerException.class, () -> {
            this.createParser().parse(null, this.createContext());
        });
    }

    protected abstract P createParser();

    protected abstract C createContext();

    protected final TextCursor parseAndCheck(final String cursorText, final T token, final String text) {
        return this.parseAndCheck(cursorText, token, text, "");
    }

    protected final TextCursor parseAndCheck(final String cursorText, final T token, final String text, final String textAfter) {
        return this.parseAndCheck(TextCursors.charSequence(cursorText), token, text, textAfter);
    }

    protected final TextCursor parseAndCheck(final TextCursor cursor, final T token, final String text) {
        return this.parseAndCheck(cursor, token, text, "");
    }

    protected final TextCursor parseAndCheck(final TextCursor cursor, final T token, final String text, final String textAfter) {
        return this.parseAndCheck(this.createParser(), this.createContext(), cursor, token, text, textAfter);
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final String cursorText, final TT token, final String text) {
        return this.parseAndCheck(parser, cursorText, token, text, "");
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final String cursorText, final TT token, final String text, final String textAfter) {
        return this.parseAndCheck(parser, this.createContext(), cursorText, token, text, textAfter);
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final String cursorText, final TT token, final String text) {
        return this.parseAndCheck(parser, context, cursorText, token, text, "");
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final String cursorText, final TT token, final String text, final String textAfter) {
        return this.parseAndCheck(parser, context, TextCursors.charSequence(cursorText), token, text, textAfter);
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final TextCursor cursor, final TT token, final String text) {
        return this.parseAndCheck(parser, context, cursor, Optional.of(token), text, "");
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final TextCursor cursor, final TT token, final String text, final String textAfter) {
        return this.parseAndCheck(parser, context, cursor, Optional.of(token), text, textAfter);
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final TextCursor cursor, final Optional<TT> token, final String text, final String textAfter) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(text, "text");

        final TextCursorSavePoint before = cursor.save();
        final Optional<TT> result = this.parse(parser, cursor, context);

        final CharSequence consumed = before.textBetween();

        final TextCursorSavePoint after = cursor.save();
        cursor.end();

        CharSequence all = consumed;
        if(all.length() == 0){
            all = after.textBetween();
        }

        final String textRemaining = after.textBetween().toString();
        if(!token.equals(result)){
            final CharSequence all2 = all;
            assertEquals(this.toString(token),
                    this.toString(result),
                    () -> "Incorrect result returned by parser: " + parser + "\ntext:\n" + CharSequences.quoteAndEscape(all2) + "\nunconsumed text:\n" + textRemaining);
        }
        assertEquals(consumed, text, "incorrect consumed text");
        assertEquals(text, result.isPresent() ? result.get().text() : "", "token consume text is incorrect");
        assertEquals(textAfter, textRemaining, "Incorrect text after match");

        after.restore();
        return cursor;
    }

    private String toString(final Optional<? extends ParserToken> token) {
        return token.isPresent() ?
                this.toString(token.get()) :
                "";
    }

    protected String toString(final ParserToken token) {
        return token.toString();
    }

    protected final TextCursor parseFailAndCheck(final String cursorText) {
        return this.parseFailAndCheck(TextCursors.charSequence(cursorText));
    }

    protected final TextCursor parseFailAndCheck(final TextCursor cursor) {
        return this.parseFailAndCheck(this.createParser(), this.createContext(), cursor);
    }

    protected final TextCursor parseFailAndCheck(final Parser <T, C> parser, final String cursorText) {
        return this.parseFailAndCheck(parser, this.createContext(), cursorText);
    }

    protected final TextCursor parseFailAndCheck(final Parser <T, C> parser, final C context, final String cursorText) {
        return this.parseFailAndCheck(parser, context, TextCursors.charSequence(cursorText));
    }

    protected final TextCursor parseFailAndCheck(final Parser <T, C> parser, final C context, final TextCursor cursor) {
        final TextCursorSavePoint before = cursor.save();
        final Optional<T> result = this.parse(parser, cursor, context);
        assertEquals(Optional.<T>empty(),
                result,
                "Incorrect result returned by " + parser + " from text " + CharSequences.quoteAndEscape(before.textBetween()));
        return cursor;
    }

    protected final void parseThrows(final String cursorText) {
        this.parseThrows(cursorText, "");
    }

    protected final void parseThrows(final String cursorText, final char c, final String column, final int row) {
        this.parseThrows(cursorText, c, column.length(), row);
    }

    protected final void parseThrows(final String cursorText, final char c, final int column, final int row) {
        // Message format from BasicParserReporter
        this.parseThrows(cursorText, "Unrecognized character " + CharSequences.quoteAndEscape(c) + " at (" + column + "," + row + ")");
    }

    protected final void parseThrowsEndOfText(final String cursorText) {
        this.parseThrowsEndOfText(cursorText, cursorText.length() + 1, 1);
    }

    protected final void parseThrowsEndOfText(final String cursorText, final int column, final int row) {
        // Message format from BasicParserReporter
        this.parseThrows(cursorText, endOfText(column, row));
    }

    protected final void parseThrowsEndOfText(final Parser<T,C> parser, final String cursorText, final int column, final int row) {
        this.parseThrowsEndOfText(parser, this.createContext(), cursorText, column, row);
    }

    protected final void parseThrowsEndOfText(final Parser<T,C> parser, final C context, final String cursorText, final int column, final int row) {
        final TextCursor cursor = TextCursors.charSequence(cursorText);
        cursor.end();

        this.parseThrows(parser, context, cursor, endOfText(column, row));
    }

    protected final String endOfText(final int column, final int row) {
        return "End of text at (" + column + "," + row + ")";
    }

    protected final void parseThrows(final String cursorText, final String messagePart) {
        this.parseThrows(TextCursors.charSequence(cursorText), messagePart);
    }

    protected final void parseThrows(final TextCursor cursor, final String messagePart) {
        this.parseThrows(this.createParser(), this.createContext(), cursor, messagePart);
    }

    protected final void parseThrows(final Parser <T, C> parser, final String cursor) {
        this.parseThrows(parser, cursor, "");
    }

    protected final void parseThrows(final Parser <T, C> parser, final String cursor, final String messagePart) {
        this.parseThrows(parser, TextCursors.charSequence(cursor), messagePart);
    }

    protected final void parseThrows(final Parser <T, C> parser, final TextCursor cursor, final String messagePart) {
        this.parseThrows(parser, this.createContext(), cursor, messagePart);
    }

    protected final void parseThrows(final Parser <T, C> parser, final C context, final TextCursor cursor, final String messagePart) {
        final TextCursorSavePoint before = cursor.save();
        try {
            final Optional<T> result = this.parse(parser, cursor, context);

            final TextCursorSavePoint left = cursor.save();
            cursor.end();

            fail("Expected ParserException from parser: " + parser +
                 " with text " + CharSequences.quoteAndEscape(before.textBetween()) +
                 " but got result=" + result +
                 " at " + left +
                 "\n" + this.toString(result));
        } catch (final ParserException cause){
            final String message = cause.getMessage();
            assertTrue(message.contains(messagePart), () -> "Message: " + message + " missing " + messagePart);
        }
    }

    protected final <TT extends ParserToken> Optional<TT> parse(final Parser <TT, C> parser, final TextCursor cursor, final C context) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(cursor, "cursor");

        final Optional<TT> result = parser.parse(cursor, context);
        assertNotNull(result, () -> "parser " + parser + " returned null result");
        return result;
    }

    protected final DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.basic("$", '.', 'E', ',', '-', '%', '+');
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return Parser.class.getSimpleName();
    }
}
