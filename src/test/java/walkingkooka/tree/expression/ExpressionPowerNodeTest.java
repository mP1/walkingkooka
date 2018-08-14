package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ExpressionPowerNodeTest extends ExpressionArithmeticBinaryNodeTestCase<ExpressionPowerNode>{

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ExpressionNode> visited = Lists.array();

        final ExpressionPowerNode power = this.createExpressionNode();
        final ExpressionNode text1 = power.children().get(0);
        final ExpressionNode text2 = power.children().get(1);

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
            protected Visiting startVisit(final ExpressionPowerNode t) {
                assertSame(power, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ExpressionPowerNode t) {
                assertSame(power, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final ExpressionTextNode t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(power);
        assertEquals("1315215242", b.toString());
        assertEquals("visited",
                Lists.of(power, power,
                        text1, text1, text1,
                        text2, text2, text2,
                        power, power),
                visited);
    }

    // toBoolean...............................................................................................

    @Test
    public void testEvaluateToBooleanTrue() {
        // left ^^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(2), bigDecimal(3)), true);
    }

    @Test
    public void testEvaluateToBooleanFalse() {
        // left ^^ right == truthy number
        this.evaluateAndCheckBoolean(this.createExpressionNode(bigDecimal(0), bigDecimal(3)), false);
    }

    // toBigDecimal...............................................................................................
    
    @Test
    public void testEvaluateToBigDecimal() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(100), bigDecimal(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToBigDecimal2() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(100), bigInteger(2)), Math.pow(100, 2));
    }

    @Test
    public void testEvaluateToBigDecimal3() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(100), doubleValue(0.5)), (int)Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToBigDecimal4() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigDecimal(100), longValue(2)), (int)Math.pow(100, 2));
    }

    // toBigInteger....................................................................................................

    @Test
    public void testEvaluateToBigInteger() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(100), bigDecimal(2)), (int)Math.pow(100, 2));
    }

    @Test
    public void testEvaluateToBigInteger2() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(100), bigInteger(2)), (int)Math.pow(100, 2));
    }

    @Test
    public void testEvaluateToBigInteger3() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(bigInteger(100), doubleValue(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToBigInteger4() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(bigInteger(100), longValue(2)), (int)Math.pow(100, 2));
    }

    // toDouble....................................................................................................

    @Test
    public void testEvaluateToDouble() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(100), bigDecimal(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToDouble2() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(doubleValue(100), bigInteger(2)), Math.pow(100, 2));
    }

    @Test
    public void testEvaluateToDouble3() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(100), doubleValue(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToDouble4() {
        this.evaluateAndCheckDouble(this.createExpressionNode(doubleValue(100), longValue(2)), Math.pow(100, 2));
    }

    // toLong....................................................................................................

    @Test
    public void testEvaluateToLong() {
        this.evaluateAndCheckBigDecimal(this.createExpressionNode(longValue(100), bigDecimal(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToLong2() {
        this.evaluateAndCheckBigInteger(this.createExpressionNode(longValue(100), bigInteger(2)), (int)Math.pow(100, 2));
    }

    @Test
    public void testEvaluateToLong3() {
        this.evaluateAndCheckDouble(this.createExpressionNode(longValue(100), doubleValue(0.5)), Math.pow(100, 0.5));
    }

    @Test
    public void testEvaluateToLong4() {
        this.evaluateAndCheckLong(this.createExpressionNode(longValue(100), longValue(2)), (long)Math.pow(100, 2));
    }

    // toNumber.....................................................................................

    @Test
    public void testEvaluateToNumberBigDecimal() {
        this.evaluateAndCheckNumberBigDecimal(this.createExpressionNode(bigDecimal(2), bigDecimal(5)), (long)Math.pow(2, 5));
    }

    @Test
    public void testEvaluateToNumberBigInteger() {
        this.evaluateAndCheckNumberBigInteger(this.createExpressionNode(bigInteger(2), bigInteger(5)), (long)Math.pow(2, 5));
    }

    @Test
    public void testEvaluateToNumberDouble() {
        this.evaluateAndCheckNumberDouble(this.createExpressionNode(doubleValue(2), doubleValue(5)), (long)Math.pow(2, 5));
    }

    @Test
    public void testEvaluateToNumberLong() {
        this.evaluateAndCheckNumberLong(this.createExpressionNode(longValue(2), longValue(5)), (long)Math.pow(2, 5));
    }

    @Override
    ExpressionPowerNode createExpressionNode(final ExpressionNode left, final ExpressionNode right) {
        return ExpressionPowerNode.with(left, right);
    }

    @Override
    String expectedToString(){
        return LEFT_TO_STRING + "^^" + RIGHT_TO_STRING;
    }

    @Override
    Class<ExpressionPowerNode> expressionNodeType() {
        return ExpressionPowerNode.class;
    }
}
