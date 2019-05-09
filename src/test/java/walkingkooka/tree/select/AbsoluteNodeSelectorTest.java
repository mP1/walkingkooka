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

import static org.junit.jupiter.api.Assertions.assertSame;

final public class AbsoluteNodeSelectorTest extends
        NonTerminalNodeSelectorTestCase<AbsoluteNodeSelector<TestNode, StringName, StringName, Object>> {

    private final static PathSeparator SEPARATOR = PathSeparator.requiredAtStart('/');
    private final static Predicate<TestNode> PREDICATE = Predicates.customToString(Predicates.always(), "always");

    @Test
    public void testAppendDescendant() {
        final DescendantNodeSelector<TestNode, StringName, StringName, Object> descendantNodeSelector = DescendantNodeSelector.get();
        assertSame(descendantNodeSelector, this.createSelector().append(descendantNodeSelector));
    }

    @Test
    public void testAbsoluteRoot() {
        final TestNode root = TestNode.with("root");
        this.applyAndCheck(root, root);
    }

    @Test
    public void testAbsoluteIgnoresDescendants() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child!", grandChild);
        final TestNode parent = TestNode.with("parent!", child);

        this.applyAndCheck(parent, parent);
    }

    @Test
    public void testAbsoluteStartUnimportant() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child!", grandChild);
        final TestNode parent = TestNode.with("parent!", child);

        this.applyAndCheck(parent.child(0), parent);
    }

    @Test
    public void testAbsoluteStartUnimportantCustomToString() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child!", grandChild);
        final TestNode parent = TestNode.with("parent!", child);

        this.applyAndCheck(this.createSelector().setToString("CustomToString"), parent.child(0), parent);
    }

    @Test
    public void testAbsoluteMap() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent.child(1),
                TestNode.with("grand*0",
                        TestNode.with("parent1",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3")))
                        .child(1));
    }

    @Test
    public void testAbsoluteMap2() {
        final TestNode grandParent = TestNode.with("grand",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2")),
                TestNode.with("parent2", TestNode.with("child3")));

        TestNode.clear();

        this.acceptMapAndCheck(grandParent,
                TestNode.with("grand*0",
                        TestNode.with("parent1",
                                TestNode.with("child1"), TestNode.with("child2")),
                        TestNode.with("parent2", TestNode.with("child3"))));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "/");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createSelector2(), "/*[" + PREDICATE + "]");
    }

    @Override
    AbsoluteNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return AbsoluteNodeSelector.get();
    }

    private NodeSelector<TestNode, StringName, StringName, Object> createSelector2() {
        return this.createSelector().predicate(PREDICATE);
    }

    @Override
    public Class<AbsoluteNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AbsoluteNodeSelector.class);
    }
}
