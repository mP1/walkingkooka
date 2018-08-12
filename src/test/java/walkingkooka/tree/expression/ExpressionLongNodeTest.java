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
import walkingkooka.tree.visit.Visiting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ExpressionLongNodeTest extends ExpressionLeafValueNodeTestCase<ExpressionLongNode, Long>{

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionLongNode node = this.createExpressionNode();

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final ExpressionLongNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("1", this.createExpressionNode(1L).toString());
    }

    @Test
    public void testToString2() {
        assertEquals("234", this.createExpressionNode(234L).toString());
    }

    @Override
    ExpressionLongNode createExpressionNode(final Long value) {
        return ExpressionLongNode.with(value);
    }

    @Override
    Long value() {
        return 1L;
    }

    @Override
    Long differentValue() {
        return 999L;
    }

    @Override
    Class<ExpressionLongNode> expressionNodeType() {
        return ExpressionLongNode.class;
    }
}
