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

import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;

import java.util.List;
import java.util.Set;

/**
 * The context used by {@link AndNodeSelector} which batches selected nodes so they can be ANDED with another batch.
 */
final class AndNodeSelectorNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> AndNodeSelectorNodeSelectorContext<N, NAME, ANAME, AVALUE> with(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return new AndNodeSelectorNodeSelectorContext<>(context);
    }

    private AndNodeSelectorNodeSelectorContext(final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super();
        this.context = context;
    }

    /**
     * Potential {@link Node nodes} are immediately notified to the wrapped {@link NodeSelectorContext}
     */
    @Override
    public void potential(final N node) {
        this.context.potential(node);
    }

    @Override
    public N selected(final N node) {
        this.selected.add(node);
        return node;
    }

    Set<N> selected = Sets.ordered();

    @Override
    public <T> T convert(final Object value, final Class<T> target) {
        return this.context.convert(value, target);
    }

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        return this.context.function(name, parameters);
    }

    private final NodeSelectorContext<N, NAME, ANAME, AVALUE> context;

    @Override
    public String toString() {
        return this.context + " " + this.selected;
    }
}
