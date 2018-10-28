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

import walkingkooka.compare.ComparisonRelation;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.List;
import java.util.Objects;

/**
 * A function that compares two parameters of the same value. Before comparing the second value is coverted to the same
 * type as the first.
 */
final class NodeSelectorPredicateComparisonFunction extends NodeSelectorPredicateFunction<Boolean> {

    /**
     * Factory
     */
    static final NodeSelectorPredicateComparisonFunction with(final ComparisonRelation relation) {
        Objects.requireNonNull(relation, "relation");

        return new NodeSelectorPredicateComparisonFunction(relation);
    }

    /**
     * Private ctor
     */
    private NodeSelectorPredicateComparisonFunction(final ComparisonRelation relation) {
        super();
        this.relation = relation;
    }

    @Override
    public Boolean apply(final List<Object> parameters, final ExpressionEvaluationContext context) {
        this.checkParameterCount(parameters, 2);

        final Comparable first = this.parameter(parameters, 0, Comparable.class, context);
        final Comparable second = this.parameter(parameters, 1, first.getClass(), context);

        return this.relation.predicate(second).test(first);
    }

    private final ComparisonRelation relation;

    @Override
    public String toString() {
        return this.relation.toString();
    }
}
