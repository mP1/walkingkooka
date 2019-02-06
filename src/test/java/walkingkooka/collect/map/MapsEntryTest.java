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
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MapsEntryTest extends EntryTestCase<MapsEntry<String, Integer>, String, Integer>
        implements HashCodeEqualsDefinedTesting<MapsEntry<String, Integer>> {

    private final static String KEY = "Key123";
    private final static Integer VALUE = 123;

    @Test
    public void testWithNullKeyFails() {
        assertThrows(NullPointerException.class, () -> {
            MapsEntry.with(null, VALUE);
        });
    }

    @Test
    public void testWith() {
        this.getKeyAndValueAndCheck(MapsEntry.with(KEY, VALUE), KEY, VALUE);
    }

    @Test
    public void testWithNullValue() {
        this.getKeyAndValueAndCheck(MapsEntry.with(KEY, null), KEY, null);
    }

    @Test
    public void testSetValueFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createEntry().setValue(null);
        });
    }

    @Test
    public void testDifferentKey() {
        this.checkNotEquals(MapsEntry.with("different", VALUE));
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(MapsEntry.with(KEY, 999));
    }

    @Override
    protected MapsEntry<String, Integer> createEntry() {
        return MapsEntry.with(KEY, VALUE);
    }

    @Override
    public Class<MapsEntry<String, Integer>> type() {
        return Cast.to(MapsEntry.class);
    }

    @Override
    public MapsEntry<String, Integer> createObject() {
        return this.createEntry();
    }
}
