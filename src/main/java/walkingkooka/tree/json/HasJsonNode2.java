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
import walkingkooka.collect.set.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
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
            throw new UnsupportedTypeJsonNodeException("Type " + type + " not supported (factory or builtin)");
        }
        return registration;
    }

    /**
     * The type name for lists.
     */
    // @VisibleForTesting
    final static String LIST = List.class.getSimpleName();

    // @VisibleForTesting
    final static String SET = Set.class.getSimpleName();

    // @VisibleForTesting
    final static String MAP = Map.class.getSimpleName();

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

        LIST_REGISTRATION = register1(LIST, HasJsonNode2::fromJsonNodeWithTypeList);
        SET_REGISTRATION = register1(SET, HasJsonNode2::fromJsonNodeWithTypeSet);
        MAP_REGISTRATION = register1(MAP, HasJsonNode2::fromJsonNodeWithTypeMap);

        register0(Boolean.class, HasJsonNode2::fromJsonNodeBoolean);
        register0(Number.class, HasJsonNode2::fromJsonNodeNumber);
        register0(String.class, HasJsonNode2::fromJsonNodeString);
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
    static <T> void register0(final Class<T> type,
                              final Function<JsonNode, T> from) {
        register1(type.getName(), from);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static HasJsonNode2Registration register1(final String type,
                                              final Function<JsonNode, ?> from) {
        final HasJsonNode2Registration previous = TYPENAME_TO_FACTORY.get(type);
        if (null != previous) {
            throw new IllegalArgumentException("Type " + type + " factory already registered to " + previous);
        }

        final HasJsonNode2Registration registration = HasJsonNode2Registration.with(type, from);
        TYPENAME_TO_FACTORY.put(type, registration);
        return registration;
    }

    // fromJsonNode.........................................................................................................

    /**
     * Accepts a json array which holds a {@link List} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link HasJsonNode#toJsonNode(List)}.
     */
    static <T> List<T> fromJsonNodeList(final JsonNode node,
                                        final Class<T> elementType) {
        return fromJsonNodeCollection(node,
                elementType,
                Collectors.toList());
    }

    /**
     * Accepts a json array which holds a {@link List} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link HasJsonNode#toJsonNode(List)}.
     */
    static <T> Set<T> fromJsonNodeSet(final JsonNode node,
                                      final Class<T> elementType) {
        return fromJsonNodeCollection(node,
                elementType,
                Collectors.toCollection(Sets::ordered));
    }

    private static <C extends Collection<T>, T> C fromJsonNodeCollection(final JsonNode node,
                                                                         final Class<T> elementType,
                                                                         final Collector<T, ?, C> collector) {
        return node.isNull() ?
                null :
                fromJsonNodeCollection0(node, elementType, collector);
    }

    private static <C extends Collection<T>, T> C fromJsonNodeCollection0(final JsonNode node,
                                                                          final Class<T> elementType,
                                                                          final Collector<T, ?, C> collector) {
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
                .collect(collector);
    }

    static <K, V> Map<K, V> fromJsonNodeMap(final JsonNode node,
                                            final Class<K> keyType,
                                            final Class<V> valueType) {
        Objects.requireNonNull(keyType, "keyType");
        Objects.requireNonNull(valueType, "valueType");

        return node.isNull() ?
                null :
                fromJsonNodeMap0(node, keyType, valueType);
    }

    private static <K, V> Map<K, V> fromJsonNodeMap0(final JsonNode node,
                                                     final Class<K> keyType,
                                                     final Class<V> valueType) {
        Objects.requireNonNull(keyType, "keyType");
        Objects.requireNonNull(valueType, "valueType");

        JsonArrayNode array;
        try {
            array = node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        final Function<JsonNode, ?> keyFactory = registrationOrFail(keyType.getName())
                .from;
        final Function<JsonNode, ?> valueFactory = registrationOrFail(valueType.getName())
                .from;

        final Map<K, V> map = Maps.ordered();

        for (JsonNode entry : array.children()) {
            JsonObjectNode entryObject;
            try {
                entryObject = entry.objectOrFail();
            } catch (final JsonNodeException cause) {
                throw new IllegalArgumentException(cause.getMessage(), cause);
            }

            map.put(keyType.cast(keyFactory.apply(entryObject.getOrFail(ENTRY_KEY))),
                    valueType.cast(valueFactory.apply(entryObject.getOrFail(ENTRY_VALUE))));
        }

        return map;
    }

    private static Boolean fromJsonNodeBoolean(final JsonNode node) {
        return node.isNull() ?
                null :
                node.booleanValueOrFail();
    }

    private static Number fromJsonNodeNumber(final JsonNode node) {
        return node.isNull() ?
                null :
                node.numberValueOrFail();
    }

    private static String fromJsonNodeString(final JsonNode node) {
        return node.isNull() ?
                null :
                node.stringValueOrFail();
    }

    // fromJsonNodeWithType.........................................................................................................

    /**
     * Unwraps a wrapper holding a type and json form of a java instance.
     */
    static <T> T fromJsonNodeWithType(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return Cast.to(node.isNull() ?
                null :
                node.isBoolean() ?
                        node.booleanValueOrFail() :
                        node.isNumber() ?
                                node.numberValueOrFail() :
                                node.isString() ?
                                        node.stringValueOrFail() :
                                        fromJsonNodeWithType0(node));
    }

    /**
     * Contains the logic to examine the object type property and then locate and dispatch the factory which will
     * create an instance from the object in json form.
     */
    private static <T> T fromJsonNodeWithType0(final JsonNode node) {
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
        return fromJsonNodeWithTypeCollection(node, Collectors.toList());
    }

    /**
     * Expects a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    // @VisibleForTesting
    static <T> Set<T> fromJsonNodeWithTypeSet(final JsonNode node) {
        return fromJsonNodeWithTypeCollection(node, Collectors.toCollection(Sets::ordered));
    }

    private static <C extends Collection<T>, T> C fromJsonNodeWithTypeCollection(final JsonNode node,
                                                                                 final Collector<T, ?, C> collector) {
        return node.isNull() ?
                null :
                fromJsonNodeWithTypeCollection0(node, collector);
    }

    private static <C extends Collection<T>, T> C fromJsonNodeWithTypeCollection0(final JsonNode node,
                                                                                  final Collector<T, ?, C> collector) {
        Objects.requireNonNull(node, "node");

        // container must be an array
        JsonArrayNode array;
        try {
            array = node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        return array.children()
                .stream()
                .map(n -> Cast.<T>to(fromJsonNodeWithType(n)))
                .collect(collector);
    }


    /**
     * Expects a {@link JsonArrayNode} holding entries of the {@link Map} tagged with type and values.
     */
    // @VisibleForTesting
    static <K, V> Map<K, V> fromJsonNodeWithTypeMap(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return node.isNull() ?
                null :
                fromJsonNodeWithTypeMap0(node);
    }

    /**
     * Accepts an array of entry objects each holding the key and value.
     */
    private static <K, V> Map<K, V> fromJsonNodeWithTypeMap0(final JsonNode node) {

        // container must be an array
        JsonArrayNode array;
        try {
            array = node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        final Map<K, V> map = Maps.ordered();

        for (JsonNode child : array.children()) {
            JsonObjectNode childObject;
            try {
                childObject = child.objectOrFail();
            } catch (final JsonNodeException cause) {
                throw new IllegalArgumentException(cause.getMessage(), cause);
            }

            map.put(fromJsonNodeWithType(childObject.getOrFail(ENTRY_KEY)),
                    fromJsonNodeWithType(childObject.getOrFail(ENTRY_VALUE)));
        }

        return map;
    }

    final static JsonNodeName ENTRY_KEY = JsonNodeName.with("key");
    final static JsonNodeName ENTRY_VALUE = JsonNodeName.with("value");


    // toJsonNode.........................................................................................................

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()} or stored in its basic form.
     */
    static JsonNode toJsonNodeList(final List<?> list) {
        return toJsonNodeCollection(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()} or stored in its basic form.
     */
    static JsonNode toJsonNodeSet(final Set<?> set) {
        return toJsonNodeCollection(set);
    }

    private static JsonNode toJsonNodeCollection(final Collection<?> collection) {
        return null == collection ?
                JsonNode.nullNode() :
                JsonObjectNode.array()
                        .setChildren(collection.stream()
                                .map(JsonNode::wrapOrFail)
                                .collect(Collectors.toList()));
    }

    /**
     * Accepts a {@link Map} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()} or stored in its basic form.
     */
    static JsonNode toJsonNodeMap(final Map<?, ?> map) {
        return null == map ?
                JsonNode.nullNode() :
                JsonObjectNode.array()
                        .setChildren(map.entrySet().stream()
                                .map(HasJsonNode2::toJsonNodeMapEntry)
                                .collect(Collectors.toList()));
    }

    private static JsonNode toJsonNodeMapEntry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(ENTRY_KEY, JsonNode.wrapOrFail(entry.getKey()))
                .set(ENTRY_VALUE, JsonNode.wrapOrFail(entry.getValue()));
    }

    // toJsonNodeWithType.........................................................................................................

    /**
     * Accepts an {@link Object} including null and returns its {@link JsonNode} equivalent. If the type is not
     * a basic type with built in support, or {@link List} or {@link Set} or does not implement {@link HasJsonNode}
     * an {@link IllegalArgumentException} will be thrown.
     */
    static JsonNode toJsonNodeWithType(final Object object) {
        return null == object ?
                JsonNode.nullNode() :
                object instanceof List ?
                        toJsonNodeWithTypeList(Cast.to(object)) :
                        object instanceof Set ?
                                toJsonNodeWithTypeSet(Cast.to(object)) :
                                object instanceof Map ?
                                        toJsonNodeWithTypeMap(Cast.to(object)) :
                                        object instanceof HasJsonNode ?
                                                toJsonNodeWithTypeHasJsonNode(Cast.to(object)) :
                                                JsonNode.wrapOrFail(object);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeWithTypeList(final List<? extends Object> list) {
        return toJsonNodeWithTypeCollection(list, LIST_REGISTRATION);
    }

    private final static HasJsonNode2Registration LIST_REGISTRATION;

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeWithTypeSet(final Set<? extends Object> set) {
        return toJsonNodeWithTypeCollection(set, SET_REGISTRATION);
    }

    private static JsonNode toJsonNodeWithTypeCollection(final Collection<? extends Object> collection,
                                                         final HasJsonNode2Registration registration) {

        return null == collection ?
                JsonNode.nullNode() :
                registration.objectWithType()
                        .set(VALUE, JsonObjectNode.array().setChildren(collection.stream()
                                .map(HasJsonNode2::toJsonNodeWithType)
                                .collect(Collectors.toList())));
    }

    private final static HasJsonNode2Registration SET_REGISTRATION;

    /**
     * Accepts a {@link Map} and returns its {@link JsonNode} equivalent.
     */
    static JsonNode toJsonNodeWithTypeMap(final Map<?, ?> map) {
        return null == map ?
                JsonNode.nullNode() :
                toJsonNodeWithTypeMap0(map);
    }

    private static JsonNode toJsonNodeWithTypeMap0(final Map<?, ?> map) {
        return MAP_REGISTRATION.objectWithType()
                .setChildren(map.entrySet().stream()
                        .map(HasJsonNode2::toMapChildrenEntry)
                        .collect(Collectors.toList()));
    }

    private final static HasJsonNode2Registration MAP_REGISTRATION;

    private static JsonNode toMapChildrenEntry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(ENTRY_KEY, toJsonNodeWithType(entry.getKey()))
                .set(ENTRY_VALUE, toJsonNodeWithType(entry.getValue()));
    }

    /**
     * Serializes the node into json with a wrapper json object holding the type=json.
     * This helper is only called by {@link HasJsonNode#toJsonNodeWithType()}
     */
    static JsonNode toJsonNodeWithTypeHasJsonNode(final HasJsonNode has) {
        return null == has ?
                JsonNode.nullNode() :
                toJsonNodeWithTypeHasJsonNode0(has);
    }

    static JsonNode toJsonNodeWithTypeHasJsonNode0(final HasJsonNode has) {
        final Class<?> type = has.toJsonNodeType();
        if (!type.isInstance(has)) {
            throw new JsonNodeException("Type " + type.getName() + " is not compatible with " + has.getClass().getName());
        }

        return registrationOrFail(type.getName())
                .objectWithType()
                .set(VALUE, has.toJsonNode());
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
