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
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EbnfRuleParserTokenTest extends EbnfParentParserTokenTestCase<EbnfRuleParserToken> {

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIdentifierFails() {
        this.createToken(this.text(), assignment(), terminal1(), terminator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingIdentifierFails2() {
        this.createToken(this.text(), terminal1(), assignment(), identifier1(), terminator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingTokenFails() {
        this.createToken(this.text(), identifier1(), assignment(), terminator());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespace() {
        final EbnfRuleParserToken token = this.createToken();
        assertSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();


        final EbnfRuleParserToken range = this.createToken();
        final EbnfIdentifierParserToken identifier = this.identifier1();
        final EbnfSymbolParserToken assignment = this.assignment();
        final EbnfTerminalParserToken terminal = this.terminal1();
        final EbnfSymbolParserToken terminator = this.terminator();

        new FakeEbnfParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("1");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                b.append("2");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final EbnfParserToken t) {
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfParserToken t) {
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfIdentifierParserToken t) {
                assertSame(range, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final EbnfRuleParserToken t) {
                assertSame(range, t);
                b.append("6");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfRuleParserToken t) {
                assertSame(range, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfSymbolParserToken t) {
                assertSame(range, t);
                b.append("8");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfTerminalParserToken t) {
                b.append("9");
                visited.add(t);
            }
        }.accept(range);
        assertEquals("13613542138421394213842742", b.toString());
        assertEquals("visited",
                Lists.of(range, range, range,
                        identifier, identifier, identifier, identifier, identifier,
                        assignment, assignment, assignment, assignment, assignment,
                        terminal, terminal, terminal, terminal, terminal,
                        terminator, terminator, terminator, terminator, terminator,
                        range, range, range),
                visited);
    }

    @Override
    protected EbnfRuleParserToken createDifferentToken() {
        return this.createToken("xyz=qrs;",
                identifier("xyz"), assignment(), identifier("qrs"), terminator());
    }

    @Override
    final String text() {
        return "abc123=def456";
    }

    final List<ParserToken> tokens() {
        return Lists.of(this.identifier1(), this.assignment(), this.terminal1(), this.terminator());
    }

    @Override
    EbnfRuleParserToken createToken(final String text, final List<ParserToken> tokens) {
        return EbnfRuleParserToken.with(tokens, text);
    }

    @Override
    EbnfRuleParserToken createTokenWithNoise() {
        return this.createDifferentToken();
    }

    @Override
    protected Class<EbnfRuleParserToken> type() {
        return EbnfRuleParserToken.class;
    }
}
