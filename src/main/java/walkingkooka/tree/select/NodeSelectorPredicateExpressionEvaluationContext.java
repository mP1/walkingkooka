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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

import java.util.List;

/**
 * A {@link ExpressionEvaluationContext} that is used when evaluating predicates.
 * It includes numerous methods for converting values to the basic types of all functions, as well as retrieving the current node.
 */
interface NodeSelectorPredicateExpressionEvaluationContext<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
        extends ExpressionEvaluationContext {

    /**
     * Returns the current node.
     *
     * @return
     */
    abstract N node();

    /**
     * Converts a value into a boolean.
     */
    default boolean booleanValue(final Object value) {
        return this.convert(value, Boolean.class);
    }

    /**
     * Converts a value into a {@link Comparable} with type parameters.
     */
    default Comparable comparable(final Object value) {
        return this.convert(value, Comparable.class);
    }

    /**
     * Converts a value into an integer.
     */
    default int integer(final Object value) {
        return this.convert(value, Integer.class);
    }

    /**
     * Converts a value into a string.
     */
    default String string(final Object value) {
        return this.convert(value, String.class);
    }

    /**
     * Type safe integer parameter getter.
     */
    default Comparable comparable(final List<?> parameters, final int i) {
        return this.comparable(this.parameter(parameters, i));
    }

    /**
     * Type safe integer parameter getter.
     */
    default int integer(final List<?> parameters, final int i) {
        return this.integer(this.parameter(parameters, i));
    }

    /**
     * Type safe String parameter getter.
     */
    default String string(final List<?> parameters, final int i) {
        return this.string(this.parameter(parameters, i));
    }

    /**
     * Retrieves the parameter at the index or throws a nice exception message.
     */
    default <T> T parameter(final List<?> parameters, final int i, final Class<T> type) {
        return this.convert(this.parameter(parameters, i), type);
    }

    /**
     * Retrieves the parameter at the index or throws a nice exception message.
     */
    default Object parameter(final List<?> parameters, final int i) {
        final int count = parameters.size();
        if (i < 0 || i >= count) {
            throw new NodeSelectorException("Parameter " + i + " missing from " + parameters);
        }
        return parameters.get(i);
    }
}
