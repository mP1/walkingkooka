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

public abstract class QuotedParserTestCase<P extends QuotedParser<T, FakeParserContext>, T extends QuotedParserToken> extends ParserTemplateTestCase<P, T> {

    @Test
    public final void testFirstNotQuote() {
        this.parseFailAndCheck("abc'");
    }

    @Test
    public final void testMissingTerminalQuote() {
        final char quoteChar = this.quoteChar();
        this.parseThrows(quoteChar + "z", QuotedParser.missingTerminatingQuote(quoteChar));
    }

    @Test
    public final void testTerminatedByOther() {
        final char quoteChar = this.quoteChar();
        this.parseThrows("" + quoteChar + 'z' + this.otherQuoteChar(), QuotedParser.missingTerminatingQuote(quoteChar));
    }

    @Test
    public final void testInvalidBackslashEscape() {
        this.quoteParseThrows("ab\\!c", QuotedParser.invalidBackslashEscapeChar('!'));
    }

    @Test
    public final void testInvalidUnicodeEscape() {
        this.quoteParseThrows("ab\\u!c", QuotedParser.invalidUnicodeEscapeChar('!'));
    }

    @Test
    public final void testInvalidUnicodeEscape2() {
        this.quoteParseThrows("ab\\u1!c", QuotedParser.invalidUnicodeEscapeChar('!'));
    }

    @Test
    public final void testInvalidUnicodeEscape3() {
        this.quoteParseThrows("ab\\u12!c", QuotedParser.invalidUnicodeEscapeChar('!'));
    }

    @Test
    public final void testInvalidUnicodeEscape4() {
        this.quoteParseThrows("ab\\u123!c", QuotedParser.invalidUnicodeEscapeChar('!'));
    }

    @Test
    public final void testTerminated() {
        this.quoteParseAndCheck("abc", "abc", "abc");
    }

    @Test
    public final void testTerminated2() {
        final String text = this.quote("abc");
        this.parseAndCheck(text + "xyz", this.createToken("abc", text), text,"xyz");
    }

    @Test
    public final void testIncludeBackslashEscapedNul() {
        this.quoteParseAndCheck("abc\\0def", "abc\0def", "abc\\0def");
    }

    @Test
    public final void testIncludeBackslashEscapedTab() {
        this.quoteParseAndCheck("abc\\tdef", "abc\tdef", "abc\\tdef");
    }

    @Test
    public final void testIncludeBackslashEscapedNewline() {
        this.quoteParseAndCheck("abc\\ndef", "abc\ndef", "abc\\ndef");
    }

    @Test
    public final void testIncludeBackslashEscapedCarriageReturn() {
        this.quoteParseAndCheck("abc\\rdef", "abc\rdef", "abc\\rdef");
    }

    @Test
    public final void testIncludeBackslashEscapedFormFeed() {
        this.quoteParseAndCheck("abc\\fdef", "abc\fdef", "abc\\fdef");
    }

    @Test
    public final void testIncludeBackslashEscapedSingleQuote() {
        this.quoteParseAndCheck("abc\\\'def", "abc\'def", "abc\\\'def");
    }

    @Test
    public final void testIncludeBackslashEscapedDoubleQuote() {
        this.quoteParseAndCheck("abc\\\"def", "abc\"def", "abc\\\"def");
    }

    @Test
    public final void testIncludeUncodeEscaped() {
        this.quoteParseAndCheck("x\\u1234y", "x\u1234y", "x\\u1234y");
    }

    @Test
    public final void testIncludeUncodeEscaped2() {
        this.quoteParseAndCheck("x\\u005Ay", "xZy", "x\\u005Ay");
    }

    @Test
    public final void testManyEscaped() {
        this.quoteParseAndCheck("x\\u005A\\0\\t\\f\\n\\r\\'\\\"y", "xZ\0\t\f\n\r'\"y", "x\\u005A\\0\\t\\f\\n\\r\\'\\\"y");
    }

    private void quoteParseAndCheck(final String in, final String content, final String text) {
        this.quoteParseAndCheck(in, content, text, "");
    }

    private void quoteParseAndCheck(final String in, final String content, final String text, final String textAfter) {
        final String quotedText =  this.quote(text);
        this.parseAndCheck(this.quote(in), this.createToken(content, quotedText), quotedText, textAfter);
    }

    private void quoteParseThrows(final String in, final String messageContains) {
        this.parseThrows(this.quoteChar() + in, messageContains);
    }

    final String quote(final String text) {
        final char quote = this.quoteChar();
        return quote + text + quote;
    }

    abstract char quoteChar();

    abstract char otherQuoteChar();

    abstract T createToken(final String content, final String text);
}
