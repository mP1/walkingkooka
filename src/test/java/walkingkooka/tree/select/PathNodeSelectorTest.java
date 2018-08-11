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
import walkingkooka.naming.StringName;

import static org.junit.Assert.assertEquals;

final public class PathNodeSelectorTest extends
        UnaryNodeSelectorTestCase<PathNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    private static TestFakeNode make() {
        final TestFakeNode childChild = TestFakeNode.node("childChild");

        final TestFakeNode child2 = TestFakeNode.node("child2");
        final TestFakeNode child3 = TestFakeNode.node("child3", childChild);

        final TestFakeNode parent1 = TestFakeNode.node("parent1");
        final TestFakeNode parent2 = TestFakeNode.node("parent2", TestFakeNode.node("child1"));
        final TestFakeNode parent3 = TestFakeNode.node("parent3", child2, child3);

        return TestFakeNode.node("root", parent1, parent2, parent3);
    }

    private static TestFakeNode child3() {
        return ROOT.child(2).child(1);
    }

    private static TestFakeNode childChild() {
        return child3().child(0);
    }

    private final static TestFakeNode ROOT = make();

    @Test
    public void testRoot() {
        this.acceptAndCheck(PathNodeSelector.with(ROOT), ROOT, ROOT);
    }

    @Test
    public void testDescendant() {
        final TestFakeNode child2 = child3();
        this.acceptAndCheck(PathNodeSelector.with(child2), ROOT, child2);
    }

    @Test
    public void testDescendantLeaf() {
        final TestFakeNode childChild = childChild();
        this.acceptAndCheck(PathNodeSelector.with(childChild), ROOT, childChild);
    }

    @Test
    public void testNotFound() {
        final TestFakeNode childChild = childChild();
        this.acceptAndCheck(PathNodeSelector.with(childChild), child3());
    }

    @Test
    public void testToString() {
        assertEquals(".", PathNodeSelector.with(ROOT).toString());
    }

    @Test
    public void testToString2() {
        assertEquals("[3][2]", PathNodeSelector.with(child3()).toString());
    }

    @Override
    protected PathNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return Cast.to(PathNodeSelector.with(child3()));
    }

    @Override
    protected Class<PathNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(PathNodeSelector.class);
    }
}
