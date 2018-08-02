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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A selector maybe used to select zero or more nodes within a tree given a {@link Node}.
 */
public abstract class NodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> {

    /**
     * This method is only ever called by {@link Node#selector()}
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> path(final N node) {
        return PathNodeSelector.with(node);
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelector(){
    }

    // NodeSelector

    final NodeSelector<N, NAME, ANAME, AVALUE> append(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        Objects.requireNonNull(selector, "selector");

        return this.append0(selector);
    }

    abstract NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    /**
     * Returns a {@link Consumer} that does nothing and may be passed to {@link #accept(Node, Consumer)}
     */
    final public Consumer<N> nulObserver() {
        return NodeSelectorNulObserverConsumer.get();
    }

    /**
     * Accepts a starting {@link Node} anywhere in a tree returning all matching nodes.
     * The {@link Consumer} is invoked for each and every {@link Node} prior to any test and continued traversal. It may be
     * used to abort the visiting process by throwing an {@link RuntimeException}
     */
    abstract public Set<N> accept(final N node, final Consumer<N> observer);

    /**
     * Sub classes must call this method which calls the observer and then immediately calls {@link #accept0(Node, NodeSelectorContext)}
     */
    final void accept(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        context.observer().accept(node);
        this.accept0(node, context);
    }

    /**
     * Sub classes must implement this to contain the core logic in testing if a node is match or unmatched.
     */
    abstract void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context);

    /**
     * Pushes all siblings {@link Node nodes} until itself is reached.
     */
    final void matchPrecedingSiblings(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final Optional<N> parent = node.parent();

        if (parent.isPresent()) {
            N current = node;
            for (; ; ) {
                final Optional<N> next = current.previousSibling();
                if (!next.isPresent()) {
                    break;
                }
                current = next.get();
                this.match(current, context);
            }
        }
    }

    /**
     * Pushes all siblings {@link Node nodes} after itself.
     */
    final void matchFollowingSiblings(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {

            N current = node;
            for (; ; ) {
                final Optional<N> next = current.nextSibling();
                if (!next.isPresent()) {
                    break;
                }
                current = next.get();
                this.match(current, context);
            }
        }
    }

    /**
     * Pushes all direct children of the {@link Node node}.`
     */
    final void matchChildren(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        for (final N child : node.children()) {
            this.match(child, context);
        }
    }

    /**
     * Matches the parent only if one is present.
     */
    final void matchParent(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {
            this.match(parent.get(), context);
        }
    }

    /**
     * Handles a matched {@link Node}
     */
    abstract void match(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context);

    final static String DEFAULT_AXIS_SEPARATOR = "::";

    /**
     * Returns a {@link Predicate} that returns true if at least one {@link Node} is matched.
     */
    public final Predicate<N> asPredicate() {
        return new NodeSelectorPredicate<>(this);
    }

    /**
     * Force sub classes to implement.
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b, "");
        return b.toString();
    }

    abstract void toString0(final StringBuilder b, final String separator);

    abstract void toStringNext(final StringBuilder b, final String separator);
}
