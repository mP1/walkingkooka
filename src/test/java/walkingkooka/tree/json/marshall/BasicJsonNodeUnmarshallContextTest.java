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
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Locale;

public final class BasicJsonNodeUnmarshallContextTest extends BasicJsonNodeContextTestCase<BasicJsonNodeUnmarshallContext>
        implements JsonNodeUnmarshallContextTesting<BasicJsonNodeUnmarshallContext> {

    // unmarshall.....................................................................................................

    @Test
    public void testFromJsonNodeBooleanTrue() {
        this.unmarshallAndCheck(JsonNode.booleanNode(true),
                Boolean.class,
                true);
    }

    @Test
    public void testFromJsonNodeBooleanFalse() {
        this.unmarshallAndCheck(JsonNode.booleanNode(false),
                Boolean.class,
                false);
    }

    @Test
    public void testFromJsonNodeNull() {
        this.unmarshallAndCheck(JsonNode.nullNode(),
                Boolean.class,
                null);
    }

    @Test
    public void testFromJsonNodeNumber() {
        final double value = 1.25;
        this.unmarshallAndCheck(JsonNode.number(value),
                Double.class,
                value);
    }

    @Test
    public void testFromJsonNodeString() {
        final String value = "abc123";
        this.unmarshallAndCheck(JsonNode.string(value),
                String.class,
                value);
    }

    @Test
    public void testFromJsonNodeObject() {
        this.unmarshallAndCheck(this.jsonNode(),
                TestJsonNodeValue.class,
                this.value());
    }

    @Test
    public void testFromJsonNodeObjectWithObjectPreProcessor() {
        this.unmarshallAndCheck(this.contextWithProcessor(),
                this.jsonNode2(),
                TestJsonNodeValue.class,
                this.value());
    }

    // unmarshallList.................................................................................................

    @Test
    public void testFromJsonNodeListBooleanTrue() {
        this.unmarshallListAndCheck(list(JsonNode.booleanNode(true)),
                Boolean.class,
                Lists.of(true));
    }

    @Test
    public void testFromJsonNodeListBooleanFalse() {
        this.unmarshallListAndCheck(list(JsonNode.booleanNode(false)),
                Boolean.class,
                Lists.of(false));
    }

    @Test
    public void testFromJsonNodeListNull() {
        this.unmarshallListAndCheck(list(JsonNode.nullNode()),
                Boolean.class,
                Lists.of((Boolean) null));
    }

    @Test
    public void testFromJsonNodeListNumber() {
        final double value = 1.25;
        this.unmarshallListAndCheck(list(JsonNode.number(value)),
                Double.class,
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeListString() {
        final String value = "abc123";
        this.unmarshallListAndCheck(list(JsonNode.string(value)),
                String.class,
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeList() {
        this.unmarshallListAndCheck(this.list(this.jsonNode()),
                TestJsonNodeValue.class,
                Lists.of(this.value()));
    }

    @Test
    public void testFromJsonNodeListObjectWithObjectPreProcessor() {
        this.unmarshallListAndCheck(this.contextWithProcessor(),
                this.list(this.jsonNode2()),
                TestJsonNodeValue.class,
                Lists.of(this.value()));
    }

    private JsonArrayNode list(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // unmarshallSet..................................................................................................

    @Test
    public void testFromJsonNodeSetBooleanTrue() {
        this.unmarshallSetAndCheck(set(JsonNode.booleanNode(true)),
                Boolean.class,
                Sets.of(true));
    }

    @Test
    public void testFromJsonNodeSetBooleanFalse() {
        this.unmarshallSetAndCheck(set(JsonNode.booleanNode(false)),
                Boolean.class,
                Sets.of(false));
    }

    @Test
    public void testFromJsonNodeSetNull() {
        this.unmarshallSetAndCheck(set(JsonNode.nullNode()),
                Boolean.class,
                Sets.of((Boolean) null));
    }

    @Test
    public void testFromJsonNodeSetNumber() {
        final double value = 1.25;
        this.unmarshallSetAndCheck(set(JsonNode.number(value)),
                Double.class,
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeSetString() {
        final String value = "abc123";
        this.unmarshallSetAndCheck(set(JsonNode.string(value)),
                String.class,
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeSetObject() {
        this.unmarshallSetAndCheck(this.set(this.jsonNode()),
                TestJsonNodeValue.class,
                Sets.of(this.value()));
    }

    @Test
    public void testFromJsonNodeSetObjectWithObjectPreProcessor() {
        this.unmarshallSetAndCheck(this.contextWithProcessor(),
                this.set(this.jsonNode2()),
                TestJsonNodeValue.class,
                Sets.of(this.value()));
    }

    private JsonArrayNode set(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // unmarshallMap..................................................................................................

    @Test
    public void testFromJsonNodeMapBooleanTrue() {
        this.unmarshallMapAndCheck2(JsonNode.booleanNode(true),
                Boolean.class,
                true);
    }

    @Test
    public void testFromJsonNodeMapBooleanFalse() {
        this.unmarshallMapAndCheck2(JsonNode.booleanNode(false),
                Boolean.class,
                false);
    }

    @Test
    public void testFromJsonNodeMapNull() {
        this.unmarshallMapAndCheck2(JsonNode.nullNode(),
                Boolean.class,
                null);
    }

    @Test
    public void testFromJsonNodeMapNumber() {
        final double value = 1.25;
        this.unmarshallMapAndCheck2(JsonNode.number(value),
                Double.class,
                value);
    }

    @Test
    public void testFromJsonNodeMapString() {
        final String value = "abc123";
        this.unmarshallMapAndCheck2(JsonNode.string(value),
                String.class,
                value);
    }

    @Test
    public void testFromJsonNodeMapObject() {
        this.unmarshallMapAndCheck2(this.jsonNode(),
                TestJsonNodeValue.class,
                this.value());
    }

    @Test
    public void testFromJsonNodeMapObjectWithObjectPreProcessor() {
        this.unmarshallMapAndCheck2(this.contextWithProcessor(),
                this.jsonNode2(),
                TestJsonNodeValue.class,
                this.value());
    }

    private <VV> void unmarshallMapAndCheck2(final JsonNode value,
                                             final Class<VV> valueType,
                                             final VV javaValue) {
        this.unmarshallMapAndCheck2(this.createContext(),
                value,
                valueType,
                javaValue);
    }

    private <VV> void unmarshallMapAndCheck2(final JsonNodeUnmarshallContext context,
                                             final JsonNode value,
                                             final Class<VV> valueType,
                                             final VV javaValue) {
        this.unmarshallMapAndCheck(context,
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(KEY))
                                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, value)),
                String.class,
                valueType,
                Maps.of(KEY, javaValue));
    }

    // unmarshallWithType.............................................................................................

    @Test
    public void testFromJsonNodeWithTypeBooleanTrue() {
        this.unmarshallWithTypeAndCheck(this.typeAndValue("boolean", JsonNode.booleanNode(true)),
                true);
    }

    @Test
    public void testFromJsonNodeWithTypeBooleanFalse() {
        this.unmarshallWithTypeAndCheck(this.typeAndValue("boolean", JsonNode.booleanNode(false)),
                false);
    }

    @Test
    public void testFromJsonNodeWithTypeNull() {
        this.unmarshallWithTypeAndCheck(this.typeAndValue("boolean", JsonNode.nullNode()),
                null);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberByte() {
        final byte value = 123;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("byte", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberShort() {
        final short value = 123;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("short", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberInteger() {
        final int value = 123;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("int", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberLong() {
        final long value = 123;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("long", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberFloat() {
        final float value = 123.5f;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("float", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeNumberDouble() {
        final double value = 1.25;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("double", JsonNode.number(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeObject() {
        final Locale value = Locale.ENGLISH;
        this.unmarshallWithTypeAndCheck(this.typeAndValue("locale", JsonNode.string(value.toLanguageTag())),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeString() {
        final String value = "abc123";
        this.unmarshallWithTypeAndCheck(this.typeAndValue("string", JsonNode.string(value)),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeObjectWithObjectPreProcessor() {
        this.unmarshallWithTypeAndCheck(this.contextWithProcessor(),
                this.typeAndValue(TestJsonNodeValue.TYPE_NAME, this.jsonNode2()),
                this.value());
    }

    // unmarshallWithTypeList.........................................................................................

    @Test
    public void testFromJsonNodeWithTypeListBooleanTrue() {
        this.unmarshallWithTypeListAndCheck(this.listWithType("boolean", JsonNode.booleanNode(true)),
                Lists.of(true));
    }

    @Test
    public void testFromJsonNodeWithTypeListBooleanFalse() {
        this.unmarshallWithTypeListAndCheck(this.listWithType("boolean", JsonNode.booleanNode(false)),
                Lists.of(false));
    }

    @Test
    public void testFromJsonNodeWithTypeListNull() {
        this.unmarshallWithTypeListAndCheck(this.listWithType("boolean", JsonNode.nullNode()),
                Lists.of((Object) null));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberByte() {
        final byte value = 1;
        this.unmarshallWithTypeListAndCheck(this.listWithType("byte", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberShort() {
        final short value = 1;
        this.unmarshallWithTypeListAndCheck(this.listWithType("short", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberInteger() {
        final int value = 123;
        this.unmarshallWithTypeListAndCheck(this.listWithType("int", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberLong() {
        final long value = 123;
        this.unmarshallWithTypeListAndCheck(this.listWithType("long", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberFloat() {
        final float value = 1.25f;
        this.unmarshallWithTypeListAndCheck(this.listWithType("float", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListNumberDouble() {
        final double value = 1.25;
        this.unmarshallWithTypeListAndCheck(this.listWithType("double", JsonNode.number(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListObject() {
        final Locale value = Locale.ENGLISH;
        this.unmarshallWithTypeListAndCheck(this.listWithType("locale", JsonNode.string(value.toLanguageTag())),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListString() {
        final String value = "abc123";
        this.unmarshallWithTypeListAndCheck(this.listWithType("string", JsonNode.string(value)),
                Lists.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeListObjectWithObjectPreProcessor() {
        this.unmarshallWithTypeListAndCheck(this.contextWithProcessor(),
                this.listWithType(TestJsonNodeValue.TYPE_NAME, this.jsonNode2()),
                Lists.of(this.value()));
    }

    private JsonNode listWithType(final String typeName,
                                  final JsonNode node) {
        return JsonNode.array().appendChild(this.typeAndValue(typeName, node));
    }

    // unmarshallWithTypeSet.........................................................................................

    @Test
    public void testFromJsonNodeWithTypeSetBooleanTrue() {
        this.unmarshallWithTypeSetAndCheck(this.setWithType("boolean", JsonNode.booleanNode(true)),
                Sets.of(true));
    }

    @Test
    public void testFromJsonNodeWithTypeSetBooleanFalse() {
        this.unmarshallWithTypeSetAndCheck(this.setWithType("boolean", JsonNode.booleanNode(false)),
                Sets.of(false));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNull() {
        this.unmarshallWithTypeSetAndCheck(this.setWithType("boolean", JsonNode.nullNode()),
                Sets.of((Object) null));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberByte() {
        final byte value = 1;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("byte", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberShort() {
        final short value = 1;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("short", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberInteger() {
        final int value = 123;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("int", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberLong() {
        final long value = 123;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("long", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberFloat() {
        final float value = 1.25f;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("float", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetNumberDouble() {
        final double value = 1.25;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("double", JsonNode.number(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetObject() {
        final Locale value = Locale.ENGLISH;
        this.unmarshallWithTypeSetAndCheck(this.setWithType("locale", JsonNode.string(value.toLanguageTag())),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetString() {
        final String value = "abc123";
        this.unmarshallWithTypeSetAndCheck(this.setWithType("string", JsonNode.string(value)),
                Sets.of(value));
    }

    @Test
    public void testFromJsonNodeWithTypeSetObjectWithObjectPreProcessor() {
        this.unmarshallWithTypeSetAndCheck(this.contextWithProcessor(),
                this.setWithType(TestJsonNodeValue.TYPE_NAME, this.jsonNode2()),
                Sets.of(this.value()));
    }

    private JsonNode setWithType(final String typeName,
                                 final JsonNode node) {
        return JsonNode.array().appendChild(this.typeAndValue(typeName, node));
    }

    // unmarshallWithTypeMap...........................................................................................

    @Test
    public void testFromJsonNodeWithTypeMapBooleanTrue() {
        this.unmarshallWithTypeMapAndCheck3(JsonNode.booleanNode(true),
                true);
    }

    @Test
    public void testFromJsonNodeWithTypeMapBooleanFalse() {
        this.unmarshallWithTypeMapAndCheck3(JsonNode.booleanNode(false),
                false);
    }

    @Test
    public void testFromJsonNodeWithTypeMapNull() {
        this.unmarshallWithTypeMapAndCheck3(JsonNode.nullNode(),
                null);
    }

    @Test
    public void testFromJsonNodeWithTypeMapNumber() {
        final double value = 1.25;
        this.unmarshallWithTypeMapAndCheck3(JsonNode.number(value),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeMapString() {
        final String value = "abc123";
        this.unmarshallWithTypeMapAndCheck3(JsonNode.string(value),
                value);
    }

    @Test
    public void testFromJsonNodeWithTypeMapObject() {
        this.unmarshallWithTypeMapAndCheck3(this.createContext(),
                this.typeAndValue(TestJsonNodeValue.TYPE_NAME, this.jsonNode()),
                this.value());
    }

    @Test
    public void testFromJsonNodeWithTypeMapStringObjectWithObjectPreProcessor() {
        this.unmarshallWithTypeMapAndCheck3(this.contextWithProcessor(),
                this.typeAndValue(TestJsonNodeValue.TYPE_NAME, this.jsonNode2()),
                this.value());
    }

    private <VV> void unmarshallWithTypeMapAndCheck3(final JsonNode jsonValue,
                                                     final VV javaValue) {
        this.unmarshallWithTypeMapAndCheck3(this.createContext(),
                jsonValue,
                javaValue);
    }

    private <VV> void unmarshallWithTypeMapAndCheck3(final JsonNodeUnmarshallContext context,
                                                     final JsonNode jsonValue,
                                                     final VV javaValue) {
        this.unmarshallMapWithTypeAndCheck(context,
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(KEY))
                                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, jsonValue)),
                Maps.of(KEY, javaValue));
    }

    // JsonNodeUnmarshallContext..............................................................................................

    @Override
    public BasicJsonNodeUnmarshallContext createContext() {
        return BasicJsonNodeUnmarshallContext.INSTANCE;
    }

    private JsonNodeUnmarshallContext contextWithProcessor() {
        return BasicJsonNodeUnmarshallContext.INSTANCE.setObjectPreProcessor(this::objectPreProcessor2);
    }

    private JsonObjectNode objectPreProcessor2(final JsonObjectNode object, final Class<?> type) {
        return object.remove(POST);
    }

    private JsonObjectNode jsonNode() {
        return JsonNode.object()
                .set(TestJsonNodeValue.KEY, JsonNode.string(VALUE));
    }

    private JsonObjectNode jsonNode2() {
        return this.jsonNode()
                .set(POST, POST_VALUE);
    }

    private TestJsonNodeValue value() {
        return TestJsonNodeValue.with(VALUE);
    }

    private final static JsonNodeName POST = JsonNodeName.with("post");
    private final static JsonNode POST_VALUE = JsonNode.booleanNode(true);
    private final static String VALUE = "abc123";
    private final static String KEY = "key1";

    @Override
    public Class<BasicJsonNodeUnmarshallContext> type() {
        return BasicJsonNodeUnmarshallContext.class;
    }
}
