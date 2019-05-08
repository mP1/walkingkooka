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

/**
 * Base class for several {@link NodeSelectorContext} wrappers. Static factory methods are also available for all sub classes.
 */
abstract class NodeSelectorContext2<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    /**
     * {@see AllNodeSelectorContext2}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelectorContext2<N, NAME, ANAME, AVALUE> all(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return AllNodeSelectorContext2.with(context);
    }
    
    /**
     * {@see ExpressionNodeSelectorNodeSelectorContext2}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> ExpressionNodeSelectorNodeSelectorContext2<N, NAME, ANAME, AVALUE> expression(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return ExpressionNodeSelectorNodeSelectorContext2.with(context);
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorContext2(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super();
        this.context = context;
    }

    /**
     * Unconditionally returns a {@link AllNodeSelectorContext2}.
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> all();

    /**
     * The context should create a {@link ExpressionNodeSelectorNodeSelectorContext2} if it is not already one.
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> expressionCreateIfNecessary();

    /**
     * Unconditionally create a {@link ExpressionNodeSelectorNodeSelectorContext2}.
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> expression();

    /**
     * Invoked by {@link ExpressionNodeSelector} to test a value against the position of the current {@link Node}.
     */
    abstract boolean nodePositionTest(final Object value);

    /**
     * The active {@link NodeSelectorContext}.
     */
    final NodeSelectorContext<N, NAME, ANAME, AVALUE> context;
}
