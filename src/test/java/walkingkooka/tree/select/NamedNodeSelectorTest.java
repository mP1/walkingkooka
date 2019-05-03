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

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class NamedNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<NamedNodeSelector<TestNode, StringName, StringName, Object>> {

    private final static StringName NAME = Names.string("never");

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            NamedNodeSelector.with(null);
        });
    }

    @Test
    public void testRootDifferentName() {
        this.acceptAndCheck(TestNode.with("root"));
    }

    @Test
    public void testRoot() {
        final TestNode node = TestNode.with("root");
        this.acceptAndCheck(node.name(), node, node);
    }

    @Test
    public void testChild() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(child.name(), parent);
    }

    @Test
    public void testAllAncestorsFromParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(parent.name(), child);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.acceptAndCheck(child2.name(), parent.child(0));
    }

    @Test
    public void testMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with(NAME.value()), TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent.child(0),
                TestNode.with("parent", TestNode.with(NAME.value() + "*0"), TestNode.with("child"))
                        .child(0));
    }

    @Test
    public void testMapUnmatched() {
        this.acceptMapAndCheck(TestNode.with("different"));
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(this.createSelector(Names.string("different-name-2"), this.wrapped()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), NAME.value());
    }

    @Override
    NamedNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return createSelector(NAME);
    }

    private NamedNodeSelector<TestNode, StringName, StringName, Object> createSelector(final StringName name) {
        return NamedNodeSelector.with(name);
    }

    final void acceptAndCheck(final StringName match, final TestNode start, final TestNode... nodes) {
        this.acceptAndCheck(this.createSelector(match), start, nodes);
    }

    @Override
    public Class<NamedNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NamedNodeSelector.class);
    }

    private NamedNodeSelector<TestNode, StringName, StringName, Object> createSelector(final StringName name,
                                                                                       final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(NamedNodeSelector.<TestNode, StringName, StringName, Object>with(name).append(selector));
    }
}
