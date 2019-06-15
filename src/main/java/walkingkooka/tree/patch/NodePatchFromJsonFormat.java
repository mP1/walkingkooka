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

import java.util.function.Function;

abstract class NodePatchFromJsonFormat {

    static NodePatchFromJsonFormat hasJsonNode() {
        return HasJsonNodeNodePatchFromJsonFormat.INSTANCE;
    }

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchFromJsonFormat jsonPatch(final Function<String, NAME> nameFactory,
                                                                                                final Function<JsonNode, N> valueFactory) {
        return JsonPatchNodePatchFromJsonFormat.with(nameFactory, valueFactory);
    }

    NodePatchFromJsonFormat() {
        super();
    }

    abstract void accept(final NodePatchFromJsonObjectNodePropertyVisitor visitor,
                         final JsonObjectNode node);

    abstract Function<String, ? extends Name> nameFactory(final NodePatchFromJsonObjectNodePropertyVisitor visitor);

    abstract Node<?, ?, ?, ?> valueOrFail(final AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor visitor);
}
