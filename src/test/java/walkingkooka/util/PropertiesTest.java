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

package walkingkooka.util;

import org.junit.jupiter.api.Test;
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PropertiesTest implements ClassTesting<Properties>,
        HashCodeEqualsDefinedTesting2<Properties>,
        ToStringTesting<Properties>,
        CanBeEmptyTesting<Properties> {

    // get..............................................................................................................

    @Test
    public void testGetNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> Properties.EMPTY.get(null)
        );
    }

    @Test
    public void testGet() {
        final PropertiesPath key = PropertiesPath.parse("key.1");
        final String value = "value1";

        this.getAndCheck(
                new Properties(
                        Maps.of(
                                key,
                                value
                        )
                ),
                key,
                Optional.of(value)
        );
    }

    @Test
    public void testGetUnknown() {
        this.getAndCheck(
                new Properties(
                        Maps.of(
                                PropertiesPath.parse("key.1"),
                                "value1"
                        )
                ),
                PropertiesPath.parse("unknown.key.404"),
                Optional.empty()
        );
    }

    private void getAndCheck(final Properties properties,
                             final PropertiesPath key,
                             final Optional<String> value) {
        this.checkEquals(
                value,
                properties.get(key),
                () -> properties + " " + key
        );
    }

    // set..............................................................................................................

    @Test
    public void testSetNullPathFails() {
        assertThrows(
                NullPointerException.class,
                () -> Properties.EMPTY.set(
                        null,
                        "*value*"
                )
        );
    }

    @Test
    public void testSetNullValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> Properties.EMPTY.set(
                        PropertiesPath.parse("key.123"),
                        null
                )
        );
    }

    @Test
    public void testSetSame() {
        final PropertiesPath key = PropertiesPath.parse("key.123");
        final String value = "*value*123";

        final Properties properties = new Properties(
                Maps.of(
                        key,
                        value
                )
        );

        assertSame(
                properties,
                properties.set(
                        key,
                        value
                )
        );
    }

    @Test
    public void testSetDifferentWhenEmpty() {
        final PropertiesPath key = PropertiesPath.parse("key.123");
        final String value = "*value*123";

        this.setAndCheck(
                Properties.EMPTY,
                key,
                value,
                Maps.of(
                        key,
                        value
                )
        );
    }

    @Test
    public void testSetDifferentWhenNonEmpty() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";

        final PropertiesPath key2 = PropertiesPath.parse("key.222");
        final String value2 = "*value*222";

        this.setAndCheck(
                new Properties(
                        Maps.of(
                                key1,
                                value1
                        )
                ),
                key2,
                value2,
                Maps.of(
                        key1,
                        value1,
                        key2,
                        value2
                )
        );
    }

    private void setAndCheck(final Properties properties,
                             final PropertiesPath key,
                             final String value,
                             final Map<PropertiesPath, String> expected) {
        final Properties set = properties.set(
                key,
                value
        );
        assertNotSame(
                properties,
                set
        );

        this.checkEquals(
                expected,
                set.pathToValue,
                () -> properties + " " + key + " " + value
        );
    }

    // remove...........................................................................................................

    @Test
    public void testRemoveNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> Properties.EMPTY.remove(null)
        );
    }

    @Test
    public void testRemoveWhenEmpty() {
        final PropertiesPath key = PropertiesPath.parse("key.123");

        assertSame(
                Properties.EMPTY.remove(key),
                Properties.EMPTY
        );
    }

    @Test
    public void testRemoveUnknown() {
        final PropertiesPath key = PropertiesPath.parse("key.111");
        final String value = "*value*111";
        final Properties properties = new Properties(
                Maps.of(
                        key,
                        value
                )
        );

        assertSame(
                properties,
                properties.remove(
                        PropertiesPath.parse("unknown.404")
                )
        );
    }

    @Test
    public void testRemoveWhenNonEmpty() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";

        final PropertiesPath key2 = PropertiesPath.parse("key.222");
        final String value2 = "*value*222";

        this.removeAndCheck(
                new Properties(
                        Maps.of(
                                key1,
                                value1,
                                key2,
                                value2
                        )
                ),
                key2,
                Maps.of(
                        key1,
                        value1
                )
        );
    }

    @Test
    public void testRemoveBecomesEmpty() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";


        assertSame(
                new Properties(
                        Maps.of(
                                key1,
                                value1
                        )
                ).remove(key1),
                Properties.EMPTY
        );
    }

    private void removeAndCheck(final Properties properties,
                                final PropertiesPath key,
                                final Map<PropertiesPath, String> expected) {
        final Properties removed = properties.remove(
                key
        );
        assertNotSame(
                properties,
                removed
        );

        this.checkEquals(
                expected,
                removed.pathToValue,
                () -> properties + " " + key
        );
    }

    // all..............................................................................................................

    @Test
    public void testSetSetSet() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";

        final PropertiesPath key2 = PropertiesPath.parse("key.222");
        final String value2 = "*value*222";

        final PropertiesPath key3 = PropertiesPath.parse("key.333");
        final String value3 = "*value*333";

        this.checkEquals(
                new Properties(
                        Maps.of(
                                key1,
                                value1,
                                key2,
                                value2,
                                key3,
                                value3
                        )
                ),
                Properties.EMPTY.set(
                        key1,
                        value1
                ).set(
                        key2,
                        value2
                ).set(
                        key3,
                        value3
                )
        );
    }

    @Test
    public void testSetAndReplace() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";

        final PropertiesPath key2 = PropertiesPath.parse("key.222");
        final String value2 = "*value*222";

        final PropertiesPath key3 = PropertiesPath.parse("key.333");
        final String value3 = "*value*333";

        this.checkEquals(
                new Properties(
                        Maps.of(
                                key1,
                                value1,
                                key2,
                                value2,
                                key3,
                                value3
                        )
                ),
                Properties.EMPTY.set(
                        key1,
                        value1
                ).set(
                        key2,
                        "replaced"
                ).set(
                        key2,
                        value2
                ).set(
                        key3,
                        value3
                )
        );
    }

    @Test
    public void testSetAndRemove() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";

        final PropertiesPath key2 = PropertiesPath.parse("key.222");
        final String value2 = "*value*222";

        final PropertiesPath key3 = PropertiesPath.parse("key.333");
        final String value3 = "*value*333";

        this.checkEquals(
                new Properties(
                        Maps.of(
                                key1,
                                value1,
                                key2,
                                value2,
                                key3,
                                value3
                        )
                ),
                Properties.EMPTY.set(
                                key1,
                                value1
                        ).set(
                                key2,
                                "removed"
                        ).remove(key2)
                        .set(
                                key2,
                                value2
                        ).set(
                                key3,
                                value3
                        )
        );
    }

    // entries..........................................................................................................

    @Test
    public void testEntriesWhenEmpty() {
        this.entriesAndCheck(
                Properties.EMPTY
        );
    }

    @Test
    public void testEntriesWhenNotEmpty() {
        final PropertiesPath key = PropertiesPath.parse("key.111");
        final String value = "*value1*";

        this.entriesAndCheck(
                Properties.EMPTY.set(
                        key,
                        value
                ),
                Maps.entry(
                        key,
                        value
                )
        );
    }

    @Test
    public void testEntriesWhenNotEmpty2() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final PropertiesPath key2 = PropertiesPath.parse("key.222");

        final String value1 = "*value1*";
        final String value2 = "*value2*";

        this.entriesAndCheck(
                Properties.EMPTY.set(
                        key1,
                        value1
                ).set(
                        key2,
                        value2
                ),
                Maps.entry(
                        key1,
                        value1
                ),
                Maps.entry(
                        key2,
                        value2
                )
        );
    }

    private void entriesAndCheck(final Properties properties,
                                 final Entry<PropertiesPath, String>... expected) {
        this.entriesAndCheck(
                properties,
                Sets.of(expected)
        );
    }

    private void entriesAndCheck(final Properties properties,
                                 final Set<Entry<PropertiesPath, String>> expected) {
        final Map<PropertiesPath, String> expectedMap = Maps.sorted();
        for(final Entry<PropertiesPath, String> entry : expected) {
            expectedMap.put(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        final Map<PropertiesPath, String> actualMap = Maps.sorted();
        for(final Entry<PropertiesPath, String> entry : properties.entries()) {
            actualMap.put(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        // cant compare Set<Entry> because Entry.hashCode is not defined

        this.checkEquals(
                expectedMap,
                actualMap,
                () -> properties.toString()
        );
    }

    @Test
    public void testEntriesReadOnly() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> Properties.EMPTY.set(
                                PropertiesPath.parse("key.111"),
                                "value111"
                        ).entries()
                        .clear()
        );
    }
    
    // keys.............................................................................................................

    @Test
    public void testKeysWhenEmpty() {
        this.keysAndCheck(
                Properties.EMPTY
        );
    }

    @Test
    public void testKeysWhenNotEmpty() {
        final PropertiesPath key = PropertiesPath.parse("key.111");

        this.keysAndCheck(
                Properties.EMPTY.set(
                        key,
                        "*value11*"
                ),
                key
        );
    }

    @Test
    public void testKeysWhenNotEmpty2() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final PropertiesPath key2 = PropertiesPath.parse("key.222");

        this.keysAndCheck(
                Properties.EMPTY.set(
                        key1,
                        "*value11*"
                ).set(
                        key2,
                        "*value22*"
                ),
                key1,
                key2
        );
    }

    private void keysAndCheck(final Properties properties,
                              final PropertiesPath... expected) {
        this.keysAndCheck(
                properties,
                Sets.of(expected)
        );
    }

    private void keysAndCheck(final Properties properties,
                              final Set<PropertiesPath> expected) {
        this.checkEquals(
                expected,
                properties.keys(),
                () -> properties.toString()
        );
    }

    @Test
    public void testKeysReadOnly() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> Properties.EMPTY.set(
                                PropertiesPath.parse("key.111"),
                                "value111"
                        ).keys()
                        .clear()
        );
    }

    // values.............................................................................................................

    @Test
    public void testValuesWhenEmpty() {
        this.valuesAndCheck(
                Properties.EMPTY
        );
    }

    @Test
    public void testValuesWhenNotEmpty() {
        final String value = "*value11*";

        this.valuesAndCheck(
                Properties.EMPTY.set(
                        PropertiesPath.parse("key.111"),
                        value
                ),
                value
        );
    }

    @Test
    public void testValuesWhenNotEmpty2() {
        final String value1 = "*value11*";
        final String value2 = "*value22*";

        this.valuesAndCheck(
                Properties.EMPTY.set(
                        PropertiesPath.parse("key.111"),
                        value1
                ).set(
                        PropertiesPath.parse("key.222"),
                        value2
                ),
                value1,
                value2
        );
    }

    private void valuesAndCheck(final Properties properties,
                                final String... expected) {
        this.valuesAndCheck(
                properties,
                Sets.of(expected)
        );
    }

    private void valuesAndCheck(final Properties properties,
                                final Collection<String> expected) {
        this.checkEquals(
                new ArrayList<>(expected),
                new ArrayList<>(
                        properties.values()
                ),
                () -> properties.toString()
        );
    }

    @Test
    public void testValuesReadOnly() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> Properties.EMPTY.set(
                        PropertiesPath.parse("key.111"),
                        "value111"
                ).values()
                        .clear()
        );
    }

    // canBeEmpty.......................................................................................................

    @Test
    public void testIsEmptyWhenEmpty() {
        this.isEmptyAndCheck(
                Properties.EMPTY,
                true
        );
    }

    @Test
    public void testIsEmptyWhenNotEmpty() {
        this.isEmptyAndCheck(
                Properties.EMPTY.set(
                        PropertiesPath.parse("key.111"),
                        "*value*111"
                ),
                false
        );
    }

    @Override
    public Properties createCanBeEmpty() {
        return Properties.EMPTY;
    }

    // size.............................................................................................................

    @Test
    public void testSizeWhenEmpty() {
        this.sizeAndCheck(
                Properties.EMPTY,
                0
        );
    }

    @Test
    public void testSizeWhenNotEmpty() {
        this.sizeAndCheck(
                Properties.EMPTY.set(
                        PropertiesPath.parse("key.111"),
                        "*value11*"
                ),
                1
        );
    }

    private void sizeAndCheck(final Properties properties,
                              final int expected) {
        this.checkEquals(
                expected,
                properties.size(),
                () -> properties.toString()
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
                Properties.EMPTY
                        .set(
                                PropertiesPath.parse("key.111"),
                                "*value*111"
                        ).set(
                                PropertiesPath.parse("key.222"),
                                "*value*222"
                        )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final PropertiesPath key1 = PropertiesPath.parse("key.111");
        final String value1 = "*value*111";

        final PropertiesPath key2 = PropertiesPath.parse("key.222");
        final String value2 = "*value*222";

        final Map<PropertiesPath, String> map = Maps.of(
                key1,
                value1,
                key2,
                value2
        );

        this.toStringAndCheck(
                new Properties(
                        map
                ),
                map.toString()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<Properties> type() {
        return Properties.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting2 ...................................................................................

    @Override
    public Properties createObject() {
        return Properties.EMPTY;
    }
}
