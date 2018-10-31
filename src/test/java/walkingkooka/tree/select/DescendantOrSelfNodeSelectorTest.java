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

import static org.junit.Assert.assertEquals;

final public class DescendantOrSelfNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<DescendantOrSelfNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test(expected = NullPointerException.class)
    public void testWithNullPathSeparatorFails() {
        DescendantOrSelfNodeSelector.with(null);
    }

    @Test
    public void testChildless() {
        final TestFakeNode only = TestFakeNode.node("only");
        this.acceptAndCheck(only, only);
    }

    @Test
    public void testIgnoresParent() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(parent.child(0), child);
    }

    @Test
    public void testParentWithChildren() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");
        final TestFakeNode parent = TestFakeNode.node("parent", child1, child2);

        this.acceptAndCheck(parent, parent, child1, child2);
    }

    @Test
    public void testParentWithGrandChildren() {
        final TestFakeNode grandChild1 = TestFakeNode.node("grandChild1");
        final TestFakeNode grandChild2 = TestFakeNode.node("grandChild2");

        final TestFakeNode child1 = TestFakeNode.node("child1", grandChild1);
        final TestFakeNode child2 = TestFakeNode.node("child2", grandChild2);

        final TestFakeNode parent = TestFakeNode.node("parent", child1, child2);

        this.acceptAndCheck(parent, parent, child1, grandChild1, child2, grandChild2);
    }

    @Test
    public void testChildrenWithChildren2() {
        final TestFakeNode grandChild1 = TestFakeNode.node("grandChild1");
        final TestFakeNode grandChild2 = TestFakeNode.node("grandChild2");

        final TestFakeNode child1 = TestFakeNode.node("child1", grandChild1, grandChild2);
        final TestFakeNode child2 = TestFakeNode.node("child2");

        final TestFakeNode parent = TestFakeNode.node("parent", child1, child2);

        this.acceptAndCheck(parent, parent, child1, grandChild1, grandChild2, child2);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);
        final TestFakeNode siblingOfParent = TestFakeNode.node("siblingOfParent");
        final TestFakeNode root = TestFakeNode.node("root", parent, siblingOfParent);

        this.acceptAndCheck(root, root, parent, child, siblingOfParent);
        this.acceptAndCheck(parent.child(0), child);
    }

    @Test
    public void testToString() {
        assertEquals("//", this.createSelector().toString());
    }

    @Override
    protected DescendantOrSelfNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return DescendantOrSelfNodeSelector.with(SEPARATOR);
    }

    @Override
    protected Class<DescendantOrSelfNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(DescendantOrSelfNodeSelector.class);
    }
}
