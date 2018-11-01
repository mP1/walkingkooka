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
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link NodeSelector} that matches {@link Node nodes} whose attributes match the provided {@link Predicate}.
 */
final class ExpressionNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link ExpressionNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    ExpressionNodeSelector<N, NAME, ANAME, AVALUE> with(final Predicate<ExpressionEvaluationContext> predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return new ExpressionNodeSelector(predicate, NodeSelector.terminal());
    }

    /**
     * Private constructor
     */
    private ExpressionNodeSelector(final Predicate<ExpressionEvaluationContext> predicate, final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.predicate = predicate;
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new ExpressionNodeSelector<>(this.predicate, selector);
    }

    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        if (this.predicate.test(ExpressionNodeSelectorPredicateExpressionEvaluationContext.with(node))) {
            context.selected(node);
        }
    }

    private final Predicate<ExpressionEvaluationContext> predicate;

    // Object


    @Override
    int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next, this.predicate);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNodeSelector;
    }

    @Override
    boolean equals1(final NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final ExpressionNodeSelector<?, ?, ?, ?> other) {
        return this.predicate.equals(other.predicate);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.predicate(this.predicate.toString());
    }
}
