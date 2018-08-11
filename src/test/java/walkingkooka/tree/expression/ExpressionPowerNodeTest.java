package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class ExpressionPowerNodeTest extends ExpressionBinaryNodeTestCase<ExpressionPowerNode>{

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
