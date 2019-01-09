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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;
import walkingkooka.tree.expression.ExpressionNode;

import java.util.List;

public abstract class LogicalNodeSelectorEqualityTestCase<S extends LogicalNodeSelector<TestFakeNode,
        StringName,
        StringName,
        Object>> extends NodeSelectorEqualityTestCase<S> {

    LogicalNodeSelectorEqualityTestCase() {
        super();
    }

    @Test
    public final void testDifferentSelectors() {
        this.checkNotEquals(this.createNodeSelector(
                NodeSelector.terminal(),
                NodeSelector.expression(ExpressionNode.text("different!"))));
    }

    @Override
    final S createNodeSelector() {
        return this.createNodeSelector(NodeSelector.self(), NodeSelector.terminal());
    }

    private S createNodeSelector(final NodeSelector<TestFakeNode,
            StringName,
            StringName,
            Object>... selectors) {
        return this.createNodeSelector(Lists.of(selectors));
    }

    abstract S createNodeSelector(final List<NodeSelector<TestFakeNode,
            StringName,
            StringName,
            Object>> selectors);
}
