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

public final class ImmutableMapSingletonTest extends ImmutableMapTestCase2<ImmutableMapSingleton<String, Integer>> {

    private final static String KEY = "a1";
    private final static Integer VALUE = 999;

    @Test
    public void testContainsKey() {
        this.containsKeyAndCheck(KEY);
    }

    @Test
    public void testContainsValue() {
        this.containsValueAndCheck(VALUE);
    }

    @Test
    public void testGet() {
        this.getAndCheck(KEY, VALUE);
    }

    @Test
    public void testIterator() {
        //noinspection unchecked
        this.iterateAndCheck(this.createMap().entrySet().iterator(), Maps.entry(KEY, VALUE));
    }

    @Test
    public void testRemoveFails() {
        this.removeFails(this.createMap(), KEY);
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createMap(), 1);
    }

    @Test
    public void testToString() {
        final Map<String, Integer> hash = Maps.hash();
        hash.put(KEY, VALUE);

        this.toStringAndCheck(this.createMap(), hash.toString());
    }

    @Override
    public ImmutableMapSingleton<String, Integer> createMap() {
        return ImmutableMapSingleton.with(Maps.entry(KEY, VALUE));
    }

    @Override
    public Class<ImmutableMapSingleton<String, Integer>> type() {
        return Cast.to(ImmutableMapSingleton.class);
    }
}
