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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A {@link NodeSelector} that continues to walking the tree, continuously trying the next select.
 */
final class OrNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        LogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link OrNodeSelector} creator
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> with(
            final List<NodeSelector<N, NAME, ANAME, AVALUE>> selectors) {
        Objects.requireNonNull(selectors, "selectors");

        final Collection<NodeSelector<N, NAME, ANAME, AVALUE>> unique = gatherUniques(selectors);
        return unique.size() == 1 ? unique.iterator().next() : new OrNodeSelector<N, NAME, ANAME, AVALUE>(unique);
    }

    /**
     * Private constructor called by factory
     */
    private OrNodeSelector(final Collection<NodeSelector<N, NAME, ANAME, AVALUE>> selectors) {
        super(selectors);
    }

    // LogicalNodeSelector

    @Override
    final N accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        N last = node;

        for (NodeSelector<N, NAME, ANAME, AVALUE> selector : this.selectors) {
            last = selector.accept1(last, context);
        }

        return last;
    }

    @Override
    String operatorToString() {
        return "|";
    }

    boolean canBeEqual(final Object other) {
        return other instanceof OrNodeSelector;
    }
}
