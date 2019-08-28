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

package walkingkooka.tree.json.marshall;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;

import java.util.Locale;

public final class BasicFromJsonNodeContextTest extends BasicJsonNodeContextTestCase<BasicFromJsonNodeContext>
        implements FromJsonNodeContextTesting<BasicFromJsonNodeContext> {

    // fromJsonNode.....................................................................................................

    @Test
    public void testFromJsonNodeBooleanTrue() {
        this.fromJsonNodeAndCheck(JsonNode.booleanNode(true),
                Boolean.class,
                true);
    }

    @Test
    public void testFromJsonNodeBooleanFalse() {
        this.fromJsonNodeAndCheck(JsonNode.booleanNode(false),
                Boolean.class,
                false);
    }

    @Test
    public void testFromJsonNodeNull() {
        this.fromJsonNodeAndCheck(JsonNode.nullNode(),
                Boolean.class,
                null);
    }

    @Test
    public void testFromJsonNodeNumber() {
        final double value = 1.25;
        this.fromJsonNodeAndCheck(JsonNode.number(value),
                Double.class,
                value);
    }

    @Test
    public void testFromJsonNodeString() {
        final String value = "abc123";
        this.fromJsonNodeAndCheck(JsonNode.string(value),
                String.class,
                value);
    }

    // fromJsonNodeList.................................................................................................

    @Test
    public void testFromJsonNodeListBooleanTrue() {
        this.fromJsonNodeListAndCheck(list(JsonNode.booleanNode(true)),
                Boolean.class,
                Lists.of(true));
    }

    @Test
    public void testFromJsonNodeListBooleanFalse() {
        this.fromJsonNodeListAndCheck(list(JsonNode.booleanNode(false)),
                Boolean.class,
                Lists.of(false));
    }

    @Test
    public void testFromJsonNodeListNull() {
        this.fromJsonNodeListAndCheck(list(JsonNode.nullNode()),
                Boolean.class,
                Lists.of((Boolean) null));
    }

    @Test
    public void testFromJsonNodeListNumber() {
        final double value = 1.25;
        this.fromJsonNodeListAndCheck(list(JsonNode.number(value)),
                Double.class,
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeListString() {
        final String value = "abc123";
        this.fromJsonNodeListAndCheck(list(JsonNode.string(value)),
                String.class,
                Lists.of(value));
    }

    private JsonArrayNode list(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // fromJsonNodeSet..................................................................................................

    @Test
    public void testFromJsonNodeSetBooleanTrue() {
        this.fromJsonNodeSetAndCheck(set(JsonNode.booleanNode(true)),
                Boolean.class,
                Sets.of(true));
    }

    @Test
    public void testFromJsonNodeSetBooleanFalse() {
        this.fromJsonNodeSetAndCheck(set(JsonNode.booleanNode(false)),
                Boolean.class,
                Sets.of(false));
    }

    @Test
    public void testFromJsonNodeSetNull() {
        this.fromJsonNodeSetAndCheck(set(JsonNode.nullNode()),
                Boolean.class,
                Sets.of((Boolean) null));
    }

    @Test
    public void testFromJsonNodeSetNumber() {
        final double value = 1.25;
        this.fromJsonNodeSetAndCheck(set(JsonNode.number(value)),
                Double.class,
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeSetString() {
        final String value = "abc123";
        this.fromJsonNodeSetAndCheck(set(JsonNode.string(value)),
                String.class,
                Sets.of(value));
    }

    private JsonArrayNode set(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // fromJsonNodeMap..................................................................................................

    @Test
    public void testFromJsonNodeMapBooleanTrue() {
        this.fromJsonNodeMapAndCheck2(JsonNode.booleanNode(true),
                Boolean.class,
                true);
    }

    @Test
    public void testFromJsonNodeMapBooleanFalse() {
        this.fromJsonNodeMapAndCheck2(JsonNode.booleanNode(false),
                Boolean.class,
                false);
    }

    @Test
    public void testFromJsonNodeMapNull() {
        this.fromJsonNodeMapAndCheck2(JsonNode.nullNode(),
                Boolean.class,
                null);
    }

    @Test
    public void testFromJsonNodeMapNumber() {
        final double value = 1.25;
        this.fromJsonNodeMapAndCheck2(JsonNode.number(value),
                Double.class,
                value);
    }

    @Test
    public void testFromJsonNodeMapString() {
        final String value = "abc123";
        this.fromJsonNodeMapAndCheck2(JsonNode.string(value),
                String.class,
                value);
    }

    private <VV> void fromJsonNodeMapAndCheck2(final JsonNode value,
                                               final Class<VV> valueType,
                                               final VV javaValue) {
        final String KEY = "key1";

        this.fromJsonNodeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicMarshallerTypedMap.ENTRY_KEY, JsonNode.string(KEY))
                                .set(BasicMarshallerTypedMap.ENTRY_VALUE, value)),
                String.class,
                valueType,
                Maps.of(KEY, javaValue));
    }

    // fromJsonNodeWithType.............................................................................................

    @Test
    public void testFromJsonNodeWithTypeBooleanTrue() {
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("boolean", JsonNode.booleanNode(true)),
                true);
    }

    @Test
    public void testFromJsonNodeWithTypeBooleanFalse() {
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("boolean", JsonNode.booleanNode(false)),
                false);
    }

    @Test
    public void testFromJsonNodeWithTypeNull() {
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("boolean", JsonNode.nullNode()),
                null);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberByte() {
        final byte value = 123;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("byte", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberShort() {
        final short value = 123;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("short", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberInteger() {
        final int value = 123;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("int", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberLong() {
        final long value = 123;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("long", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberFloat() {
        final float value = 123.5f;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("float", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberDouble() {
        final double value = 1.25;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("double", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeObject() {
        final Locale value = Locale.ENGLISH;
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("locale", JsonNode.string(value.toLanguageTag())),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeString() {
        final String value = "abc123";
        this.fromJsonNodeWithTypeAndCheck(this.typeAndValue("string", JsonNode.string(value)),
                value);
    }

    // fromJsonNodeWithTypeList.........................................................................................

    @Test
    public void testFromJsonNodeWithTypeListBooleanTrue() {
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("boolean", JsonNode.booleanNode(true)),
                Lists.of(true));
    }

    @Test
    public void testFromJsonNodeWithTypeListBooleanFalse() {
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("boolean", JsonNode.booleanNode(false)),
                Lists.of(false));
    }

    @Test
    public void testFromJsonNodeWithTypeListNull() {
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("boolean", JsonNode.nullNode()),
                Lists.of((Object) null));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberByte() {
        final byte value = 1;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("byte", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberShort() {
        final short value = 1;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("short", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberInteger() {
        final int value = 123;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("int", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberLong() {
        final long value = 123;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("long", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberFloat() {
        final float value = 1.25f;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("float", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberDouble() {
        final double value = 1.25;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("double", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListObject() {
        final Locale value = Locale.ENGLISH;
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("locale", JsonNode.string(value.toLanguageTag())),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListString() {
        final String value = "abc123";
        this.fromJsonNodeWithTypeListAndCheck(this.listWithType("string", JsonNode.string(value)),
                Lists.of(value));
    }

    private JsonNode listWithType(final String typeName,
                                  final JsonNode node) {
        return JsonNode.array().appendChild(this.typeAndValue(typeName, node));
    }

    // fromJsonNodeWithTypeSet.........................................................................................

    @Test
    public void testFromJsonNodeWithTypeSetBooleanTrue() {
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("boolean", JsonNode.booleanNode(true)),
                Sets.of(true));
    }

    @Test
    public void testFromJsonNodeWithTypeSetBooleanFalse() {
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("boolean", JsonNode.booleanNode(false)),
                Sets.of(false));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNull() {
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("boolean", JsonNode.nullNode()),
                Sets.of((Object) null));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberByte() {
        final byte value = 1;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("byte", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberShort() {
        final short value = 1;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("short", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberInteger() {
        final int value = 123;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("int", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberLong() {
        final long value = 123;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("long", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberFloat() {
        final float value = 1.25f;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("float", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberDouble() {
        final double value = 1.25;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("double", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetObject() {
        final Locale value = Locale.ENGLISH;
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("locale", JsonNode.string(value.toLanguageTag())),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetString() {
        final String value = "abc123";
        this.fromJsonNodeWithTypeSetAndCheck(this.setWithType("string", JsonNode.string(value)),
                Sets.of(value));
    }

    private JsonNode setWithType(final String typeName,
                                 final JsonNode node) {
        return JsonNode.array().appendChild(this.typeAndValue(typeName, node));
    }

    // fromJsonNodeWithTypeMap...........................................................................................

    @Test
    public void testFromJsonNodeWithTypeMapBooleanTrue() {
        this.fromJsonNodeWithTypeMapAndCheck3(JsonNode.booleanNode(true),
                true);
    }

    @Test
    public void testFromJsonNodeWithTypeMapBooleanFalse() {
        this.fromJsonNodeWithTypeMapAndCheck3(JsonNode.booleanNode(false),
                false);
    }

    @Test
    public void testFromJsonNodeWithTypeMapNull() {
        this.fromJsonNodeWithTypeMapAndCheck3(JsonNode.nullNode(),
                null);
    }

    @Test
    public void testFromJsonNodeWithTypeMapNumber() {
        final double value = 1.25;
        this.fromJsonNodeWithTypeMapAndCheck3(JsonNode.number(value),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeMapString() {
        final String value = "abc123";
        this.fromJsonNodeWithTypeMapAndCheck3(JsonNode.string(value),
                value);
    }

    private <VV> void fromJsonNodeWithTypeMapAndCheck3(final JsonNode jsonValue,
                                                       final VV javaValue) {
        final String KEY = "key1";

        this.fromJsonNodeWithTypeMapAndCheck(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicMarshallerTypedMap.ENTRY_KEY, JsonNode.string(KEY))
                                .set(BasicMarshallerTypedMap.ENTRY_VALUE, jsonValue)),
                Maps.of(KEY, javaValue));
    }

    // FromJsonNodeContext..............................................................................................

    @Override
    public BasicFromJsonNodeContext createContext() {
        return BasicFromJsonNodeContext.INSTANCE;
    }

    @Override
    public Class<BasicFromJsonNodeContext> type() {
        return BasicFromJsonNodeContext.class;
    }
}
