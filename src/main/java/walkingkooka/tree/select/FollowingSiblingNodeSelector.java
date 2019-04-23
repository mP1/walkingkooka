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
 * A {@link NodeSelector} that selects all the following siblings of a given {@link Node}.
 */
final class FollowingSiblingNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link FollowingSiblingNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> FollowingSiblingNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(FollowingSiblingNodeSelector.INSTANCE);
    }

    @SuppressWarnings("unchecked")
    private final static FollowingSiblingNodeSelector INSTANCE = new FollowingSiblingNodeSelector(NodeSelector.terminal());

    /**
     * Private constructor
     */
    private FollowingSiblingNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        // no point appending a followingSibling to another...
        return selector instanceof FollowingSiblingNodeSelector ?
                this :
                new FollowingSiblingNodeSelector<>(selector);
    }

    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.selectFollowingSiblings(node, context);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.axis("following-sibling");
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof FollowingSiblingNodeSelector;
    }
}
