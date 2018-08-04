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

public final class EbnfRangeParserTokenTest extends EbnfParentParserTokenTestCase<EbnfRangeParserToken> {

    private final static String BETWEEN = "..";

    private final static String WHITESPACE = "   ";

    @Test(expected = IllegalArgumentException.class)
    public void testMissingBeginTokenFails() {
        final EbnfParserToken identifier1 = this.identifier1();
        this.createToken(this.text(), EbnfParserToken.optional(Lists.of(identifier1), "{" + identifier1 + "}"), between(), terminal2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingRangeBetweenFails() {
        this.createToken(this.text(), identifier1(), terminal2(), terminal2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingEndTokenFails2() {
        this.createToken(this.text(), terminal1(), between(), comment1());
    }

    @Test
    public void testTerminalTerminal() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + BETWEEN + TERMINAL_TEXT2,
                terminal1, between(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testIdentifierIdentifier() {
        final EbnfParserToken identifier1 = this.identifier1();
        final EbnfParserToken identifier2 = this.identifier2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + BETWEEN + TERMINAL_TEXT2,
                identifier1, between(), identifier2);
        assertSame("begin", identifier1, token.begin());
        assertSame("end", identifier2, token.end());
    }

    @Test
    public void testTerminalBeginWhitespace() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + WHITESPACE + BETWEEN + TERMINAL_TEXT2,
                terminal1, whitespace("   "), between(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testTerminalBeginComment() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + COMMENT1 + BETWEEN + TERMINAL_TEXT2,
                terminal1, comment1(), between(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testRangeBeginWhitespace() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + BETWEEN + WHITESPACE + TERMINAL_TEXT2,
                terminal1, between(), whitespace(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testRangeBeginComment() {
        final EbnfParserToken terminal1 = this.terminal1();
        final EbnfParserToken terminal2 = this.terminal2();
        final EbnfRangeParserToken token = this.createToken(
                TERMINAL_TEXT1 + BETWEEN + COMMENT1 +TERMINAL_TEXT2,
                terminal1, between(), comment1(), terminal2);
        assertSame("begin", terminal1, token.begin());
        assertSame("end", terminal2, token.end());
    }

    @Test
    public void testWithoutCommentsSymbolsOrWhitespace() {
        final EbnfRangeParserToken token = this.createToken();
        assertSame(token, token.withoutCommentsSymbolsOrWhitespace().get());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final EbnfRangeParserToken range = this.createToken();
        final EbnfTerminalParserToken terminal1 = this.terminal1();
        final EbnfSymbolParserToken between = this.between();
        final EbnfTerminalParserToken terminal2 = this.terminal2();

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
            protected Visiting startVisit(final EbnfRangeParserToken t) {
                assertSame(range, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfRangeParserToken t) {
                assertSame(range, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfSymbolParserToken t) {
                assertSame(range, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfTerminalParserToken t) {
                b.append("8");
                visited.add(t);
            }
        }.accept(range);
        assertEquals("135138421374213842642", b.toString());
        assertEquals("visited",
                Lists.of(range, range, range,
                        terminal1, terminal1, terminal1, terminal1, terminal1,
                        between, between, between, between, between,
                        terminal2, terminal2, terminal2, terminal2, terminal2,
                        range, range, range),
                visited);
    }

    @Override
    protected EbnfRangeParserToken createDifferentToken() {
        return this.createToken("'different-1'..'different-2'",
                terminal("different-1"), between(), terminal("different-2"));
    }

    @Override
    final String text() {
        return "'terminal-1'..'terminal2'";
    }

    @Override
    final List<ParserToken> tokens() {
        return Lists.of(terminal1(), between(), terminal2());
    }

    @Override
    EbnfRangeParserToken createToken(final String text, final List<ParserToken> tokens) {
        return EbnfRangeParserToken.with(tokens, text);
    }

    @Override
    EbnfRangeParserToken createTokenWithNoise() {
        return this.createDifferentToken();
    }

    @Override
    protected Class<EbnfRangeParserToken> type() {
        return EbnfRangeParserToken.class;
    }
}
