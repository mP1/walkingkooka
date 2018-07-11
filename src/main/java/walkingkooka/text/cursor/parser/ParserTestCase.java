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

import org.junit.Test;
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class ParserTestCase<P extends Parser<T, C>, T extends ParserToken, C extends ParserContext> extends PackagePrivateClassTestCase<P> {

    @Test(expected = NullPointerException.class)
    public final void testNullCursorFail() {
        this.createParser().parse(null, this.createContext());
    }

    @Test
    public final void testEmptyCursorFail() {
        this.parseFailAndCheck("");
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

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final String cursorText, final TT token, final String text) {
        return this.parseAndCheck(parser, context, TextCursors.charSequence(cursorText), token, text, "");
    }

    protected final <TT extends ParserToken> TextCursor parseAndCheck(final Parser <TT, C> parser, final C context, final TextCursor cursor, final TT token, final String text, final String textAfter) {
        assertNotNull("value", token);
        assertNotNull("text", text);

        final TextCursorSavePoint before = cursor.save();
        final Optional<TT> result = this.parse(parser, cursor, context);

        CharSequence consumed = before.textBetween();

        final TextCursorSavePoint after = cursor.save();
        this.moveToEnd(cursor);

        if(consumed.length() == 0){
            consumed = after.textBetween();
        }

        assertEquals("Incorrect result returned by parser: " + parser + " from text " + CharSequences.quoteAndEscape(consumed),
                Optional.of(token),
                result);
        assertEquals("incorrect consumed text", consumed, text);
        assertEquals("token consume text is incorrect", text, result.get().text());
        assertEquals("Incorrect text after match", textAfter, after.textBetween().toString());

        after.restore();
        return cursor;
    }

    private void moveToEnd(final TextCursor cursor){
        while(!cursor.isEmpty()){
            cursor.next();
        }
    }

    protected final TextCursor parseFailAndCheck(final String cursorText) {
        return this.parseFailAndCheck(TextCursors.charSequence(cursorText));
    }

    protected final TextCursor parseFailAndCheck(final TextCursor cursor) {
        return this.parseFailAndCheck(this.createParser(), this.createContext(), cursor);
    }

    protected final TextCursor parseFailAndCheck(final Parser <T, C> parser, final C context, final TextCursor cursor) {
        final TextCursorSavePoint before = cursor.save();
        final Optional<T> result = this.parse(parser, cursor, context);
        assertEquals("Incorrect result returned by " + parser + " from text " + CharSequences.quoteAndEscape(before.textBetween()),
                Optional.<T>empty(),
                result);
        return cursor;
    }

    protected final void parseThrows(final String cursorText) {
        this.parseThrows(cursorText, "");
    }

    protected final void parseThrows(final String cursorText, final String messagePart) {
        this.parseThrows(TextCursors.charSequence(cursorText), messagePart);
    }

    protected final void parseThrows(final TextCursor cursor, final String messagePart) {
        this.parseThrows(this.createParser(), this.createContext(), cursor, messagePart);
    }

    protected final void parseThrows(final Parser <T, C> parser, final C context, final TextCursor cursor, final String messagePart) {
        final TextCursorSavePoint before = cursor.save();
        try {
            final Optional<T> result = this.parse(parser, cursor, context);
            fail("Expected ParserException from parser: " + parser + " but got result=" + result + " at " + CharSequences.quoteAndEscape(before.textBetween()));
        } catch (final ParserException cause){
            final String message = cause.getMessage();
            assertTrue("Message: " + message + " missing " + messagePart, message.contains(messagePart));
        }
    }

    protected final <TT extends ParserToken> Optional<TT> parse(final Parser <TT, C> parser, final TextCursor cursor, final C context) {
        assertNotNull("parser", parser);
        assertNotNull("context", context);
        assertNotNull("cursor", cursor);

        final Optional<TT> result = parser.parse(cursor, context);
        assertNotNull("parser returned null result", result);
        return result;
    }
}
