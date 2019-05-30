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

import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A read only {@link Set} sorted view of properties that have had their values checked.
 */
final class TextPropertiesMapEntrySet extends AbstractSet<Entry<TextPropertyName<?>, Object>> {

    static TextPropertiesMapEntrySet with(final Map<TextPropertyName<?>, Object> entries) {
        final List<Entry<TextPropertyName<?>, Object>> list = Lists.array();

        for(Entry<TextPropertyName<?>, Object> propertyAndValue : entries.entrySet()) {
            final TextPropertyName<?> property = propertyAndValue.getKey();
            final Object value = propertyAndValue.getValue();
            property.check(value);

            list.add(Maps.entry(property, value));
        }

        list.sort(TextPropertiesMapEntrySet::comparator);
        return new TextPropertiesMapEntrySet(list);
    }

    /**
     * A {@link Comparator} that maybe used to sort all entries so they appear in alphabetical order.
     */
    private static int comparator(final Entry<TextPropertyName<?>, Object> first,
                                  final Entry<TextPropertyName<?>, Object> second) {
        return first.getKey().compareTo(second.getKey());
    }

    private TextPropertiesMapEntrySet(final List<Entry<TextPropertyName<?>, Object>> entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Iterator<Entry<TextPropertyName<?>, Object>> iterator() {
        return Iterators.readOnly(this.entries.iterator());
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    private final List<Entry<TextPropertyName<?>, Object>> entries;
}