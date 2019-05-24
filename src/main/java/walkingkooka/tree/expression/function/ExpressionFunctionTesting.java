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

package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.util.BiFunctionTesting;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing interface that provides methods to test a {@link ExpressionFunction}
 */
public interface ExpressionFunctionTesting<F extends ExpressionFunction<V>, V>
        extends BiFunctionTesting<F, List<Object>, ExpressionFunctionContext, V>,
        TypeNameTesting<F> {

    default void apply2(final Object... parameters) {
        this.createBiFunction().apply(parameters(parameters), this.createContext());
    }

    default void applyAndCheck2(final List<Object> parameters,
                                final V result) {
        this.applyAndCheck2(this.createBiFunction(), parameters, result);
    }

    default <TT, RR> void applyAndCheck2(final ExpressionFunction<RR> function,
                                         final List<Object> parameters,
                                         final RR result) {
        this.applyAndCheck2(function, parameters, this.createContext(), result);
    }

    default <TT, RR> void applyAndCheck2(final ExpressionFunction<RR> function,
                                         final List<Object> parameters,
                                         final ExpressionFunctionContext context,
                                         final RR result) {
        assertEquals(result,
                function.apply(parameters, context),
                () -> "Wrong result for " + function + " for params: " + CharSequences.quoteIfChars(parameters));
    }

    default ExpressionFunctionContext createContext() {
        return new FakeExpressionFunctionContext() {
            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                return ExpressionFunctionTesting.this.convert(value, target);
            }
        };
    }

    default <T> T convert(final Object value, final Class<T> target) {
        if (target.isInstance(value)) {
            return target.cast(value);
        }
        if (target == String.class) {
            return Cast.to(value.toString());
        }
        if (target == Boolean.class) {
            return Cast.to(Boolean.parseBoolean(value.toString()));
        }
        if (BigDecimal.class == target) {
            return target.cast(Converters.numberBigDecimal().convert(value,
                    BigDecimal.class,
                    ConverterContexts.fake()));
        }
        if (Number.class.isAssignableFrom(target)) {
            return Cast.to(Integer.parseInt(value.toString()));
        }
        throw new UnsupportedOperationException("Unable to convert " + value.getClass().getName() + "=" + CharSequences.quoteIfChars(value) + " to " + target.getName());
    }

    default List<Object> parameters(final Object... values) {
        return Lists.of(values);
    }

    default List<Object> parametersWithThis(final Object... values) {
        return Lists.of(values);
    }

    // TypeNameTesting...........................................................................................

    @Override
    default String typeNamePrefix() {
        return this.subtractTypeNameSuffix();
    }

    @Override
    default String typeNameSuffix() {
        return Function.class.getSimpleName();
    }
}
