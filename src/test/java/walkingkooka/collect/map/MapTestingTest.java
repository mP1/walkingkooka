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

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MapTestingTest implements MapTesting<Map<String, Integer>, String, Integer> {

    private final static String KEY1 = "KEY1";
    private final static String KEY2 = "KEY2";
    private final static Integer VALUE1 = 1;
    private final static Integer VALUE2 = 22;

    @Test
    public void testContainsKey() {
        final Map<String, Integer> map = Maps.ordered();

        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);

        this.containsKeyAndCheck(map, KEY1);
    }

    @Test
    public void testContainsValue() {
        final Map<String, Integer> map = Maps.ordered();

        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);

        this.containsValueAndCheck(map, VALUE1);
    }

    @Test
    public void testContainsValueAbsent() {
        final Map<String, Integer> map = Maps.ordered();

        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);

        this.containsValueAndCheckAbsent(map, 999);
    }

    @Test
    public void testGet() {
        final Map<String, Integer> map = Maps.ordered();

        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);

        this.getAndCheck(map, KEY1, VALUE1);
    }

    @Test
    public void testPutFails() {
        assertThrows(UnsupportedOperationException.class, () -> Collections.emptyMap().put("key", "value"));
    }

    @Test
    public void testRemoveFails() {
        assertThrows(UnsupportedOperationException.class, () -> Collections.singletonMap("key", "value")
            .remove("key"));
    }

    @Override
    public Map<String, Integer> createMap() {
        return Maps.sorted();
    }
}
