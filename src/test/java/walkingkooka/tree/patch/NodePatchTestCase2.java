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

package walkingkooka.tree.patch;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodePatchTestCase2<P extends NodePatch<JsonNode, JsonNodeName>> extends NodePatchTestCase<P>
        implements HashCodeEqualsDefinedTesting<P>,
        HasJsonNodeTesting<P>,
        TypeNameTesting<P> {

    NodePatchTestCase2() {
        super();
    }

    @Test
    public final void testApplyNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().apply(null);
        });
    }

    @Test
    public final void testAddNullPathFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().add(null, JsonNode.string("value"));
        });
    }

    @Test
    public final void testAddNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().add(this.path(), null);
        });
    }

    @Test
    public final void testCopyNullFromFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().copy(null, this.path());
        });
    }

    @Test
    public final void testCopyNullPathFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().copy(this.path(), null);
        });
    }

    @Test
    public final void testCopyFromSameAsPathFails() {
        final NodePointer<JsonNode, JsonNodeName> path = this.path();

        assertThrows(IllegalArgumentException.class, () -> {
            this.createPatch().copy(path, path);
        });
    }

    @Test
    public final void testMoveNullFromFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().move(null, this.path());
        });
    }

    @Test
    public final void testMoveNullPathFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().move(this.path(), null);
        });
    }

    @Test
    public final void testMoveFromSameAsPathFails() {
        final NodePointer<JsonNode, JsonNodeName> path = this.path();

        assertThrows(IllegalArgumentException.class, () -> {
            this.createPatch().move(path, path);
        });
    }

    @Test
    public final void testReplaceNullPathFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().replace(null, this.value());
        });
    }

    @Test
    public final void testReplaceNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().replace(this.path(), null);
        });
    }

    @Test
    public final void testRemoveNullFromFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPatch().remove(null);
        });
    }

    // HasJsonNode..................................................................................

    @Test
    public final void testFromJsonBooleanNodeFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public final void testFromJsonNullNodeFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public final void testFromJsonNumberNodeFails() {
        this.fromJsonNodeFails(JsonNode.number(123));
    }

    @Test
    public final void testFromJsonObjectNodeFails() {
        this.fromJsonNodeFails(JsonNode.object());
    }

    @Test
    public final void testFromJsonStringNodeFails() {
        this.fromJsonNodeFails(JsonNode.string("string123"));
    }

    final void toJsonPatchAndCheck(final NodePatch<JsonNode, JsonNodeName> patch,
                                   final String json) {
        this.toJsonPatchAndCheck(patch,
                JsonNode.parse(json));
    }

    final void toJsonPatchAndCheck(final NodePatch<JsonNode, JsonNodeName> patch,
                                   final JsonNode node) {
        assertEquals(node,
                patch.toJsonPatch(),
                () -> patch + " toJsonPatch");
    }

    // NodePatchTestCase2..................................................................................

    abstract P createPatch();

    @Override
    public final P createObject() {
        return this.createPatch();
    }

    private NodePointer<JsonNode, JsonNodeName> path() {
        return NodePointer.any(JsonNode.class);
    }

    private JsonNode value() {
        return JsonNode.nullNode();
    }

    // ClassTesting.................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HasJsonNodeTesting.................................................................................

    @Override
    public final P fromJsonNode(final JsonNode from) {
        return Cast.to(NodePatch.fromJsonNode(from));
    }

    @Override
    public final P createHasJsonNode() {
        return this.createPatch();
    }

    // TypeNameTesting.................................................................................

    @Override
    public final String typeNameSuffix() {
        return NodePatch.class.getSimpleName();
    }
}
