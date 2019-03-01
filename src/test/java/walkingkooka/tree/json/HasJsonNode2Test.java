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
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.test.ClassTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HasJsonNode2Test implements ClassTesting<HasJsonNode2> {

    @Test
    public void testRegisterNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.register(null, JsonNode::fromJsonNode);
        });
    }

    @Test
    public void testRegisterNullFactoryFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.register(JsonNode.class, null);
        });
    }

    // fromJsonNode List, element type..........................................................................

    @Test
    public void testFromJsonNodeListNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeList(null, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeListNullElementTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeList(JsonNode.array(), null);
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
            HasJsonNode2.fromJsonNodeList(list, Color.class);
        });
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

    private <E> void fromJsonNodeListAndCheck(final JsonNode node,
                                             final Class<E> elementType,
                                             final List<E> list) {
        assertEquals(list,
                HasJsonNode2.fromJsonNodeList(node, elementType),
                () -> "fromJsonNodeList(List) failed=" + node);
    }

    // fromJsonNodeWithType .....................................................................................

    @Test
    public void testFromJsonNodeWithTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeWithType(null);
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
            HasJsonNode2.fromJsonNodeWithType(JsonNode.object());
        });
    }

    @Test
    public void testFromJsonNodeWithTypeUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            HasJsonNode2.fromJsonNodeWithType(JsonNode.object().set(HasJsonNode2.TYPE, JsonNode.string(this.getClass().getName())));
        });
    }

    private void fromJsonNodeWithTypeAndCheck(final JsonNode node, final Object value) {
        assertEquals(value,
                HasJsonNode2.fromJsonNodeWithType(node),
                "fromJsonNodeWithType(JsonNode) failed=" + node);
    }

    // fromJsonNodeWithType List, element type..........................................................................

    @Test
    public void testFromJsonNodeWithTypeListNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeWithTypeList(null);
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
            HasJsonNode2.fromJsonNodeWithTypeList(list);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeList() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        fromJsonNodeWithTypeListAndCheck(JsonNode.object()
                        .set(HasJsonNode2.TYPE, JsonNode.string(HasJsonNode2.LIST))
                        .set(HasJsonNode2.VALUE, JsonNode.array().appendChild(color1.toJsonNodeWithType()).appendChild(color2.toJsonNodeWithType())),
                Lists.of(color1, color2));
    }

    @Test
    public void testFromJsonNodeWithTypeList2() {
        final String string1 = "a1";
        final String string2 = "b2";

        fromJsonNodeWithTypeListAndCheck(JsonNode.object()
                        .set(HasJsonNode2.TYPE, JsonNode.string(HasJsonNode2.LIST))
                        .set(HasJsonNode2.VALUE, JsonNode.array().appendChild(JsonNode.string(string1)).appendChild(JsonNode.string(string2))),
                Lists.of(string1, string2));
    }

    private void fromJsonNodeWithTypeListAndCheck(final JsonNode from, final List<?> list) {
        assertEquals(list,
                HasJsonNode2.fromJsonNodeWithType(from),
                "fromJsonNodeWithType(List) failed: " + from);
    }

    // fromJsonNode Set, element type..........................................................................

    @Test
    public void testFromJsonNodeSetNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeSet(null, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeSetNullElementTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeSet(JsonNode.array(), null);
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
            HasJsonNode2.fromJsonNodeSet(set, Color.class);
        });
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

    private <E> void fromJsonNodeSetAndCheck(final JsonNode node,
                                             final Class<E> elementType,
                                             final Set<E> set) {
        assertEquals(set,
                HasJsonNode2.fromJsonNodeSet(node, elementType),
                () -> "fromJsonNodeSet(Set) failed=" + node);
    }

    // fromJsonNodeWithType Set, element type..........................................................................

    @Test
    public void testFromJsonNodeWithTypeSetNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeWithTypeSet(null);
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
            HasJsonNode2.fromJsonNodeWithTypeSet(set);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeSet() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        fromJsonNodeWithTypeSetAndCheck(JsonNode.object()
                .set(HasJsonNode2.TYPE, JsonNode.string(HasJsonNode2.SET))
                .set(HasJsonNode2.VALUE, JsonNode.array().appendChild(color1.toJsonNodeWithType()).appendChild(color2.toJsonNodeWithType())),
                Sets.of(color1, color2));
    }

    @Test
    public void testFromJsonNodeWithTypeSet2() {
        final String string1 = "a1";
        final String string2 = "b2";

        fromJsonNodeWithTypeSetAndCheck(JsonNode.object()
                        .set(HasJsonNode2.TYPE, JsonNode.string(HasJsonNode2.SET))
                        .set(HasJsonNode2.VALUE, JsonNode.array().appendChild(JsonNode.string(string1)).appendChild(JsonNode.string(string2))),
                Sets.of(string1, string2));
    }

    private void fromJsonNodeWithTypeSetAndCheck(final JsonNode from, final Set<?> set) {
        assertEquals(set,
                HasJsonNode2.fromJsonNodeWithType(from),
                "fromJsonNodeWithType(Set) failed: " + from);
    }

    // toJsonNode..........................................................................

    @Test
    public void testToJsonWithType() {
        final String typeName = TestHasJsonNode.class.getName();

        try {
            HasJsonNode2.register(TestHasJsonNode.class, TestHasJsonNode::fromJsonNode);
            assertEquals(JsonNode.object()
                            .set(HasJsonNode2.TYPE, JsonNode.string(typeName))
                            .set(HasJsonNode2.VALUE, JsonNode.string("FIRST")),
                    TestHasJsonNode.FIRST.toJsonNodeWithType());
        } finally {
            HasJsonNode2.TYPENAME_TO_FACTORY.remove(typeName);
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

        @Override
        public Class<TestHasJsonNode> toJsonNodeType() {
            return TestHasJsonNode.class;
        }
    }

    // toJsonNodeWithType List....................................................................................

    @Test
    public void testToJsonNodeListWithTypeColorRoundtrip() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);
        final List<Color> list = Lists.of(color1, color2);

        final JsonNode json = HasJsonNode2.toJsonNodeWithTypeList(list);
        assertEquals(
          list,
          HasJsonNode2.fromJsonNodeWithType(json),
          "fromJsonNodeWithType " + json + " failed");
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
                HasJsonNode2.toJsonNodeList(list),
                "toJsonNodeList(List) failed");
        assertEquals(expected,
                HasJsonNode.toJsonNode(list),
                "toJsonNode(Object) failed");
    }

    // toJsonNodeWithType Set....................................................................................

    @Test
    public void testToJsonNodeSetWithTypeColorRoundtrip() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);
        final Set<Color> set = Sets.of(color1, color2);

        final JsonNode json = HasJsonNode2.toJsonNodeWithTypeSet(set);
        assertEquals(
                set,
                HasJsonNode2.fromJsonNodeWithType(json),
                "fromJsonNodeWithType " + json + " failed");
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
                HasJsonNode2.toJsonNodeSet(set),
                "toJsonNodeSet(Set) failed");
        assertEquals(expected,
                HasJsonNode.toJsonNode(set),
                "toJsonNode(Object) failed");
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
    public void testToJsonNodeWithTypeNumber() {
        this.toJsonNodeWithTypeAndCheck(123, JsonNode.number(123L));
    }

    @Test
    public void testToJsonNodeWithTypeString() {
        this.toJsonNodeWithTypeAndCheck("abc", JsonNode.string("abc"));
    }

    @Test
    public void testToJsonNodeWithTypeList() {
        final List<Object> list = Lists.of(true, 1, "abc");
        this.toJsonNodeWithTypeAndCheck(list, HasJsonNode2.toJsonNodeWithTypeList(list));
    }

    @Test
    public void testToJsonNodeWithTypeListColor() {
        final List<Object> list = Lists.of(Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(list, HasJsonNode2.toJsonNodeWithTypeList(list));
    }

    @Test
    public void testToJsonNodeWithTypeSetColor() {
        final Set<Object> set = Sets.of(Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(set, HasJsonNode2.toJsonNodeWithTypeSet(set));
    }

    private void toJsonNodeWithTypeAndCheck(final Object value, final JsonNode expected) {
        assertEquals(expected,
                HasJsonNode2.toJsonNodeWithType(value),
                "value " + CharSequences.quoteIfChars(value) + " toJsonNodeWithType failed");
    }
    
    // ClassTesting..........................................................................................

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<HasJsonNode2> type() {
        return HasJsonNode2.class;
    }
}
