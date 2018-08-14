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
import walkingkooka.ShouldNeverHappenError;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Base class for the AND, OR, XOR {@link ExpressionBinaryNode}.
 */
abstract class ExpressionLogicalBinaryNode extends ExpressionBinaryNode {

    ExpressionLogicalBinaryNode(final int index, final ExpressionNode left, final ExpressionNode right){
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
    public final Number toValue(final ExpressionEvaluationContext context) {
        return this.toNumber(context);
    }

    /**
     * Logical operations only work on numbers without decimals.
     */
    final Class<Number> commonNumberType(final Class<? extends Number> type) {
        return Cast.to(BigDecimal.class == type || Double.class == type?
                BigInteger.class :
                type);
    }

    @Override
    final ExpressionNode applyBigDecimal(final BigDecimal left, final BigDecimal right, final ExpressionEvaluationContext context) {
        throw new ShouldNeverHappenError(this.getClass().getName() + ".applyBigDecimal");
    }

    @Override
    final ExpressionNode applyDouble(final double left, final double right, final ExpressionEvaluationContext context) {
        throw new ShouldNeverHappenError(this.getClass().getName() + ".applyDouble");
    }
}
