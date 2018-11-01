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
 * A {@link NodeSelector} that selects all the direct children of a given {@link Node}.
 */
final class ChildrenNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link ChildrenNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> ChildrenNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(ChildrenNodeSelector.INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static ChildrenNodeSelector INSTANCE = new ChildrenNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private ChildrenNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new ChildrenNodeSelector(selector);
    }

    // NodeSelector

    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.selectChildren(node, context);
    }

    // Object

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axis("child");
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ChildrenNodeSelector;
    }
}
