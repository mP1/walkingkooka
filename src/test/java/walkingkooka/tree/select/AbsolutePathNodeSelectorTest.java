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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

final public class AbsolutePathNodeSelectorTest extends
        UnaryNodeSelectorTestCase<AbsolutePathNodeSelector<TestFakeNode, StringName, StringName, Object>> {

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
        this.acceptAndCheck(AbsolutePathNodeSelector.with(ROOT), ROOT, ROOT);
    }

    @Test
    public void testDescendant() {
        final TestFakeNode child2 = child3();
        this.acceptAndCheck(AbsolutePathNodeSelector.with(child2), ROOT, child2);
    }

    @Test
    public void testDescendantLeaf() {
        final TestFakeNode childChild = childChild();
        this.acceptAndCheck(AbsolutePathNodeSelector.with(childChild), ROOT, childChild);
    }

    @Test
    public void testNotFound() {
        final TestFakeNode childChild = childChild();
        this.acceptAndCheck(AbsolutePathNodeSelector.with(childChild), child3());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(".", AbsolutePathNodeSelector.with(ROOT).toString());
    }

    @Test
    public void testToString2() {
        Assert.assertEquals("[3][2]", AbsolutePathNodeSelector.with(child3()).toString());
    }

    @Override
    protected AbsolutePathNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return Cast.to(AbsolutePathNodeSelector.with(child3()));
    }

    @Override
    protected Class<AbsolutePathNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(AbsolutePathNodeSelector.class);
    }
}
