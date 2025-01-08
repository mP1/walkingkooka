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

import walkingkooka.Cast;

import java.util.Map.Entry;

public final class ImmutableMapNonSingletonArrayEntrySetTest extends ImmutableMapEntrySetTestCase2<ImmutableMapNonSingletonArrayEntrySet<String, Integer>> {
    @Override
    ImmutableMapNonSingletonArrayEntrySet<String, Integer> createSet(final String key0,
                                                                     final Integer value0,
                                                                     final String key1,
                                                                     final Integer value1) {
        @SuppressWarnings("unchecked") final Entry<String, Integer>[] array = new Entry[2];
        array[0] = Maps.entry(KEY1, VALUE1);
        array[1] = Maps.entry(KEY2, VALUE2);

        return ImmutableMapNonSingletonArrayEntrySet.with(array);
    }

    @Override
    public Class<ImmutableMapNonSingletonArrayEntrySet<String, Integer>> type() {
        return Cast.to(ImmutableMapNonSingletonArrayEntrySet.class);
    }
}
