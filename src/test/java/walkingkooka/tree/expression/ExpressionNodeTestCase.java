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

package walkingkooka.tree.expression;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConversionException;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTestCase2;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

public abstract class ExpressionNodeTestCase<N extends ExpressionNode> extends NodeTestCase2<ExpressionNode, ExpressionNodeName, Name, Object> {

    @Test
    public final void testCheckNaming() {
        this.checkNamingStartAndEnd("Expression", Node.class);
    }

    @Test
    public final void testPublicStaticFactoryMethod()  {
        this.publicStaticFactoryCheck(ExpressionNode.class, "Expression", Node.class);
    }

    @Test
    @Ignore
    public final void testSetSameAttributes() {
        // Ignored
    }

    @Test
    public final void testIsMethods() throws Exception {
        final String prefix = "Expression";
        final String suffix = Node.class.getSimpleName();

        final N node = this.createExpressionNode();
        final String name = node.getClass().getSimpleName();
        assertEquals(name + " starts with " + prefix, true, name.startsWith(prefix));
        assertEquals(name + " ends with " + suffix, true, name.endsWith(suffix));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for(Method method : node.getClass().getMethods()) {
            if(MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if(methodName.equals("isRoot")){
                continue;
            }
            if(!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(node));
        }
    }

    @Override
    protected ExpressionNode createNode() {
        return this.createExpressionNode();
    }

    abstract N createExpressionNode();

    @Override
    protected Class<ExpressionNode> type() {
        return Cast.to(this.expressionNodeType());
    }

    abstract Class<N> expressionNodeType();

    @Override
    protected ExpressionNode appendChildAndCheck(final ExpressionNode parent, final ExpressionNode child) {
        final N newParent = parent.appendChild(child).cast();
        assertNotSame("appendChild must not return the same node", newParent, parent);

        final List<N> children = Cast.to(newParent.children());
        assertNotEquals("children must have at least 1 child", 0, children.size());
        //assertEquals("last child must be the added child", child.name(), children.get(children.size() - 1).name());

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    // evaluation........................................................................................................

    final ExpressionBooleanNode booleanValue(final boolean value) {
        return ExpressionNode.booleanNode(value);
    }

    final ExpressionBigDecimalNode bigDecimal(final double value) {
        return ExpressionNode.bigDecimal(BigDecimal.valueOf(value));
    }

    final ExpressionBigIntegerNode bigInteger(final long value) {
        return ExpressionNode.bigInteger(BigInteger.valueOf(value));
    }

    final ExpressionDoubleNode doubleValue(final double value) {
        return ExpressionNode.doubleNode(value);
    }

    final ExpressionLongNode longValue(final long value) {
        return ExpressionNode.longNode(value);
    }

    final ExpressionTextNode text(final String value) {
        return ExpressionNode.text(value);
    }

    final void evaluateAndCheckBoolean(final ExpressionNode node, final boolean expected) {
        this.evaluateAndCheckBoolean(node, this.context(), expected);
    }

    final void evaluateAndCheckBoolean(final ExpressionNode node, final ExpressionEvaluationContext context, final boolean expected) {
        this.checkEquals("toBoolean of " + node + " failed", expected, node.toBoolean(context));
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckBigDecimal(node, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final BigDecimal expected) {
        this.evaluateAndCheckBigDecimal(node, this.context(), expected);
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.evaluateAndCheckBigDecimal(node, context, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckBigDecimal(final ExpressionNode node, final ExpressionEvaluationContext context, final BigDecimal expected) {
        this.checkEquals("toBigDecimal of " + node + " failed", expected, node.toBigDecimal(context));
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckBigInteger(node, BigInteger.valueOf(expected));
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final BigInteger expected) {
        this.evaluateAndCheckBigInteger(node, this.context(), expected);
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final ExpressionEvaluationContext context, final long expected) {
        this.evaluateAndCheckBigInteger(node, context, BigInteger.valueOf(expected));
    }

    final void evaluateAndCheckBigInteger(final ExpressionNode node, final ExpressionEvaluationContext context, final BigInteger expected) {
        this.checkEquals("toBigInteger of " + node + " failed", expected, node.toBigInteger(context));
    }

    final void evaluateAndCheckDouble(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckDouble(node, this.context(), expected);
    }

    final void evaluateAndCheckDouble(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.checkEquals("toDouble of " + node + " failed", expected, node.toDouble(context));
    }

    final void evaluateAndCheckLong(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckLong(node, this.context(), expected);
    }

    final void evaluateAndCheckLong(final ExpressionNode node, final ExpressionEvaluationContext context, final long expected) {
        this.checkEquals("toLong of " + node + " failed", expected, node.toLong(context));
    }

    final void evaluateAndCheckNumberBigDecimal(final ExpressionNode node, final ExpressionEvaluationContext context, final double expected) {
        this.evaluateAndCheckNumber(node, context, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckNumberBigDecimal(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckNumberBigDecimal(node, BigDecimal.valueOf(expected));
    }

    final void evaluateAndCheckNumberBigDecimal(final ExpressionNode node, final BigDecimal expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumberBigInteger(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckNumberBigInteger(node, BigInteger.valueOf(expected));
    }

    final void evaluateAndCheckNumberBigInteger(final ExpressionNode node, final BigInteger expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumberDouble(final ExpressionNode node, final double expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumberLong(final ExpressionNode node, final long expected) {
        this.evaluateAndCheckNumber(node, expected);
    }

    final void evaluateAndCheckNumber(final ExpressionNode node, final Number expected) {
        this.evaluateAndCheckNumber(node, this.context(), expected);
    }

    final void evaluateAndCheckNumber(final ExpressionNode node, final ExpressionEvaluationContext context, final Number expected) {
        this.checkEquals("toNumber of " + node + " failed", Cast.to(expected), Cast.to(node.toNumber(context)));
    }

    final void evaluateAndCheckText(final ExpressionNode node, final String expected) {
        this.evaluateAndCheckText(node, this.context(), expected);
    }

    final void evaluateAndCheckText(final ExpressionNode node, final ExpressionEvaluationContext context, final String expected) {
        this.checkEquals("toText of " + node + " failed", expected, node.toText(context));
    }

    private <T extends Comparable<T>> void checkEquals(final String message, final T expected, final T actual) {
        // necessary because BigDecimals of different precisions (extra zeros) will not be equal.
        if(0!=expected.compareTo(actual)){
            assertEquals(message, expected, actual);
        }
    }

    static ExpressionEvaluationContext context() {
        final Converter converters = Converters.collection(Lists.of(
                Converters.numberBigDecimal(),
                Converters.numberBigInteger(),
                Converters.numberDouble(),
                Converters.numberLong(),
                Converters.parser(BigDecimal.class,
                        Parsers.bigDecimal('.', MathContext.DECIMAL32),
                        FakeParserContext::new),
                Converters.parser(Number.class,
                        Cast.to(Parsers.bigDecimal('.', MathContext.DECIMAL32)),
                        FakeParserContext::new),
                Converters.parser(BigInteger.class,
                        Parsers.bigInteger(10),
                        FakeParserContext::new),
                Converters.parser(Double.class,
                        Parsers.doubleParser('.'),
                        FakeParserContext::new),
                Converters.parser(Long.class,
                        Parsers.longParser(10),
                        FakeParserContext::new),
                Converters.simple(),
                Converters.stringBoolean(),
                Converters.booleanConverter(Boolean.class, Boolean.FALSE, BigDecimal.class, BigDecimal.ONE, BigDecimal.ZERO),
                Converters.booleanConverter(Boolean.class, Boolean.FALSE, BigInteger.class, BigInteger.ONE, BigInteger.ZERO),
                Converters.booleanConverter(Boolean.class, Boolean.FALSE, Double.class, 1.0, 0.0),
                Converters.booleanConverter(Boolean.class, Boolean.FALSE, Long.class, 1L, 0L),
                Converters.truthyNumberBoolean(),
                Converters.string()));

        return new FakeExpressionEvaluationContext() {

            @Override
            public MathContext mathContext() {
                return this.matchContext;
            }

            private final MathContext matchContext = MathContext.DECIMAL64;

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                try {
                    if (!(value instanceof Boolean || value instanceof Number || value instanceof String)) {
                        fail("Cannot expects only Boolean | Number | String " + value.getClass().getName() + "=" + value);
                    }
                    return converters.convert(value, target);
                } catch ( final ConversionException fail) {
                    throw new ExpressionEvaluationConversionException(fail.getMessage(), fail);
                }
            }
        };
    }
}
