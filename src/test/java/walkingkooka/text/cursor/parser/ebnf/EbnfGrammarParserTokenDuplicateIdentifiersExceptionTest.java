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
 *
 */

package walkingkooka.text.cursor.parser.ebnf;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public final class EbnfGrammarParserTokenDuplicateIdentifiersExceptionTest extends ClassTestCase<EbnfGrammarParserTokenDuplicateIdentifiersException> {

    @Test(expected = NullPointerException.class)
    public void testWithNullDuplicatesFails() {
        new EbnfGrammarParserTokenDuplicateIdentifiersException("message 123", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyDuplicatesFails() {
        new EbnfGrammarParserTokenDuplicateIdentifiersException("message 123", Sets.empty());
    }

    @Test
    public void testWith() {
        final String message = "message 123";
        final Set<EbnfRuleParserToken> duplicates = this.duplicates();
        final EbnfGrammarParserTokenDuplicateIdentifiersException exception = new EbnfGrammarParserTokenDuplicateIdentifiersException(message, duplicates);
        assertEquals("message", message, exception.getMessage());
        assertEquals("duplicates", duplicates, exception.duplicates());
    }

    @Test
    public void testToString() {
        assertEquals("Unknown duplicates=[abc]", new EbnfGrammarParserTokenDuplicateIdentifiersException("abc 123", this.duplicates()).toString());
    }

    private Set<EbnfRuleParserToken> duplicates() {
        return Sets.of(
                EbnfRuleParserToken.with(Lists.of(EbnfParserToken.identifier(EbnfIdentifierName.with("abc"), "abc"), EbnfParserToken.terminal("def", "def")),
                        "abc"));
    }

    @Override
    protected Class<EbnfGrammarParserTokenDuplicateIdentifiersException> type() {
        return EbnfGrammarParserTokenDuplicateIdentifiersException.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
