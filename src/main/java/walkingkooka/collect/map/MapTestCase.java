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

package walkingkooka.collect.map;

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class MapTestCase<M extends Map<K, V>, K, V> extends ClassTestCase<M> {

    @Test
    public final void testIteratorContainsKeyAndSize() {
        int size = 0;
        final M map = this.createMap();
        final Iterator<Entry<K, V>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            final Entry<K, V> keyAndValue = iterator.next();
            containsKeyAndCheck(map, keyAndValue.getKey());
            size++;
        }

        sizeAndCheck(map, size);
    }

    @Test
    public final void testIteratorAndContainsValueAndSize() {
        int size = 0;
        final M map = this.createMap();
        final Iterator<Entry<K, V>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            final Entry<K, V> keyAndValue = iterator.next();
            containsValueAndCheck(map, keyAndValue.getValue());
            size++;
        }

        sizeAndCheck(map, size);
    }

    @Test
    public final void testIteratorAndSize() {
        int size = 0;
        final M map = this.createMap();
        final Iterator<Entry<K, V>> iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            iterator.next();
            size++;
        }

        this.sizeAndCheck(map, size);
    }

    @Test
    public final void testIsEmptyAndSize() {
        final M map = this.createMap();
        final int size = map.size();
        this.isEmptyAndCheck(map, 0 == size);
    }

    protected abstract M createMap();

    protected void containsKeyAndCheck(final K key) {
        this.containsKeyAndCheck(this.createMap(), key);
    }

    protected void containsKeyAndCheck(final Map<K, V> map, final K key) {
        assertTrue(map + " should contain key " + CharSequences.quoteIfChars(key),
                map.containsKey(key));
    }

    protected void containsKeyAndCheckAbsent(final Object key) {
        this.containsKeyAndCheckAbsent(this.createMap(), key);
    }

    protected void containsKeyAndCheckAbsent(final Map<K, V> map, final Object key) {
        assertFalse(map + " should contain key " + CharSequences.quoteIfChars(key),
                map.containsKey(key));
    }

    protected void containsValueAndCheck(final V value) {
        this.containsValueAndCheck(this.createMap(), value);
    }

    protected void containsValueAndCheck(final Map<K, V> map, final V value) {
        assertTrue(map + " should contain value " + CharSequences.quoteIfChars(value),
                map.containsValue(value));
    }

    protected void getAndCheck(final K key, final V value) {
        this.getAndCheck(this.createMap(), key, value);
    }

    protected void getAndCheck(final Map<K, V> map, final K key, final V value) {
        assertEquals("get " + CharSequences.quoteIfChars(key) + " from " + map,
                value,
                map.get(key));
        this.containsKeyAndCheck(map, key);
        this.containsValueAndCheck(map, value);
    }

    protected void getAndCheckAbsent(final Object key) {
        this.getAndCheckAbsent(this.createMap(), key);
    }

    protected void getAndCheckAbsent(final Map<K, V> map, final Object key) {
        assertEquals("get " + CharSequences.quoteIfChars(key) + " from " + map,
                null,
                map.get(key));
        this.containsKeyAndCheckAbsent(map, key);
    }

    protected void isEmptyAndCheck(final Map<K, V> map, final boolean empty) {
        assertEquals("isEmpty of " + map, empty, map.isEmpty());
    }

    protected void sizeAndCheck(final Map<K, V> map, final int size) {
        assertEquals("size of " + map, size, map.size());
        this.isEmptyAndCheck(map, 0 == size);
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
