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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;
import walkingkooka.text.CaseSensitivity;

public final class SequenceParserEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<SequenceParser> {

    private final static Parser<ParserToken, FakeParserContext> PARSER1 = CaseSensitivity.SENSITIVE.parser("a").cast();
    private final static Parser<ParserToken, FakeParserContext> PARSER2 = CaseSensitivity.SENSITIVE.parser("b").cast();
    private final static Parser<ParserToken, FakeParserContext> PARSER3 = CaseSensitivity.SENSITIVE.parser("c").cast();

    @Test
    public void testEqualWithoutNames() {
        this.checkEquals(SequenceParserBuilder.<FakeParserContext>empty()
                .required(PARSER1)
                .required(PARSER2)
                .optional(PARSER3)
                .build());
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(SequenceParserBuilder.<FakeParserContext>empty()
                .required(PARSER3)
                .required(PARSER2)
                .required(PARSER1)
                .build());
    }

    @Test
    public void testDifferentRequiredOptionals() {
        this.checkNotEquals(SequenceParserBuilder.<FakeParserContext>empty()
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

    @Override
    protected SequenceParser createObject() {
        return Cast.to(SequenceParserBuilder.<FakeParserContext>empty()
                .required(PARSER1)
                .required(PARSER2)
                .optional(PARSER3)
                .build());
    }
}
