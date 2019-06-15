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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SetTesting;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextStyleMapEntrySetTest implements SetTesting<TextStyleMapEntrySet, Entry<TextStylePropertyName<?>, Object>>,
        IteratorTesting {

    @Test
    public void testWithInvalidPropertyFails() {
        assertThrows(TextStylePropertyValueException.class, () -> {
            TextStyleMapEntrySet.with(Maps.of(TextStylePropertyName.WORD_BREAK, null));
        });
    }

    @Test
    public void testEmpty() {
        assertSame(TextStyleMapEntrySet.EMPTY, TextStyleMapEntrySet.with(Maps.empty()));
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createSet(), 2);
    }

    @Test
    public void testAddFails() {
        this.addFails(this.createSet(), Maps.entry(this.property1(), this.value1()));
    }

    @Test
    public void testIteratorRemoveFails() {
        final Iterator<?> iterator = this.createSet().iterator();
        iterator.next();

        assertThrows(UnsupportedOperationException.class, () -> {
            iterator.remove();
        });
    }

    @Test
    public void testToString() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.sorted();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());

        this.toStringAndCheck(this.createSet(), map.entrySet().toString());
    }

    @Override
    public TextStyleMapEntrySet createSet() {
        final Map<TextStylePropertyName<?>, Object> map = Maps.ordered();
        map.put(this.property1(), this.value1());
        map.put(this.property2(), this.value2());
        return TextStyleMapEntrySet.with(map);
    }

    private TextStylePropertyName<?> property1() {
        return TextStylePropertyName.WORD_WRAP;
    }

    private Object value1() {
        return WordWrap.BREAK_WORD;
    }

    private TextStylePropertyName<?> property2() {
        return TextStylePropertyName.FONT_FAMILY_NAME;
    }

    private Object value2() {
        return FontFamilyName.with("Times News Roman");
    }

    @Override
    public Class<TextStyleMapEntrySet> type() {
        return TextStyleMapEntrySet.class;
    }
}
