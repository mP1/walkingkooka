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

public final class CommentRemovingHeaderValueParserTest extends HeaderValueParserTestCase<CommentRemovingHeaderValueParser, String> {

    @Test
    public void testUnterminatedCommentFails() {
        this.parseFails("(unclosed", "Missing closing ')' in \"(unclosed\"");
    }

    @Test
    public void testUnterminatedDoubleQuotedTextFails() {
        this.parseMissingClosingQuoteFails("\"abc");
    }

    @Override
    public void testParseEmptyFails() {
    }

    @Test
    public void testEmpty() {
        this.parseAndCheck("");
    }

    @Test
    public void testSpace() {
        this.parseAndCheck(" ");
    }

    @Test
    public void testLetters() {
        this.parseAndCheck("abc");
    }

    @Test
    public void testDigits() {
        this.parseAndCheck("123");
    }

    @Test
    public void testSlash() {
        this.parseAndCheck("/");
    }

    @Test
    public void testWildcard() {
        this.parseAndCheck("*");
    }

    @Test
    public void testMimeType() {
        this.parseAndCheck("text/plain");
    }

    @Test
    public void testMimeTypeStar() {
        this.parseAndCheck("text/*");
    }

    @Test
    public void testMimeTypeTokenSeparator() {
        this.parseAndCheck("text/plain;");
    }

    @Test
    public void testMimeTypeWithParameters() {
        this.parseAndCheck("text/plain;q=0.5");
    }

    @Test
    public void testTokenWithParameters() {
        this.parseAndCheck("abc;q=0.5");
    }

    @Test
    public void testTokenWithManyParameters() {
        this.parseAndCheck("abc;q=0.5,r=2");
    }

    @Test
    public void testDoubleQuotedText() {
        this.parseAndCheck("\"abc123\"");
    }

    @Test
    public void testDoubleQuotedTextWithEscaped() {
        this.parseAndCheck("\"abc\\t123\"");
    }

    @Test
    public void testEmptyComment() {
        this.parseAndCheck("()", "");
    }

    @Test
    public void testComment() {
        this.parseAndCheck("(comment-a1)", "");
    }

    @Test
    public void testCommentComment() {
        this.parseAndCheck("(comment-a1)(comment-b2)", "");
    }

    @Test
    public void testCommentCommentText() {
        this.parseAndCheck("(comment-a1)(comment-b2)abc", "abc");
    }

    @Test
    public void testCommentTextComment() {
        this.parseAndCheck("(comment-a1)bcd(comment-d2)", "bcd");
    }

    @Test
    public void testTextCommentTextComment() {
        this.parseAndCheck("a1(comment-1)b2(comment-2)c3", "a1b2c3");
    }

    @Test
    public void testTextCommentDoubleQuotedText() {
        this.parseAndCheck("a1(comment-1)\"xyz\"", "a1\"xyz\"");
    }

    @Test
    public void testTextCommentDoubleQuotedTextTextSingleQuoted() {
        this.parseAndCheck("a1(comment-1)\"xyz\".'qrs'", "a1\"xyz\".'qrs'");
    }

    @Test
    public void testNoise() {
        this.parseAndCheck("a*1;/=\"xyz\",q", "a*1;/=\"xyz\",q");
    }

    private void parseAndCheck(final String text) {
        this.parseAndCheck(text, text);
    }

    @Override
    public String parse(final String text) {
        return CommentRemovingHeaderValueParser.removeComments(text);
    }

    @Override
    String valueLabel() {
        return "without comments";
    }

    @Override
    public Class<CommentRemovingHeaderValueParser> type() {
        return CommentRemovingHeaderValueParser.class;
    }
}
