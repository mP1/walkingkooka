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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.test.ClassTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HasJsonNodeTest implements ClassTesting<HasJsonNode> {

    @Test
    public void testFromJsonNodeAndType() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(color,
                HasJsonNode.fromJsonNode(color.toJsonNode(), Color.class));
    }

    @Test
    public void testFromJsonNodeAndTypeString() {
        final String string = "abc123";

        assertEquals(string,
                HasJsonNode.fromJsonNode(JsonNode.string(string), String.class));
    }

    @Test
    public void testFromJsonNodeListAndElementType() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(Lists.of(color),
                HasJsonNode.fromJsonNodeList(JsonNode.array()
                                .appendChild(color.toJsonNode()),
                        Color.class));
    }

    @Test
    public void testFromJsonNodeSetAndElementType() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(Sets.of(color),
                HasJsonNode.fromJsonNodeSet(JsonNode.array()
                                .appendChild(color.toJsonNode()),
                        Color.class));
    }

    @Test
    public void testFromJsonNodeMapAndKeyTypeAndValueType() {
        final EmailAddress key = EmailAddress.parse("user@example.com");
        final Color value = Color.fromRgb(0x123);

        assertEquals(Maps.one(key, value),
                HasJsonNode.fromJsonNodeMap(JsonNode.array()
                                .appendChild(
                                        JsonNode.object()
                                                .set(HasJsonNodeMapMapper.ENTRY_KEY, key.toJsonNode())
                                                .set(HasJsonNodeMapMapper.ENTRY_VALUE, value.toJsonNode())),
                        EmailAddress.class,
                        Color.class));
    }

    @Test
    public void testFromJsonNodeWithTypeList() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(Lists.of(color),
                HasJsonNode.fromJsonNodeWithTypeList(JsonNode.array()
                        .appendChild(color.toJsonNodeWithType())));
    }

    @Test
    public void testFromJsonNodeWithTypeSet() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(Sets.of(color),
                HasJsonNode.fromJsonNodeWithTypeSet(JsonNode.array()
                        .appendChild(color.toJsonNodeWithType())));
    }

    @Test
    public void testFromJsonNodeWithTypeMap() {
        final EmailAddress key = EmailAddress.parse("user@example.com");
        final Color value = Color.fromRgb(0x123);

        assertEquals(Maps.one(key, value),
                HasJsonNode.fromJsonNodeWithTypeMap(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapMapper.ENTRY_KEY, key.toJsonNodeWithType())
                                .set(HasJsonNodeMapMapper.ENTRY_VALUE, value.toJsonNodeWithType()))));
    }

    @Test
    public void testToJsonNodeWithTypeList() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(JsonNode.array()
                        .appendChild(color.toJsonNodeWithType()),
                HasJsonNode.toJsonNodeWithTypeList(Lists.of(color)));
    }

    @Test
    public void testToJsonNodeWithTypeSet() {
        final Color color = Color.fromRgb(0x123);

        assertEquals(JsonNode.array()
                        .appendChild(color.toJsonNodeWithType()),
                HasJsonNode.toJsonNodeWithTypeSet(Sets.of(color)));
    }

    @Test
    public void testToJsonNodeWithTypeMap() {
        final EmailAddress key = EmailAddress.parse("email@example.com");
        final Color value = Color.fromRgb(0x123);

        assertEquals(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapMapper.ENTRY_KEY, key.toJsonNodeWithType())
                                .set(HasJsonNodeMapMapper.ENTRY_VALUE, value.toJsonNodeWithType())),
                HasJsonNode.toJsonNodeWithTypeMap(Maps.one(key, value)));
    }

    @Test
    public void testToJsonNodeWithType() {
        final EmailAddress value = EmailAddress.parse("email@example.com");

        assertEquals(value.toJsonNodeWithType(),
                HasJsonNode.toJsonNodeWithType(value));
    }

    @Override
    public Class<HasJsonNode> type() {
        return HasJsonNode.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
