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

import walkingkooka.convert.Converter;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.type.PublicStaticHelper;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A collection of factory methods to create {@link NodeSelectorContext}.
 */
public final class NodeSelectorContexts implements PublicStaticHelper {

    /**
     * {@see NodeSelectorContext}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelectorContext<N, NAME, ANAME, AVALUE> basic(final Consumer<N> potential,
                                                                      final Function<N, N> mapper,
                                                                      final Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> functions,
                                                                      final Converter converter,
                                                                      final DecimalNumberContext decimalNumberContext) {
        return BasicNodeSelectorContext.with(potential, mapper, functions, converter, decimalNumberContext);
    }

    /**
     * {@see BasicNodeSelectorContextFunction}
     */
    public static Function<ExpressionNodeName, Optional<ExpressionFunction<?>>> basicFunctions() {
        return BasicNodeSelectorContextFunction.INSTANCE;
    }

    /**
     * {@see FakeNodeSelectorContext}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelectorContext<N, NAME, ANAME, AVALUE> fake() {
        return new FakeNodeSelectorContext<N, NAME, ANAME, AVALUE>();
    }

    /**
     * Stop creation.
     */
    private NodeSelectorContexts() {
        throw new UnsupportedOperationException();
    }
}
