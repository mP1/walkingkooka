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

final public class FollowingSiblingNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test final public void testRoot() {
        this.acceptAndCheck(TestNode.with("root"));
    }

    @Test final public void testChildIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.child(0));
    }

    @Test final public void testFirstChildWithFollowingSibling() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.acceptAndCheck(parent.child(0), following);
    }

    @Test final public void testFirstChildWithFollowingSiblings2() {
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode parent = TestNode.with("parent", self, following1, following2);

        this.acceptAndCheck(parent.child(0), following1, following2);
    }

    @Test final public void testLastChild() {
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", TestNode.with("before"), self);

        this.acceptAndCheck(parent.child(1));
    }

    @Test final public void testIgnoresPreceeding() {
        final TestNode preceeding = TestNode.with("preceeding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceeding, self, following);

        this.acceptAndCheck(parent.child(1), following);
    }

    @Test final public void testIgnoresPreceeding2() {
        final TestNode preceeding1 = TestNode.with("preceeding1");
        final TestNode preceeding2 = TestNode.with("preceeding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceeding1, preceeding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), following1, following2);
    }

    @Test final public void testIgnoresChildren() {
        this.acceptAndCheck(TestNode.with("parent", TestNode.with("child")));
    }

    @Test
    public void testGrandChildrenIgnored() {
        final TestNode preceeding = TestNode.with("preceeding");
        final TestNode self = TestNode.with("self");

        final TestNode followingGrandChild = TestNode.with("followingGrandChild");
        final TestNode following = TestNode.with("following", followingGrandChild);

        final TestNode parent = TestNode.with("parent", preceeding, self, following);

        this.acceptAndCheck(parent.child(1), following);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "following-sibling::*");
    }

    @Override
    protected FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return FollowingSiblingNodeSelector.get();
    }

    @Override
    public Class<FollowingSiblingNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(FollowingSiblingNodeSelector.class);
    }
}
