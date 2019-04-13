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

package walkingkooka.tree.patch;

import walkingkooka.tree.Node;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;

/**
 * Represents an remove operation within a patch.
 */
final class RemoveNodePatch<N extends Node<N, ?, ?, ?>> extends NonEmptyNodePatch<N> {

    static <N extends Node<N, ?, ?, ?>> RemoveNodePatch<N> with(final NodePointer<N, ?> path) {
        checkPath(path);

        return new RemoveNodePatch<>(path, null);
    }

    private RemoveNodePatch(final NodePointer<N, ?> path,
                            final NodePatch<N> next) {
        super(path, next);
    }

    @Override
    NodePatch<N> append0(final NodePatch<N> next) {
        return new RemoveNodePatch<>(this.path, next);
    }

    @Override
    final N apply1(final N node, final NodePointer<N, ?> start) {
        return this.remove0(node, this.path, start);
    }

    // Object........................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof RemoveNodePatch;
    }

    @Override
    boolean equals1(final NonEmptyNodePatch<?> other) {
        return this.path.equals(other.path);
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append("remove path=")
                .append(this.toString(this.path));
    }
}
