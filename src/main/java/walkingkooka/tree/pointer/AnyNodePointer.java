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

/**
 * Matches all the nodes, or the start node.
 */
final class AnyNodePointer<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME>{

    /**
     * Creates a {@link AnyNodePointer}
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> AnyNodePointer<N, NAME> get() {
        return Cast.to(INSTANCE);
    }

    private final static NodePointer INSTANCE = new AnyNodePointer();

    /**
     * Private ctor.
     */
    private AnyNodePointer() {
        super(null);
    }

    @Override
    NodePointer<N, NAME> append(final NodePointer<N, NAME> pointer) {
        return pointer;
    }

    @Override
    N nextNodeOrNull(final N node) {
        return node;
    }

    @Override
    public boolean isRelative() {
        return false;
    }

    @Override
    void toString0(final StringBuilder b) {
        // nop
    }

    @Override
    void lastToString(final StringBuilder b) {
    }
}
