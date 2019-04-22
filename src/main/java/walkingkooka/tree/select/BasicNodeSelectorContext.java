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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link NodeSelectorContext} that routes potential and selected {@link Node} to individual {@link Consumer}
 */
final class BasicNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> BasicNodeSelectorContext<N, NAME, ANAME, AVALUE> with(final Consumer<N> potential,
                                                                          final Consumer<N> selected,
                                                                          final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions,
                                                                          final Converter converter,
                                                                          final DecimalNumberContext decimalNumberContext) {
        Objects.requireNonNull(potential, "potential");
        Objects.requireNonNull(selected, "selected");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(converter, "converter");
        Objects.requireNonNull(decimalNumberContext, "decimalNumberContext");

        return new BasicNodeSelectorContext<>(potential, selected, functions, converter, decimalNumberContext);
    }

    private BasicNodeSelectorContext(final Consumer<N> potential,
                                     final Consumer<N> selected,
                                     final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions,
                                     final Converter converter,
                                     final DecimalNumberContext decimalNumberContext) {
        this.potential = potential;
        this.selected = selected;
        this.functions = functions;
        this.converter = converter;
        this.decimalNumberContext = decimalNumberContext;
    }

    @Override
    public void potential(final N node) {
        this.potential.accept(node);
        this.node = node;
    }

    private final Consumer<N> potential;

    @Override
    public void selected(final N node) {
        this.selected.accept(node);
    }

    private final Consumer<N> selected;

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
        nodeAndParameters.add(this.node);
        nodeAndParameters.addAll(parameters);
        return function.get().apply(Lists.readOnly(nodeAndParameters), this);
    }

    private final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions;

    private N node;

    @Override
    public String toString() {
        return this.potential + " " + this.selected + " " + this.functions + " " + this.converter + " " + this.decimalNumberContext;
    }
}
