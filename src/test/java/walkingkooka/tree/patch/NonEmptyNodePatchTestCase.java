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

package walkingkooka.tree.patch;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.pointer.NodePointer;

public abstract class NonEmptyNodePatchTestCase<P extends NonEmptyNodePatch<JsonNode, JsonNodeName>> extends NodePatchTestCase2<P> {

    NonEmptyNodePatchTestCase() {
        super();
    }

    @Test
    public final void testEqualsEmpty() {
        this.checkNotEquals(NodePatch.empty(JsonNode.class));
    }

    @Test
    public final void testEqualsDifferentPath() {
        this.checkNotEquals(this.createPatch(this.path3()));
    }

    @Test
    public final void testEqualsWithNext() {
        final NodePointer<JsonNode, JsonNodeName> path2 = this.path3();
        final JsonNode value2 = this.value2();

        this.checkEquals(this.createPatch().add(path2, value2),
                this.createPatch().add(path2, value2));
    }

    @Test
    public final void testEqualsWithDifferentNext() {
        final NodePointer<JsonNode, JsonNodeName> path2 = this.path3();
        this.checkNotEquals(this.createPatch().add(path2, this.value2()),
                this.createPatch().add(path2, this.value3()));
    }

    @Test
    public final void testFromJsonUnknownPropertyFails() {
        this.fromJsonNodeFails2("[{\n" +
                "  \"op\": \"$OP\",\n" +
                "  \"unexpected-property\": \"fromJson must fail!\"\n" +
                "}]");
    }

    @Override
    final P createPatch() {
        return this.createPatch(this.path1());
    }

    abstract P createPatch(final NodePointer<JsonNode, JsonNodeName> path);

    final void fromJsonNodeAndCheck2(final String json,
                                     final NodePatch<JsonNode, JsonNodeName> patch) {
        this.fromJsonNodeAndCheck(json.replace("$OP", this.operation()),
                patch);
    }

    final void fromJsonNodeFails2(final String json) {
        this.fromJsonNodeFails(json.replace("$OP", this.operation()));
    }

    final void toJsonNodeAndCheck2(final NodePatch<JsonNode, JsonNodeName> patch,
                                   final String json) {
        this.toJsonNodeAndCheck(patch, json.replace("$OP", this.operation()));
    }

    abstract String operation();
}
