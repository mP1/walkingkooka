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

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class DescendantOrSelfNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<DescendantOrSelfNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testWithNullPathSeparatorFails() {
        assertThrows(NullPointerException.class, () -> {
            DescendantOrSelfNodeSelector.with(null);
        });
    }

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
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "//");
    }

    @Override
    DescendantOrSelfNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return DescendantOrSelfNodeSelector.with(SEPARATOR);
    }

    @Override
    public Class<DescendantOrSelfNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(DescendantOrSelfNodeSelector.class);
    }
}
