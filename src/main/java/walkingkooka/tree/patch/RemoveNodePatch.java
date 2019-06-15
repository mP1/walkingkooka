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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;

/**
 * Represents an REMOVE operation within a patch.
 */
final class RemoveNodePatch<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NonEmptyNodePatch<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> RemoveNodePatch<N, NAME> with(final NodePointer<N, NAME> path) {
        checkPath(path);

        return new RemoveNodePatch<>(path, null);
    }

    private RemoveNodePatch(final NodePointer<N, NAME> path,
                            final NonEmptyNodePatch<N, NAME> next) {
        super(path, next);
    }

    @Override
    RemoveNodePatch<N, NAME> append0(final NonEmptyNodePatch<N, NAME> next) {
        return new RemoveNodePatch<>(this.path, next);
    }

    @Override
    final N apply1(final N node, final NodePointer<N, NAME> start) {
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
    boolean equals1(final NonEmptyNodePatch<?, ?> other) {
        return this.path.equals(other.path);
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(REMOVE + " path=")
                .append(toString(this.path));
    }

    // HasJsonNode...............................................................................

    private final static JsonObjectNode JSON_OBJECT_WITH_OPERATION = JsonNode.object()
            .set(OP_PROPERTY, JsonNode.string(REMOVE));

    @Override
    JsonObjectNode jsonObjectWithOp() {
        return JSON_OBJECT_WITH_OPERATION;
    }

    /**
     * <pre>
     * {
     *     "op": "add",
     *     "path-name-type": "json-property-name",
     *     "path": "/1/2/abc"
     * }
     * </pre>
     */
    @Override
    JsonObjectNode toJsonNode1(final JsonObjectNode object,
                               final NodePatchToJsonFormat format) {
        return this.setPath(format.setPathNameType(object, this.path));
    }
}
