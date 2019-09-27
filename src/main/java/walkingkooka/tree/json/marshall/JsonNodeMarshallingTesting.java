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

public interface JsonNodeMarshallingTesting<V> extends Testing {

    @Test
    default void testRegistered() throws Exception {
        this.createJsonNodeMappingValue();

        final Class<V> type = this.type();
        final String typeName = this.type().getName();

        if (type.isEnum()) {
            final Object[] values = Cast.to(type.getMethod("values").invoke(null));
            assertEquals(Lists.empty(),
                    Arrays.stream(values)
                            .filter(e -> BasicJsonMarshaller.TYPENAME_TO_MARSHALLER.get(e.getClass().getName()) == null)
                            .collect(Collectors.toList()),
                    () -> "Not all enum: " + typeName + " value types not registered -> JsonNodeContext.register()=" + BasicJsonMarshaller.TYPENAME_TO_MARSHALLER);

        } else {
            assertNotEquals(
                    null,
                    BasicJsonMarshaller.TYPENAME_TO_MARSHALLER.get(typeName),
                    () -> "Type: " + typeName + " factory not registered -> JsonNodeContext.register()=" + BasicJsonMarshaller.TYPENAME_TO_MARSHALLER);
        }
    }

    @Test
    default void testTypeNameFromClass() {
        final V value = this.createJsonNodeMappingValue();

        final JsonNodeMarshallContext context = this.marshallContext();

        final JsonNode node = context.marshallWithType(value);
        if (node.isObject()) {
            assertEquals(node.objectOrFail().get(BasicJsonNodeContext.TYPE).map(n -> n.removeParent()),
                    context.typeName(value.getClass()),
                    () -> value + " & " + node);
        }
    }

    @Test
    default void testTypeNameAndRegisteredType() {
        this.createJsonNodeMappingValue(); // ensure static initializer is run...

        final JsonNodeMarshallContext context = this.marshallContext();

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
                        .filter(m -> m.getName().startsWith("unmarshall"))
                        .collect(Collectors.toList()),
                () -> "static unmarshall methods must not be public");
    }

    @Test
    default void testFromJsonNodeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.unmarshall(null);
        });
    }

    default void unmarshallAndCheck(final String from,
                                      final Object value) {
        this.unmarshallAndCheck(JsonNode.parse(from), value);
    }

    default void unmarshallAndCheck(final JsonNode from,
                                      final Object value) {
        assertEquals(value,
                this.unmarshall(from),
                () -> "unmarshall failed " + from);
    }

    default void unmarshallFails(final String from,
                                   final Class<? extends Throwable> thrown) {
        unmarshallFails(JsonNode.parse(from), thrown);
    }

    default void unmarshallFails(final JsonNode from,
                                   final Class<? extends Throwable> thrown) {
        this.unmarshallFails(from,
                thrown,
                this.unmarshallContext());
    }

    default void unmarshallFails(final JsonNode from,
                                   final Class<? extends Throwable> thrown,
                                   final JsonNodeUnmarshallContext context) {
        assertThrows(JsonNodeUnmarshallException.class, () -> {
            context.unmarshall(from, this.type());
        });
    }

    @Test
    default void testMarshallRoundtripTwice() {
        this.marshallRoundTripTwiceAndCheck(this.createJsonNodeMappingValue());
    }

    @Test
    default void testMarshallWithTypeRoundtripTwice() {
        this.marshallWithTypeRoundTripTwiceAndCheck(this.createJsonNodeMappingValue());
    }

    @Test
    default void testMarshallRoundtripList() {
        final List<Object> list = Lists.of(this.createJsonNodeMappingValue());

        assertEquals(list,
                this.unmarshallContext().unmarshallWithTypeList(this.marshallContext().marshallWithTypeList(list)),
                () -> "Roundtrip to -> from -> to failed list=" + list);
    }

    @Test
    default void testMarshallRoundtripSet() {
        final Set<Object> set = Sets.of(this.createJsonNodeMappingValue());

        assertEquals(set,
                this.unmarshallContext().unmarshallWithTypeSet(this.marshallContext().marshallWithTypeSet(set)),
                () -> "Roundtrip to -> from -> to failed set=" + set);
    }

    @Test
    default void testMarshallRoundtripMap() {
        final Map<String, Object> map = Maps.of("key123", this.createJsonNodeMappingValue());

        assertEquals(map,
                this.unmarshallContext().unmarshallWithTypeMap(this.marshallContext().marshallWithTypeMap(map)),
                () -> "Roundtrip to -> from -> to failed marshall=" + map);
    }

    default V unmarshall(final JsonNode from) {
        return this.unmarshall(from, this.unmarshallContext());
    }

    /**
     * Typically calls a static method that accepts a {@link JsonNode} and creates a {@link V object}.
     */
    V unmarshall(final JsonNode from,
                   final JsonNodeUnmarshallContext context);

    default void marshallAndCheck(final Object value,
                                    final String json) {
        marshallAndCheck(value, JsonNode.parse(json));
    }

    default void marshallAndCheck(final Object value,
                                    final JsonNode json) {
        this.marshallAndCheck(value,
                json,
                this.marshallContext());
    }

    default void marshallAndCheck(final Object value,
                                    final JsonNode json,
                                    final JsonNodeMarshallContext context) {
        assertEquals(json,
                context.marshall(value),
                () -> "marshall doesnt match=" + value);
    }

    default void marshallRoundTripTwiceAndCheck(final Object value) {
        this.marshallRoundTripTwiceAndCheck(value, this.marshallContext());
    }

    default void marshallRoundTripTwiceAndCheck(final Object value,
                                                  final JsonNodeMarshallContext context) {
        final JsonNode jsonNode = context.marshall(value);

        final Object fromValue = this.unmarshall(jsonNode);
        final JsonNode jsonNode2 = context.marshall(fromValue);

        assertEquals(fromValue,
                this.unmarshall(jsonNode2),
                () -> "Roundtrip to -> from -> to failed value=" + CharSequences.quoteIfChars(value));
    }

    default void marshallWithTypeRoundTripTwiceAndCheck(final Object value) {
        this.marshallWithTypeRoundTripTwiceAndCheck(value,
                this.unmarshallContext(),
                this.marshallContext());
    }

    default void marshallWithTypeRoundTripTwiceAndCheck(final Object value,
                                                          final JsonNodeUnmarshallContext fromContext,
                                                          final JsonNodeMarshallContext toContext) {
        final JsonNode jsonNode = toContext.marshallWithType(value);

        final Object from = fromContext.unmarshallWithType(jsonNode);
        final JsonNode jsonNode2 = toContext.marshallWithType(from);

        assertEquals(from,
                fromContext.unmarshallWithType(jsonNode2),
                () -> "BasicJsonMarshaller roundtrip to -> from -> to failed =" + CharSequences.quoteIfChars(value));

        assertEquals(from,
                fromContext.unmarshallWithType(jsonNode2),
                () -> "BasicJsonMarshaller roundtrip to -> from -> to failed =" + CharSequences.quoteIfChars(value));
    }

    V createJsonNodeMappingValue();

    default JsonNodeUnmarshallContext unmarshallContext() {
        return JsonNodeUnmarshallContexts.basic();
    }

    default JsonNodeMarshallContext marshallContext() {
        return JsonNodeMarshallContexts.basic();
    }

    Class<V> type();
}
