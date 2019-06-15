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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EbnfTerminalParserTest extends EbnfParserTestCase3<EbnfTerminalParserToken> {

    @Test
    public void testIncompleteFails() {
        this.parseFailAndCheck("'incomplete");
    }

    @Test
    public void testIncompleteFails2() {
        this.parseFailAndCheck("\"incomplete");
    }

    @Test
    public void testInvalidBackslash() {
        assertThrows(EbnfTerminalParserException.class, () -> {
            this.parseFailAndCheck("'\\x'");
        });
    }

    @Test
    public void testSingleQuoteLiteral() {
        final String text = "hello";

        this.parseAndCheck2(singleQuote(text), text);
    }

    @Test
    public void testDoubleQuoteLiteral() {
        final String text = "hello";

        this.parseAndCheck2(doubleQuote(text), text);
    }

    @Test
    public void testBackslashNul() {
        this.parseAndCheck2(singleQuote("hel\\0lo"), "hel\0lo");
    }

    @Test
    public void testBackslashForm() {
        this.parseAndCheck2(singleQuote("hel\\flo"), "hel\flo");
    }

    @Test
    public void testBackslashTab() {
        this.parseAndCheck2(singleQuote("hel\\tlo"), "hel\tlo");
    }

    @Test
    public void testBackslashCr() {
        this.parseAndCheck2(singleQuote("hel\\rlo"), "hel\rlo");
    }

    @Test
    public void testBackslashNl() {
        this.parseAndCheck2(singleQuote("hel\\nlo"), "hel\nlo");
    }

    @Test
    public void testBackslashSingleQuote() {
        this.parseAndCheck2(singleQuote("hel\\'lo"), "hel\'lo");
    }

    @Test
    public void testBackslashDoubleQuote() {
        this.parseAndCheck2(singleQuote("hel\\\"lo"), "hel\"lo");
    }

    @Test
    public void testInvalidUnicode() {
        this.parseThrows("'\\u0XYZ1");
    }

    @Test
    public void testUnicode() {
        this.parseAndCheck2(singleQuote("hel\\u1234lo"), "hel\u1234lo");
    }

    @Test
    public void testUnicode2() {
        this.parseAndCheck2(singleQuote("hel\\u12345lo"), "hel\u12345lo");
    }

    @Test
    public void test2() {
        this.parseAndCheck2(singleQuote("h\tel\\u12345lo"), "h\tel\u12345lo", "after");
    }

    private void parseAndCheck2(final String quoted, final String text) {
        this.parseAndCheck2(quoted, text, "");
    }

    private void parseAndCheck2(final String quoted, final String text, final String after) {
        this.parseAndCheck(quoted + after, EbnfTerminalParserToken.with(text, quoted), quoted, after);
    }

    private static String singleQuote(final String text) {
        return '\'' + text + '\'';
    }

    private static String doubleQuote(final String text) {
        return '"' + text + '"';
    }

    @Override
    public Parser<EbnfParserContext> createParser() {
        return EbnfGrammarParser.TERMINAL;
    }

    @Override
    String text() {
        return singleQuote("hello");
    }

    @Override
    EbnfTerminalParserToken token(final String text) {
        final String quotedText = this.text();
        return EbnfTerminalParserToken.with(
                quotedText.substring(1, quotedText.length() - 1),
                text);
    }
}
