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
    static <T extends HasJsonNode> void register(final Class<T> type,
                                                 final Function<JsonNode, T> from) {
        HasJsonNode2.register(type, from);
    }

    // fromJsonNode.......................................................................................................

    /**
     * Accepts a json array which holds a {@link List} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link #toJsonNode(List)}.
     */
    static <T> List<T> fromJsonNodeList(final JsonNode node, final Class<T> elementType) {
        return HasJsonNode2.fromJsonNodeList(node, elementType);
    }

    /**
     * Accepts a json array which holds a {@link Set} and uses the element type to determine the elements and reads them from json.
     * Essentially the inverse of {@link #toJsonNode(Set)}.
     */
    static <T> Set<T> fromJsonNodeSet(final JsonNode node, final Class<T> elementType) {
        return HasJsonNode2.fromJsonNodeSet(node, elementType);
    }

    // fromJsonNodeWithType.......................................................................................................

    /**
     * Assumes a wrapper object with the type and value, basically the inverse of {@link HasJsonNode#toJsonNodeWithType()}.
     */
    static <T> T fromJsonNodeWithType(final JsonNode node) {
        return HasJsonNode2.fromJsonNodeWithType(node);
    }

    // toJsonNode .................................................................................................

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final List<? extends HasJsonNode> list) {
        return HasJsonNode2.toJsonNode(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNode()}.
     */
    static JsonNode toJsonNode(final Set<? extends HasJsonNode> set) {
        return HasJsonNode2.toJsonNode(set);
    }

    // toJsonNodeWithType .................................................................................................

    /**
     * Supports all basic types such as primitive wrappers including null, {@link String} and implementations of {@link HasJsonNode}.
     */
    static JsonNode toJsonNodeWithType(final Object object) {
        return HasJsonNode2.toJsonNodeWithType(object);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNodeWithType()}.
     */
    static JsonNode toJsonNodeWithType(final List<? extends HasJsonNode> list) {
        return HasJsonNode2.toJsonNodeWithTypeList(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}. Each element
     * is converted to json using {@link HasJsonNode#toJsonNodeWithType()}.
     */
    static JsonNode toJsonNodeWithType(final Set<? extends HasJsonNode> set) {
        return HasJsonNode2.toJsonNodeWithTypeSet(set);
    }

    /**
     * Wraps the {@link JsonNode} with a type name declaration.
     */
    default JsonNode toJsonNodeWithType() {
        return HasJsonNode2.toJsonNodeWithTypeHasJsonNode(this);
    }

    /**
     * Sub classes such as enums should override this, to return the "public" type.
     */
    default Class<?> toJsonNodeType() {
        return this.getClass();
    }

    /**
     * Returns the {@link JsonNode} equivalent of this object.
     */
    JsonNode toJsonNode();
}
