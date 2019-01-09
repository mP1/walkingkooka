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
 */

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.convert.ConversionException;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;

import java.util.List;
import java.util.Objects;

/**
 * A {@link NodeSelector} that selects {@link Node nodes} depending on the result of executing the {@link ExpressionNode}.
 * A boolean result may or may not select the current node, a number is used as an index to select the child.
 */
final class ExpressionNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link ExpressionNodeSelector} factory
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ExpressionNodeSelector<N, NAME, ANAME, AVALUE> with(final ExpressionNode expressionNode) {
        Objects.requireNonNull(expressionNode, "expressionNode");

        return new ExpressionNodeSelector<N, NAME, ANAME, AVALUE>(expressionNode, NodeSelector.terminal());
    }

    /**
     * Private constructor
     */
    private ExpressionNodeSelector(final ExpressionNode expressionNode,
                                   final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.expressionNode = expressionNode;
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new ExpressionNodeSelector<N, NAME, ANAME, AVALUE>(this.expressionNode, selector);
    }

    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        try {
            final Object value = this.expressionNode.toValue(ExpressionNodeSelectorExpressionEvaluationContext.with(node, context));
            if(value instanceof Boolean) {
                this.booleanResult(node, Boolean.TRUE.equals(value), context);
            } else {
                this.attemptIndex(node, value, context);
            }
        } catch (final ConversionException | NodeSelectorException fail) {
        }
    }

    /**
     * The expression holds the xpath predicate. If a boolean is returned the current node is selected, for numbers,
     * the child with that position is selected. Other value types always select the current node.
     */
    private final ExpressionNode expressionNode;

    /**
     * Select the node only if the boolean value is true.
     */
    private void booleanResult(final N node,
                               final boolean value,
                               final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        if(value) {
            context.selected(node);
        }
    }

    /**
     * Converts the value to an index and attempts to visit the child at that position.
     */
    private void attemptIndex(final N node,
                              final Object value,
                              final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final int index = context.convert(value, Integer.class) - INDEX_BIAS;
        final List<N> children = node.children();
        if (index >= 0 && index < children.size()) {
            final N child = children.get(index);
            context.potential(child);
            this.select(child, context);
        }
    }

    // Object

    @Override
    int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next, this.expressionNode);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNodeSelector;
    }

    @Override
    boolean equals1(final NonLogicalNodeSelector<?, ?, ?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final ExpressionNodeSelector<?, ?, ?, ?> other) {
        return this.expressionNode.equals(other.expressionNode);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.predicate(this.expressionNode.toString());
    }
}
