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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonObjectNodeTest extends JsonParentNodeTestCase<JsonObjectNode, JsonObjectNodeList>{

    private final static String KEY1 = "key1";
    private final static String KEY2 = "key2";
    private final static String KEY3 = "key3";

    private final static String VALUE1 = "value1";
    private final static String VALUE2 = "value2";
    private final static String VALUE3 = "value3";

    @Override
    public void testAppendChild2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveChildFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveChildLast() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Override
    public void testSetDifferentChildren() {
        final JsonObjectNode parent = JsonNode.object();

        final JsonStringNode child1 = JsonNode.string("a1");
        final JsonNumberNode child2 = JsonNode.number(2);

        final JsonNode parent1 = this.setChildrenAndCheck(parent, child1, child2);

        final JsonObjectNode child3 = JsonNode.object();
        final JsonNode parent2 = this.setChildrenAndCheck(parent1, child3);

        this.childCountCheck(parent2, child3);
    }

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

    @Test
    public void testSetChildrenSameDifferentOrder() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();


        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1)
                .set(key2, value2);
        this.childrenCheck(object);
        this.getAndCheck(object, key1, VALUE1);

        final JsonObjectNode object2 = JsonNode.object()
                .set(key2, value2)
                .set(key1, value1);
        this.childrenCheck(object);
        this.getAndCheck(object, key2, VALUE2);

        final List<JsonNode> differentOrder = Lists.array();
        differentOrder.addAll(object2.children());

        assertSame(object, object.setChildren(differentOrder));
    }

    // set and get

    @Test
    public void testGetAbsent() {
        assertEquals(Optional.empty(), this.createJsonNode().get(key1()));
    }

    @Test
    public void testGetOrFailFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createJsonNode().getOrFail(JsonNodeName.with("unknown-property"));
        });
    }

    @Test
    public void testSetAndGet() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();
        final JsonObjectNode empty = JsonNode.object();
        
        final JsonObjectNode object = empty.set(key1, value1);
        this.childrenCheck(object);
        this.childCountCheck(object, 1);
        this.getAndCheck(object, key1, VALUE1);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);

        this.childCountCheck(empty, 0);
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
    public void testSetNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNode.object().set(
                    null,
                    this.value1());
        });
    }

    @Test
    public void testSetNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNode.object().set(
                    this.key1(),
                    null);
        });
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
        this.childCountCheck(object, 2);
        this.getAndCheck(object, key1, VALUE1);
        this.getAndCheck(object, key2, VALUE2);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.childCountCheck(empty, 0);
    }

    private void getAndCheck(final JsonObjectNode object, final JsonNodeName key, final String value) {
        final Optional<JsonNode> got = object.get(key);
        assertNotEquals(Optional.empty(), got, "expected value for key " + key);

        // JsonStringNode retrieved will include the key component so a new JsonStringNode cant be created and assertEqual'd.
        assertEquals(value,
                JsonStringNode.class.cast(object.get(key).get()).value(),
                "incorrect string value for get key=" + key);

        assertEquals(value,
                JsonStringNode.class.cast(object.getOrFail(key)).value(),
                "incorrect JsonNode for getOrFail " + key);
    }

    // remove

    @Test
    public void testRemoveNullKeyFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createJsonNode().remove(null);
        });
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
        this.childCountCheck(object, 1);

        this.getAndCheck(object, key2, VALUE2);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.childCountCheck(empty, 0);
    }

    @Test
    public void testRemove2() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();

        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode zzz = empty.set(key1, value1)
                .set(key2, value2)
                .remove(key1);

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2)
                .remove(key1)
                .remove(key2);
        this.childrenCheck(object);
        this.childCountCheck(object, 0);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.childCountCheck(empty, 0);
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
        this.childCountCheck(root, 2);

        final JsonObjectNode nested = root.get(key3).get().cast();
        final JsonObjectNode updated = nested.set(key1, value3());
        final JsonObjectNode updatedRoot = updated.root().cast();

        this.childCountCheck(updatedRoot, 2);
        this.getAndCheck(updatedRoot, key2, VALUE2);
    }

    @Test
    public void testReplaceChild() {
        final JsonNodeName key1 = this.key1();

        final JsonObjectNode root = JsonNode.object()
                .set(key1, this.value1());
        this.childCountCheck(root, 1);

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2().setName(key2);
        final JsonObjectNode updated = root.replaceChild(root.get(key1).get(), value2)
                .cast();

        this.childCountCheck(updated, 1);
        this.getAndCheck(updated, key2, VALUE2);
    }

    @Test
    public void testContainsAbsent() {
        this.containsAndCheck(JsonNode.object()
                        .set(JsonNodeName.with("prop1"), JsonNode.string("value1")),
                JsonNodeName.with("different-prop"),
                false);
    }

    @Test
    public void testContainsPresent() {
        final JsonNodeName property = JsonNodeName.with("prop123");
        this.containsAndCheck(JsonNode.object()
                        .set(JsonNodeName.with("prop1"), JsonNode.string("value1"))
                        .set(property, JsonNode.string("value2")),
                property,
                true);
    }

    private void containsAndCheck(final JsonObjectNode object, final JsonNodeName name, final boolean contains) {
        assertEquals(contains, object.contains(name), () -> object + " contains " + name);
    }

    @Test
    public void testEqualsDifferentChildren() {
        this.checkNotEquals(JsonNode.object().set(property(), JsonNode.string("different")));
    }

    @Test
    public void testEqualsDifferentChildren2() {
        this.checkNotEquals(JsonNode.object().set(property(), JsonNode.string("child1")),
                JsonNode.object().set(property(), JsonNode.string("child2")));
    }

    @Test
    public void testEqualsDifferentChildren3() {
        final JsonNode value = JsonNode.string("value");

        this.checkNotEquals(JsonNode.object().set(property(), value),
                JsonNode.object().set(JsonNodeName.with("different"), value));
    }

    @Test
    public void testEqualsArray() {
        this.checkNotEquals(JsonNode.object().set(property(), JsonNode.string("child1")),
                JsonNode.array().appendChild(JsonNode.string("child2")));
    }

    @Test
    public void testEqualsArrayPropertyValue() {
        final JsonObjectNode object = JsonNode.object();
        final JsonNodeName property = this.property();

        this.checkNotEquals(
                object.set(property, JsonNode.object().set(JsonNodeName.with("p"), JsonNode.string("child1"))),
                object.set(property, JsonNode.array().appendChild(JsonNode.string("child2"))));
    }

    private JsonNodeName property() {
        return JsonNodeName.with("property");
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
        assertEquals(Lists.of(object, object,
                        string1, string1, string1,
                        string2, string2, string2,
                        object, object),
                visited,
                "visited");
    }

    @Test
    public void testSelectorUsage() {
        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));
        final JsonNodeName key2 = this.key2();

        this.selectorAcceptAndCheck(object,
                JsonNode.PATH_SEPARATOR.absoluteNodeSelectorBuilder(JsonNode.class)
                        .descendant()
                        .named(key2)
                        .build(),
                object.get(key2).get());
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
    public void testToSearchNode() {
        final JsonBooleanNode booleanNode = JsonNode.booleanNode(true);
        final JsonNumberNode number = JsonNode.number(2);
        final JsonStringNode string = JsonNode.string("third");

        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));

        this.toSearchNodeAndCheck(object, SearchNode.sequence(Lists.of(
                booleanNode.toSearchNode().setName(SearchNodeName.with(KEY1)),
                number.toSearchNode().setName(SearchNodeName.with(KEY2)),
                string.toSearchNode().setName(SearchNodeName.with(KEY3)))));
    }

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(this.createJsonNode(), "{}");
    }

    @Test
    public void testToStringWithChildren() {
        final JsonObjectNode object = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2))
                .set(key3(), JsonNode.string("third"));

        this.toStringAndCheck(object,
                "{\n  \"key1\": true,\n  \"key2\": 2,\n  \"key3\": \"third\"\n}".replace("\n", LineEnding.SYSTEM.toString()));
    }

    @Test
    public void testToStringNestedObject() {
        final JsonObjectNode nested = JsonNode.object()
                .set(key1(), JsonNode.booleanNode(true))
                .set(key2(), JsonNode.number(2));
        final JsonObjectNode object = JsonNode.object()
                .set(key3(), nested);

        this.toStringAndCheck(object,
                "{\n  \"key3\": {\n    \"key1\": true,\n    \"key2\": 2\n  }\n}".replace("\n", LineEnding.SYSTEM.toString()));
    }

    @Test
    public void testToStringNestedArray() {
        final JsonArrayNode nested = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));
        final JsonObjectNode object = JsonNode.object()
                .set(key3(), nested);

        this.toStringAndCheck(object,
                "{\n  \"key3\": [true, 2, \"third\"]\n}".replace("\n", LineEnding.SYSTEM.toString()));
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

    // HasJsonNodeTesting..................................................................

    @Override
    public final JsonObjectNode fromJsonNode(final JsonNode from) {
        return JsonObjectNode.fromJsonNode(from).cast();
    }
}
