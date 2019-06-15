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
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class MapsTest implements ClassTesting2<Maps>,
        PublicStaticHelperTesting<Maps>,
        IteratorTesting {

    final static String KEY1 = "a1";
    final static Integer VALUE1 = 111;

    final static String KEY2 = "b2";
    final static Integer VALUE2 = 222;

    final static String KEY3 = "c3";
    final static Integer VALUE3 = 33;

    final static String KEY4 = "D4";
    final static Integer VALUE4 = 444;

    final static String KEY5 = "E5";
    final static Integer VALUE5 = 55;


    @Test
    public void testOfKeyValue() {
        this.checkType(Maps.of(KEY1, VALUE1), SingletonImmutableMap.class);
    }

    @Test
    public void testOfKeyValue2() {
        this.checkType(Maps.of(KEY1, VALUE1, KEY2, VALUE2), NonSingletonArrayImmutableMap.class);
    }

    @Test
    public void testOfKeyValue3() {
        this.checkType(Maps.of(KEY1, VALUE1, KEY2, VALUE2, KEY3, VALUE3), NonSingletonArrayImmutableMap.class);
    }

    @Test
    public void testOfKeyValue4() {
        this.checkType(Maps.of(KEY1, VALUE1, KEY2, VALUE2, KEY3, VALUE3, KEY4, VALUE4), NonSingletonArrayImmutableMap.class);
    }

    @Test
    public void testImmutableMapEmpty() {
        final Map<String, Integer> map = Maps.ordered();
        this.checkType(Maps.immutable(map), Collections.emptyMap().getClass());
    }

    @Test
    public void testImmutableMap1() {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        this.checkType(Maps.immutable(map), SingletonImmutableMap.class);
    }

    @Test
    public void testImmutableMap2() {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        this.checkType(Maps.immutable(map), NonSingletonArrayImmutableMap.class);
    }

    @Test
    public void testImmutableMap3() {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        map.put(KEY3, VALUE3);
        this.checkType(Maps.immutable(map), NonSingletonArrayImmutableMap.class);
    }

    @Test
    public void testImmutableMap4() {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        map.put(KEY3, VALUE3);
        map.put(KEY4, VALUE4);
        this.checkType(Maps.immutable(map), NonSingletonArrayImmutableMap.class);
    }

    @Test
    public void testImmutableMap5() {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        map.put(KEY3, VALUE3);
        map.put(KEY4, VALUE4);
        map.put(KEY5, VALUE5);
        this.checkType(Maps.immutable(map), NonSingletonMapImmutableMap.class);
    }

    @Test
    public void testImmutableMapSortedMap() {
        final Map<String, Integer> map = Maps.sorted();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        map.put(KEY3, VALUE3);
        map.put(KEY4, VALUE4);
        map.put(KEY5, VALUE5);

        final Map<String, Integer> immutable = Maps.immutable(map);
        this.checkType(immutable, NonSingletonMapImmutableMap.class);

        this.iterateAndCheck(immutable.keySet().iterator(), KEY4, KEY5, KEY1, KEY2, KEY3);
    }

    @Test
    public void testImmutableMapSortedMapComparator() {
        final Map<String, Integer> map = Maps.sorted(String.CASE_INSENSITIVE_ORDER);
        map.put("a1", VALUE1);
        map.put("B2", VALUE2);
        map.put("c3", VALUE3);
        map.put("D4", VALUE4);
        map.put("e5", VALUE5);

        final Map<String, Integer> immutable = Maps.immutable(map);
        this.checkType(immutable, NonSingletonMapImmutableMap.class);

        this.iterateAndCheck(immutable.keySet().iterator(), "a1", "B2", "c3", "D4", "e5");
    }

    private void checkType(Map<String, Integer> immutable, final Class<?> type) {
        assertEquals(type,
                immutable.getClass(),
                () -> " type of " + immutable);
    }

    @Test
    public void testReadOnlyImmutableMap() {
        final Map<String, Integer> immutable = Maps.of(KEY1, VALUE1);
        assertSame(immutable, Maps.readOnly(immutable));
    }

    @Test
    public void testReadOnly() {
        final Map<String, Integer> map = Maps.hash();
        map.put(KEY1, VALUE1);

        final Map<String, Integer> readonly = Maps.readOnly(map);
        assertNotSame(readonly, map);
        assertEquals(map, readonly);
    }

    @Override
    public Class<Maps> type() {
        return Maps.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
