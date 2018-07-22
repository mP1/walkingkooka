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
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

public final class EbnfExceptionParserTest extends EbnfParserTestCase3<EbnfExceptionParserToken> {

    @Test
    public void testExceptionFails() {
        this.parseFailAndCheck(EXCEPTION);
    }
    
    @Test
    public void testExceptionWhitespaceIdentifier() {
        final String text = EXCEPTION + WHITESPACE1 + IDENTIFIER1;
        this.parseAndCheck(text,
                this.token(text, this.exceptionToken(), whitespace1(), this.identifier1()),
                text);
    }

    @Test
    public void testExceptionWhitespaceIdentifierWhitespace() {
        final String text = EXCEPTION + WHITESPACE1 + IDENTIFIER1;
        this.parseAndCheck(text+ WHITESPACE2,
                this.token(text, this.exceptionToken(), whitespace1(), this.identifier1()),
                text,
                WHITESPACE2);
    }

    @Test
    public void testExceptionCommentIdentifier() {
        final String text = EXCEPTION + COMMENT1 + IDENTIFIER1;
        this.parseAndCheck(text,
                this.token(text, this.exceptionToken(), comment1(), this.identifier1()),
                text);
    }

    @Test
    public void testExceptionCommentIdentifierComment() {
        final String text = EXCEPTION + COMMENT1 + IDENTIFIER1;
        this.parseAndCheck(text + COMMENT2,
                this.token(text, this.exceptionToken(), comment1(), this.identifier1()),
                text,
                COMMENT2);
    }

    @Test
    public void testExceptionWhitespaceIdentifierComment() {
        final String text = EXCEPTION + WHITESPACE1 + IDENTIFIER1;
        this.parseAndCheck(text + COMMENT2,
                this.token(text, this.exceptionToken(), whitespace1(), this.identifier1()),
                text,
                COMMENT2);
    }

    @Override
    String text() {
        return EXCEPTION + IDENTIFIER1;
    }
    
    @Override
    protected Parser<ParserToken, EbnfParserContext> createParser() {
        return EbnfGrammarParser.EXCEPTION;
    }

    @Override
    final EbnfExceptionParserToken token(final String text) {
        return token(text, this.exceptionToken(), this.identifier1());
    }

    private EbnfExceptionParserToken token(final String text, final EbnfParserToken...tokens) {
        return EbnfParserToken.exception(Lists.of(tokens), text);
    }
}
