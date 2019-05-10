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

import walkingkooka.convert.ConversionException;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.List;

/**
 * A {@link NodeSelectorContext2} that tracks the position of selected {@link Node}. This allows {@link ExpressionNodeSelector} to
 * test numeric values against the current position of the current {@link Node}.
 */
final class ExpressionNodeSelectorNodeSelectorContext2<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NodeSelectorContext2<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> ExpressionNodeSelectorNodeSelectorContext2<N, NAME, ANAME, AVALUE> with(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return new ExpressionNodeSelectorNodeSelectorContext2<>(context);
    }

    private ExpressionNodeSelectorNodeSelectorContext2(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super(context);
    }

    @Override
    public boolean test(final N node) {
        return this.context.test(node);
    }

    /**
     * Updates the position and selects the given {@link Node}.
     */
    @Override
    public N selected(final N node) {
        return this.context.selected(node);
    }

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        return this.context.function(name, parameters);
    }

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        return this.context.convert(value, target);
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> all() {
        return NodeSelectorContext2.all(this.context);
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> expressionCreateIfNecessary() {
        return this;
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> expression() {
        return NodeSelectorContext2.expression(this.context);
    }

    /**
     * Tests if a {@link Number} result matches the current position of the current {@link Node}.
     */
    @Override
    boolean nodePositionTest(final Object value) {
        boolean result = false;

        try {
            result = value instanceof Boolean ?
                    Boolean.TRUE.equals(value) :
                    this.position == this.convert(value, Integer.class)
                            .intValue();
        } catch (final ConversionException ignore) {
        }
        this.position++;

        return result;
    }

    @Override
    int nodePosition() {
        return this.position;
    }

    // VisibleForTesting
    int position = NodeSelector.INDEX_BIAS;

    @Override
    public String toString() {
        return "position: " + this.position + " " + this.context.toString();
    }
}
