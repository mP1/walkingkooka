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

import walkingkooka.convert.ConversionException;
import walkingkooka.convert.Converter;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An {@link ExpressionEvaluationContext} delegates to helpers or constants for each method.
 */
final class BasicExpressionEvaluationContext implements ExpressionEvaluationContext {

    /**
     * Factory that creates a {@link BasicExpressionEvaluationContext}
     */
    static BasicExpressionEvaluationContext with(final BiFunction<ExpressionNodeName, List<Object>, Object> functions,
                                                 final Function<ExpressionReference, ExpressionNode> references,
                                                 final MathContext mathContext,
                                                 final Converter converter) {
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(mathContext, "mathContext");
        Objects.requireNonNull(converter, "converter");

        return new BasicExpressionEvaluationContext(functions, references, mathContext, converter);
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final BiFunction<ExpressionNodeName, List<Object>, Object> functions,
                                             final Function<ExpressionReference, ExpressionNode> references,
                                             final MathContext mathContext,
                                             final Converter converter) {
        super();
        this.functions = functions;
        this.references = references;
        this.mathContext = mathContext;
        this.converter = converter;
    }

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        return this.functions.apply(name, parameters);
    }

    private final BiFunction<ExpressionNodeName, List<Object>, Object> functions;

    @Override
    public ExpressionNode reference(final ExpressionReference reference) {
        return this.references.apply(reference);
    }

    private final Function<ExpressionReference, ExpressionNode> references;

    @Override
    public MathContext mathContext() {
        return this.mathContext;
    }

    private final MathContext mathContext;

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        try {
            return this.converter.convert(value, target);
        } catch (final ConversionException cause) {
            throw new ExpressionEvaluationConversionException(cause.getMessage(), cause);
        }
    }

    private final Converter converter;

    @Override
    public String toString() {
        return this.functions + " " + this.references + " " + this.mathContext + " " + this.converter;
    }
}
