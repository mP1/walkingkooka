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

import org.junit.Before;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

public final class PathNodeSelectorEqualityTest extends NonLogicalNodeSelectorEqualityTestCase<PathNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Before
    public void beforeEachTest() {
        TestFakeNode.names.clear();
    }

    @Test
    public void testDifferentNode() {
        this.checkNotEquals(this.createNodeSelector(
                TestFakeNode.node("different-parent", TestFakeNode.node("different-child-1"), TestFakeNode.node("different-child-2")).child(1),
                this.wrapped()));
    }

    @Override
    PathNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return this.createNodeSelector(this.node(), selector);
    }

    private PathNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final TestFakeNode node,
                                                                                              final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return Cast.to(PathNodeSelector.with(node).append(selector));
    }

    private TestFakeNode node() {
        return this.node;
    }

    private final TestFakeNode node = TestFakeNode.node("parent", TestFakeNode.node("child")).child(0);
}
