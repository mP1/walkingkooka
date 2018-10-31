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

import java.util.List;

/**
 * A {@link NodeSelector} that pushes the child at the index from the {@link Node#children()}. Note that index begins
 * at 1.
 */
final class IndexedChildNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalRelativeNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Creates a {@link IndexedChildNodeSelector}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> IndexedChildNodeSelector<N, NAME, ANAME, AVALUE> with(
            final int index) {
        if (index < INDEX_BIAS) {
            throw new IllegalArgumentException("Invalid index " + index + " must begin at " + INDEX_BIAS);
        }
        return new IndexedChildNodeSelector<>(index);
    }

    /**
     * Private constructor use factory
     */
    private IndexedChildNodeSelector(final int index) {
        super();
        this.index = index;
    }

    /**
     * Private constructor
     */
    private IndexedChildNodeSelector(final int index, final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.index = index;
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new IndexedChildNodeSelector<>(this.index, selector);
    }

    @Override
    void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final List<N> children = node.children();
        final int index = this.index-INDEX_BIAS;
        if (index < children.size()) {
            this.match(children.get(index), context);
        }
    }

    private final int index;

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.predicate(String.valueOf(this.index));
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof IndexedChildNodeSelector;
    }
}
