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
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;


/**
 * A {@link NodeSelector} that begins the search at the root of the graph.
 */
final class AbsoluteNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link AbsoluteNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> AbsoluteNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(INSTANCE);
    }

    @SuppressWarnings("unchecked")
    private final static AbsoluteNodeSelector INSTANCE = new AbsoluteNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private AbsoluteNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        // no point appending a descending to a absolute, as it already is a absolute search start...
        return selector instanceof AbsoluteNodeSelector ?
                this :
                selector instanceof DescendantNodeSelector ?
                        selector :
                        new AbsoluteNodeSelector<N, NAME, ANAME, AVALUE>(selector);
    }

    @Override
    final N accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final NodePointer<N, NAME> pointer = node.pointer();
        final N node2 = this.select(node.root(), context);
        return pointer.traverse(node2).orElse(node2); // try and return the Node at the equivalent location as $node otherwise return node2 itself.
    }

    @Override
    final int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next);
    }

    @Override
    final boolean equals1(final NonLogicalNodeSelector<?, ?, ?, ?> other) {
        return true;
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.absolute();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AbsoluteNodeSelector;
    }
}
