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
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;

import static org.junit.Assert.assertEquals;

final public class NamedNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<NamedNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    private final static StringName NAME = Names.string("never");
    private final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');

    @Test(expected = NullPointerException.class)
    public void testWithNullPathSeparatorFails() {
        NamedNodeSelector.with(NAME, null);
    }

    @Test
    public void testRootDifferentName() {
        this.acceptAndCheck(TestFakeNode.node("root"));
    }

    @Test
    public void testRoot() {
        final TestFakeNode node = TestFakeNode.node("root");
        this.acceptAndCheck(node.name(), node, node);
    }

    @Test
    public void testChild() {
        final TestFakeNode grandChild = TestFakeNode.node("grandChild");
        final TestFakeNode child = TestFakeNode.node("child", grandChild);
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(child.name(), parent);
    }

    @Test
    public void testAllAncestorsFromParent() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);

        this.acceptAndCheck(parent.name(), child);
    }

    @Test
    public void testIgnoresSiblings() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");
        final TestFakeNode parent = TestFakeNode.node("parent", child1, child2);

        this.acceptAndCheck(child2.name(), parent.child(0));
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(this.createSelector(Names.string("different-name-2"),
                SEPARATOR,
                this.wrapped()));
    }

    @Test
    public void testEqualsDifferentPathSeparator() {
        this.checkNotEquals(this.createSelector(this.name(),
                PathSeparator.requiredAtStart('.'),
                this.wrapped()));
    }

    @Test
    public void testToString() {
        assertEquals(NAME.value(), this.createSelector().toString());
    }

    @Test
    public void testToStringPathSeparatorNotRequiredAtStart() {
        assertEquals(NAME.value(),  this.createSelector(PathSeparator.notRequiredAtStart('/')).toString());
    }

    @Override
    protected NamedNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return createSelector(NAME);
    }

    private NamedNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector(final StringName name) {
        return NamedNodeSelector.with(name, SEPARATOR);
    }

    private NamedNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector(final PathSeparator separator) {
        return NamedNodeSelector.with(NAME, separator);
    }

    final void acceptAndCheck(final StringName match, final TestFakeNode start, final TestFakeNode... nodes) {
        this.acceptAndCheck(this.createSelector(match), start, nodes);
    }

    @Override
    protected Class<NamedNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(NamedNodeSelector.class);
    }

    private NamedNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector(final StringName name,
                                                                                           final PathSeparator pathSeparator,
                                                                                           final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return Cast.to(NamedNodeSelector.<TestFakeNode, StringName, StringName, Object>with(name, pathSeparator).append(selector));
    }

    private StringName name() {
        return Names.string("name1");
    }
}
