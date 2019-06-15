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

/**
 * Used to read patch operations using factories to create {@link Name} and {@link Node values} from portions of the json.
 */
final class JsonPatchNodePatchFromJsonFormat<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchFromJsonFormat {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> JsonPatchNodePatchFromJsonFormat with(final Function<String, NAME> nameFactory,
                                                                                                    final Function<JsonNode, N> valueFactory) {
        return new JsonPatchNodePatchFromJsonFormat<>(nameFactory, valueFactory);
    }

    private JsonPatchNodePatchFromJsonFormat(final Function<String, NAME> nameFactory,
                                             final Function<JsonNode, N> valueFactory) {
        super();
        this.nameFactory = nameFactory;
        this.valueFactory = valueFactory;
    }

    @Override
    void accept(final NodePatchFromJsonObjectNodePropertyVisitor visitor,
                final JsonObjectNode node) {
        visitor.acceptJsonPatch(node);
    }

    @Override
    Function<String, NAME> nameFactory(final NodePatchFromJsonObjectNodePropertyVisitor visitor) {
        return this.nameFactory;
    }

    @Override
    Node<?, ?, ?, ?> valueOrFail(final AddReplaceOrTestNodePatchFromJsonObjectNodePropertyVisitor visitor) {
        return this.valueFactory.apply(visitor.propertyOrFail(visitor.value, NodePatch.VALUE_PROPERTY));
    }

    private final Function<String, NAME> nameFactory;
    private final Function<JsonNode, N> valueFactory;

    @Override
    public String toString() {
        return this.nameFactory + " " + this.valueFactory;
    }
}
