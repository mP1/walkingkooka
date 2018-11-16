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


/**
 * A {@link NodeSelector} that selects all the descendants of a given {@link Node} until all are visited.
 */
final class DescendantNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link DescendantNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> DescendantNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static DescendantNodeSelector INSTANCE = new DescendantNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private DescendantNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        // no point appending a descending to another...
        return selector instanceof DescendantNodeSelector ?
                this :
                new DescendantNodeSelector<N, NAME, ANAME, AVALUE>(selector);
    }


    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.selectChildren(node, context);
    }

    @Override
    void select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super.select(node, context);

        this.selectChildren(node, context);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axis("descendant");
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DescendantNodeSelector;
    }
}
