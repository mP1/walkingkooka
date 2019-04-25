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

final public class PrecedingSiblingNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testRoot() {
        this.acceptAndCheck(TestNode.with("root"));
    }

    @Test
    public void testOnlyChild() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.child(0));
    }

    @Test
    public void testFirstChild() {
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", self, following);

        this.acceptAndCheck(parent.child(0));
    }

    @Test
    public void testLastChild() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", preceding, self);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test
    public void testIgnoresGrandChildren() {
        final TestNode preceding = TestNode.with("preceding", TestNode.with("grandchild-of-preceding"));
        final TestNode self = TestNode.with("self");
        final TestNode parent = TestNode.with("parent", preceding, self);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test
    public void testMiddleChildIgnoresFollowing() {
        final TestNode preceding = TestNode.with("preceding");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");

        final TestNode parent = TestNode.with("parent", preceding, self, following);

        this.acceptAndCheck(parent.child(1), preceding);
    }

    @Test
    public void testMiddleChild2() {
        final TestNode preceding1 = TestNode.with("preceding1");
        final TestNode preceding2 = TestNode.with("preceding2");
        final TestNode self = TestNode.with("self");
        final TestNode following1 = TestNode.with("following1");
        final TestNode following2 = TestNode.with("following2");

        final TestNode parent = TestNode.with("parent", preceding1, preceding2, self, following1, following2);

        this.acceptAndCheck(parent.child(2), preceding2, preceding1);
    }

    @Test
    public void testIgnoresChildren() {
        this.acceptAndCheck(TestNode.with("parent", TestNode.with("child")));
    }

    @Test
    public void testMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")),
                TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(1),
                TestNode.with("grand",
                        TestNode.with("parent1*0",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3")),
                        TestNode.with("parent3", TestNode.with("child4")))
                        .child(1));
    }

    @Test
    public void testMapSeveralPrecedingSiblings() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")),
                TestNode.with("parent3", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(2),
                TestNode.with("grand",
                        TestNode.with("parent1*1",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2*0", TestNode.with("child3")),
                        TestNode.with("parent3", TestNode.with("child4")))
                        .child(2));
    }

    @Test
    public void testMapWithoutPrecedingSiblings() {
        this.acceptMapAndCheck(TestNode.with("node123"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "preceding-sibling::*");
    }

    @Override
    PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return PrecedingSiblingNodeSelector.get();
    }

    @Override
    public Class<PrecedingSiblingNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(PrecedingSiblingNodeSelector.class);
    }
}
