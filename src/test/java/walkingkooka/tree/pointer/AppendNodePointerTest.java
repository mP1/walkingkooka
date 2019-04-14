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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AppendNodePointerTest extends NodePointerTestCase<AppendNodePointer<JsonNode, JsonNodeName>> {

    @Test
    public void testAppendIndexedFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNodePointer().indexed(1);
        });
    }

    @Test
    public void testAppendNamedFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNodePointer().named(JsonNodeName.with("fail!"));
        });
    }

    @Test
    public void testAppendAppendFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNodePointer().append();
        });
    }

    @Test
    public void testAdd() {
        final JsonArrayNode array = JsonNode.array();
        final JsonNode add = JsonNode.string("add");

        this.addAndCheck(this.createNodePointer(),
                array,
                add,
                array.appendChild(add));
    }

    @Test
    public void testAdd2() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.string("a1"));
        final JsonNode add = JsonNode.string("add");

        this.addAndCheck(this.createNodePointer(),
                array,
                add,
                array.appendChild(add));
    }

    @Test
    public void testRemoveFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNodePointer().remove(JsonNode.object());
        });
    }

    @Test
    public void testToStringElementAppend() {
        final NodePointer<JsonNode, JsonNodeName> element = NodePointer.named(JsonNodeName.with("abc"), JsonNode.class);
        this.toStringAndCheck(element.append(), "/abc/-");
    }

    @Test
    public void testToStringArrayAppend() {
        final NodePointer<JsonNode, JsonNodeName> array = NodePointer.indexed(123, JsonNode.class);
        this.toStringAndCheck(array.append(), "/123/-");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(AppendNodePointer.create(), "/-");
    }

    @Test
    public void testVisitor() {
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

        }.accept(this.createNodePointer());

        assertEquals("132", b.toString());
    }

    @Override
    AppendNodePointer<JsonNode, JsonNodeName> createNodePointer() {
        return AppendNodePointer.create();
    }

    @Override
    public Class<AppendNodePointer<JsonNode, JsonNodeName>> type() {
        return Cast.to(AppendNodePointer.class);
    }
}
