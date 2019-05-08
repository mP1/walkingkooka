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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.TestNode;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;


final public class NodePredicateNodeSelectorTest extends
        NonTerminalNodeSelectorTestCase<NodePredicateNodeSelector<TestNode, StringName, StringName, Object>> {

    // constants

    private final static String MAGIC_VALUE = "self";
    private final static Predicate<TestNode> PREDICATE = (n) -> n.name().value().equals(MAGIC_VALUE);

    @Test
    public void testWithNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePredicateNodeSelector.with(null);
        });
    }

    @Test
    public void testPredicate() {
        final TestNode self = TestNode.with("self");
        this.acceptAndCheck(self, self);
    }

    @Test
    public void testPredicateIgnoresNonSelfNodes() {
        final TestNode siblingBefore = TestNode.with("siblingBefore");
        final TestNode self = TestNode.with(MAGIC_VALUE, TestNode.with("child"));
        final TestNode siblingAfter = TestNode.with("siblingAfter");
        final TestNode parent = TestNode.with("parent", siblingBefore, self, siblingAfter);

        this.acceptAndCheck(parent.child(1), self);
    }

    @Test
    public void testDescendantOrSelfPredicate() {
        final TestNode grand1 = nodeWithAttributes("grand1", "a1", "v1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2).setAttributes(this.attributes("a1", "v1"));

        final TestNode grand3 = nodeWithAttributes("grand3", "different", "v1");
        final TestNode grand4 = nodeWithAttributes("grand4", "a1", "v1");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.acceptAndCheck(TestNode.absoluteNodeSelector().descendantOrSelf().attributeValueEquals(Names.string("a1"), "v1"),
                TestNode.with("parent", child1, child2),
                child1, grand1, grand4);
    }

    @Test
    public void testPredicateChildren() {
        final TestNode grand1 = nodeWithAttributes("grand1", "a1", "v1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2).setAttributes(this.attributes("a1", "v1"));

        this.acceptAndCheck(TestNode.relativeNodeSelector().attributeValueEquals(Names.string("a1"), "v1").children(),
                child1,
                grand1, grand2);
    }

    @Test
    public void testDescendantOrSelfPredicateChildren() {
        final TestNode grand1 = nodeWithAttributes("grand1", "a1", "v1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2).setAttributes(this.attributes("a1", "v1"));

        final TestNode grand3 = nodeWithAttributes("grand3", "different", "v1");
        final TestNode grand4 = nodeWithAttributes("grand4", "a1", "v1");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.acceptAndCheck(TestNode.relativeNodeSelector().descendantOrSelf().attributeValueEquals(Names.string("a1"), "v1").children(),
                TestNode.with("parent", child1, child2),
                grand1, grand2);
    }

    @Test
    public void testPredicateMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with(MAGIC_VALUE), TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent.child(0),
                TestNode.with("parent", TestNode.with(MAGIC_VALUE + "*0"), TestNode.with("child"))
                        .child(0));
    }

    @Test
    public void testEqualsDifferentPredicate() {
        this.checkNotEquals(this.createSelector(Predicates.fake(), this.wrapped()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "*[" + PREDICATE.toString() + "]");
    }

    @Override
    NodePredicateNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return NodePredicateNodeSelector.with(PREDICATE);
    }

    @Override
    public Class<NodePredicateNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodePredicateNodeSelector.class);
    }

    private NodePredicateNodeSelector<TestNode, StringName, StringName, Object> createSelector(final Predicate<TestNode> node,
                                                                                               final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(NodePredicateNodeSelector.with(node).append(selector));
    }
}
