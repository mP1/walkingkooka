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

import walkingkooka.text.CharSequences;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        throw new IllegalArgumentException("Required property " + CharSequences.quoteAndEscape(property.value()) + " missing=" + node);
    }

    /**
     * Shared function used to report a required property is missing within a static fromJsonNode.
     */
    static void unknownPropertyPresent(final JsonNodeName property,
                                       final JsonNode node) {
        throw new IllegalArgumentException("Unknown property " + CharSequences.quoteAndEscape(property.value()) + " present=" + node);
    }

    /**
     * Registers a factory that parses a {@link JsonNode} into a value for the given {@link Class}.
     */
    static <T extends HasJsonNode> void register(final String typeName,
                                                 final Function<JsonNode, T> from,
                                                 final Class<?>... types) {
        HasJsonNodeMapper.register(typeName, from, types);
    }

    // registeredType .................................................................................................

    /**
     * Returns one of possibly many registered {@link Class types} for the given type name.
     */
    static Optional<Class<?>> registeredType(final JsonStringNode name) {
        return HasJsonNodeMapper.registeredType(name);
    }

    // typeName .....................................................................................................

    /**
     * Returns the type name identifying the given {@link Class} providing it is registered.
     */
    static Optional<JsonStringNode> typeName(final Class<?> type) {
        return HasJsonNodeMapper.typeName(type);
    }

    // toJsonNode .................................................................................................

    /**
     * Accepts an {@link Object} and creates a {@link JsonNode} equivalent. The inverse operation would be {@link JsonNode#fromJsonNode(Class)}.
     * This is useful for types that do not implement {@link HasJsonNode} but are probably handled automatically eg: {@link String}
     * and {@link LocalDate}.
     */
    static JsonNode toJsonNodeObject(final Object object) {
        return HasJsonNodeMapper.toJsonNodeObject(object);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeList(final List<?> list) {
        return HasJsonNodeMapper.toJsonNodeList(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeSet(final Set<?> set) {
        return HasJsonNodeMapper.toJsonNodeSet(set);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNodeMap(final Map<?, ?> map) {
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
        return HasJsonNodeMapper.toJsonNodeWithTypeObject(object);
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
