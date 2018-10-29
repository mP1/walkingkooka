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

final public class FollowingNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<FollowingNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test final public void testRoot() {
        this.acceptAndCheck(TestFakeNode.node("root"));
    }

    @Test final public void testOnlyChildIgnoresParent() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(parent.child(0));
    }

    @Test final public void testChild() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(parent, child);
    }

    @Test final public void testFirstChild() {
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");
        final TestFakeNode parent = TestFakeNode.node("parent", self, following);

        this.acceptAndCheck(parent.child(0), following);
    }

    @Test final public void testFirstChild2() {
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following1 = TestFakeNode.node("following1");
        final TestFakeNode following2 = TestFakeNode.node("following2");
        final TestFakeNode parent = TestFakeNode.node("parent", self, following1, following2);

        this.acceptAndCheck(parent.child(0), following1, following2);
    }

    @Test final public void testLastChild() {
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode parent = TestFakeNode.node("parent", TestFakeNode.node("before"), self);

        this.acceptAndCheck(parent.child(1));
    }

    @Test final public void testParentFollowingSibling() {
        final TestFakeNode parent = TestFakeNode.node("parent");

        final TestFakeNode parentFollowingSibling = TestFakeNode.node("parentFollowingSibling");

        final TestFakeNode grandParent = TestFakeNode.node("grandParent", parent, parentFollowingSibling);

        this.acceptAndCheck(grandParent.child(0), parentFollowingSibling);
    }

    @Test final public void testChildrenAndParentFollowingSibling() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        final TestFakeNode parentFollowingSiblingChild = TestFakeNode.node("parentFollowingSiblingChild");
        final TestFakeNode parentFollowingSibling = TestFakeNode.node("parentFollowingSibling", parentFollowingSiblingChild);

        final TestFakeNode grandParent = TestFakeNode.node("grandParent", parent, parentFollowingSibling);

        this.acceptAndCheck(grandParent.child(0), child, parentFollowingSibling, parentFollowingSiblingChild);
    }

    @Test final public void testIgnoresPreceding() {
        final TestFakeNode preceding = TestFakeNode.node("preceding");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");

        final TestFakeNode parent = TestFakeNode.node("parent", preceding, self, following);

        this.acceptAndCheck(parent.child(1), following);
    }

    @Test final public void testIgnoresPreceding2() {
        final TestFakeNode preceding1 = TestFakeNode.node("preceding1");
        final TestFakeNode preceding2 = TestFakeNode.node("preceding2");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following1 = TestFakeNode.node("following1");
        final TestFakeNode following2 = TestFakeNode.node("following2");

        final TestFakeNode parent = TestFakeNode.node("parent", preceding1, preceding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), following1, following2);
    }

    @Test
    public void testToString() {
        assertEquals("following::*", this.createSelector().toString());
    }

    @Override
    protected FollowingNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return FollowingNodeSelector.get();
    }

    @Override
    protected Class<FollowingNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(FollowingNodeSelector.class);
    }
}
