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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EmptyNodePatchTest extends NodePatchTestCase2<EmptyNodePatch<JsonNode, JsonNodeName>> {

    @Test
    public void testWithNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            EmptyNodePatch.get(null);
        });
    }

    @Test
    public void testWith() {
        final EmptyNodePatch<JsonNode, JsonNodeName> patch = EmptyNodePatch.get(JsonNode.class);
        assertNotNull(patch);
    }

    @Test
    public void testApply() {
        final JsonNode node = JsonNode.object()
                .set(this.property1(), JsonNode.object()
                        .set(this.property2(), this.value2()));
        this.applyAndCheck(this.createPatch(), node, node);
    }

    @Test
    public void testFromJsonNode() {
        this.fromJsonNodeAndCheck(JsonNode.array(),
                EmptyNodePatch.get(JsonNode.class));
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck(EmptyNodePatch.get(JsonNode.class),
                JsonNode.array());
    }

    @Test
    public void testToJsonPatch() {
        this.toJsonPatchAndCheck(EmptyNodePatch.get(JsonNode.class),
                JsonNode.array());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(EmptyNodePatch.get(JsonNode.class), "");
    }

    // HasJsonNode..................................................................................

    @Test
    public void testFromEmptyJsonObject() {
        this.fromJsonNodeAndCheck(JsonNode.array(),
                NodePatch.empty(JsonNode.class));
    }

    // NodePatchTestCase2..................................................................................

    @Override
    EmptyNodePatch<JsonNode, JsonNodeName> createPatch() {
        return EmptyNodePatch.get(JsonNode.class);
    }

    // ClassTesting2............................................................................

    @Override
    public Class<EmptyNodePatch<JsonNode, JsonNodeName>> type() {
        return Cast.to(EmptyNodePatch.class);
    }

    // TypeNameTesting.................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Empty";
    }
}
