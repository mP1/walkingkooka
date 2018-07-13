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

public final class SequenceParserTest extends ParserTemplateTestCase<SequenceParser<TestParserContext>,
        SequenceParserToken> {

    private final static String TEXT1 = "abc";
    private final static String TEXT2 = "xyz";
    private final static String TEXT3 = "123";
    private final static Parser<StringParserToken, TestParserContext> PARSER1 = Parsers.string(TEXT1);
    private final static Parser<StringParserToken, TestParserContext> PARSER2 = Parsers.string(TEXT2);
    private final static Parser<StringParserToken, TestParserContext> PARSER3 = Parsers.string(TEXT3);
    private final static StringParserToken TOKEN1 = string(TEXT1);
    private final static StringParserToken TOKEN2 = string(TEXT2);
    private final static StringParserToken TOKEN3 = string(TEXT3);
    private final static MissingParserToken MISSING3 = ParserTokens.missing(StringParserToken.NAME, "");
    private final static SequenceParserToken SEQUENCE_MISSING = ParserTokens.sequence(
            Lists.of(TOKEN1, TOKEN2, MISSING3),
            TEXT1 + TEXT2);
    private final static SequenceParserToken SEQUENCE_TOKEN3 = ParserTokens.sequence(
            Lists.of(TOKEN1, TOKEN2, TOKEN3),
            TEXT1 + TEXT2 + TEXT3);

    @Test
    public void testNone() {
        this.parseFailAndCheck("a");
    }

    @Test
    public void testNone2() {
        this.parseFailAndCheck("ab");
    }

    @Test
    public void testMissingRequired() {
        this.parseFailAndCheck(TEXT1);
    }

    @Test
    public void testMissingRequired2() {
        this.parseFailAndCheck(TEXT1 + "x");
    }

    @Test
    public void testMissingRequired3() {
        this.parseFailAndCheck(TEXT1 + "xy");
    }

    @Test
    public void testMissingRequired4() {
        this.parseFailAndCheck(TEXT1 + TEXT3);
    }

    @Test
    public void testMissingOptionalFirst() {
        final String text = TEXT2 + TEXT1;
        this.parseAndCheck(SequenceParserBuilder.create()
                .optional(PARSER3, StringParserToken.NAME)
                .required(PARSER2, StringParserToken.NAME)
                .required(PARSER1, StringParserToken.NAME)
                .build(),
                this.createContext(),
                text,
                ParserTokens.sequence(Lists.of(MISSING3, TOKEN2, TOKEN1), text),
                text);
    }

    @Test
    public void testOutOfOrder() {
        this.parseFailAndCheck(TEXT2 + TEXT1);
    }

    @Test
    public void testOutOfOrder2() {
        this.parseFailAndCheck(TEXT3 + TEXT2 + TEXT1);
    }

    @Test
    public void testAllRequiredMissingOptional() {
        final String text = TEXT1 + TEXT2;
        this.parseAndCheck(text, SEQUENCE_MISSING, text, "");
    }

    @Test
    public void testAllRequiredMissingOptional2() {
        final String text = TEXT1 + TEXT2;
        final String textAfter = "...";
        this.parseAndCheck(text + textAfter,
                SEQUENCE_MISSING,
                text,
                textAfter);
    }

    @Test
    public void testAllRequiredMissingOptional3() {
        final String text = TEXT1 + TEXT2;
        final String textAfter = "12";
        this.parseAndCheck(text + textAfter,
                SEQUENCE_MISSING,
                text,
                textAfter);
    }

    @Test
    public void testAllRequiredAndOptional() {
        final String text = TEXT1 + TEXT2 + TEXT3;
        this.parseAndCheck(text,
                SEQUENCE_TOKEN3,
                text,
                "");
    }

    @Test
    public void testAllRequiredAndOptional2() {
        final String text = TEXT1 + TEXT2 + TEXT3;
        final String textAfter = "...";
        this.parseAndCheck(text + textAfter,
                SEQUENCE_TOKEN3,
                text,
                textAfter);
    }

    @Test
    public void testToString() {
        assertEquals(PARSER1 + ", " + PARSER2 + ", " + PARSER3 + "?", this.createParser().toString());
    }

    @Override
    protected SequenceParser<TestParserContext> createParser() {
        return SequenceParserBuilder.create()
                .required(PARSER1, StringParserToken.NAME)
                .required(PARSER2, StringParserToken.NAME)
                .optional(PARSER3, StringParserToken.NAME)
                .build();
    }

    private static StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<SequenceParser<TestParserContext>> type() {
        return Cast.to(SequenceParser.class);
    }
}
