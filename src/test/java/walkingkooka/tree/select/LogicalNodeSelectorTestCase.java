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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class LogicalNodeSelectorTestCase<S extends LogicalNodeSelector<TestNode, StringName, StringName, Object>>
        extends NodeSelectorTestCase3<S> {

    private final static NodeSelector<TestNode, StringName, StringName, Object> SELECTOR = new FakeNodeSelector();
    private final static NodeSelector<TestNode, StringName, StringName, Object> SELECTOR2 = new FakeNodeSelector();

    LogicalNodeSelectorTestCase() {
        super();
    }

    @Test
    public final void testWithNullFirstFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSelector(null, SELECTOR);
        });
    }

    @Test
    public final void testWithNullSecondFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createSelector(SELECTOR, null);
        });
    }

    @Test
    public final void testWithEqualFirstAndSecond() {
        assertSame(SELECTOR, this.createSelector0(SELECTOR, SELECTOR));
    }

    @Test
    public final void testEqualsDifferentSelectors() {
        this.checkNotEquals(this.createSelector(
                NodeSelector.terminal(),
                ExpressionNodeSelector.with(ExpressionNode.text("different!"))));
    }

    @Override final S createSelector() {
        return this.createSelector(SELECTOR, SELECTOR2);
    }

    @SafeVarargs final S createSelector(final NodeSelector<TestNode, StringName, StringName, Object>... selectors) {
        return Cast.to(this.createSelector0(selectors));
    }

    @SafeVarargs
    final NodeSelector<TestNode, StringName, StringName, Object> createSelector0(final NodeSelector<TestNode, StringName, StringName, Object>... selectors) {
        return this.createSelector0(Lists.of(selectors));
    }

    abstract NodeSelector<TestNode, StringName, StringName, Object> createSelector0(final List<NodeSelector<TestNode, StringName, StringName, Object>> selectors);

    @SafeVarargs final void acceptAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                            final TestNode start,
                                            final String... nodes) {
        this.acceptAndCheckUsingContext(selector, start, nodes);
    }
}
