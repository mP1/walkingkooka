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

import java.util.Optional;

/**
 * Represents the opening relative pointer..
 */
final class RelativeNodePointer<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointer<N, NAME>{

    /**
     * Creates a {@link RelativeNodePointer}
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> RelativeNodePointer<N, NAME> with(final int ancestorCount, final boolean hash) {
        if(ancestorCount < 0) {
            throw new IllegalArgumentException("Invalid ancestorCount " + ancestorCount + " values should be greater or equal to 0");
        }

        return new RelativeNodePointer<N, NAME>(ancestorCount, hash, absent());
    }

    /**
     * Private ctor.
     */
    private RelativeNodePointer(final int ancestorCount, final boolean hash, final NodePointer<N, NAME> pointer) {
        super(pointer);
        this.ancestorCount = ancestorCount;
        this.hash = hash;
    }

    @Override
    NodePointer<N, NAME> append(final NodePointer<N, NAME> pointer) {
        return new RelativeNodePointer<>(this.ancestorCount, this.hash, this.appendToNext(pointer));
    }

    @Override
    N nextNodeOrNull(final N node) {
        N next = node;

        for (int i = 0; i < this.ancestorCount; i++) {
            final Optional<N> parent = next.parent();
            if (!parent.isPresent()) {
                next = null;
                break;
            }
            next = parent.get();
        }

        return next;
    }

    final int ancestorCount;
    private final boolean hash;

    @Override
    public boolean isRelative() {
        return true;
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(this.ancestorCount);
    }

    @Override
    void lastToString(final StringBuilder b) {
        if (this.hash) {
            b.append('#');
        }
    }
}
