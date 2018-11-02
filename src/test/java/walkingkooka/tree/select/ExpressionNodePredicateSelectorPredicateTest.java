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
import walkingkooka.predicate.PredicateTestCase;
import walkingkooka.text.cursor.parser.select.NodeSelectorAttributeName;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class ExpressionNodePredicateSelectorPredicateTest extends PredicateTestCase<ExpressionNodeSelectorPredicate, ExpressionEvaluationContext> {

    private final static String ATTRIBUTE_NAME = "text";

    @Before
    public void beforeTest() {
        TestFakeNode.names.clear();
    }

    @Test
    public void testAttributePresentContainsText() {
        this.testTrue(context(node("1", "abc123")));
    }

    @Test
    public void testAttributePresentWithoutText() {
        this.testFalse(context(node("1", "xyz")));
    }

    @Test
    public void testAttributeAbsent() {
        this.testFalse(context(TestFakeNode.node("1")));
    }

    private static TestFakeNode node(final String name, final String attribute, final TestFakeNode... nodes) {
        return TestFakeNode.node(name)
                .setAttributes(Maps.one(Names.string(ATTRIBUTE_NAME), attribute))
                .setChildren(Lists.of(nodes));
    }

    private static ExpressionEvaluationContext context(final TestFakeNode node) {
        return new FakeExpressionEvaluationContext() {

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
                assertEquals("attributeName", NodeSelectorAttributeName.with(ATTRIBUTE_NAME), reference);

                final String value = (String) node.attributes().get(Names.string(ATTRIBUTE_NAME));
                return Optional.of(ExpressionNode.text(null==value ? "" : value));
            }

            @Override
            public Object function(final ExpressionNodeName name, final List<Object> parameters) {
                return String.valueOf(parameters.get(0)).contains(String.valueOf(parameters.get(1)));
            }

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                return target.cast(value);
            }
        };
    }

    @Test
    public void testToString() {
        assertEquals(this.expression().toString(), this.createPredicate().toString());
    }

    @Override
    protected ExpressionNodeSelectorPredicate createPredicate() {
        return ExpressionNodeSelectorPredicate.with(this.expression());
    }

    private ExpressionNode expression() {
        return ExpressionNode.function(ExpressionNodeName.with("contains"),
                Lists.of(ExpressionNode.reference(this.attributeName()), ExpressionNode.text("abc")));
    }

    private NodeSelectorAttributeName attributeName() {
        return NodeSelectorAttributeName.with(ATTRIBUTE_NAME);
    }

    @Override
    protected Class<ExpressionNodeSelectorPredicate> type() {
        return Cast.to(ExpressionNodeSelectorPredicate.class);
    }
}
