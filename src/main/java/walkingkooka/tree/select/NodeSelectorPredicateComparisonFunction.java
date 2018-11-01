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
import walkingkooka.compare.ComparisonRelation;

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
    public Boolean apply(final List<Object> parameters,
                         final NodeSelectorPredicateExpressionEvaluationContext<?, ?, ?, ?> context) {
        this.checkParameterCount(parameters, 2);

        final Comparable first = context.comparable(parameters, 0);
        final Comparable second = Cast.to(context.parameter(parameters, 1, first.getClass())); // compiler fails to infer T=Comparable

        return this.relation.predicate(second).test(first);
    }

    interface X<T> {
        <T> T make(Class<T> t);
    };

    interface Y<T> extends  X<T>{

        default <T> T make2(Class<T> t) {
            return null;
        }
    }

    private final ComparisonRelation relation;

    @Override
    public String toString() {
        return this.relation.toString();
    }
}
