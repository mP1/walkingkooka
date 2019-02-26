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

package walkingkooka.tree.json;

import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Holds non public internal methods and state for {@link HasJsonNode}.
 */
final class HasJsonNode2 {

    /**
     * All factory registations for all {@link HasJsonNode}.
     */
    // @VisibleForTesting
    final static Map<String, HasJsonNode2Registration> TYPENAME_TO_FACTORY = Maps.ordered();

    /**
     * To avoid race conditions register {@link JsonNode} here.
     */
    static {
        HasJsonNode.register(JsonBooleanNode.class, JsonBooleanNode::fromJsonNode0);
        HasJsonNode.register(JsonNullNode.class, JsonNullNode::fromJsonNode0);
        HasJsonNode.register(JsonNumberNode.class, JsonNumberNode::fromJsonNode0);
        HasJsonNode.register(JsonStringNode.class, JsonStringNode::fromJsonNode0);
        HasJsonNode.register(JsonArrayNode.class, JsonArrayNode::fromJsonNode0);
        HasJsonNode.register(JsonObjectNode.class, JsonObjectNode::fromJsonNode0);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static synchronized <T extends HasJsonNode> void register(final Class<T> type,
                                                              final Function<JsonNode, T> from) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(from, "from");

        final String name = type.getName();
        HasJsonNode2Registration previous = TYPENAME_TO_FACTORY.get(name);
        if(null!=previous) {
            throw new IllegalArgumentException("Type " + type + " factory already registered to " + previous);
        }
        TYPENAME_TO_FACTORY.put(name, HasJsonNode2Registration.with(from, type));
    }

    /**
     * Accepts a json array which holds a {@link List} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link HasJsonNode#toJsonNode(List)}.
     */
    static <T> List<T> fromJsonNode(final JsonNode node, final Class<T> elementType) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(elementType, "elementType");

        JsonArrayNode array;
        try {
            array = node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
        final Function<JsonNode, ? extends HasJsonNode> factory = registrationOrFail(elementType.getName())
                .from;
        return array.children()
                .stream()
                .map(n -> elementType.cast(factory.apply(n)))
                .collect(Collectors.toList());
    }

    /**
     * Unwraps a wrapper holding a type and json form of a java instance.
     */
    static HasJsonNode fromJsonNodeWithType(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        JsonObjectNode object;
        try {
            object = node.objectOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        String type;
        try {
            type = object.getOrFail(TYPE).stringValueOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        return registrationOrFail(type)
                .from.apply(object.getOrFail(VALUE));
    }

    final static JsonNodeName TYPE = JsonNodeName.with("type");

    static HasJsonNode2Registration registrationOrFail(final String type) {
        final HasJsonNode2Registration registration = TYPENAME_TO_FACTORY.get(type);
        if(null==registration) {
            throw new IllegalArgumentException("Type " + type + " factory not registered.");
        }
        return registration;
    }

    /**
     * Accepts a list of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final List<? extends HasJsonNode> list) {
        Objects.requireNonNull(list, "list");

        return JsonObjectNode.array().setChildren(list.stream()
                .map(e -> ((HasJsonNode) e).toJsonNode())
                .collect(Collectors.toList()));
    }

    /**
     * Serializes the node into json with a wrapper json object holding the type=json.
     */
    static JsonNode toJsonNode(final HasJsonNode has) {
        final String name = has.getClass().getName();
        final HasJsonNode2Registration registration = TYPENAME_TO_FACTORY.get(name);
        if(null==registration) {
            throw new IllegalArgumentException("Type " + name + " not registered");
        }
        return registration.objectWithType().set(VALUE, has.toJsonNode());
    }

    private final static JsonNodeName VALUE = JsonNodeName.with("value");

    /**
     * Stop creation
     */
    private HasJsonNode2() {
        throw new UnsupportedOperationException();
    }
}
