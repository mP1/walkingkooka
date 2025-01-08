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

import java.util.Map.Entry;

public final class ImmutableMapNonSingletonArrayTest extends ImmutableMapTestCase3<ImmutableMapNonSingletonArray<String, Integer>> {

    @Test
    public void testNotCopied() {
        @SuppressWarnings("unchecked") final Entry<String, Integer>[] array = new Entry[2];
        array[0] = Maps.entry(KEY1, VALUE1);
        array[1] = Maps.entry(KEY2, VALUE2);
        final ImmutableMapNonSingletonArray<String, Integer> immutable = ImmutableMapNonSingletonArray.with(array);

        array[1] = Maps.entry(KEY2, 666);

        this.getAndCheck(immutable, KEY1, VALUE1);
        this.getAndCheck(immutable, KEY2, 666);
        this.sizeAndCheck(immutable, 2);
    }

    @Test
    public void testGetNullValue() {
        final ImmutableMapNonSingletonArray<String, Integer> map = this.createMap(KEY1, null, KEY2, VALUE2);
        this.getAndCheck(map, KEY1, null);
        this.getAndCheck(map, KEY2, VALUE2);
    }

    @Override
    ImmutableMapNonSingletonArray<String, Integer> createMap(final String key0,
                                                             final Integer value0,
                                                             final String key1,
                                                             final Integer value1) {
        //noinspection unchecked
        return ImmutableMapNonSingletonArray.with(Maps.entry(key0, value0), Maps.entry(key1, value1));
    }

    @Override
    public Class<ImmutableMapNonSingletonArray<String, Integer>> type() {
        return Cast.to(ImmutableMapNonSingletonArray.class);
    }
}
