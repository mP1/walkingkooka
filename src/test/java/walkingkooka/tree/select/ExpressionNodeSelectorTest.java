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

package walkingkooka.tree.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;

import static org.junit.jupiter.api.Assertions.assertThrows;


final public class ExpressionNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<ExpressionNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            ExpressionNodeSelector.with(null);
        });
    }

    @Test
    public void testSelfSelected() {
        final TestNode self = TestNode.with("self");
        this.acceptAndCheck(self, self);
    }

    @Test
    public void testIgnoresNonSelfNodes() {
        final TestNode siblingBefore = TestNode.with("siblingBefore");
        final TestNode self = TestNode.with("self", TestNode.with("child"));
        final TestNode siblingAfter = TestNode.with("siblingAfter");
        final TestNode parent = TestNode.with("parent", siblingBefore, self, siblingAfter);

        this.acceptAndCheck(parent.child(1), self);
    }

    @Test
    public void testFunctionBooleanChildrenSelectorAppended() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("self", child1, child2);

        this.acceptAndCheck(ExpressionNodeSelector.<TestNode, StringName, StringName, Object>with(ExpressionNode.booleanNode(true)).children(),
                parent,
                child1, child2);
    }

    @Test
    public void testFunctionNumberChildrenSelectorAppended() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1", grandChild1),
                TestNode.with("child2", grandChild2, grandChild3));

        this.acceptAndCheck(ExpressionNodeSelector.<TestNode, StringName, StringName, Object>with(ExpressionNode.longNode(NodeSelector.INDEX_BIAS + 1))
                        .children(),
                parent,
                grandChild2, grandChild3);
    }

    @Test
    public void testMapFalse() {
        final TestNode node = TestNode.with("node");

        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(false)),
                node);
    }

    @Test
    public void testMapTrue() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(true)),
                TestNode.with("node"),
                TestNode.with("node*0"));
    }

    @Test
    public void testMapTrue2() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(true)),
                parent.child(0),
                TestNode.with("parent", TestNode.with("child*0")).child(0));
    }

    @Test
    public void testMapNumberNegative() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.longNode(-2)),
                TestNode.with("node"));
    }

    @Test
    public void testMapNumberOutOfRange() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.longNode(999)),
                TestNode.with("node"));
    }

    @Test
    public void testMapNumberValidIndex() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        final int index = 1;
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.longNode(NodeSelector.INDEX_BIAS + index)),
                parent,
                TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2*0")));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "*[" + expression() + "]");
    }

    @Override
    ExpressionNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return ExpressionNodeSelector.with(expression());
    }

    private ExpressionNode expression() {
        return ExpressionNode.equalsNode(
                ExpressionNode.function(ExpressionNodeName.with("name"), ExpressionNode.NO_CHILDREN),
                ExpressionNode.text("self")
        );
    }

    @Override
    public Class<ExpressionNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ExpressionNodeSelector.class);
    }
}
