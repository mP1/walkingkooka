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

import java.util.List;

/**
 * Represents a component that matches a node by its element.
 */
final class NodeChildElementNodePointer<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends NodePointer<N, NAME, ANAME, AVALUE>{

    /**
     * Creates a {@link NodeChildElementNodePointer}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> NodeChildElementNodePointer<N, NAME, ANAME, AVALUE> with(final int index) {
        if(index < 0) {
            throw new IllegalArgumentException("Invalid index " + index + " values should be greater or equal to 0");
        }

        return new NodeChildElementNodePointer<N, NAME, ANAME, AVALUE>(index, absent());
    }

    /**
     * Private ctor.
     */
    private NodeChildElementNodePointer(final int index, final NodePointer<N, NAME, ANAME, AVALUE> pointer) {
        super(pointer);
        this.index = index;
    }

    @Override
    NodePointer<N, NAME, ANAME, AVALUE> append(final NodePointer<N, NAME, ANAME, AVALUE> pointer) {
        return new NodeChildElementNodePointer<N, NAME, ANAME, AVALUE>(this.index, this.appendToNext(pointer));
    }

    @Override
    final N nextNodeOrNull(final N node) {
        final List<N> children = node.children();
        return this.index < children.size() ?
               children.get(this.index) :
               null;
    }

    private final int index;

    @Override
    public final boolean isRelative(){
        return false;
    }

    @Override
    final void toString0(final StringBuilder b) {
        b.append(SEPARATOR.character());
        b.append(this.index);
    }

    @Override
    final void lastToString(final StringBuilder b){
    }
}
