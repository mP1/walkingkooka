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
import walkingkooka.color.Color;
import walkingkooka.test.ClassTesting;
import walkingkooka.type.MemberVisibility;

import java.util.List;

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

    @Test
    public void testFromJsonNodeWithTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNodeWithType(null);
        });
    }

    // fromJsonNode List, element type..........................................................................

    @Test
    public void testFromJsonNodeListNullNodeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNode(null, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeListNullElementTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode2.fromJsonNode(JsonNode.array(), null);
        });
    }

    @Test
    public void testFromJsonNodeListBooleanFails() {
        this.fromJsonNodeListFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeListNullFails() {
        this.fromJsonNodeListFails(JsonNode.nullNode());
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
            HasJsonNode2.fromJsonNode(list, Color.class);
        });
    }

    @Test
    public void testFromJsonNodeList() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        assertEquals(Lists.of(color1, color2),
                HasJsonNode2.fromJsonNode(JsonNode.array().appendChild(color1.toJsonNode()).appendChild(color2.toJsonNode()), Color.class));
    }

    // toJsonNode List..........................................................................

    @Test
    public void testToJsonNodeListNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNode.toJsonNode((List<JsonNode>) null);
        });
    }

    @Test
    public void testToJsonNodeList() {
        final Color color1 = Color.fromRgb(0x111);
        final Color color2 = Color.fromRgb(0x222);

        assertEquals(JsonNode.array().appendChild(color1.toJsonNode()).appendChild(color2.toJsonNode()),
                HasJsonNode.toJsonNode(Lists.of(color1, color2)));
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<HasJsonNode2> type() {
        return HasJsonNode2.class;
    }
}
