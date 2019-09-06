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

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface FromJsonNodeContextTesting<C extends FromJsonNodeContext> extends JsonNodeContextTesting<C> {

    // setObjectPreProcessor............................................................................................

    @Test
    default void testSetObjectPreProcessorNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().setObjectPreProcessor(null);
        });
    }

    @Test
    default void testSetObjectPreProcessor() {
        final FromJsonNodeContext context = this.createContext();
        final BiFunction<JsonObjectNode, Class<?>, JsonObjectNode> processor = (jsonObject, type) -> jsonObject;

        final FromJsonNodeContext with = context.setObjectPreProcessor(processor);
        assertNotSame(context, with);
    }

    @Test
    default void testSetObjectPreProcessorSame() {
        final FromJsonNodeContext context = this.createContext();
        final BiFunction<JsonObjectNode, Class<?>, JsonObjectNode> processor = (jsonObject, type) -> jsonObject;

        final FromJsonNodeContext with = context.setObjectPreProcessor(processor);
        assertSame(with, with.setObjectPreProcessor(processor));
    }
    
    // fromJsonNode.....................................................................................................

    @Test
    default void testFromJsonNodeNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().fromJsonNode(JsonNode.nullNode(), null);
        });
    }

    // fromJsonNodeList.................................................................................................

    @Test
    default void testFromJsonNodeListNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().fromJsonNodeList(JsonNode.nullNode(), null);
        });
    }

    // fromJsonNodeSet..................................................................................................

    @Test
    default void testFromJsonNodeSetNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().fromJsonNodeSet(JsonNode.nullNode(), null);
        });
    }

    // fromJsonNodeMap..................................................................................................

    @Test
    default void testFromJsonNodeMapNullKeyTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().fromJsonNodeMap(JsonNode.array(), null, String.class);
        });
    }

    @Test
    default void testFromJsonNodeMapNullValueTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().fromJsonNodeMap(JsonNode.array(), String.class, null);
        });
    }

    // fromJsonNode.....................................................................................................

    default <T> void fromJsonNodeAndCheck(final JsonNode node,
                                          final Class<T> type,
                                          final T expected) {
        this.fromJsonNodeAndCheck(this.createContext(),
                node,
                type,
                expected);
    }

    default <T> void fromJsonNodeAndCheck(final FromJsonNodeContext context,
                                          final JsonNode node,
                                          final Class<T> type,
                                          final T expected) {
        assertEquals(expected,
                context.fromJsonNode(node, type),
                () -> context + " fromJsonNode " + node + " type " + type.getName());
    }

    // fromJsonNodeList.................................................................................................

    default <T> void fromJsonNodeListAndCheck(final JsonNode node,
                                              final Class<T> type,
                                              final List<T> expected) {
        this.fromJsonNodeListAndCheck(this.createContext(),
                node,
                type,
                expected);
    }

    default <T> void fromJsonNodeListAndCheck(final FromJsonNodeContext context,
                                              final JsonNode node,
                                              final Class<T> type,
                                              final List<T> expected) {
        assertEquals(expected,
                context.fromJsonNodeList(node, type),
                () -> context + " fromJsonNodeList " + node + " type " + type.getName());
    }

    // fromJsonNodeSet..................................................................................................

    default <T> void fromJsonNodeSetAndCheck(final JsonNode node,
                                             final Class<T> type,
                                             final Set<T> expected) {
        this.fromJsonNodeSetAndCheck(this.createContext(),
                node,
                type,
                expected);
    }

    default <T> void fromJsonNodeSetAndCheck(final FromJsonNodeContext context,
                                             final JsonNode node,
                                             final Class<T> type,
                                             final Set<T> expected) {
        assertEquals(expected,
                context.fromJsonNodeSet(node, type),
                () -> context + " fromJsonNodeSet " + node + " type " + type.getName());
    }

    // fromJsonNodeMap.................................................................................................

    default <K, V> void fromJsonNodeMapAndCheck(final JsonNode node,
                                                final Class<K> key,
                                                final Class<V> value,
                                                final Map<K, V> expected) {
        this.fromJsonNodeMapAndCheck(this.createContext(),
                node,
                key,
                value,
                expected);
    }

    default <K, V> void fromJsonNodeMapAndCheck(final FromJsonNodeContext context,
                                                final JsonNode node,
                                                final Class<K> key,
                                                final Class<V> value,
                                                final Map<K, V> expected) {
        assertEquals(expected,
                context.fromJsonNodeMap(node, key, value),
                () -> context + " fromJsonNodeMap " + node + " key " + key.getName() + " value " + value.getName());
    }

    // fromJsonNodeWithType.............................................................................................

    default void fromJsonNodeWithTypeAndCheck(final JsonNode node,
                                              final Object expected) {
        this.fromJsonNodeWithTypeAndCheck(this.createContext(),
                node,
                expected);
    }

    default void fromJsonNodeWithTypeAndCheck(final FromJsonNodeContext context,
                                              final JsonNode node,
                                              final Object expected) {
        assertEquals(expected,
                context.fromJsonNodeWithType(node),
                () -> context + " fromJsonNodeWithType " + node);
    }

    // fromJsonNodeWithTypeList..........................................................................................

    default void fromJsonNodeWithTypeListAndCheck(final JsonNode node,
                                                  final List<?> expected) {
        this.fromJsonNodeWithTypeListAndCheck(this.createContext(),
                node,
                expected);
    }

    default void fromJsonNodeWithTypeListAndCheck(final FromJsonNodeContext context,
                                                  final JsonNode node,
                                                  final List<?> expected) {
        assertEquals(expected,
                context.fromJsonNodeWithTypeList(node),
                () -> context + " fromJsonNodeWithTypeList " + node);
    }

    // fromJsonNodeWithTypeSet..........................................................................................

    default void fromJsonNodeWithTypeSetAndCheck(final JsonNode node,
                                                 final Set<?> expected) {
        this.fromJsonNodeWithTypeSetAndCheck(this.createContext(),
                node,
                expected);
    }

    default void fromJsonNodeWithTypeSetAndCheck(final FromJsonNodeContext context,
                                                 final JsonNode node,
                                                 final Set<?> expected) {
        assertEquals(expected,
                context.fromJsonNodeWithTypeSet(node),
                () -> context + " fromJsonNodeWithTypeSet " + node);
    }

    // fromJsonNodeMap.................................................................................................

    default void fromJsonNodeWithTypeMapAndCheck(final JsonNode node,
                                                 final Map<?, ?> expected) {
        this.fromJsonNodeMapWithTypeAndCheck(this.createContext(),
                node,
                expected);
    }

    default void fromJsonNodeMapWithTypeAndCheck(final FromJsonNodeContext context,
                                                 final JsonNode node,
                                                 final Map<?, ?> expected) {
        assertEquals(expected,
                context.fromJsonNodeWithTypeMap(node),
                () -> context + " fromJsonNodeWithTypeMap " + node);
    }

    // TypeNameTesting..................................................................................................

    @Override
    default String typeNameSuffix() {
        return FromJsonNodeContext.class.getSimpleName();
    }
}
