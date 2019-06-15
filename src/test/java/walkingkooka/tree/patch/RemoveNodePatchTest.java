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

public final class RemoveNodePatchTest extends NonEmptyNodePatchTestCase<RemoveNodePatch<JsonNode, JsonNodeName>> {

    @Test
    public void testPathUnknownFails() {
        this.applyFails(this.createPatch("/a1/b2/c3"),
                JsonNode.object());
    }

    @Test
    public void testPathUnknownFails2() {
        this.applyFails(this.createPatch("/a1/b2/c3"),
                "{\"a1\": {}}");
    }

    @Test
    public void testRemoveChild() {
        this.applyAndCheck(this.createPatch(),
                JsonNode.object()
                        .set(this.property1(), this.value1()),
                JsonNode.object());
    }

    @Test
    public void testRemoveTwoChildren() {
        this.applyAndCheck(this.createPatch().append0(this.createPatch(this.property2())),
                "{\"a1\":\"value1\", \"b2\": \"value2\", \"c3\": \"value3\"}",
                "{\"c3\": \"value3\"}");
    }

    @Test
    public void testRemoveGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2"),
                "{\"a1\": {\"b2\": \"value2\"}}",
                "{\"a1\": {}}");
    }

    @Test
    public void testRemoveGreatGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2/c3"),
                "{\"a1\": {\"b2\": {\"c3\": \"value3\"}}}",
                "{\"a1\": { \"b2\": {}}}");
    }

    @Test
    public void testRemoveMultiStep() {
        this.applyAndCheck(this.createPatch("/a1/b2").append0(this.createPatch("/a1")),
                "{\"a1\": {\"b2\": \"value2\"}, \"c3\": \"value3\"}",
                "{\"c3\": \"value3\"}");
    }

    @Test
    public void testFromJsonNodeRequiredPathNameTypeMissingFails() {
        this.fromJsonNodeAndCheck("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]",
                this.createPatch());
    }

    // HasJsonNode..................................................................................................

    @Test
    public void testFromJsonNodeFromPropertyFails() {
        this.fromJsonNodeFails("[{\n" +
                "  \"op\": \"remove\",\n" +
                "  \"from\": \"/123\"\n" +
                "}]");
    }

    @Test
    public void testFromJsonNodeValueTypePropertyFails() {
        this.fromJsonNodeFails("[{\n" +
                "  \"op\": \"remove\",\n" +
                "  \"value-type\": \"json-property-name\"\n" +
                "}]");
    }

    @Test
    public void testFromJsonNodeValuePropertyFails() {
        this.fromJsonNodeFails("[{\n" +
                "  \"op\": \"remove\",\n" +
                "  \"value\": true\n" +
                "}]");
    }

    @Test
    public void testFromJsonNodePathNameTypeMissing() {
        this.fromJsonNodeAndCheck("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/123\"\n" +
                        "}]",
                this.createPatch(NodePointer.indexed(123, JsonNode.class)));
    }

    @Test
    public void testToJsonNodePathNameTypeNotRequired() {
        this.toJsonNodeAndCheck(this.createPatch(NodePointer.indexed(123, JsonNode.class)),
                "[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path\": \"/123\"\n" +
                        "}]");
    }

    @Test
    public void testToJsonNodePathNameTypeRequired() {
        this.toJsonNodeAndCheck(this.createPatch(NodePointer.named(JsonNodeName.with("abc"), JsonNode.class)),
                "[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/abc\"\n" +
                        "}]");
    }

    @Test
    public void testToJsonNodePathNameTypeRequired2() {
        this.toJsonNodeAndCheck(this.createPatch(NodePointer.named(JsonNodeName.with("abc"), JsonNode.class)),
                "[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path\": \"/abc\",\n" +
                        "  \"path-name-type\": \"json-property-name\"\n" +
                        "}]");
    }

    @Test
    public void testToJsonNodeRoundtrip() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .remove(this.path2()));
    }

    @Test
    public void testToJsonNodeRoundtrip2() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .remove(this.path2())
                .remove(this.path3()));
    }

    // fromJsonPatch/toJsonPatch..........................................................................................

    @Test
    public final void testFromJsonPatch() {
        this.fromJsonPatchAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]",
                this.createPatch());
    }

    @Test
    public final void testToJsonPatch() {
        this.toJsonPatchAndCheck2(this.createPatch(),
                "[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]");
    }

    @Test
    public final void testToJsonPatchFromJsonPatch() {

    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPatch(), "remove path=\"/a1\"");
    }

    @Override
    RemoveNodePatch<JsonNode, JsonNodeName> createPatch(final NodePointer<JsonNode, JsonNodeName> path) {
        return RemoveNodePatch.with(path);
    }

    private RemoveNodePatch<JsonNode, JsonNodeName> createPatch(final JsonNodeName property) {
        return RemoveNodePatch.with(NodePointer.named(property, JsonNode.class));
    }

    private RemoveNodePatch<JsonNode, JsonNodeName> createPatch(final String path) {
        return RemoveNodePatch.with(this.pointer(path));
    }

    @Override
    String operation() {
        return "remove";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<RemoveNodePatch<JsonNode, JsonNodeName>> type() {
        return Cast.to(RemoveNodePatch.class);
    }

    // TypeNameTesting.................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Remove";
    }
}
