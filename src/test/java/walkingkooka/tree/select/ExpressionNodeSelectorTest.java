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
import walkingkooka.naming.Names;
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
    public void testExpressionSelfSelected() {
        final TestNode self = TestNode.with("self");
        this.acceptAndCheck(self, self);
    }

    @Test
    public void testExpressionIgnoresNonSelfNodes() {
        final TestNode siblingBefore = TestNode.with("siblingBefore");
        final TestNode self = TestNode.with("self", TestNode.with("child"));
        final TestNode siblingAfter = TestNode.with("siblingAfter");
        final TestNode parent = TestNode.with("parent", siblingBefore, self, siblingAfter);

        this.acceptAndCheck(parent.child(1), self);
    }

    @Test
    public void testExpressionFunctionBooleanChildrenSelectorAppended() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("self", child1, child2);

        this.acceptAndCheck(ExpressionNodeSelector.<TestNode, StringName, StringName, Object>with(ExpressionNode.booleanNode(true)).children(),
                parent,
                child1, child2);
    }

    @Test
    public void testExpressionFunctionNumberChildrenSelectorAppended() {
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
    public void testExpressionFalseMap() {
        final TestNode node = TestNode.with("node");

        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(false)),
                node);
    }

    @Test
    public void testExpressionTrueMap() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(true)),
                TestNode.with("node"),
                TestNode.with("node*0"));
    }

    @Test
    public void testExpressionTrue2Map() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(true)),
                parent.child(0),
                TestNode.with("parent", TestNode.with("child*0")).child(0));
    }

    @Test
    public void testExpressionTrueIgnoresChildrenMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child",
                        TestNode.with("grand-child1"), TestNode.with("grand-child2")));

        TestNode.clear();

        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.booleanNode(true)),
                parent.child(0),
                TestNode.with("parent",
                        TestNode.with("child*0",
                                TestNode.with("grand-child1"), TestNode.with("grand-child2")))
                        .child(0));
    }

    @Test
    public void testExpressionNumberNegativeMap() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.longNode(-2)),
                TestNode.with("node"));
    }

    @Test
    public void testExpressionNumberOutOfRangeMap() {
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.longNode(999)),
                TestNode.with("node"));
    }

    @Test
    public void testExpressionNumberValidIndexMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        final int index = 1;
        this.acceptMapAndCheck(ExpressionNodeSelector.with(ExpressionNode.longNode(NodeSelector.INDEX_BIAS + index)),
                parent,
                TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2*0")));
    }

    @Test
    public void testChildrenExpressionTrueMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1", TestNode.with("grandChildren1")),
                TestNode.with("child2", TestNode.with("grandChildren2")),
                TestNode.with("child3"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.relativeNodeSelector().children().expression(ExpressionNode.booleanNode(true)),
                parent,
                TestNode.with("parent",
                        TestNode.with("child1*0", TestNode.with("grandChildren1")),
                        TestNode.with("child2*1", TestNode.with("grandChildren2")),
                        TestNode.with("child3*2")));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "*[" + expression() + "]");
    }

    @Test
    public void testToStringChildrenExpression() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .expression(ExpressionNode.booleanNode(true)),
                "child::*[true]");
    }

    @Test
    public void testToStringChildrenNamedExpression() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("ABC"))
                        .expression(ExpressionNode.booleanNode(true)),
                "child::ABC[true]");
    }

    @Test
    public void testToStringExpressionChildren() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .expression(ExpressionNode.booleanNode(true))
                        .firstChild(),
                "*[true]/first-child::*");
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
