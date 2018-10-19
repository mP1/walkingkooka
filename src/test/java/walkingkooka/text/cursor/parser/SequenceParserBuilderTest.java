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
import walkingkooka.build.BuilderTestCase;
import walkingkooka.text.CaseSensitivity;

import static org.junit.Assert.assertEquals;

public final class SequenceParserBuilderTest extends BuilderTestCase<SequenceParserBuilder<FakeParserContext>, Parser<SequenceParserToken, FakeParserContext>> {

    private final static Parser<ParserToken, FakeParserContext> PARSER1 = parser("1");
    private final static Parser<ParserToken, FakeParserContext> PARSER2 = parser("2");
    private final static Parser<ParserToken, FakeParserContext> PARSER3 = parser("3");

    @Test(expected = NullPointerException.class)
    public void testOptionalNullParserFails() {
        this.createBuilder().optional(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRequiredNullParserFails() {
        this.createBuilder().required(null);
    }

    @Test
    public void testMoreThanTwoParsers(){
        this.createBuilder()
                .optional(PARSER1)
                .required(PARSER2)
                .build();
    }

    @Test
    public void testMoreThanTwoParsers2(){
        this.createBuilder()
                .optional(PARSER1)
                .required(PARSER2)
                .build();
    }

    @Test
    public void testParserBuilder() {
        PARSER1.builder()
                .optional(PARSER2.cast())
                .build();
    }

    @Test
    public void testToString() {
        assertEquals("([" + PARSER1 + "], " + PARSER2 + ", " + PARSER3 + ")",
                this.createBuilder()
                        .optional(PARSER1)
                        .required(PARSER2)
                        .required(PARSER3)
                        .build().toString());
    }

    @Override
    protected SequenceParserBuilder<FakeParserContext> createBuilder() {
        return SequenceParserBuilder.create();
    }

    private static Parser<ParserToken, FakeParserContext> parser(final String string) {
        return CaseSensitivity.SENSITIVE.parser(string).cast();
    }

    @Override
    protected Class<SequenceParserBuilder> type() {
        return Cast.to(SequenceParserBuilder.class);
    }

    @Override
    protected Class<Parser<SequenceParserToken, FakeParserContext>> builderProductType() {
        return Cast.to(SequenceParserToken.class);
    }
}
