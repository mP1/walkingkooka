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
 * See the License for the specific language governing permissions functionNode
 * limitations under the License.
 *
 *
 */

package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ExpressionFunctionNodeTest extends ExpressionVariableNodeTestCase<ExpressionFunctionNode>{

    @Test(expected = NullPointerException.class)
    public void testWithNullNameFails() {
        ExpressionFunctionNode.with(null, this.children());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNameNullFails() {
        this.createExpressionNode().setName(null);
    }

    @Test
    public void testSetNameSame() {
        final ExpressionFunctionNode node = this.createExpressionNode();
        assertSame(node, node.setName(node.name()));
    }

    @Test
    public void testSetNameDifferent() {
        final ExpressionFunctionNode node = this.createExpressionNode();
        final ExpressionNodeName differentName = name("different-name");
        final ExpressionFunctionNode different = node.setName(differentName);
        assertEquals("name", differentName, different.name());
        this.checkChildren(different, node.children());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionFunctionNode function = this.createExpressionNode();
        final ExpressionNode text1 = function.children().get(0);
        final ExpressionNode text2 = function.children().get(1);
        final ExpressionNode text3 = function.children().get(2);

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final ExpressionFunctionNode t) {
                assertSame(function, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionFunctionNode t) {
                assertSame(function, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(function);
        assertEquals("1315215215242", b.toString());
        assertEquals("visited",
                Lists.of(function, function,
                        text1, text1, text1,
                        text2, text2, text2,
                        text3, text3, text3,
                        function, function),
                visited);
    }

    @Override
    ExpressionFunctionNode createExpressionNode(final List<ExpressionNode> children) {
        return ExpressionFunctionNode.with(name("fx"), children);
    }

    private ExpressionNodeName name(final String name) {
        return ExpressionNodeName.with(name);
    }

    @Override
    Class<ExpressionFunctionNode> expressionNodeType() {
        return ExpressionFunctionNode.class;
    }
}
