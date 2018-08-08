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

package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

public final class ExpressionGreaterThanNodeTest extends ExpressionBinaryNodeTestCase<ExpressionGreaterThanNode>{

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionGreaterThanNode gt = this.createExpressionNode();
        final ExpressionNode text1 = gt.children().get(0);
        final ExpressionNode text2 = gt.children().get(1);

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
            protected Visiting startVisit(final ExpressionGreaterThanNode t) {
                assertSame(gt, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionGreaterThanNode t) {
                assertSame(gt, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(gt);
        assertEquals("1315215242", b.toString());
        assertEquals("visited",
                Lists.of(gt, gt,
                        text1, text1, text1,
                        text2, text2, text2,
                        gt, gt),
                visited);
    }
    
    @Override
    ExpressionGreaterThanNode createExpressionNode(final ExpressionNode left, final ExpressionNode right) {
        return ExpressionGreaterThanNode.with(left, right);
    }

    @Override
    String expectedToString(){
        return LEFT_TO_STRING + "&" + RIGHT_TO_STRING;
    }

    @Override
    Class<ExpressionGreaterThanNode> expressionNodeType() {
        return ExpressionGreaterThanNode.class;
    }
}
