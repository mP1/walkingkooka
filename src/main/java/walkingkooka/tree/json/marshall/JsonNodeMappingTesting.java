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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.type.MethodAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface JsonNodeMappingTesting<V> extends Testing {

    @Test
    default void testRegistered() throws Exception {
        this.createJsonNodeMappingValue();

        final Class<V> type = this.type();
        final String typeName = this.type().getName();

        if (type.isEnum()) {
            final Object[] values = Cast.to(type.getMethod("values").invoke(null));
            assertEquals(Lists.empty(),
                    Arrays.stream(values)
                            .filter(e -> BasicMarshaller.TYPENAME_TO_MARSHALLER.get(e.getClass().getName()) == null)
                            .collect(Collectors.toList()),
                    () -> "Not all enum: " + typeName + " value types not registered -> JsonNodeContext.register()=" + BasicMarshaller.TYPENAME_TO_MARSHALLER);

        } else {
            assertNotEquals(
                    null,
                    BasicMarshaller.TYPENAME_TO_MARSHALLER.get(typeName),
                    () -> "Type: " + typeName + " factory not registered -> JsonNodeContext.register()=" + BasicMarshaller.TYPENAME_TO_MARSHALLER);
        }
    }

    @Test
    default void testTypeNameFromClass() {
        final V value = this.createJsonNodeMappingValue();

        final ToJsonNodeContext context = this.toJsonNodeContext();

        final JsonNode node = context.toJsonNodeWithType(value);
        if (node.isObject()) {
            assertEquals(node.objectOrFail().get(BasicJsonNodeContext.TYPE).map(n -> n.removeParent()),
                    context.typeName(value.getClass()),
                    () -> value + " & " + node);
        }
    }

    @Test
    default void testTypeNameAndRegisteredType() {
        this.createJsonNodeMappingValue(); // ensure static initializer is run...

        final ToJsonNodeContext context = this.toJsonNodeContext();

        final Class<V> type = this.type();
        final Optional<JsonStringNode> maybeTypeName = context.typeName(type);
        assertNotEquals(Optional.empty(), maybeTypeName, () -> "typeName for " + type.getName() + " failed");

        final JsonStringNode typeName = maybeTypeName.get();
        final Optional<Class<?>> maybeRegisteredType = context.registeredType(typeName);
        assertNotEquals(Optional.empty(), maybeRegisteredType, () -> "registeredType for " + type.getName() + " failed");

        final Class<?> registeredType = maybeRegisteredType.get();
        assertTrue(registeredType.isAssignableFrom(type), () -> "registeredType for " + registeredType.getName() + " failed " + registeredType + " " + type);
    }

    @Test
    default void testStaticFromJsonNodeMethodsNonPublic() {
        assertEquals(Lists.empty(),
                Arrays.stream(this.type().getMethods())
                        .filter(MethodAttributes.STATIC::is)
                        .filter(m -> m.getName().startsWith("fromJsonNode"))
                        .collect(Collectors.toList()),
                () -> "static fromJsonNode methods must not be public");
    }

    @Test
    default void testFromJsonNodeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.fromJsonNode(null);
        });
    }

    default void fromJsonNodeAndCheck(final String from,
                                      final Object value) {
        this.fromJsonNodeAndCheck(JsonNode.parse(from), value);
    }

    default void fromJsonNodeAndCheck(final JsonNode from,
                                      final Object value) {
        assertEquals(value,
                this.fromJsonNode(from),
                () -> "fromJsonNode failed " + from);
    }

    default void fromJsonNodeFails(final String from,
                                   final Class<? extends Throwable> thrown) {
        fromJsonNodeFails(JsonNode.parse(from), thrown);
    }

    default void fromJsonNodeFails(final JsonNode from,
                                   final Class<? extends Throwable> thrown) {
        this.fromJsonNodeFails(from,
                thrown,
                this.fromJsonNodeContext());
    }

    default void fromJsonNodeFails(final JsonNode from,
                                   final Class<? extends Throwable> thrown,
                                   final FromJsonNodeContext context) {
        assertThrows(FromJsonNodeException.class, () -> {
            context.fromJsonNode(from, this.type());
        });
    }

    @Test
    default void testToJsonNodeRoundtripTwice() {
        this.toJsonNodeRoundTripTwiceAndCheck(this.createJsonNodeMappingValue());
    }

    @Test
    default void testToJsonNodeWithTypeRoundtripTwice() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createJsonNodeMappingValue());
    }

    @Test
    default void testToJsonNodeRoundtripList() {
        final List<Object> list = Lists.of(this.createJsonNodeMappingValue());

        assertEquals(list,
                this.fromJsonNodeContext().fromJsonNodeWithTypeList(this.toJsonNodeContext().toJsonNodeWithTypeList(list)),
                () -> "Roundtrip to -> from -> to failed list=" + list);
    }

    @Test
    default void testToJsonNodeRoundtripSet() {
        final Set<Object> set = Sets.of(this.createJsonNodeMappingValue());

        assertEquals(set,
                this.fromJsonNodeContext().fromJsonNodeWithTypeSet(this.toJsonNodeContext().toJsonNodeWithTypeSet(set)),
                () -> "Roundtrip to -> from -> to failed set=" + set);
    }

    @Test
    default void testToJsonNodeRoundtripMap() {
        final Map<String, Object> map = Maps.of("key123", this.createJsonNodeMappingValue());

        assertEquals(map,
                this.fromJsonNodeContext().fromJsonNodeWithTypeMap(this.toJsonNodeContext().toJsonNodeWithTypeMap(map)),
                () -> "Roundtrip to -> from -> to failed marshall=" + map);
    }

    default V fromJsonNode(final JsonNode from) {
        return this.fromJsonNode(from, this.fromJsonNodeContext());
    }

    /**
     * Typically calls a static method that accepts a {@link JsonNode} and creates a {@link V object}.
     */
    V fromJsonNode(final JsonNode from,
                   final FromJsonNodeContext context);

    default void toJsonNodeAndCheck(final Object value,
                                    final String json) {
        toJsonNodeAndCheck(value, JsonNode.parse(json));
    }

    default void toJsonNodeAndCheck(final Object value,
                                    final JsonNode json) {
        this.toJsonNodeAndCheck(value,
                json,
                this.toJsonNodeContext());
    }

    default void toJsonNodeAndCheck(final Object value,
                                    final JsonNode json,
                                    final ToJsonNodeContext context) {
        assertEquals(json,
                context.toJsonNode(value),
                () -> "toJsonNode doesnt match=" + value);
    }

    default void toJsonNodeRoundTripTwiceAndCheck(final Object value) {
        this.toJsonNodeRoundTripTwiceAndCheck(value, this.toJsonNodeContext());
    }

    default void toJsonNodeRoundTripTwiceAndCheck(final Object value,
                                                  final ToJsonNodeContext context) {
        final JsonNode jsonNode = context.toJsonNode(value);

        final Object fromValue = this.fromJsonNode(jsonNode);
        final JsonNode jsonNode2 = context.toJsonNode(fromValue);

        assertEquals(fromValue,
                this.fromJsonNode(jsonNode2),
                () -> "Roundtrip to -> from -> to failed value=" + CharSequences.quoteIfChars(value));
    }

    default void toJsonNodeWithTypeRoundTripTwiceAndCheck(final Object value) {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(value,
                this.fromJsonNodeContext(),
                this.toJsonNodeContext());
    }

    default void toJsonNodeWithTypeRoundTripTwiceAndCheck(final Object value,
                                                          final FromJsonNodeContext fromContext,
                                                          final ToJsonNodeContext toContext) {
        final JsonNode jsonNode = toContext.toJsonNodeWithType(value);

        final Object from = fromContext.fromJsonNodeWithType(jsonNode);
        final JsonNode jsonNode2 = toContext.toJsonNodeWithType(from);

        assertEquals(from,
                fromContext.fromJsonNodeWithType(jsonNode2),
                () -> "BasicMarshaller roundtrip to -> from -> to failed =" + CharSequences.quoteIfChars(value));

        assertEquals(from,
                fromContext.fromJsonNodeWithType(jsonNode2),
                () -> "BasicMarshaller roundtrip to -> from -> to failed =" + CharSequences.quoteIfChars(value));
    }

    V createJsonNodeMappingValue();

    default FromJsonNodeContext fromJsonNodeContext() {
        return FromJsonNodeContexts.basic((o, t) -> o);
    }

    default ToJsonNodeContext toJsonNodeContext() {
        return ToJsonNodeContexts.basic((v, o)-> o);
    }

    Class<V> type();
}
