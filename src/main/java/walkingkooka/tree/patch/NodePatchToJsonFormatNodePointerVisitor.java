/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.patch;

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.pointer.NamedChildNodePointer;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.pointer.NodePointerVisitor;
import walkingkooka.tree.visit.Visiting;

import java.util.Optional;

/**
 * Walks a {@link NodePointerVisitor} returning a {@link JsonStringNode} holding the path {@link Name} type if one is found.
 */
final class NodePatchToJsonFormatNodePointerVisitor<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointerVisitor<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> Optional<JsonStringNode> pathNameType(final NodePointer<N, NAME> path) {
        final NodePatchToJsonFormatNodePointerVisitor<N, NAME> visitor = new NodePatchToJsonFormatNodePointerVisitor<>();
        visitor.accept(path);
        return visitor.pathNameType;
    }

    // VisibleForTesting
    NodePatchToJsonFormatNodePointerVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final NamedChildNodePointer<N, NAME> node) {
        this.pathNameType = HasJsonNode.typeName(node.name().getClass());
        return Visiting.SKIP;
    }

    // VisibleForTesting
    Optional<JsonStringNode> pathNameType = Optional.empty();

    @Override
    public String toString() {
        return this.pathNameType.map(p -> p.toString())
                .orElse("");
    }
}
