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

package walkingkooka.text.cursor.parser.select;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class NodeSelectorFunctionParserTokenTest extends NodeSelectorParentParserTokenTestCase<NodeSelectorFunctionParserToken> {

    // [ends-with(@href, '/')]

    @Test
    public void testWithoutSymbols() {
        final NodeSelectorFunctionParserToken expression = this.createToken();
        final NodeSelectorFunctionParserToken without = expression.withoutSymbols().get().cast();
        assertEquals("value", Lists.of(functionName(), quotedText()), without.value());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorFunctionParserToken function = this.createToken();

        final NodeSelectorFunctionNameParserToken functionName = function.value().get(0).cast();
        final NodeSelectorParenthesisOpenSymbolParserToken parenOpen = function.value().get(1).cast();
        final NodeSelectorQuotedTextParserToken quotedText = function.value().get(2).cast();
        final NodeSelectorParenthesisCloseSymbolParserToken parenClose = function.value().get(3).cast();

        new FakeNodeSelectorParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final NodeSelectorParserToken n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorParserToken n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final NodeSelectorFunctionParserToken t) {
                assertSame(function, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorFunctionParserToken t) {
                assertSame(function, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorFunctionNameParserToken t) {
                assertSame(functionName, t);
                b.append("5");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParenthesisOpenSymbolParserToken t) {
                assertSame(parenOpen, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorQuotedTextParserToken t) {
                assertSame(quotedText, t);
                b.append("7");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorParenthesisCloseSymbolParserToken t) {
                assertSame(parenClose, t);
                b.append("8");
                visited.add(t);
            }
        }.accept(function);
        assertEquals("1315216217218242", b.toString());
        assertEquals("visited",
                Lists.of(function, function,
                        functionName, functionName, functionName,
                        parenOpen, parenOpen, parenOpen,
                        quotedText, quotedText, quotedText,
                        parenClose, parenClose, parenClose,
                        function, function),
                visited);
    }

    @Override
    NodeSelectorFunctionParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorFunctionParserToken.with(tokens, text);
    }

    @Override
    protected String text() {
        return "contains(\"xyz\")";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(functionName(), parenthesisOpen(), quotedText(), parenthesisClose());
    }

    @Override
    protected NodeSelectorFunctionParserToken createDifferentToken() {
        return function(functionName(), parenthesisOpen(), parenthesisClose());
    }

    @Override
    protected Class<NodeSelectorFunctionParserToken> type() {
        return NodeSelectorFunctionParserToken.class;
    }
}
