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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RelativeNodePointerTest extends NodePointerTestCase<RelativeNodePointer<JsonNode, JsonNodeName>> {

    private final static boolean NO_HASH = false;
    private final static boolean HASH = !NO_HASH;

    @Test
    public void testWithNegativeIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RelativeNodePointer.with(-1, NO_HASH);
        });
    }

    @Test
    public void testWithZero() {
        this.createAndCheck(0);
    }

    @Test
    public void testWith() {
        this.createAndCheck(1);
    }

    @Test
    public void testWith2() {
        this.createAndCheck(10);
    }

    private void createAndCheck(final int ancestorCount) {
        final RelativeNodePointer<JsonNode, JsonNodeName> pointer = RelativeNodePointer.with(ancestorCount, NO_HASH);
        assertEquals(ancestorCount, pointer.ancestorCount, "ancestorCount");
    }

    @Test
    public void testAdd() {
        final JsonNode value = JsonNode.string("added");

        final JsonArrayNode start = JsonNode.array()
                .appendChild(JsonNode.string("first"))
                .appendChild(JsonNode.string("replaced"));

        this.addAndCheck(NodePointer.relative(0, JsonNode.class)
                        .append(IndexedChildNodePointer.with(1)),
                start,
                value,
                start.set(1, value));
    }

    @Test
    public void testRemove() {
        final JsonArrayNode start = JsonNode.array()
                .appendChild(JsonNode.string("first"))
                .appendChild(JsonNode.string("removed"));

        this.removeAndCheck(NodePointer.relative(0, JsonNode.class)
                        .append(IndexedChildNodePointer.with(1)),
                start,
                start.remove(1));
    }

    @Test
    public final void testEqualsAncestor() {
        this.checkNotEquals(RelativeNodePointer.with(99, NO_HASH));
    }

    @Test
    public final void testEqualsHash() {
        this.checkNotEquals(RelativeNodePointer.with(1, ! NO_HASH));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(RelativeNodePointer.with(1, NO_HASH), "1");
    }

    @Test
    public void testToStringHash() {
        this.toStringAndCheck(RelativeNodePointer.with(1, HASH), "1#");
    }

    @Override
    RelativeNodePointer<JsonNode, JsonNodeName> createNodePointer() {
        return RelativeNodePointer.with(1, false);
    }

    @Override
    public Class<RelativeNodePointer<JsonNode, JsonNodeName>> type() {
        return Cast.to(RelativeNodePointer.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
