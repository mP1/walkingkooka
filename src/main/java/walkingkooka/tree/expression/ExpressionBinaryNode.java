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

import walkingkooka.ShouldNeverHappenError;
import walkingkooka.collect.list.Lists;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for any expression that accepts two parameters.
 */
abstract class ExpressionBinaryNode extends ExpressionParentFixedNode {

    static void check(final ExpressionNode left, final ExpressionNode right){
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
    }

    ExpressionBinaryNode(final int index, final ExpressionNode left, final ExpressionNode right){
        super(index, Lists.of(left, right));
    }

    public ExpressionNode left() {
        return this.children().get(0);
    }

    public ExpressionNode right() {
        return this.children().get(1);
    }

    @Override
    int expectedChildCount(){
        return 2;
    }

    @Override
    final ExpressionBinaryNode wrap0(final int index, final List<ExpressionNode> children) {
        return wrap1(index, children.get(0), children.get(1));
    }

    abstract ExpressionBinaryNode wrap1(final int index, final ExpressionNode left, final ExpressionNode right);

    // is...........................................................................................................

    @Override
    public final boolean isFunction() {
        return false;
    }

    @Override
    public final boolean isNegative() {
        return false;
    }

    @Override
    public final boolean isNot() {
        return false;
    }


    // Node........................................................................................................

    @Override
    public ExpressionNode appendChild(final ExpressionNode child) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionNode removeChild(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Optional<ExpressionNode> firstChild() {
        return Optional.of(this.left());
    }

    @Override
    public final Optional<ExpressionNode> lastChild() {
        return Optional.of(this.right());
    }

    // Visitor........................................................................................................

    final void acceptValues(final ExpressionNodeVisitor visitor){
        visitor.accept(this.left());
        visitor.accept(this.right());
    }

    // Evaluation ...................................................................................................

    @Override 
    public final BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toBigDecimal(context);
    }

    @Override
    public final BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toBigInteger(context);
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toBoolean(context);
    }

    @Override
    public final double toDouble(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toDouble(context);
    }

    @Override
    public final long toLong(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toLong(context);
    }

    @Override
    public final Number toNumber(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toNumber(context);
    }

    @Override
    public final String toText(final ExpressionEvaluationContext context) {
        return this.apply(this.left(), this.right(), context)
                .toText(context);
    }

    final ExpressionNode apply(final ExpressionNode left,
                               final ExpressionNode right,
                               final ExpressionEvaluationContext context) {
        final Number rightNumber = right.toNumber(context);
        final Class<?> best = this.commonNumberType(left.commonNumberType(rightNumber.getClass()));

        ExpressionNode result;

        try {
            for (; ; ) {
                if (best == BigDecimal.class) {
                    result = this.applyBigDecimal(
                            left.toBigDecimal(context),
                            context.convert(rightNumber, BigDecimal.class),
                            context);
                    break;
                }
                if (best == BigInteger.class) {
                    result = this.applyBigInteger(
                            left.toBigInteger(context),
                            context.convert(rightNumber, BigInteger.class),
                            context);
                    break;
                }
                if (best == Double.class) {
                    result = this.applyDouble(
                            left.toDouble(context),
                            context.convert(rightNumber, Double.class),
                            context);
                    break;
                }
                if (best == Long.class) {
                    result = this.applyLong(
                            left.toLong(context),
                            context.convert(rightNumber, Long.class),
                            context);
                    break;
                }
                throw new ShouldNeverHappenError("Unexpected type=" + best.getName());
            }
        } catch (final ArithmeticException cause) {
            throw new ExpressionEvaluationException(cause.getMessage() + "\n" + this.toString(), cause);
        }

        return result;
    }

    abstract ExpressionNode applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context);

    abstract ExpressionNode applyBigInteger(final BigInteger left, final BigInteger right, final ExpressionEvaluationContext context);

    abstract ExpressionNode applyDouble(final double left, final double right, final ExpressionEvaluationContext context);

    abstract ExpressionNode applyLong(final long left, final long right, final ExpressionEvaluationContext context);

    // Object........................................................................................................

    final void toString0(final StringBuilder b) {
        this.left().toString0(b);
        this.appendSymbol(b);
        this.right().toString0(b);
    }

    abstract void appendSymbol(final StringBuilder b);
}
