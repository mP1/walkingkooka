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
import walkingkooka.naming.PathSeparator;
import walkingkooka.tree.Node;

import java.util.Objects;

/**
 * A {@link NodeSelector} that selects all the ancestors of a given {@link Node} until the root of the graph is reached.
 */
final class NamedNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonLogicalNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link NamedNodeSelector} factory
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NamedNodeSelector<N, NAME, ANAME, AVALUE> with(final NAME name, final PathSeparator separator) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(separator, "separator");

        return new NamedNodeSelector<N, NAME, ANAME, AVALUE>(name, separator, NodeSelector.terminal());
    }

    /**
     * Private constructor
     */
    private NamedNodeSelector(final NAME name, final PathSeparator separator, final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.name = name;
        this.separator = separator;
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new NamedNodeSelector<>(this.name, this.separator, selector);
    }

    @Override
    final void accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        if (this.name.equals(node.name())) {
            this.select(node, context);
        }
    }

    private final NAME name;

    @Override
    void toString1(final NodeSelectorToStringBuilder b) {
        b.separator(this.separator);
        b.node(this.name.value());
    }

    private final PathSeparator separator;

    @Override
    int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next, this.name);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NamedNodeSelector;
    }

    @Override
    boolean equals1(final NonLogicalNodeSelector<?, ?, ?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final NamedNodeSelector<N, NAME, ANAME, AVALUE> other) {
        return this.name.equals(other.name) &&
                this.separator.equals(other.separator);
    }
}
