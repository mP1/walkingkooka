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
import walkingkooka.tree.expression.ExpressionNode;

import java.util.function.Predicate;

/**
 * A {@link Predicate} that evaluates a {@link ExpressionNode} along with a {@link walkingkooka.tree.expression.ExpressionEvaluationContext}
 */
final class NodeSelectorPredicate<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements Predicate<N> {

    /**
     * Factory that creates a {@link NodeSelectorPredicate}.
     */
    static NodeSelectorPredicate with(final ExpressionNode expressionNode) {
        return new NodeSelectorPredicate(expressionNode);
    }

    /**
     * Private ctor use factory
     */
    private NodeSelectorPredicate(final ExpressionNode expressionNode) {
        super();
        this.expressionNode = expressionNode;
    }

    @Override
    public boolean test(final N n) {
        boolean test;
        try {
            test = this.expressionNode.toBoolean(NodeSelectorPredicateExpressionEvaluationContext.with(n));
        } catch (final ConversionException | NodeSelectorException fail) {
            test = false;
        }
        return test;
    }

    private final ExpressionNode expressionNode;

    @Override
    public String toString() {
        return this.expressionNode.toString();
    }
}
