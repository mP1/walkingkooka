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

package walkingkooka.tree.json.marshall;

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonBooleanNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNullNode;
import walkingkooka.tree.json.JsonNumberNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;

/**
 * A {@link BasicJsonMarshallerTyped} that handles {@link JsonNode}.
 */
final class BasicJsonMarshallerTypedJsonNode extends BasicJsonMarshallerTyped<JsonNode> {

    static BasicJsonMarshallerTypedJsonNode instance() {
        return new BasicJsonMarshallerTypedJsonNode();
    }

    private BasicJsonMarshallerTypedJsonNode() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
        this.registerTypes(Lists.of(JsonArrayNode.class,
                JsonBooleanNode.class,
                JsonNullNode.class,
                JsonNumberNode.class,
                JsonObjectNode.class,
                JsonStringNode.class));

    }

    @Override
    Class<JsonNode> type() {
        return JsonNode.class;
    }

    @Override
    String typeName() {
        return "json";
    }

    @Override
    JsonNode unmarshallNull(final JsonNodeUnmarshallContext context) {
        return JsonNode.nullNode();
    }

    @Override
    JsonNode unmarshallNonNull(final JsonNode node,
                                 final JsonNodeUnmarshallContext context) {
        return node.removeParent();
    }

    @Override
    JsonNode toJsonNodeNonNull(final JsonNode value,
                               final ToJsonNodeContext context) {
        return value.removeParent();
    }
}
