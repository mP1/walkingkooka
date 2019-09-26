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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.json.marshall.FromJsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMappingTesting;
import walkingkooka.tree.json.marshall.TestJsonNodeValue;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonNodeNameTest implements ClassTesting2<JsonNodeName>,
        NameTesting<JsonNodeName, JsonNodeName>,
        JsonNodeMappingTesting<JsonNodeName> {

    @BeforeAll
    public static void beforeAll() {
        TestJsonNodeValue.register();
    }

    @AfterAll
    public static void afterAll() {
        TestJsonNodeValue.unregister();
    }

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

    // JsonNodeContext..................................................................................................

    @Test
    public void testFromJsonNodeString() {
        final String value = "property-1a";
        this.fromJsonNodeAndCheck(JsonNode.string(value), JsonNodeName.with(value));
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
    public void testCompareToArraySort() {
        final JsonNodeName a1 = JsonNodeName.with("A1");
        final JsonNodeName b2 = JsonNodeName.with("B2");
        final JsonNodeName c3 = JsonNodeName.with("c3");
        final JsonNodeName d4 = JsonNodeName.with("d4");

        this.compareToArraySortAndCheck(d4, a1, c3, b2,
                a1, b2, c3, d4);
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

    // JsonNodeMappingTesting.....................................................................

    @Override
    public JsonNodeName fromJsonNode(final JsonNode from,
                                     final FromJsonNodeContext context) {
        return JsonNodeName.fromJsonNode(from, context);
    }

    @Override
    public JsonNodeName createJsonNodeMappingValue() {
        return JsonNodeName.with("property-1a");
    }
}
