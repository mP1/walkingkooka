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
 *
 */

package walkingkooka.tree.pointer;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Objects;

/**
 * Represents a component that matches a node by its name from a parent.
 */
final class NamedChildNodePointer<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME>{

    /**
     * Creates a new {@link NamedChildNodePointer}.
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NamedChildNodePointer<N, NAME> with(final NAME name) {
        Objects.requireNonNull(name, "name");

        return new NamedChildNodePointer<N, NAME>(name, absent());
    }

    /**
     * Private ctor.
     */
    private NamedChildNodePointer(final NAME name, final NodePointer<N, NAME> next) {
        super(next);
        this.name = name;
    }

    @Override
    NodePointer<N, NAME> append(final NodePointer<N, NAME> pointer) {
        return new NamedChildNodePointer<>(this.name, this.appendToNext(pointer));
    }

    @Override
    N nextNodeOrNull(final N node) {
        N matched = null;

        for (N child : node.children()) {
            if (child.name().equals(this.name)) {
                matched = child;
                break;
            }
        }
        return matched;
    }

    private final NAME name;

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    N add0(final N node, final N value) {
        return node.setChild(this.name, value);
    }

    @Override
    N remove0(final N node) {
        return this.removeOrFail(node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NamedChildNodePointer;
    }

    @Override
    boolean equals1(final NodePointer<?, ?> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final NamedChildNodePointer<?, ?> other) {
        return this.name.equals(other.name);
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(SEPARATOR.character());
        b.append(this.name.value()
                .replace("~", "~0")
                .replace("/", "~1")); // escape slash and tilde
    }

    @Override
    void lastToString(final StringBuilder b) {
    }
}
