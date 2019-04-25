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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

final public class ChildrenNodeSelectorTest
        extends NonLogicalNodeSelectorTestCase<ChildrenNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestNode.with("childless"));
    }

    @Test
    public void testChildWithParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent").setChildren(Lists.of(child));

        this.acceptAndCheck(parent.child(0));
    }

    @Test
    public void testParentWithChild() {
        final TestNode child = TestNode.with("child");

        this.acceptAndCheck(TestNode.with("parent of one", child), child);
    }

    @Test
    public void testManyChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        this.acceptAndCheck(TestNode.with("parent of many children", child1, child2), child1, child2);
    }

    @Test
    public void testIgnoresGrandChild() {
        final TestNode grandChild = TestNode.with("grandchild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent, child);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode siblingOfParent = TestNode.with("siblingOfParent", TestNode.with("siblingOfParent-child"));
        final TestNode grandParent = TestNode.with("grandParent", parent, siblingOfParent);

        this.acceptAndCheck(parent, child);
    }

    @Test
    public void testMapWithoutChildren() {
        this.acceptMapAndCheck(TestNode.with("without-children"));
    }

    @Test
    public void testMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(0),
                TestNode.with("grand",
                        TestNode.with("parent1",
                                TestNode.with("child1*0"), TestNode.with("child2*1")),
                        TestNode.with("parent2", TestNode.with("child3")))
                        .child(0));
    }

    @Test
    public void testMap2() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent,
                TestNode.with("grand",
                        TestNode.with("parent1*0",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2*1", TestNode.with("child3"))));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "child::*");
    }

    @Override
    ChildrenNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return ChildrenNodeSelector.get();
    }

    @Override
    public Class<ChildrenNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ChildrenNodeSelector.class);
    }
}
