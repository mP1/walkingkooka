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

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class PathNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<PathNodeSelector<TestNode, StringName, StringName, Object>> {

    private static TestNode make() {
        final TestNode childChild = TestNode.with("childChild");

        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3", childChild);

        final TestNode parent1 = TestNode.with("parent1");
        final TestNode parent2 = TestNode.with("parent2", TestNode.with("child1"));
        final TestNode parent3 = TestNode.with("parent3", child2, child3);

        return TestNode.with("root", parent1, parent2, parent3);
    }

    private static TestNode child3() {
        return ROOT.child(2).child(1);
    }

    private static TestNode childChild() {
        return child3().child(0);
    }

    private final static TestNode ROOT = make();

    @Test
    public void testRoot() {
        this.acceptAndCheck(PathNodeSelector.with(ROOT), ROOT, ROOT);
    }

    @Test
    public void testDescendant() {
        final TestNode child2 = child3();
        this.acceptAndCheck(PathNodeSelector.with(child2), ROOT, child2);
    }

    @Test
    public void testDescendantLeaf() {
        final TestNode childChild = childChild();
        this.acceptAndCheck(PathNodeSelector.with(childChild), ROOT, childChild);
    }

    @Test
    public void testNotFound() {
        final TestNode childChild = childChild();
        this.acceptAndCheck(PathNodeSelector.with(childChild), child3());
    }

    @Test
    public void testEqualsDifferentNode() {
        this.checkNotEquals(this.createSelector(
                TestNode.with("different-parent", TestNode.with("different-child-1"), TestNode.with("different-child-2")).child(1),
                this.wrapped()));
    }

    @Test
    public void testToString() {
        assertEquals(".", PathNodeSelector.with(ROOT).toString());
    }

    @Test
    public void testToString2() {
        assertEquals("*[3]/*[2]", PathNodeSelector.with(child3()).toString());
    }

    @Override
    protected PathNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return Cast.to(PathNodeSelector.with(child3()));
    }

    @Override
    protected Class<PathNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(PathNodeSelector.class);
    }

    private PathNodeSelector<TestNode, StringName, StringName, Object> createSelector(final TestNode node,
                                                                                      final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(PathNodeSelector.with(node).append(selector));
    }
}
