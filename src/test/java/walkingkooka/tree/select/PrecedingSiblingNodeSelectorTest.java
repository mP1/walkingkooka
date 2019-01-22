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
import walkingkooka.tree.TestNode;

import static org.junit.Assert.assertEquals;

final public class PrecedingSiblingNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test final public void testRoot() {
        this.acceptAndCheck(TestNode.with("root"));
    }

    @Test final public void testOnlyChild() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.child(0));
    }

    @Test final public void testFirstChild() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.acceptAndCheck(parent.child(0));
    }

    @Test final public void testLastChild() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", preceding, self);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test final public void testIgnoresGrandChildren() {
        final TestNode preceding = TestNode.with("preceding", TestNode.with("grandchild-of-preceding"));
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", preceding, self);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test final public void testMiddleChildIgnoresFollowing() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding, self, following);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test final public void testMiddleChild2() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), preceding2, preceding1);
    }

    @Test final public void testIgnoresChildren() {
        this.acceptAndCheck(TestNode.with("parent", TestNode.with("child")));
    }

    @Test
    public void testToString() {
        assertEquals("preceding-sibling::*", this.createSelector().toString());
    }

    @Override
    protected PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return PrecedingSiblingNodeSelector.get();
    }

    @Override
    protected Class<PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(PrecedingSiblingNodeSelector.class);
    }
}
