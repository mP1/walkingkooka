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

    ExpressionBinaryNode2(final int index, final ExpressionNode left, final ExpressionNode right){
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

    @Override
    final ExpressionNode apply(final ExpressionNode left,
                               final ExpressionNode right,
                               final ExpressionEvaluationContext context) {
        final Number leftNumber = left.toNumber(context);
        final Number rightNumber = right.toNumber(context);

        ExpressionNode result;

        try {
            for (; ; ) {
                // both Long
                final boolean leftLong = leftNumber instanceof Long;
                final boolean rightLong = rightNumber instanceof Long;
                if (leftLong && rightLong) {
                    result = this.applyLong(
                            context.convert(leftNumber, Long.class),
                            context.convert(rightNumber, Long.class),
                            context);
                    break;
                }
                // BigInteger and Long or both BigInteger
                final boolean leftBigInteger = leftNumber instanceof BigInteger;
                final boolean rightBigInteger = rightNumber instanceof BigInteger;
                if (leftBigInteger && rightBigInteger ||
                    leftBigInteger && rightLong ||
                    leftLong && rightBigInteger) {
                    result = this.applyBigInteger(
                            context.convert(leftNumber, BigInteger.class),
                            context.convert(rightNumber, BigInteger.class),
                            context);
                    break;
                }
                // both must be double,
                if (leftNumber instanceof Double && rightNumber instanceof Double) {
                    result = this.applyDouble(
                            context.convert(leftNumber, Double.class),
                            context.convert(rightNumber, Double.class),
                            context);
                    break;
                }
                // default is to promote both to BigDecimal.
                result = this.applyBigDecimal(
                            context.convert(leftNumber, BigDecimal.class),
                            context.convert(rightNumber, BigDecimal.class),
                            context);
                break;
            }
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException(cause.getMessage() + "\n" + this.toString(), cause);
        }

        return result;
    }

    abstract ExpressionNode applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context);

    abstract ExpressionNode applyDouble(final double left, final double right, final ExpressionEvaluationContext context);
}
