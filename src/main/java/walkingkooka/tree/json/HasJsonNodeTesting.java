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
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface HasJsonNodeTesting<H extends HasJsonNode> {

    @Test
    default void testHasJsonNodeFactoryRegistered() {
        final String type = this.type().getName();

        assertNotEquals(
                null,
                HasJsonNode2.TYPENAME_TO_FACTORY.get(type),
                ()->"Type: " + type + " factory not registered -> HasJsonNode.register()=" + HasJsonNode2.TYPENAME_TO_FACTORY);
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
    default void testToJsonNodeRoundtripTwiceList() {
        final List<HasJsonNode> list = Lists.of(this.createHasJsonNode());

        assertEquals(
                list,
                HasJsonNode.fromJsonNodeWithType(HasJsonNode2.toJsonNodeWithType(list)),
                () -> "Roundtrip to -> from -> to failed list=" + list);
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
        final JsonNode jsonNode = has.toJsonNode();

        final HasJsonNode has2 = this.fromJsonNode(jsonNode);
        final JsonNode jsonNode2 = has2.toJsonNodeWithType();

        assertEquals(has2,
                HasJsonNode.fromJsonNodeWithType(jsonNode2),
                () -> "Roundtrip to -> from -> to failed has=" + has);
    }

    H createHasJsonNode();

    Class<H> type();
}
