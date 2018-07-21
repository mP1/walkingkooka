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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;

import static org.junit.Assert.assertNotSame;

public class OptionalParserTest extends ParserTestCase2<OptionalParser<FakeParserContext>, ParserToken> {

    private final static Parser<StringParserToken, FakeParserContext> PARSER = Parsers.stringCharPredicate(CharPredicates.digit(), 1, 10);
    private final static ParserTokenNodeName NAME = StringParserToken.NAME;
    private final static ParserToken MISSING = ParserTokens.missing(NAME, "");

    @Test
    @Ignore
    public void testEmptyCursorFail() {
        // ignore
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParserFails() {
        OptionalParser.with(null, NAME);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullNameFails() {
        OptionalParser.with(PARSER, null);
    }

    @Test
    public void testFailure() {
        this.parseAndCheck("a", MISSING, "", "a");
    }

    @Test
    public void testFailure2() {
        this.parseAndCheck("abc", MISSING, "", "abc");
    }

    @Test
    public void testSuccess() {
        this.parseAndCheck2("1", "");
    }

    @Test
    public void testSuccess2() {
        this.parseAndCheck2("123", "");
    }

    @Test
    public void testSuccess3() {
        this.parseAndCheck2("12345", "abc");
    }

    private void parseAndCheck2(final String text, final String textAfter) {
        this.parseAndCheck(text + textAfter, ParserTokens.string(text, text), text, textAfter);
    }

    @Test
    public void testOptionalSameName() {
        final OptionalParser<FakeParserContext> parser = this.createParser();
        assertSame(parser, parser.optional(NAME));
    }

    @Test
    public void testOptionalDifferentName() {
        final OptionalParser<FakeParserContext> parser = this.createParser();

        final ParserTokenNodeName different = NumberParserToken.NAME;
        final OptionalParser<FakeParserContext> parser2 = parser.optional(different);

        assertNotSame(parser, parser2);

        final String text = "abc";
        this.parseAndCheck(parser2,
                text,
                ParserTokens.missing(different, ""),
                "",
                text);
    }

    @Test
    public void testToString() {
        assertEquals("[" + PARSER + "]", this.createParser().toString());
    }

    @Override
    protected OptionalParser<FakeParserContext> createParser() {
        return OptionalParser.with(PARSER, NAME);
    }

    @Override
    protected Class<OptionalParser<FakeParserContext>> type() {
        return Cast.to(OptionalParser.class);
    }
}
