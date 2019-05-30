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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.MapTesting2;
import walkingkooka.collect.map.Maps;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextPropertiesMapTest implements MapTesting2<TextPropertiesMap, TextPropertyName<?>, Object> {

    @Test
    public void testWithInvalidPropertyFails() {
        assertThrows(TextPropertyValueException.class, () -> {
            TextPropertiesMap.with(Maps.of(TextPropertyName.WORD_BREAK, null));
        });
    }


    @Test
    public void testWithMapCopied() {
        final Map<TextPropertyName<?>, Object> from = Maps.sorted();
        from.put(this.property1(), this.value1());
        from.put(this.property2(), this.value2());

        final TextPropertiesMap map = TextPropertiesMap.with(from);

        from.clear();
        this.sizeAndCheck(map, 2);
    }

    @Test
    public void testGet() {
        this.getAndCheck(this.property1(), this.value1());
    }

    @Test
    public void testGetUnknown() {
        this.getAndCheckAbsent(TextPropertyName.DIRECTION);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createMap(), 2);
    }

    @Test
    public void testPutFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createMap().put(this.property1(), this.value1());
        });
    }

    @Test
    public void testKeySet() {
        final List<TextPropertyName<?>> keys = Lists.array();

        for (TextPropertyName<?> key : this.createMap().keySet()) {
            keys.add(key);
        }

        assertEquals(Lists.of(this.property2(), this.property1()), keys);
    }

    @Test
    public void testToString() {
        final Map<TextPropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(this.createMap(), map.toString());
    }

    @Override
    public TextPropertiesMap createMap() {
        final Map<TextPropertyName<?>, Object> map = Maps.ordered();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        return TextPropertiesMap.with(map);
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

    @Override
    public Class<TextPropertiesMap> type() {
        return TextPropertiesMap.class;
    }
}