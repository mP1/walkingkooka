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
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.StringName;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionNodeSelectorNodeSelectorContext2Test extends NodeSelectorContext2TestCase<ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object>,
        TestNode, StringName, StringName, Object> {

    private final static int INDEX = 5;

    @Test
    public void testAll() {
        final ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        this.checkType(this.createContext().all(), AllNodeSelectorContext2.class);
    }

    @Test
    public void testExpression() {
        final ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        final NodeSelectorContext2<TestNode, StringName, StringName, Object> expression = context.expression();
        assertNotSame(context, expression);
        this.checkType(expression, ExpressionNodeSelectorNodeSelectorContext2.class);
    }

    @Test
    public void testExpressionCreateIfNecessary() {
        final ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        assertSame(context, context.expressionCreateIfNecessary());
    }

    @Test
    public void testNodePositionBooleanFalse() {
        this.nodePositionTestAndCheck(false, false);
    }

    @Test
    public void testNodePositionBooleanTrue() {
        this.nodePositionTestAndCheck(true, true);
    }

    @Test
    public void testNodePositionLongDifferent() {
        this.nodePositionTestAndCheck(Long.valueOf(INDEX + 99), false);
    }

    @Test
    public void testNodePositionLongEqual() {
        this.nodePositionTestAndCheck(Long.valueOf(INDEX), true);
    }

    @Test
    public void testNodePositionStringDifferent() {
        this.nodePositionTestAndCheck(String.valueOf(INDEX + 99), false);
    }

    @Test
    public void testNodePositionStringEqual() {
        this.nodePositionTestAndCheck(String.valueOf(INDEX), true);
    }

    private void nodePositionTestAndCheck(final Object value,
                                          final boolean expected) {
        assertEquals(expected,
                this.createContext().nodePositionTest(value),
                () -> "value: " + CharSequences.quoteIfChars(value));
    }

    @Test
    public void testNodePosition() {
        final ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        assertEquals(INDEX,
                context.nodePosition(),
                () -> "nodePosition in " + context);
    }

    @Override
    public ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> createContext() {
        final ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = ExpressionNodeSelectorNodeSelectorContext2.with(new FakeNodeSelectorContext<TestNode, StringName, StringName, Object>() {

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                assertEquals(Integer.class, target, "target");

                return Converters.collection(Lists.of(
                        Converters.numberInteger(),
                        Converters.function(String.class, Integer.class, Integer::parseInt)))
                        .convert(value, target, this.converterContext());
            }

            private ConverterContext converterContext() {
                return ConverterContexts.basic(DecimalNumberContexts.basic("$", '.', 'E', ',', '-', '%', '+'));
            }
        });
        context.position = INDEX;
        return context;
    }

    @Test
    public void testToString() {
        final ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = ExpressionNodeSelectorNodeSelectorContext2.with(this.contextWithToString("Context123"));
        context.position = 45;
        this.toStringAndCheck(context, "position: 45 Context123");
    }

    @Override
    public Class<ExpressionNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(ExpressionNodeSelectorNodeSelectorContext2.class);
    }
}