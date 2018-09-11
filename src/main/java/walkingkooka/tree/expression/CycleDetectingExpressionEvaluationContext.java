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

import walkingkooka.collect.set.Sets;
import walkingkooka.util.variable.Variable;
import walkingkooka.util.variable.Variables;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Wraps another {@link ExpressionEvaluationContext} delegating all methods except for a guard within
 * {@link #reference(ExpressionReference)} to detect cycles between resolving a {@link ExpressionReference} to a
 * {@link ExpressionNode}, even indirectly.<br>
 * To make this class safe in a multi-threaded environment a {@link Variables#threadLocal()} should be used.
 */
final class CycleDetectingExpressionEvaluationContext implements ExpressionEvaluationContext {

    /**
     * Factory that creates a new {@link CycleDetectingExpressionEvaluationContext}.
     */
    static CycleDetectingExpressionEvaluationContext with(final ExpressionEvaluationContext context,
                                                          final Variable<Set<ExpressionReference>> cycles) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(cycles, "cycles");

        return new CycleDetectingExpressionEvaluationContext(context, cycles);
    }

    /**
     * Private ctor use factory.
     */
    private CycleDetectingExpressionEvaluationContext(final ExpressionEvaluationContext context,
                                                      final Variable<Set<ExpressionReference>> cycles) {
        this.context = context;
        this.cycles = cycles;
    }

    @Override
    public Object function(final ExpressionNodeName expressionNodeName, final List<Object> parameters) {
        return this.context.function(expressionNodeName, parameters);
    }

    @Override
    public ExpressionNode reference(final ExpressionReference reference) {
        final Set<ExpressionReference> cycles = this.cycles();

        this.cycleCheck(reference, cycles);

        try {
           cycles.add(reference);
           final ExpressionNode result =  this.context.reference(reference);

            if(result.isReference()) {
                final ExpressionReferenceNode referenceNode = result.cast();
                this.cycleCheck(referenceNode.value(), cycles);
            }

           return result;
        } finally {
            cycles.remove(reference);
        }
    }

    /**
     * If the reference is in {@link #cycles} then there must be a cycle of some sort.
     */
    private void cycleCheck(final ExpressionReference reference, final Set<ExpressionReference> cycles) {
        if(cycles.contains(reference)) {
            this.reportCycle(reference);
        }
    }

    private Set<ExpressionReference> cycles() {
        Set<ExpressionReference> cycles = this.cycles.get();
        if(null==cycles) {
            cycles = Sets.ordered();
            this.cycles.set(cycles);
        }
        return cycles;
    }

    /**
     * Used to keep track of references and to detect cycles.
     */
    private final Variable<Set<ExpressionReference>> cycles;

    /**
     * Reports a cycle for a given {@link ExpressionReference}
     */
    private void reportCycle(final ExpressionReference reference) {
        throw new CycleDetectedExpressionEvaluationConversionException("Cycle detected to " + reference, reference);
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public <T> T convert(final Object from, final Class<T> toType) {
        return this.context.convert(from, toType);
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
