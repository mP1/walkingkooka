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
import walkingkooka.naming.Name;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A node in the route graph that involves a test condition.
 */
final class RouterBuilderRouterPredicate<T> extends RouterBuilderRouter<T> {

    static <T> RouterBuilderRouterPredicate<T> with(final Name name, final Predicate<Object> value, final RouterBuilderRouter<T> next) {
        return new RouterBuilderRouterPredicate<T>(name, value, next);
    }

    private RouterBuilderRouterPredicate(final Name name, final Predicate<Object> value, final RouterBuilderRouter<T> next) {
        this.name = name;
        this.value = value;
        this.next = next;
    }

    @Override
    RouterBuilderRouter<T> add(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        // if the condition is the same, try adding to the next.
        final Predicate<Object> possible = nameToCondition.get(this.name);
        return this.value.equals(possible) ?
                this.addNext(target, nameToCondition) :
                this.addChoice(target, nameToCondition);
    }

    private RouterBuilderRouter<T> addNext(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        nameToCondition.remove(this.name);
        return this.next.add(target, nameToCondition);
    }

    private RouterBuilderRouter<T> addChoice(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        return RouterBuilderRouterChoices.with(Lists.of(this, this.expand(target, nameToCondition)));
    }

    @Override
    RouterBuilderRouter<T> build() {
        return this;
    }

    @Override
    Optional<T> route0(final Map<Name, Object> parameters) {
        return this.value.test(parameters.get(this.name)) ?
                this.next.route(parameters) :
                this.noRoute();
    }

    private final Name name;

    private final Predicate<Object> value;

    /**
     * The next step to try. The step may the only route target itself or more conditional tests.
     */
    private final RouterBuilderRouter<T> next;

    @Override
    void toString0(final boolean separatorRequired, final StringBuilder b) {
        if (separatorRequired) {
            b.append(" & ");
        }
        b.append(this.name)
                .append('=').
                append(this.value);
        this.next.toString0(true, b);
    }
}
