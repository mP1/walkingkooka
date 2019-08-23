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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.json.map.FromJsonNodeContext;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class JsonStringNodeTest extends JsonLeafNonNullNodeTestCase<JsonStringNode, String> {

    @Override
    public void testStringValueOrFail() {
        assertEquals("abc",
                JsonStringNode.with("abc").stringValueOrFail());
    }

    @Test
    public void testStringValueOrFail2() {
        assertEquals("123",
                JsonStringNode.with("123").stringValueOrFail());
    }

    @Test
    public void testSameValueDifferentCase() {
        this.checkNotEquals(JsonNode.string("ABC123"));
    }

    // toSearchNode...............................................................................................

    @Test
    public void testToSearchNode() {
        final String text = "abc123";
        this.toSearchNodeAndCheck(this.createJsonNode(text), SearchNode.text(text, text));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final JsonStringNode node = this.createJsonNode();

        new FakeJsonNodeVisitor() {
            @Override
            protected Visiting startVisit(final JsonNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final JsonStringNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createJsonNode("abc123"),
                "\"abc123\"");
    }

    @Test
    public void testToStringRequiresEscaping() {
        this.toStringAndCheck(this.createJsonNode("abc\t123"),
                "\"abc\\t123\"");
    }

    @Override
    JsonStringNode createJsonNode(final String value) {
        return JsonStringNode.with(value);
    }

    @Override
    JsonStringNode setValue(final JsonStringNode node, final String value) {
        return node.setValue(value);
    }

    @Override
    String value() {
        return "A";
    }

    @Override
    String differentValue() {
        return "Different";
    }

    @Override
    String nodeTypeName() {
        return "json-string";
    }

    @Override
    Class<JsonStringNode> jsonNodeType() {
        return JsonStringNode.class;
    }

    @Override
    List<String> propertiesNeverReturnNullSkipProperties() {
        return Lists.of(ARRAY_OR_FAIL,
                BOOLEAN_VALUE_OR_FAIL,
                FROM_WITH_TYPE_LIST,
                FROM_WITH_TYPE_SET,
                FROM_WITH_TYPE_MAP,
                FROM_WITH_TYPE,
                NUMBER_VALUE_OR_FAIL,
                OBJECT_OR_FAIL,
                PARENT_OR_FAIL);
    }

    // JsonNodeMappingTesting...............................................................................................

    @Override
    public final JsonStringNode fromJsonNode(final JsonNode from,
                                             final FromJsonNodeContext context) {
        return from.cast();
    }
}
