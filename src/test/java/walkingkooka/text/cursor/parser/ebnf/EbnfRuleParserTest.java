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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.Test;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

public final class EbnfRuleParserTest extends EbnfParserTestCase2<EbnfRuleParserToken> {

    @Test
    public void testIdentifierOnlyFails() {
        this.parseFailAndCheck(IDENTIFIER1);
    }

    @Test
    public void testIdentifierAndAssignmentOnlyFails() {
        this.parseFailAndCheck(IDENTIFIER1 + ASSIGNMENT);
    }

    @Test
    public void testAssignmentOnlyFails() {
        this.parseFailAndCheck(ASSIGNMENT);
    }

    @Test
    public void testDoubleTerminalFails() {
        this.parseFailAndCheck(IDENTIFIER1 + ASSIGNMENT + TERMINAL1_TEXT + ASSIGNMENT + TERMINAL1_TEXT);
    }

    @Test
    public void testWhitespaceAssignment() {
        final String text = IDENTIFIER1 + WHITESPACE1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), whitespace1(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testCommentAssignment() {
        final String text = IDENTIFIER1 + COMMENT1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), comment1(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testWhitespaceCommentAssignment() {
        final String text = IDENTIFIER1 + WHITESPACE1 + COMMENT1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), whitespace1(), comment1(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testWhitespaceCommentWhitespaceAssignment() {
        final String text = IDENTIFIER1 + WHITESPACE1 + COMMENT1 + WHITESPACE2 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), whitespace1(), comment1(), whitespace2(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }
    
    // assignment

    @Test
    public void testeAssignmentWhitespac() {
        final String text = IDENTIFIER1 + ASSIGNMENT + WHITESPACE1 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), whitespace1(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testeAssignmentComment() {
        final String text = IDENTIFIER1 + ASSIGNMENT + COMMENT1 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), comment1(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testAssignmentWhitespaceComment() {
        final String text = IDENTIFIER1 + ASSIGNMENT + WHITESPACE1 + COMMENT1 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), whitespace1(), comment1(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testAssignmentWhitespaceCommentWhitespace() {
        final String text = IDENTIFIER1 +  ASSIGNMENT + WHITESPACE1 + COMMENT1 + WHITESPACE2 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), whitespace1(), comment1(), whitespace2(), terminal1(), terminatorToken()),
                text);
    }
    
    // terminator

    @Test
    public void testeTokenWhitespace() {
        final String text = IDENTIFIER1 + ASSIGNMENT + TERMINAL1_TEXT + WHITESPACE1 + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), whitespace1(), terminatorToken()),
                text);
    }

    @Test
    public void testeTokenComment() {
        final String text = IDENTIFIER1 + ASSIGNMENT + TERMINAL1_TEXT + COMMENT1 + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), comment1(), terminatorToken()),
                text);
    }

    @Test
    public void testTokenWhitespaceComment() {
        final String text = IDENTIFIER1 + ASSIGNMENT + TERMINAL1_TEXT + WHITESPACE1 + COMMENT1 + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), whitespace1(), comment1(), terminatorToken()),
                text);
    }

    @Test
    public void testTokenWhitespaceCommentWhitespace() {
        final String text = IDENTIFIER1 +  ASSIGNMENT + TERMINAL1_TEXT + WHITESPACE1 + COMMENT1 + WHITESPACE2 +TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), whitespace1(), comment1(), whitespace2(), terminatorToken()),
                text);
    }

    @Test
    public void testTerminatorWhitespace() {
        final String text = IDENTIFIER1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text + WHITESPACE1,
                rule(text, identifier1(), assignmentToken(), terminal1(), terminatorToken()),
                text,
                WHITESPACE1);
    }

    @Test
    public void testTerminatorComments() {
        final String text = IDENTIFIER1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text + COMMENT1,
                rule(text, identifier1(), assignmentToken(), terminal1(), terminatorToken()),
                text,
                COMMENT1);
    }

    @Override
    protected Parser<ParserToken, EbnfParserContext> createParser() {
        return EbnfGrammarParser.RULE;
    }

    @Override
    String text() {
        return "identifier123=\"terminal456\";";
    }

    @Override
    EbnfRuleParserToken token(final String text) {
        return rule(text,
                new EbnfIdentifierParserToken(IDENTIFIER1, IDENTIFIER1),
                assignmentToken(),
                new EbnfTerminalParserToken(TERMINAL1, TERMINAL1_TEXT),
                terminatorToken());
    }
}
