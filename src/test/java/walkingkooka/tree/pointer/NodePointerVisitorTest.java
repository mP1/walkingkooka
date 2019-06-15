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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodePointerVisitorTest implements NodePointerVisitorTesting<FakeNodePointerVisitor<JsonNode, JsonNodeName>, JsonNode, JsonNodeName> {

    @Override
    public void testTestNaming() {
    }

    @Test
    public void testAcceptSkipRemaining() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<JsonNode, JsonNodeName>() {
            @Override
            protected Visiting startVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("2");
            }

        }.accept(NodePointer.parse("/1/abc/-", JsonNodeName::with, JsonNode.class));

        assertEquals("12", b.toString());
    }

    @Test
    public void testAny() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<JsonNode, JsonNodeName>() {
            @Override
            protected Visiting startVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("2");
            }

            @Override
            protected void visit(final AnyNodePointer<JsonNode, JsonNodeName> node) {
                b.append("3");
            }
        }.accept(NodePointer.any(JsonNode.class));

        assertEquals("132", b.toString());
    }

    @Test
    public void testMultipleComponents() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<JsonNode, JsonNodeName>() {
            @Override
            protected Visiting startVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("2");
            }

            @Override
            protected void visit(final AppendNodePointer<JsonNode, JsonNodeName> node) {
                b.append("3");
            }

            @Override
            protected Visiting startVisit(final IndexedChildNodePointer<JsonNode, JsonNodeName> node) {
                assertEquals(1, node.index(), "index");
                b.append("4");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final IndexedChildNodePointer<JsonNode, JsonNodeName> node) {
                b.append("5");
            }

            @Override
            protected Visiting startVisit(final NamedChildNodePointer<JsonNode, JsonNodeName> node) {
                assertEquals(JsonNodeName.with("abc"), node.name(), "name");
                b.append("6");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NamedChildNodePointer<JsonNode, JsonNodeName> node) {
                b.append("7");
            }
        }.accept(NodePointer.parse("/1/abc/-", JsonNodeName::with, JsonNode.class));

        assertEquals("14161327252", b.toString());
    }

    @Test
    public void testMultipleComponents2() {
        this.visitAndCheck("/1/abc/-");
    }

    @Test
    public void testMultipleComponents3() {
        this.visitAndCheck("/1/abc/2/def/-");
    }

    private void visitAndCheck(final String path) {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<JsonNode, JsonNodeName>() {
            @Override
            protected Visiting startVisit(final NodePointer<JsonNode, JsonNodeName> node) {
                b.append("/");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<JsonNode, JsonNodeName> node) {
            }

            @Override
            protected void visit(final AppendNodePointer<JsonNode, JsonNodeName> node) {
                b.append("-");
            }

            @Override
            protected Visiting startVisit(final IndexedChildNodePointer<JsonNode, JsonNodeName> node) {
                b.append(node.index());
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final IndexedChildNodePointer<JsonNode, JsonNodeName> node) {
            }

            @Override
            protected Visiting startVisit(final NamedChildNodePointer<JsonNode, JsonNodeName> node) {
                b.append(node.name());
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NamedChildNodePointer<JsonNode, JsonNodeName> node) {
            }
        }.accept(NodePointer.parse(path, JsonNodeName::with, JsonNode.class));

        assertEquals(path, b.toString());
    }

    @Override
    public FakeNodePointerVisitor<JsonNode, JsonNodeName> createVisitor() {
        return new FakeNodePointerVisitor<>();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public Class<FakeNodePointerVisitor<JsonNode, JsonNodeName>> type() {
        return Cast.to(FakeNodePointerVisitor.class);
    }
}
