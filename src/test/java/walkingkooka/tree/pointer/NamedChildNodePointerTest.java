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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.type.MemberVisibility;

public final class NamedChildNodePointerTest extends NodePointerTestCase<NamedChildNodePointer<JsonNode, JsonNodeName>> {

    private final static JsonNodeName A1 = JsonNodeName.with("A1");
    private final static JsonNodeName B2 = JsonNodeName.with("B2");

    private final static JsonNode A1_VALUE = JsonNode.string("a1-value");
    private final static JsonNode B2_VALUE = JsonNode.string("b2-value");

    // add..................................................................................................

    @Test
    public void testAddUnknownPathFails() {
        this.addAndFail(NodePointer.named(A1, JsonNode.class).append(NamedChildNodePointer.with(B2)),
                JsonNode.array(),
                A1_VALUE);
    }

    @Test
    public void testAddUnknownPathFails2() {
        this.addAndFail(NodePointer.named(A1, JsonNode.class).append(IndexedChildNodePointer.with(99)),
                JsonNode.object(),
                A1_VALUE);
    }

    @Test
    public void testAddSameProperty() {
        final JsonObjectNode start = JsonNode.object()
                .set(A1, A1_VALUE);

        this.addAndCheck(NamedChildNodePointer.with(A1),
                start,
                A1_VALUE,
                start);
    }

    @Test
    public void testAddNewProperty() {
        final JsonObjectNode start = JsonNode.object()
                .set(A1, A1_VALUE);

        this.addAndCheck(NamedChildNodePointer.with(B2),
                start,
                B2_VALUE,
                start.set(B2, B2_VALUE));
    }

    @Test
    public void testAddReplaces() {
        final JsonNode oldB2 = JsonNode.string("b2-old-value");

        final JsonObjectNode start = JsonNode.object()
                .set(A1, A1_VALUE)
                .set(B2, oldB2);

        final JsonNode replacedB2 = JsonNode.string("b2-replaced-value");

        this.addAndCheck(NamedChildNodePointer.with(B2),
                start,
                replacedB2,
                start.set(B2, replacedB2));
    }

    // remove..................................................................................................

    @Test
    public void testRemoveUnknownPathFails() {
        this.removeAndFail(NamedChildNodePointer.with(A1),
                JsonNode.array());
    }

    @Test
    public void testRemoveUnknownPathFails2() {
        this.removeAndFail(NamedChildNodePointer.with(A1),
                JsonNode.object()
                        .set(B2, B2_VALUE));
    }

    @Test
    public void testRemoveChild() {
        this.removeAndCheck2(JsonNode.object()
                        .set(A1, A1_VALUE),
                A1);

    }

    @Test
    public void testRemoveChild2() {
        this.removeAndCheck2(JsonNode.object()
                        .set(A1, A1_VALUE)
                        .set(B2, B2_VALUE),
                B2);

    }

    private void removeAndCheck2(final JsonObjectNode node,
                                 final JsonNodeName name) {
        this.removeAndCheck(NamedChildNodePointer.with(name),
                node,
                node.remove(name));
    }

    @Test
    public final void testEqualsDifferentName() {
        this.checkNotEquals(NamedChildNodePointer.with(JsonNodeName.with("different")));
    }
    
    // toString................................................................................

    @Test
    public void testToStringWithSlash() {
        this.toStringAndCheck(NamedChildNodePointer.with(JsonNodeName.with("slash/")), "/slash~1");
    }

    @Test
    public void testToStringWithTilde() {
        this.toStringAndCheck(NamedChildNodePointer.with(JsonNodeName.with("tilde~")), "/tilde~0");
    }

    @Override
    NamedChildNodePointer<JsonNode, JsonNodeName> createNodePointer() {
        return NamedChildNodePointer.with(JsonNodeName.with("someProperty"));
    }

    @Override
    public Class<NamedChildNodePointer<JsonNode, JsonNodeName>> type() {
        return Cast.to(NamedChildNodePointer.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
