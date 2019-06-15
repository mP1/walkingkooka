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

import org.w3c.dom.NamedNodeMap;
import walkingkooka.Cast;

import java.util.AbstractMap;
import java.util.Set;

/**
 * Read only typed map view of a {@link NamedNodeMap}.
 */
abstract class XmlMap<K, V> extends AbstractMap<K, V> {

    /**
     * Helper that returns true if a {@link NamedNodeMap} has absolutely no entries.
     */
    static boolean isEmpty(final NamedNodeMap map) {
        return null == map || map.getLength() == 0;
    }

    XmlMap(final NamedNodeMap named) {
        this.named = named;
    }

    @Override
    public final V get(final Object key) {
        return this.isKey(key) ? get0(Cast.to(key)) : null;
    }

    abstract boolean isKey(final Object key);

    /**
     * Given a key of the correct type searched the {@link NamedNodeMap} linearly until a match is found.
     */
    private V get0(final K name) {
        V value = null;
        for (Entry<K, V> possible : this.entries()) {
            if (possible.getKey().equals(name)) {
                value = possible.getValue();
                break;
            }
        }
        return value;
    }

    @Override
    public final Set<Entry<K, V>> entrySet() {
        if (null == this.entrySet) {
            this.entrySet = XmlMapEntrySet.with(this);
        }
        return this.entrySet;
    }

    @Override
    public final int size() {
        return this.named.getLength();
    }

    private XmlMapEntrySet<K, V> entrySet;

    private final NamedNodeMap named;

    /**
     * Factory that lazily creates an array of entries from the items in the {@link NamedNodeMap}
     */
    final Entry<K, V>[] entries() {
        if (null == this.entries) {
            final int count = this.named.getLength();
            final Entry<K, V>[] entries = new Entry[count];

            for (int i = 0; i < count; i++) {
                entries[i] = entry(this.named.item(i));
            }
            this.entries = entries;
        }
        return this.entries;
    }

    private Entry<K, V>[] entries;

    /**
     * Given a {@link org.w3c.dom.Node} creates an entry.
     */
    abstract Entry<K, V> entry(org.w3c.dom.Node node);
}
