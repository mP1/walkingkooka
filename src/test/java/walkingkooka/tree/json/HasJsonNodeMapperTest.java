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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HasJsonNodeMapperTest extends HasJsonNodeMapperTestCase<HasJsonNodeMapper<Void>, Void> {

    // register..............................................................................

    @Test
    public void testRegisterNullTypeNameFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.register(null, JsonNode::fromJsonNode, Object.class);
        });
    }

    @Test
    public void testRegisterEmptyTypeNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HasJsonNodeMapper.register("", JsonNode::fromJsonNode, Object.class);
        });
    }

    @Test
    public void testRegisterNullFactoryFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.register("!", null, Object.class);
        });
    }

    @Test
    public void testRegisterNullClassesFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapper.register("!", JsonNode::fromJsonNode, null);
        });
    }

    @Test
    public void testRegisterTwiceFails() {
        final Class<?> type = this.getClass();
        final String name = type.getName();

        try {
            HasJsonNodeMapper.register(name, JsonNode::fromJsonNode, type);
            assertThrows(IllegalArgumentException.class, () -> {
                HasJsonNodeMapper.register(name, JsonNode::fromJsonNode, type);
            });
        } finally {
            HasJsonNodeMapper.TYPENAME_TO_FACTORY.remove(name);
        }
    }

    @Test
    public void testMapperOrFailStringUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
           HasJsonNodeMapper.mapperOrFail("???");
        });
    }

    @Test
    public void testMapperOrFailClassUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            HasJsonNodeMapper.mapperOrFail(this.getClass());
        });
    }

    @Test
    public void testToJsonNodeWithTypeNull() {
        this.toJsonNodeWithTypeAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testToJsonNodeWithTypeBooleanTrue() {
        this.toJsonNodeWithTypeAndCheck(Boolean.TRUE, JsonNode.booleanNode(true));
    }

    @Test
    public void testToJsonNodeWithTypeBooleanFalse() {
        this.toJsonNodeWithTypeAndCheck(Boolean.FALSE, JsonNode.booleanNode(false));
    }

    @Test
    public void testToJsonNodeWithTypeByte() {
        this.toJsonNodeWithTypeAndCheck(Byte.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeWithTypeShort() {
        this.toJsonNodeWithTypeAndCheck(Short.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeWithTypeInteger() {
        this.toJsonNodeWithTypeAndCheck(Integer.MAX_VALUE);
    }

    @Test
    public void testToJsonNodeWithTypeLong() {
        this.toJsonNodeWithTypeAndCheck(Long.MAX_VALUE, this.typeNameAndValue(Long.MAX_VALUE));
    }

    @Test
    public void testToJsonNodeWithTypeFloat() {
        this.toJsonNodeWithTypeAndCheck(123.5f);
    }

    @Test
    public void testToJsonNodeWithTypeDouble() {
        this.toJsonNodeWithTypeAndCheck(123.5,JsonNode.number(123.5));
    }

    private void toJsonNodeWithTypeAndCheck(final Number value) {
        this.toJsonNodeWithTypeAndCheck(value,
                this.typeNameAndValue(value.getClass(),
                        JsonNode.number(value.doubleValue())));
    }

    @Test
    public void testToJsonNodeWithTypeString() {
        this.toJsonNodeWithTypeAndCheck("abc", JsonNode.string("abc"));
    }

    @Test
    public void testToJsonNodeWithTypeList() {
        final List<Object> list = Lists.of(true, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, "abc");
        this.toJsonNodeWithTypeAndCheck(list,
                this.typeNameAndValue("list",
                        HasJsonNodeMapper.toJsonNodeWithTypeList(list)));
    }

    @Test
    public void testToJsonNodeWithTypeListHasJsonNode() {
        final List<Object> list = Lists.of(Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(list,
                this.typeNameAndValue("list",
                        HasJsonNodeMapper.toJsonNodeWithTypeList(list)));
    }

    @Test
    public void testToJsonNodeWithTypeSet() {
        final Set<Object> set = Sets.of(true, Byte.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE, "abc");
        this.toJsonNodeWithTypeAndCheck(set,
                this.typeNameAndValue("set",
                        HasJsonNodeMapper.toJsonNodeWithTypeSet(set)));
    }

    @Test
    public void testToJsonNodeWithTypeSetHasJsonNode() {
        final Set<Object> set = Sets.of(Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(set,
                this.typeNameAndValue("set",
                        HasJsonNodeMapper.toJsonNodeWithTypeSet(set)));
    }

    @Test
    public void testToJsonNodeWithTypeMap() {
        final Map<Object, Object> map = Maps.ordered();
        map.put("boolean-key", Boolean.TRUE);
        map.put("byte-key", Byte.MAX_VALUE);
        map.put("short-key", Short.MAX_VALUE);
        map.put("integer-key", Integer.MAX_VALUE);
        map.put("long-key", Long.MAX_VALUE);
        map.put("float-key", Float.MAX_VALUE);
        map.put("double-key", Double.MAX_VALUE);

        this.toJsonNodeWithTypeAndCheck(map,
                this.typeNameAndValue("map",
                        HasJsonNodeMapper.toJsonNodeWithTypeMap(map)));
    }

    @Test
    public void testToJsonNodeWithTypeMapHasJsonNode() {
        final Map<Object, Object> map = Maps.one("key", Color.fromRgb(0x123));
        this.toJsonNodeWithTypeAndCheck(map,
                this.typeNameAndValue("map",
                        HasJsonNodeMapper.toJsonNodeWithTypeMap(map)));
    }

    private void toJsonNodeWithTypeAndCheck(final Object value, final JsonNode expected) {
        assertEquals(expected,
                HasJsonNodeMapper.toJsonNodeWithTypeObject(value),
                "value " + CharSequences.quoteIfChars(value) + " toJsonNodeWithType failed");
    }
    private JsonNode typeNameAndValue(final long value) {
        return this.typeNameAndValue("long", JsonNode.string( Long.toString(value)));
    }

    private JsonNode typeNameAndValue(final Class<?> type, final JsonNode value) {
        return this.typeNameAndValue(Integer.class == type ? "int" :
                        Boolean.class == type | Number.class.isAssignableFrom(type) | String.class == type ?
                                type.getSimpleName().toLowerCase() :
                                JsonNode.class.isAssignableFrom(type) ?
                                        type.getSimpleName().substring(0, type.getSimpleName().length() - 4) :
                                        type.getName(),
                value);
    }

    private JsonNode typeNameAndValue(final String typeName, final JsonNode value) {
        return JsonNode.object()
                .set(JsonObjectNode.TYPE, JsonNode.string(typeName))
                .set(JsonObjectNode.VALUE, value);
    }

    // toJsonNodeWithType fromJsonNodeWithType Rountrip .........................................................................................

    @Test
    public void testRoundtripNull() {
        this.roundtripAndCheck(null);
    }

    @Test
    public void testRoundtripBooleanTrue() {
        this.roundtripAndCheck(Boolean.TRUE);
    }

    @Test
    public void testRoundtripBooleanFalse() {
        this.roundtripAndCheck(Boolean.FALSE);
    }

    @Test
    public void testRoundtripNumber() {
        this.roundtripAndCheck(123.5);
    }

    @Test
    public void testRoundtripString() {
        this.roundtripAndCheck("abc123");
    }

    @Test
    public void testRoundtripHasJsonNode() {
        this.roundtripAndCheck(Color.fromRgb(0x123));
    }

    @Test
    public void testRoundtripListPrimitive() {
        this.roundtripAndCheck(Lists.of("abc123"));
    }

    @Test
    public void testRoundtripList() {
        this.roundtripAndCheck(Lists.of(Color.fromRgb(0x123)));
    }

    @Test
    public void testRoundtripSetPrimitive() {
        this.roundtripAndCheck(Sets.of("abc123"));
    }

    @Test
    public void testRoundtripSet() {
        this.roundtripAndCheck(Sets.of(Color.fromRgb(0x123)));
    }

    @Test
    public void testRoundtripMapPrimitive() {
        final Map<Object, Object> map = Maps.ordered();
        map.put("boolean-key", Boolean.TRUE);
        map.put("byte-key", Byte.MAX_VALUE);
        map.put("short-key", Short.MAX_VALUE);
        map.put("integer-key", Integer.MAX_VALUE);
        map.put("long-key", Long.MAX_VALUE);
        map.put("float-key", Float.MAX_VALUE);
        map.put("double-key", Double.MAX_VALUE);

        this.roundtripAndCheck(map);
    }

    @Test
    public void testRoundtripMapHasJsonNode() {
        this.roundtripAndCheck(Maps.one(Color.fromRgb(0x1), Color.fromRgb(0x2)));
    }

    private void roundtripAndCheck(final Object value) {
        assertEquals(value,
                HasJsonNodeMapper.fromJsonNodeWithType(HasJsonNodeMapper.toJsonNodeWithTypeObject(value)),
                () -> "roundtrip " + value);
    }

    @Override
    public Class<HasJsonNodeMapper<Void>> type() {
        return Cast.to(HasJsonNodeMapper.class);
    }
}