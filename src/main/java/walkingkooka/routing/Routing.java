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

package walkingkooka.routing;

import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.Predicates;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A route is a collection of parameter tests that when matched provide a target.
 * In a web environment these parameters will include HTTP artifacts such as the scheme,
 * request method, http headers, url and the individual path components. A {@link Map}
 * will contain the well known names of these components and the value. The dispatcher
 * will populate a Map and Routes will also use the same names and the {@link RouterBuilderRouter}
 * will return the target.
 */
public final class Routing<K, T> {

    /**
     * Creates a {@link Routing} with initially no conditions attached to it.
     */
    public static <K, T> Routing<K, T> with(final Class<K> type, final T target) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(target, "target");

        return new Routing<K, T>(target, Maps.empty());
    }

    private Routing(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        this.target = target;
        this.keyToCondition = keyToCondition;
    }

    /**
     * Follows the route if the equality condition is true.
     */
    public Routing<K, T> andValueEquals(final K key, final Object value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        return this.andPredicateTrue(key, Predicates.is(value));
    }

    /**
     * Routing continues if the predicate is true.
     */
    public Routing<K, T> andPredicateTrue(final K key, final Predicate<Object> valueTest) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(valueTest, "valueTest");

        final Map<K, Predicate<Object>> keyToCondition = Maps.ordered();
        keyToCondition.putAll(this.keyToCondition);
        keyToCondition.put(key, valueTest);

        return new Routing<K, T>(this.target, keyToCondition);
    }

    final Map<K, Predicate<Object>> keyToCondition;

    final T target;

    @Override
    public String toString() {
        return this.target.toString();
    }
}
