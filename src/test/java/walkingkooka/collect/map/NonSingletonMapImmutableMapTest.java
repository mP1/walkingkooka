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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NonSingletonMapImmutableMapTest extends ImmutableMapTestCase3<NonSingletonMapImmutableMap<String, Integer>> {

    @Test
    public void testNotCopied() {
        final Map<String, Integer> ordered = Maps.ordered();
        ordered.put(KEY1, VALUE1);
        ordered.put(KEY2, VALUE2);
        final NonSingletonMapImmutableMap immutable = NonSingletonMapImmutableMap.with(ordered);

        ordered.clear();
        assertEquals(ordered, immutable);

        this.getAndCheckAbsent(immutable, KEY1);
    }

    @Override
    NonSingletonMapImmutableMap<String, Integer> createMap(final String key0,
                                                           final Integer value0,
                                                           final String key1,
                                                           final Integer value1) {
        final Map<String, Integer> ordered = Maps.ordered();
        ordered.put(key0, value0);
        ordered.put(key1, value1);
        return NonSingletonMapImmutableMap.with(ordered);
    }

    @Override
    public Class<NonSingletonMapImmutableMap<String, Integer>> type() {
        return Cast.to(NonSingletonMapImmutableMap.class);
    }
}
