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

import walkingkooka.collect.list.Lists;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A node in the route graph that involves a test condition.
 */
final class RouterBuilderRouterPredicate<K, T> extends RouterBuilderRouter<K, T> {

    static <K, T> RouterBuilderRouterPredicate<K, T> with(final K key, final Predicate<Object> value, final RouterBuilderRouter<K, T> next) {
        return new RouterBuilderRouterPredicate<K, T>(key, value, next);
    }

    private RouterBuilderRouterPredicate(final K key, final Predicate<Object> value, final RouterBuilderRouter<K, T> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    RouterBuilderRouter<K, T> add(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        // if the condition is the same, try adding to the next.
        final Predicate<Object> possible = keyToCondition.get(this.key);
        return this.value.equals(possible) ?
                this.addNext(target, keyToCondition) :
                this.addChoice(target, keyToCondition);
    }

    private RouterBuilderRouter<K, T> addNext(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        keyToCondition.remove(this.key);
        return this.next.add(target, keyToCondition);
    }

    private RouterBuilderRouter<K, T> addChoice(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        return RouterBuilderRouterChoices.with(Lists.of(this, this.expand(target, keyToCondition)));
    }

    @Override
    RouterBuilderRouter<K, T> build() {
        return this;
    }

    @Override
    Optional<T> route0(final Map<K, Object> parameters) {
        return this.value.test(parameters.get(this.key)) ?
                this.next.route(parameters) :
                this.noRoute();
    }

    private final K key;

    private final Predicate<Object> value;

    /**
     * The next step to try. The step may the only route target itself or more conditional tests.
     */
    private final RouterBuilderRouter<K, T> next;

    @Override
    void toString0(final boolean separatorRequired, final StringBuilder b) {
        if (separatorRequired) {
            b.append(" & ");
        }
        b.append(this.key)
                .append('=').
                append(this.value);
        this.next.toString0(true, b);
    }
}
