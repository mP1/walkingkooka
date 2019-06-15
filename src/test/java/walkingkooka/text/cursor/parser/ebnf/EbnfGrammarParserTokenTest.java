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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EbnfGrammarParserTokenTest extends EbnfParentParserTokenTestCase<EbnfGrammarParserToken> {

    @Test
    public void testMissingRuleFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), terminal1());
        });
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespace() {
        final EbnfGrammarParserToken token = this.createToken();
        assertSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();


        final EbnfGrammarParserToken grammar = this.createToken();
        final EbnfRuleParserToken range = Cast.to(grammar.value().get(0));

        final Iterator<ParserToken> rangeTokens = range.value().iterator();
        final EbnfIdentifierParserToken identifier = Cast.to(rangeTokens.next());
        final EbnfSymbolParserToken assignment = Cast.to(rangeTokens.next());
        final EbnfTerminalParserToken terminal = Cast.to(rangeTokens.next());
        final EbnfSymbolParserToken terminator = Cast.to(rangeTokens.next());

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
                assertSame(identifier, t);
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
        assertEquals(Lists.of(range, range, range,
                identifier, identifier, identifier, identifier, identifier,
                assignment, assignment, assignment, assignment, assignment,
                terminal, terminal, terminal, terminal, terminal,
                terminator, terminator, terminator, terminator, terminator,
                range, range, range),
                visited,
                "visited");
    }

    @Test
    public void testCheckIdentifierReferencesExistNullExternalsFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createToken().checkIdentifiers(null);
        });
    }

    @Test
    public void testCheckIdentifierReferencesExist() {
        this.createToken().checkIdentifiers(EbnfGrammarParserToken.NO_EXTERNALS);
    }

    @Test
    public void testCheckIdentifierReferencesExist2() {
        final EbnfRuleParserToken rule = this.rule();
        final EbnfRuleParserToken rule2 = this.rule(this.identifier2(), this.identifier1(), "identifier2:identifier1;");

        this.createToken(rule.text() + rule2.text(), rule, rule2)
                .checkIdentifiers(EbnfGrammarParserToken.NO_EXTERNALS);
    }

    @Test
    public void testCheckIdentifierExternalReferences() {
        final EbnfIdentifierName identifier = EbnfIdentifierName.with("external");
        final EbnfIdentifierParserToken external = EbnfIdentifierParserToken.with(identifier, "external");
        final EbnfRuleParserToken rule = this.rule(this.identifier1(), external, "identifier1:external;");

        this.createToken(rule.text(), rule)
                .checkIdentifiers(Sets.of(identifier));
    }

    @Test
    public void testCheckIdentifierInvalidReferencesInvalidFails() {
        final EbnfRuleParserToken rule = this.rule(this.identifier1(), this.identifier2(), "identifier1:identifier2;");

        assertThrows(EbnfGrammarParserTokenInvalidReferencesException.class, () -> {
            this.createToken(rule.text(), rule)
                    .checkIdentifiers(EbnfGrammarParserToken.NO_EXTERNALS);
        });
    }

    @Test
    public void testCheckIdentifierInvalidReferencesInvalidFails2() {
        final EbnfRuleParserToken rule = this.rule();
        final EbnfRuleParserToken rule2 = this.rule(this.identifier2(), this.identifier("identifier3"), "identifier2:identifier3;");

        assertThrows(EbnfGrammarParserTokenInvalidReferencesException.class, () -> {
            this.createToken(rule.text() + rule2.text(), rule, rule2)
                    .checkIdentifiers(EbnfGrammarParserToken.NO_EXTERNALS);
        });
    }

    @Test
    public void testCheckIdentifierDuplicatesFail() {
        final EbnfRuleParserToken rule = this.rule(this.identifier1(), this.terminal1(), "identifier2:'terminal1';");
        final EbnfRuleParserToken rule2 = this.rule(this.identifier1(), this.terminal2(), "identifier2:'terminal2';");

        assertThrows(EbnfGrammarParserTokenDuplicateIdentifiersException.class, () -> {
            this.createToken(rule.text() + rule2.text(), rule, rule2)
                    .checkIdentifiers(EbnfGrammarParserToken.NO_EXTERNALS);
        });
    }

    @Test
    public void testCheckIdentifierDuplicates() {
        final EbnfRuleParserToken rule = this.rule(this.identifier1(), this.terminal1(), "identifier1:'terminal1';");
        final EbnfRuleParserToken rule2 = this.rule(this.identifier2(), this.terminal2(), "identifier2:'terminal2';");

        this.createToken(rule.text() + rule2.text(), rule, rule2)
                .checkIdentifiers(EbnfGrammarParserToken.NO_EXTERNALS);
    }

    @Test
    public final void testToSearchNode() {
        final String ruleText = "identifier2=\"terminal-2\";";
        final EbnfParserToken rule = EbnfParserToken.rule(Lists.of(identifier2(), assignment(), terminal2(), terminator()), ruleText);

        final EbnfGrammarParserToken token = EbnfGrammarParserToken.with(Lists.of(rule), ruleText);
        final SearchNode searchNode = token.toSearchNode();

        assertEquals(token.text(), searchNode.text(), "text");
    }

    @Override
    public EbnfGrammarParserToken createDifferentToken() {
        final String ruleText = "identifier2='terminal2';";
        final EbnfParserToken rule = EbnfParserToken.rule(Lists.of(identifier2(), assignment(), terminal1(), terminator()), ruleText);

        return EbnfGrammarParserToken.with(Lists.of(rule), ruleText);
    }

    @Override
    final public String text() {
        return "identifier1='terminal-1';";
    }

    final List<ParserToken> tokens() {
        return Lists.of(rule());
    }

    private EbnfRuleParserToken rule() {
        return this.rule(identifier1());
    }

    private EbnfRuleParserToken rule(final EbnfIdentifierParserToken identifier) {
        return this.rule(identifier, terminal1(), "identifier1:'terminal1';");
    }

    private EbnfRuleParserToken rule(final EbnfIdentifierParserToken identifier, final EbnfParserToken rhs, final String text) {
        return EbnfParserToken.rule(Lists.of(identifier, assignment(), rhs, terminator()), text);
    }

    @Override
    EbnfGrammarParserToken createToken(final String text, final List<ParserToken> tokens) {
        return EbnfGrammarParserToken.with(tokens, text);
    }

    @Override
    EbnfGrammarParserToken createTokenWithNoise() {
        return this.createToken("  " + text(), whitespace(), rule());
    }

    @Override
    public Class<EbnfGrammarParserToken> type() {
        return EbnfGrammarParserToken.class;
    }
}
