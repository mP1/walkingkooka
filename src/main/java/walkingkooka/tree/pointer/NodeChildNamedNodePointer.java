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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Objects;

/**
 * Represents a component that matches a node by its name from a parent.
 */
final class NodeChildNamedNodePointer<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> extends NodePointer<N, NAME, ANAME, AVALUE>{

    /**
     * Creates a new {@link NodeChildNamedNodePointer}.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeChildNamedNodePointer<N, NAME, ANAME, AVALUE> with(final NAME name) {
        Objects.requireNonNull(name, "name");

        return new NodeChildNamedNodePointer(name, NO_NEXT);
    }

    /**
     * Private ctor.
     */
    private NodeChildNamedNodePointer(final NAME name, final NodePointer<N, NAME, ANAME, AVALUE> next) {
        super(next);
        this.name = name;
    }

    @Override
    NodePointer<N, NAME, ANAME, AVALUE> append(final NodePointer<N, NAME, ANAME, AVALUE> pointer) {
        return new NodeChildNamedNodePointer(this.name, this.appendToNext(pointer));
    }

    @Override
    final N nextNodeOrNull(final N node) {
        N matched = null;

        for(N child : node.children()) {
            if(child.name().equals(this.name)){
                matched = child;
                break;
            }
        }
        return matched;
    }

    private final NAME name;

    @Override
    final void toString0(final StringBuilder b) {
        b.append(SEPARATOR.character());
        b.append(this.name.value()); // escape slash and tilde
    }

    @Override
    final void lastToString(final StringBuilder b){
    }
}
