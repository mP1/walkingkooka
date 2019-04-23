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

final public class FollowingNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<FollowingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testRoot() {
        this.acceptAndCheck(TestNode.with("root"));
    }

    @Test
    public void testOnlyChildIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.child(0));
    }

    @Test
    public void testChild() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent, child);
    }

    @Test
    public void testFirstChild() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.acceptAndCheck(parent.child(0), following);
    }

    @Test
    public void testFirstChild2() {
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode parent = TestNode.with("parent", self, following1, following2);

        this.acceptAndCheck(parent.child(0), following1, following2);
    }

    @Test
    public void testLastChild() {
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", TestNode.with("before"), self);

        this.acceptAndCheck(parent.child(1));
    }

    @Test
    public void testParentFollowingSibling() {
        final TestNode parent = TestNode.with("parent");

        final TestNode parentFollowingSibling = TestNode.with("parentFollowingSibling");

        final TestNode grandParent = TestNode.with("grandParent", parent, parentFollowingSibling);

        this.acceptAndCheck(grandParent.child(0), parentFollowingSibling);
    }

    @Test
    public void testChildrenAndParentFollowingSibling() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        final TestNode parentFollowingSiblingChild = TestNode.with("parentFollowingSiblingChild");
        final TestNode parentFollowingSibling = TestNode.with("parentFollowingSibling", parentFollowingSiblingChild);

        final TestNode grandParent = TestNode.with("grandParent", parent, parentFollowingSibling);

        this.acceptAndCheck(grandParent.child(0), child, parentFollowingSibling, parentFollowingSiblingChild);
    }

    @Test
    public void testIgnoresPreceding() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding, self, following);

        this.acceptAndCheck(parent.child(1), following);
    }

    @Test
    public void testIgnoresPreceding2() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), following1, following2);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "following::*");
    }

    @Override
    FollowingNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return FollowingNodeSelector.get();
    }

    @Override
    public Class<FollowingNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(FollowingNodeSelector.class);
    }
}
