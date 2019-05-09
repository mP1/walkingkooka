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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

final public class FollowingNodeSelectorTest extends
        NonTerminalNodeSelectorTestCase<FollowingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testFollowingRoot() {
        this.applyAndCheck(TestNode.with("root"));
    }

    @Test
    public void testFollowingChild() {
        this.applyAndCheck(TestNode.with("parent", TestNode.with("child")));
    }

    @Test
    public void testFollowingOnlyChildIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0));
    }

    @Test
    public void testFollowingFirstChild() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.applyAndCheck(parent.child(0), following);
    }

    @Test
    public void testFollowingFirstChild2() {
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");
        final TestNode parent = TestNode.with("parent", self, following1, following2);

        this.applyAndCheck(parent.child(0), following1, following2);
    }

    @Test
    public void testFollowingLastChild() {
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", TestNode.with("before"), self);

        this.applyAndCheck(parent.child(1));
    }

    @Test
    public void testFollowingParentFollowingSibling() {
        final TestNode parent = TestNode.with("parent");

        final TestNode parentFollowingSibling = TestNode.with("parentFollowingSibling");

        final TestNode grandParent = TestNode.with("grandParent", parent, parentFollowingSibling);

        this.applyAndCheck(grandParent.child(0), parentFollowingSibling);
    }

    @Test
    public void testFollowingChildrenAndParentFollowingSibling() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        final TestNode parentFollowingSiblingChild = TestNode.with("parentFollowingSiblingChild");
        final TestNode parentFollowingSibling = TestNode.with("parentFollowingSibling", parentFollowingSiblingChild);

        final TestNode grandParent = TestNode.with("grandParent", parent, parentFollowingSibling);

        this.applyAndCheck(grandParent.child(0), parentFollowingSibling, parentFollowingSiblingChild);
    }

    @Test
    public void testFollowingIgnoresPreceding() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding, self, following);

        this.applyAndCheck(parent.child(1), following);
    }

    @Test
    public void testFollowingIgnoresPreceding2() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2);

        this.applyAndCheck(parent.child(2), following1, following2);
    }

    @Test
    public void testFollowingNamed() {
        TestNode.disableUniqueNameChecks();

        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following");
        final TestNode following2 = TestNode.with("skip");
        final TestNode following3 = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2, following3);

        this.applyAndCheck(TestNode.relativeNodeSelector().following().named(following1.name()),
                parent.child(2),
                following1);
    }

    @Test
    public void testFollowingWithAttributeEqualsValue() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = nodeWithAttributes("following1", "a1", "v1");
        final TestNode following2 = nodeWithAttributes("following2", "a1", "v1");
        final TestNode following3 = nodeWithAttributes("following3", "a1", "different");
        final TestNode following4 = nodeWithAttributes("following4", "a1", "v1");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2, following3, following4);

        this.applyAndCheck(TestNode.relativeNodeSelector().following().attributeValueEquals(Names.string("a1"), "v1"),
                parent.child(2),
                following1, following2, following4);
    }

    @Test
    public void testFollowingMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")),
                TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(1),
                TestNode.with("grand",
                        TestNode.with("parent1",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3")),
                        TestNode.with("parent3*0", TestNode.with("child4*1")))
                        .child(1));
    }

    @Test
    public void testFollowingMapSeveralFollowingSiblings() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")),
                TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(0),
                TestNode.with("grand",
                        TestNode.with("parent1",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2*0", TestNode.with("child3*1")),
                        TestNode.with("parent3*2", TestNode.with("child4*3")))
                        .child(0));
    }

    @Test
    public void testFollowingMapWithoutFollowing() {
        this.acceptMapAndCheck(TestNode.with("node123"));
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
