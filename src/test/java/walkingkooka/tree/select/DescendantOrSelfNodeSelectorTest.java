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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

final public class DescendantOrSelfNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<DescendantOrSelfNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testChildless() {
        final TestNode only = TestNode.with("only");
        this.acceptAndCheck(only, only);
    }

    @Test
    public void testIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.child(0), child);
    }

    @Test
    public void testParentWithChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.acceptAndCheck(parent, parent, child1, child2);
    }

    @Test
    public void testParentWithGrandChildren() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");

        final TestNode child1 = TestNode.with("child1", grandChild1);
        final TestNode child2 = TestNode.with("child2", grandChild2);

        final TestNode parent = TestNode.with("parent", child1, child2);

        this.acceptAndCheck(parent, parent, child1, grandChild1, child2, grandChild2);
    }

    @Test
    public void testChildrenWithChildren2() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");

        final TestNode child1 = TestNode.with("child1", grandChild1, grandChild2);
        final TestNode child2 = TestNode.with("child2");

        final TestNode parent = TestNode.with("parent", child1, child2);

        this.acceptAndCheck(parent, parent, child1, grandChild1, grandChild2, child2);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode siblingOfParent = TestNode.with("siblingOfParent");
        final TestNode root = TestNode.with("root", parent, siblingOfParent);

        this.acceptAndCheck(root, root, parent, child, siblingOfParent);
        this.acceptAndCheck(parent.child(0), child);
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
                        TestNode.with("parent1*0",
                                TestNode.with("child1*1"), TestNode.with("child2*2")),
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
                TestNode.with("grand*0",
                        TestNode.with("parent1*1",
                                TestNode.with("child1*2"), TestNode.with("child2*3")),
                        TestNode.with("parent2*4", TestNode.with("child3*5"))));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "//");
    }

    @Override
    DescendantOrSelfNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return DescendantOrSelfNodeSelector.get();
    }

    @Override
    public Class<DescendantOrSelfNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(DescendantOrSelfNodeSelector.class);
    }
}
