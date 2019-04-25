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
import walkingkooka.naming.Name;
import walkingkooka.text.cursor.parser.select.NodeSelectorAttributeName;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.ExpressionReference;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionEvaluationContext} that is used when evaluating predicates.
 */
final class ExpressionNodeSelectorExpressionEvaluationContext<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
        implements ExpressionEvaluationContext {

    /**
     * Factory that creates a new {@link ExpressionNodeSelectorExpressionEvaluationContext}, using the given {@link Node} as the context.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ExpressionNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> with(final N node,
                                                                                   final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return new ExpressionNodeSelectorExpressionEvaluationContext<>(node, context);
    }

    /**
     * Private ctor use factory.
     */
    private ExpressionNodeSelectorExpressionEvaluationContext(final N node,
                                                              final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super();
        this.node = node;
        this.context = context;
    }

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        final List<Object> thisAndParameters = Lists.array();
        thisAndParameters.add(this.node);
        thisAndParameters.addAll(parameters);
        return this.context.function(name, Lists.readOnly(parameters));
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

    /**
     * The node which will become parameter 0 in all function parameters.
     */
    // @VisibleForTesting
    final Node<?, ?, ?, ?> node;

    private final static Optional<ExpressionNode> ABSENT = Optional.of(ExpressionNode.text(""));

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        return this.context.convert(value, target);
    }

    private final NodeSelectorContext<N, NAME, ANAME, AVALUE> context;

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
