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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HasJsonNodeMapperTestCase2<M extends HasJsonNodeMapper<T>, T> extends HasJsonNodeMapperTestCase<M, T>
        implements ToStringTesting<M> {

    HasJsonNodeMapperTestCase2() {
        super();
    }

    @Test
    public void testFromJsonNodeNullFails() {
        this.fromJsonNodeFailed(null, NullPointerException.class);
    }

    @Test
    public final void testFromJsonNodeJsonNullNode() {
        this.fromJsonNodeAndCheck(JsonNode.nullNode(), null);
    }

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
    public final void testToJsonNodeWithType2() {
        this.toJsonNodeWithTypeObjectAndCheck(this.value(), this.nodeWithType());
    }

    @Test
    public final void testRoundtripMapperToJsonNodeWithTypeFromJsonNodeWithType() {
        final T value = this.value();

        final JsonNode json = this.mapper().toJsonNodeWithType(value);

        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(json),
                () -> "roundtrip starting with value failed fromValue: " + value + " -> json: " + json);
    }

    @Test
    public final void testRoundtripFromJsonNodeWithTypeMapperToJsonNodeWithType() {
        final JsonNode json = this.nodeWithType();

        final T value = HasJsonNodeMapper.fromJsonNodeWithType(json);

        assertEquals(json,
                this.mapper().toJsonNodeWithType(value),
                () -> "roundtrip starting with node failed, json: " + json + " -> value:: " + value);
    }

    @Test
    public final void testRoundtripToJsonNodeWithTypeObjectFromJsonNodeWithType() {
        final T value = this.value();

        final JsonNode json = HasJsonNodeMapper.toJsonNodeWithTypeObject(value);

        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(json),
                () -> "roundtrip starting with value failed, value: " + value + " -> json: " + json);
    }

    /**
     * Overridden to ignore by List, Map, Set.
     */
    @Test
    public void testRoundtripToJsonNodeObjectFromJsonNodeWithType() {
        final T value = this.value();
        final Class<?> valueType = value.getClass();
        final JsonNode json = HasJsonNodeMapper.toJsonNodeObject(value);

        assertEquals(value,
                HasJsonNodeMapper.fromJsonNode(json, valueType),
                () -> "roundtrip starting with value failed, value: " + value + " " + valueType.getName() + " -> json: " + json);
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.mapper(), this.typeName());
    }

    abstract M mapper();

    abstract T value();

    abstract boolean requiresTypeName();

    abstract JsonNode node();

    final JsonNode nodeWithType() {
        final JsonNode node = this.node();
        return this.requiresTypeName() ?
                this.typeAndValue(node) :
                node;
    }

    final void fromJsonNodeFailed(final JsonNode node, final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            this.mapper().fromJsonNode(node);
        });
    }

    final void fromJsonNodeAndCheck(final JsonNode node, final T value) {
        this.fromJsonNodeAndCheck(this.mapper(), node, value);
    }

    final void fromJsonNodeAndCheck(final HasJsonNodeMapper<T> mapper,
                                    final JsonNode node,
                                    final T value) {
        assertEquals(value,
                mapper.fromJsonNode(node),
                () -> "fromJsonNode failed " + node);
    }

    final void fromJsonNodeWithTypeAndCheck(final JsonNode node,
                                            final T value) {
        assertEquals(value,
                HasJsonNode.fromJsonNodeWithType(node),
                () -> "fromJsonNode failed " + node);
    }

    final void toJsonNodeWithTypeFailed(final T value,
                                        final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            this.mapper().toJsonNodeWithType(value);
        });
    }

    final void toJsonNodeAndCheck(final T value,
                                  final JsonNode node) {
        this.toJsonNodeAndCheck(this.mapper(), value, node);
    }

    final void toJsonNodeAndCheck(final HasJsonNodeMapper<T> mapper,
                                  final T value,
                                  final JsonNode node) {
        assertEquals(node,
                mapper.toJsonNode(value),
                () -> "toJsonNode failed " + node);
    }

    final void toJsonNodeWithTypeAndCheck(final T value,
                                          final JsonNode node) {
        this.toJsonNodeWithTypeAndCheck(this.mapper(), value, node);
    }

    final void toJsonNodeWithTypeAndCheck(final HasJsonNodeMapper<T> mapper,
                                          final T value,
                                          final JsonNode node) {
        assertEquals(node,
                mapper.toJsonNodeWithType(value),
                () -> "toJsonNodeWithType failed " + node);
    }

    final void toJsonNodeWithTypeObjectAndCheck(final T value,
                                                final JsonNode node) {
        assertEquals(node,
                HasJsonNodeMapMapper.toJsonNodeWithTypeObject(value),
                () -> "HasJsonNode.toJsonNodeWithTypeObject failed " + node);
    }

    abstract String typeName();

    final JsonObjectNode typeAndValue(final JsonNode value) {
        return this.typeAndValue(this.typeName(), value);
    }

    final JsonObjectNode typeAndValue(final String typeName, final JsonNode value) {
        return JsonNode.object()
                .set(HasJsonNodeMapper.TYPE, JsonNode.string(typeName))
                .set(HasJsonNodeMapper.VALUE, value);
    }
}
