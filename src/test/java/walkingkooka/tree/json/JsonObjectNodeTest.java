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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.naming.Name;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.select.FakeNodeSelectorContext;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public final class JsonObjectNodeTest extends JsonParentNodeTestCase<JsonObjectNode>{

    private final static String KEY1 = "key1";
    private final static String KEY2 = "key2";
    private final static String KEY3 = "key3";

    private final static String VALUE1 = "value1";
    private final static String VALUE2 = "value2";
    private final static String VALUE3 = "value3";

    @Test
    public void testSetChildrenSame() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();
        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1);
        this.childrenCheck(object);
        this.getAndCheck(object, key1, VALUE1);

        assertSame(object, object.setChildren(object.children()));
    }

    @Test
    public void testSetChildrenEquivalent() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();
        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1);
        this.childrenCheck(object);
        this.getAndCheck(object, key1, VALUE1);

        assertSame(object, object.setChildren(Lists.of(JsonNode.string(VALUE1).setName(key1))));
    }

    // set and get

    @Test
    public void testGetAbsent() {
        assertEquals(Optional.empty(), this.createJsonNode().get(key1()));
    }

    @Test
    public void testSetAndGet() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();
        final JsonObjectNode empty = JsonNode.object();
        
        final JsonObjectNode object = empty.set(key1, value1);
        this.childrenCheck(object);
        this.checkChildCount(object, 1);
        this.getAndCheck(object, key1, VALUE1);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);

        this.checkChildCount(empty, 0);
    }

    @Test
    public void testGetAbsent2() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();
        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1);

        assertEquals(Optional.empty(), object.get(key2()));
    }

    @Test
    public void testSetSame() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();
        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1);
        this.childrenCheck(object);
        this.getAndCheck(object, key1, VALUE1);

        assertSame(object, object.set(key1, value1));
    }

    @Test
    public void testSetAndGet2() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();
        
        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2);
        this.childrenCheck(object);
        this.checkChildCount(object, 2);
        this.getAndCheck(object, key1, VALUE1);
        this.getAndCheck(object, key2, VALUE2);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.checkChildCount(empty, 0);
    }
    
    private void getAndCheck(final JsonObjectNode object, final JsonNodeName key, final String value) {
        final Optional<JsonNode> got = object.get(key);
        assertNotEquals("expected value for key " + key, Optional.empty(), got);
        
        assertEquals("incorrect string value for key=" + key, value, JsonStringNode.class.cast(got.get()).value());
    }

    // remove

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKeyFails() {
        this.createJsonNode().remove(null);
    }

    @Test
    public void testRemoveUnknownKey() {
        final JsonObjectNode object = this.createJsonNode();
        assertSame(object, object.remove(key1()));
    }

    @Test
    public void testRemove() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();

        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2)
                .remove(key1);
        this.childrenCheck(object);
        this.checkChildCount(object, 1);
        this.getAndCheck(object, key1, VALUE1);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.checkChildCount(empty, 0);
    }

    @Test
    public void testRemove2() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();

        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2)
                .remove(key1)
                .remove(key2);
        this.childrenCheck(object);
        this.checkChildCount(object, 0);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.checkChildCount(empty, 0);
    }

    @Test
    public void testChildReplaced() {
        final JsonNodeName key1 = this.key1();
        final JsonNodeName key2 = this.key2();
        final JsonNodeName key3 = this.key3();

        final JsonObjectNode root = JsonNode.object()
                .set(key2, this.value2())
                .set(key3, JsonNode.object()
                        .set(key1, this.value1()));
        this.checkChildCount(root, 2);

        final JsonObjectNode nested = root.get(key3).get().cast();
        final JsonObjectNode updated = nested.set(key1, value3());
        final JsonObjectNode updatedRoot = updated.root().cast();

        this.checkChildCount(updatedRoot, 2);
        this.getAndCheck(updatedRoot, key2, VALUE2);
    }

    @Test
    public void testReplaceChild() {
        final JsonNodeName key1 = this.key1();

        final JsonObjectNode root = JsonNode.object()
                .set(key1, this.value1());
        this.checkChildCount(root, 1);

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2().setName(key2);
        final JsonObjectNode updated = root.replaceChild(root.get(key1).get(), value2)
                .cast();

        this.checkChildCount(updated, 1);
        this.getAndCheck(updated, key2, VALUE2);
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<JsonNode> visited = Lists.array();

        final JsonObjectNode object = this.createJsonNode()
                .set(key1(), value1())
                .set(key2(), value2());
        final JsonStringNode string1 = object.children().get(0).cast();
        final JsonStringNode string2 = object.children().get(1).cast();

        new FakeJsonNodeVisitor() {
            @Override
            protected Visiting startVisit(final JsonNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final JsonObjectNode t) {
                assertSame(object, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonObjectNode t) {
                assertSame(object, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final JsonStringNode t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(object);
        assertEquals("1517217262", b.toString());
        assertEquals("visited",
                Lists.of(object, object,
                        string1, string1, string1,
                        string2, string2, string2,
                        object, object),
                visited);
    }

    @Test
    public void testSelectorUsage() {
        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));
        final JsonNodeName key2 = this.key2();
        final NodeSelector<JsonNode, JsonNodeName, Name, Object> selector = JsonNode.absoluteNodeSelectorBuilder()
                .descendant()
                .named(key2)
                .build();
        final Set<JsonNode> selected = Sets.ordered();
        selector.accept(object, new FakeNodeSelectorContext<JsonNode, JsonNodeName, Name, Object>(){
            @Override
            public void potential(final JsonNode node) {

            }

            @Override
            public void selected(final JsonNode node) {
                selected.add(node);
            }
        });
        assertEquals("matched nodes", Sets.of(object.get(key2).get()), selected);
    }

    @Test
    public void testPrintJsonWithoutIndentationAndNoneLineEnding() {
        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));

        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.fixed(Printers.stringBuilder(b, LineEnding.NONE), Indentation.EMPTY);
        object.printJson(printer);

        assertEquals("{\"key1\": true,\"key2\": 2,\"key3\": \"third\"}", b.toString());
    }

    @Test
    public void testTextWithoutChildren() {
        assertEquals("", JsonNode.object().text());
    }

    @Test
    public void testTextWithChildren() {
        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));

        assertEquals("true2.0third", object.text());
    }

    @Test
    public void testToStringEmpty() {
        assertEquals("{}", this.createJsonNode().toString());
    }

    @Test
    public void testToStringWithChildren() {
        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));

        assertEquals("{\n  \"key1\": true,\n  \"key2\": 2,\n  \"key3\": \"third\"\n}".replace("\n", LineEnding.SYSTEM.toString()), object.toString());
    }

    @Test
    public void testToStringNestedObject() {
        final JsonObjectNode nested = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2));
        final JsonObjectNode object = JsonNode.object()
                .set(key3(), nested);

        assertEquals("{\n  \"key3\": {\n    \"key1\": true,\n    \"key2\": 2\n  }\n}".replace("\n", LineEnding.SYSTEM.toString()), object.toString());
    }

    @Test
    public void testToStringNestedArray() {
        final JsonArrayNode nested = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));
        final JsonObjectNode object = JsonNode.object()
                .set(key3(), nested);

        assertEquals("{\n  \"key3\": [true, 2, \"third\"]\n}".replace("\n", LineEnding.SYSTEM.toString()), object.toString());
    }
    
    @Override
    JsonObjectNode createJsonNode() {
        return JsonNode.object();
    }

    private JsonNodeName key1() {
        return JsonNodeName.with(KEY1);
    }

    private JsonNodeName key2() {
        return JsonNodeName.with(KEY2);
    }

    private JsonNodeName key3() {
        return JsonNodeName.with(KEY3);
    }

    private JsonStringNode value1() {
        return JsonNode.string(VALUE1);
    }

    private JsonStringNode value2() {
        return JsonNode.string(VALUE2);
    }

    private JsonStringNode value3() {
        return JsonNode.string(VALUE3);
    }

    @Override
    Class<JsonObjectNode> jsonNodeType() {
        return JsonObjectNode.class;
    }
}
