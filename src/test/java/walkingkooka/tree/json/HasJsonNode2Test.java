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
        assertEquals(null,
                HasJsonNode.fromJsonNodeList(JsonNode.nullNode(), Color.class));
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

        assertEquals(Lists.of(color1, color2),
                HasJsonNode2.fromJsonNodeList(JsonNode.array().appendChild(color1.toJsonNode()).appendChild(color2.toJsonNode()), Color.class));
    }

    // fromJsonNodeWithType .....................................................................................

    @Test
    public void testFromJsonNodeWithTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeWithType(null);
        });
    }

    @Test
    public void testFromJsonNodeWithTypeBooleanFails() {
        this.fromJsonNodeWithTypeFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeWithTypeJsonNullNodeFails() {
        this.fromJsonNodeWithTypeFails(JsonNode.nullNode());
    }

    @Test
    public void testFromJsonNodeWithTypeNumberFails() {
        this.fromJsonNodeWithTypeFails(JsonNode.number(123));
    }

    @Test
    public void testFromJsonNodeWithTypeStringFails() {
        this.fromJsonNodeWithTypeFails(JsonNode.string("abc123"));
    }

    @Test
    public void testFromJsonNodeWithTypeObjectFails() {
        this.fromJsonNodeWithTypeFails(JsonNode.object());
    }

    private void fromJsonNodeWithTypeFails(final JsonNode node) {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNode2.fromJsonNodeWithType(node);
        });
    }

    @Test
    public void testFromJsonNodeWithUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            HasJsonNode2.fromJsonNodeWithType(JsonNode.object().set(HasJsonNode2.TYPE, JsonNode.string(this.getClass().getName())));
        });
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
    public void testFromJsonNodeWithTypeListNullFails() {
        this.fromJsonNodeWithTypeListFails(JsonNode.nullNode());
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

        final JsonNode json = JsonNode.object()
                .set(HasJsonNode2.TYPE, JsonNode.string(HasJsonNode2.LIST))
                .set(HasJsonNode2.VALUE, JsonNode.array().appendChild(color1.toJsonNodeWithType()).appendChild(color2.toJsonNodeWithType()));

        assertEquals(Lists.of(color1, color2),
                HasJsonNode2.fromJsonNodeWithType(json),
                "fromJsonNodeWithType " + json);
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
    public void testFromJsonNodeSetJsonNullNodeFails() {
        assertEquals(null,
                HasJsonNode.fromJsonNodeSet(JsonNode.nullNode(), Color.class));
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

        assertEquals(Sets.of(color1, color2),
                HasJsonNode2.fromJsonNodeSet(JsonNode.array().appendChild(color1.toJsonNode()).appendChild(color2.toJsonNode()), Color.class));
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
    public void testFromJsonNodeWithTypeSetNullFails() {
        this.fromJsonNodeWithTypeSetFails(JsonNode.nullNode());
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

        final JsonNode json = JsonNode.object()
                .set(HasJsonNode2.TYPE, JsonNode.string(HasJsonNode2.SET))
                .set(HasJsonNode2.VALUE, JsonNode.array().appendChild(color1.toJsonNodeWithType()).appendChild(color2.toJsonNodeWithType()));

        assertEquals(Sets.of(color1, color2),
                HasJsonNode2.fromJsonNodeWithType(json),
                "fromJsonNodeWithType " + json);
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

    // toJsonNode List....................................................................................

    @Test
    public void testToJsonNodeListWithTypeColorRoundtrip() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);
        final List<Color> list = Lists.of(color1, color2);

        final JsonNode json = HasJsonNode2.toJsonNodeWithType(list);
        assertEquals(
          list,
          HasJsonNode2.fromJsonNodeWithType(json),
          "fromJsonNodeWithType " + json + " failed");
    }

    // toJsonNode List......................................................................................

    @Test
    public void testToJsonNodeListNull() {
        assertEquals(JsonNode.nullNode(),
                HasJsonNode.toJsonNode((List<JsonNode>) null));
    }

    @Test
    public void testToJsonNodeList() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        assertEquals(JsonNode.array().appendChild(color1.toJsonNode()).appendChild(color2.toJsonNode()),
                HasJsonNode.toJsonNode(Lists.of(color1, color2)));
    }

    // toJsonNode Set....................................................................................

    @Test
    public void testToJsonNodeSetWithTypeColorRoundtrip() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);
        final Set<Color> set = Sets.of(color1, color2);

        final JsonNode json = HasJsonNode2.toJsonNodeWithType(set);
        assertEquals(
                set,
                HasJsonNode2.fromJsonNodeWithType(json),
                "fromJsonNodeWithType " + json + " failed");
    }

    // toJsonNode Set......................................................................................

    @Test
    public void testToJsonNodeSetNull() {
        assertEquals(JsonNode.nullNode(),
                HasJsonNode.toJsonNode((Set<JsonNode>) null));
    }

    @Test
    public void testToJsonNodeSet() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        assertEquals(JsonNode.array().appendChild(color1.toJsonNode()).appendChild(color2.toJsonNode()),
                HasJsonNode.toJsonNode(Sets.of(color1, color2)));
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
