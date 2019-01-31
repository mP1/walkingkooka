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

import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class EntryTestCase<M extends Entry<K, V>, K, V> extends ClassTestCase<M> {

    protected abstract M createEntry();

    protected void getKeyAndValueAndCheck(final Entry<K, V> entry, final K key, final V value) {
        this.getKeyAndCheck(entry, key);
        this.getValueAndCheck(entry, value);
    }

    protected void getKeyAndCheck(final Entry<K, V> entry, final K key) {
        assertEquals(key, entry.getKey(), "key from " + entry);
    }

    protected void getValueAndCheck(final Entry<K, V> entry, final V value) {
        assertEquals(value, entry.getValue(), "Value from " + entry);
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
