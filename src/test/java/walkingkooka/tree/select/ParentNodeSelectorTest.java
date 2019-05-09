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

final public class ParentNodeSelectorTest
        extends NonTerminalNodeSelectorTestCase<ParentNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testParentRoot() {
        this.applyAndCheck(TestNode.with("root"));
    }

    @Test
    public void testParentChildOfRoot() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0), parent);
    }

    @Test
    public void testParentIgnoresDescendants() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild); //
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(parent.child(0), parent);
    }

    @Test
    public void testParentIgnoresSiblings() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child1 = TestNode.with("child1", grandChild); //
        final TestNode child2 = TestNode.with("child2"); //
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.applyAndCheck(parent.child(1), parent);
    }

    @Test
    public void testParentIgnoresGrandParent() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild); //
        final TestNode parent = TestNode.with("parent", child);

        this.applyAndCheck(child.child(0), child);
    }

    @Test
    public void testParentMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent.child(0),
                TestNode.with("parent*0", TestNode.with("child"))
                        .child(0));
    }

    @Test
    public void testParentMapWithoutParent() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "..");
    }

    @Override
    ParentNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return ParentNodeSelector.get();
    }

    @Override
    public Class<ParentNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ParentNodeSelector.class);
    }
}
