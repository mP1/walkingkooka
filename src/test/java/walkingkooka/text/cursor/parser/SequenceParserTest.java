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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.text.CaseSensitivity;

public final class SequenceParserTest extends ParserTemplateTestCase<SequenceParser<ParserContext>, SequenceParserToken> 
        implements HashCodeEqualsDefinedTesting<SequenceParser<ParserContext>> {

    private final static String TEXT1 = "abc";
    private final static String TEXT2 = "xyz";
    private final static String TEXT3 = "123";
    private final static Parser<ParserToken, ParserContext> PARSER1 = parser(TEXT1);
    private final static Parser<ParserToken, ParserContext> PARSER2 = parser(TEXT2);
    private final static Parser<ParserToken, ParserContext> PARSER3 = parser(TEXT3);
    private final static StringParserToken TOKEN1 = string(TEXT1);
    private final static StringParserToken TOKEN2 = string(TEXT2);
    private final static StringParserToken TOKEN3 = string(TEXT3);
    private final static SequenceParserToken SEQUENCE_MISSING = ParserTokens.sequence(
            Lists.of(TOKEN1, TOKEN2),
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
        this.parseAndCheck(SequenceParserBuilder.empty()
                .optional(PARSER3)
                .required(PARSER2)
                .required(PARSER1)
                .build(),
                this.createContext(),
                text,
                ParserTokens.sequence(Lists.of(TOKEN2, TOKEN1), text),
                text);
    }

    @Test
    public void testAllOptionalFail() {
        this.parseFailAndCheck(SequenceParserBuilder.empty()
                        .optional(PARSER3)
                        .optional(PARSER2)
                        .optional(PARSER1)
                        .build(),
                "!@#");
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
    public void testEqualWithoutNames() {
        this.checkEquals(SequenceParserBuilder.<ParserContext>empty()
                .required(PARSER1)
                .required(PARSER2)
                .optional(PARSER3)
                .build());
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(SequenceParserBuilder.<ParserContext>empty()
                .required(PARSER3)
                .required(PARSER2)
                .required(PARSER1)
                .build());
    }

    @Test
    public void testEqualsDifferentRequiredOptionals() {
        this.checkNotEquals(SequenceParserBuilder.<ParserContext>empty()
                .optional(PARSER1)
                .required(PARSER2)
                .required(PARSER3)
                .build());
    }

    @Test
    public void testEqualsBuiltUsingDefaultMethods() {
        this.checkEquals(PARSER1.builder()
                .required(PARSER2.cast())
                .optional(PARSER3.cast())
                .build());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createParser(),
                "(" + PARSER1 + ", " + PARSER2 + ", [" + PARSER3 + "])");
    }

    @Override
    protected SequenceParser<ParserContext> createParser() {
        return Cast.to(SequenceParserBuilder.<ParserContext>empty()
                .required(PARSER1)
                .required(PARSER2)
                .optional(PARSER3)
                .build());
    }

    private static Parser<ParserToken, ParserContext> parser(final String string) {
        return CaseSensitivity.SENSITIVE.parser(string).cast();
    }

    private static StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    public Class<SequenceParser<ParserContext>> type() {
        return Cast.to(SequenceParser.class);
    }

    @Override
    public SequenceParser<ParserContext> createObject() {
        return this.createParser();
    }
}
