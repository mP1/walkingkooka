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

package walkingkooka.tree.json;

import org.junit.Test;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class JsonBooleanNodeTest extends JsonLeafNodeTestCase<JsonBooleanNode, Boolean>{

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
        assertEquals("true", this.createJsonNode(true).toString());
    }

    @Test
    public void testToStringFalse() {
        assertEquals("false", this.createJsonNode(false).toString());
    }

    @Override
    JsonBooleanNode createJsonNode(final Boolean value) {
        return JsonBooleanNode.with(value);
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
    Class<JsonBooleanNode> jsonNodeType() {
        return JsonBooleanNode.class;
    }
}
