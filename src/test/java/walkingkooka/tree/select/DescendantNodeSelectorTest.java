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

final public class DescendantNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<DescendantNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestNode.with("only"));
    }

    @Test
    public void testIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(child);
    }

    @Test
    public void testParentWithChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        this.acceptAndCheck(TestNode.with("parent", child1, child2), child1, child2);
    }

    @Test
    public void testParentWithGrandChildren() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");

        final TestNode child1 = TestNode.with("child1", grandChild1);
        final TestNode child2 = TestNode.with("child2", grandChild2);

        this.acceptAndCheck(TestNode.with("parent", child1, child2), child1, grandChild1, child2, grandChild2);
    }

    @Test
    public void testChildrenWithChildren2() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");

        final TestNode child1 = TestNode.with("child1", grandChild1, grandChild2);
        final TestNode child2 = TestNode.with("child2");

        this.acceptAndCheck(TestNode.with("parent", child1, child2), child1, grandChild1, grandChild2, child2);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);
        final TestNode siblingOfParent = TestNode.with("siblingOfParent", child);
        final TestNode grandParent = TestNode.with("grandParent", parent, siblingOfParent);

        this.acceptAndCheck(parent, child);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "descendant::*");
    }

    @Override
    DescendantNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return DescendantNodeSelector.get();
    }

    @Override
    public Class<DescendantNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(DescendantNodeSelector.class);
    }
}
