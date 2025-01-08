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
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Map.Entry;

/**
 * Mixing interface that provides methods to test a {@link Entry}
 */
public interface EntryTesting<E extends Entry<K, V>, K, V> extends ToStringTesting<E>,
    TypeNameTesting<E> {

    @Test
    default void testToString() {
        final E entry = this.createEntry();
        this.toStringAndCheck(entry, entry.getKey() + "=" + entry.getValue());
    }

    E createEntry();

    default void getKeyAndValueAndCheck(final Entry<K, V> entry, final K key, final V value) {
        this.getKeyAndCheck(entry, key);
        this.getValueAndCheck(entry, value);
    }

    default void getKeyAndCheck(final Entry<K, V> entry, final K key) {
        this.checkEquals(key, entry.getKey(), "key from " + entry);
    }

    default void getValueAndCheck(final Entry<K, V> entry, final V value) {
        this.checkEquals(value, entry.getValue(), "Value from " + entry);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return Entry.class.getSimpleName();
    }
}
