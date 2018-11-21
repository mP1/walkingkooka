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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;

import java.util.List;

import static org.junit.Assert.assertSame;

public abstract class LogicalNodeSelectorTestCase<S extends LogicalNodeSelector<TestFakeNode, StringName, StringName, Object>>
extends NodeSelectorTestCase<S>{

    private final static NodeSelector<TestFakeNode, StringName, StringName, Object> SELECTOR = new FakeNodeSelector();

    LogicalNodeSelectorTestCase(){
        super();
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullFirstFails() {
        this.createSelector(null, SELECTOR);
    }

    @Test(expected = NullPointerException.class)
    public final void testWithNullSecondFails() {
        this.createSelector(SELECTOR, null);
    }

    @Test
    public final void testWithEqualFirstAndSecond() {
        assertSame(SELECTOR, this.createSelector0(SELECTOR, SELECTOR));
    }

    @Override
    final S createSelector() {
        return this.createSelector(SELECTOR, new FakeNodeSelector());
    }

    @SafeVarargs
    final S createSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object>...selectors) {
        return Cast.to(this.createSelector0(selectors));
    }

    @SafeVarargs
    final NodeSelector<TestFakeNode, StringName, StringName, Object> createSelector0(final NodeSelector<TestFakeNode, StringName, StringName, Object>...selectors) {
        return this.createSelector0(Lists.of(selectors));
    }

    abstract NodeSelector<TestFakeNode, StringName, StringName, Object> createSelector0(final List<NodeSelector<TestFakeNode, StringName, StringName, Object>> selectors);

    @SafeVarargs
    final void acceptAndCheck0(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector,
                               final TestFakeNode start,
                               final String... nodes) {
        this.acceptAndCheckUsingContext(selector, start, nodes);
    }
}
