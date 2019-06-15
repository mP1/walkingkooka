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
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.pointer.NodePointer;

public final class TestNodePatchTest extends AddReplaceOrTestNodePatchTestCase<TestNodePatch<JsonNode, JsonNodeName>> {

    @Test
    public void testPathUnknownFails() {
        this.applyFails(this.createPatch(),
                JsonNode.object());
    }

    @Test
    public void testPathUnknownFails2() {
        this.applyFails(this.createPatch(),
                JsonNode.object().set(JsonNodeName.with("A"), this.value1()));
    }

    @Test
    public void testIncorrectValueFails() {
        this.applyFails(this.createPatch(),
                JsonNode.object().set(this.property1(), JsonNode.string("wrong-different-value")));
    }

    @Test
    public void testPathAndValueObject() {
        final JsonNode object = JsonNode.object()
                .set(this.property1(), this.value1());

        this.applyAndCheck(this.createPatch(),
                object,
                object);
    }

    @Test
    public void testPathAndValueArray0() {
        pathAndValueAndCheck(0);
    }

    @Test
    public void testPathAndValueArray1() {
        pathAndValueAndCheck(1);
    }

    @Test
    public void testPathAndValueArray2() {
        pathAndValueAndCheck(2);
    }

    private void pathAndValueAndCheck(final int index) {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.string("value-a1"))
                .appendChild(JsonNode.string("value-b2"))
                .appendChild(JsonNode.string("value-c3"));

        this.applyAndCheck(TestNodePatch.with(NodePointer.indexed(index, JsonNode.class), array.get(index)),
                array,
                array);
    }

    @Test
    public void testPathAppendFails() {
        this.applyFails(TestNodePatch.with(NodePointer.parse("/-", JsonNodeName::with, JsonNode.class), this.value1()),
                JsonNode.object());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPatch(), "test path=\"/a1\" value=\"value1\"");
    }

    @Override
    TestNodePatch<JsonNode, JsonNodeName> createPatch(final NodePointer<JsonNode, JsonNodeName> path, final JsonNode value) {
        return TestNodePatch.with(path, value);
    }

    @Override
    String operation() {
        return "test";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<TestNodePatch<JsonNode, JsonNodeName>> type() {
        return Cast.to(TestNodePatch.class);
    }

    // TypeNameTesting.................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Test";
    }
}
