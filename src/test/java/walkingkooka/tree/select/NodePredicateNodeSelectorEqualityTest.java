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

import org.junit.Before;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;

import java.util.function.Predicate;

public final class NodePredicateNodeSelectorEqualityTest extends NonLogicalNodeSelectorEqualityTestCase<NodePredicateNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Before
    public void beforeEachTest() {
        TestFakeNode.names.clear();
    }

    @Test
    public void testDifferentPredicate() {
        this.checkNotEquals(this.createNodeSelector(Predicates.fake(), this.wrapped()));
    }

    @Override
    NodePredicateNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return this.createNodeSelector(this.predicate(), selector);
    }

    private NodePredicateNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final Predicate<TestFakeNode> node,
                                                                                                       final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return Cast.to(NodePredicateNodeSelector.with(node).append(selector));
    }

    private Predicate<TestFakeNode> predicate() {
        return Predicates.is(this.node);
    }

    private final TestFakeNode node = TestFakeNode.node("node1");
}
