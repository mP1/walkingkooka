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
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class AbsoluteNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<AbsoluteNodeSelector<TestNode, StringName, StringName, Object>> {

    private final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');
    private final static Predicate<TestNode> PREDICATE = Predicates.customToString(Predicates.always(), "always");

    @Test
    public void testWithNullPathSeparatorFails() {
        assertThrows(NullPointerException.class, () -> {
            AbsoluteNodeSelector.with(null);
        });
    }

    @Test
    public void testAppendDescendant() {
        final DescendantNodeSelector<TestNode, StringName, StringName, Object> descendantNodeSelector = DescendantNodeSelector.get();
        assertSame(descendantNodeSelector, this.createSelector().append(descendantNodeSelector));
    }

    @Test
    public void testRoot() {
        final TestNode root = TestNode.with("root");
        this.acceptAndCheck(root, root);
    }

    @Test
    public void testIgnoresDescedants() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child!", grandChild);
        final TestNode parent = TestNode.with("parent!", child);

        this.acceptAndCheck(parent, parent);
    }

    @Test
    public void testStartUnimportant() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child!", grandChild);
        final TestNode parent = TestNode.with("parent!", child);

        this.acceptAndCheck(parent.child(0), parent);
    }

    @Test
    public void testStartUnimportantCustomToString() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child!", grandChild);
        final TestNode parent = TestNode.with("parent!", child);

        this.acceptAndCheck(this.createSelector().setToString("CustomToString"), parent.child(0), parent);
    }

    @Test
    public void testEqualsDifferentPathSeparator() {
        this.checkNotEquals(this.createSelector(PathSeparator.requiredAtStart('.'),
                this.wrapped()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "/");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createSelector2(), "/*[" + PREDICATE + "]");
    }

    @Test
    public void testToStringPathSeparatorNotRequiredAtStart() {
        final PathSeparator separator = PathSeparator.notRequiredAtStart('/');
        this.toStringAndCheck(this.createSelector(separator), "/");
    }

    @Override
    protected AbsoluteNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return this.createSelector(SEPARATOR);
    }

    private AbsoluteNodeSelector<TestNode, StringName, StringName, Object> createSelector(final PathSeparator separator) {
        return AbsoluteNodeSelector.with(separator);
    }

    private NodeSelector<TestNode, StringName, StringName, Object> createSelector2() {
        return this.createSelector().append(NodeSelector.nodePredicate(PREDICATE));
    }

    @Override
    public Class<AbsoluteNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AbsoluteNodeSelector.class);
    }

    private AbsoluteNodeSelector<TestNode, StringName, StringName, Object> createSelector(final PathSeparator pathSeparator,
                                                                                          final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(AbsoluteNodeSelector.<TestNode, StringName, StringName, Object>with(pathSeparator).append(selector));
    }
}
