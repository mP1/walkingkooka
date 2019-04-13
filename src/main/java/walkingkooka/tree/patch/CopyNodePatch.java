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
import walkingkooka.tree.pointer.NodePointerException;

/**
 * Represents a copy patch operation.
 */
final class CopyNodePatch<N extends Node<N, ?, ?, ?>> extends CopyOrMoveNodePatch<N> {

    static <N extends Node<N, ?, ?, ?>> CopyNodePatch<N> with(final NodePointer<N, ?> from,
                                                              final NodePointer<N, ?> path) {
        checkFromAndPath(from, path);

        return new CopyNodePatch<>(from, path, null);
    }

    private CopyNodePatch(final NodePointer<N, ?> from,
                          final NodePointer<N, ?> path,
                          final NodePatch<N> next) {
        super(from, path, next);
    }

    @Override
    NodePatch<N> append0(final NodePatch<N> patch) {
        return new CopyNodePatch<>(this.from, this.path, patch);
    }

    @Override
    N apply1(final N node, final NodePointer<N, ?> start) {
        final N copying = this.from.traverse(node)
                .orElseThrow(() -> new NodePointerException("Unable to navigate to find node to copy from: " + node));
        return this.add0(node, copying, start);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof CopyNodePatch;
    }

    @Override
    String operation() {
        return "copy";
    }
}
