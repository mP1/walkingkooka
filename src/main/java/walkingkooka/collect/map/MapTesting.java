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

package walkingkooka.collect.map;

import org.junit.jupiter.api.Test;
import walkingkooka.test.Testing;
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link Map}.
 */
public interface MapTesting<M extends Map<K, V>, K, V> extends Testing {

    @Test
    default void testIteratorContainsKeyAndSize() {
        int size = 0;
        final M map = this.createMap();

        for (final Entry<K, V> keyAndValue : map.entrySet()) {
            containsKeyAndCheck(map, keyAndValue.getKey());
            size++;
        }

        sizeAndCheck(map, size);
    }

    @Test
    default void testIteratorAndContainsValueAndSize() {
        int size = 0;
        final M map = this.createMap();

        for (Entry<K, V> keyAndValue : map.entrySet()) {
            containsValueAndCheck(map, keyAndValue.getValue());
            size++;
        }

        sizeAndCheck(map, size);
    }

    @Test
    default void testIteratorAndSize() {
        int size = 0;
        final M map = this.createMap();

        for (Entry<K, V> kvEntry : map.entrySet()) {
            size++;
        }

        this.sizeAndCheck(map, size);
    }

    @Test
    default void testIsEmptyAndSize() {
        final M map = this.createMap();
        final int size = map.size();
        this.isEmptyAndCheck(map, 0 == size);
    }

    M createMap();

    default void containsKeyAndCheck(final K key) {
        this.containsKeyAndCheck(this.createMap(), key);
    }

    default void containsKeyAndCheck(final Map<K, V> map, final K key) {
        this.checkEquals(
            true,
            map.containsKey(key),
            () -> map + " should contain key " + CharSequences.quoteIfChars(key)
        );
    }

    default void containsKeyAndCheckAbsent(final Object key) {
        this.containsKeyAndCheckAbsent(this.createMap(), key);
    }

    default void containsKeyAndCheckAbsent(final Map<K, V> map, final Object key) {
        this.checkEquals(
            false,
            map.containsKey(key),
            () -> map + " should contain key " + CharSequences.quoteIfChars(key)
        );
    }

    default void containsValueAndCheck(final V value) {
        this.containsValueAndCheck(this.createMap(), value);
    }

    default void containsValueAndCheck(final Map<K, V> map, final V value) {
        this.checkEquals(
            true,
            map.containsValue(value),
            () -> map + " should contain value " + CharSequences.quoteIfChars(value)
        );
    }

    default void containsValueAndCheckAbsent(final Map<K, V> map, final V value) {
        this.checkEquals(
            false,
            map.containsValue(value),
            () -> map + " should contain value " + CharSequences.quoteIfChars(value)
        );
    }

    default void getAndCheck(final K key, final V value) {
        this.getAndCheck(this.createMap(), key, value);
    }

    default void getAndCheck(final Map<K, V> map, final K key, final V value) {
        this.checkEquals(
            value,
            map.get(key),
            () -> "get " + CharSequences.quoteIfChars(key) + " from " + map
        );
        this.containsKeyAndCheck(map, key);
        this.containsValueAndCheck(map, value);
    }

    default void getAndCheckAbsent(final Object key) {
        this.getAndCheckAbsent(this.createMap(), key);
    }

    default void getAndCheckAbsent(final Map<K, V> map, final Object key) {
        this.checkEquals(
            null,
            map.get(key),
            () -> "get " + CharSequences.quoteIfChars(key) + " from " + map
        );
        this.containsKeyAndCheckAbsent(map, key);
    }

    default void isEmptyAndCheck(final Map<K, V> map, final boolean empty) {
        this.checkEquals(
            empty,
            map.isEmpty(),
            () -> "isEmpty of " + map
        );
    }

    default void putFails(final Map<K, V> map,
                          final K key,
                          final V value) {
        assertThrows(
            UnsupportedOperationException.class,
            () -> map.put(key, value)
        );
    }

    default void removeFails(final Map<K, V> map,
                             final K key) {
        assertThrows(
            UnsupportedOperationException.class,
            () -> map.remove(key)
        );
    }

    default void sizeAndCheck(final Map<K, V> map, final int size) {
        this.checkEquals(
            size,
            map.size(),
            () -> "size of " + map
        );
        this.isEmptyAndCheck(map, 0 == size);
    }
}
