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

package walkingkooka.tree.select;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.expression.ExpressionNode;

public final class ExpressionNodeSelectorEqualityTest extends NonLogicalNodeSelectorEqualityTestCase<ExpressionNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test
    public void testDifferentExpressionNode() {
        this.checkNotEquals(this.createNodeSelector(ExpressionNode.text("different"), this.wrapped()));
    }

    @Override
    ExpressionNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return this.createNodeSelector(expressionNode(), selector);
    }

    private ExpressionNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final ExpressionNode expressionNode,
                                                                                                    final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return Cast.to(ExpressionNodeSelector.<TestFakeNode, StringName, StringName, Object>with(expressionNode).append(selector));
    }

    private ExpressionNode expressionNode() {
        return ExpressionNode.text("text");
    }
}
