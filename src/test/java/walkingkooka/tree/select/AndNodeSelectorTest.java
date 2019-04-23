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

import java.util.List;

public final class AndNodeSelectorTest extends
        LogicalNodeSelectorTestCase<AndNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testNothingSelected() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode sibling = TestNode.with("sibling");
        final TestNode root = TestNode.with("root", parent, sibling);

        this.acceptAndCheck(this.createSelector0(NamedNodeSelector.with(Names.string("unknown1"), SEPARATOR),
                NamedNodeSelector.with(Names.string("unknown1"), SEPARATOR)),
                root.child(0));
    }

    @Test
    public void testDifferentSelectors() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode sibling = TestNode.with("sibling");
        final TestNode root = TestNode.with("root", parent, sibling);

        this.acceptAndCheck(this.createSelector0(ChildrenNodeSelector.get(), AncestorNodeSelector.get()),
                root.child(0));
    }

    @Test
    public void testOverlappingSelectors() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode sibling = TestNode.with("sibling");
        final TestNode root = TestNode.with("root", parent, sibling);

        this.acceptAndCheck(this.createSelector0(ChildrenNodeSelector.get(), DescendantNodeSelector.get()),
                root.child(0),
                child);
    }

    @Test
    public void testMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        // children selects $child1 & $child2, index(1) only selects $child1 giving $child1.
        this.acceptMapAndCheck(this.createSelector0(ChildrenNodeSelector.get(), NodeSelector.indexedChild(1)),
                parent, // parent1
                TestNode.with("parent", TestNode.with("child1*0"), TestNode.with("child2")));
    }

    @Test
    public void testMap2() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        // children selects $child1 & $child2, index(2) only selects $child2 giving $child2.
        this.acceptMapAndCheck(this.createSelector0(ChildrenNodeSelector.get(), NodeSelector.indexedChild(2)),
                parent, // parent1
                TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2*0")));
    }

    @Test
    public void testMap3() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        // children and descendant both select all children.
        this.acceptMapAndCheck(this.createSelector0(ChildrenNodeSelector.get(), NodeSelector.descendant()),
                parent, // parent1
                TestNode.with("parent", TestNode.with("child1*0"), TestNode.with("child2*1")));
    }

    @Override
    NodeSelector<TestNode, StringName, StringName, Object> createSelector0(final List<NodeSelector<TestNode, StringName, StringName, Object>> selectors) {
        return AndNodeSelector.with(selectors);
    }

    @Override
    public Class<AndNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AndNodeSelector.class);
    }
}
