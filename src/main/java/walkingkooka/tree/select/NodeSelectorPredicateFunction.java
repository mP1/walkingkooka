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

import walkingkooka.collect.map.Maps;
import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Base class for all predicate functions.
 */
abstract class NodeSelectorPredicateFunction<T> implements BiFunction<List<Object>, NodeSelectorPredicateExpressionEvaluationContext<?, ?, ?, ?>, T> {

    /**
     * A {@link Map} holding all functions by name. This requires that all functions are stateless.
     */
    final static Map<ExpressionNodeName, NodeSelectorPredicateFunction<?>> NAME_TO_FUNCTION = Maps.ordered();

    final static NodeSelectorPredicateBooleanFunction BOOLEAN = NodeSelectorPredicateBooleanFunction.INSTANCE;

    final static NodeSelectorPredicateConcatFunction CONCAT = NodeSelectorPredicateConcatFunction.INSTANCE;

    final static NodeSelectorPredicateContainsFunction CONTAINS = NodeSelectorPredicateContainsFunction.INSTANCE;

    final static NodeSelectorPredicateEndsWithFunction ENDS_WITH = NodeSelectorPredicateEndsWithFunction.INSTANCE;

    final static NodeSelectorPredicateFalseFunction FALSE = NodeSelectorPredicateFalseFunction.INSTANCE;

    final static NodeSelectorPredicateNameFunction NAME = NodeSelectorPredicateNameFunction.INSTANCE;

    final static NodeSelectorPredicateNormalizeSpaceFunction NORMALIZE_SPACE = NodeSelectorPredicateNormalizeSpaceFunction.INSTANCE;

    final static NodeSelectorPredicateNumberFunction NUMBER = NodeSelectorPredicateNumberFunction.INSTANCE;

    final static NodeSelectorPredicateStartsWithFunction STARTS_WITH = NodeSelectorPredicateStartsWithFunction.INSTANCE;

    final static NodeSelectorPredicateStringLengthFunction STRING_LENGTH = NodeSelectorPredicateStringLengthFunction.INSTANCE;

    final static NodeSelectorPredicateSubstringFunction SUBSTRING = NodeSelectorPredicateSubstringFunction.INSTANCE;

    final static NodeSelectorPredicateSubstringAfterFunction SUBSTRING_AFTER = NodeSelectorPredicateSubstringAfterFunction.INSTANCE;

    final static NodeSelectorPredicateSubstringBeforeFunction SUBSTRING_BEFORE = NodeSelectorPredicateSubstringBeforeFunction.INSTANCE;

    final static NodeSelectorPredicateTextFunction TEXT = NodeSelectorPredicateTextFunction.INSTANCE;

    final static NodeSelectorPredicateTrueFunction TRUE = NodeSelectorPredicateTrueFunction.INSTANCE;

    /**
     * Checks that the given function name is a supported function name.
     */
    static ExpressionNodeName checkSupported(final String name) {
        final ExpressionNodeName key = key(name);
        if (!NAME_TO_FUNCTION.containsKey(key)) {
            throw new NodeSelectorException("Unknown function " + key);
        }
        return key;
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorPredicateFunction() {
        super();
        if (false == this instanceof NodeSelectorPredicateComparisonFunction) {
            NAME_TO_FUNCTION.put(key(this.toString()), this);
        }
    }

    private static ExpressionNodeName key(final String name) {
        return ExpressionNodeName.with(name);
    }

    /**
     * Checks and complains if the parameter count doesnt match the expected count.
     */
    final void checkParameterCount(final List<Object> parameters, final int expectedCount) {
        final int count = parameters.size();
        if (expectedCount != count) {
            throw new IllegalArgumentException("Expected " + expectedCount + " but got " + count + "=" + parameters);
        }
    }

    @Override
    abstract public String toString();
}
