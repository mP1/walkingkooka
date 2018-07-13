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

public final class SequenceParserBuilderTest extends BuilderTestCase<SequenceParserBuilder<TestParserContext>, SequenceParser<TestParserContext>> {

    private final static Parser<StringParserToken, TestParserContext> PARSER1 = Parsers.string("1");
    private final static Parser<StringParserToken, TestParserContext> PARSER2 = Parsers.string("2");
    private final static Parser<StringParserToken, TestParserContext> PARSER3 = Parsers.string("3");

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
    public void testToString() {
        assertEquals(PARSER1 + "?, " + PARSER2 + ", " + PARSER3,
                SequenceParserBuilder.create()
                        .optional(PARSER1, StringParserToken.NAME)
                        .required(PARSER2, StringParserToken.NAME)
                        .required(PARSER3, StringParserToken.NAME)
                        .build().toString());
    }

    @Override
    protected SequenceParserBuilder<TestParserContext> createBuilder() {
        return SequenceParserBuilder.create();
    }
}
