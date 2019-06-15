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

public abstract class AddReplaceOrTestNodePatchTestCase<P extends AddReplaceOrTestNodePatch<JsonNode, JsonNodeName>> extends NonEmptyNodePatchTestCase<P> {

    AddReplaceOrTestNodePatchTestCase() {
        super();
    }

    @Test
    public final void testEqualsDifferentValue() {
        this.checkNotEquals(this.createPatch(this.path1(), JsonNode.string("different")));
    }

    @Test
    public final void testFromJsonNodeFromPropertyFails() {
        this.fromJsonNodeFails2("[{\"op\": \"$OP\", \"from\": \"/A1\", \"path\": \"/a1\", \"value-type\": \"json-string\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testFromJsonNodePathNameTypeMissingFails() {
        this.fromJsonNodeFails2("[{\"op\": \"$OP\", \"path\": \"/a1\", \"value-type\": \"json-string\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testFromJsonNodePathMissingFails() {
        this.fromJsonNodeFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"value-type\": \"json-string\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testFromJsonNodePathInvalidFails() {
        this.fromJsonNodeFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"!!!\", \"value-type\": \"json-string\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testFromJsonNodeValueTypeMissingFails() {
        this.fromJsonNodeFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/a1\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testFromJsonNodeValueMissingFails() {
        this.fromJsonNodeFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/a1\", \"value-type\": \"json-string\"}]");
    }

    @Test
    public final void testFromJsonNode() {
        this.fromJsonNodeAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/a1\",\n" +
                        "  \"value-type\": \"json-string\",\n" +
                        "  \"value\": \"value1\"\n" +
                        "}]",
                this.createPatch());
    }

    @Test
    public final void testFromJsonNode2() {
        this.fromJsonNodeAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/b2\",\n" +
                        "  \"value-type\": \"json-string\",\n" +
                        "  \"value\": \"value2\"\n" +
                        "}]",
                this.createPatch(this.path2(), this.value2()));
    }

    @Test
    public final void testFromJsonNode3() {
        this.fromJsonNodeAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/b2\",\n" +
                        "  \"value\": \"value2\",\n" +
                        "  \"value-type\": \"json-string\"\n" +
                        "}]",
                this.createPatch(this.path2(), this.value2()));
    }

    @Test
    public final void testFromJsonNodeMissingPathNameType() {
        this.fromJsonNodeAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/123\",\n" +
                        "  \"value-type\": \"json-string\",\n" +
                        "  \"value\": \"value1\"\n" +
                        "}]",
                this.createPatch(NodePointer.indexed(123, JsonNode.class)));
    }

    @Test
    public final void testToJsonNode() {
        this.toJsonNodeAndCheck2(this.createPatch(),
                "[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/a1\", \"value-type\": \"json-string\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testToJsonNode2() {
        this.toJsonNodeAndCheck2(this.createPatch(this.path2(), this.value2()),
                "[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/b2\", \"value-type\": \"json-string\", \"value\": \"value2\"}]");
    }

    @Test
    public final void testToJsonNodeRoundtrip() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .add(this.path2(), this.value2()));
    }

    @Test
    public final void testToJsonNodeRoundtrip2() {
        this.toJsonNodeWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .add(this.path2(), this.value2())
                .add(this.path3(), this.value3()));
    }

    // fromJsonPatch/toJsonPatch..........................................................................................

    @Test
    public final void testFromJsonPatch() {
        this.fromJsonPatchAndCheck2("[{\"op\": \"$OP\", \"path\": \"/a1\", \"value\": \"value1\"}]",
                this.createPatch());
    }

    @Test
    public final void testToJsonPatch() {
        this.toJsonPatchAndCheck2(this.createPatch(),
                "[{\"op\": \"$OP\", \"path\": \"/a1\", \"value\": \"value1\"}]");
    }

    @Override
    final P createPatch(final NodePointer<JsonNode, JsonNodeName> path) {
        return this.createPatch(path, this.value1());
    }

    abstract P createPatch(final NodePointer<JsonNode, JsonNodeName> path, final JsonNode value);
}
