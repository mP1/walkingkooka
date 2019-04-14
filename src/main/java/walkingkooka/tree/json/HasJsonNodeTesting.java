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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface HasJsonNodeTesting<H extends HasJsonNode> {

    @Test
    default void testHasJsonNodeFactoryRegistered() throws Exception{
        final Class<H> type = this.type();
        final String typeName = this.type().getName();

        if (type.isEnum()) {
            final Object[] values = Cast.to(type.getMethod("values").invoke(null));
            assertEquals(Lists.empty(),
                    Arrays.stream(values)
                            .filter(e -> HasJsonNodeMapper.TYPENAME_TO_FACTORY.get(e.getClass().getName()) == null)
                            .collect(Collectors.toList()),
                    () -> "Not all enum: " + typeName + " value types not registered -> HasJsonNode.register()=" + HasJsonNodeMapper.TYPENAME_TO_FACTORY);

        } else {
            assertNotEquals(
                    null,
                    HasJsonNodeMapper.TYPENAME_TO_FACTORY.get(typeName),
                    () -> "Type: " + typeName + " factory not registered -> HasJsonNode.register()=" + HasJsonNodeMapper.TYPENAME_TO_FACTORY);
        }
    }

    @Test
    default void testTypeNameFromClass() {
        final H has = this.createHasJsonNode();
        final JsonNode node = has.toJsonNodeWithType();
        if (node.isObject()) {
            assertEquals(node.objectOrFail().get(JsonObjectNode.TYPE).map(n -> n.removeParent()),
                    HasJsonNode.typeName(has.getClass()),
                    () -> has + " & " + node);
        }
    }

    @Test
    default void testFromJsonNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.fromJsonNode(null);
        });
    }

    default void fromJsonNodeAndCheck(final JsonNode from, final H has) {
        assertEquals(has,
                this.fromJsonNode(from),
                () -> "fromJsonNode failed " + from);
    }

    default void fromJsonNodeFails(final JsonNode from) {
        assertThrows(IllegalArgumentException.class, () -> {
            this.fromJsonNode(from);
        });
    }

    @Test
    default void testToJsonNodeRoundtripTwice() {
        this.toJsonNodeRoundTripTwiceAndCheck(this.createHasJsonNode());
    }

    @Test
    default void testToJsonNodeWithTypeRoundtripTwice() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createHasJsonNode());
    }

    @Test
    default void testToJsonNodeRoundtripList() {
        final List<HasJsonNode> list = Lists.of(this.createHasJsonNode());

        assertEquals(list,
                HasJsonNode.toJsonNodeWithType(list)
                        .fromJsonNodeWithType(),
                () -> "Roundtrip to -> from -> to failed list=" + list);
    }

    @Test
    default void testToJsonNodeRoundtripSet() {
        final Set<HasJsonNode> set = Sets.of(this.createHasJsonNode());

        assertEquals(set,
                HasJsonNode.toJsonNodeWithType(set)
                        .fromJsonNodeWithType(),
                () -> "Roundtrip to -> from -> to failed set=" + set);
    }

    @Test
    default void testToJsonNodeRoundtripMap() {
        final Map<String, HasJsonNode> map = Maps.of("key123", this.createHasJsonNode());

        assertEquals(map,
                HasJsonNode.toJsonNodeWithType(map)
                        .fromJsonNodeWithType(),
                () -> "Roundtrip to -> from -> to failed map=" + map);
    }

    /**
     * Typically calls a static method that accepts a {@link JsonNode} and creates a {@link H object}.
     */
    H fromJsonNode(final JsonNode from);

    default void toJsonNodeAndCheck(final HasJsonNode has, final String json) {
        toJsonNodeAndCheck(has, JsonNode.parse(json));
    }

    default void toJsonNodeAndCheck(final HasJsonNode has, final JsonNode json) {
        assertEquals(json,
                has.toJsonNode(),
                () -> "toJsonNode doesnt match=" + has);
    }

    default void toJsonNodeRoundTripTwiceAndCheck(final HasJsonNode has) {
        final JsonNode jsonNode = has.toJsonNode();

        final HasJsonNode has2 = this.fromJsonNode(jsonNode);
        final JsonNode jsonNode2 = has2.toJsonNode();

        assertEquals(has2,
                this.fromJsonNode(jsonNode2),
                () -> "Roundtrip to -> from -> to failed has=" + has);
    }

    default void toJsonNodeWithTypeRoundTripTwiceAndCheck(final HasJsonNode has) {
        final JsonNode jsonNode = has.toJsonNodeWithType();

        final HasJsonNode has2 = jsonNode.fromJsonNodeWithType();
        final JsonNode jsonNode2 = HasJsonNode.toJsonNodeWithType(has2);

        assertEquals(has2,
                jsonNode2.fromJsonNodeWithType(),
                () -> "Roundtrip to -> from -> to failed has=" + has);

        assertEquals(has2,
                HasJsonNodeMapper.fromJsonNodeWithType(jsonNode2),
                () -> "HasJsonNodeMapper roundtrip to -> from -> to failed has=" + has);
    }

    H createHasJsonNode();

    Class<H> type();
}
