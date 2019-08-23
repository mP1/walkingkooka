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

public final class JsonBooleanNodeTest extends JsonLeafNonNullNodeTestCase<JsonBooleanNode, Boolean> {

    @Test
    public void testWithTrue() {
        this.withAndCheck(true);
    }

    @Test
    public void testWithFalse() {
        this.withAndCheck(false);
    }

    private void withAndCheck(final boolean value) {
        assertSame(JsonBooleanNode.with(value), JsonBooleanNode.with(value));
    }

    @Override
    public void testBooleanValueOrFail() {
        // ignore
    }

    @Test
    public void testBooleanValueOrFailTrue() {
        assertEquals(true,
                JsonBooleanNode.with(true).booleanValueOrFail());
    }

    @Test
    public void testBooleanValueOrFailFalse() {
        assertEquals(false,
                JsonBooleanNode.with(false).booleanValueOrFail());
    }

    // toSearchNode.........................................................................................

    @Test
    public void testToSearchNodeTrue() {
        this.toSearchNodeAndCheck(this.createJsonNode(true), SearchNode.text("true", "true"));
    }

    @Test
    public void testToSearchNodeFalse() {
        this.toSearchNodeAndCheck(this.createJsonNode(false), SearchNode.text("false", "false"));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final JsonBooleanNode node = this.createJsonNode();

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
            protected void visit(final JsonBooleanNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToStringTrue() {
        this.toStringAndCheck(this.createJsonNode(true), "true");
    }

    @Test
    public void testToStringFalse() {
        this.toStringAndCheck(this.createJsonNode(false), "false");
    }

    @Override
    JsonBooleanNode createJsonNode(final Boolean value) {
        return JsonBooleanNode.with(value);
    }

    @Override
    JsonBooleanNode setValue(final JsonBooleanNode node, final Boolean value) {
        return node.setValue(value);
    }

    @Override
    Boolean value() {
        return true;
    }

    @Override
    Boolean differentValue() {
        return false;
    }

    @Override
    String nodeTypeName() {
        return "json-boolean";
    }

    @Override
    Class<JsonBooleanNode> jsonNodeType() {
        return JsonBooleanNode.class;
    }

    @Override
    List<String> propertiesNeverReturnNullSkipProperties() {
        return Lists.of(ARRAY_OR_FAIL,
                FROM_WITH_TYPE_LIST,
                FROM_WITH_TYPE_SET,
                FROM_WITH_TYPE_MAP,
                FROM_WITH_TYPE,
                NUMBER_VALUE_OR_FAIL,
                OBJECT_OR_FAIL,
                PARENT_OR_FAIL,
                STRING_VALUE_OR_FAIL);
    }

    // JsonNodeMappingTesting..................................................................

    @Override
    public final JsonBooleanNode fromJsonNode(final JsonNode from,
                                              final FromJsonNodeContext context) {
        return from.cast();
    }
}
