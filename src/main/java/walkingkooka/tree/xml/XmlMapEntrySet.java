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

package walkingkooka.tree.xml;

import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.set.Sets;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A readonly {@link java.util.Set} view of a {@link XmlMap}
 */
final class XmlMapEntrySet<K, V> extends AbstractSet<Entry<K, V>> {

    static {
        Sets.registerImmutableType(XmlMapEntrySet.class);
    }

    static <K, V> XmlMapEntrySet<K, V> with(final XmlMap<K, V> map) {
        return new XmlMapEntrySet<K, V>(map);
    }

    private XmlMapEntrySet(final XmlMap<K, V> map) {
        this.map = map;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return Iterators.array(this.map.entries());
    }

    @Override
    public int size() {
        return this.map.size();
    }

    private final XmlMap<K, V> map;
}
