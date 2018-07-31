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

public final class EbnfExceptionParserTest extends EbnfParserTestCase2<EbnfExceptionParserToken> {

    @Test
    public void testExceptionFails() {
        this.parseFailAndCheck(EXCEPTION);
    }

    @Test
    public void testCommentIdentifierExceptionIdentifier() {
        final String text = COMMENT1 + IDENTIFIER1 + EXCEPTION + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, comment1(), this.identifier1(), this.exceptionToken(), this.identifier2()),
                text);
    }

    @Test
    public void testWhitespaceIdentifierExceptionIdentifier() {
        final String text = WHITESPACE1 + IDENTIFIER1 + EXCEPTION + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, whitespace1(), this.identifier1(), this.exceptionToken(), this.identifier2()),
                text);
    }
    
    @Test
    public void testIdentifierExceptionWhitespaceIdentifier() {
        final String text = IDENTIFIER1 + EXCEPTION + WHITESPACE1 + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, identifier1(), this.exceptionToken(), whitespace1(), this.identifier2()),
                text);
    }

    @Test
    public void testIdentifierExceptionCommentIdentifier() {
        final String text = IDENTIFIER1 + EXCEPTION + COMMENT1 + IDENTIFIER2;
        this.parseAndCheck(text,
                this.token(text, identifier1(), this.exceptionToken(), comment1(), this.identifier2()),
                text);
    }

    @Test
    public void testIdentifierExceptionIdentifierWhitespace() {
        final String text = IDENTIFIER1 + EXCEPTION + IDENTIFIER2;
        this.parseAndCheck(text + WHITESPACE1,
                this.token(text, identifier1(), this.exceptionToken(), this.identifier2()),
                text,
                WHITESPACE1);
    }

    @Test
    public void testIdentifierExceptionIdentifierComment() {
        final String text = IDENTIFIER1 + EXCEPTION + IDENTIFIER2;
        this.parseAndCheck(text + COMMENT1,
                this.token(text, identifier1(), this.exceptionToken(), this.identifier2()),
                text,
                COMMENT1);
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

    private EbnfExceptionParserToken token(final String text, final ParserToken...tokens) {
        return EbnfParserToken.exception(Lists.of(tokens), text);
    }
}
