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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An absolute path for a {@link Node}.
 */
final class PathNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> extends NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> with(final N node) {
        Objects.requireNonNull(node, "node");
        return node.isRoot() ? SelfNodeSelector.get() : new PathNodeSelector<>(node, NodeSelector.terminal());
    }

    private PathNodeSelector(final N node, final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);

        final List<Integer> path = Lists.array();
        this.path = path;
        walkAncestorPath(node, path);
    }

    private static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> void walkAncestorPath(final N node, final List<Integer> path) {
        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {
            walkAncestorPath(parent.get(), path);
            path.add(node.index() + 1);
        }
    }

    @Override
    void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        boolean abort = false;

        N current = node;
        for (Integer index : this.path) {
            final int zeroIndex = index - 1;
            final List<N> children = current.children();
            if (zeroIndex >= children.size()) {
                abort = true;
                break;
            }
            current = children.get(zeroIndex);
        }

        if (!abort) {
            this.select(current, context);
        }
    }

    @Override
    void select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        context.selected(node);
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    int hashCode0(NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return this.path.hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionNodeSelector;
    }

    @Override
    boolean equals1(final NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final PathNodeSelector<N, NAME, ANAME, AVALUE> other) {
        return this.path.equals(other.path);
    }

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        this.path.stream().forEach(i -> b.predicate(i.toString()));
    }

    private final List<Integer> path;
}
