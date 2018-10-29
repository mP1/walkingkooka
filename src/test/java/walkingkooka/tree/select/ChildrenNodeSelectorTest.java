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

import static org.junit.Assert.assertEquals;

final public class ChildrenNodeSelectorTest
        extends NonLogicalNodeSelectorTestCase<ChildrenNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestFakeNode.node("childless"));
    }

    @Test
    public void testChildWithParent() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent").setChildren(Lists.of(child));

        this.acceptAndCheck(parent.child(0));
    }

    @Test
    public void testParentWithChild() {
        final TestFakeNode child = TestFakeNode.node("child");

        this.acceptAndCheck(TestFakeNode.node("parent of one", child), child);
    }

    @Test
    public void testManyChildren() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");

        this.acceptAndCheck(TestFakeNode.node("parent of many children", child1, child2), child1, child2);
    }

    @Test
    public void testIgnoresGrandChild() {
        final TestFakeNode grandChild = TestFakeNode.node("grandchild");
        final TestFakeNode child = TestFakeNode.node("child", grandChild);
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(parent, child);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);
        final TestFakeNode siblingOfParent = TestFakeNode.node("siblingOfParent", child);
        final TestFakeNode grandParent = TestFakeNode.node("grandParent", parent, siblingOfParent);

        this.acceptAndCheck(parent, child);
    }

    @Test
    public void testToString() {
        assertEquals("child::*", this.createSelector().toString());
    }

    @Override
    protected ChildrenNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return ChildrenNodeSelector.get();
    }

    @Override
    protected Class<ChildrenNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(ChildrenNodeSelector.class);
    }
}
