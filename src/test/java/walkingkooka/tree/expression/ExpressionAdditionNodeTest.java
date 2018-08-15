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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ExpressionAdditionNodeTest extends ExpressionArithmeticBinaryNodeTestCase<ExpressionAdditionNode>{

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionAdditionNode addition = this.createExpressionNode();
        final ExpressionNode text1 = addition.children().get(0);
        final ExpressionNode text2 = addition.children().get(1);

        new FakeExpressionNodeVisitor() {
            @Override
            protected Visiting startVisit(final ExpressionNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final ExpressionAdditionNode t) {
                assertSame(addition, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionAdditionNode t) {
                assertSame(addition, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(addition);
        assertEquals("1315215242", b.toString());
        assertEquals("visited",
                Lists.of(addition, addition,
                        text1, text1, text1,
                        text2, text2, text2,
                        addition, addition),
                visited);
    }

    // toBoolean.....................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left + right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigDecimal(34)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left + right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(12), bigDecimal(-12)), false);
    }

    // toBigDecimal.....................................................................................

    @Test
    public void testEvaluateToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), bigDecimal(34)), 12+34);
    }

    @Test
    public void testEvaluateToBigDecimal2() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), bigInteger(34)), 12+34);
    }

    @Test
    public void testEvaluateToBigDecimal3() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), doubleValue(34)), 12+34);
    }

    @Test
    public void testEvaluateToBigDecimal4() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(12), longValue(34)), 12+34);
    }

    // toBigInteger.....................................................................................

    @Test
    public void testEvaluateToBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(12), bigDecimal(34)), 12+34);
    }

    @Test
    public void testEvaluateToBigInteger2() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(12), bigInteger(34)), 12+34);
    }

    @Test
    public void testEvaluateToBigInteger3() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(12), doubleValue(34)), 12+34);
    }

    @Test
    public void testEvaluateToBigInteger4() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(12), longValue(34)), 12+34);
    }

    // toDouble.....................................................................................

    @Test
    public void testEvaluateToDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(12), bigDecimal(34)), 12.0+34.0);
    }

    @Test
    public void testEvaluateToDouble2() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(12), bigInteger(34)), 12.0+34.0);
    }

    @Test
    public void testEvaluateToDouble3() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(12), doubleValue(34)), 12.0+34.0);
    }

    @Test
    public void testEvaluateToDouble4() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(12), longValue(34)), 12.0+34.0);
    }

    // toLong...............................................................................................

    @Test
    public void testEvaluateToLong() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(longValue(12), bigDecimal(34)), 12L + 34L);
    }

    @Test
    public void testEvaluateToLong2() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(longValue(12), bigInteger(34)), 12L + 34L);
    }

    @Test
    public void testEvaluateToLong3() {
        this.evaluateAndCheckDouble(this.createExpressionNode(longValue(12), doubleValue(34)), 12L + 34.0);
    }

    @Test
    public void testEvaluateToLong4() {
        this.evaluateAndCheckLong(this.createExpressionNode(longValue(12), longValue(34)), 12L + 34L);
    }

    // toNumber.....................................................................................

    @Test
    public void testEvaluateToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(bigDecimal(12), bigDecimal(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(bigInteger(12), bigInteger(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToNumberDouble() {
        this.evaluateAndCheckNumberDouble(this.createExpressionNode(doubleValue(12), doubleValue(34)), 12 + 34);
    }

    @Test
    public void testEvaluateToNumberLong() {
        this.evaluateAndCheckNumberLong(this.createExpressionNode(longValue(12), longValue(34)), 12 + 34);
    }

    @Override
    ExpressionAdditionNode createExpressionNode(final ExpressionNode left, final ExpressionNode right) {
        return ExpressionAdditionNode.with(left, right);
    }

    @Override
    String expectedToString(){
        return LEFT_TO_STRING + "+" + RIGHT_TO_STRING;
    }

    @Override
    Class<ExpressionAdditionNode> expressionNodeType() {
        return ExpressionAdditionNode.class;
    }
}
