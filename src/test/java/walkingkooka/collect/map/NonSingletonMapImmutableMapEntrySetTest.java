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

import walkingkooka.Cast;

import java.util.Map;

public final class NonSingletonMapImmutableMapEntrySetTest extends ImmutableMapEntrySetTestCase2<NonSingletonMapImmutableMapEntrySet<String, Integer>> {
    @Override
    NonSingletonMapImmutableMapEntrySet<String, Integer> createSet(final String key0,
                                                                   final Integer value0,
                                                                   final String key1,
                                                                   final Integer value1) {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);
        return NonSingletonMapImmutableMapEntrySet.with(map);
    }

    @Override
    public Class<NonSingletonMapImmutableMapEntrySet<String, Integer>> type() {
        return Cast.to(NonSingletonMapImmutableMapEntrySet.class);
    }
}
