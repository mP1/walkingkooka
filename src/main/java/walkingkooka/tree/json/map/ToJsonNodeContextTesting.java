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

package walkingkooka.tree.json.map;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface ToJsonNodeContextTesting<C extends ToJsonNodeContext> extends JsonNodeContextTesting<C> {

    @Test
    default void testToJsonNodeNull() {
        this.toJsonNodeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeListNull() {
        this.toJsonNodeListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeSetNull() {
        this.toJsonNodeSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeMapNull() {
        this.toJsonNodeMapAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeWithTypeNull() {
        this.toJsonNodeWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeWithTypeListNull() {
        this.toJsonNodeWithTypeListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeWithTypeSetNull() {
        this.toJsonNodeWithTypeSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testToJsonNodeWithTypeMapNull() {
        this.toJsonNodeWithTypeMapAndCheck(null, JsonNode.nullNode());
    }

    // toJsonNode.......................................................................................................

    default void toJsonNodeAndCheck(final Object value,
                                    final JsonNode expected) {
        this.toJsonNodeAndCheck(this.createContext(),
                value,
                expected);
    }

    default void toJsonNodeAndCheck(final ToJsonNodeContext context,
                                    final Object value,
                                    final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNode(value),
                () -> context + " toJsonNode " + value);
    }

    // toJsonNodeList...................................................................................................

    default void toJsonNodeListAndCheck(final List<?> list,
                                        final JsonNode expected) {
        this.toJsonNodeListAndCheck(this.createContext(),
                list,
                expected);
    }

    default void toJsonNodeListAndCheck(final ToJsonNodeContext context,
                                        final List<?> list,
                                        final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeList(list),
                () -> context + " toJsonNodeList " + list);
    }

    // toJsonNodeSet....................................................................................................

    default void toJsonNodeSetAndCheck(final Set<?> set,
                                       final JsonNode expected) {
        this.toJsonNodeSetAndCheck(this.createContext(),
                set,
                expected);
    }

    default void toJsonNodeSetAndCheck(final ToJsonNodeContext context,
                                       final Set<?> set,
                                       final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeSet(set),
                () -> context + " toJsonNodeSet " + set);
    }

    // toJsonNodeMap....................................................................................................

    default void toJsonNodeMapAndCheck(final Map<?, ?> map,
                                       final JsonNode expected) {
        this.toJsonNodeMapAndCheck(this.createContext(),
                map,
                expected);
    }

    default void toJsonNodeMapAndCheck(final ToJsonNodeContext context,
                                       final Map<?, ?> map,
                                       final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeMap(map),
                () -> context + " toJsonNodeMap " + map);
    }

    // toJsonNodeWithType...............................................................................................

    default void toJsonNodeWithTypeAndCheck(final Object value,
                                            final JsonNode expected) {
        this.toJsonNodeWithTypeAndCheck(this.createContext(),
                value,
                expected);
    }

    default void toJsonNodeWithTypeAndCheck(final ToJsonNodeContext context,
                                            final Object value,
                                            final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeWithType(value),
                () -> context + " toJsonNodeWithType " + value);
    }

    // toJsonNodeWithTypeList...........................................................................................

    default void toJsonNodeWithTypeListAndCheck(final List<?> list,
                                                final JsonNode expected) {
        this.toJsonNodeWithTypeListAndCheck(this.createContext(),
                list,
                expected);
    }

    default void toJsonNodeWithTypeListAndCheck(final ToJsonNodeContext context,
                                                final List<?> list,
                                                final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeWithTypeList(list),
                () -> context + " toJsonNodeWithTypeList " + list);
    }

    // toJsonNodeWithTypeSet............................................................................................

    default void toJsonNodeWithTypeSetAndCheck(final Set<?> set,
                                               final JsonNode expected) {
        this.toJsonNodeWithTypeSetAndCheck(this.createContext(),
                set,
                expected);
    }

    default void toJsonNodeWithTypeSetAndCheck(final ToJsonNodeContext context,
                                               final Set<?> set,
                                               final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeWithTypeSet(set),
                () -> context + " toJsonNodeWithTypeSet " + set);
    }

    // toJsonNodeWithTypeMap............................................................................................

    default void toJsonNodeWithTypeMapAndCheck(final Map<?, ?> map,
                                               final JsonNode expected) {
        this.toJsonNodeWithTypeMapAndCheck(this.createContext(),
                map,
                expected);
    }

    default void toJsonNodeWithTypeMapAndCheck(final ToJsonNodeContext context,
                                               final Map<?, ?> map,
                                               final JsonNode expected) {
        assertEquals(expected,
                context.toJsonNodeWithTypeMap(map),
                () -> context + " toJsonNodeWithTypeMap " + map);
    }

    // TypeNameTesting..................................................................................................

    @Override
    default String typeNameSuffix() {
        return ToJsonNodeContext.class.getSimpleName();
    }
}
