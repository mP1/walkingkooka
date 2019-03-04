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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Interface implemented by objects that can be expressed or have a {@link JsonNode} equivalent.
 * All static methods from methods return a {@link JsonNullNode} if given a null, and all static to methods
 * return null when given a {@link JsonNullNode}.
 */
public interface HasJsonNode {

    /**
     * Shared function used to report a required property is missing within a static fromJsonNode.
     */
    static void requiredPropertyMissing(final JsonNodeName property,
                                        final JsonNode node) {
        throw new IllegalArgumentException("Required property " + property + " missing=" + node);
    }

    /**
     * Shared function used to report a required property is missing within a static fromJsonNode.
     */
    static void unknownPropertyPresent(final JsonNodeName property,
                                       final JsonNode node) {
        throw new IllegalArgumentException("Unknown property " + property + " present=" + node);
    }

    /**
     * Registers a factory that parses a {@link JsonNode} into a value for the given {@link Class}.
     */
    static <T extends HasJsonNode> void register(final String typeName,
                                                 final Function<JsonNode, T> from,
                                                 final Class<?>...types) {
        HasJsonNodeMapper.register(typeName, from, types);
    }

    // fromJsonNode.......................................................................................................

    /**
     * Accepts a {@link JsonNode} which does not hold a type and uses the {@link Class} parameter to dispatch the reading from json.
     * This is useful to perform the inverted operation after making json from a java object of a single type.
     */
    static <T> T fromJsonNode(final JsonNode node, final Class<T> type) {
        return HasJsonNodeMapper.fromJsonNode(node, type);
    }

    /**
     * Accepts a json array which holds a {@link List} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link #toJsonNode(List)}.
     */
    static <T> List<T> fromJsonNodeList(final JsonNode node, final Class<T> elementType) {
        return HasJsonNodeMapper.fromJsonNodeList(node, elementType);
    }

    /**
     * Accepts a json array which holds a {@link Set} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link #toJsonNode(Set)}.
     */
    static <T> Set<T> fromJsonNodeSet(final JsonNode node, final Class<T> elementType) {
        return HasJsonNodeMapper.fromJsonNodeSet(node, elementType);
    }

    /**
     * Accepts a json object which holds a {@link Map} using the key and element types to deserialize the json.
     * This is essentially the inverse of {@link #toJsonNode(Map}
     */
    static <K, V> Map<K, V> fromJsonNodeMap(final JsonNode node, final Class<K> keyType, final Class<V> valueType) {
        return HasJsonNodeMapper.fromJsonNodeMap(node, keyType, valueType);
    }

    // fromJsonNodeWithType.......................................................................................................

    /**
     * Expects a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    static <T> List<T> fromJsonNodeWithTypeList(final JsonNode node) {
        return HasJsonNodeMapper.fromJsonNodeWithTypeList(node);
    }

    /**
     * Expects a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    static <T> Set<T> fromJsonNodeWithTypeSet(final JsonNode node) {
        return HasJsonNodeMapper.fromJsonNodeWithTypeSet(node);
    }

    /**
     * Expects a {@link JsonArrayNode} holding entries of the {@link Map} tagged with type and values.
     */
    static <K, V> Map<K, V> fromJsonNodeWithTypeMap(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return HasJsonNodeMapper.fromJsonNodeWithTypeMap(node);
    }

    /**
     * Assumes a wrapper object with the type and value, basically the inverse of {@link HasJsonNode#toJsonNodeWithType()}.
     */
    static <T> T fromJsonNodeWithType(final JsonNode node) {
        return HasJsonNodeMapper.fromJsonNodeWithType(node);
    }

    // toJsonNode .................................................................................................

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final List<?> list) {
        return HasJsonNodeMapper.toJsonNodeList(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final Set<?> set) {
        return HasJsonNodeMapper.toJsonNodeSet(set);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final Map<?, ?> map) {
        return HasJsonNodeMapper.toJsonNodeMap(map);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeWithTypeList(final List<?> list) {
        return HasJsonNodeListMapper.toJsonNodeWithTypeList0(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeWithTypeSet(final Set<?> set) {
        return HasJsonNodeSetMapper.toJsonNodeWithTypeSet0(set);
    }

    /**
     * Accepts a {@link Map} and returns its {@link JsonNode} equivalent.
     */
    static JsonNode toJsonNodeWithTypeMap(final Map<?, ?> map) {
        return HasJsonNodeMapMapper.toJsonNodeWithTypeMap0(map);
    }
    
    // toJsonNodeWithType .................................................................................................

    /**
     * Supports all basic types such as primitive wrappers including null, {@link String} and implementations of {@link HasJsonNode}.
     */
    static JsonNode toJsonNodeWithType(final Object object) {
        return HasJsonNodeMapper.toJsonNodeWithType(object);
    }

    /**
     * Wraps the {@link JsonNode} with a type name declaration.
     */
    default JsonNode toJsonNodeWithType() {
        return HasJsonNodeMapper.toJsonNodeWithTypeHasJsonNode(this);
    }

    /**
     * Returns the {@link JsonNode} equivalent of this object.
     */
    JsonNode toJsonNode();
}
