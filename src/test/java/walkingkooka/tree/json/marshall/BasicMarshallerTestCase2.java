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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class BasicMarshallerTestCase2<M extends BasicMarshaller<T>, T> extends BasicMarshallerTestCase<M>
        implements ToStringTesting<M> {

    BasicMarshallerTestCase2() {
        super();
    }

    @Test
    public final void testType() {
        final M marshaller = this.marshaller();
        assertEquals(this.marshallerType(), marshaller.type(), () -> ".type failed for " + marshaller);
    }

    abstract Class<T> marshallerType();

    @Test
    public final void testFromJsonNodeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.marshaller().fromJsonNode(null, this.fromJsonNodeContext());
        });
    }

    @Test
    public void testFromJsonNodeJsonNullNode() {
        this.fromJsonNodeAndCheck(JsonNode.nullNode(), this.jsonNullNode());
    }

    abstract T jsonNullNode();

    @Test
    public final void testFromJsonNode() {
        this.fromJsonNodeAndCheck(this.node(), this.value());
    }

    @Test
    public final void testFromJsonNodeWithType() {
        this.fromJsonNodeWithTypeAndCheck(this.nodeWithType(), this.value());
    }

    @Test
    public final void testToJsonNodeWithNull() {
        this.toJsonNodeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public final void testToJsonNode() {
        this.toJsonNodeAndCheck(this.value(), this.node());
    }

    @Test
    public final void testToJsonNodeWithTypeNull() {
        this.toJsonNodeWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public final void testToJsonNodeWithType() {
        this.toJsonNodeWithTypeAndCheck(this.value(), this.nodeWithType());
    }

    @Test
    public final void testRoundtripToJsonNodeWithTypeFromJsonNodeWithType() {
        final T value = this.value();

        final JsonNode json = this.marshaller()
                .toJsonNodeWithType(value, this.toJsonNodeContext());

        assertEquals(value,
                this.fromJsonNodeContext().fromJsonNodeWithType(json),
                () -> "roundtrip starting with value failed fromValue: " + value + " -> json: " + json);
    }

    @Test
    public final void testRoundtripFromJsonNodeWithTypeMapperToJsonNodeWithType() {
        final JsonNode json = this.nodeWithType();

        final T value = this.fromJsonNodeContext().
                fromJsonNodeWithType(json);

        assertEquals(json,
                this.marshaller().toJsonNodeWithType(value, this.toJsonNodeContext()),
                () -> "roundtrip starting with node failed, json: " + json + " -> value:: " + value);
    }

    @Test
    public final void testRoundtripToJsonNodeWithTypeObjectFromJsonNodeWithType() {
        final T value = this.value();

        final JsonNode json = this.marshaller()
                .toJsonNodeWithType(value, this.toJsonNodeContext());

        assertEquals(value,
                this.fromJsonNodeContext().fromJsonNodeWithType(json),
                () -> "roundtrip starting with value failed, value: " + value + " -> json: " + json);
    }

    @Test
    public void testRoundtripList() {
        final T value = this.value();
        final List<T> list = List.of(value);

        final JsonNode jsonNode = this.toJsonNodeContext().toJsonNodeList(list);

        assertEquals(list,
                this.fromJsonNodeContext().fromJsonNodeList(jsonNode, value.getClass()),
                () -> "roundtrip list: " + list + " -> json: " + jsonNode);
    }

    @Test
    public void testRoundtripSet() {
        final T value = this.value();
        final Set<T> set = Set.of(value);

        final JsonNode jsonNode = this.toJsonNodeContext().toJsonNodeSet(set);

        assertEquals(set,
                this.fromJsonNodeContext().fromJsonNodeSet(jsonNode, value.getClass()),
                () -> "roundtrip set: " + set + " -> json: " + jsonNode);
    }

    @Test
    public void testRoundtripMap() {
        final T value = this.value();
        final Map<String, T> map = Map.of("key1", value);

        final JsonNode jsonNode = this.toJsonNodeContext().toJsonNodeMap(map);

        assertEquals(map,
                this.fromJsonNodeContext().fromJsonNodeMap(jsonNode, String.class, value.getClass()),
                () -> "roundtrip marshall: " + map + " -> json: " + jsonNode);
    }

    @Test
    public void testRoundtripTypeList() {
        final List<T> list = List.of(this.value());

        final JsonNode jsonNode = this.toJsonNodeContext().toJsonNodeWithTypeList(list);

        assertEquals(list,
                this.fromJsonNodeContext().fromJsonNodeWithTypeList(jsonNode),
                () -> "roundtrip list: " + list + " -> json: " + jsonNode);
    }

    @Test
    public void testRoundtripTypeSet() {
        final Set<T> set = Set.of(this.value());

        final JsonNode jsonNode = this.toJsonNodeContext().toJsonNodeWithTypeSet(set);

        assertEquals(set,
                this.fromJsonNodeContext().fromJsonNodeWithTypeSet(jsonNode),
                () -> "roundtrip set: " + set + " -> json: " + jsonNode);
    }

    @Test
    public void testRoundtripTypeMap() {
        final Map<String, T> map = Map.of("key1", this.value());

        final JsonNode jsonNode = this.toJsonNodeContext().toJsonNodeWithTypeMap(map);

        assertEquals(map,
                this.fromJsonNodeContext().fromJsonNodeWithTypeMap(jsonNode),
                () -> "roundtrip marshall: " + map + " -> json: " + jsonNode);
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.marshaller(), this.typeName());
    }

    abstract M marshaller();

    abstract T value();

    abstract boolean requiresTypeName();

    abstract JsonNode node();

    final JsonNode nodeWithType() {
        final JsonNode node = this.node();
        return this.requiresTypeName() ?
                this.typeAndValue(node) :
                node;
    }

    final void fromJsonNodeFailed(final JsonNode node, final Class<? extends Throwable> wrapped) {
        final FromJsonNodeContext context = this.fromJsonNodeContext();

        if (NullPointerException.class == wrapped) {
            assertThrows(NullPointerException.class, () -> {
                this.marshaller().fromJsonNode(node, context);
            });
        } else {
            final FromJsonNodeException from = assertThrows(FromJsonNodeException.class, () -> {
                this.marshaller().fromJsonNode(node, context);
            });
            final Throwable cause = from.getCause();
            if (null == wrapped) {
                from.printStackTrace();
                Assertions.assertEquals(null, cause, "Cause");
            } else {
                if (wrapped != cause.getClass()) {
                    from.printStackTrace();
                    Assertions.assertEquals(wrapped, cause, "Wrong cause type");
                }
            }
        }
    }

    final void fromJsonNodeAndCheck(final JsonNode node, final T value) {
        this.fromJsonNodeAndCheck(this.marshaller(), node, value);
    }

    final void fromJsonNodeAndCheck(final BasicMarshaller<T> marshaller,
                                    final JsonNode node,
                                    final T value) {
        assertEquals(value,
                marshaller.fromJsonNode(node, this.fromJsonNodeContext()),
                () -> "fromJsonNode failed " + node);
    }

    final void fromJsonNodeWithTypeAndCheck(final JsonNode node,
                                            final T value) {
        assertEquals(value,
                this.fromJsonNodeContext().fromJsonNodeWithType(node),
                () -> "fromJsonNode failed " + node);
    }

    final void toJsonNodeWithTypeFailed(final T value,
                                        final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            this.marshaller().toJsonNodeWithType(value, this.toJsonNodeContext());
        });
    }

    final void toJsonNodeAndCheck(final T value,
                                  final JsonNode node) {
        this.toJsonNodeAndCheck(this.marshaller(), value, node);
    }

    final void toJsonNodeAndCheck(final BasicMarshaller<T> marshaller,
                                  final T value,
                                  final JsonNode node) {
        assertEquals(node,
                marshaller.toJsonNode(value, this.toJsonNodeContext()),
                () -> "toJsonNode failed " + node);
    }

    final void toJsonNodeWithTypeAndCheck(final T value,
                                          final JsonNode node) {
        this.toJsonNodeWithTypeAndCheck(this.marshaller(), value, node);
    }

    final void toJsonNodeWithTypeAndCheck(final BasicMarshaller<T> marshaller,
                                          final T value,
                                          final JsonNode node) {
        assertEquals(node,
                marshaller.toJsonNodeWithType(value, this.toJsonNodeContext()),
                () -> "toJsonNodeWithType failed " + node);
    }

    abstract String typeName();

    final JsonObjectNode typeAndValue(final JsonNode value) {
        return this.typeAndValue(this.typeName(), value);
    }

    final FromJsonNodeContext fromJsonNodeContext() {
        return BasicFromJsonNodeContext.INSTANCE;
    }

    final ToJsonNodeContext toJsonNodeContext() {
        return BasicToJsonNodeContext.with(this::postObjectProcessor);
    }

    private JsonObjectNode postObjectProcessor(final Object value, final JsonObjectNode object) {
        return object;
    }
}
