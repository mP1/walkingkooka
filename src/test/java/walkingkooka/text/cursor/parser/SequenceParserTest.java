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

public final class SequenceParserTest extends ParserTemplateTestCase<SequenceParser<StringParserToken, TestParserContext>,
        SequenceParserToken<StringParserToken>> {

    private final static String TEXT1 = "abc";
    private final static String TEXT2 = "xyz";
    private final static Parser<StringParserToken, TestParserContext> PARSER1 = Parsers.string(TEXT1);
    private final static Parser<StringParserToken, TestParserContext> PARSER2 = Parsers.string(TEXT2);

    @Test(expected = NullPointerException.class)
    public void testWithNullParsersFails() {
        SequenceParser.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroParsersFails() {
        SequenceParser.with(Lists.empty());
    }

    @Test
    public void testWithOneNeverWrapped() {
        assertSame(PARSER1, SequenceParser.with(Lists.of(PARSER1)));
    }

    @Test
    public void testNone() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testNone2() {
        this.parseFailAndCheck("ab");
    }

    @Test
    public void testFirst() {
        this.parseFailAndCheck(TEXT1);
    }

    @Test
    public void testFirstIncompleteSecond() {
        this.parseFailAndCheck(TEXT1 + "xy");
    }

    @Test
    public void testOutOfOrder() {
        this.parseFailAndCheck(TEXT2 + TEXT1);
    }

    @Test
    public void testSecondEoc() {
        this.twoTokensAndCheck("");
    }

    @Test
    public void testSecondIgnoresExtra() {
        this.twoTokensAndCheck("mnop");
    }

    private void twoTokensAndCheck(final String textAfter) {
        final String text = TEXT1 + TEXT2;

        this.parseAndCheck(text + textAfter,
                SequenceParserToken.with(Lists.of(string(TEXT1), string(TEXT2)), text),
                text,
                textAfter);
    }

    @Test
    public void testToString() {
        assertEquals(PARSER1 + ", " + PARSER2, this.createParser().toString());
    }

    @Override
    protected SequenceParser<StringParserToken, TestParserContext> createParser() {
        return Cast.to(SequenceParser.with(Lists.of(PARSER1, PARSER2)));
    }

    private static StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<SequenceParser<StringParserToken, TestParserContext>> type() {
        return Cast.to(SequenceParser.class);
    }
}
