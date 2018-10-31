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
import walkingkooka.convert.ConversionException;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.naming.Name;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.select.NodeSelectorAttributeName;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.ExpressionReference;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionEvaluationContext} that is used when evaluating predicates.
 */
final class NodeSelectorPredicateExpressionEvaluationContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements ExpressionEvaluationContext {

    /**
     * Factory that creates a new {@link NodeSelectorPredicateExpressionEvaluationContext}, using the given {@link Node} as the context.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelectorPredicateExpressionEvaluationContext<N, NAME, ANAME, AVALUE> with(final N node) {
        return new NodeSelectorPredicateExpressionEvaluationContext(node);
    }

    /**
     * Private ctor use factory.
     */
    private NodeSelectorPredicateExpressionEvaluationContext(final N node) {
        super();
        this.node = node;
    }

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        final NodeSelectorPredicateFunction function = NodeSelectorPredicateFunction.NAME_TO_FUNCTION.get(name);

        return function.apply(parameters, this);
    }

    /**
     * The reference should be an attribute name, cast and find the owner attribute.
     */
    @Override
    public Optional<ExpressionNode> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        if (false == reference instanceof NodeSelectorAttributeName) {
            throw new IllegalArgumentException("Expected attribute name but got " + reference);
        }
        final NodeSelectorAttributeName attributeName = Cast.to(reference);
        final String attributeNameString = attributeName.value();

        final Optional<ExpressionNode> attributeValue = this.node.attributes()
                .entrySet()
                .stream()
                .filter(nameAndValue -> nameAndValue.getKey().value().equals(attributeNameString))
                .map(nameAndValue -> ExpressionNode.valueOrFail(nameAndValue.getValue()))
                .findFirst();
        return attributeValue.isPresent() ?
                attributeValue :
                ABSENT;
    }

    private final static Optional<ExpressionNode> ABSENT = Optional.of(ExpressionNode.text(""));

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(target, "target");

        return Cast.to(target.isInstance(value) ?
                target.cast(value) :
                target == BigDecimal.class ?
                        this.convertToBigDecimal(value) :
                        target == Boolean.class ?
                                this.convertToBoolean(value) :
                                target == Integer.class ?
                                        this.convertToInteger(value) :
                                        target == String.class ?
                                                this.convertToString(value) :
                                                this.failConversion(value, target));
    }

    private BigDecimal convertToBigDecimal(final Object value) {
        final Converter converter = value instanceof String ?
                Converters.parser(BigDecimal.class, Parsers.bigDecimal(MathContext.DECIMAL32), (c) -> ParserContexts.basic(c)) :
                Converters.numberBigDecimal();
        return converter.convert(value, BigDecimal.class, this.converterContext);
    }

    private Boolean convertToBoolean(final Object value) {
        return Converters.truthyNumberBoolean().convert(value, Boolean.class, this.converterContext);
    }

    private Integer convertToInteger(final Object value) {
        return Converters.string().convert(value, Integer.class, this.converterContext);
    }

    private String convertToString(final Object value) {
        return Converters.string().convert(value, String.class, this.converterContext);
    }

    private <T> T failConversion(final Object value, final Class<T> target) {
        throw new ConversionException("Failed to convert " + CharSequences.quoteIfChars(value) + " to " + target.getSimpleName());
    }

    private final ConverterContext converterContext = ConverterContexts.basic(this);

    private final N node;

    @Override
    public String currencySymbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char decimalPoint() {
        return '.';
    }

    @Override
    public char exponentSymbol() {
        return 'E';
    }

    @Override
    public char groupingSeparator() {
        return ',';
    }

    @Override
    public char percentageSymbol() {
        return '%';
    }

    @Override
    public char minusSign() {
        return '-';
    }

    @Override
    public char plusSign() {
        return '+';
    }

    @Override
    public String toString() {
        return this.node.toString();
    }
}
