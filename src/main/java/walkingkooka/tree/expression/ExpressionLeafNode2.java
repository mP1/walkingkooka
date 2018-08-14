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
 * Base class for all leafs except {@link ExpressionReferenceNode}.
 * All toXXX methods take their value and convert it to the actual target value.
 */
abstract class ExpressionLeafNode2<V> extends ExpressionLeafNode<V> {

    ExpressionLeafNode2(final int index, final V value){
        super(index, value);
    }

    @Override
    public final BigDecimal toBigDecimal(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), BigDecimal.class);
    }

    @Override
    public final BigInteger toBigInteger(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), BigInteger.class);
    }

    @Override
    public final boolean toBoolean(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), Boolean.class);
    }

    @Override
    public final double toDouble(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), Double.class);
    }

    @Override
    public final long toLong(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), Long.class);
    }

    @Override
    public final Number toNumber(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), Number.class);
    }

    @Override
    public final String toText(final ExpressionEvaluationContext context) {
        return context.convert(this.value(), String.class);
    }
}
