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

public final class BasicToJsonNodeContextTest extends BasicJsonNodeContextTestCase<BasicToJsonNodeContext>
        implements ToJsonNodeContextTesting<BasicToJsonNodeContext> {

    // toJsonNode.....................................................................................................

    @Test
    public void testToJsonNodeBooleanTrue() {
        this.toJsonNodeAndCheck(true,
                JsonNode.booleanNode(true));
    }

    @Test
    public void testToJsonNodeBooleanFalse() {
        this.toJsonNodeAndCheck(false,
                JsonNode.booleanNode(false));
    }

    @Test
    public void testToJsonNodeNull() {
        this.toJsonNodeAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeNumber() {
        final double value = 1.25;
        this.toJsonNodeAndCheck(value,
                JsonNode.number(value));
    }

    @Test
    public void testToJsonNodeString() {
        final String value = "abc123";
        this.toJsonNodeAndCheck(value,
                JsonNode.string(value));
    }

    @Test
    public void testToJsonNodeWithObjectPostProcessor() {
        final TestJsonNodeValue value = TestJsonNodeValue.with("abc123");
        this.toJsonNodeAndCheck(this.contextWithProcessor(),
                value,
                value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE));
    }

    // toJsonNodeList...................................................................................................

    @Test
    public void testToJsonNodeListNullList() {
        this.toJsonNodeListAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeListBooleanTrue() {
        this.toJsonNodeListAndCheck(Lists.of(true),
                list(JsonNode.booleanNode(true)));
    }

    @Test
    public void testToJsonNodeListBooleanFalse() {
        this.toJsonNodeListAndCheck(Lists.of(false),
                list(JsonNode.booleanNode(false)));
    }

    @Test
    public void testToJsonNodeListNull() {
        this.toJsonNodeListAndCheck(Lists.of((Object)null),
                list(JsonNode.nullNode()));
    }

    @Test
    public void testToJsonNodeListNumber() {
        final double value = 1.25;
        this.toJsonNodeListAndCheck(Lists.of(value),
                list(JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeListString() {
        final String value = "abc123";
        this.toJsonNodeListAndCheck(Lists.of(value),
                list(JsonNode.string(value)));
    }

    @Test
    public void testToJsonNodeListWithObjectPostProcessor() {
        final TestJsonNodeValue value = TestJsonNodeValue.with("abc123");
        this.toJsonNodeListAndCheck(this.contextWithProcessor(),
                Lists.of(value),
                list(value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE)));
    }

    private JsonArrayNode list(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // toJsonNodeSet....................................................................................................

    @Test
    public void testToJsonNodeSetNullSet() {
        this.toJsonNodeSetAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeSetBooleanTrue() {
        this.toJsonNodeSetAndCheck(Sets.of(true),
                set(JsonNode.booleanNode(true)));
    }

    @Test
    public void testToJsonNodeSetBooleanFalse() {
        this.toJsonNodeSetAndCheck(Sets.of(false),
                set(JsonNode.booleanNode(false)));
    }

    @Test
    public void testToJsonNodeSetNullElement() {
        this.toJsonNodeSetAndCheck(Sets.of((Object) null),
                set(JsonNode.nullNode()));
    }

    @Test
    public void testToJsonNodeSetNumber() {
        final double value = 1.25;
        this.toJsonNodeSetAndCheck(Sets.of(value),
                set(JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeSetString() {
        final String value = "abc123";
        this.toJsonNodeSetAndCheck(Sets.of(value),
                set(JsonNode.string(value)));
    }

    @Test
    public void testToJsonNodeSetWithObjectPostProcessor() {
        final TestJsonNodeValue value = TestJsonNodeValue.with("abc123");
        this.toJsonNodeSetAndCheck(this.contextWithProcessor(),
                Sets.of(value),
                set(value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE)));
    }

    private JsonArrayNode set(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // toJsonNodeMap....................................................................................................

    @Test
    public void testToJsonNodeMapNullMap() {
        this.toJsonNodeMapAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeMapBooleanTrue() {
        this.toJsonNodeMapAndCheck2(true,
                JsonNode.booleanNode(true));
    }

    @Test
    public void testToJsonNodeMapBooleanFalse() {
        this.toJsonNodeMapAndCheck2(false,
                JsonNode.booleanNode(false));
    }

    @Test
    public void testToJsonNodeMapNullValue() {
        this.toJsonNodeMapAndCheck2(null,
                JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeMapNumber() {
        final double value = 1.25;
        this.toJsonNodeMapAndCheck2(value,
                JsonNode.number(value));
    }

    @Test
    public void testToJsonNodeMapString() {
        final String value = "abc123";
        this.toJsonNodeMapAndCheck2(value,
                JsonNode.string(value));
    }

    private <VV> void toJsonNodeMapAndCheck2(final VV value,
                                             final JsonNode expected) {
        final String KEY = "key1";

        this.toJsonNodeMapAndCheck(Maps.of(KEY, value),
                this.map(KEY, expected)
        );
    }

    @Test
    public void testToJsonNodeMapWithObjectPostProcessor() {
        final String key = "key-123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(key);
        this.toJsonNodeMapAndCheck(this.contextWithProcessor(),
                Maps.of(key, value),
                this.map(key, value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE)));
    }

    private JsonNode map(final String key, final JsonNode value) {
        return JsonNode.array()
                .appendChild(JsonNode.object()
                        .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(key))
                        .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, value));
    }

    // toJsonNodeWithType...............................................................................................

    @Test
    public void testToJsonNodeWithTypeBooleanTrue() {
        this.toJsonNodeWithTypeAndCheck(true, JsonNode.booleanNode(true));
    }

    @Test
    public void testToJsonNodeWithTypeBooleanFalse() {
        this.toJsonNodeWithTypeAndCheck(false, JsonNode.booleanNode(false));
    }

    @Test
    public void testToJsonNodeWithTypeNull() {
        this.toJsonNodeWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeWithTypeNumberByte() {
        final byte value = 123;
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeAndValue("byte", JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeWithTypeNumberShort() {
        final short value = 123;
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeAndValue("short", JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeWithTypeNumberInteger() {
        final int value = 123;
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeAndValue("int", JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeWithTypeNumberLong() {
        final long value = 123;
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeAndValue("long", JsonNode.string(String.valueOf(value))));
    }

    @Test
    public void testToJsonNodeWithTypeNumberFloat() {
        final float value = 123.5f;
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeAndValue("float", JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeWithTypeNumberDouble() {
        final double value = 1.25;
        this.toJsonNodeWithTypeAndCheck(value, JsonNode.number(value));
    }

    @Test
    public void testToJsonNodeWithTypeObject() {
        final Locale value = Locale.ENGLISH;
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeAndValue("locale", JsonNode.string(value.toLanguageTag())));
    }

    @Test
    public void testToJsonNodeWithTypeString() {
        final String value = "abc123";
        this.toJsonNodeWithTypeAndCheck(value, JsonNode.string(value));
    }

    @Test
    public void testToJsonNodeWithTypeObjectPostProcessor() {
        final String string = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(string);
        this.toJsonNodeWithTypeAndCheck(this.contextWithProcessor(),
                value,
                this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE)));
    }

    // toJsonNodeWithTypeList...........................................................................................

    @Test
    public void testToJsonNodeWithTypeListNullList() {
        this.toJsonNodeWithTypeListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeWithTypeListBooleanTrue() {
        this.toJsonNodeWithTypeListAndCheck(Lists.of(true),
                this.list(JsonNode.booleanNode(true)));
    }

    @Test
    public void testToJsonNodeWithTypeListBooleanFalse() {
        this.toJsonNodeWithTypeListAndCheck(Lists.of(false),
                this.list(JsonNode.booleanNode(false)));
    }

    @Test
    public void testToJsonNodeWithTypeListNullElement() {
        this.toJsonNodeWithTypeListAndCheck(Lists.of((Object)null),
                this.list(JsonNode.nullNode()));
    }

    @Test
    public void testToJsonNodeWithTypeListNumberByte() {
        final byte value = 1;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("byte", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeListNumberShort() {
        final short value = 1;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("short", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeListNumberInteger() {
        final int value = 123;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("int", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeListNumberLong() {
        final long value = 123;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("long", JsonNode.string(String.valueOf(value)))));
    }

    @Test
    public void testToJsonNodeWithTypeListNumberFloat() {
        final float value = 1.25f;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("float", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeListNumberDouble() {
        final double value = 1.25;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeWithTypeListObject() {
        final Locale value = Locale.ENGLISH;
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("locale", JsonNode.string(value.toLanguageTag()))));
    }

    @Test
    public void testToJsonNodeWithTypeListString() {
        final String value = "abc123";
        this.toJsonNodeWithTypeListAndCheck(Lists.of(value),
                this.list(JsonNode.string(value)));
    }

    @Test
    public void testToJsonNodeWithTypeListWithObjectPostProcessor() {
        final String string = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(string);

        this.toJsonNodeWithTypeListAndCheck(this.contextWithProcessor(),
                Lists.of(value),
                this.list(this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE))));
    }

    // toJsonNodeWithTypeSet............................................................................................

    @Test
    public void testToJsonNodeWithTypeSetNullSet() {
        this.toJsonNodeWithTypeSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeWithTypeSetBooleanTrue() {
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(true),
                this.set(JsonNode.booleanNode(true)));
    }

    @Test
    public void testToJsonNodeWithTypeSetBooleanFalse() {
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(false),
                this.set(JsonNode.booleanNode(false)));
    }

    @Test
    public void testToJsonNodeWithTypeSetNullElement() {
        this.toJsonNodeWithTypeSetAndCheck(Sets.of((Object)null),
                this.set(JsonNode.nullNode()));
    }

    @Test
    public void testToJsonNodeWithTypeSetNumberByte() {
        final byte value = 1;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("byte", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeSetNumberShort() {
        final short value = 1;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("short", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeSetNumberInteger() {
        final int value = 123;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("int", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeSetNumberLong() {
        final long value = 123;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("long", JsonNode.string(String.valueOf(value)))));
    }

    @Test
    public void testToJsonNodeWithTypeSetNumberFloat() {
        final float value = 1.25f;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("float", JsonNode.number(value))));
    }

    @Test
    public void testToJsonNodeWithTypeSetNumberDouble() {
        final double value = 1.25;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(JsonNode.number(value)));
    }

    @Test
    public void testToJsonNodeWithTypeSetObject() {
        final Locale value = Locale.ENGLISH;
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("locale", JsonNode.string(value.toLanguageTag()))));
    }

    @Test
    public void testToJsonNodeWithTypeSetString() {
        final String value = "abc123";
        this.toJsonNodeWithTypeSetAndCheck(Sets.of(value),
                this.set(JsonNode.string(value)));
    }

    @Test
    public void testToJsonNodeWithTypeSetWithObjectPostProcessor() {
        final String string = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(string);

        this.toJsonNodeWithTypeSetAndCheck(this.contextWithProcessor(),
                Sets.of(value),
                this.set(this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE))));
    }

    // toJsonNodeWithTypeMap...........................................................................................

    @Test
    public void testToJsonNodeWithTypeMapNullMap() {
        this.toJsonNodeWithTypeMapAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeWithTypeMapBooleanTrue() {
        this.toJsonNodeWithTypeMapAndCheck3(JsonNode.booleanNode(true),
                true);
    }

    @Test
    public void testToJsonNodeWithTypeMapBooleanFalse() {
        this.toJsonNodeWithTypeMapAndCheck3(JsonNode.booleanNode(false),
                false);
    }

    @Test
    public void testToJsonNodeWithTypeMapNullValue() {
        this.toJsonNodeWithTypeMapAndCheck3(JsonNode.nullNode(),
                null);
    }

    @Test
    public void testToJsonNodeWithTypeMapNumber() {
        final double value = 1.25;
        this.toJsonNodeWithTypeMapAndCheck3(JsonNode.number(value),
                value);
    }

    @Test
    public void testToJsonNodeWithTypeMapString() {
        final String value = "abc123";
        this.toJsonNodeWithTypeMapAndCheck3(JsonNode.string(value),
                value);
    }

    private <VV> void toJsonNodeWithTypeMapAndCheck3(final JsonNode value,
                                                     final VV javaValue) {
        final String KEY = "key1";

        this.toJsonNodeWithTypeMapAndCheck(Maps.of(KEY, javaValue),
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(KEY))
                                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, value)));
    }

    @Test
    public void testToJsonNodeWithTypeMapWithObjectPostProcessor() {
        final String key = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(key);

        this.toJsonNodeWithTypeMapAndCheck(this.contextWithProcessor(),
                Maps.of(key, value),
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(key))
                                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.toJsonNode(ToJsonNodeContexts.fake()).set(POST, POST_VALUE)))));
    }

    // ToJsonNodeContext................................................................................................

    @Override
    public BasicToJsonNodeContext createContext() {
        return BasicToJsonNodeContext.INSTANCE;
    }

    private ToJsonNodeContext contextWithProcessor() {
        return this.createContext().setObjectPostProcessor(this::objectPostProcessor);
    }

    private JsonObjectNode objectPostProcessor(final Object value, JsonObjectNode object) {
        return object.set(POST, POST_VALUE);
    }

    private final static JsonNodeName POST = JsonNodeName.with("post");
    private final static JsonNode POST_VALUE = JsonNode.booleanNode(true);

    @Override
    public Class<BasicToJsonNodeContext> type() {
        return BasicToJsonNodeContext.class;
    }
}
