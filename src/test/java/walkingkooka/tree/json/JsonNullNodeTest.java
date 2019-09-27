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

public final class JsonNullNodeTest extends JsonLeafNodeTestCase<JsonNullNode, Void> {

    // toSearchNode..............................................................................

    @Test
    public void testToSearchNode() {
        this.toSearchNodeAndCheck(this.createJsonNode(), SearchNode.text("null", "null"));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final JsonNullNode node = this.createJsonNode();

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
            protected void visit(final JsonNullNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createJsonNode(), "null");
    }

    @Override
    public void testEqualsDifferentValue() {
        // nop
    }

    @Override
    public final void testSetDifferentValue() {
        // nop
    }

    @Override
    JsonNullNode createJsonNode(final Void value) {
        return JsonNullNode.INSTANCE;
    }

    @Override
    JsonNullNode setValue(final JsonNullNode node, final Void value) {
        return node.setValue(value);
    }

    @Override
    Void value() {
        return null;
    }

    @Override
    Void differentValue() {
        return null;
    }

    @Override
    Class<JsonNullNode> jsonNodeType() {
        return JsonNullNode.class;
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
                PARENT_OR_FAIL,
                STRING_VALUE_OR_FAIL,
                VALUE);
    }
}
