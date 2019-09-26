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

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Optional;

/**
 * Handles transforming Optionals and the contained value to and from json.
 */
final class BasicJsonMarshallerTypedOptional extends BasicJsonMarshallerTyped<Optional<?>> {

    static BasicJsonMarshallerTypedOptional instance() {
        return new BasicJsonMarshallerTypedOptional();
    }

    /**
     * Private ctor use factory.
     */
    private BasicJsonMarshallerTypedOptional() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<Optional<?>> type() {
        return Cast.to(Optional.class);
    }

    @Override
    String typeName() {
        return "optional";
    }

    @Override
    Optional<?> fromJsonNodeNull(final FromJsonNodeContext context) {
        return null;
    }

    @Override
    Optional<?> fromJsonNodeNonNull(final JsonNode node,
                                    final FromJsonNodeContext context) {
        return fromJsonNodeNonNull0(node.arrayOrFail(), context);
    }

    private Optional<?> fromJsonNodeNonNull0(final JsonArrayNode array,
                                             final FromJsonNodeContext context) {
        final List<JsonNode> children = array.children();
        return children.isEmpty() ?
                Optional.empty() :
                this.fromJsonNodeNonNull1(children, array, context);
    }

    private Optional<?> fromJsonNodeNonNull1(final List<JsonNode> children,
                                             final JsonNode parent,
                                             final FromJsonNodeContext context) {
        if(children.size() > 1) {
            throw new FromJsonNodeException("Optional expected only 0/1 children but got " + children, parent);
        }

        return Optional.of(context.fromJsonNodeWithType(children.get(0)));
    }

    /**
     * Returns an array which will hold either a single value or nothing when the optional is empty.
     */
    @Override
    JsonNode toJsonNodeNonNull(final Optional<?> value,
                               final ToJsonNodeContext context) {
        return value.map(v -> this.toJsonNodeNonNullValue(v, context))
                .orElse(JsonNode.array());
    }

    private static JsonArrayNode toJsonNodeNonNullValue(final Object value,
                                                        final ToJsonNodeContext context) {
        return JsonNode.array().appendChild(context.toJsonNodeWithType(value));
    }
}
