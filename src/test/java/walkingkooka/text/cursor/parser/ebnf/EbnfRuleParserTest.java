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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;

public final class EbnfRuleParserTest extends EbnfParserTestCase2<EbnfRuleParserToken> {

    @Test
    public void testIdentifierOnlyFails() {
        this.parseFailAndCheck(IDENTIFIER1_TEXT);
    }

    @Test
    public void testIdentifierAndAssignmentOnlyFails() {
        this.parseFailAndCheck(IDENTIFIER1_TEXT +ASSIGNMENT);
    }

    @Test
    public void testAssignmentOnlyFails() {
        this.parseFailAndCheck(ASSIGNMENT);
    }

    @Test
    public void testDoubleTerminalFails() {
        this.parseFailAndCheck(IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + ASSIGNMENT + TERMINAL1_TEXT);
    }

    @Test
    public void testIdentifierWhitespaceAssignmentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +WHITESPACE1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), whitespace1(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierCommentAssignmentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +COMMENT1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), comment1(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierWhitespaceCommentAssignmentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +WHITESPACE1 + COMMENT1 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), whitespace1(), comment1(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierWhitespaceCommentWhitespaceAssignmentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +WHITESPACE1 + COMMENT1 + WHITESPACE2 + ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), whitespace1(), comment1(), whitespace2(), assignmentToken(), terminal1(), terminatorToken()),
                text);
    }
    
    // assignment

    @Test
    public void testIdentifierAssignmentWhitespaceTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + WHITESPACE1 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), whitespace1(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentCommentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + COMMENT1 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), comment1(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentWhitespaceCommentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + WHITESPACE1 + COMMENT1 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), whitespace1(), comment1(), terminal1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentWhitespaceCommentWhitespaceTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT + ASSIGNMENT + WHITESPACE1 + COMMENT1 + WHITESPACE2 + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), whitespace1(), comment1(), whitespace2(), terminal1(), terminatorToken()),
                text);
    }
    
    // terminator

    @Test
    public void testIdentifierAssignmentTerminalWhitespaceTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + WHITESPACE1 + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), whitespace1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentTerminalCommentTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + COMMENT1 + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), comment1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentTerminalWhitespaceCommentTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + WHITESPACE1 + COMMENT1 + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), whitespace1(), comment1(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentTerminalWhitespaceCommentWhitespaceTerminator() {
        final String text = IDENTIFIER1_TEXT + ASSIGNMENT + TERMINAL1_TEXT + WHITESPACE1 + COMMENT1 + WHITESPACE2 +TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), terminal1(), whitespace1(), comment1(), whitespace2(), terminatorToken()),
                text);
    }

    @Test
    public void testIdentifierAssignmentTerminalTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text + WHITESPACE1,
                rule(text, identifier1(), assignmentToken(), terminal1(), terminatorToken()),
                text,
                WHITESPACE1);
    }

    @Test
    public void testIdentifierAssignmentIdentifierTerminator() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + IDENTIFIER2_TEXT + TERMINATOR;
        this.parseAndCheck(text + WHITESPACE1,
                rule(text, identifier1(), assignmentToken(), identifier2(), terminatorToken()),
                text,
                WHITESPACE1);
    }

    @Test
    public void testConcatenation() {
        final String concatText = TERMINAL1_TEXT + "," + TERMINAL2_TEXT;
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + concatText + TERMINATOR;

        final EbnfParserToken concat = EbnfParserToken.concatenation(Lists.of(terminal1(), concatToken(), terminal2()), concatText);
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), concat, terminatorToken()),
                text);
    }

    @Test
    public void testTerminatorComments() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + TERMINATOR;
        this.parseAndCheck(text + COMMENT1,
                rule(text, identifier1(), assignmentToken(), terminal1(), terminatorToken()),
                text,
                COMMENT1);
    }

    @Test
    public void testIncludesRange() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + TERMINAL1_TEXT + BETWEEN + TERMINAL2_TEXT + TERMINATOR;
        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), range(TERMINAL1_TEXT + BETWEEN + TERMINAL2_TEXT, terminal1(), between(), terminal2()), terminatorToken()),
                text);
    }

    @Test
    public void testExceptionFails() {
        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + EXCEPTION + TERMINAL2_TEXT + TERMINATOR;
        this.parseFailAndCheck(text);
    }

    @Test
    public void testTokenThenException() {
        final String exceptionText = TERMINAL1_TEXT + EXCEPTION + TERMINAL2_TEXT;
        final EbnfParserToken exception = EbnfParserToken.exception(Lists.of(terminal1(), exceptionToken(), terminal2()),exceptionText);

        final String text = IDENTIFIER1_TEXT +ASSIGNMENT + exceptionText + TERMINATOR;

        this.parseAndCheck(text,
                rule(text, identifier1(), assignmentToken(), exception, terminatorToken()),
                text);
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
                EbnfParserToken.identifier(IDENTIFIER1, IDENTIFIER1_TEXT),
                assignmentToken(),
                EbnfParserToken.terminal(TERMINAL1, TERMINAL1_TEXT),
                terminatorToken());
    }
}
