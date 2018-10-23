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

import walkingkooka.math.DecimalNumberContext;
import walkingkooka.convert.Converter;
import walkingkooka.type.PublicStaticHelper;
import walkingkooka.util.variable.Variable;

import java.math.MathContext;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ExpressionEvaluationContexts implements PublicStaticHelper {

    /**
     * {@see BasicExpressionEvaluationContext}
     */
    public static ExpressionEvaluationContext basic(final BiFunction<ExpressionNodeName, List<Object>, Object> functions,
                                                    final Function<ExpressionReference, ExpressionNode> references,
                                                    final MathContext mathContext,
                                                    final Converter converter,
                                                    final DecimalNumberContext context) {
        return BasicExpressionEvaluationContext.with(functions, references, mathContext, converter, context);
    }

    /**
     * {@see CycleDetectingExpressionEvaluationContext}
     */
    public static ExpressionEvaluationContext cycleDetecting(final ExpressionEvaluationContext context,
                                                             final Variable<Set<ExpressionReference>> cycles) {
        return CycleDetectingExpressionEvaluationContext.with(context, cycles);
    }

    /**
     * {@see FakeExpressionEvaluationContext}
     */
    public static ExpressionEvaluationContext fake() {
        return new FakeExpressionEvaluationContext();
    }

    /**
     * Stop creation
     */
    private ExpressionEvaluationContexts() {
        throw new UnsupportedOperationException();
    }
}
