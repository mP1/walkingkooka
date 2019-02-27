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

import walkingkooka.Cast;
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

    private static HasJsonNode2Registration registrationOrFail(final String type) {
        final HasJsonNode2Registration registration = TYPENAME_TO_FACTORY.get(type);
        if (null == registration) {
            throw new IllegalArgumentException("Type " + type + " factory not registered.");
        }
        return registration;
    }

    /**
     * The type name for lists.
     */
    // @VisibleForTesting
    final static String LIST = "List";

    // register.........................................................................................................

    /**
     * To avoid race conditions register {@link JsonNode} here.
     */
    static {
        register0(JsonBooleanNode.class, JsonBooleanNode::fromJsonNode0);
        register0(JsonNullNode.class, JsonNullNode::fromJsonNode0);
        register0(JsonNumberNode.class, JsonNumberNode::fromJsonNode0);
        register0(JsonStringNode.class, JsonStringNode::fromJsonNode0);
        register0(JsonArrayNode.class, JsonArrayNode::fromJsonNode0);
        register0(JsonObjectNode.class, JsonObjectNode::fromJsonNode0);

        register1(LIST, HasJsonNode2::fromJsonNodeWithTypeList);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static synchronized <T extends HasJsonNode> void register(final Class<T> type,
                                                              final Function<JsonNode, T> from) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(from, "from");

        register0(type, from);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static <T extends HasJsonNode> void register0(final Class<T> type,
                                                  final Function<JsonNode, T> from) {
        register1(type.getName(), from);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static void register1(final String type,
                          final Function<JsonNode, ?> from) {
        HasJsonNode2Registration previous = TYPENAME_TO_FACTORY.get(type);
        if(null!=previous) {
            throw new IllegalArgumentException("Type " + type + " factory already registered to " + previous);
        }
        TYPENAME_TO_FACTORY.put(type, HasJsonNode2Registration.with(type, from));
    }

    // fromJsonNode.........................................................................................................

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
        final Function<JsonNode, ?> factory = registrationOrFail(elementType.getName())
                .from;
        return array.children()
                .stream()
                .map(n -> elementType.cast(factory.apply(n)))
                .collect(Collectors.toList());
    }

    /**
     * Unwraps a wrapper holding a type and json form of a java instance.
     */
    static <T> T fromJsonNodeWithType(final JsonNode node) {
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

        return Cast.to(registrationOrFail(type)
                .from.apply(object.getOrFail(VALUE)));
    }

    // HasJsonNode2Registration
    final static JsonNodeName TYPE = JsonNodeName.with("type");

    /**
     * Expects a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    // @VisibleForTesting
    static <T> List<T> fromJsonNodeWithTypeList(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        // container must be an array
        JsonArrayNode array;
        try {
            array = node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        return Cast.to(array.children()
                .stream()
                .map(HasJsonNode2::fromJsonNodeWithType)
                .collect(Collectors.toList()));
    }

    // toJsonNode.........................................................................................................

    /**
     * Accepts a list of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final List<? extends HasJsonNode> list) {
        Objects.requireNonNull(list, "list");

        return JsonObjectNode.array()
                .setChildren(list.stream()
                .map(e -> ((HasJsonNode) e).toJsonNode())
                .collect(Collectors.toList()));
    }

    /**
     * Accepts a list of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeWithType(final List<? extends HasJsonNode> list) {
        Objects.requireNonNull(list, "list");

        return registrationOrFail(LIST)
                .objectWithType()
                .set(VALUE, JsonObjectNode.array().setChildren(list.stream()
                        .map(e -> ((HasJsonNode) e).toJsonNodeWithType())
                        .collect(Collectors.toList())));
    }

    /**
     * Serializes the node into json with a wrapper json object holding the type=json.
     */
    static JsonNode toJsonNode(final HasJsonNode has) {
        final Class<?> type = has.toJsonNodeType();
        if (!type.isInstance(has)) {
            throw new JsonNodeException("Type " + type.getName() + " is not compatible with " + has.getClass().getName());
        }

        return registrationOrFail(type.getName())
                .objectWithType().set(VALUE, has.toJsonNode());
    }

    // @VisibleForTesting
    final static JsonNodeName VALUE = JsonNodeName.with("value");

    /**
     * Stop creation
     */
    private HasJsonNode2() {
        throw new UnsupportedOperationException();
    }
}
