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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursors;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class RepeatedParserTest extends ParserTemplateTestCase<RepeatedParser<ParserContext>,
        RepeatedParserToken> {

    private final static String TEXT = "abc";
    private final static Parser<ParserToken, ParserContext> PARSER = CaseSensitivity.SENSITIVE.parser(TEXT).cast();

    @Test(expected = NullPointerException.class)
    public void testWithNullParserFails() {
        RepeatedParser.with(null);
    }

    @Test
    public void testWrapAnotherRepeatedParser() {
        final RepeatedParser<ParserContext> parser = this.createParser();
        assertSame(parser, Parsers.repeated(parser.cast()));
    }

    @Test
    public void testIncomplete() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testIncomplete2() {
        this.parseFailAndCheck("ab");
    }

    public void testSkipsMissing() {
        this.parseFailAndCheck(RepeatedParser.with(Parsers.fixed(Optional.of(ParserTokens.missing(ParserTokenNodeName.with("Missing"), "Missing")))), "ABC");
    }

    @Test
    public void testOnce() {
        this.parseAndCheck(TEXT,
                RepeatedParserToken.with(Lists.of(string(TEXT)), TEXT),
                TEXT,
                "");
    }

    @Test
    public void testTwiceDifferentTokens() {
        final String text1 = "'123'";
        final String text2 = "'4'";
        final String all = text1 + text2;

        this.parseAndCheck(
                RepeatedParser.with(Parsers.singleQuoted().cast()),
                this.createContext(),
                TextCursors.charSequence(all),
                RepeatedParserToken.with(Lists.of(quoted(text1), quoted(text2)), all),
                all,
                "");
    }

    @Test
    public void testMultipleTokensAndTextAfter() {
        final String text1 = "'123'";
        final String text2 = "'4'";
        final String all = text1 + text2;
        final String after = "!!!";

        this.parseAndCheck(
                RepeatedParser.with(Parsers.singleQuoted().cast()),
                this.createContext(),
                TextCursors.charSequence(all + after),
                RepeatedParserToken.with(Lists.of(quoted(text1), quoted(text2)), all),
                all,
                after);
    }

    private static SingleQuotedParserToken quoted(final String text) {
        return ParserTokens.singleQuoted(text.substring(1, text.length() -1), text);
    }

    @Test
    public void testUntilUnmatched() {
        this.parseAndCheck(TEXT + "!!",
                RepeatedParserToken.with(Lists.of(string(TEXT)), TEXT),
                TEXT,
                "!!");
    }

    @Test
    public void testRepeating2() {
        final RepeatedParser<ParserContext> parser = this.createParser();
        assertSame(parser, parser.repeating());
    }

    @Test
    public void testToString() {
        assertEquals("{" + PARSER + "}", this.createParser().toString());
    }

    @Override
    protected RepeatedParser<ParserContext> createParser() {
        return RepeatedParser.with(PARSER);
    }

    private static StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    private static String repeat(final String TEXT, final int count) {
        final StringBuilder b = new StringBuilder();

        for(int i = 0; i < count; i++) {
            b.append(TEXT);
        }

        return b.toString();
    }

    @Override
    protected Class<RepeatedParser<ParserContext>> type() {
        return Cast.to(RepeatedParser.class);
    }
}
