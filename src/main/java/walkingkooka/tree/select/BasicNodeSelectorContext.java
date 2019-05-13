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

import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionException;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A {@link NodeSelectorContext} that routes test and selected {@link Node} to a individual {@link Consumer}
 */
final class BasicNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> BasicNodeSelectorContext<N, NAME, ANAME, AVALUE> with(final BooleanSupplier finisher,
                                                                          final Predicate<N> filter,
                                                                          final Function<N, N> mapper,
                                                                          final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions,
                                                                          final Converter converter,
                                                                          final DecimalNumberContext decimalNumberContext,
                                                                          final Class<N> nodeType) {
        Objects.requireNonNull(finisher, "finisher");
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(mapper, "mapper");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(decimalNumberContext, "decimalNumberContext");
        Objects.requireNonNull(nodeType, "nodeType");

        return new BasicNodeSelectorContext<>(finisher,
                filter,
                mapper,
                functions,
                converter,
                decimalNumberContext);
    }

    private BasicNodeSelectorContext(final BooleanSupplier finisher,
                                     final Predicate<N> filter,
                                     final Function<N, N> mapper,
                                     final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions,
                                     final Converter converter,
                                     final DecimalNumberContext decimalNumberContext) {
        this.finisher = finisher;
        this.filter = filter;
        this.mapper = mapper;
        this.functions = functions;
        this.converter = converter;
        this.decimalNumberContext = decimalNumberContext;
    }

    @Override
    public boolean isFinished() {
        return this.finisher.getAsBoolean();
    }

    /**
     * Allows outsider termination of node selection.
     */
    private final BooleanSupplier finisher;

    @Override
    public boolean test(final N node) {
        // $current must be set here, so ExpressionNodeSelector can execute functions that require the current node.
        this.current = node;
        return this.filter.test(node);
    }

    /**
     * The {@link Consumer} receives all {@link Node} that are visited, this provides an opportunity to throw
     * an {@link RuntimeException} to abort a long running select.
     */
    private final Predicate<N> filter;

    @Override
    public N selected(final N node) {
        return this.mapper.apply(node);
    }

    private final Function<N, N> mapper;

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        return this.converter.convert(value, target, ConverterContexts.basic(this.decimalNumberContext));
    }

    private final Converter converter;

    private final DecimalNumberContext decimalNumberContext;

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        final Optional<ExpressionFunction<?>> function = this.functions.apply(name);
        if (!function.isPresent()) {
            throw new ExpressionException("Unknown function " + name);
        }

        final List<Object> nodeAndParameters = Lists.array();
        nodeAndParameters.add(this.current);
        nodeAndParameters.addAll(parameters);
        return function.get().apply(Lists.readOnly(nodeAndParameters), this);
    }

    private final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions;

    /**
     * The current {@link Node} which is also becomes the first argument for all function invocations.
     */
    private N current;

    @Override
    public String toString() {
        return this.finisher + " " +
                this.filter + " " +
                this.mapper + " " +
                this.functions + " " +
                this.converter + " " +
                this.decimalNumberContext;
    }
}
