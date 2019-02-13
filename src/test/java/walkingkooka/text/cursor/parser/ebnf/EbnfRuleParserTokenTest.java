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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EbnfRuleParserTokenTest extends EbnfParentParserTokenTestCase<EbnfRuleParserToken> {

    @Test
    public void testMissingIdentifierFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), assignment(), terminal1(), terminator());
        });
    }

    @Test
    public void testMissingIdentifierFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), terminal1(), assignment(), identifier1(), terminator());
        });
    }

    @Test
    public void testMissingTokenFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), identifier1(), assignment(), terminator());
        });
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespace() {
        final EbnfRuleParserToken token = this.createToken();
        assertNotSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Test
    public final void testSetValueMissingIdentifierFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setValue(Lists.of(assignment(), identifier1(), terminator()));
        });
    }

    @Test
    public final void testSetValueMissingRhsFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken().setValue(Lists.of(terminal1(), assignment()));
        });
    }

    @Test
    public final void testSetValueDifferentRule() {
        final EbnfRuleParserToken token = this.createToken();

        final List<ParserToken> tokens = Lists.of(identifier2(), terminal2());
        final EbnfRuleParserToken different = token.setValue(tokens);
        this.checkValue(different, tokens);
        assertEquals(Optional.of(different), different.withoutCommentsSymbolsOrWhitespace());
    }

    @Test
    public final void testSetValueDifferentRule2() {
        final EbnfRuleParserToken token = this.createToken();

        final List<ParserToken> tokens = Lists.of(whitespace(), identifier2(), assignment(), terminal2(), terminator());
        final EbnfRuleParserToken different = token.setValue(tokens);
        this.checkValue(different, tokens);

        final Optional<EbnfParserToken> differentWithout = different.withoutCommentsSymbolsOrWhitespace();
        assertNotEquals(Optional.of(different), different);

        this.checkValue(differentWithout.get(), identifier2(), terminal2());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();


        final EbnfRuleParserToken rule = this.createToken();
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
                assertEquals(identifier, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final EbnfRuleParserToken t) {
                assertSame(rule, t);
                b.append("6");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfRuleParserToken t) {
                assertSame(rule, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfSymbolParserToken t) {
                b.append("8");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfTerminalParserToken t) {
                b.append("9");
                visited.add(t);
            }
        }.accept(rule);
        assertEquals("13613542138421394213842742", b.toString());
        assertEquals(Lists.<Object>of(rule, rule, rule,
                        identifier, identifier, identifier, identifier, identifier,
                        assignment, assignment, assignment, assignment, assignment,
                        terminal, terminal, terminal, terminal, terminal,
                        terminator, terminator, terminator, terminator, terminator,
                        rule, rule, rule),
                visited,
                "visited");
    }

    @Test
    public final void testToSearchNode() {
        final EbnfRuleParserToken token = EbnfRuleParserToken.with(
                Lists.of(identifier1(), assignment(), identifier2(), terminator()),
                this.identifier1().text() + "=" + this.identifier2() + ";");
        final SearchNode searchNode = token.toSearchNode();

        assertEquals(token.text(), searchNode.text(), "text");
    }

    @Override
    public EbnfRuleParserToken createDifferentToken() {
        return this.createToken("xyz=qrs;",
                identifier("xyz"), assignment(), identifier("qrs"), terminator());
    }

    @Override
    public final String text() {
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
    public Class<EbnfRuleParserToken> type() {
        return EbnfRuleParserToken.class;
    }
}
