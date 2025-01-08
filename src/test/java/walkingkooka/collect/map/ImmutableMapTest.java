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
import walkingkooka.Cast;

import java.util.Map;

public final class ImmutableMapTest extends ImmutableMapTestCase<ImmutableMap<String, Integer>> {

    @Test
    public void testImmutableHashMap() {
        this.immutableAndCheck(Maps.hash(), false);
    }

    @Test
    public void testIsImmutableTreeMap() {
        this.immutableAndCheck(Maps.ordered(), false);
    }

    @Test
    public void testIsImmutableEmpty() {
        this.immutableAndCheck(Maps.empty(), true);
    }

    @Test
    public void testIsImmutableSingletonImmutable() {
        this.immutableAndCheck(ImmutableMapSingleton.with(Maps.entry(KEY1, VALUE1)), true);
    }

    @Test
    public void testIsImmutableArrayImmutable() {
        this.immutableAndCheck(ImmutableMap.array(Maps.entry(KEY1, VALUE1)), true);
    }

    @Test
    public void testIsImmutableMapImmutable() {
        final Map<String, Integer> map = Maps.hash();
        map.put(KEY1, VALUE1);

        this.immutableAndCheck(ImmutableMap.map(map), true);
    }

    private void immutableAndCheck(final Map<String, Integer> map, final boolean expected) {
        this.checkEquals(expected,
            ImmutableMap.isImmutable(map),
            () -> "immutable " + map);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ImmutableMap<String, Integer>> type() {
        return Cast.to(ImmutableMap.class);
    }
}
