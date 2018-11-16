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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

/**
 * A {@link NodeSelector} that selects all the ancestors or self of a given {@link Node} until the root of the graph is reached.
 */
final class AncestorOrSelfNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link AncestorOrSelfNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> AncestorOrSelfNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static AncestorOrSelfNodeSelector INSTANCE = new AncestorOrSelfNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private AncestorOrSelfNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
         // no point appending a ancestor to another...
        return selector instanceof AncestorOrSelfNodeSelector ?
                this :
                new AncestorOrSelfNodeSelector<N, NAME, ANAME, AVALUE>(selector);
    }

    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.select(node, context);
        this.selectParent(node, context);
    }

    @Override
    void select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super.select(node, context);

        this.selectParent(node, context);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axis("ancestor-or-self");
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AncestorOrSelfNodeSelector;
    }
}
