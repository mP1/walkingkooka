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

final public class AncestorOrSelfNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testAncestorOrSelfRoot() {
        final TestNode root = TestNode.with("root");
        this.acceptAndCheck(root, root);
    }

    @Test
    public void testAncestorOrSelfFromParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent, parent);
    }

    @Test
    public void testAncestorOrSelfFromGrandChild() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.child(0).child(0), grandChild, child, parent);
    }

    @Test
    public void testAncestorOrSelfIgnoresDescendants() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent, parent);
    }

    @Test
    public void testAncestorOrSelfIgnoresSiblings() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.acceptAndCheck(parent.child(0), child1, parent);
    }

    @Test
    public void testAncestorOrSelfMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(0),
                TestNode.with("grand*1",
                        TestNode.with("parent1*0",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3")))
                        .child(0));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "ancestor-or-self::*");
    }

    @Override
    AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return AncestorOrSelfNodeSelector.get();
    }

    @Override
    public Class<AncestorOrSelfNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AncestorOrSelfNodeSelector.class);
    }
}
