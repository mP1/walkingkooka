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
import walkingkooka.tree.expression.FakeExpressionNodeVisitor;
import walkingkooka.tree.expression.ExpressionBooleanNode;
import walkingkooka.tree.expression.ExpressionLeafNodeTestCase;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.Assert.*;

public final class ExpressionBooleanNodeTest extends ExpressionLeafValueNodeTestCase<ExpressionBooleanNode, Boolean>{

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ExpressionBooleanNode node = this.createExpressionNode();

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
            protected void visit(final ExpressionBooleanNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }
    
    @Test
    public void testToStringTrue() {
        assertEquals("true", this.createExpressionNode(true).toString());
    }

    @Test
    public void testToStringFalse() {
        assertEquals("false", this.createExpressionNode(false).toString());
    }

    @Override
    ExpressionBooleanNode createExpressionNode(final Boolean value) {
        return ExpressionBooleanNode.with(value);
    }

    @Override
    Boolean value() {
        return true;
    }

    @Override
    Boolean differentValue() {
        return false;
    }

    @Override
    Class<ExpressionBooleanNode> expressionNodeType() {
        return ExpressionBooleanNode.class;
    }
}
