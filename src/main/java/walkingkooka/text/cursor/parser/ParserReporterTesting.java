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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public interface ParserReporterTesting<R extends ParserReporter<C>, T extends ParserToken, C extends ParserContext>
        extends ToStringTesting<R>, TypeNameTesting<R> {

    @Test
    default void testNullTextCursorFails() {
        assertThrows(NullPointerException.class, () -> this.report(null, this.createContext(), Parsers.fake()));
    }

    @Test
    default void testNullContextFails() {
        assertThrows(NullPointerException.class, () -> this.report(TextCursors.fake(), null, Parsers.fake()));
    }

    @Test
    default void testNullParserFails() {
        assertThrows(NullPointerException.class, () -> this.report(TextCursors.fake(), this.createContext(), null));
    }

    R createParserReporter();

    C createContext();

    default void reportAndCheck(final String text,
                                final Parser<C> parser,
                                final String messageContains) {
        this.reportAndCheck(text, this.createContext(), parser, messageContains);
    }

    default void reportAndCheck(final String text,
                                final C context,
                                final Parser<C> parser,
                                final String messageContains) {
        this.reportAndCheck(TextCursors.charSequence(text), context, parser, messageContains);
    }

    default void reportAndCheck(final TextCursor cursor,
                                final Parser<C> parser,
                                final String messageContains) {
        this.reportAndCheck(cursor, this.createContext(), parser, messageContains);
    }

    default void reportAndCheck(final TextCursor cursor,
                                final C context,
                                final Parser<C> parser,
                                final String messageContains) {
        this.reportAndCheck(this.createParserReporter(), cursor, context, parser, messageContains);
    }

    default void reportAndCheck(final ParserReporter<C> reporter,
                                final TextCursor cursor,
                                final C context,
                                final Parser<C> parser,
                                final String messageContains) {
        assertFalse(CharSequences.isNullOrEmpty(messageContains), "messageContains must not be null or empty");

        final TextCursorSavePoint save = cursor.save();
        try {
            this.report(reporter, cursor, context, parser);
            fail("Reporter should have reported exception");
        } catch (final ParserReporterException expected) {
            save.restore();
            final String message = expected.getMessage();
            assertTrue(message.contains(messageContains),
                    () -> "report message: " + CharSequences.quoteAndEscape(message) + " missing contains: " + CharSequences.quoteAndEscape(messageContains));
        }
    }

    default void report(final TextCursor cursor,
                        final C context,
                        final Parser<C> parser) {
        this.report(this.createParserReporter(), cursor, context, parser);
    }

    default void report(final ParserReporter<C> reporter,
                        final TextCursor cursor,
                        final C context,
                        final Parser<C> parser) {
        reporter.report(cursor, context, parser);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return ParserReporter.class.getSimpleName();
    }
}
