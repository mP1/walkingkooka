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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;

import java.util.List;

public final class OrNodeSelectorTest extends
        LogicalNodeSelectorTestCase<OrNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test
    public void testNothing() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);
        final TestFakeNode sibling = TestFakeNode.node("sibling");
        final TestFakeNode root = TestFakeNode.node("root", parent, sibling);

        this.acceptAndCheck(this.createSelector0(NamedNodeSelector.with(Names.string("unknown1"), SEPARATOR),
                NamedNodeSelector.with(Names.string("unknown1"), SEPARATOR)),
                root.child(0));
    }

    @Test
    public void testDifferentSelectors() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);
        final TestFakeNode sibling = TestFakeNode.node("sibling");
        final TestFakeNode root = TestFakeNode.node("root", parent, sibling);

        this.acceptAndCheck(this.createSelector0(
                ChildrenNodeSelector.get(),
                SelfNodeSelector.get()),
                root.child(0),
                child, parent);
    }

    @Test
    public void testOverlappingSelectors() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);
        final TestFakeNode sibling = TestFakeNode.node("sibling");
        final TestFakeNode root = TestFakeNode.node("root", parent, sibling);

        this.acceptAndCheck(this.createSelector0(ChildrenNodeSelector.get(),
                AncestorNodeSelector.get()),
                root.child(0),
                child, root);
    }

    @Override
    final NodeSelector<TestFakeNode, StringName, StringName, Object> createSelector0(
            final List<NodeSelector<TestFakeNode, StringName, StringName, Object>> selectors) {
        return OrNodeSelector.with(selectors);
    }

    @Override protected Class<OrNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(OrNodeSelector.class);
    }
}
