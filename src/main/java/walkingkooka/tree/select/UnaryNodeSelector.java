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
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Base class for all non logical (binary) selectors.
 */
abstract class UnaryNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends NodeSelector<N, NAME, ANAME, AVALUE> {

    UnaryNodeSelector() {
        this(TerminalNodeSelector.get());
    }

    UnaryNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        super();
        this.next = next;
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector){
        return this.append1(this.next.append0(selector));
    }
    
    abstract NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    final Set<N> accept1(final N node, final Consumer<N> observer) {
        final Set<N> matches = Sets.ordered();
        this.accept(node, new NodeSelectorNodeSelectorContext<>(observer, matches));
        return matches;
    }

    /**
     * Used to match a node wrapped in an optional. Empty optionals have no effect.
     */
    final void match(final Optional<N> node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        if(node.isPresent()){
            this.match(node.get(), context);
        }
    }

    /**
     * The default simply records the {@link Node} to the {@link NodeSelectorContext}.
     */
    void match(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.next.accept(node, context);
    }

    @Override
    void toString0(final NodeSelectorToStringBuilder b) {
        this.toString1(b);
        this.toStringNext(b);
    }

    abstract void toString1(final NodeSelectorToStringBuilder b);

    final void toStringNext(final NodeSelectorToStringBuilder b){
        if(null!=this.next) {
            this.next.toString0(b);
        }
    }

    final NodeSelector<N, NAME, ANAME, AVALUE> next;

    // Object

    public final int hashCode(){
        return this.hashCode0(this.next);
    }

    abstract int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next);

    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final UnaryNodeSelector<N, NAME, ANAME, AVALUE> other) {
        return this.equals1(other) && this.next.equals(other.next);
    }

    abstract boolean equals1(final UnaryNodeSelector<N, NAME, ANAME, AVALUE> other);
}
