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
import walkingkooka.text.CharSequences;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 * A mapper that converts values to {@link JsonNode} and back.
 */
abstract class HasJsonNodeMapper<T> {

    /**
     * All factory registations for all {@link HasJsonNode}.
     */
    // @VisibleForTesting
    final static Map<String, HasJsonNodeMapper<?>> TYPENAME_TO_FACTORY = Maps.ordered();

    /**
     * Returns the mapper for the given type name.
     */
    private static <T> HasJsonNodeMapper<T> mapperOrFail(final String type) {
        final HasJsonNodeMapper<T> mapper = Cast.to(TYPENAME_TO_FACTORY.get(type));
        if (null == mapper) {
            throw new UnsupportedTypeJsonNodeException("Type " + CharSequences.quote(type) + " not supported, currently: " + TYPENAME_TO_FACTORY.keySet());
        }
        return mapper;
    }

    // register.........................................................................................................

    /**
     * To avoid race conditions register {@link JsonNode} here, all instance methods create the singleton
     * rather than referencing private static which would be null at this time because their super class
     * this is now running.
     */
    static {
        register0(HasJsonNodeBooleanMapper.instance(), Boolean.class);
        register0(HasJsonNodeByteMapper.instance(), Byte.class);
        register0(HasJsonNodeShortMapper.instance(), Short.class);
        register0(HasJsonNodeIntegerMapper.instance(), Integer.class);
        register0(HasJsonNodeLongMapper.instance(), Long.class);
        register0(HasJsonNodeFloatMapper.instance(), Float.class);
        register0(HasJsonNodeDoubleMapper.instance(), Double.class);
        register0(HasJsonNodeStringMapper.instance(), String.class);

        register0(HasJsonNodeBigDecimalMapper.instance(), BigDecimal.class);
        register0(HasJsonNodeBigIntegerMapper.instance(), BigInteger.class);

        register0(HasJsonNodeNumberMapper.instance(), Number.class);

        registerListSetOrMap(HasJsonNodeListMapper.instance());
        registerListSetOrMap(HasJsonNodeSetMapper.instance());
        registerListSetOrMap(HasJsonNodeMapMapper.instance());

        registerJsonNode(JsonNode.class);
        registerJsonNode(JsonBooleanNode.class);
        registerJsonNode(JsonNullNode.class);
        registerJsonNode(JsonNumberNode.class);
        registerJsonNode(JsonStringNode.class);
        registerJsonNode(JsonObjectNode.class);
        registerJsonNode(JsonArrayNode.class);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static synchronized <T extends HasJsonNode> void register(final String typeName,
                                                              final Function<JsonNode, T> from,
                                                              final Class<?>...types) {
        CharSequences.failIfNullOrEmpty(typeName, "typeName");
        Objects.requireNonNull(from, "from");

        register1(typeName, HasJsonNodeHasJsonNodeMapper.with(typeName, from), types);
    }

    /**
     * Registers a {@link HasJsonNodeMapper} using both its type and type name which will be different.
     */
    static <T> void register0(final HasJsonNodeMapper<T> mapper, final Class<?> type) {
        register2(mapper.typeName().value(), mapper);
        register2(type.getName(), mapper);
    }

    /**
     * Registers a {@link HasJsonNodeMapper} using only its type name.
     */
    static <T> void registerListSetOrMap(final HasJsonNodeMapper<T> mapper) {
        register2(mapper.typeName().value(), mapper);
    }

    /**
     * Registers a {@link HasJsonNodeMapper} using only its type name.
     */
    static <T extends JsonNode> void registerJsonNode(final Class<T> type) {
        final HasJsonNodeJsonNodeMapper mapper = HasJsonNodeJsonNodeMapper.with(type);
        register1(mapper.typeName().value(), mapper, type);
    }

    /**
     * Registers a mapper for a type name.
     */
    private static <T> void register1(final String typeName, final HasJsonNodeMapper<T> mapper, final Class<?>...types) {
        register2(typeName, mapper);

        for(Class<?> type : types) {
            final String name = type.getName();
            if(!name.equals(typeName)) {
                register2(name, mapper);
            }
        }
    }

    /**
     * Registers a mapper for a type name.
     */
    private static <T> void register2(final String type, final HasJsonNodeMapper<T> mapper) {
        final HasJsonNodeMapper<?> previous = TYPENAME_TO_FACTORY.get(type);
        if (null != previous) {
            throw new IllegalArgumentException("Type " + CharSequences.quote(type) + " already registered to " + previous + " all=" + TYPENAME_TO_FACTORY.keySet());
        }

        TYPENAME_TO_FACTORY.put(type, mapper);
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
        final HasJsonNodeMapper<T> mapper = mapperOrFail(elementType.getName());
        return array(node).children()
                .stream()
                .map(n -> mapper.fromJsonNode(n))
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

        final HasJsonNodeMapper<K> keyMapper = mapperOrFail(keyType.getName());
        final HasJsonNodeMapper<V> valueMapper = mapperOrFail(valueType.getName());

        final Map<K, V> map = Maps.ordered();

        for (JsonNode entry : array(node).children()) {
            JsonObjectNode entryObject;
            try {
                entryObject = entry.objectOrFail();
            } catch (final JsonNodeException cause) {
                throw new IllegalArgumentException(cause.getMessage(), cause);
            }

            map.put(keyMapper.fromJsonNode(entryObject.getOrFail(ENTRY_KEY)),
                    valueMapper.fromJsonNode(entryObject.getOrFail(ENTRY_VALUE)));
        }

        return map;
    }

    private static JsonArrayNode array(final JsonNode node) {
        try {
            return node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    // fromJsonNodeWithType.........................................................................................................

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

    /**
     * Unwraps a wrapper holding a type and json form of a java instance.
     */
    static <T> T fromJsonNodeWithType(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return node.isNull() ?
                null :
                Cast.to(node.isBoolean() ?
                        node.booleanValueOrFail() :
                        node.isNumber() ?
                                node.numberValueOrFail() :
                                node.isString() ?
                                        node.stringValueOrFail() :
                                        fromJsonNodeWithType0(node));
    }

    /**
     * Contains the logic to examine the object type property and then locate and dispatch the mapper which will
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

        return Cast.to(mapperOrFail(type).fromJsonNode(object.getOrFail(VALUE)));
    }

    // HasJsonNodeMapper
    final static JsonNodeName TYPE = JsonNodeName.with("type");

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
                                .map(HasJsonNodeMapper::toJsonNodeMapEntry)
                                .collect(Collectors.toList()));
    }

    private static JsonNode toJsonNodeMapEntry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(ENTRY_KEY, JsonNode.wrapOrFail(entry.getKey()))
                .set(ENTRY_VALUE, JsonNode.wrapOrFail(entry.getValue()));
    }


    static JsonNode toJsonNodeWithTypeMapEntry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(ENTRY_KEY, toJsonNodeWithType(entry.getKey()))
                .set(ENTRY_VALUE, toJsonNodeWithType(entry.getValue()));
    }

    final static JsonNodeName ENTRY_KEY = JsonNodeName.with("key");
    final static JsonNodeName ENTRY_VALUE = JsonNodeName.with("value");

    // toJsonNodeWithType.........................................................................................................

    /**
     * Accepts a {@link List} and creates a {@link JsonArrayNode} with elements converted to json with types.
     */
    static JsonNode toJsonNodeWithTypeList(final List<?> list) {
        return HasJsonNodeListMapper.toJsonNodeWithTypeList0(list);
    }

    /**
     * Accepts a {@link Set} and creates a {@link JsonArrayNode} with elements converted to json with types.
     */
    static JsonNode toJsonNodeWithTypeSet(final Set<?> set) {
        return HasJsonNodeSetMapper.toJsonNodeWithTypeSet0(set);
    }

    /**
     * Accepts a {@link Map} and creates a {@link JsonArrayNode} with entries converted to json with types.
     */
    static JsonNode toJsonNodeWithTypeMap(final Map<?, ?> map) {
        return HasJsonNodeMapMapper.toJsonNodeWithTypeMap0(map);
    }

    // toJsonNodeWithType.........................................................................................................

    /**
     * Accepts an {@link Object} including null and returns its {@link JsonNode} equivalent. If the type is not
     * a basic type with built in support, or {@link List} or {@link Set} or does not implement {@link HasJsonNode}
     * an {@link IllegalArgumentException} will be thrown.
     */
    static JsonNode toJsonNodeWithType(final Object value) {
        return null == value ?
                JsonNode.nullNode() :
                value instanceof List ?
                        toJsonNodeWithType0("list", Cast.to(value)) :
                        value instanceof Set ?
                                toJsonNodeWithType0("set", Cast.to(value)) :
                                value instanceof Map ?
                                        toJsonNodeWithType0("map", Cast.to(value)) :
                                        toJsonNodeWithType0(value);
    }

    /**
     * Locates the mapper for the given value using its class.
     */
    private static JsonNode toJsonNodeWithType0(final Object value) {
        return toJsonNodeWithType0(value.getClass().getName(), value);
    }

    /**
     * Locates the mapper for the given value using its class.
     */
    private static JsonNode toJsonNodeWithType0(final String typeName, final Object value) {
        return mapperOrFail(typeName).toJsonNode(value);
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

    private static JsonNode toJsonNodeWithTypeHasJsonNode0(final HasJsonNode has) {
        final Class<?> type = has.getClass();
        if (!type.isInstance(has)) {
            throw new JsonNodeException("Type " + type.getName() + " is not compatible with " + has.getClass().getName());
        }

        return mapperOrFail(type.getName()).toJsonNode0(has);
    }

    // @VisibleForTesting
    final static JsonNodeName VALUE = JsonNodeName.with("value");


    HasJsonNodeMapper() {
        super();
    }

    /**
     * Returns the value from its {@link JsonNode} representation.
     */
    final T fromJsonNode(final JsonNode node) {
        try {
            return node.isNull() ?
                    null :
                    this.fromJsonNode0(node);
        } catch (final NullPointerException | JsonNodeException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            final String message = cause.getMessage();
            throw new JsonNodeException(CharSequences.isNullOrEmpty(message) ?
                    node.toString() :
                    message, cause);
        }
    }

    abstract T fromJsonNode0(final JsonNode node);

    /**
     * Creates the {@link JsonNode} representation of the given value.
     */
    final JsonNode toJsonNode(final T value) {
        return null == value ?
                JsonNode.nullNode() :
                this.toJsonNode0(value);
    }

    abstract JsonNode toJsonNode0(final T value);

    abstract JsonStringNode typeName();

    @Override
    public final String toString() {
        return this.typeName().value();
    }
}
