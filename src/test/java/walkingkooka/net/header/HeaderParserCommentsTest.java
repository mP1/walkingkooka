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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HeaderParserCommentsTest implements ClassTesting2<HeaderParserComments> {

    @Test
    public void testMissingClosingParensFails() {
        this.consumeFails("(", 0, "Missing closing ')' in \"(\"");
    }

    @Test
    public void testMissingClosingSingleQuoteFails() {
        this.consumeFails("(\'abc", 0, "Missing closing '\"' in \"(\\'abc\"");
    }

    @Test
    public void testMissingClosingDoubleQuoteFails() {
        this.consumeFails("(\"abc", 0, "Missing closing '\"' in \"(\\\"abc\"");
    }

    @Test
    public void testMissingBackslashClosingSingleQuoteFails() {
        this.consumeFails("(\'abc\\", 0, "Missing closing '\"' in \"(\\'abc\\\\\"");
    }

    @Test
    public void testMissingBackslashClosingDoubleQuoteFails() {
        this.consumeFails("(\"abc\\", 0, "Missing closing '\"' in \"(\\\"abc\\\\\"");
    }

    @Test
    public void testInvalidCharacterFails() {
        this.consumeFails("(abc\0)", 0, "Invalid character '\\0' at 4 in \"(abc\0)\"");
    }

    @Test
    public void testEmpty() {
        this.consumeAndCheck("()");
    }

    @Test
    public void testEmpty2() {
        this.consumeAndCheck(" ()", 1);
    }

    @Test
    public void testContent() {
        this.consumeAndCheck("(abc)");
    }

    @Test
    public void testContent2() {
        this.consumeAndCheck("(abc)X", 0, -1);
    }

    @Test
    public void testContent3() {
        this.consumeAndCheck("(abc)XYZ", 0, -3);
    }

    @Test
    public void testContent4() {
        this.consumeAndCheck("(abc)X", 1, -1);
    }

    // single quotes.....................................................................................................

    @Test
    public void testSingleQuotes() {
        this.consumeAndCheck("(\'abc\')");
    }

    @Test
    public void testSingleQuotes2() {
        this.consumeAndCheck(" (\'abc\')", 1);
    }

    @Test
    public void testSingleQuotesEscaped() {
        this.consumeAndCheck("(\'a\\tbc\')");
    }

    @Test
    public void testSingleQuotesDoubleQuote() {
        this.consumeAndCheck("(\'a\"bc\')");
    }

    @Test
    public void testTextSingleQuotes() {
        this.consumeAndCheck("(a\'QRS\')");
    }

    @Test
    public void testTextSingleQuotes2() {
        this.consumeAndCheck("(ab\'QRS\')");
    }

    @Test
    public void testSingleQuotesText() {
        this.consumeAndCheck("(\'QRS\'x)");
    }

    @Test
    public void testSingleQuotesText2() {
        this.consumeAndCheck("(\'QRS\'xy)");
    }

    @Test
    public void testTextSingleQuotesText() {
        this.consumeAndCheck("(a\'QRS\'x)");
    }

    @Test
    public void testTextSingleQuotesText2() {
        this.consumeAndCheck("(a\'QRS\'xy)");
    }

    // single quotes.....................................................................................................

    @Test
    public void testDoubleQuotes() {
        this.consumeAndCheck("(\"abc\")");
    }

    @Test
    public void testDoubleQuotes2() {
        this.consumeAndCheck("(\"abc\")", 1);
    }

    @Test
    public void testDoubleQuotesSingleQuote() {
        this.consumeAndCheck("(\"a'\")");
    }

    @Test
    public void testDoubleQuotesEscaped() {
        this.consumeAndCheck("(\"a\\tbc\")");
    }

    @Test
    public void testTextDoubleQuotes() {
        this.consumeAndCheck("(a\"QRS\")");
    }

    @Test
    public void testTextDoubleQuotes2() {
        this.consumeAndCheck("(ab\"QRS\")");
    }

    @Test
    public void testDoubleQuotesText() {
        this.consumeAndCheck("(\"QRS\"x)");
    }

    @Test
    public void testDoubleQuotesText2() {
        this.consumeAndCheck("(\"QRS\"xy)");
    }

    @Test
    public void testTextDoubleQuotesText() {
        this.consumeAndCheck("(a\"QRS\"x)");
    }

    @Test
    public void testTextDoubleQuotesText2() {
        this.consumeAndCheck("(a\"QRS\"xy)");
    }
    
    // helpers..........................................................................................................

    private void consumeAndCheck(final String text) {
        this.consumeAndCheck(text, 0);
    }

    private void consumeAndCheck(final String text,
                                 final int position) {
        this.consumeAndCheck(text, position, text.length());
    }

    private void consumeAndCheck(final String text,
                                 final int position,
                                 final int end) {
        final int end0 = end <= 0 ? text.length() + end : end;

        assertEquals(')',
                text.charAt(end0 - 1),
                () -> "consume " + CharSequences.quoteAndEscape(text) + " starting at " + position );

        assertEquals(end0,
                HeaderParserComments.consume(text, position),
                () -> "consume " + CharSequences.quoteAndEscape(text) + " starting at " + position);
    }

    private void consumeFails(final String text,
                              final int position,
                              final String expected) {
        final Throwable thrown = assertThrows(Exception.class, () -> {
            HeaderParserComments.consume(text, position);
        });

        assertEquals(expected, thrown.getMessage(), "message");
    }

    @Override
    public Class<HeaderParserComments> type() {
        return HeaderParserComments.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}