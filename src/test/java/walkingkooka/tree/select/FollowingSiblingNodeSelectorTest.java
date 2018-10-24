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

final public class FollowingSiblingNodeSelectorTest extends
        UnaryNodeSelectorTestCase<FollowingSiblingNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test final public void testRoot() {
        this.acceptAndCheck(TestFakeNode.node("root"));
    }

    @Test final public void testChildIgnoresParent() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(parent.child(0));
    }

    @Test final public void testFirstChildWithFollowingSibling() {
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");
        final TestFakeNode parent = TestFakeNode.node("parent", self, following);

        this.acceptAndCheck(parent.child(0), following);
    }

    @Test final public void testFirstChildWithFollowingSiblings2() {
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

    @Test final public void testIgnoresPreceeding() {
        final TestFakeNode preceeding = TestFakeNode.node("preceeding");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");

        final TestFakeNode parent = TestFakeNode.node("parent", preceeding, self, following);

        this.acceptAndCheck(parent.child(1), following);
    }

    @Test final public void testIgnoresPreceeding2() {
        final TestFakeNode preceeding1 = TestFakeNode.node("preceeding1");
        final TestFakeNode preceeding2 = TestFakeNode.node("preceeding2");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following1 = TestFakeNode.node("following1");
        final TestFakeNode following2 = TestFakeNode.node("following2");

        final TestFakeNode parent = TestFakeNode.node("parent", preceeding1, preceeding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), following1, following2);
    }

    @Test final public void testIgnoresChildren() {
        this.acceptAndCheck(TestFakeNode.node("parent", TestFakeNode.node("child")));
    }

    @Test
    public void testGrandChildrenIgnored() {
        final TestFakeNode preceeding = TestFakeNode.node("preceeding");
        final TestFakeNode self = TestFakeNode.node("self");

        final TestFakeNode followingGrandChild = TestFakeNode.node("followingGrandChild");
        final TestFakeNode following = TestFakeNode.node("following", followingGrandChild);

        final TestFakeNode parent = TestFakeNode.node("parent", preceeding, self, following);

        this.acceptAndCheck(parent.child(1), following);
    }

    @Test
    public void testToString() {
        assertEquals("following-sibling::*", this.createSelector().toString());
    }

    @Override
    protected FollowingSiblingNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return FollowingSiblingNodeSelector.get();
    }

    @Override
    protected Class<FollowingSiblingNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(FollowingSiblingNodeSelector.class);
    }
}
