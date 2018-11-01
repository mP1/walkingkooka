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

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.util.BiFunctionTestCase;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

public abstract class NodeSelectorPredicateFunctionTestCase<F extends NodeSelectorPredicateFunction<V>, V> extends BiFunctionTestCase<F, List<Object>, NodeSelectorPredicateExpressionEvaluationContext, V> {

    final void apply2(final Object... parameters) {
        this.createBiFunction().apply(list(parameters), this.createContext());
    }

    final void applyAndCheck2(final List<Object> parameters,
                              final V result) {
        this.applyAndCheck2(this.createBiFunction(), parameters, result);
    }

    final <TT, RR> void applyAndCheck2(final BiFunction<List<Object>, NodeSelectorPredicateExpressionEvaluationContext, RR> function,
                                       final List<Object> parameters,
                                       final RR result) {
        this.applyAndCheck2(function, parameters, this.createContext(), result);
    }

    final <TT, RR> void applyAndCheck2(final BiFunction<List<Object>, NodeSelectorPredicateExpressionEvaluationContext, RR> function,
                                       final List<Object> parameters,
                                       final NodeSelectorPredicateExpressionEvaluationContext context,
                                       final RR result) {
        assertEquals("Wrong result for " + function + " for params: " + CharSequences.quoteIfChars(parameters),
                result,
                function.apply(parameters, context));
    }

    NodeSelectorPredicateExpressionEvaluationContext createContext() {
        return new FakeNodeSelectorPredicateExpressionEvaluationContext() {
            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                return NodeSelectorPredicateFunctionTestCase.this.convert(value, target);
            }
        };
    }

    final <T> T convert(final Object value, final Class<T> target) {
        if (target.isInstance(value)) {
            return target.cast(value);
        }
        if (target == String.class) {
            return Cast.to(value.toString());
        }
        if (target == Boolean.class) {
            return Cast.to(Boolean.parseBoolean(value.toString()));
        }
        if (Number.class.isAssignableFrom(target)) {
            return Cast.to(Integer.parseInt(value.toString()));
        }
        throw new UnsupportedOperationException("Unable to convert " + value.getClass().getName() + "=" + CharSequences.quoteIfChars(value) + " to " + target.getName());
    }

    final List<Object> list(final Object... values) {
        return Lists.of(values);
    }
}
