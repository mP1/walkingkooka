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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.pointer.NodePointerVisitorTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodePatchToJsonFormatNodePointerVisitorTest extends NodePatchTestCase<NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonNodeName>>
        implements NodePointerVisitorTesting<NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonNodeName>, JsonNode, JsonNodeName> {

    @Test
    public void testPathNameTypeNameAbsentFromPath() {
        this.pathNameTypeAndCheck("/1/2/3/-", null);
    }

    @Test
    public void testPathNameTypeNamePresentInPath() {
        this.pathNameTypeAndCheck("/abc", "json-property-name");
    }

    @Test
    public void testPathNameTypeNamePresentInPath2() {
        this.pathNameTypeAndCheck("/1/abc", "json-property-name");
    }

    private void pathNameTypeAndCheck(final String path, final String typeName) {
        assertEquals(Optional.ofNullable(typeName).map(JsonNode::string),
                NodePatchToJsonFormatNodePointerVisitor.pathNameType(NodePointer.parse(path, JsonNodeName::with, JsonNode.class)),
                () -> "path: " + CharSequences.quoteAndEscape(path));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(new NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonNodeName>(), "");
    }

    @Test
    public void testToString2() {
        final JsonStringNode type = JsonNode.string(this.getClass().getName());

        final NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonNodeName> visitor = new NodePatchToJsonFormatNodePointerVisitor<>();
        visitor.pathNameType = Optional.of(type);
        this.toStringAndCheck(visitor, type.toString());
    }

    @Override
    public NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonNodeName> createVisitor() {
        return new NodePatchToJsonFormatNodePointerVisitor<>();
    }

    @Override
    public String typeNamePrefix() {
        return NodePatchToJsonFormat.class.getSimpleName();
    }

    @Override
    public Class<NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonNodeName>> type() {
        return Cast.to(NodePatchToJsonFormatNodePointerVisitor.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
