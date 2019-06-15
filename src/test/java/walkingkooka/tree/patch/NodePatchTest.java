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
import walkingkooka.type.JavaVisibility;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodePatchTest extends NodePatchTestCase<NodePatch<JsonNode, JsonNodeName>> {

    @Test
    public void testFromJsonPatchNullJsonNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePatch.fromJsonPatch(null, this.nameFactory(), this.valueFactory());
        });
    }

    @Test
    public void testFromJsonPatchNullNameFactoryFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePatch.fromJsonPatch(JsonNode.object(), null, this.valueFactory());
        });
    }

    @Test
    public void testFromJsonPatchNulValueFactorylFails() {
        assertThrows(NullPointerException.class, () -> {
            NodePatch.fromJsonPatch(JsonNode.object(), this.nameFactory(), null);
        });
    }

    private Function<String, JsonNodeName> nameFactory() {
        return JsonNodeName::with;
    }

    private Function<JsonNode, JsonNode> valueFactory() {
        return Function.identity();
    }

    @Test
    public void testTest() {
        final JsonNode node = this.parse("{\"a1\": \"value1\"}");

        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .test(this.pointer("/a1"), this.value1()),
                node,
                node);
    }

    @Test
    public void testTestTest() {
        final JsonNode node = this.parse("{\"a1\": \"value1\", \"b2\": \"value2\"}");

        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .test(this.pointer("/a1"), this.value1())
                        .test(this.pointer("/b2"), this.value2()),
                node,
                node);
    }

    @Test
    public void testTestAdd() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .test(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2()),
                "{\"a1\": \"value1\"}",
                "{\"a1\": \"value1\", \"b2\": \"value2\"}");
    }

    @Test
    public void testAddAdd() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2()),
                "{\"c3\": \"value3\"}",
                "{\"a1\": \"value1\", \"b2\": \"value2\", \"c3\": \"value3\"}");
    }

    @Test
    public void testRemoveAdd() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .remove(this.pointer("/a1"))
                        .add(this.pointer("/b2"), this.value2()),
                "{\"a1\": \"value1\", \"c3\": \"value3\"}",
                "{\"b2\": \"value2\", \"c3\": \"value3\"}");
    }

    @Test
    public void testRemoveAddRemove() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .remove(this.pointer("/a1"))
                        .add(this.pointer("/b2"), this.value2())
                        .remove(this.pointer("/c3")),
                "{\"a1\": \"value1\", \"c3\": \"value3\"}",
                "{\"b2\": \"value2\"}");
    }

    @Test
    public void testAddAddRemove() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2())
                        .remove(this.pointer("/a1")),
                "{\"c3\": \"value3\"}",
                "{\"b2\": \"value2\", \"c3\": \"value3\"}");
    }

    @Test
    public void testCopyTestRemove() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .copy(this.pointer("/a1"), this.pointer("/b2/c3"))
                        .test(this.pointer("/b2/c3"), this.string("COPIED"))
                        .remove(this.pointer("/a1")),
                "{\"a1\": \"COPIED\", \"b2\": {\"c3\": \"value3\"}}",
                "{\"b2\": {\"c3\": \"COPIED\"}}");
    }

    @Test
    public void testMoveTest() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .move(this.pointer("/a1"), this.pointer("/b2/c3"))
                        .test(this.pointer("/b2/c3"), this.string("MOVED")),
                "{\"a1\": \"MOVED\", \"b2\": {\"c3\": \"value3\"}}",
                "{\"b2\": {\"c3\": \"MOVED\"}}");
    }

    /**
     * <a href="http://jsonpatch.com/"></a>
     * <pre>
     * The original document
     * {
     *   "baz": "qux",
     *   "foo": "bar"
     * }
     * The patch
     * [
     *   { "op": "replace", "path": "/baz", "value": "boo" },
     *   { "op": "add", "path": "/hello", "value": ["world"] },
     *   { "op": "remove", "path": "/foo" }
     * ]
     * The result
     * {
     *   "baz": "boo",
     *   "hello": ["world"]
     * }
     * </pre>
     */
    @Test
    public void testJsonPatchExample() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .replace(this.pointer("/baz"), this.string("boo"))
                        .add(this.pointer("/hello"), JsonNode.array().appendChild(this.string("world")))
                        .remove(this.pointer("/foo")),
                "{\"baz\": \"qux\", \"foo\": \"bar\"}",
                "{\"baz\": \"boo\", \"hello\": [\"world\"]}");
    }

    @Test
    public void testToStringAddAdd() {
        this.toStringAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2()),
                "add path=\"/a1\" value=\"value1\", add path=\"/b2\" value=\"value2\"");
    }

    @Test
    public void testToStringAddReplaceRemove() {
        this.toStringAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .replace(this.pointer("/b2"), this.value2())
                        .remove(this.pointer("/c3")),
                "add path=\"/a1\" value=\"value1\", replace path=\"/b2\" value=\"value2\", remove path=\"/c3\"");
    }

    @Test
    public void testToStringCopyMove() {
        this.toStringAndCheck(NodePatch.empty(JsonNode.class)
                        .copy(this.pointer("/a1"), this.pointer("/b2"))
                        .move(this.pointer("/c3"), this.pointer("/d4/e5")),
                "copy from=\"/a1\" path=\"/b2\", move from=\"/c3\" path=\"/d4/e5\"");
    }

    private JsonNode string(final String string) {
        return JsonNode.string(string);
    }

    // ClassTesting2........................................................................

    @Override
    public Class<NodePatch<JsonNode, JsonNodeName>> type() {
        return Cast.to(NodePatch.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
