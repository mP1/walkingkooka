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
 * Base class for all non logical (binary) selectors.
 */
abstract class NonLogicalNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NodeSelector<N, NAME, ANAME, AVALUE> {

    NonLogicalNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        super();
        this.next = next;
    }

    @Override final NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return this.append1(this.next.append0(selector));
    }

    abstract NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    /**
     * Used to match a node wrapped in an optional. Empty optionals have no effect.
     */
    final void select(final Optional<N> node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        if (node.isPresent()) {
            this.select(node.get(), context);
        }
    }

    /**
     * The default simply records the {@link Node} to the {@link NodeSelectorContext}.
     */
    void select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.next.accept0(node, context);
    }

    @Override final void toString0(final NodeSelectorToStringBuilder b) {
        this.toString1(b);
        this.toStringNext(b);
    }

    abstract void toString1(final NodeSelectorToStringBuilder b);

    final void toStringNext(final NodeSelectorToStringBuilder b) {
        if (null != this.next) {
            this.next.toString0(b);
        }
    }

    private final NodeSelector<N, NAME, ANAME, AVALUE> next;

    // Object

    public final int hashCode() {
        return this.hashCode0(this.next);
    }

    abstract int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next);

    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final NonLogicalNodeSelector<?, ?, ?, ?> other) {
        return this.equals1(other) && this.next.equals(other.next);
    }

    abstract boolean equals1(final NonLogicalNodeSelector<?, ?, ?, ?> other);

    @Override final NodeSelector<N, NAME, ANAME, AVALUE> unwrapIfCustomToStringNodeSelector() {
        return this;
    }
}
