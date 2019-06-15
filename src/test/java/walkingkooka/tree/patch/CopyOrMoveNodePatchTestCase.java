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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.pointer.NodePointer;

public abstract class CopyOrMoveNodePatchTestCase<P extends CopyOrMoveNodePatch<JsonNode, JsonNodeName>> extends NonEmptyNodePatchTestCase<P> {

    CopyOrMoveNodePatchTestCase() {
        super();
    }

    @Test
    public final void testFromPathUnknownFails() {
        this.applyFails(this.createPatch(),
                JsonNode.object());
    }

    @Test
    public final void testFromPathUnknownFails2() {
        this.applyFails(this.createPatch(),
                JsonNode.object().set(this.property3(), this.value3()));
    }

    @Test
    public final void testTargetPathUnknownFails() {
        this.applyFails(this.createPatch("/a1", "/b2/c3"),
                JsonNode.object().set(this.property3(), this.value3()));
    }

    @Test
    public final void testFromJsonNodeValueTypeFails() {
        this.fromJsonNodeFails2("[{\n" +
                "  \"op\": \"$OP\",\n" +
                "  \"value-type\": \"json-property-name\"\n" +
                "}]");
    }

    @Test
    public final void testFromJsonNodeValueFails() {
        this.fromJsonNodeFails2("[{\n" +
                "  \"op\": \"$OP\",\n" +
                "  \"value\": true\n" +
                "}]");
    }

    @Test
    public final void testFromJsonNode() {
        this.fromJsonNodeAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"from\": \"/b2\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]",
                this.createPatch());
    }

    @Test
    public final void testFromJsonNodePathNameTypeNotRequired() {
        this.fromJsonNodeAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"from\": \"/123\",\n" +
                        "  \"path\": \"/456\"\n" +
                        "}]",
                this.createPatch(this.pointer("/123"), this.pointer("/456")));
    }

    @Test
    public final void testToJsonNode() {
        this.toJsonNodeAndCheck2(this.createPatch(),
                "[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"from\": \"/b2\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]");
    }

    @Test
    public final void testToJsonNodePathNameTypeNotRequired() {
        this.toJsonNodeAndCheck2(this.createPatch(this.pointer("/123"), this.pointer("/456")),
                "[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"from\": \"/123\",\n" +
                        "  \"path\": \"/456\"\n" +
                        "}]");
    }

    @Test
    public final void testToJsonNodeRoundtrip() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .move(this.path2(), this.path3())
                .move(this.path3(), this.path1()));
    }

    // fromJsonPatch/toJsonPatch..........................................................................................

    @Test
    public final void testFromJsonPatch() {
        this.fromJsonPatchAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"from\": \"/b2\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]",
                this.createPatch());
    }

    @Test
    public final void testToJsonPatch() {
        this.toJsonPatchAndCheck2(this.createPatch(),
                "[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"from\": \"/b2\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]");
    }

    @Test
    public final void testEqualsDifferentFrom() {
        this.checkNotEquals(this.createPatch(this.path3(), this.path1()));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createPatch(), this.operation() + " from=\"/b2\" path=\"/a1\"");
    }

    abstract String operation();

    @Override
    final P createPatch(final NodePointer<JsonNode, JsonNodeName> path) {
        return this.createPatch(this.path2(), path);
    }

    final P createPatch(final String from,
                        final String path) {
        return this.createPatch(this.pointer(from), this.pointer(path));
    }

    abstract P createPatch(final NodePointer<JsonNode, JsonNodeName> from,
                           final NodePointer<JsonNode, JsonNodeName> path);
}
