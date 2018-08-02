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
 * A {@link NodeSelector} that selects all the ancestors of a given {@link Node} until the root of the graph is reached.
 */
final class AncestorNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        UnaryRelativeNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link AncestorNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> AncestorNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static AncestorNodeSelector INSTANCE = new AncestorNodeSelector();

    /**
     * Private constructor use type safe getter
     */
    private AncestorNodeSelector() {
        super();
    }

    /**
     * Private constructor
     */
    private AncestorNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
         // no point appending a ancestor to another...
        return selector instanceof AncestorNodeSelector ?
                this :
                new AncestorNodeSelector(selector);
    }

    @Override
    final void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.matchParent(node, context);
    }

    @Override
    void match(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super.match(node, context);

        this.matchParent(node, context);
    }

    @Override
    void toString0(final StringBuilder b, String separator){
        b.append(separator).append("ancestor");
        this.toStringNext(b, DEFAULT_AXIS_SEPARATOR);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AncestorNodeSelector;
    }
}
