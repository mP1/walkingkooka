/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.color.Color;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.JavaVisibility;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonNodeNameTest implements ClassTesting2<JsonNodeName>,
        NameTesting<JsonNodeName, JsonNodeName>,
        HasJsonNodeTesting<JsonNodeName> {

    @Test
    public void testWithNegativeIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            JsonNodeName.index(-1);
        });
    }

    @Test
    public void testIndex() {
        assertEquals("123", JsonNodeName.index(123).value());
    }

    @Test
    public void testFromJsonNodeBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeNumberFails() {
        this.fromJsonNodeFails(JsonNode.number(12.5));
    }

    @Test
    public void testFromJsonNodeNulFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public void testFromJsonNodeString() {
        final String value = "property-1a";
        this.fromJsonNodeAndCheck(JsonNode.string(value), JsonNodeName.with(value));
    }

    @Test
    public void testFromJsonNodeArrayFails() {
        this.fromJsonNodeFails(JsonNode.array());
    }

    @Test
    public void testFromJsonNodeObjectFails() {
        this.fromJsonNodeFails(JsonNode.object());
    }

    @Test
    public void testToJsonNode() {
        final String value = "property-1a";
        this.toJsonNodeAndCheck(JsonNodeName.with(value), JsonNode.string(value));
    }

    @Test
    public void testToJsonNodeIndex() {
        final JsonNodeName name = JsonNodeName.index(1);
        this.toJsonNodeAndCheck(name, JsonNode.string("1"));
    }

    @Test
    public void testToJsonNodeRoundtripIndex() {
        this.toJsonNodeRoundTripTwiceAndCheck(JsonNodeName.index(1));
    }

    @Test
    public void testToJsonNodeRoundtripIndex2() {
        this.toJsonNodeRoundTripTwiceAndCheck(JsonNodeName.index(JsonNodeName.INDEX_CACHE_SIZE + 1));
    }

    @Test
    public void testFromJsonNodeWithTypeFactory() {
        final JsonNodeName typeNameProperty = JsonNodeName.with("typeName123");
        final Function<JsonNode, Color> factory = typeNameProperty.fromJsonNodeWithTypeFactory(JsonNode.object()
                        .set(typeNameProperty, HasJsonNode.typeName(Color.class).get()),
                Color.class);

        final Color color = Color.fromRgb(0x123456);
        assertEquals(color, factory.apply(color.toJsonNode()));
    }

    @Test
    public void testFromJsonNodeWithTypeFactoryFromArray() {
        final JsonNodeName typeNameProperty = JsonNodeName.with("typeName123");
        final Function<JsonNode, Color> factory = typeNameProperty.fromJsonNodeWithTypeFactory(JsonNode.object()
                        .set(typeNameProperty, HasJsonNode.typeName(Color.class).get()),
                Color.class);

        final Color color1 = Color.fromRgb(1);
        final Color color2 = Color.fromRgb(2);
        final Color color3 = Color.fromRgb(3);

        assertEquals(Lists.of(color1, color2, color3),
                Lists.of(color1, color2, color3).stream()
                        .map(e -> e.toJsonNode())
                        .map(factory::apply)
                        .collect(Collectors.toList()));
    }

    @Override
    public JsonNodeName createName(final String name) {
        return JsonNodeName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "property-2";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "property-1";
    }

    @Override
    public Class<JsonNodeName> type() {
        return JsonNodeName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HasJsonNodeTesting.....................................................................

    @Override
    public JsonNodeName fromJsonNode(final JsonNode from) {
        return JsonNodeName.fromJsonNode(from);
    }

    @Override
    public JsonNodeName createHasJsonNode() {
        return JsonNodeName.with("property-1a");
    }
}
