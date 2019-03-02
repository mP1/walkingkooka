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
    public final void testToJsonNodeNull() {
        this.toJsonNodeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public final void testToJsonNode() {
        this.toJsonNodeAndCheck(this.value(), this.nodeWithType());
    }

    @Test
    public final void testToJsonNodeWithType() {
        this.toJsonNodeWithTypeAndCheck(this.value(), this.nodeWithType());
    }

    @Test
    public final void testRoundtripValue() {
        final T value = this.value();

        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(this.mapper().toJsonNode(value)),
                () -> "rountrip starting with value failed " + value);
    }

    @Test
    public final void testRoundtripNodeWithType() {
        final JsonNode maybe = this.nodeWithType();

        assertEquals(maybe,
                this.mapper().toJsonNode(HasJsonNodeMapper.fromJsonNodeWithType(maybe)),
                () -> "rountrip starting with node failed " + this.node());
    }

    @Test
    public final void testRoundtrip() {
        final T value = this.value();

        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(HasJsonNodeMapper.toJsonNodeWithType(value)),
                () -> "rountrip starting with value failed " + value);
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

    final void toJsonNodeFailed(final T value, final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            this.mapper().toJsonNode(value);
        });
    }

    final void toJsonNodeAndCheck(final T value, final JsonNode node) {
        this.toJsonNodeAndCheck(this.mapper(), value, node);
    }

    final void toJsonNodeAndCheck(final HasJsonNodeMapper<T> mapper,
                                  final T value,
                                  final JsonNode node) {
        assertEquals(node,
                mapper.toJsonNode(value),
                () -> "toJsonNode failed " + node);
    }

    final void toJsonNodeWithTypeAndCheck(final T value, final JsonNode node) {
        assertEquals(node,
                HasJsonNode.toJsonNodeWithType(value),
                () -> "HasJsonNode.toJsonNodeWithType failed " + node);
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
