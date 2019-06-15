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

package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EbnfGrammarParserTokenDuplicateIdentifiersExceptionTest implements ClassTesting2<EbnfGrammarParserTokenDuplicateIdentifiersException>,
        ToStringTesting<EbnfGrammarParserTokenDuplicateIdentifiersException> {

    @Test
    public void testWithNullDuplicatesFails() {
        assertThrows(NullPointerException.class, () -> {
            new EbnfGrammarParserTokenDuplicateIdentifiersException("message 123", null);
        });
    }

    @Test
    public void testWithEmptyDuplicatesFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            new EbnfGrammarParserTokenDuplicateIdentifiersException("message 123", Sets.empty());
        });
    }

    @Test
    public void testWith() {
        final String message = "message 123";
        final Set<EbnfRuleParserToken> duplicates = this.duplicates();
        final EbnfGrammarParserTokenDuplicateIdentifiersException exception = new EbnfGrammarParserTokenDuplicateIdentifiersException(message, duplicates);
        assertEquals(message, exception.getMessage(), "message");
        assertEquals(duplicates, exception.duplicates(), "duplicates");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(new EbnfGrammarParserTokenDuplicateIdentifiersException("abc 123", this.duplicates()),
                "Unknown duplicates=[abc]");
    }

    private Set<EbnfRuleParserToken> duplicates() {
        return Sets.of(
                EbnfRuleParserToken.with(Lists.of(EbnfParserToken.identifier(EbnfIdentifierName.with("abc"), "abc"), EbnfParserToken.terminal("def", "def")),
                        "abc"));
    }

    @Override
    public Class<EbnfGrammarParserTokenDuplicateIdentifiersException> type() {
        return EbnfGrammarParserTokenDuplicateIdentifiersException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
