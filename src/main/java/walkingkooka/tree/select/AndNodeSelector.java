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
 */

package walkingkooka.tree.select;

import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A {@link NodeSelector} that continues to walking the tree, continuously trying the next select.
 */
final class AndNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        LogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link AndNodeSelector} creator
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> with(
            final List<NodeSelector<N, NAME, ANAME, AVALUE>> selectors) {
        Objects.requireNonNull(selectors, "selectors");

        final Collection<NodeSelector<N, NAME, ANAME, AVALUE>> unique = gatherUniques(selectors);
        return unique.size() == 1 ? unique.iterator().next() : new AndNodeSelector<N, NAME, ANAME, AVALUE>(unique);
    }

    /**
     * Private constructor called by factory
     */
    private AndNodeSelector(final Collection<NodeSelector<N, NAME, ANAME, AVALUE>> selectors) {
        super(selectors);
    }

    @Override
    public Set<N> accept(final N node, final Consumer<N> observer) {
        Set<N> all = null;

        for(NodeSelector<N, NAME, ANAME, AVALUE> selector : this.selectors) {
            final Set<N> current = Sets.ordered();
            selector.accept(node, new NodeSelectorNodeSelectorContext<>(observer, current));

            if(null==all) {
                all = current;
            } else {
                all.retainAll(current);
            }
            if(all.isEmpty()) {
                break;
            }
        }

        return all;
    }

    @Override
    final void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        for(N match : this.accept(node, context.observer())) {
            context.match(match);
        }
    }

    @Override
    String operatorToString(){
        return "&";
    }

    boolean canBeEqual(final Object other){
        return other instanceof AndNodeSelector;
    }
}
