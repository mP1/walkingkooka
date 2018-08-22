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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class SequenceParserEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<SequenceParser> {

    private final static Parser<ParserToken, FakeParserContext> PARSER1 = Parsers.string("a").cast();
    private final static Parser<ParserToken, FakeParserContext> PARSER2 = Parsers.string("b").cast();
    private final static Parser<ParserToken, FakeParserContext> PARSER3 = Parsers.string("c").cast();
    
    private final static ParserTokenNodeName NAME1 = ParserTokenNodeName.with(0);
    private final static ParserTokenNodeName NAME2 = ParserTokenNodeName.with(1);
    private final static ParserTokenNodeName NAME3 = ParserTokenNodeName.with(2);

    @Test
    @Ignore
    public void testEqualsOnlyOverridesAbstractOrObject() {
        // nop
    }

    @Test
    public void testEqualWithoutNames() {
        this.checkEquals(SequenceParserBuilder.<FakeParserContext>create()
                .required(PARSER1)
                .required(PARSER2)
                .optional(PARSER3)
                .build());
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(SequenceParserBuilder.<FakeParserContext>create()
                .required(PARSER3, NAME1)
                .required(PARSER2, NAME2)
                .required(PARSER1, NAME3)
                .build());
    }

    @Test
    public void testDifferentRequiredOptionals() {
        this.checkNotEquals(SequenceParserBuilder.<FakeParserContext>create()
                .optional(PARSER1, NAME1)
                .required(PARSER2, NAME2)
                .required(PARSER3, NAME3)
                .build());
    }

    @Test
    public void testEqualsBuiltUsingDefaultMethods() {
        this.checkEquals(PARSER1.builder(NAME1)
                .required(PARSER2.cast(), NAME2)
                .optional(PARSER3.cast(), NAME3)
                .build());
    }

    @Override
    protected SequenceParser createObject() {
        return Cast.to(SequenceParserBuilder.<FakeParserContext>create()
                .required(PARSER1, NAME1)
                .required(PARSER2, NAME2)
                .optional(PARSER3, NAME3)
                .build());
    }
}
