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
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Base class for all predicate functions.
 */
abstract class NodeSelectorPredicateFunction<T> implements BiFunction<List<Object>, ExpressionEvaluationContext, T> {

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

    final static NodeSelectorPredicatePositionFunction POSITION = NodeSelectorPredicatePositionFunction.INSTANCE;

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
     * Not the expected count should not include the implied {@link Node} which occupies position 0.
     */
    final void checkParameterCount(final List<Object> parameters, final int expectedCount) {
        final int count = parameters.size() - 1;
        if (expectedCount != count) {
            throw new IllegalArgumentException("Expected " + expectedCount + " but got " + count + "=" + parameters);
        }
    }

    /**
     * Converts a value into a boolean.
     */
    final boolean booleanValue(final Object value, final ExpressionEvaluationContext context) {
        return context.convert(value, Boolean.class);
    }

    /**
     * Converts a value into a {@link Comparable} with type parameters.
     */
    final Comparable comparable(final Object value, final ExpressionEvaluationContext context) {
        return context.convert(value, Comparable.class);
    }

    /**
     * Converts a value into an integer.
     */
    final int integer(final Object value, final ExpressionEvaluationContext context) {
        return context.convert(value, Integer.class);
    }

    /**
     * Converts a value into a {@link Number}.
     */
    final Number number(final Object value, final ExpressionEvaluationContext context) {
        return context.convert(value, Number.class);
    }

    /**
     * Converts a value into a string.
     */
    final String string(final Object value, final ExpressionEvaluationContext context) {
        return context.convert(value, String.class);
    }

    /**
     * Type safe {@link Boolean} parameter getter.
     */
    final Boolean booleanValue(final List<?> parameters, final int i, final ExpressionEvaluationContext context) {
        return this.booleanValue(this.parameter(parameters, i), context);
    }

    /**
     * Type safe {@link Comparable} parameter getter.
     */
    final Comparable comparable(final List<?> parameters, final int i, final ExpressionEvaluationContext context) {
        return this.comparable(this.parameter(parameters, i), context);
    }

    /**
     * Type safe integer parameter getter.
     */
    final int integer(final List<?> parameters, final int i, final ExpressionEvaluationContext context) {
        return this.integer(this.parameter(parameters, i), context);
    }

    /**
     * Type safe integer parameter getter.
     */
    final Node<?, ?, ?, ?> node(final List<?> parameters) {
        return Cast.to(parameters.get(0));
    }

    /**
     * Type safe number parameter getter.
     */
    final Number number(final List<?> parameters, final int i, final ExpressionEvaluationContext context) {
        return this.number(this.parameter(parameters, i), context);
    }

    /**
     * Type safe String parameter getter.
     */
    final String string(final List<?> parameters, final int i, final ExpressionEvaluationContext context) {
        return this.string(this.parameter(parameters, i), context);
    }

    /**
     * Retrieves the parameter at the index or throws a nice exception message.
     */
    final <T> T parameter(final List<?> parameters, final int i, final Class<T> type, final ExpressionEvaluationContext context) {
        return context.convert(this.parameter(parameters, i), type);
    }

    /**
     * Retrieves the parameter at the index or throws a nice exception message.
     */
    private Object parameter(final List<?> parameters, final int i) {
        final int count = parameters.size() - 1; // without node at 0.
        if (i < 0 || i >= count) {
            throw new NodeSelectorException("Parameter " + i + " missing from " + parameters);
        }
        return parameters.get(i + 1);
    }

    @Override
    abstract public String toString();
}
