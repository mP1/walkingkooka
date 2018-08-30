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
import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A route is a collection of parameter tests that when matched provide a target.
 * In a web environment these parameters will include HTTP artifacts such as the scheme,
 * request method, http headers, url and the individual path components. A {@link Map}
 * will contain the well known names of these components and the value. The dispatcher
 * will populate a Map and Routes will also use the same names and the {@link RouteMaster}
 * will return the target.
 */
public final class Route<T> {

    /**
     * Creates a {@link Route} with initially no conditions attached to it.
     */
    public static <T> Route<T> with(final T target) {
        Objects.requireNonNull(target, "target");

        return new Route<>(target, Maps.empty());
    }

    private Route(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        this.target = target;
        this.nameToCondition = nameToCondition;
    }

    public Route<T> equalsValue(final Name name, final Object value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        return this.predicate(name, Predicates.is(value));
    }

    public Route<T> predicate(final Name name, final Predicate<Object> valueTest) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(valueTest, "valueTest");

        final Map<Name, Predicate<Object>> nameToCondition = Maps.ordered();
        nameToCondition.putAll(this.nameToCondition);
        nameToCondition.put(name, valueTest);

        return new Route(this.target, nameToCondition);
    }

    final Map<Name, Predicate<Object>> nameToCondition;

    final T target;

    @Override
    public String toString() {
        return this.target.toString();
    }
}
