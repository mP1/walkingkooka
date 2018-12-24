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
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public final class EbnfGrammarParserTokenInvalidReferencesExceptionTest extends ClassTestCase<EbnfGrammarParserTokenInvalidReferencesException> {

    @Test(expected = NullPointerException.class)
    public void testWithNullReferencesFails() {
        new EbnfGrammarParserTokenInvalidReferencesException("message", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEMptyReferencesFails() {
        new EbnfGrammarParserTokenInvalidReferencesException("message", Sets.empty());
    }

    @Test
    public void testWith() {
        final String message = "abc";
        final Set<EbnfIdentifierName> references = this.references();
        final EbnfGrammarParserTokenInvalidReferencesException exception = new EbnfGrammarParserTokenInvalidReferencesException(message, references);
        assertEquals("message", message, exception.getMessage());
        assertEquals("references", references, exception.references());
    }

    @Test
    public void testToString() {
        assertEquals("Unknown references=[abc]", new EbnfGrammarParserTokenInvalidReferencesException("message 123", this.references()).toString());
    }

    private Set<EbnfIdentifierName> references() {
        return Sets.of(EbnfIdentifierName.with("abc"));
    }

    @Override
    protected Class<EbnfGrammarParserTokenInvalidReferencesException> type() {
        return EbnfGrammarParserTokenInvalidReferencesException.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
