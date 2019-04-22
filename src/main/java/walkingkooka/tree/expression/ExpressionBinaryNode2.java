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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Base class for any expression that accepts two parameters and accepts all types of numbers including {@link Double} and {@link BigDecimal}.
 */
abstract class ExpressionBinaryNode2 extends ExpressionBinaryNode {

    ExpressionBinaryNode2(final int index, final ExpressionNode left, final ExpressionNode right) {
        super(index, left, right);
    }

    @Override
    public final boolean isAnd() {
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

    @Override final ExpressionNode apply(final ExpressionNode left,
                                         final ExpressionNode right,
                                         final ExpressionEvaluationContext context) {
        ExpressionNode result;

        try {
            for (; ; ) {
                final Object leftValue = left.toValue(context);
                final Object rightValue = right.toValue(context);

                if (leftValue instanceof String) {

                    result = this.applyText(
                            context.convert(leftValue, String.class),
                            context.convert(rightValue, String.class),
                            context);
                    break;
                }

                // both Long
                final boolean leftByteShortIntegerLong = isByteShortIntegerLong(leftValue);
                final boolean rightByteShortIntegerLong = isByteShortIntegerLong(rightValue);
                if (leftByteShortIntegerLong && rightByteShortIntegerLong) {
                    result = this.applyLong(
                            context.convert(leftValue, Long.class),
                            context.convert(rightValue, Long.class),
                            context);
                    break;
                }
                // BigInteger and Long or both BigInteger
                final boolean leftBigInteger = leftValue instanceof BigInteger;
                final boolean rightBigInteger = rightValue instanceof BigInteger;
                if (leftBigInteger && rightBigInteger ||
                        leftBigInteger && rightByteShortIntegerLong ||
                        leftByteShortIntegerLong && rightBigInteger) {
                    result = this.applyBigInteger(
                            context.convert(leftValue, BigInteger.class),
                            context.convert(rightValue, BigInteger.class),
                            context);
                    break;
                }
                // both must be double,
                if (isFloatDouble(leftValue) && isFloatDouble(rightValue)) {
                    result = this.applyDouble(
                            context.convert(leftValue, Double.class),
                            context.convert(rightValue, Double.class),
                            context);
                    break;
                }
                // default is to promote both to BigDecimal.
                result = this.applyBigDecimal(
                        context.convert(leftValue, BigDecimal.class),
                        context.convert(rightValue, BigDecimal.class),
                        context);
                break;
            }
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException(cause.getMessage() + "\n" + this.toString(), cause);
        }

        return result;
    }

    abstract ExpressionNode applyText(final String left, final String right, final ExpressionEvaluationContext context);

    abstract ExpressionNode applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context);

    abstract ExpressionNode applyDouble(final double left, final double right, final ExpressionEvaluationContext context);
}
