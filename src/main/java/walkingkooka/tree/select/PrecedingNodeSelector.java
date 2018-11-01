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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Optional;

/**
 * A {@link NodeSelector} that selects all the preceding nodes of a given {@link Node}. It does this by asking all ancestors to process their previous
 * siblings and their ancestors.
 */
final class PrecedingNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link PrecedingNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> PrecedingNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(PrecedingNodeSelector.INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static PrecedingNodeSelector INSTANCE = new PrecedingNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private PrecedingNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        // no point appending a preceeding to another...
        return selector instanceof PrecedingNodeSelector ?
                this :
                new PrecedingNodeSelector<>(selector);
    }

    @Override
    void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.selectChildren(node, context);
        this.selectPrecedingSiblings(node, context);

        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {
            this.selectPrecedingSiblings(parent.get(), context);
        }
    }

    @Override
    void select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super.select(node, context);
        this.selectChildren(node, context);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axis("preceding");
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof PrecedingNodeSelector;
    }
}
