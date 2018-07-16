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
import walkingkooka.build.BuilderException;
import walkingkooka.build.BuilderTestCase;

public final class SequenceParserBuilderTest extends BuilderTestCase<SequenceParserBuilder<FakeParserContext>, Parser<SequenceParserToken, FakeParserContext>> {

    private final static Parser<StringParserToken, FakeParserContext> PARSER1 = Parsers.string("1");
    private final static Parser<StringParserToken, FakeParserContext> PARSER2 = Parsers.string("2");
    private final static Parser<StringParserToken, FakeParserContext> PARSER3 = Parsers.string("3");

    @Test(expected = NullPointerException.class)
    public void testOptionalNullParserFails() {
        this.createBuilder().optional(null, StringParserToken.NAME);
    }

    @Test(expected = NullPointerException.class)
    public void testOptionalNullNameFails() {
        this.createBuilder().optional(PARSER1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRequiredNullParserFails() {
        this.createBuilder().required(null, StringParserToken.NAME);
    }

    @Test(expected = NullPointerException.class)
    public void testRequiredNullNameFails() {
        this.createBuilder().required(PARSER1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionalInvalidIndexName() {
        this.createBuilder()
                .optional(PARSER1, ParserTokenNodeName.with(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredInvalidIndexName() {
        this.createBuilder()
                .required(PARSER1, ParserTokenNodeName.with(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionalInvalidIndexName2() {
        this.createBuilder()
                .optional(PARSER1, ParserTokenNodeName.with(0))
                .optional(PARSER1, ParserTokenNodeName.with(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredInvalidIndexName2() {
        this.createBuilder()
                .required(PARSER1, ParserTokenNodeName.with(0))
                .required(PARSER1, ParserTokenNodeName.with(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionalInvalidIndexName3() {
        this.createBuilder()
                .optional(PARSER1)
                .optional(PARSER1, ParserTokenNodeName.with(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequiredInvalidIndexName3() {
        this.createBuilder()
                .required(PARSER1)
                .required(PARSER1, ParserTokenNodeName.with(2));
    }

    @Test(expected = BuilderException.class)
    public void testBuildOneParserFails() {
        this.createBuilder()
                .optional(PARSER1, StringParserToken.NAME)
                .build();
    }

    @Test
    public void testMoreThanTwoParsers(){
        SequenceParserBuilder.create()
                .optional(PARSER1, StringParserToken.NAME)
                .required(PARSER2, StringParserToken.NAME)
                .build();
    }

    @Test
    public void testMoreThanTwoParsers2(){
        SequenceParserBuilder.create()
                .optional(PARSER1)
                .required(PARSER2)
                .build();
    }

    @Test
    public void testParserBuilder() {
        PARSER1.builder(StringParserToken.NAME)
                .optional(PARSER2.castC(), StringParserToken.NAME)
                .build();
    }

    @Test
    public void testToString() {
        assertEquals(PARSER1 + "?, " + PARSER2 + ", " + PARSER3,
                SequenceParserBuilder.create()
                        .optional(PARSER1, StringParserToken.NAME)
                        .required(PARSER2, StringParserToken.NAME)
                        .required(PARSER3, StringParserToken.NAME)
                        .build().toString());
    }

    @Override
    protected SequenceParserBuilder<FakeParserContext> createBuilder() {
        return SequenceParserBuilder.create();
    }
}
