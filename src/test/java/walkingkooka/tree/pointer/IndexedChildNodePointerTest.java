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

public final class IndexedChildNodePointerTest extends NodePointerTestCase2<IndexedChildNodePointer<JsonNode, JsonNodeName>> {

    // add..................................................................................................

    @Test
    public void testAddUnknownPathFails() {
        this.addAndFail(NodePointer.indexed(1, JsonNode.class).appendToLast(IndexedChildNodePointer.with(99)),
                JsonNode.array(),
                JsonNode.string("!"));
    }

    @Test
    public void testAddUnknownPathFails2() {
        this.addAndFail(NodePointer.indexed(1, JsonNode.class).appendToLast(IndexedChildNodePointer.with(99)),
                JsonNode.object(),
                JsonNode.string("!"));
    }

    @Test
    public void testAddReplaceFirstChild() {
        this.addAndCheck2(0);
    }

    @Test
    public void testAddReplaceChild() {
        this.addAndCheck2(1);
    }

    @Test
    public void testAddReplaceLastChild() {
        this.addAndCheck2(2);
    }

    private void addAndCheck2(final int index) {
        final JsonArrayNode start = JsonNode.array()
                .appendChild(JsonNode.string("value-0a"))
                .appendChild(JsonNode.string("value-1b"))
                .appendChild(JsonNode.string("value-2c"));

        final JsonNode add = JsonNode.string("add");

        this.addAndCheck(IndexedChildNodePointer.with(index),
                start,
                add,
                start.set(index, add));
    }

    // remove..................................................................................................

    @Test
    public void testRemoveUnknownPathFails() {
        this.removeAndFail(IndexedChildNodePointer.with(0),
                JsonNode.object());
    }

    @Test
    public void testRemoveUnknownPathFails2() {
        this.removeAndFail(IndexedChildNodePointer.with(1),
                JsonNode.array().appendChild(JsonNode.string("a")));
    }

    @Test
    public void testRemoveChildIndex0() {
        this.removeAndCheck2(JsonNode.array()
                        .appendChild(JsonNode.string("a1"))
                        .appendChild(JsonNode.string("b2")),
                0);

    }

    @Test
    public void testRemoveChildIndex1() {
        this.removeAndCheck2(JsonNode.array()
                        .appendChild(JsonNode.string("a1"))
                        .appendChild(JsonNode.string("b2")),
                1);

    }

    @Test
    public void testRemoveChildIndex2() {
        this.removeAndCheck2(JsonNode.array()
                        .appendChild(JsonNode.string("a1"))
                        .appendChild(JsonNode.string("b2"))
                        .appendChild(JsonNode.string("c3")),
                2);

    }

    private void removeAndCheck2(final JsonArrayNode node,
                                 final int index) {
        this.removeAndCheck(IndexedChildNodePointer.with(index),
                node,
                node.removeChild(index));
    }

    @Test
    public final void testEqualsDifferentIndex() {
        this.checkNotEquals(IndexedChildNodePointer.with(99));
    }

    @Override
    IndexedChildNodePointer<JsonNode, JsonNodeName> createNodePointer() {
        return IndexedChildNodePointer.with(1);
    }

    @Override
    public Class<IndexedChildNodePointer<JsonNode, JsonNodeName>> type() {
        return Cast.to(IndexedChildNodePointer.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
