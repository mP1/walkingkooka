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

import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * A multiplication expression.
 */
public final class ExpressionMultiplicationNode extends ExpressionArithmeticBinaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionMultiplicationNode.class);

    public final static String SYMBOL = "*";

    static ExpressionMultiplicationNode with(final ExpressionNode left, final ExpressionNode right) {
        check(left, right);
        return new ExpressionMultiplicationNode(NO_INDEX, left, right);
    }

    private ExpressionMultiplicationNode(final int index, final ExpressionNode left, final ExpressionNode right) {
        super(index, left, right);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionMultiplicationNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public ExpressionMultiplicationNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionMultiplicationNode wrap1(final int index, final ExpressionNode left, final ExpressionNode right) {
        return new ExpressionMultiplicationNode(index, left, right);
    }
    // is .........................................................................................................

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isDivision() {
        return false;
    }

    @Override
    public boolean isModulo() {
        return false;
    }

    @Override
    public boolean isMultiplication() {
        return true;
    }

    @Override
    public boolean isPower() {
        return false;
    }

    @Override
    public boolean isSubtraction() {
        return false;
    }

    // Visitor .........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Evaluation .......................................................................................................

    @Override
    String applyText0(final String left, final String right, final ExpressionEvaluationContext context) {
        throw new UnsupportedOperationException(left + SYMBOL + right); // MAYBE try and convert right to int and times the string.
    }

    @Override
    BigDecimal applyBigDecimal0(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context) {
        return left.multiply(right, context.mathContext());
    }

    @Override
    BigInteger applyBigInteger0(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context) {
        return left.multiply(right);
    }

    @Override
    double applyDouble0(final double left, final double right, final ExpressionEvaluationContext context) {
        return left * right;
    }

    @Override
    long applyLong0(final long left, final long right, final ExpressionEvaluationContext context) {
        return left * right;
    }

    // HasJsonNode....................................................................................................

    // @VisibleForTesting
    static ExpressionMultiplicationNode fromJsonNode(final JsonNode node) {
        final JsonArrayNode array = node.arrayOrFail();

        return ExpressionMultiplicationNode.with(
                array.get(0).fromJsonNodeWithType(),
                array.get(1).fromJsonNodeWithType());
    }

    static {
        register(SYMBOL, ExpressionMultiplicationNode::fromJsonNode, ExpressionMultiplicationNode.class);
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionMultiplicationNode;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
