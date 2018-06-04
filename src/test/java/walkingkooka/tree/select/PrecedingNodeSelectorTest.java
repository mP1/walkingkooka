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

final public class PrecedingNodeSelectorTest extends
        UnaryNodeSelectorTestCase<PrecedingNodeSelector<TestFakeNode, StringName, StringName, Object>> {

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

    @Test final public void testPrecedingSibling() {
        final TestFakeNode preceding1 = TestFakeNode.node("preceding1");
        final TestFakeNode preceding2 = TestFakeNode.node("preceding2");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode parent = TestFakeNode.node("parent", preceding1, preceding2, self);

        this.acceptAndCheck(parent.child(2), preceding2, preceding1);
    }

    @Test final public void testIgnoresFollowingSibling() {
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");
        final TestFakeNode parent = TestFakeNode.node("parent", self, following);

        this.acceptAndCheck(parent.child(0));
    }

    @Test final public void testIgnoresFollowingSibling2() {
        final TestFakeNode preceding1 = TestFakeNode.node("preceding1");
        final TestFakeNode preceding2 = TestFakeNode.node("preceding2");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");

        final TestFakeNode parent = TestFakeNode.node("parent", preceding1, preceding2, self, following);

        this.acceptAndCheck(parent.child(2), preceding2, preceding1);
    }

    @Test final public void testParentPrecedingSibling() {
        final TestFakeNode parent = TestFakeNode.node("parent");

        final TestFakeNode parentProcedingSibling = TestFakeNode.node("parentProcedingSibling");

        final TestFakeNode grandParent = TestFakeNode.node("grandParent", parentProcedingSibling, parent);

        this.acceptAndCheck(grandParent.child(1), parentProcedingSibling);
    }

    @Test final public void testParentPrecedingSiblingWithChild() {
        final TestFakeNode parent = TestFakeNode.node("parent");

        final TestFakeNode parentProcedingSiblingChild = TestFakeNode.node("parentProcedingSiblingChild");
        final TestFakeNode parentProcedingSibling = TestFakeNode.node("parentProcedingSibling", parentProcedingSiblingChild);

        final TestFakeNode grandParent = TestFakeNode.node("grandParent", parentProcedingSibling, parent);

        this.acceptAndCheck(grandParent.child(1), parentProcedingSibling, parentProcedingSiblingChild);
    }

    @Test final public void testIgnoresParentFollowingSibling() {
        final TestFakeNode parent = TestFakeNode.node("parent");

        final TestFakeNode parentFollowingSibling = TestFakeNode.node("parentFollowingSibling");

        final TestFakeNode grandParent = TestFakeNode.node("grandParent", parent, parentFollowingSibling);

        this.acceptAndCheck(grandParent.child(0));
    }

    @Test final public void testIgnoresFollowing() {
        final TestFakeNode preceding = TestFakeNode.node("preceding");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following = TestFakeNode.node("following");

        final TestFakeNode parent = TestFakeNode.node("parent", preceding, self, following);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test final public void testIgnoresFollowing2() {
        final TestFakeNode preceding1 = TestFakeNode.node("preceding1");
        final TestFakeNode preceding2 = TestFakeNode.node("preceding2");
        final TestFakeNode self = TestFakeNode.node("self");
        final TestFakeNode following1 = TestFakeNode.node("following1");
        final TestFakeNode following2 = TestFakeNode.node("following2");

        final TestFakeNode parent = TestFakeNode.node("parent", preceding1, preceding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), preceding2, preceding1);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("preceding", this.createSelector().toString());
    }

    @Override
    protected PrecedingNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return PrecedingNodeSelector.get();
    }

    @Override
    protected Class<PrecedingNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(PrecedingNodeSelector.class);
    }
}
