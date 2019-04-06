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
 * Base class for both copy and move operations.
 */
abstract class CopyOrMoveNodePatch<N extends Node<N, ?, ?, ?>> extends NonEmptyNodePatch<N> {

    static <N extends Node<N, ?, ?, ?>> void checkFromAndPath(final NodePointer<N, ?> from,
                                                              final NodePointer<N, ?> path) {
        Objects.requireNonNull(from, "from");
        checkPath(path);

        if (from.equals(path)) {
            throw new IllegalArgumentException("From and to path are equal=" + from);
        }
    }

    CopyOrMoveNodePatch(final NodePointer<N, ?> from,
                        final NodePointer<N, ?> path,
                        final NodePatch<N> next) {
        super(path, next);
        this.from = from;
    }

    final NodePointer<N, ?> from;

    @Override
    final void toString0(final StringBuilder b) {
        b.append(this.operation())
                .append(" from=")
                .append(this.toString(this.from))
                .append(" path=")
                .append(this.toString(this.path));
    }

    /**
     * Should return either "copy" or "move".
     */
    abstract String operation();
}
