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

import org.junit.Before;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.Names;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.PredicateTestCase;

public final class NodeSelectorNodeAttributeValuePredicateTest extends PredicateTestCase<NodeSelectorNodeAttributeValuePredicate<TestFakeNode, StringName, StringName, Object>,
        TestFakeNode> {

    private final static StringName NAME = Names.string("match!");

    @Before
    public void resetTestFakeNodeNames() {
        TestFakeNode.names.clear();
    }

    @Test
    public void testUnmatched() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node("parent", child);
        this.testFalse(parent);
    }

    @Test
    public void testUnmatched2() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node(NAME.value(), child);
        this.testFalse(parent.child(0));
    }

    @Test
    public void testMatched() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node(NAME.value(), child);
        this.testTrue(parent);
    }

    @Override
    protected NodeSelectorNodeAttributeValuePredicate<TestFakeNode, StringName, StringName, Object> createPredicate() {
        return new NodeSelectorNodeAttributeValuePredicate(NamedNodeSelector.with(NAME, PathSeparator.requiredAtStart('/')));
    }

    @Override
    protected Class<NodeSelectorNodeAttributeValuePredicate<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorNodeAttributeValuePredicate.class);
    }
}
