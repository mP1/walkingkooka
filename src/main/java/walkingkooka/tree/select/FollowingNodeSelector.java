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
 * A {@link NodeSelector} that selects all the preceding nodes of a given {@link Node}. It does this by asking all ancestors to process their following
 * siblings and their ancestors.
 */
final class FollowingNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        UnaryRelativeNodeSelector2<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link FollowingNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> FollowingNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(FollowingNodeSelector.INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static FollowingNodeSelector INSTANCE = new FollowingNodeSelector();

    /**
     * Private constructor use type safe getter
     */
    private FollowingNodeSelector() {
        super();
    }

    /**
     * Private constructor
     */
    private FollowingNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        // no point appending a following to another...
        return selector instanceof FollowingNodeSelector ?
                this :
                new FollowingNodeSelector(selector);
    }

    @Override
    void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.matchChildren(node, context);
        this.matchFollowingSiblings(node, context);

        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {
            this.matchFollowingSiblings(parent.get(), context);
        }
    }

    @Override
    void match(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        super.match(node, context);
        this.matchChildren(node, context);
    }

    @Override
    void toString0(final StringBuilder b, String separator){
        b.append(separator).append("following");
        this.toStringNext(b, DEFAULT_AXIS_SEPARATOR);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof FollowingNodeSelector;
    }
}