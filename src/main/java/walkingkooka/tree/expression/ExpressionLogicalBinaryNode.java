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

import java.math.BigInteger;

/**
 * Base class for the AND, OR, XOR {@link ExpressionBinaryNode}.
 */
abstract class ExpressionLogicalBinaryNode extends ExpressionBinaryNode {

    ExpressionLogicalBinaryNode(final int index, final ExpressionNode left, final ExpressionNode right) {
        super(index, left, right);
    }

    // is .........................................................................................................

    @Override
    public final boolean isAddition() {
        return false;
    }

    @Override
    public final boolean isDivision() {
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
    public final boolean isModulo() {
        return false;
    }

    @Override
    public final boolean isMultiplication() {
        return false;
    }

    @Override
    public final boolean isNotEquals() {
        return false;
    }

    @Override
    public final boolean isPower() {
        return false;
    }

    @Override
    public final boolean isSubtraction() {
        return false;
    }

    // evaluation .....................................................................................................

    @Override
    public final Object toValue(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toValue(context);
    }

    @Override final ExpressionNode apply(final ExpressionNode left,
                                         final ExpressionNode right,
                                         final ExpressionEvaluationContext context) {
        ExpressionNode result;

        try {
            for (; ; ) {
                final Object leftValue = left.toValue(context);
                final Object rightValue = right.toValue(context);

                if (leftValue instanceof Boolean) {
                    result = this.applyBoolean(
                            context.convert(leftValue, Boolean.class),
                            context.convert(rightValue, Boolean.class),
                            context);
                    break;
                }

                // both Long
                final boolean leftLong = isByteShortIntegerLong(leftValue);
                final boolean rightLong = isByteShortIntegerLong(rightValue);
                if (leftLong && rightLong) {
                    result = this.applyLong(
                            context.convert(leftValue, Long.class),
                            context.convert(rightValue, Long.class),
                            context);
                    break;
                }
                // default is to promote both to BigInteger, doubles and BigDecimal may fail if they have decimals.
                result = this.applyBigInteger(
                        context.convert(leftValue, BigInteger.class),
                        context.convert(rightValue, BigInteger.class),
                        context);
                break;
            }
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException(cause.getMessage() + "\n" + this.toString(), cause);
        }

        return result;
    }

    final ExpressionNode applyBoolean(final boolean left, final boolean right, final ExpressionEvaluationContext context) {
        return ExpressionNode.booleanNode(this.applyBoolean0(left, right));
    }

    abstract boolean applyBoolean0(final boolean left, final boolean right);
}
