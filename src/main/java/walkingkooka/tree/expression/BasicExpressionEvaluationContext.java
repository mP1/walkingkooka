/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.tree.expression;

import walkingkooka.convert.ConversionException;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.math.DecimalNumberContext;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
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
                                                 final Function<ExpressionReference, Optional<ExpressionNode>> references,
                                                 final Converter converter,
                                                 final DecimalNumberContext context) {
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(references, "references");
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(context, "context");

        return new BasicExpressionEvaluationContext(functions, references, converter, context);
    }

    /**
     * Private ctor use factory
     */
    private BasicExpressionEvaluationContext(final BiFunction<ExpressionNodeName, List<Object>, Object> functions,
                                             final Function<ExpressionReference, Optional<ExpressionNode>> references,
                                             final Converter converter,
                                             final DecimalNumberContext context) {
        super();
        this.functions = functions;
        this.references = references;
        this.converter = converter;
        this.decimalNumberContext = context;

        this.converterContext = ConverterContexts.basic(context);
    }

    @Override
    public String currencySymbol() {
        return this.decimalNumberContext.currencySymbol();
    }

    @Override
    public char decimalPoint() {
        return this.decimalNumberContext.decimalPoint();
    }

    @Override
    public char exponentSymbol() {
        return this.decimalNumberContext.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.decimalNumberContext.groupingSeparator();
    }

    @Override
    public char minusSign() {
        return this.decimalNumberContext.minusSign();
    }

    @Override
    public char percentageSymbol() {
        return this.decimalNumberContext.percentageSymbol();
    }

    @Override
    public char plusSign() {
        return this.decimalNumberContext.plusSign();
    }

    @Override
    public Locale locale() {
        return this.decimalNumberContext.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.decimalNumberContext.mathContext();
    }

    private final DecimalNumberContext decimalNumberContext;

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        return this.functions.apply(name, parameters);
    }

    private final BiFunction<ExpressionNodeName, List<Object>, Object> functions;

    @Override
    public Optional<ExpressionNode> reference(final ExpressionReference reference) {
        final Optional<ExpressionNode> node = this.references.apply(reference);
        if (!node.isPresent()) {
            new ExpressionEvaluationReferenceException("Missing reference: " + reference);
        }
        return node;
    }

    private final Function<ExpressionReference, Optional<ExpressionNode>> references;

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        try {
            return this.converter.convert(value, target, this.converterContext);
        } catch (final ConversionException cause) {
            throw new ExpressionEvaluationConversionException(cause.getMessage(), cause);
        }
    }

    private final Converter converter;
    private final ConverterContext converterContext;

    @Override
    public String toString() {
        return this.decimalNumberContext + " " + this.functions + " " + this.references + " " + this.converter;
    }
}
