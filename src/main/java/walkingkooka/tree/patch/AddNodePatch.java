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

/**
 * Represents an add operation within a patch.
 */
final class AddNodePatch<N extends Node<N, ?, ?, ?>> extends AddReplaceOrTestNodePatch<N> {

    static <N extends Node<N, ?, ?, ?>> AddNodePatch<N> with(final NodePointer<N, ?> path,
                                                             final N value) {
        checkPath(path);
        checkValue(value);

        return new AddNodePatch<>(path, value, null);
    }

    private AddNodePatch(final NodePointer<N, ?> pointer,
                         final N value,
                         final NodePatch<N> next) {
        super(pointer, value, next);
    }

    @Override
    NodePatch<N> append0(final NodePatch<N> next) {
        return new AddNodePatch<>(this.path,
                this.value,
                next);
    }

    @Override
    final N apply1(final N node, final NodePointer<N, ?> start) {
        return this.add0(node, this.value, start);
    }

    @Override
    String operation() {
        return "add";
    }
}
