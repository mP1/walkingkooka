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

import walkingkooka.collect.ImmutableTypeRegistry;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;

/**
 * Base class for numerous {@link Map} views which are considered immutable and read only.
 */
abstract class ImmutableMap<K, V> extends AbstractMap<K, V> implements HashCodeEqualsDefined {

    /**
     * Special case checking for empty {@link Map} created by {@link Collections#emptyMap()}.
     */
    final static ImmutableTypeRegistry TYPES = ImmutableTypeRegistry.with(Map.class)
            .add(Maps.empty().getClass());

    /**
     * Returns true if the {@link Map} is immutable. This may not detect all but tries to attempt a few known to {@link Map}.
     */
    static boolean isImmutable(final Map<?, ?> map) {
        return map instanceof ImmutableMap || TYPES.contains(map.getClass());
    }

    /**
     * {@see NonSingletonArrayImmutableMap}
     */
    static <K, V> Map<K, V> array(final Entry<K, V>... entries) {
        return NonSingletonArrayImmutableMap.with(entries);
    }

    /**
     * {@see NonSingletonMapImmutableMap}
     */
    static <K, V> Map<K, V> map(final Map<K, V> map) {
        return NonSingletonMapImmutableMap.with(map);
    }

    /**
     * {@see SingletonImmutableMap}
     */
    static <K, V> Map<K, V> singleton(final Entry<K, V> entry) {
        return SingletonImmutableMap.with(entry);
    }

    /**
     * Package private to limit sub classing.
     */
    ImmutableMap() {
        super();
    }
}
