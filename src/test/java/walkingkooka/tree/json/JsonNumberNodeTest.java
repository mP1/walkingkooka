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
import walkingkooka.tree.search.SearchNode;
import walkingkooka.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class JsonNumberNodeTest extends JsonLeafNonNullNodeTestCase<JsonNumberNode, Double> {

    @Override
    public void testNumberValueOrFail() {
        assertEquals(1.0,
                JsonNumberNode.with(1).numberValueOrFail());
    }

    @Test
    public void testNumberValueOrFail2() {
        assertEquals(2.0,
                JsonNumberNode.with(2).numberValueOrFail());
    }

    // toSearchNode.........................................................................................

    @Test
    public void testToSearchNode() {
        this.toSearchNodeAndCheck(this.createJsonNode(123.5), SearchNode.doubleNode("123.5", 123.5));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final JsonNumberNode node = this.createJsonNode();

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
            protected void visit(final JsonNumberNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createJsonNode(1.0), "1");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createJsonNode(234.5), "234.5");
    }

    @Override
    JsonNumberNode createJsonNode(final Double value) {
        return JsonNumberNode.with(value);
    }

    @Override
    JsonNumberNode setValue(final JsonNumberNode node, final Double value) {
        return node.setValue(value);
    }

    @Override
    Double value() {
        return 1.0;
    }

    @Override
    Double differentValue() {
        return 999.0;
    }

    @Override
    Class<JsonNumberNode> jsonNodeType() {
        return JsonNumberNode.class;
    }

    @Override
    List<String> propertiesNeverReturnNullSkipProperties() {
        return Lists.of(ARRAY_OR_FAIL,
                BOOLEAN_VALUE_OR_FAIL,
                UNMARSHALL_LIST,
                UNMARSHALL_SET,
                UNMARSHALL_MAP,
                UNMARSHALL,
                OBJECT_OR_FAIL,
                PARENT_OR_FAIL,
                STRING_VALUE_OR_FAIL);
    }
}
