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

public class AlternativesParserTest extends ParserTemplateTestCase<AlternativesParser<StringParserToken, FakeParserContext>,
        StringParserToken> {

    private final static String TEXT1 = "abc";
    private final static String TEXT2 = "xyz";
    private final static Parser<StringParserToken, FakeParserContext> PARSER1 = Parsers.string(TEXT1);
    private final static Parser<StringParserToken, FakeParserContext> PARSER2 = Parsers.string(TEXT2);

    @Test(expected = NullPointerException.class)
    public void testWithNullParsersFails() {
        AlternativesParser.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroParsersFails() {
        AlternativesParser.with(Lists.empty());
    }

    @Test
    public void testWithOneNeverWrapped() {
        assertSame(PARSER1, AlternativesParser.with(Lists.of(PARSER1)));
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
        this.parseAndCheck(TEXT1,
                string(TEXT1),
                TEXT1,
                "");
    }

    @Test
    public void testSecond() {
        this.parseAndCheck(TEXT2,
                string(TEXT2),
                TEXT2,
                "");
    }

    @Test
    public void testSecondIgnoreExtra() {
        final String extra = "!!!";
        this.parseAndCheck(TEXT2 + extra,
                string(TEXT2),
                TEXT2,
                extra);
    }

    @Test
    public void testOr() {
        final AlternativesParser<StringParserToken, FakeParserContext> parser = createParser();

        final Parser<StringParserToken, FakeParserContext> parser3 = Parsers.string("text3");
        assertEquals(this.createParser0(PARSER1, PARSER2, parser3), parser.or(parser3));
    }
    
    @Test
    public void testToString() {
        assertEquals("(" + PARSER1 + " | " + PARSER2 +")", this.createParser().toString());
    }

    @Override
    protected AlternativesParser<StringParserToken, FakeParserContext> createParser() {
        return this.createParser0(PARSER1, PARSER2);
    }

    private AlternativesParser<StringParserToken, FakeParserContext> createParser0(final Parser<StringParserToken, FakeParserContext>...parsers) {
        return Cast.to(AlternativesParser.with(Lists.of(parsers)));
    }

    private static StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<AlternativesParser<StringParserToken, FakeParserContext>> type() {
        return Cast.to(AlternativesParser.class);
    }
}
