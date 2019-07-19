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

package walkingkooka.tree.json;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.type.ClassAttributes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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
     * Returns the mapper for the given {@link Class}.
     */
    static <T> HasJsonNodeMapper<T> mapperOrFail(final Class<T> type) {
        return mapperOrFail(type.getName());
    }

    /**
     * Returns the mapper for the given type name.
     */
    static <T> HasJsonNodeMapper<T> mapperOrFail(final String type) {
        final HasJsonNodeMapper<T> mapper = Cast.to(TYPENAME_TO_FACTORY.get(type));
        if (null == mapper) {
            throw new UnsupportedTypeJsonNodeException("Type " + CharSequences.quote(type) + " not supported, currently: " + TYPENAME_TO_FACTORY.keySet());
        }
        return mapper;
    }

    // register.........................................................................................................

    /*
     * To avoid race conditions register {@link JsonNode} here, all instance methods create the singleton
     * rather than referencing private static which would be null at this time because their super class
     * this is now running.
     */
    static {
        register0(HasJsonNodeBooleanMapper.instance(), Boolean.class);
        register0(HasJsonNodeByteMapper.instance(), Byte.class);
        register0(HasJsonNodeShortMapper.instance(), Short.class);
        register0(HasJsonNodeIntegerMapper.instance(), Integer.class);
        register0(HasJsonNodeLocaleMapper.instance(), Locale.class);
        register0(HasJsonNodeLongMapper.instance(), Long.class);
        register0(HasJsonNodeFloatMapper.instance(), Float.class);
        register0(HasJsonNodeDoubleMapper.instance(), Double.class);
        register0(HasJsonNodeStringMapper.instance(), String.class);
        register0(HasJsonNodeNumberMapper.instance(), Number.class);

        register0(HasJsonNodeCharacterMapper.instance(), Character.class);

        register0(HasJsonNodeBigDecimalMapper.instance(), BigDecimal.class);
        register0(HasJsonNodeBigIntegerMapper.instance(), BigInteger.class);

        register0(HasJsonNodeLocalDateMapper.instance(), LocalDate.class);
        register0(HasJsonNodeLocalDateTimeMapper.instance(), LocalDateTime.class);
        register0(HasJsonNodeLocalTimeMapper.instance(), LocalTime.class);

        register0(HasJsonNodeMathContextMapper.instance(), MathContext.class);

        registerListSetOrMap(HasJsonNodeListMapper.instance());
        registerListSetOrMap(HasJsonNodeSetMapper.instance());
        registerListSetOrMap(HasJsonNodeMapMapper.instance());

        registerJsonNode(JsonNode.class, "json-node");
        registerJsonNode(JsonBooleanNode.class, "json-boolean");
        registerJsonNode(JsonNullNode.class, "json-null");
        registerJsonNode(JsonNumberNode.class, "json-number");
        registerJsonNode(JsonStringNode.class, "json-string");
        registerJsonNode(JsonObjectNode.class, "json-object");
        registerJsonNode(JsonArrayNode.class, "json-array");

        register0(HasJsonNodeOptionalMapper.instance(), Optional.class);

        register("json-property-name", JsonNodeName::fromJsonNode, JsonNodeName.class);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static synchronized <T extends HasJsonNode> void register(final String typeName,
                                                              final Function<JsonNode, T> from,
                                                              final Class<T> type,
                                                              final Class<? extends T>... types) {
        CharSequences.failIfNullOrEmpty(typeName, "typeName");
        Objects.requireNonNull(from, "from");

        Class<?> first = type;
        List<Class<?>> all;

        if (0 == types.length) {
            all = Lists.of(type);
        } else {
            if (false == ClassAttributes.ABSTRACT.is(first)) {
                throw new IllegalArgumentException("Class " + CharSequences.quoteAndEscape(first.getName()) + " must be abstract");
            }
            final String notSubclasses = Arrays.stream(types)
                    .filter(t -> !first.isAssignableFrom(t))
                    .map(t -> CharSequences.quoteAndEscape(t.getName()))
                    .collect(Collectors.joining(", "));
            if (!notSubclasses.isEmpty()) {
                throw new IllegalArgumentException("Several classes " + notSubclasses + " are not sub classes of " + CharSequences.quoteAndEscape(first.getName()));
            }
            all = Lists.array();
            all.add(type);
            Arrays.stream(types).forEach(all::add);
        }

        register1(typeName,
                HasJsonNodeHasJsonNodeMapper.with(typeName, from, Cast.to(first)),
                all);
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
    static <T extends JsonNode> void registerJsonNode(final Class<T> type, final String typeName) {
        final HasJsonNodeJsonNodeMapper<T> mapper = HasJsonNodeJsonNodeMapper.with(type, typeName);
        register1(mapper.typeName().value(),
                mapper,
                Lists.of(type));
    }

    /**
     * Registers a {@link HasJsonNodeMapper} for a type name and given types.
     */
    private static <T> void register1(final String typeName,
                                      final HasJsonNodeMapper<T> mapper,
                                      final List<Class<?>> types) {
        register2(typeName, mapper);

        for (Class<?> type : types) {
            final String name = type.getName();
            if (!name.equals(typeName)) {
                register2(name, mapper);
            }
        }
    }

    /**
     * Registers a mapper for a type name.
     */
    private static <T> void register2(final String typeName, final HasJsonNodeMapper<T> mapper) {
        final HasJsonNodeMapper<?> previous = TYPENAME_TO_FACTORY.get(typeName);
        if (null != previous) {
            throw new IllegalArgumentException("Type " + CharSequences.quote(typeName) + " already registered to " + CharSequences.quoteAndEscape(previous.typeName().value) + " all=" + TYPENAME_TO_FACTORY.keySet());
        }

        TYPENAME_TO_FACTORY.put(typeName, mapper);
    }

    /**
     * Returns the {@link Class} for the given type name.
     */
    static Optional<Class<?>> registeredType(final JsonStringNode name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(TYPENAME_TO_FACTORY.get(name.value())).map(m -> m.type());
    }

    /**
     * Returns the type name identifying the given {@link Class} providing it is registered.
     */
    static Optional<JsonStringNode> typeName(final Class<?> type) {
        Objects.requireNonNull(type, "type");

        return Optional.ofNullable(TYPENAME_TO_FACTORY.get(type.getName())).map(m -> m.typeName());
    }

    // fromJsonNodeWithType.........................................................................................................

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
                                        node.fromJsonNodeWithType());
    }

    // toJsonNode.........................................................................................................

    /**
     * Accepts an {@link Object} and creates a {@link JsonNode} equivalent.
     */
    static JsonNode toJsonNodeObject(final Object object) {
        return null == object ?
                JsonNode.nullNode() :
                mapperOrFail(object.getClass().getName())
                        .toJsonNode(object);
    }

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
                                .map(HasJsonNodeMapper::toJsonNodeObject)
                                .collect(Collectors.toList()));
    }

    /**
     * Accepts a {@link Map} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()} or stored in its basic form.
     */
    static JsonNode toJsonNodeMap(final Map<?, ?> map) {
        return null == map ?
                JsonNode.nullNode() :
                toJsonNodeMapNotNull(map);
    }

    // HasJsonNodeMapMapper
    static JsonNode toJsonNodeMapNotNull(final Map<?, ?> map) {
        return toJsonNodeMapNotNull(map, HasJsonNodeMapper::toJsonNodeMapEntry);
    }

    static JsonNode toJsonNodeMapNotNull(final Map<?, ?> map,
                                         final Function<Entry<?, ?>, JsonNode> entry) {
        return JsonObjectNode.array()
                .setChildren(map.entrySet().stream()
                        .map(entry)
                        .collect(Collectors.toList()));
    }

    private static JsonNode toJsonNodeMapEntry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(ENTRY_KEY, toJsonNodeObject(entry.getKey()))
                .set(ENTRY_VALUE, toJsonNodeObject(entry.getValue()));
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
    static JsonNode toJsonNodeWithTypeObject(final Object value) {
        return null == value ?
                JsonNode.nullNode() :
                value instanceof List ?
                        toJsonNodeWithTypeObject1("list", Cast.to(value)) :
                        value instanceof Set ?
                                toJsonNodeWithTypeObject1("set", Cast.to(value)) :
                                value instanceof Map ?
                                        toJsonNodeWithTypeObject1("map", Cast.to(value)) :
                                        toJsonNodeWithTypeObject0(value);
    }

    /**
     * Locates the mapper for the given value using its class.
     */
    private static JsonNode toJsonNodeWithTypeObject0(final Object value) {
        return toJsonNodeWithTypeObject1(value.getClass().getName(), value);
    }

    /**
     * Locates the mapper for the given value using its class.
     */
    private static JsonNode toJsonNodeWithTypeObject1(final String typeName, final Object value) {
        return mapperOrFail(typeName).toJsonNodeWithType(value);
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

        return mapperOrFail(type.getName()).toJsonNodeWithTypeNonNull(has);
    }

    // instance...................................................................................................

    /**
     * Package private to limit sub classing.
     */
    HasJsonNodeMapper() {
        super();
    }

    /**
     * Returns the primary type ignoring others.
     */
    abstract Class<T> type();

    /**
     * Returns the value from its {@link JsonNode} representation.
     */
    final T fromJsonNode(final JsonNode node) {
        try {
            return node.isNull() ?
                    this.fromJsonNodeNull() :
                    this.fromJsonNodeNonNull(node);
        } catch (final FromJsonNodeException | NullPointerException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new FromJsonNodeException(cause.getMessage(), node, cause);
        }
    }

    abstract T fromJsonNodeNull();

    abstract T fromJsonNodeNonNull(final JsonNode node);

    /**
     * Creates the {@link JsonNode} with the type representation of the given value.
     */
    final JsonNode toJsonNodeWithType(final T value) {
        return null == value ?
                JsonNode.nullNode() :
                this.toJsonNodeWithTypeNonNull(value);
    }

    abstract JsonNode toJsonNodeWithTypeNonNull(final T value);

    abstract JsonStringNode typeName();

    /**
     * Creates the {@link JsonNode} representation of the given value without any type/value enclosing object.
     */
    final JsonNode toJsonNode(final T value) {
        return null == value ?
                JsonNode.nullNode() :
                this.toJsonNodeNonNull(value);
    }

    abstract JsonNode toJsonNodeNonNull(final T value);

    @Override
    public final String toString() {
        return this.typeName().value();
    }
}
