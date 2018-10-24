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
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class AbsoluteNodeSelectorTest extends
        UnaryNodeSelectorTestCase<AbsoluteNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    private final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');
    private final static Predicate<TestFakeNode> PREDICATE = Predicates.customToString(Predicates.always(), "always");

    @Test(expected = NullPointerException.class)
    public void testWithNullPathSeparatorFails() {
        AbsoluteNodeSelector.with(null);
    }

    @Test
    public void testAppendDescendant() {
        final DescendantNodeSelector<TestFakeNode, StringName, StringName, Object> descendantNodeSelector = DescendantNodeSelector.with(SEPARATOR);
        assertSame(descendantNodeSelector, this.createSelector().append(descendantNodeSelector));
    }

    @Test
    public void testRoot() {
        final TestFakeNode root = TestFakeNode.node("root");
        this.acceptAndCheck(root, root);
    }

    @Test
    public void testIgnoresDescedants() {
        final TestFakeNode grandChild = TestFakeNode.node("grandChild");
        final TestFakeNode child = TestFakeNode.node("child!", grandChild);
        final TestFakeNode parent = TestFakeNode.node("parent!", child);

        this.acceptAndCheck(parent, parent);
    }

    @Test
    public void testStartUnimportant() {
        final TestFakeNode grandChild = TestFakeNode.node("grandChild");
        final TestFakeNode child = TestFakeNode.node("child!", grandChild);
        final TestFakeNode parent = TestFakeNode.node("parent!", child);

        this.acceptAndCheck(parent.child(0), parent);
    }

    @Test
    public void testToString() {
        assertEquals("/", this.createSelector().toString());
    }

    @Test
    public void testToString2() {
        assertEquals("/*[" + PREDICATE + "]", this.createSelector2().toString());
    }

    @Test
    public void testToStringPathSeparatorNotRequiredAtStart() {
        final PathSeparator separator = PathSeparator.notRequiredAtStart('/');
        assertEquals("/", this.createSelector(separator).toString());
    }

    @Override
    protected AbsoluteNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return this.createSelector(SEPARATOR);
    }

    private AbsoluteNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector(final PathSeparator separator) {
        return AbsoluteNodeSelector.with(separator);
    }

    private NodeSelector<TestFakeNode, StringName, StringName, Object> createSelector2() {
        return this.createSelector().append(PredicateNodeSelector.with(PREDICATE));
    }

    @Override
    protected Class<AbsoluteNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(AbsoluteNodeSelector.class);
    }
}
