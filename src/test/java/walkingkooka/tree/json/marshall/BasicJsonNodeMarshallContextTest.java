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

public final class BasicJsonNodeMarshallContextTest extends BasicJsonNodeContextTestCase<BasicJsonNodeMarshallContext>
        implements JsonNodeMarshallContextTesting<BasicJsonNodeMarshallContext> {

    // marshall.....................................................................................................

    @Test
    public void testMarshallBooleanTrue() {
        this.marshallAndCheck(true,
                JsonNode.booleanNode(true));
    }

    @Test
    public void testMarshallBooleanFalse() {
        this.marshallAndCheck(false,
                JsonNode.booleanNode(false));
    }

    @Test
    public void testMarshallNull() {
        this.marshallAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testMarshallNumber() {
        final double value = 1.25;
        this.marshallAndCheck(value,
                JsonNode.number(value));
    }

    @Test
    public void testMarshallString() {
        final String value = "abc123";
        this.marshallAndCheck(value,
                JsonNode.string(value));
    }

    @Test
    public void testMarshallWithObjectPostProcessor() {
        final TestJsonNodeValue value = TestJsonNodeValue.with("abc123");
        this.marshallAndCheck(this.contextWithProcessor(),
                value,
                value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE));
    }

    // marshallList...................................................................................................

    @Test
    public void testMarshallListNullList() {
        this.marshallListAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testMarshallListBooleanTrue() {
        this.marshallListAndCheck(Lists.of(true),
                list(JsonNode.booleanNode(true)));
    }

    @Test
    public void testMarshallListBooleanFalse() {
        this.marshallListAndCheck(Lists.of(false),
                list(JsonNode.booleanNode(false)));
    }

    @Test
    public void testMarshallListNull() {
        this.marshallListAndCheck(Lists.of((Object)null),
                list(JsonNode.nullNode()));
    }

    @Test
    public void testMarshallListNumber() {
        final double value = 1.25;
        this.marshallListAndCheck(Lists.of(value),
                list(JsonNode.number(value)));
    }

    @Test
    public void testMarshallListString() {
        final String value = "abc123";
        this.marshallListAndCheck(Lists.of(value),
                list(JsonNode.string(value)));
    }

    @Test
    public void testMarshallListWithObjectPostProcessor() {
        final TestJsonNodeValue value = TestJsonNodeValue.with("abc123");
        this.marshallListAndCheck(this.contextWithProcessor(),
                Lists.of(value),
                list(value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE)));
    }

    private JsonArrayNode list(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // marshallSet....................................................................................................

    @Test
    public void testMarshallSetNullSet() {
        this.marshallSetAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testMarshallSetBooleanTrue() {
        this.marshallSetAndCheck(Sets.of(true),
                set(JsonNode.booleanNode(true)));
    }

    @Test
    public void testMarshallSetBooleanFalse() {
        this.marshallSetAndCheck(Sets.of(false),
                set(JsonNode.booleanNode(false)));
    }

    @Test
    public void testMarshallSetNullElement() {
        this.marshallSetAndCheck(Sets.of((Object) null),
                set(JsonNode.nullNode()));
    }

    @Test
    public void testMarshallSetNumber() {
        final double value = 1.25;
        this.marshallSetAndCheck(Sets.of(value),
                set(JsonNode.number(value)));
    }

    @Test
    public void testMarshallSetString() {
        final String value = "abc123";
        this.marshallSetAndCheck(Sets.of(value),
                set(JsonNode.string(value)));
    }

    @Test
    public void testMarshallSetWithObjectPostProcessor() {
        final TestJsonNodeValue value = TestJsonNodeValue.with("abc123");
        this.marshallSetAndCheck(this.contextWithProcessor(),
                Sets.of(value),
                set(value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE)));
    }

    private JsonArrayNode set(final JsonNode element) {
        return JsonNode.array().appendChild(element);
    }

    // marshallMap....................................................................................................

    @Test
    public void testMarshallMapNullMap() {
        this.marshallMapAndCheck(null,
                JsonNode.nullNode());
    }

    @Test
    public void testMarshallMapBooleanTrue() {
        this.marshallMapAndCheck2(true,
                JsonNode.booleanNode(true));
    }

    @Test
    public void testMarshallMapBooleanFalse() {
        this.marshallMapAndCheck2(false,
                JsonNode.booleanNode(false));
    }

    @Test
    public void testMarshallMapNullValue() {
        this.marshallMapAndCheck2(null,
                JsonNode.nullNode());
    }

    @Test
    public void testMarshallMapNumber() {
        final double value = 1.25;
        this.marshallMapAndCheck2(value,
                JsonNode.number(value));
    }

    @Test
    public void testMarshallMapString() {
        final String value = "abc123";
        this.marshallMapAndCheck2(value,
                JsonNode.string(value));
    }

    private <VV> void marshallMapAndCheck2(final VV value,
                                             final JsonNode expected) {
        final String KEY = "key1";

        this.marshallMapAndCheck(Maps.of(KEY, value),
                this.map(KEY, expected)
        );
    }

    @Test
    public void testMarshallMapWithObjectPostProcessor() {
        final String key = "key-123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(key);
        this.marshallMapAndCheck(this.contextWithProcessor(),
                Maps.of(key, value),
                this.map(key, value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE)));
    }

    private JsonNode map(final String key, final JsonNode value) {
        return JsonNode.array()
                .appendChild(JsonNode.object()
                        .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(key))
                        .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, value));
    }

    // marshallWithType...............................................................................................

    @Test
    public void testMarshallWithTypeBooleanTrue() {
        this.marshallWithTypeAndCheck(true, JsonNode.booleanNode(true));
    }

    @Test
    public void testMarshallWithTypeBooleanFalse() {
        this.marshallWithTypeAndCheck(false, JsonNode.booleanNode(false));
    }

    @Test
    public void testMarshallWithTypeNull() {
        this.marshallWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testMarshallWithTypeNumberByte() {
        final byte value = 123;
        this.marshallWithTypeAndCheck(value,
                this.typeAndValue("byte", JsonNode.number(value)));
    }

    @Test
    public void testMarshallWithTypeNumberShort() {
        final short value = 123;
        this.marshallWithTypeAndCheck(value,
                this.typeAndValue("short", JsonNode.number(value)));
    }

    @Test
    public void testMarshallWithTypeNumberInteger() {
        final int value = 123;
        this.marshallWithTypeAndCheck(value,
                this.typeAndValue("int", JsonNode.number(value)));
    }

    @Test
    public void testMarshallWithTypeNumberLong() {
        final long value = 123;
        this.marshallWithTypeAndCheck(value,
                this.typeAndValue("long", JsonNode.string(String.valueOf(value))));
    }

    @Test
    public void testMarshallWithTypeNumberFloat() {
        final float value = 123.5f;
        this.marshallWithTypeAndCheck(value,
                this.typeAndValue("float", JsonNode.number(value)));
    }

    @Test
    public void testMarshallWithTypeNumberDouble() {
        final double value = 1.25;
        this.marshallWithTypeAndCheck(value, JsonNode.number(value));
    }

    @Test
    public void testMarshallWithTypeObject() {
        final Locale value = Locale.ENGLISH;
        this.marshallWithTypeAndCheck(value,
                this.typeAndValue("locale", JsonNode.string(value.toLanguageTag())));
    }

    @Test
    public void testMarshallWithTypeString() {
        final String value = "abc123";
        this.marshallWithTypeAndCheck(value, JsonNode.string(value));
    }

    @Test
    public void testMarshallWithTypeObjectPostProcessor() {
        final String string = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(string);
        this.marshallWithTypeAndCheck(this.contextWithProcessor(),
                value,
                this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE)));
    }

    // marshallWithTypeList...........................................................................................

    @Test
    public void testMarshallWithTypeListNullList() {
        this.marshallWithTypeListAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testMarshallWithTypeListBooleanTrue() {
        this.marshallWithTypeListAndCheck(Lists.of(true),
                this.list(JsonNode.booleanNode(true)));
    }

    @Test
    public void testMarshallWithTypeListBooleanFalse() {
        this.marshallWithTypeListAndCheck(Lists.of(false),
                this.list(JsonNode.booleanNode(false)));
    }

    @Test
    public void testMarshallWithTypeListNullElement() {
        this.marshallWithTypeListAndCheck(Lists.of((Object)null),
                this.list(JsonNode.nullNode()));
    }

    @Test
    public void testMarshallWithTypeListNumberByte() {
        final byte value = 1;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("byte", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeListNumberShort() {
        final short value = 1;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("short", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeListNumberInteger() {
        final int value = 123;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("int", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeListNumberLong() {
        final long value = 123;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("long", JsonNode.string(String.valueOf(value)))));
    }

    @Test
    public void testMarshallWithTypeListNumberFloat() {
        final float value = 1.25f;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("float", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeListNumberDouble() {
        final double value = 1.25;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(JsonNode.number(value)));
    }

    @Test
    public void testMarshallWithTypeListObject() {
        final Locale value = Locale.ENGLISH;
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(this.typeAndValue("locale", JsonNode.string(value.toLanguageTag()))));
    }

    @Test
    public void testMarshallWithTypeListString() {
        final String value = "abc123";
        this.marshallWithTypeListAndCheck(Lists.of(value),
                this.list(JsonNode.string(value)));
    }

    @Test
    public void testMarshallWithTypeListWithObjectPostProcessor() {
        final String string = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(string);

        this.marshallWithTypeListAndCheck(this.contextWithProcessor(),
                Lists.of(value),
                this.list(this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE))));
    }

    // marshallWithTypeSet............................................................................................

    @Test
    public void testMarshallWithTypeSetNullSet() {
        this.marshallWithTypeSetAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testMarshallWithTypeSetBooleanTrue() {
        this.marshallWithTypeSetAndCheck(Sets.of(true),
                this.set(JsonNode.booleanNode(true)));
    }

    @Test
    public void testMarshallWithTypeSetBooleanFalse() {
        this.marshallWithTypeSetAndCheck(Sets.of(false),
                this.set(JsonNode.booleanNode(false)));
    }

    @Test
    public void testMarshallWithTypeSetNullElement() {
        this.marshallWithTypeSetAndCheck(Sets.of((Object)null),
                this.set(JsonNode.nullNode()));
    }

    @Test
    public void testMarshallWithTypeSetNumberByte() {
        final byte value = 1;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("byte", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeSetNumberShort() {
        final short value = 1;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("short", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeSetNumberInteger() {
        final int value = 123;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("int", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeSetNumberLong() {
        final long value = 123;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("long", JsonNode.string(String.valueOf(value)))));
    }

    @Test
    public void testMarshallWithTypeSetNumberFloat() {
        final float value = 1.25f;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("float", JsonNode.number(value))));
    }

    @Test
    public void testMarshallWithTypeSetNumberDouble() {
        final double value = 1.25;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(JsonNode.number(value)));
    }

    @Test
    public void testMarshallWithTypeSetObject() {
        final Locale value = Locale.ENGLISH;
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(this.typeAndValue("locale", JsonNode.string(value.toLanguageTag()))));
    }

    @Test
    public void testMarshallWithTypeSetString() {
        final String value = "abc123";
        this.marshallWithTypeSetAndCheck(Sets.of(value),
                this.set(JsonNode.string(value)));
    }

    @Test
    public void testMarshallWithTypeSetWithObjectPostProcessor() {
        final String string = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(string);

        this.marshallWithTypeSetAndCheck(this.contextWithProcessor(),
                Sets.of(value),
                this.set(this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE))));
    }

    // marshallWithTypeMap...........................................................................................

    @Test
    public void testMarshallWithTypeMapNullMap() {
        this.marshallWithTypeMapAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testMarshallWithTypeMapBooleanTrue() {
        this.marshallWithTypeMapAndCheck3(JsonNode.booleanNode(true),
                true);
    }

    @Test
    public void testMarshallWithTypeMapBooleanFalse() {
        this.marshallWithTypeMapAndCheck3(JsonNode.booleanNode(false),
                false);
    }

    @Test
    public void testMarshallWithTypeMapNullValue() {
        this.marshallWithTypeMapAndCheck3(JsonNode.nullNode(),
                null);
    }

    @Test
    public void testMarshallWithTypeMapNumber() {
        final double value = 1.25;
        this.marshallWithTypeMapAndCheck3(JsonNode.number(value),
                value);
    }

    @Test
    public void testMarshallWithTypeMapString() {
        final String value = "abc123";
        this.marshallWithTypeMapAndCheck3(JsonNode.string(value),
                value);
    }

    private <VV> void marshallWithTypeMapAndCheck3(final JsonNode value,
                                                     final VV javaValue) {
        final String KEY = "key1";

        this.marshallWithTypeMapAndCheck(Maps.of(KEY, javaValue),
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(KEY))
                                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, value)));
    }

    @Test
    public void testMarshallWithTypeMapWithObjectPostProcessor() {
        final String key = "abc123";
        final TestJsonNodeValue value = TestJsonNodeValue.with(key);

        this.marshallWithTypeMapAndCheck(this.contextWithProcessor(),
                Maps.of(key, value),
                JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, JsonNode.string(key))
                                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, this.typeAndValue(TestJsonNodeValue.TYPE_NAME, value.marshall(JsonNodeMarshallContexts.fake()).set(POST, POST_VALUE)))));
    }

    // JsonNodeMarshallContext................................................................................................

    @Override
    public BasicJsonNodeMarshallContext createContext() {
        return BasicJsonNodeMarshallContext.INSTANCE;
    }

    private JsonNodeMarshallContext contextWithProcessor() {
        return this.createContext().setObjectPostProcessor(this::objectPostProcessor);
    }

    private JsonObjectNode objectPostProcessor(final Object value, JsonObjectNode object) {
        return object.set(POST, POST_VALUE);
    }

    private final static JsonNodeName POST = JsonNodeName.with("post");
    private final static JsonNode POST_VALUE = JsonNode.booleanNode(true);

    @Override
    public Class<BasicJsonNodeMarshallContext> type() {
        return BasicJsonNodeMarshallContext.class;
    }
}
