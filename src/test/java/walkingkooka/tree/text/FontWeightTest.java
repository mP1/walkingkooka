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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.tree.json.JsonNode;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FontWeightTest extends TextStylePropertyValueTestCase2<FontWeight>
        implements ComparableTesting<FontWeight>,
        ConstantsTesting<FontWeight>,
        SerializationTesting<FontWeight>  {

    private final static int VALUE = 456;

    @Test
    public void testWithNegativeValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            FontWeight.with(-1);
        });
    }

    @Test
    public void testWith() {
        final int value = 400;
        final FontWeight size = FontWeight.with(value);
        assertEquals(value, size.value(), "value");
    }

    @Test
    public void testBold() {
        assertSame(FontWeight.BOLD, FontWeight.with(FontWeight.BOLD.value()));
    }

    @Test
    public void testNormal() {
        assertSame(FontWeight.NORMAL, FontWeight.with(FontWeight.NORMAL.value()));
    }

    // HasJsonNode......................................................................................

    @Test
    public void testFromJsonNodeBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeNullFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public void testFromJsonNodeInvalidStringFails() {
        this.fromJsonNodeFails(JsonNode.string("not bold or normal"));
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
    public void testFromJsonNodeNumberInvalidFails() {
        this.fromJsonNodeFails(JsonNode.number(-1));
    }

    @Test
    public void testFromJsonBold() {
        this.fromJsonNodeAndCheck(JsonNode.string("bold"),
                FontWeight.BOLD);
    }

    @Test
    public void testFromJsonNormal() {
        this.fromJsonNodeAndCheck(JsonNode.string("normal"),
                FontWeight.NORMAL);
    }

    @Test
    public void testFromJsonNormal2() {
        this.fromJsonNodeAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string("normal"))
                        .get(0),
                FontWeight.NORMAL);
    }

    @Test
    public void testFromJsonNumber() {
        final int value = 20;
        this.fromJsonNodeAndCheck(JsonNode.number(value),
                FontWeight.with(value));
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck(this.createComparable(), JsonNode.number(VALUE));
    }

    @Test
    public void testToJsonNodeRoundtripTwice() {
        this.toJsonNodeRoundTripTwiceAndCheck(this.createObject());
    }

    @Test
    public void testToJsonNodeRoundtripBold() {
        this.toJsonNodeRoundTripTwiceAndCheck(FontWeight.BOLD);
    }

    @Test
    public void testToJsonNodeRoundtripNormal() {
        this.toJsonNodeRoundTripTwiceAndCheck(FontWeight.NORMAL);
    }

    // Serializable.....................................................................................................

    @Test
    public void testSerializeBold() throws Exception {
        this.serializeSingletonAndCheck(FontWeight.BOLD);
    }

    @Test
    public void testSerializeNormal() throws Exception {
        this.serializeSingletonAndCheck(FontWeight.NORMAL);
    }

    // Object...........................................................................................................

    @Test
    public void testToStringNormal() {
        this.toStringAndCheck(FontWeight.NORMAL, "normal");
    }

    @Test
    public void testToStringBold() {
        this.toStringAndCheck(FontWeight.BOLD, "bold");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(FontWeight.with(456), "456");
    }

    @Override
    FontWeight createTextStylePropertyValue() {
        return FontWeight.with(VALUE);
    }

    @Override
    TextStylePropertyName<FontWeight> textStylePropertyName() {
        return TextStylePropertyName.FONT_WEIGHT;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<FontWeight> type() {
        return FontWeight.class;
    }

    // ComparableTesting................................................................................................

    @Override
    public FontWeight createComparable() {
        return this.createTextStylePropertyValue();
    }

    // ConstantsTesting.................................................................................................

    @Override
    public Set<FontWeight> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public FontWeight fromJsonNode(final JsonNode jsonNode) {
        return FontWeight.fromJsonNode(jsonNode);
    }

    // SerializationTesting.............................................................................................

    @Override
    public FontWeight serializableInstance() {
        return this.createComparable();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
