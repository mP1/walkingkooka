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

/**
 * Represents a REPLACE operation within a patch.
 */
final class ReplaceNodePatch<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends AddReplaceOrTestNodePatch<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> ReplaceNodePatch<N, NAME> with(final NodePointer<N, NAME> path,
                                                                                             final N value) {
        checkPath(path);
        checkValue(value);

        return new ReplaceNodePatch<>(path, value, null);
    }

    private ReplaceNodePatch(final NodePointer<N, NAME> path,
                             final N value,
                             final NonEmptyNodePatch<N, NAME> next) {
        super(path, value, next);
    }

    @Override
    ReplaceNodePatch<N, NAME> append0(final NonEmptyNodePatch<N, NAME> next) {
        return new ReplaceNodePatch<>(this.path,
                this.value,
                next);
    }

    /**
     * <a href="http://jsonpatch.com/"></a>
     * <pre>
     * Replace
     * { "op": "replace", "path": "/biscuits/0/name", "value": "Chocolate Digestive" }
     * Replaces a value. Equivalent to a “remove” followed by an “add”.
     * </pre>
     */
    @Override
    final N apply1(final N node, final NodePointer<N, NAME> start) {
        return this.add0(this.remove0(node, this.path, start),
                this.value,
                start);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ReplaceNodePatch;
    }

    @Override
    String operation() {
        return REPLACE;
    }

    // HasJsonNode...............................................................................

    private final static JsonObjectNode JSON_OBJECT_WITH_OPERATION = JsonNode.object()
            .set(OP_PROPERTY, JsonNode.string(REPLACE));

    @Override
    JsonObjectNode jsonObjectWithOp() {
        return JSON_OBJECT_WITH_OPERATION;
    }
}
