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

public final class NodeSelectorExpressionParserTokenTest extends NodeSelectorParentParserTokenTestCase<NodeSelectorExpressionParserToken> {

    // [ends-with(@href, '/')]

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final NodeSelectorExpressionParserToken expression = this.createToken();

        final NodeSelectorWildcardParserToken wildcard = expression.value().get(0).cast();

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
            protected Visiting startVisit(final NodeSelectorExpressionParserToken t) {
                assertSame(expression, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorExpressionParserToken t) {
                assertSame(expression, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final NodeSelectorWildcardParserToken t) {
                assertSame(wildcard, t);
                b.append("5");
                visited.add(t);
            }
        }.accept(expression);
        assertEquals("1315242", b.toString());
        assertEquals("visited",
                Lists.of(expression, expression,
                        wildcard, wildcard, wildcard,
                        expression, expression),
                visited);
    }

    @Test
    public void testWithoutSymbolsExpressionWhitespace() {
        final NodeSelectorExpressionParserToken expression = this.createToken();
        final NodeSelectorExpressionParserToken without = expression.withoutSymbols().get().cast();
        assertEquals("value", Lists.of(wildcard()), without.value());
    }

    @Override
    NodeSelectorExpressionParserToken createToken(final String text, final List<ParserToken> tokens) {
        return NodeSelectorExpressionParserToken.with(tokens, text);
    }

    @Override
    protected String text() {
        return "*";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(wildcard());
    }

    @Override
    protected NodeSelectorExpressionParserToken createDifferentToken() {
        return expression(descendantOrSelfSlashSlash());
    }

    @Override
    protected Class<NodeSelectorExpressionParserToken> type() {
        return NodeSelectorExpressionParserToken.class;
    }
}
