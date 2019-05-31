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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextPropertiesTest implements ClassTesting2<TextProperties>,
        HashCodeEqualsDefinedTesting<TextProperties>,
        HasJsonNodeTesting<TextProperties>,
        ToStringTesting<TextProperties> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            TextProperties.with(null);
        });
    }

    @Test
    public void testWithInvalidPropertyFails() {
        assertThrows(TextPropertyValueException.class, () -> {
            TextProperties.with(Maps.of(TextPropertyName.WORD_BREAK, null));
        });
    }

    @Test
    public void testWithTextPropertiesMap() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        final TextPropertiesMap textPropertiesMap = TextPropertiesMap.with(map);

        final TextProperties textProperties = TextProperties.with(textPropertiesMap);
        assertSame(textPropertiesMap, textProperties.value(), "value");
    }

    @Test
    public void testWithMapCopied() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final Map<TextPropertyName<?>, Object> copy = Maps.sorted();
        copy.putAll(map);

        final TextProperties textProperties = TextProperties.with(map);

        map.clear();
        assertEquals(copy, textProperties.value(), "value");
    }

    @Test
    public void testValue() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        final TextProperties textProperties = TextProperties.with(map);
        assertEquals(TextPropertiesMap.class, textProperties.value().getClass(), () -> "" + textProperties.value);
    }

    @Test
    public void testToString() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(TextProperties.with(map), map.toString());
    }

    @Override
    public TextProperties createObject() {
        final Map<TextPropertyName<?>, Object> map = Maps.ordered();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        return TextProperties.with(map);
    }

    private TextPropertyName<?> property1() {
        return TextPropertyName.WORD_WRAP;
    }

    private Object value1() {
        return WordWrap.BREAK_WORD;
    }

    private TextPropertyName<?> property2() {
        return TextPropertyName.FONT_FAMILY_NAME;
    }

    private Object value2() {
        return FontFamilyName.with("Times News Roman");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextProperties> type() {
        return TextProperties.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // HasJsonNodeTesting................................................................................................

    @Override
    public TextProperties fromJsonNode(final JsonNode from) {
        return TextProperties.fromJsonNode(from);
    }

    @Override
    public TextProperties createHasJsonNode() {
        return this.createObject();
    }
}
