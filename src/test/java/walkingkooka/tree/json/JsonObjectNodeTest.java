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
import walkingkooka.collect.map.MapTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonObjectNodeTest extends JsonParentNodeTestCase<JsonObjectNode, JsonObjectNodeList>
        implements MapTesting<Map<JsonNodeName, JsonNode>, JsonNodeName, JsonNode> {

    private final static String KEY1 = "key1";
    private final static String KEY2 = "key2";
    private final static String KEY3 = "key3";
    private final static String KEY4 = "key4";

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

        assertSame(object, object.setChildren(Lists.of(value1.setName(key1))));
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

    @Test
    public void testSetReplacesAndGet() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();
        final JsonStringNode value3 = this.value3();

        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2)
                .set(key1, value3);

        this.childrenCheck(object);
        this.childCountCheck(object, 2);
        this.getAndCheck(object, key1, VALUE3);
        this.getAndCheck(object, key2, VALUE2);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.childCountCheck(empty, 0);
    }

    @Test
    public void testSetReplacesAndGet2() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();
        final JsonStringNode value3 = this.value3();

        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2)
                .set(key2, value3);

        this.childrenCheck(object);
        this.childCountCheck(object, 2);
        this.getAndCheck(object, key1, VALUE1);
        this.getAndCheck(object, key2, VALUE3);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);

        this.childCountCheck(empty, 0);
    }

    @Test
    public void testSetReplacesAndGet3() {
        final JsonNodeName key1 = this.key1();
        final JsonStringNode value1 = this.value1();

        final JsonNodeName key2 = this.key2();
        final JsonStringNode value2 = this.value2();

        final JsonNodeName key3 = this.key3();
        final JsonStringNode value3 = this.value3();

        final JsonStringNode value4 = this.value4();

        final JsonObjectNode empty = JsonNode.object();

        final JsonObjectNode object = empty.set(key1, value1)
                .set(key2, value2)
                .set(key3, value3)
                .set(key3, value4);

        this.childrenCheck(object);
        this.childCountCheck(object, 3);
        this.getAndCheck(object, key1, VALUE1);
        this.getAndCheck(object, key2, VALUE2);
        this.getAndCheck(object, key3, VALUE4);

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);
        this.checkWithoutParent(value3);
        this.checkWithoutParent(value4);

        this.childCountCheck(empty, 0);
    }

    @Test
    public void testSetChildrenSameKeyDifferentValueType() {
        final JsonNodeName key1 = this.key1();
        final JsonNode value1 = this.value1();

        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1);

        final JsonNode value2 = JsonNode.object();
        final JsonObjectNode object2 = JsonNode.object()
                .set(key1, value2);

        assertNotSame(object, object2);
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
                        .set(key1(), this.value1()),
                JsonNodeName.with("different-prop"),
                false);
    }

    @Test
    public void testContainsPresent() {
        final JsonNodeName key = this.key1();
        this.containsAndCheck(JsonNode.object()
                        .set(key1(), this.value1())
                        .set(key, this.value2()),
                key,
                true);
    }

    private void containsAndCheck(final JsonObjectNode object, final JsonNodeName name, final boolean contains) {
        assertEquals(contains, object.contains(name), () -> object + " contains " + name);
    }

    // replace ....................................................................................................

    @Test
    public void testReplaceRoot() {
        this.replaceAndCheck(JsonNode.object()
                        .set(this.key1(), this.value1()),
                JsonNode.object()
                        .set(this.key2(), this.value2()));
    }

    @Test
    public void testReplace() {
        final JsonObjectNode root = JsonNode.object()
                .set(this.key1(), this.value1());

        final JsonObjectNode value2 = JsonNode.object()
                .set(this.key3(), this.value3());

        final JsonObjectNode replacement2 = JsonNode.object()
                .set(this.key4(), this.value4());

        final JsonNodeName key2 = this.key2();

        this.replaceAndCheck(root.set(key2, value2).getOrFail(key2),
                replacement2,
                root.set(key2, replacement2).getOrFail(key2));
    }

    // asMap.......................................................................................................

    @Test
    public void testMapWhenEmpty() {
        this.checkEquals(Maps.empty(), JsonNode.object().asMap());
    }

    @Test
    public void testMapWhenReadonly() {
        assertThrows(UnsupportedOperationException.class, () -> {
            JsonNode.object().asMap().put(this.key1(), this.value1());
        });
    }

    @Test
    public void testMapWhenReadonly2() {
        final JsonNodeName key1 = this.key1();
        final JsonObjectNode value1 = JsonNode.object()
                .set(key1, this.value1());

        assertThrows(UnsupportedOperationException.class, () -> {
            value1.asMap().remove(key1);
        });
    }

    @Test
    public void testMapContainsKey() {
        final JsonNodeName key1 = key1();
        final JsonNode value1 = this.value1();
        ;

        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1)
                .set(this.key2(), this.value2());
        this.containsKeyAndCheck(object.asMap(), key1);
    }

    @Test
    public void testMapContainsKeyAbsent() {
        final JsonNodeName key1 = key1();
        final JsonNode value1 = this.value1();
        ;

        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1)
                .set(this.key2(), this.value2());
        this.containsKeyAndCheckAbsent(object.asMap(), JsonNodeName.with("absent-property"));
    }

    @Test
    public void testMapContainsValue() {
        final JsonNodeName key1 = key1();
        final JsonNode value1 = this.value1();
        ;

        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1)
                .set(this.key2(), this.value2());
        this.containsValueAndCheck(object.asMap(),
                object.getOrFail(key1));
    }

    @Test
    public void testMapContainsValueAbsent() {
        final JsonNodeName key1 = key1();
        final JsonNode value1 = this.value1();
        ;

        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1)
                .set(this.key2(), this.value2());
        this.containsValueAndCheckAbsent(object.asMap(),
                value1); // match fails because value gotten has a parent the enclosing object
    }

    @Test
    public void testMapGet() {
        final JsonNodeName key1 = key1();
        final JsonNode value1 = this.value1();
        ;

        final JsonObjectNode object = JsonNode.object()
                .set(key1, value1)
                .set(this.key2(), this.value2());
        this.getAndCheck(object.asMap(), key1, object.getOrFail(key1));
    }

    @Test
    public void testMapGetAbsentKey() {
        final JsonNodeName key = key1();
        final JsonNode value = this.value1();
        ;

        final JsonObjectNode object = JsonNode.object()
                .set(key, value)
                .set(this.key2(), this.value2());
        this.getAndCheckAbsent(object.asMap(), JsonNodeName.with("absent-property"));
    }

    // equals.......................................................................................................

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
        final JsonNode value = this.value1();

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

    @Test
    public void testEqualsSameKeyDifferentValueType() {
        final JsonObjectNode object = JsonNode.object();
        final JsonNodeName property = this.property();

        this.checkNotEquals(
                object.set(property, JsonNode.object()),
                object.set(property, JsonNode.string("string")));
    }

    @Test
    public void testEqualsSameKeyDifferentValueType2() {
        final JsonObjectNode object = JsonNode.object();
        final JsonNodeName property = this.property();

        this.checkNotEquals(
                object.set(property, JsonNode.object()),
                object.set(property, JsonNode.array()));
    }

    private JsonNodeName property() {
        return JsonNodeName.with("property");
    }

    @Test
    public void testArrayOrFail() {
        assertThrows(JsonNodeException.class, () -> {
            this.createJsonNode().arrayOrFail();
        });
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
    public void testFromJsonNodeListFails() {
        this.fromJsonNodeListAndFail(Void.class, JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeSetFails() {
        this.fromJsonNodeSetAndFail(Void.class, JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeMapFails() {
        this.fromJsonNodeMapAndFail(Void.class, Void.class, JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeListWithTypeFails() {
        this.fromJsonNodeWithTypeListAndFail(JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeSetWithTypeFails() {
        this.fromJsonNodeWithTypeSetAndFail(JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeMapWithTypeFails() {
        this.fromJsonNodeWithTypeMapAndFail(JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeWithType() {
        final String value = "abc123";

        this.fromJsonNodeWithTypeAndCheck(JsonNode.object()
                        .set(JsonObjectNode.TYPE, JsonNode.string("string"))
                        .set(JsonObjectNode.VALUE, JsonNode.string(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeMissingTypePropertyFails() {
        this.fromJsonNodeWithTypeAndFail(JsonNode.object()
                        .set(JsonObjectNode.VALUE, JsonNode.string("abc")),
                IllegalArgumentException.class);
    }

    @Test
    public void testFromJsonNodeWithTypeIncorrectTypePropertyFails() {
        this.fromJsonNodeWithTypeAndFail(JsonNode.object()
                        .set(JsonObjectNode.TYPE, JsonNode.number(123)),
                IllegalArgumentException.class);
    }

    @Test
    public void testFromJsonNodeWithTypeMissingValuePropertyFails() {
        this.fromJsonNodeWithTypeAndFail(JsonNode.object()
                        .set(JsonObjectNode.TYPE, JsonNode.string("string")),
                IllegalArgumentException.class);
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

    private JsonNodeName key4() {
        return JsonNodeName.with(KEY4);
    }

    @Override
    Class<JsonObjectNode> jsonNodeType() {
        return JsonObjectNode.class;
    }

    @Override
    String nodeTypeName() {
        return "json-object";
    }

    // HasJsonNodeTesting..................................................................

    @Override
    public final JsonObjectNode fromJsonNode(final JsonNode from) {
        return JsonObjectNode.fromJsonNode(from).cast();
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
                PARENT_OR_FAIL,
                STRING_VALUE_OR_FAIL,
                VALUE);
    }

    // MapTesting..........................................................................................

    @Override
    public final Map<JsonNodeName, JsonNode> createMap() {
        return JsonNode.object().asMap();
    }

    @Override
    public String typeNameSuffix() {
        return Node.class.getSimpleName();
    }
}
