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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonArrayNodeTest extends JsonParentNodeTestCase<JsonArrayNode, List<JsonNode>> {

    // append

    @Test
    public void testAppendEmptyArray() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.array());
    }

    @Test
    public void testAppendBoolean() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.booleanNode(true));
    }

    @Test
    public void testAppendNumber() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.number(123));
    }

    @Test
    public void testAppendNull() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.nullNode());
    }

    @Test
    public void testAppendEmptyObject() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.object());
    }

    @Test
    public void testAppendString() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.string("string-abc-123"));
    }

    // append and get

    @Test
    public void testGet() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        this.childrenCheck(array);

        for(int i = 0; i < 2; i++) {
            final JsonStringNode element = array.children().get(i).cast();
            assertEquals(values[i], element.value(), "value");
        }

        // verify originals were not mutated.
        this.checkWithoutParent(first);
        this.checkWithoutParent(second);
    }

    @Test
    public void testSetChildrenSame() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(this.value1())
                .appendChild(this.value2());
        assertSame(array, array.setChildren(array.children()));
    }

    @Test
    public void testSetChildrenEquivalent() {
        final JsonStringNode value1 = this.value1();
        final JsonStringNode value2 = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(value1)
                .appendChild(value2);
        assertSame(array, array.setChildren(Lists.of(value1, value2)));
    }

    @Test
    public void testSetSame() {
        final JsonStringNode value1 = this.value1();
        final JsonStringNode value2 = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(value1)
                .appendChild(value2);
        assertSame(array, array.set(0, value1));
        assertSame(array, array.set(1, value2));
    }

    @Test
    public void testSetSame2() {
        final JsonStringNode value1 = this.value1();
        final JsonStringNode value2 = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(value1)
                .appendChild(value2);
        assertSame(array, array.set(0, array.get(0)));
        assertSame(array, array.set(1, array.get(1)));

    }

    @Test
    public void testSetDifferent() {
        final JsonStringNode value1 = this.value1();
        final JsonStringNode value2 = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(value1)
                .appendChild(value2);

        final String differentValue = "different-1";
        final JsonNode differentChild = JsonNode.string(differentValue);
        final JsonArrayNode different = array.set(1, differentChild);

        assertNotSame(array, different);
        this.childrenCheck(different);
        assertEquals(VALUE1, JsonStringNode.class.cast(different.children().get(0)).value(), "child[0].value");
        assertEquals(differentValue, JsonStringNode.class.cast(different.children().get(1)).value(), "child[1].value");

        // verify originals were not mutated.
        this.checkWithoutParent(value1);
        this.checkWithoutParent(value2);
        this.checkWithoutParent(differentChild);
    }

    @Test
    public void testSetExpands() {
        final JsonNode first = JsonNode.nullNode();
        final JsonNode second = JsonNode.nullNode();
        final JsonNode third = this.value3();

        this.checkEquals(JsonNode.array().appendChild(first).appendChild(second).appendChild(third),
                JsonNode.array().set(2, third));
    }

    @Test
    public void testSetExpands2() {
        final JsonNode first = this.value1();

        this.checkEquals(JsonNode.array().appendChild(first),
                JsonNode.array().set(0, first));
    }

    @Test
    public void testSetExpands3() {
        final JsonNode first = this.value1();
        final JsonNode second = JsonNode.nullNode();
        final JsonNode third = this.value3();

        final JsonArrayNode array = JsonNode.array().appendChild(first);

        this.checkEquals(JsonNode.array().appendChild(first).appendChild(second).appendChild(third),
                array.set(2, third));
    }

    @Test
    public void testSetExpands4() {
        final JsonNode first = this.value1();
        final JsonNode second = JsonNode.nullNode();
        final JsonNode third = JsonNode.nullNode();
        final JsonNode fourth = this.value4();

        final JsonArrayNode array = JsonNode.array().appendChild(first);

        this.checkEquals(JsonNode.array().appendChild(first).appendChild(second).appendChild(third).appendChild(fourth),
                array.set(3, fourth));
    }

    // remove

    @Test
    public void testRemoveNegativeIndexFails() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.createJsonNode().remove(-1);
        });
    }

    @Test
    public void testRemoveInvalidIndexFails() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.createJsonNode().remove(0);
        });
    }

    @Test
    public void testRemove() {
        final JsonNode first = this.value1();
        final JsonNode second = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(first)
                .appendChild(second);
        final JsonArrayNode removed = array.remove(0);

        assertNotSame(array, removed);
        this.childrenCheck(removed);
        assertEquals(VALUE2, JsonStringNode.class.cast(removed.children().get(0)).value(), "child[0].value");

        // verify originals were not mutated.
        this.checkWithoutParent(first);
        this.checkWithoutParent(second);
    }

    @Test
    public void testRemoveInvalidIndexFails2() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(this.value1())
                .appendChild(this.value2());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            array.remove(2);
        });
        this.childrenCheck(array);
        this.childCountCheck(array, 2);
    }

    @Test
    public void testReplaceChild() {
        final JsonArrayNode root = JsonNode.array()
                .appendChild(this.value1());
        this.childCountCheck(root, 1);

        final JsonArrayNode updated = root.replaceChild(root.get(0), this.value2())
                .cast();

        this.childCountCheck(updated, 1);
        assertEquals(VALUE2, JsonStringNode.class.cast(updated.get(0)).value());
    }

    @Test
    public void testReplaceChild2() {
        final JsonNode child1 = JsonNode.string("C1");

        final JsonArrayNode root = JsonNode.array()
                .appendChild(child1)
                .appendChild(JsonNode.array()
                        .appendChild(JsonNode.string("GC1-old")));
        this.childCountCheck(root, 2);

        final JsonNode grandChild1New = JsonNode.string("GC1-new");
        final JsonArrayNode updatedChild = root.get(1)
                .setChildren(Lists.of(grandChild1New))
                .cast();
        this.childCountCheck(updatedChild, 1);
        
        final JsonArrayNode updatedRoot = updatedChild.parent().get().cast();
        this.childCountCheck(updatedRoot, 2);
        assertEquals(JsonNode.array()
                        .appendChild(child1)
                        .appendChild(JsonNode.array()
                                .appendChild(grandChild1New)),
                updatedRoot,
                "updatedRoot");
    }

    // setLength ...........................................................................................

    @Test
    public void testSetLength() {
        final JsonArrayNode array = JsonNode.array();

        assertSame(array, array.setLength(0));
    }

    @Test
    public void testSetLength2() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(this.value1())
                .appendChild(this.value2());

        assertSame(array, array.setLength(2));
    }

    @Test
    public void testSetLengthShorter() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(this.value1())
                .appendChild(this.value2())
                .appendChild(this.value3());

        this.setLengthAndCheck(array,
                0);
    }

    @Test
    public void testSetLengthShorter2() {
        final JsonNode child1 = this.value1();
        final JsonNode child2 = this.value2();
        final JsonNode child3 = this.value3();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(child1)
                .appendChild(child2)
                .appendChild(child3);

        this.setLengthAndCheck(array,
                2,
                child1, child2);
    }

    @Test
    public void testSetLengthLonger() {
        this.setLengthAndCheck(JsonArrayNode.array(),
                1,
                JsonNode.nullNode());
    }

    @Test
    public void testSetLengthLonger2() {
        this.setLengthAndCheck(JsonArrayNode.array(),
                2,
                JsonNode.nullNode(), JsonNode.nullNode());
    }

    @Test
    public void testSetLengthLonger3() {
        final JsonNode child1 = this.value1();
        final JsonNode child2 = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(child1)
                .appendChild(child2);

        this.setLengthAndCheck(array,
                3,
                child1, child2, JsonNode.nullNode());
    }

    @Test
    public void testSetLengthLonger4() {
        final JsonNode child1 = this.value1();
        final JsonNode child2 = this.value2();

        final JsonArrayNode array = JsonNode.array()
                .appendChild(child1)
                .appendChild(child2);

        this.setLengthAndCheck(array,
                4,
                child1, child2, JsonNode.nullNode(), JsonNode.nullNode());
    }

    private void setLengthAndCheck(final JsonArrayNode array,
                                   final int length,
                                   final JsonNode... children) {
        assertEquals(length, children.length, "expected children length doesnt match setLength length");
        assertEquals(JsonNode.array().setChildren(Lists.of(children)),
                array.setLength(length),
                () -> "setLength " + length + " on " + array);
    }

    // Visitor ...........................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<JsonNode> visited = Lists.array();

        final JsonArrayNode array = this.createJsonNode()
                .appendChild(this.value1())
                .appendChild(this.value2());
        final JsonStringNode string1 = array.get(0).cast();
        final JsonStringNode string2 = array.get(1).cast();

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
            protected Visiting startVisit(final JsonArrayNode t) {
                assertSame(array, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonArrayNode t) {
                assertSame(array, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final JsonStringNode t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(array);
        assertEquals("1517217262", b.toString());
        assertEquals(Lists.of(array, array,
                        string1, string1, string1,
                        string2, string2, string2,
                        array, array),
                visited,
                "visited");
    }

    @Test
    public void testTextWithoutChildren() {
        assertEquals("", JsonNode.array().text());
    }

    @Test
    public void testTextWithChildren() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));
        assertEquals("true2.0third", array.text());
    }

    @Test
    public void testSelectorUsage() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));

        final JsonNode expected = array.get(1);

        this.selectorAcceptAndCheck(array,
                JsonNode.PATH_SEPARATOR.absoluteNodeSelectorBuilder(JsonNode.class)
                .descendant()
                .named(expected.name())
                .build(),
                expected);
    }

    // HasJsonNode.......................................................................................

    @Test
    public void testFromJsonNodeWithType() {
        this.fromJsonNodeWithTypeAndFail(JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeWithTypeList() {
        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array(), Lists.empty());
    }

    @Test
    public void testFromJsonNodeWithTypeList2() {
        final String string = "abc123";

        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string)),
                Lists.of(string));
    }

    @Test
    public void testFromJsonNodeWithTypeSet() {
        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array(), Sets.empty());
    }

    @Test
    public void testFromJsonNodeWithTypeSet2() {
        final String string = "abc123";

        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string)),
                Sets.of(string));
    }

    @Test
    public void testFromJsonNodeWithTypeMap() {
        this.fromJsonNodeWithTypeMapAndCheck(JsonNode.array(),
                Maps.empty());
    }

    @Test
    public void testFromJsonNodeWithTypeMap2() {
        final String key = "key1";
        final Color value = Color.fromRgb(0x123);

        this.fromJsonNodeWithTypeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value.toJsonNodeWithType())),
                Maps.of(key, value));
    }

    @Test
    public void testFromJsonNodeListBoolean() {
        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.booleanNode(true))
                        .appendChild(JsonNode.booleanNode(false)),
                Boolean.class,
                Lists.of(true, false));
    }

    @Test
    public void testFromJsonNodeListByte() {
        this.fromJsonNodeListNumberAndCheck((byte)1, Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.class);
    }

    @Test
    public void testFromJsonNodeListShort() {
        this.fromJsonNodeListNumberAndCheck((short)1, Short.MAX_VALUE, Short.MIN_VALUE, Short.class);
    }

    @Test
    public void testFromJsonNodeListInteger() {
        this.fromJsonNodeListNumberAndCheck(1, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.class);
    }

    @Test
    public void testFromJsonNodeListLong() {
        this.fromJsonNodeListNumberAndCheck(1L, 2L, -3L, Long.class);
    }

    @Test
    public void testFromJsonNodeListFloat() {
        this.fromJsonNodeListNumberAndCheck(1.0f, Float.MAX_VALUE, Float.MIN_VALUE, Float.class);
    }

    @Test
    public void testFromJsonNodeListDouble() {
        this.fromJsonNodeListNumberAndCheck(1.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.class);
    }
    
    private <N extends Number> void fromJsonNodeListNumberAndCheck(final N number1,
                                                final N number2,
                                                final N number3,
                                                                   final Class<N> type) {
        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.number(number1.doubleValue()))
                        .appendChild(JsonNode.number(number2.doubleValue()))
                        .appendChild(JsonNode.number(number3.doubleValue())),
                type,
                Lists.of(number1, number2, number3));
    }

    @Test
    public void testFromJsonNodeListString() {
        final String string1 = "a1";
        final String string2 = "b2";
        final String string3 = "c3";

        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2))
                        .appendChild(JsonNode.string(string3)),
                String.class,
                Lists.of(string1, string2, string3));
    }

    @Test
    public void testFromJsonNodeListHasJson() {
        final Color color1 = Color.fromRgb(0x122);
        final Color color2 = Color.fromRgb(0x222);
        final Color color3 = Color.fromRgb(0x333);

        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(color1.toJsonNode())
                        .appendChild(color2.toJsonNode())
                        .appendChild(color3.toJsonNode()),
                Color.class,
                Lists.of(color1, color2, color3));
    }

    @Test
    public void testFromJsonNodeListJsonNode() {
        final JsonNode json1 = JsonNode.booleanNode(true);
        //final JsonNode json2 = JsonNode.nullNode();
        final JsonNode json3 = JsonNode.number(123.5);
        final JsonNode json4 = JsonNode.string("abc123");
        final JsonNode json5 = JsonNode.array()
                .appendChild(JsonNode.number(2));
        final JsonNode json6 = JsonNode.object();

        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(json1)
                        //              .appendChild(json2)
                        .appendChild(json3)
                        .appendChild(json4)
                        .appendChild(json5)
                        .appendChild(json6),
                JsonNode.class,
                Lists.of(json1, json3, json4, json5, json6));
    }

    @Test
    public void testFromJsonNodeSetBoolean() {
        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.booleanNode(true))
                        .appendChild(JsonNode.booleanNode(false)),
                Boolean.class,
                Sets.of(true, false));
    }

    @Test
    public void testFromJsonNodeSetByte() {
        this.fromJsonNodeSetNumberAndCheck((byte)1, Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.class);
    }

    @Test
    public void testFromJsonNodeSetShort() {
        this.fromJsonNodeSetNumberAndCheck((short)1, Short.MAX_VALUE, Short.MIN_VALUE, Short.class);
    }

    @Test
    public void testFromJsonNodeSetInteger() {
        this.fromJsonNodeSetNumberAndCheck(1, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.class);
    }

    @Test
    public void testFromJsonNodeSetLong() {
        this.fromJsonNodeSetNumberAndCheck(1L, 2L, -3L, Long.class);
    }

    @Test
    public void testFromJsonNodeSetFloat() {
        this.fromJsonNodeSetNumberAndCheck(1.0f, Float.MAX_VALUE, Float.MIN_VALUE, Float.class);
    }

    @Test
    public void testFromJsonNodeSetDouble() {
        this.fromJsonNodeSetNumberAndCheck(1.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.class);
    }

    private <N extends Number> void fromJsonNodeSetNumberAndCheck(final N number1,
                                                                   final N number2,
                                                                   final N number3,
                                                                   final Class<N> type) {
        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.number(number1.doubleValue()))
                        .appendChild(JsonNode.number(number2.doubleValue()))
                        .appendChild(JsonNode.number(number3.doubleValue())),
                type,
                Sets.of(number1, number2, number3));
    }

    @Test
    public void testFromJsonNodeSetString() {
        final String string1 = "a1";
        final String string2 = "b2";
        final String string3 = "c3";

        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2))
                        .appendChild(JsonNode.string(string3)),
                String.class,
                Sets.of(string1, string2, string3));
    }

    @Test
    public void testFromJsonNodeSetHasJson() {
        final Color color1 = Color.fromRgb(0x122);
        final Color color2 = Color.fromRgb(0x222);
        final Color color3 = Color.fromRgb(0x333);

        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(color1.toJsonNode())
                        .appendChild(color2.toJsonNode())
                        .appendChild(color3.toJsonNode()),
                Color.class,
                Sets.of(color1, color2, color3));
    }

    @Test
    public void testFromJsonNodeSetJsonNode() {
        final JsonNode json1 = JsonNode.booleanNode(true);
        //final JsonNode json2 = JsonNode.nullNode();
        final JsonNode json3 = JsonNode.number(123.5);
        final JsonNode json4 = JsonNode.string("abc123");
        final JsonNode json5 = JsonNode.array()
                .appendChild(JsonNode.number(2));
        final JsonNode json6 = JsonNode.object();

        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(json1)
                        //              .appendChild(json2)
                        .appendChild(json3)
                        .appendChild(json4)
                        .appendChild(json5)
                        .appendChild(json6),
                JsonNode.class,
                Sets.of(json1, json3, json4, json5, json6));
    }

    // fromJsonNodeMap.....................................................................................

    @Test
    public void testFromJsonNodeMapBoolean() {
        this.fromJsonNodeMapAndCheck(Boolean.TRUE, Boolean.FALSE, Boolean.class, Boolean.class);
    }

    @Test
    public void testFromJsonNodeMapNumber() {
        this.fromJsonNodeMapAndCheck(1.0, 2.0, Number.class, Number.class);
    }

    @Test
    public void testFromJsonNodeMapString() {
        this.fromJsonNodeMapAndCheck("key1", "value1", String.class, String.class);
    }

    @Test
    public void testFromJsonNodeMapHasJsonNode() {
        this.fromJsonNodeMapAndCheck(Color.fromRgb(0x111),
                Color.fromRgb(0x222),
                Color.class,
                Color.class);
    }

    @Test
    public void testFromJsonNodeMap2() {
        final String key1 = "key1";
        final Color value1 = Color.fromRgb(0x111);

        final String key2 = "key2";
        final Color value2 = Color.fromRgb(0x222);

        this.fromJsonNodeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key1))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value1.toJsonNode()))
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key2))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value2.toJsonNode())),
                Maps.of(key1, value1, key2, value2),
                String.class,
                Color.class);
    }

    private <K, V> void fromJsonNodeMapAndCheck(final K key,
                                                final V value,
                                                final Class<K> keyType,
                                                final Class<V> valueType) {
        this.fromJsonNodeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, HasJsonNodeMapper.toJsonNodeObject(key))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, HasJsonNodeMapper.toJsonNodeObject(value))),
                Maps.of(key, value),
                keyType,
                valueType);
    }

    private <K, V> void fromJsonNodeMapAndCheck(final JsonArrayNode node,
                                                final Map<K, V> map,
                                                final Class<K> keyType,
                                                final Class<V> valueType) {
        assertEquals(map,
                node.fromJsonNodeMap(keyType, valueType),
                () -> "fromJsonNode(Map) failed: " + node);
    }

    // fromJsonNodeWithType List, element type..........................................................................

    @Test
    public void testFromJsonNodeWithTypeListByte() {
        this.fromJsonNodeWithTypeListAndCheck(Byte.MAX_VALUE, Byte.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListShort() {
        this.fromJsonNodeWithTypeListAndCheck(Short.MAX_VALUE, Short.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListInteger() {
        this.fromJsonNodeWithTypeListAndCheck(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListLong() {
        this.fromJsonNodeWithTypeListAndCheck(Long.MAX_VALUE, Long.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListFloat() {
        this.fromJsonNodeWithTypeListAndCheck(Float.MAX_VALUE, Float.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListDouble() {
        this.fromJsonNodeWithTypeListAndCheck(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    private void fromJsonNodeWithTypeListAndCheck(final Number number1,
                                                  final Number number2) {
        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(HasJsonNode.toJsonNodeWithType(number1))
                        .appendChild(HasJsonNode.toJsonNodeWithType(number2)),
                number1, number2);
    }

    @Test
    public void testFromJsonNodeWithTypeListString() {
        final String string1 = "a1";
        final String string2 = "b2";

        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2)),
                string1, string2);
    }

    @Test
    public void testFromJsonNodeWithTypeListHasJsonNode() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(color1.toJsonNodeWithType())
                        .appendChild(color2.toJsonNodeWithType()),
                color1, color2);
    }

    private void fromJsonNodeWithTypeListAndCheck(final JsonArrayNode array,
                                                  final Object... values) {
        assertEquals(Lists.of(values),
                array.fromJsonNodeWithTypeList(),
                () -> "fromJsonNodeWithTypeList() failed: " + array);
    }

    // fromJsonNodeWithType Set, element type..........................................................................

    @Test
    public void testFromJsonNodeWithTypeSetBoolean() {
        final Boolean boolean1 = true;
        final Boolean boolean2 = false;

        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.booleanNode(boolean1))
                        .appendChild(JsonNode.booleanNode(boolean2)),
                boolean1, boolean2);
    }

    @Test
    public void testFromJsonNodeWithTypeSetByte() {
        this.fromJsonNodeWithTypeSetAndCheck(Byte.MAX_VALUE, Byte.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetShort() {
        this.fromJsonNodeWithTypeSetAndCheck(Short.MAX_VALUE, Short.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetInteger() {
        this.fromJsonNodeWithTypeSetAndCheck(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetFloat() {
        this.fromJsonNodeWithTypeSetAndCheck(Float.MAX_VALUE, Float.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetDouble() {
        this.fromJsonNodeWithTypeSetAndCheck(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    private void fromJsonNodeWithTypeSetAndCheck(final Number number1,
                                                 final Number number2) {
        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(HasJsonNode.toJsonNodeWithType(number1))
                        .appendChild(HasJsonNode.toJsonNodeWithType(number2)),
                number1, number2);
    }

    @Test
    public void testFromJsonNodeWithTypeSetLong() {
        final Long long1 = 1L;
        final Long long2 = 2L;

        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(HasJsonNode.toJsonNodeWithType(long1))
                        .appendChild(HasJsonNode.toJsonNodeWithType(long2)),
                long1, long2);
        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(HasJsonNode.toJsonNodeWithType(long1))
                        .appendChild(HasJsonNode.toJsonNodeWithType(long2)),
                long1, long2);
    }

    @Test
    public void testFromJsonNodeWithTypeSetString() {
        final String string1 = "a1";
        final String string2 = "b2";

        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2)),
                string1, string2);
    }

    @Test
    public void testFromJsonNodeWithTypeSetHasJsonNode() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(color1.toJsonNodeWithType())
                        .appendChild(color2.toJsonNodeWithType()),
                color1, color2);
    }

    private void fromJsonNodeWithTypeSetAndCheck(final JsonArrayNode array,
                                                 final Object... values) {
        assertEquals(Sets.of(values),
                array.fromJsonNodeWithTypeSet(),
                "fromJsonNodeWithTypeSet() failed: " + array);
    }

    // toSearchNode .......................................................................................

    @Test
    public void testToSearchNode() {
        final JsonBooleanNode booleanNode = JsonNode.booleanNode(true);
        final JsonNumberNode number = JsonNode.number(2);
        final JsonStringNode string = JsonNode.string("third");

        final JsonArrayNode array = JsonNode.array()
                .appendChild(booleanNode)
                .appendChild(number)
                .appendChild(string);

        this.toSearchNodeAndCheck(array, SearchNode.sequence(Lists.of(booleanNode.toSearchNode(),
                number.toSearchNode(),
                string.toSearchNode())));
    }

    // HashCodeAnddEqualityDefined.......................................................

    @Test
    public void testEqualsDifferentChildren() {
        this.checkNotEquals(JsonNode.array().appendChild(JsonNode.string("different")));
    }

    @Test
    public void testEqualsDifferentChildren2() {
        this.checkNotEquals(JsonNode.array().appendChild(JsonNode.string("child1")),
                JsonNode.array().appendChild(JsonNode.string("child2")));
    }

    @Test
    public void testEqualsArray() {
        this.checkNotEquals(JsonNode.array().appendChild(JsonNode.string("child2")),
                JsonNode.object().set(JsonNodeName.with("prop"), JsonNode.string("child1")));
    }

    @Test
    public void testEqualsArrayAndObjectElement() {
        this.checkNotEquals(
                JsonNode.array().appendChild(JsonNode.array().appendChild(JsonNode.string("element1"))),
                JsonNode.array().appendChild(JsonNode.object().set(JsonNodeName.with("element-prop"), JsonNode.string("element-value"))));
    }

    // toString .......................................................................................

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(this.createJsonNode(), "[]");
    }

    @Test
    public void testToStringWithChildren() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));

        this.toStringAndCheck(array, "[true, 2, \"third\"]");
    }

    @Override
    JsonArrayNode createJsonNode() {
        return JsonArrayNode.array();
    }

    @Override
    String nodeTypeName() {
        return "json-array";
    }

    @Override
    Class<JsonArrayNode> jsonNodeType() {
        return JsonArrayNode.class;
    }

    // NodeTesting2................................................................................................

    @Override
    public JsonArrayNode appendChildAndCheck(final JsonNode parent,
                                             final JsonNode child) {
        final JsonArrayNode newParent = parent.appendChild(child).cast();
        assertNotSame(newParent, parent, "appendChild must not return the same node");

        final List<JsonNode> children = newParent.children();
        assertNotEquals(0, children.size(), "children must have at least 1 child");

        final int lastIndex = children.size() - 1;
        assertEquals(JsonNodeName.index(lastIndex), children.get(lastIndex).name(), "last child must be the added child");

        this.childrenParentCheck(newParent);
        this.checkWithoutParent(child);

        return newParent;
    }

    // HasJsonNodeTesting..................................................................

    @Override
    public final JsonArrayNode fromJsonNode(final JsonNode from) {
        return JsonArrayNode.fromJsonNode(from).cast();
    }

    @Override
    List<String> propertiesNeverReturnNullSkipProperties() {
        return Lists.of(BOOLEAN_VALUE_OR_FAIL,
                FROM_WITH_TYPE_LIST,
                FROM_WITH_TYPE_SET,
                FROM_WITH_TYPE_MAP,
                FROM_WITH_TYPE,
                NUMBER_VALUE_OR_FAIL,
                OBJECT_OR_FAIL,
                STRING_VALUE_OR_FAIL,
                VALUE);
    }
}
