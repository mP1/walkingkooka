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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.PredicateTestCase;
import walkingkooka.text.cursor.parser.select.NodeSelectorAttributeName;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;

import static org.junit.Assert.assertEquals;

public final class NodeSelectorPredicateTest extends PredicateTestCase<NodeSelectorPredicate<TestFakeNode, StringName, StringName, Object>, TestFakeNode> {

    private final static String ATTRIBUTE_NAME = "text";

    @Before
    public void beforeTest() {
        TestFakeNode.names.clear();
        ;
    }

    @Test
    public void testAttributePresentContainsText() {
        this.testTrue(node("1", "abc123"));
    }

    @Test
    public void testAttributePresentWithoutText() {
        this.testFalse(node("1", "xyz"));
    }

    @Test
    public void testAttributeAbsent() {
        this.testFalse(TestFakeNode.node("1"));
    }

    private static TestFakeNode node(final String name, final String attribute, final TestFakeNode... nodes) {
        return TestFakeNode.node(name)
                .setAttributes(Maps.one(Names.string(ATTRIBUTE_NAME), attribute))
                .setChildren(Lists.of(nodes));
    }

    @Test
    public void testToString() {
        assertEquals(this.node().toString(), this.createPredicate().toString());
    }

    @Override
    protected NodeSelectorPredicate<TestFakeNode, StringName, StringName, Object> createPredicate() {
        return NodeSelectorPredicate.with(this.node());
    }

    private ExpressionNode node() {
        return ExpressionNode.function(ExpressionNodeName.with("contains"),
                Lists.of(ExpressionNode.reference(this.attributeName()), ExpressionNode.text("abc")));
    }

    private NodeSelectorAttributeName attributeName() {
        return NodeSelectorAttributeName.with(ATTRIBUTE_NAME);
    }

    @Override
    protected Class<NodeSelectorPredicate<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorPredicate.class);
    }
}
