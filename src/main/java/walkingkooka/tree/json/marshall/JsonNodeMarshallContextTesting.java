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

public interface JsonNodeMarshallContextTesting<C extends JsonNodeMarshallContext> extends JsonNodeContextTesting<C> {

    @Test
    default void testSetObjectPostProcessorNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createContext().setObjectPostProcessor(null);
        });
    }

    @Test
    default void testSetObjectPostProcessor() {
        final JsonNodeMarshallContext context = this.createContext();
        final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor = (value, jsonObject) -> jsonObject;

        final JsonNodeMarshallContext with = context.setObjectPostProcessor(processor);
        assertNotSame(context, with);
    }

    @Test
    default void testSetObjectPostProcessorSame() {
        final JsonNodeMarshallContext context = this.createContext();
        final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor = (value, jsonObject) -> jsonObject;

        final JsonNodeMarshallContext with = context.setObjectPostProcessor(processor);
        assertSame(with, with.setObjectPostProcessor(processor));
    }

    @Test
    default void testMarshallNull() {
        this.marshallAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallListNull() {
        this.marshallListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallSetNull() {
        this.marshallSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallMapNull() {
        this.marshallMapAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallWithTypeNull() {
        this.marshallWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallWithTypeListNull() {
        this.marshallWithTypeListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallWithTypeSetNull() {
        this.marshallWithTypeSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    default void testMarshallWithTypeMapNull() {
        this.marshallWithTypeMapAndCheck(null, JsonNode.nullNode());
    }

    // marshall.......................................................................................................

    default void marshallAndCheck(final Object value,
                                    final JsonNode expected) {
        this.marshallAndCheck(this.createContext(),
                value,
                expected);
    }

    default void marshallAndCheck(final JsonNodeMarshallContext context,
                                    final Object value,
                                    final JsonNode expected) {
        assertEquals(expected,
                context.marshall(value),
                () -> context + " marshall " + value);
    }

    // marshallList...................................................................................................

    default void marshallListAndCheck(final List<?> list,
                                        final JsonNode expected) {
        this.marshallListAndCheck(this.createContext(),
                list,
                expected);
    }

    default void marshallListAndCheck(final JsonNodeMarshallContext context,
                                        final List<?> list,
                                        final JsonNode expected) {
        assertEquals(expected,
                context.marshallList(list),
                () -> context + " marshallList " + list);
    }

    // marshallSet....................................................................................................

    default void marshallSetAndCheck(final Set<?> set,
                                       final JsonNode expected) {
        this.marshallSetAndCheck(this.createContext(),
                set,
                expected);
    }

    default void marshallSetAndCheck(final JsonNodeMarshallContext context,
                                       final Set<?> set,
                                       final JsonNode expected) {
        assertEquals(expected,
                context.marshallSet(set),
                () -> context + " marshallSet " + set);
    }

    // marshallMap....................................................................................................

    default void marshallMapAndCheck(final Map<?, ?> map,
                                       final JsonNode expected) {
        this.marshallMapAndCheck(this.createContext(),
                map,
                expected);
    }

    default void marshallMapAndCheck(final JsonNodeMarshallContext context,
                                       final Map<?, ?> map,
                                       final JsonNode expected) {
        assertEquals(expected,
                context.marshallMap(map),
                () -> context + " marshallMap " + map);
    }

    // marshallWithType...............................................................................................

    default void marshallWithTypeAndCheck(final Object value,
                                            final JsonNode expected) {
        this.marshallWithTypeAndCheck(this.createContext(),
                value,
                expected);
    }

    default void marshallWithTypeAndCheck(final JsonNodeMarshallContext context,
                                            final Object value,
                                            final JsonNode expected) {
        assertEquals(expected,
                context.marshallWithType(value),
                () -> context + " marshallWithType " + value);
    }

    // marshallWithTypeList...........................................................................................

    default void marshallWithTypeListAndCheck(final List<?> list,
                                                final JsonNode expected) {
        this.marshallWithTypeListAndCheck(this.createContext(),
                list,
                expected);
    }

    default void marshallWithTypeListAndCheck(final JsonNodeMarshallContext context,
                                                final List<?> list,
                                                final JsonNode expected) {
        assertEquals(expected,
                context.marshallWithTypeList(list),
                () -> context + " marshallWithTypeList " + list);
    }

    // marshallWithTypeSet............................................................................................

    default void marshallWithTypeSetAndCheck(final Set<?> set,
                                               final JsonNode expected) {
        this.marshallWithTypeSetAndCheck(this.createContext(),
                set,
                expected);
    }

    default void marshallWithTypeSetAndCheck(final JsonNodeMarshallContext context,
                                               final Set<?> set,
                                               final JsonNode expected) {
        assertEquals(expected,
                context.marshallWithTypeSet(set),
                () -> context + " marshallWithTypeSet " + set);
    }

    // marshallWithTypeMap............................................................................................

    default void marshallWithTypeMapAndCheck(final Map<?, ?> map,
                                               final JsonNode expected) {
        this.marshallWithTypeMapAndCheck(this.createContext(),
                map,
                expected);
    }

    default void marshallWithTypeMapAndCheck(final JsonNodeMarshallContext context,
                                               final Map<?, ?> map,
                                               final JsonNode expected) {
        assertEquals(expected,
                context.marshallWithTypeMap(map),
                () -> context + " marshallWithTypeMap " + map);
    }

    // TypeNameTesting..................................................................................................

    @Override
    default String typeNameSuffix() {
        return JsonNodeMarshallContext.class.getSimpleName();
    }
}
