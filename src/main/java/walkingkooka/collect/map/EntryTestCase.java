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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class EntryTestCase<E extends Entry<K, V>, K, V> extends ClassTestCase<E>
        implements ToStringTesting<E>,
        TypeNameTesting<E> {

    @Test
    public void testToString() {
        final E entry = this.createEntry();
        this.toStringAndCheck(entry, entry.getKey() + "=" + entry.getValue());
    }

    protected abstract E createEntry();

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

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Entry.class.getSimpleName();
    }
}
