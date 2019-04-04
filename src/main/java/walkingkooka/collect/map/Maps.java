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
 */

package walkingkooka.collect.map;

import walkingkooka.type.PublicStaticHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final public class Maps implements PublicStaticHelper {
    /**
     * {@see ConcurrentHashMap}
     */
    static public <K, V> ConcurrentMap<K, V> concurrent() {
        return new ConcurrentHashMap<K, V>();
    }

    /**
     * {@see Collections#emptyMap()
     */
    static public <K, V> Map<K, V> empty() {
        return Collections.emptyMap();
    }

    /**
     * {@see MapsEntry}
     */
    static public <K,V> Entry<K,V> entry(final K key, final V value) {
        return MapsEntry.with(key, value);
    }

    /**
     * {@see FakeMap}
     */
    static public <K, V> Map<K, V> fake() {
        return FakeMap.create();
    }

    /**
     * {@see HashMap}
     */
    static public <K, V> Map<K, V> hash() {
        return new HashMap<K, V>();
    }

    /**
     * {@see IdentityHashMap}
     */
    static public <K, V> Map<K, V> identity() {
        return new IdentityHashMap<K, V>();
    }

    /**
     * {@see TreeMap}
     */
    static public <K, V> NavigableMap<K, V> navigable() {
        return new TreeMap<K, V>();
    }

    /**
     * {@see TreeMap}
     */
    static public <K, V> NavigableMap<K, V> navigable(final Comparator<? super K> comparator) {
        return new TreeMap<K, V>(comparator);
    }

    /**
     * {@see Collections#singletonMap(Object, Object)
     */
    static public <K, V> Map<K, V> of(final K key, final V value) {
        return Collections.singletonMap(key, value);
    }

    /**
     * {@see LinkedHashMap}
     */
    static public <K, V> Map<K, V> ordered() {
        return new LinkedHashMap<K, V>();
    }

    /**
     * {@see Collections#unmodifiableMap(Map)
     */
    static public <K, V> Map<K, V> readOnly(final Map<K, V> map) {
        return Collections.unmodifiableMap(map);
    }

    /**
     * {@see TreeMap}
     */
    static public <K, V> SortedMap<K, V> sorted() {
        return new TreeMap<K, V>();
    }

    /**
     * {@see TreeMap}
     */
    static public <K, V> SortedMap<K, V> sorted(final Comparator<? super K> comparator) {
        return new TreeMap<K, V>(comparator);
    }

    /**
     * {@see WeakHashMap}
     */
    static public <K, V> Map<K, V> weak() {
        return new WeakHashMap<K, V>();
    }

    /**
     * Stop creation
     */
    private Maps() {
        throw new UnsupportedOperationException();
    }
}
