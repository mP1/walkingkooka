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
import walkingkooka.tree.pointer.NodePointer;

public final class AddNodePatchTest extends AddReplaceOrTestNodePatchTestCase<AddNodePatch<JsonNode, JsonNodeName>> {

    @Test
    public void testPathUnknownFails() {
        this.applyFails(this.createPatch("/1/1/1", this.value1()),
                JsonNode.object());
    }

    @Test
    public void testAddChild() {
        final JsonNode object = JsonNode.object()
                .set(this.property1(), this.value1());

        this.applyAndCheck(this.createPatch(),
                object,
                object);
    }

    @Test
    public void testAddTwoChildren() {
        this.applyAndCheck(this.createPatch().append0(this.createPatch(this.property2(), this.value2())),
                "{}",
                "{\"a1\":\"value1\", \"b2\": \"value2\"}");
    }

    @Test
    public void testAddGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2", this.value2()),
                "{\"a1\": {}}",
                "{\"a1\": {\"b2\": \"value2\"}}");
    }

    @Test
    public void testAddGreatGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2/c3", this.value3()),
                "{\"a1\": {\"b2\":{}}}",
                "{\"a1\": {\"b2\": {\"c3\": \"value3\"}}}");
    }

    @Test
    public void testAddMultiStep() {
        this.applyAndCheck(this.createPatch("/a1", JsonNode.object()).append0(this.createPatch("/a1/b2", this.value2())),
                "{}",
                "{\"a1\": {\"b2\": \"value2\"}}");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPatch(), "add path=\"/a1\" value=\"value1\"");
    }

    @Override
    AddNodePatch<JsonNode, JsonNodeName> createPatch(final NodePointer<JsonNode, JsonNodeName> path, JsonNode value) {
        return AddNodePatch.with(path, value);
    }

    private AddNodePatch<JsonNode, JsonNodeName> createPatch(final JsonNodeName property, final JsonNode value) {
        return AddNodePatch.with(NodePointer.named(property, JsonNode.class), value);
    }

    private AddNodePatch<JsonNode, JsonNodeName> createPatch(final String path, final JsonNode value) {
        return AddNodePatch.with(this.pointer(path), value);
    }

    @Override
    String operation() {
        return "add";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<AddNodePatch<JsonNode, JsonNodeName>> type() {
        return Cast.to(AddNodePatch.class);
    }

    // TypeNameTesting.................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Add";
    }
}
