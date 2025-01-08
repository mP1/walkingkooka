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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.collect.set.SetTesting2;

import java.util.Map;
import java.util.Map.Entry;

public abstract class ImmutableMapEntrySetTestCase2<S extends ImmutableMapEntrySet<Entry<String, Integer>>> extends ImmutableMapEntrySetTestCase<S>
    implements SetTesting2<S, Entry<String, Integer>>,
    IteratorTesting,
    ToStringTesting<S> {

    ImmutableMapEntrySetTestCase2() {
        super();
    }

    @Test
    public final void testIterate() {
        //noinspection unchecked
        this.iterateAndCheck(this.createSet().iterator(),
            Maps.entry(KEY1, VALUE1),
            Maps.entry(KEY2, VALUE2));
    }

    @Test
    public final void testIsEmptyNot() {
        this.isEmptyAndCheck(this.createSet(), false);
    }

    @Test
    public final void testToString() {
        final Map<String, Integer> map = Maps.ordered();
        map.put(KEY1, VALUE1);
        map.put(KEY2, VALUE2);

        this.toStringAndCheck(this.createSet(), map.entrySet().toString());
    }

    @Test
    public final S createSet() {
        return this.createSet(KEY1, VALUE1, KEY2, VALUE2);
    }

    abstract S createSet(final String key0,
                         final Integer value0,
                         final String key1,
                         final Integer value1);
}
