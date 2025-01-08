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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.collect.iterator.IteratorTesting;

import java.util.Map;

public abstract class ImmutableMapTestCase3<M extends ImmutableMap<String, Integer>> extends ImmutableMapTestCase2<M>
    implements HashCodeEqualsDefinedTesting2<M>,
    IteratorTesting {

    ImmutableMapTestCase3() {
        super();
    }

    @Test
    public final void testContainsKey() {
        this.containsKeyAndCheck(this.createMap(), KEY1);
    }

    @Test
    public final void testContainsKey2() {
        this.containsKeyAndCheck(this.createMap(), KEY2);
    }

    @Test
    public final void testContainsValue() {
        this.containsValueAndCheck(this.createMap(), VALUE1);
    }

    @Test
    public final void testContainsValue2() {
        this.containsValueAndCheck(this.createMap(), VALUE2);
    }

    @Test
    public final void testGet() {
        this.getAndCheck(KEY1, VALUE1);
    }

    @Test
    public final void testGet2() {
        this.getAndCheck(KEY2, VALUE2);
    }

    @Test
    public final void testKeys() {
        this.iterateAndCheck(this.createMap().keySet().iterator(), KEY1, KEY2);
    }

    @Test
    public final void testRemoveFails() {
        this.removeFails(this.createMap(), KEY1);
    }

    @Test
    public final void testSize() {
        this.sizeAndCheck(this.createMap(), 2);
    }

    @Test
    public final void testValues() {
        this.iterateAndCheck(this.createMap().values().iterator(), VALUE1, VALUE2);
    }

    @Test
    public final void testEqualsDifferent() {
        this.checkNotEquals(this.createMap(KEY1, 777, KEY2, VALUE2));
    }

    @Test
    public final void testToString() {
        final Map<String, Integer> ordered = Maps.ordered();
        ordered.put(KEY1, VALUE1);
        ordered.put(KEY2, VALUE2);
        this.toStringAndCheck(this.createMap(KEY1, VALUE1, KEY2, VALUE2), ordered.toString());
    }

    @Override
    public final M createMap() {
        return this.createMap(KEY1, VALUE1, KEY2, VALUE2);
    }

    abstract M createMap(final String key0,
                         final Integer value0,
                         final String key1,
                         final Integer value1);

    @Override
    public final M createObject() {
        return this.createMap();
    }
}
