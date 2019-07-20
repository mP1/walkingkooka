/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ThrowableTesting;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EbnfParserCombinatorDuplicateRuleExceptionTest implements ClassTesting2<EbnfParserCombinatorDuplicateRuleException>,
        ThrowableTesting {

    @Override
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWithNullMessageFails() {
        assertThrows(NullPointerException.class, () -> {
            new EbnfParserCombinatorDuplicateRuleException(null, this.duplicate());
        });
    }

    @Test
    public void testWithNullDuplicateRuleFails() {
        assertThrows(NullPointerException.class, () -> {
            new EbnfParserCombinatorDuplicateRuleException(this.message(), null);
        });
    }

    @Test
    public void testWith() {
        final String message = this.message();
        final EbnfRuleParserToken duplicate = this.duplicate();

        final EbnfParserCombinatorDuplicateRuleException exception = new EbnfParserCombinatorDuplicateRuleException(message, duplicate);

        checkMessage(exception, message);
        checkCause(exception, null);
        assertEquals(duplicate, exception.duplicate(), "duplicate");
    }

    private String message() {
        return "message 123";
    }

    private EbnfRuleParserToken duplicate() {
        return EbnfParserToken.rule(Lists.of(this.name(), EbnfParserToken.terminal("text", "text")), "xyz: \"text\"");
    }

    private EbnfIdentifierParserToken name() {
        final String text = "xyz";
        return EbnfParserToken.identifier(EbnfIdentifierName.with(text), text);
    }

    @Override
    public Class<EbnfParserCombinatorDuplicateRuleException> type() {
        return EbnfParserCombinatorDuplicateRuleException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
