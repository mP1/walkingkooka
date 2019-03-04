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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HasJsonNodeMapperTest extends HasJsonNodeMapperTestCase<HasJsonNodeMapper<Void>, Void> {

    // register..............................................................................

    @Test
    public void testRegisterNullTypeNameFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.register(null, JsonNode::fromJsonNode, Object.class);
        });
    }

    @Test
    public void testRegisterEmptyTypeNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.register("", JsonNode::fromJsonNode, Object.class);
        });
    }

    @Test
    public void testRegisterNullFactoryFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.register("!", null, Object.class);
        });
    }

    @Test
    public void testRegisterNullClassesFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.register("!", JsonNode::fromJsonNode, null);
        });
    }

    // fromJsonNodeWithType Object ..............................................................................

    @Test
    public void testFromJsonNodeAndTypeNullNodeFails() {
        this.fromJsonNodeAndTypeAndFail(null, Object.class, NullPointerException.class);
    }

    @Test
    public void testFromJsonNodeAndTypeNullTypeFails() {
        this.fromJsonNodeAndTypeAndFail(JsonNode.string("1a"), null, NullPointerException.class);
    }

    @Test
    public void testFromJsonNodeAndTypeJsonBooleanTrue() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.booleanNode(true), Boolean.class, true);
    }

    @Test
    public void testFromJsonNodeAndTypeJsonBooleanFalse() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.booleanNode(false), Boolean.class, false);
    }

    @Test
    public void testFromJsonNodeAndTypeByte() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.number(1), (byte) 1);
    }

    @Test
    public void testFromJsonNodeAndTypeByteWithTypeFail() {
        this.fromJsonNodeAndTypeAndFail(HasJsonNodeMapMapper.toJsonNodeWithType((byte) 1), Byte.class, JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeAndTypeShort() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.number(1), (short) 1);
    }

    @Test
    public void testFromJsonNodeAndTypeInteger() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.number(1), 1);
    }

    @Test
    public void testFromJsonNodeAndTypeLong() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.wrapLong(1), 1L);
    }

    @Test
    public void testFromJsonNodeAndTypeFloat() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.number(1.5), 1.5f);
    }

    @Test
    public void testFromJsonNodeAndTypeDouble() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.number(1.5), 1.5);
    }

    @Test
    public void testFromJsonNodeAndTypeJsonNodeNull() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.nullNode(), String.class, null);
    }

    @Test
    public void testFromJsonNodeAndTypeString() {
        this.fromJsonNodeAndTypeAndCheck(JsonNode.string("abc123"), "abc123");
    }

    @Test
    public void testFromJsonNodeAndTypeHasJsonNode() {
        final EmailAddress email = EmailAddress.parse("user@example.com");

        this.fromJsonNodeAndTypeAndCheck(email.toJsonNode(), email);
    }

    @Test
    public void testFromJsonNodeAndTypeHasJsonNode2() {
        final Color color = Color.fromRgb(0x123);

        this.fromJsonNodeAndTypeAndCheck(color.toJsonNode(), color);
    }

    private <T> void fromJsonNodeAndTypeAndCheck(final JsonNode node,
                                                 final T value) {
        this.fromJsonNodeAndTypeAndCheck(node, Cast.to(value.getClass()), value);
    }

    private <T> void fromJsonNodeAndTypeAndCheck(final JsonNode node,
                                                 final Class<T> type,
                                                 final T value) {
        assertEquals(value,
                HasJsonNodeMapMapper.fromJsonNode(node, type),
                () -> "fromJsonNode(JsonNode, Class) failed=" + node + " " + type.getName());
    }

    private void fromJsonNodeAndTypeAndFail(final JsonNode node,
                                            final Class<?> type,
                                            final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            HasJsonNodeMapper.fromJsonNode(node, type);
        });
    }

    // fromJsonNodeList, element type..........................................................................

    @Test
    public void testFromJsonNodeListNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeList(null, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeListNullElementTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeList(JsonNode.array(), null);
        });
    }

    @Test
    public void testFromJsonNodeListBooleanFails() {
        this.fromJsonNodeListFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeListJsonNullNode() {
        this.fromJsonNodeListAndCheck(JsonNode.nullNode(),
                Object.class,
                null);
    }

    @Test
    public void testFromJsonNodeListNumberFails() {
        this.fromJsonNodeListFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeListStringFails() {
        this.fromJsonNodeListFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeListObjectFails() {
        this.fromJsonNodeListFails(JsonNode.object());
    }

    private void fromJsonNodeListFails(final JsonNode list) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeList(list, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeListNull() {
        this.fromJsonNodeListAndCheck(null, Boolean.class);
    }

    @Test
    public void testFromJsonNodeListBooleanTrue() {
        this.fromJsonNodeListAndCheck(Boolean.TRUE, Boolean.class);
    }

    @Test
    public void testFromJsonNodeListBooleanFalse() {
        this.fromJsonNodeListAndCheck(Boolean.FALSE, Boolean.class);
    }

    @Test
    public void testFromJsonNodeListNumber() {
        this.fromJsonNodeListAndCheck(123.5, Number.class);
    }

    @Test
    public void testFromJsonNodeListString() {
        this.fromJsonNodeListAndCheck("abc123", String.class);
    }

    @Test
    public void testFromJsonNodeList() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(color1.toJsonNode())
                        .appendChild(color2.toJsonNode()),
                Color.class,
                Lists.of(color1, color2));
    }

    @Test
    public void testFromJsonNodeList2() {
        final String string1 = "value1";
        final String string2 = "value2";

        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2)),
                String.class,
                Lists.of(string1, string2));
    }

    private <E> void fromJsonNodeListAndCheck(final E value,
                                              final Class<E> elementType) {
        this.fromJsonNodeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.wrapOrFail(value)),
                elementType,
                Lists.of(value));
    }

    private <E> void fromJsonNodeListAndCheck(final JsonNode node,
                                              final Class<E> elementType,
                                              final List<E> list) {
        assertEquals(list,
                HasJsonNodeMapper.fromJsonNodeList(node, elementType),
                () -> "fromJsonNodeList(List) failed=" + node);
    }

    // fromJsonNodeSet, element type..........................................................................

    @Test
    public void testFromJsonNodeSetNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeSet(null, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeSetNullElementTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeSet(JsonNode.array(), null);
        });
    }

    @Test
    public void testFromJsonNodeSetBooleanFails() {
        this.fromJsonNodeSetFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeSetJsonNullNode() {
        this.fromJsonNodeSetAndCheck(JsonNode.nullNode(),
                Object.class,
                null);
    }

    @Test
    public void testFromJsonNodeSetNumberFails() {
        this.fromJsonNodeSetFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeSetStringFails() {
        this.fromJsonNodeSetFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeSetObjectFails() {
        this.fromJsonNodeSetFails(JsonNode.object());
    }

    private void fromJsonNodeSetFails(final JsonNode set) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeSet(set, String.class);
        });
    }

    @Test
    public void testFromJsonNodeSetNull() {
        this.fromJsonNodeSetAndCheck(null, Boolean.class);
    }

    @Test
    public void testFromJsonNodeSetBooleanTrue() {
        this.fromJsonNodeSetAndCheck(Boolean.TRUE, Boolean.class);
    }

    @Test
    public void testFromJsonNodeSetBooleanFalse() {
        this.fromJsonNodeSetAndCheck(Boolean.FALSE, Boolean.class);
    }

    @Test
    public void testFromJsonNodeSetNumber() {
        this.fromJsonNodeSetAndCheck(123.5, Number.class);
    }

    @Test
    public void testFromJsonNodeSetString() {
        this.fromJsonNodeSetAndCheck("abc123", String.class);
    }

    @Test
    public void testFromJsonNodeSet() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(color1.toJsonNode())
                        .appendChild(color2.toJsonNode()),
                Color.class,
                Sets.of(color1, color2));
    }

    @Test
    public void testFromJsonNodeSet2() {
        final String string1 = "value1";
        final String string2 = "value2";

        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2)),
                String.class,
                Sets.of(string1, string2));
    }

    private <E> void fromJsonNodeSetAndCheck(final E value,
                                             final Class<E> elementType) {
        this.fromJsonNodeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.wrapOrFail(value)),
                elementType,
                Sets.of(value));
    }

    private <E> void fromJsonNodeSetAndCheck(final JsonNode node,
                                             final Class<E> elementType,
                                             final Set<E> set) {
        assertEquals(set,
                HasJsonNodeMapper.fromJsonNodeSet(node, elementType),
                () -> "fromJsonNodeSet(Set) failed=" + node);
    }

    // fromJsonNodeMap, element type..........................................................................

    @Test
    public void testFromJsonNodeMapNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeMap(null, Object.class, Object.class);
        });
    }

    @Test
    public void testFromJsonNodeMapNullKeyTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeMap(JsonNode.array(), null, Object.class);
        });
    }

    @Test
    public void testFromJsonNodeMapNullValueTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeMap(JsonNode.array(), Object.class, null);
        });
    }

    @Test
    public void testFromJsonNodeMapBooleanFails() {
        this.fromJsonNodeMapFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeMapJsonNullNodeFails() {
        assertEquals(null,
                HasJsonNode.fromJsonNodeMap(JsonNode.nullNode(), Object.class, Object.class));
    }

    @Test
    public void testFromJsonNodeMapNumberFails() {
        this.fromJsonNodeMapFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeMapStringFails() {
        this.fromJsonNodeMapFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeMapObjectFails() {
        this.fromJsonNodeMapFails(JsonNode.object());
    }

    private void fromJsonNodeMapFails(final JsonNode map) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeMap(map, String.class, String.class);
        });
    }

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

        final Map<String, Color> map = Maps.ordered();
        map.put(key1, value1);
        map.put(key2, value2);

        this.fromJsonNodeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key1))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value1.toJsonNode()))
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key2))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value2.toJsonNode())),
                map,
                String.class,
                Color.class);
    }

    private <K, V> void fromJsonNodeMapAndCheck(final K key,
                                                final V value,
                                                final Class<K> keyType,
                                                final Class<V> valueType) {
        this.fromJsonNodeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.wrapOrFail(key))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, JsonNode.wrapOrFail(value))),
                Maps.one(key, value),
                keyType,
                valueType);
    }

    private <K, V> void fromJsonNodeMapAndCheck(final JsonNode node,
                                                final Map<K, V> map,
                                                final Class<K> keyType,
                                                final Class<V> valueType) {
        assertEquals(map,
                HasJsonNodeMapper.fromJsonNodeMap(node, keyType, valueType),
                () -> "fromJsonNode(Map) failed: " + node);
    }

    // fromJsonNodeWithType .....................................................................................

    @Test
    public void testFromJsonNodeWithTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithType(null);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeBooleanTrue() {
        this.fromJsonNodeWithTypeAndCheck(JsonNode.booleanNode(true), true);
    }

    @Test
    public void testFromJsonNodeWithTypeBooleanFalse() {
        this.fromJsonNodeWithTypeAndCheck(JsonNode.booleanNode(false), false);
    }

    @Test
    public void testFromJsonNodeWithTypeJsonNullNode() {
        this.fromJsonNodeWithTypeAndCheck(JsonNode.nullNode(), null);
    }

    @Test
    public void testFromJsonNodeWithTypeNumber() {
        this.fromJsonNodeWithTypeAndCheck(JsonNode.number(123), 123.0);
    }

    @Test
    public void testFromJsonNodeWithTypeString() {
        this.fromJsonNodeWithTypeAndCheck(JsonNode.string("abc123"), "abc123");
    }

    @Test
    public void testFromJsonNodeWithTypeObjectFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithType(JsonNode.object());
        });
    }

    @Test
    public void testFromJsonNodeWithTypeUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithType(JsonNode.object().set(HasJsonNodeMapper.TYPE, JsonNode.string(this.getClass().getName())));
        });
    }

    private void fromJsonNodeWithTypeAndCheck(final JsonNode node, final Object value) {
        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(node),
                () -> "fromJsonNodeWithType(JsonNode) failed=" + node);
    }


    // fromJsonNodeWithType List, element type..........................................................................

    @Test
    public void testFromJsonNodeWithTypeListNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithTypeList(null);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeListBooleanFails() {
        this.fromJsonNodeWithTypeListFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeWithTypeListNull() {
        this.fromJsonNodeWithTypeListAndCheck(JsonNode.nullNode(), null);
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberFails() {
        this.fromJsonNodeWithTypeListFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeWithTypeListStringFails() {
        this.fromJsonNodeWithTypeListFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeWithTypeListObjectFails() {
        this.fromJsonNodeWithTypeListFails(JsonNode.object());
    }

    private void fromJsonNodeWithTypeListFails(final JsonNode list) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithTypeList(list);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeListByte() {
        this.fromJsonNodeWithTypeListAndCheck2(Byte.MAX_VALUE, Byte.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListShort() {
        this.fromJsonNodeWithTypeListAndCheck2(Short.MAX_VALUE, Short.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListInteger() {
        this.fromJsonNodeWithTypeListAndCheck2(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListFloat() {
        this.fromJsonNodeWithTypeListAndCheck2(Float.MAX_VALUE, Float.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeListDouble() {
        this.fromJsonNodeWithTypeListAndCheck(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    private void fromJsonNodeWithTypeListAndCheck(final Number number1,
                                                  final Number number2) {
        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(JsonNode.number(number1.doubleValue()))
                        .appendChild(JsonNode.number(number2.doubleValue())),
                number1, number2);
    }

    private void fromJsonNodeWithTypeListAndCheck2(final Number number1,
                                                   final Number number2) {
        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(this.typeNameAndValue(number1.getClass(), JsonNode.number(number1.doubleValue())))
                        .appendChild(this.typeNameAndValue(number2.getClass(), JsonNode.number(number2.doubleValue()))),
                number1, number2);
    }


    @Test
    public void testFromJsonNodeWithTypeListLong() {
        final Long long1 = 1L;
        final Long long2 = 2L;

        this.fromJsonNodeWithTypeListAndCheck(JsonNode.array()
                        .appendChild(this.typeNameAndValue(long1))
                        .appendChild(this.typeNameAndValue(long2)),
                long1, long2);
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
        this.fromJsonNodeWithTypeListAndCheck(this.typeListNameAndValue(array),
                Lists.of(values));
    }

    private void fromJsonNodeWithTypeListAndCheck(final JsonNode from,
                                                  final List<?> list) {
        assertEquals(list,
                HasJsonNodeMapper.fromJsonNodeWithType(from),
                () -> "fromJsonNodeWithType(List) failed: " + from);
    }


    // fromJsonNodeWithType Set, element type..........................................................................

    @Test
    public void testFromJsonNodeWithTypeSetNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithTypeSet(null);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeSetBooleanFails() {
        this.fromJsonNodeWithTypeSetFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNull() {
        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.nullNode(), null);
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberFails() {
        this.fromJsonNodeWithTypeSetFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeWithTypeSetStringFails() {
        this.fromJsonNodeWithTypeSetFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeWithTypeSetObjectFails() {
        this.fromJsonNodeWithTypeSetFails(JsonNode.object());
    }

    private void fromJsonNodeWithTypeSetFails(final JsonNode set) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithTypeSet(set);
        });
    }

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
        this.fromJsonNodeWithTypeSetAndCheck2(Byte.MAX_VALUE, Byte.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetShort() {
        this.fromJsonNodeWithTypeSetAndCheck2(Short.MAX_VALUE, Short.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetInteger() {
        this.fromJsonNodeWithTypeSetAndCheck2(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetFloat() {
        this.fromJsonNodeWithTypeSetAndCheck2(Float.MAX_VALUE, Float.MIN_VALUE);
    }

    @Test
    public void testFromJsonNodeWithTypeSetDouble() {
        this.fromJsonNodeWithTypeSetAndCheck(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    private void fromJsonNodeWithTypeSetAndCheck(final Number number1,
                                                 final Number number2) {
        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(JsonNode.number(number1.doubleValue()))
                        .appendChild(JsonNode.number(number2.doubleValue())),
                number1, number2);
    }

    private void fromJsonNodeWithTypeSetAndCheck2(final Number number1,
                                                  final Number number2) {
        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(this.typeNameAndValue(number1.getClass(), JsonNode.number(number1.doubleValue())))
                        .appendChild(this.typeNameAndValue(number2.getClass(), JsonNode.number(number2.doubleValue()))),
                number1, number2);
    }

    @Test
    public void testFromJsonNodeWithTypeSetLong() {
        final Long long1 = 1L;
        final Long long2 = 2L;

        this.fromJsonNodeWithTypeSetAndCheck(JsonNode.array()
                        .appendChild(this.typeNameAndValue(long1))
                        .appendChild(this.typeNameAndValue(long2)),
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
        this.fromJsonNodeWithTypeSetAndCheck(this.typeSetNameAndValue(array),
                Sets.of(values));
    }

    private void fromJsonNodeWithTypeSetAndCheck(final JsonNode from,
                                                 final Set<?> set) {
        assertEquals(set,
                HasJsonNodeMapper.fromJsonNodeWithType(from),
                "fromJsonNodeWithType(Set) failed: " + from);
    }


    // fromJsonNodeWithType Map ..............................................................................

    @Test
    public void testFromJsonNodeWithTypeMapNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithTypeMap(null);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeMapBooleanFails() {
        this.fromJsonNodeWithTypeMapFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeWithTypeMapNull() {
        this.fromJsonNodeWithTypeMapAndCheck(JsonNode.nullNode(), null);
    }

    @Test
    public void testFromJsonNodeWithTypeMapNumberFails() {
        this.fromJsonNodeWithTypeMapFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeWithTypeMapStringFails() {
        this.fromJsonNodeWithTypeMapFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeWithTypeMapObjectFails() {
        this.fromJsonNodeWithTypeMapFails(JsonNode.object());
    }

    private void fromJsonNodeWithTypeMapFails(final JsonNode map) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.fromJsonNodeWithTypeMap(map);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeMap() {
        final Color key = Color.fromRgb(0x111);
        final Color value = Color.fromRgb(0x222);

        this.fromJsonNodeWithTypeMapAndCheck(this.typeMapNameAndValue(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, key.toJsonNodeWithType())
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value.toJsonNodeWithType()))),
                Maps.one(key, value));
    }

    @Test
    public void testFromJsonNodeWithTypeMap2() {
        final String key = "key1";
        final String value = "value1";

        this.fromJsonNodeWithTypeMapAndCheck(this.typeMapNameAndValue(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, JsonNode.string(value)))),
                Maps.one(key, value));
    }

    @Test
    public void testFromJsonNodeWithTypeMap3() {
        final String key1 = "key1";
        final Color value1 = Color.fromRgb(0x111);

        final String key2 = "key2";
        final Color value2 = Color.fromRgb(0x222);

        final Map<String, Color> map = Maps.ordered();
        map.put(key1, value1);
        map.put(key2, value2);

        this.fromJsonNodeWithTypeMapAndCheck(this.typeMapNameAndValue(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key1))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value1.toJsonNodeWithType()))
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key2))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value2.toJsonNodeWithType()))),
                map);
    }

    private void fromJsonNodeWithTypeMapAndCheck(final JsonNode from, final Map<?, ?> map) {
        assertEquals(map,
                HasJsonNodeMapper.fromJsonNodeWithType(from),
                () -> "fromJsonNodeWithType(Map) failed: " + from);
    }

    // toJsonNode..........................................................................

    @Test
    public void testToJsonWithType() {
        final String typeName = TestHasJsonNode.class.getName();

        try {
            HasJsonNodeMapper.register(TestHasJsonNode.class.getName(), TestHasJsonNode::fromJsonNode, TestHasJsonNode.class);
            assertEquals(this.typeNameAndValue(typeName, JsonNode.string("FIRST")),
                    TestHasJsonNode.FIRST.toJsonNodeWithType());
        } finally {
            HasJsonNodeMapper.TYPENAME_TO_FACTORY.remove(typeName);
        }
    }

    enum TestHasJsonNode implements HasJsonNode {
        FIRST,
        SECOND;

        static TestHasJsonNode fromJsonNode(final JsonNode node) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JsonNode toJsonNode() {
            return JsonNode.string(this.name());
        }
    }

    // toJsonNodeWithType List....................................................................................

    @Test
    public void testToJsonNodeListWithTypeRoundtrip() {
        this.roundtripAndCheck(Lists.of(Boolean.TRUE, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, "abc123"));
    }

    @Test
    public void testToJsonNodeListWithTypeHasJsonNodeRoundtrip() {
        this.roundtripAndCheck(Lists.of(Color.fromRgb(0x111), Color.fromRgb(0x222)));
    }

    // toJsonNode List......................................................................................

    @Test
    public void testToJsonNodeListNull() {
        this.toJsonNodeListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeListHasJsonNode() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        this.toJsonNodeListAndCheck(Lists.of(color1, color2),
                JsonNode.array()
                        .appendChild(color1.toJsonNode())
                        .appendChild(color2.toJsonNode()));
    }

    @Test
    public void testToJsonNodeListString() {
        final String string1 = "a1";
        final String string2 = "b2";

        this.toJsonNodeListAndCheck(Lists.of(string1, string2),
                JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2)));
    }

    private void toJsonNodeListAndCheck(final List<?> list, final JsonNode expected) {
        assertEquals(expected,
                HasJsonNodeMapper.toJsonNodeList(list),
                "toJsonNodeList(List) failed");
        assertEquals(expected,
                HasJsonNode.toJsonNode(list),
                "toJsonNode(Object) failed");
    }

    // toJsonNodeWithType Set....................................................................................

    @Test
    public void testToJsonNodeSetWithTypeRoundtrip() {
        this.roundtripAndCheck(Sets.of(Boolean.TRUE, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, "abc123"));
    }

    @Test
    public void testToJsonNodeSetWithTypeHasJsonNodeRoundtrip() {
        this.roundtripAndCheck(Sets.of(Color.fromRgb(0x111), Color.fromRgb(0x222)));
    }

    // toJsonNode Set......................................................................................

    @Test
    public void testToJsonNodeSetNull() {
        this.toJsonNodeSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeSetHasJsonNode() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        this.toJsonNodeSetAndCheck(Sets.of(color1, color2),
                JsonNode.array()
                        .appendChild(color1.toJsonNode())
                        .appendChild(color2.toJsonNode()));
    }

    @Test
    public void testToJsonNodeSetString() {
        final String string1 = "a1";
        final String string2 = "b2";

        this.toJsonNodeSetAndCheck(Sets.of(string1, string2),
                JsonNode.array()
                        .appendChild(JsonNode.string(string1))
                        .appendChild(JsonNode.string(string2)));
    }

    private void toJsonNodeSetAndCheck(final Set<?> set, final JsonNode expected) {
        assertEquals(expected,
                HasJsonNodeMapper.toJsonNodeSet(set),
                "toJsonNodeSet(Set) failed");
        assertEquals(expected,
                HasJsonNode.toJsonNode(set),
                "toJsonNode(Object) failed");
    }

    // toJsonNode Map......................................................................................

    @Test
    public void testToJsonNodeMapNull() {
        this.toJsonNodeMapAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeMapHasJsonNode() {
        final Color key1 = Color.fromRgb(0x111);
        final Color value1 = Color.fromRgb(0xfff);

        final Color key2 = Color.fromRgb(0x222);
        final Color value2 = Color.fromRgb(0xeee);

        final Map<Color, Color> map = Maps.ordered();
        map.put(key1, value1);
        map.put(key2, value2);

        this.toJsonNodeMapAndCheck(map,
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, key1.toJsonNode())
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value1.toJsonNode()))
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, key2.toJsonNode())
                                .set(HasJsonNodeMapper.ENTRY_VALUE, value2.toJsonNode()))
        );
    }

    @Test
    public void testToJsonNodeMapString() {
        final String key1 = "key1";
        final String value1 = "value1";

        final String key2 = "key2";
        final String value2 = "value2";

        final Map<String, String> map = Maps.ordered();
        map.put(key1, value1);
        map.put(key2, value2);

        this.toJsonNodeMapAndCheck(map,
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key1))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, JsonNode.string(value1)))
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapper.ENTRY_KEY, JsonNode.string(key2))
                                .set(HasJsonNodeMapper.ENTRY_VALUE, JsonNode.string(value2)))
        );
    }

    // toJsonNodeWithType....................................................................................

    @Test
    public void testToJsonNodeWithTypeNull() {
        this.toJsonNodeWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeWithTypeBooleanTrue() {
        this.toJsonNodeWithTypeAndCheck(Boolean.TRUE, JsonNode.booleanNode(true));
    }

    @Test
    public void testToJsonNodeWithTypeBooleanFalse() {
        this.toJsonNodeWithTypeAndCheck(Boolean.FALSE, JsonNode.booleanNode(false));
    }

    @Test
    public void testToJsonNodeWithTypeByte() {
        this.toJsonNodeWithTypeAndCheck(Byte.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeWithTypeShort() {
        this.toJsonNodeWithTypeAndCheck(Short.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeWithTypeInteger() {
        this.toJsonNodeWithTypeAndCheck(Integer.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeWithTypeLong() {
        this.toJsonNodeWithTypeAndCheck(Long.MAX_VALUE, this.typeNameAndValue(Long.MAX_VALUE));
    }

    @Test
    public void testToJsonNodeWithTypeFloat() {
        this.toJsonNodeWithTypeAndCheck(123.5f);
    }

    @Test
    public void testToJsonNodeWithTypeDouble() {
        this.toJsonNodeWithTypeAndCheck(123.5,JsonNode.number(123.5));
    }

    private void toJsonNodeWithTypeAndCheck(final Number value) {
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeNameAndValue(value.getClass(),
                        JsonNode.number(value.doubleValue())));
    }

    @Test
    public void testToJsonNodeWithTypeString() {
        this.toJsonNodeWithTypeAndCheck("abc", JsonNode.string("abc"));
    }

    @Test
    public void testToJsonNodeWithTypeList() {
        final List<Object> list = Lists.of(true, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, "abc");
        this.toJsonNodeWithTypeAndCheck(list,
                this.typeNameAndValue("list",
                        HasJsonNodeMapper.toJsonNodeWithTypeList(list)));
    }

    @Test
    public void testToJsonNodeWithTypeListHasJsonNode() {
        final List<Object> list = Lists.of(Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(list,
                this.typeNameAndValue("list",
                        HasJsonNodeMapper.toJsonNodeWithTypeList(list)));
    }

    @Test
    public void testToJsonNodeWithTypeSet() {
        final Set<Object> set = Sets.of(true, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, "abc");
        this.toJsonNodeWithTypeAndCheck(set,
                this.typeNameAndValue("set",
                        HasJsonNodeMapper.toJsonNodeWithTypeSet(set)));
    }

    @Test
    public void testToJsonNodeWithTypeSetHasJsonNode() {
        final Set<Object> set = Sets.of(Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(set,
                this.typeNameAndValue("set",
                        HasJsonNodeMapper.toJsonNodeWithTypeSet(set)));
    }

    @Test
    public void testToJsonNodeWithTypeMap() {
        final Map<Object, Object> map = Maps.ordered();
        map.put("boolean-key", Boolean.TRUE);
        map.put("byte-key", Byte.MAX_VALUE);
        map.put("short-key", Short.MAX_VALUE);
        map.put("integer-key", Integer.MAX_VALUE);
        map.put("long-key", Long.MAX_VALUE);
        map.put("float-key", Float.MAX_VALUE);
        map.put("double-key", Double.MAX_VALUE);

        this.toJsonNodeWithTypeAndCheck(map,
                this.typeNameAndValue("map",
                        HasJsonNodeMapper.toJsonNodeWithTypeMap(map)));
    }

    @Test
    public void testToJsonNodeWithTypeMapHasJsonNode() {
        final Map<Object, Object> map = Maps.one("key", Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(map,
                this.typeNameAndValue("map",
                        HasJsonNodeMapper.toJsonNodeWithTypeMap(map)));
    }

    private void toJsonNodeWithTypeAndCheck(final Object value, final JsonNode expected) {
        assertEquals(expected,
                HasJsonNodeMapper.toJsonNodeWithType(value),
                "value " + CharSequences.quoteIfChars(value) + " toJsonNodeWithType failed");
    }

    // toJsonNodeWithType fromJsonNodeWithType Rountrip .........................................................................................

    @Test
    public void testRoundtripNull() {
        this.roundtripAndCheck(null);
    }

    @Test
    public void testRoundtripBooleanTrue() {
        this.roundtripAndCheck(Boolean.TRUE);
    }

    @Test
    public void testRoundtripBooleanFalse() {
        this.roundtripAndCheck(Boolean.FALSE);
    }

    @Test
    public void testRoundtripNumber() {
        this.roundtripAndCheck(123.5);
    }

    @Test
    public void testRoundtripString() {
        this.roundtripAndCheck("abc123");
    }

    @Test
    public void testRoundtripHasJsonNode() {
        this.roundtripAndCheck(Color.fromRgb(0x123));
    }

    @Test
    public void testRoundtripListPrimitive() {
        this.roundtripAndCheck(Lists.of("abc123"));
    }

    @Test
    public void testRoundtripList() {
        this.roundtripAndCheck(Lists.of(Color.fromRgb(0x123)));
    }

    @Test
    public void testRoundtripSetPrimitive() {
        this.roundtripAndCheck(Sets.of("abc123"));
    }

    @Test
    public void testRoundtripSet() {
        this.roundtripAndCheck(Sets.of(Color.fromRgb(0x123)));
    }

    @Test
    public void testRoundtripMapPrimitive() {
        final Map<Object, Object> map = Maps.ordered();
        map.put("boolean-key", Boolean.TRUE);
        map.put("byte-key", Byte.MAX_VALUE);
        map.put("short-key", Short.MAX_VALUE);
        map.put("integer-key", Integer.MAX_VALUE);
        map.put("long-key", Long.MAX_VALUE);
        map.put("float-key", Float.MAX_VALUE);
        map.put("double-key", Double.MAX_VALUE);

        this.roundtripAndCheck(map);
    }

    @Test
    public void testRoundtripMapHasJsonNode() {
        this.roundtripAndCheck(Maps.one(Color.fromRgb(0x1), Color.fromRgb(0x2)));
    }


    private void toJsonNodeMapAndCheck(final Map<?, ?> map, final JsonNode expected) {
        assertEquals(expected,
                HasJsonNodeMapper.toJsonNodeMap(map),
                "toJsonNodeMap(Map) failed");
        assertEquals(expected,
                HasJsonNode.toJsonNode(map),
                "toJsonNode(Object) failed");
    }

    private JsonNode typeListNameAndValue(final JsonNode value) {
        return this.typeNameAndValue("list", value);
    }

    private JsonNode typeSetNameAndValue(final JsonNode value) {
        return this.typeNameAndValue("set", value);
    }

    private JsonNode typeMapNameAndValue(final JsonNode value) {
        return this.typeNameAndValue("map", value);
    }

    private JsonNode typeNameAndValue(final long value) {
        return this.typeNameAndValue("long", JsonNode.wrapLong(value));
    }

    private JsonNode typeNameAndValue(final Class<?> type, final JsonNode value) {
        return this.typeNameAndValue(Integer.class == type ? "int" :
                        Boolean.class == type | Number.class.isAssignableFrom(type) | String.class == type ?
                                type.getSimpleName().toLowerCase() :
                                JsonNode.class.isAssignableFrom(type) ?
                                        type.getSimpleName().substring(0, type.getSimpleName().length() - 4) :
                                        type.getName(),
                value);
    }

    private JsonNode typeNameAndValue(final String typeName, final JsonNode value) {
        return JsonNode.object()
                .set(HasJsonNodeMapper.TYPE, JsonNode.string(typeName))
                .set(HasJsonNodeMapper.VALUE, value);
    }

    private void roundtripAndCheck(final Object value) {
        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(HasJsonNodeMapper.toJsonNodeWithType(value)),
                () -> "roundtrip " + value);
    }

    @Override
    public Class<HasJsonNodeMapper<Void>> type() {
        return Cast.to(HasJsonNodeMapper.class);
    }
}
