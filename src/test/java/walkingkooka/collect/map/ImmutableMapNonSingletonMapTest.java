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

public final class ImmutableMapNonSingletonMapTest extends ImmutableMapTestCase3<ImmutableMapNonSingletonMap<String, Integer>> {

    @Test
    public void testNotCopied() {
        final Map<String, Integer> ordered = Maps.ordered();
        ordered.put(KEY1, VALUE1);
        ordered.put(KEY2, VALUE2);
        final ImmutableMapNonSingletonMap<String, Integer> immutable = ImmutableMapNonSingletonMap.with(ordered);

        ordered.clear();
        this.checkEquals(ordered, immutable);

        this.getAndCheckAbsent(immutable, KEY1);
    }

    @Override
    ImmutableMapNonSingletonMap<String, Integer> createMap(final String key0,
                                                           final Integer value0,
                                                           final String key1,
                                                           final Integer value1) {
        final Map<String, Integer> ordered = Maps.ordered();
        ordered.put(key0, value0);
        ordered.put(key1, value1);
        return ImmutableMapNonSingletonMap.with(ordered);
    }

    @Override
    public Class<ImmutableMapNonSingletonMap<String, Integer>> type() {
        return Cast.to(ImmutableMapNonSingletonMap.class);
    }
}
