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

package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class ParserReporterTestCase<R extends ParserReporter<T, C>, T extends ParserToken, C extends ParserContext> extends ClassTestCase<R>
        implements TypeNameTesting<R> {

    @Test
    public final void testNullTextCursorFails() {
        assertThrows(NullPointerException.class, () -> {
            this.report(null, this.createContext(), this.parser());
        });
    }

    @Test
    public final void testNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.report(TextCursors.fake(), null, this.parser());
        });
    }

    @Test
    public final void testNullParserFails() {
        assertThrows(NullPointerException.class, () -> {
            this.report(TextCursors.fake(), this.createContext(), null);
        });
    }

    protected abstract R createParserReporter();

    protected abstract C createContext();

    private Parser<T, C> parser() {
        return Parsers.fake();
    }

    protected void reportAndCheck(final String text,
                                  final Parser<T, C> parser,
                                  final String messageContains) {
        this.reportAndCheck(text, this.createContext(), parser, messageContains);
    }

    protected void reportAndCheck(final String text,
                                  final C context,
                                  final Parser<T, C> parser,
                                  final String messageContains) {
        this.reportAndCheck(TextCursors.charSequence(text), context, parser, messageContains);
    }

    protected void reportAndCheck(final TextCursor cursor,
                                  final Parser<T, C> parser,
                                  final String messageContains) {
        this.reportAndCheck(cursor, this.createContext(), parser, messageContains);
    }

    protected void reportAndCheck(final TextCursor cursor,
                                  final C context,
                                  final Parser<T, C> parser,
                                  final String messageContains) {
        this.reportAndCheck(this.createParserReporter(), cursor, context, parser, messageContains);
    }

    protected void reportAndCheck(final ParserReporter<T, C> reporter,
                        final TextCursor cursor,
                        final C context,
                        final Parser<T, C> parser,
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

    protected void report(final TextCursor cursor,
                          final C context,
                          final Parser<T, C> parser) {
        this.report(this.createParserReporter(), cursor, context, parser);
    }

    protected void report(final ParserReporter<T, C> reporter,
                                  final TextCursor cursor,
                                  final C context,
                                  final Parser<T, C> parser) {
        reporter.report(cursor, context, parser);
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
    public final String typeNameSuffix() {
        return ParserReporter.class.getSimpleName();
    }
}
