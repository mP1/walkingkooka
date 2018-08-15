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

import walkingkooka.Cast;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Base class for all arithmetic {@link ExpressionBinaryNode} nodes such as addition, power etc.
 */
abstract class ExpressionArithmeticBinaryNode extends ExpressionBinaryNode {

    ExpressionArithmeticBinaryNode(final int index, final ExpressionNode left, final ExpressionNode right){
        super(index, left, right);
    }

    // is .........................................................................................................

    @Override
    public final boolean isAnd() {
        return false;
    }

    @Override
    public final boolean isEquals() {
        return false;
    }

    @Override
    public final boolean isGreaterThan() {
        return false;
    }

    @Override
    public final boolean isGreaterThanEquals() {
        return false;
    }

    @Override
    public final boolean isLessThan() {
        return false;
    }

    @Override
    public final boolean isLessThanEquals() {
        return false;
    }

    @Override
    public final boolean isNotEquals() {
        return false;
    }

    @Override
    public final boolean isOr() {
        return false;
    }

    @Override
    public final boolean isXor() {
        return false;
    }

    // evaluation .....................................................................................................

    @Override
    public final Number toValue(final ExpressionEvaluationContext context) {
        return this.toNumber(context);
    }

    /**
     * Arithmetic operations accept all number types.
     */
    final Class<Number> commonNumberType(final Class<? extends Number> type) {
        return Cast.to(type);
    }

    final ExpressionNode applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context) {
        return ExpressionNode.bigDecimal(this.applyBigDecimal0(left, right, context));
    }

    abstract BigDecimal applyBigDecimal0(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context);

    @Override
    final ExpressionNode applyBigInteger(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context) {
        return ExpressionNode.bigInteger(this.applyBigInteger0(left, right, context));
    }

    abstract BigInteger applyBigInteger0(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context);

    @Override
    final ExpressionNode applyDouble(final double left, final double right, final ExpressionEvaluationContext context) {
        return ExpressionNode.doubleNode(Double.isFinite(left) && Double.isFinite(right) ?
                this.applyDouble0(left, right, context) :
                left);
    }

    abstract double applyDouble0(final double left, final double right, final ExpressionEvaluationContext context);

    @Override
    final ExpressionNode applyLong(final long left, final long right, final ExpressionEvaluationContext context) {
        return ExpressionNode.longNode(this.applyLong0(left, right, context));
    }

    abstract long applyLong0(final long left, final long right, final ExpressionEvaluationContext context);
}
