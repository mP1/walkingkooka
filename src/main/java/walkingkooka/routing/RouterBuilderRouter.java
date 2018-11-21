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

import walkingkooka.naming.Name;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The base class for all types or router created by {@link RouterBuilderRouter}.
 */
abstract class RouterBuilderRouter<T> implements Router<T>{

    /**
     * Package private to limit sub classing.
     */
    RouterBuilderRouter() {
        super();
    }

    /**
     * Used by {@link RouterBuilder} to add a new route. This is not intended to be public.
     */
    abstract RouterBuilderRouter<T> add(final T target, final Map<Name, Predicate<Object>> nameToCondition);

    /**
     * Accepts a route and creates individual {@link RouterBuilderRouter} steps for each of its outstanding parameter to condition.
     */
    final RouterBuilderRouter<T> expand(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        return nameToCondition.isEmpty() ?
                RouterBuilderRouterTerminal.with(target) :
                this.expand0(target, nameToCondition);
    }

    private RouterBuilderRouter<T> expand0(final T target, final Map<Name, Predicate<Object>> nameToCondition) {
        final Iterator<Entry<Name, Predicate<Object>>> i = nameToCondition.entrySet().iterator();
        final Entry<Name, Predicate<Object>> first = i.next();
        i.remove();

        return RouterBuilderRouterPredicate.with(first.getKey(),
                first.getValue(),
                this.expand(target, nameToCondition));
    }

    /**
     * Called by {@link RouterBuilder} during its build, allowing {@link RouterBuilderRouterNull#build()} to complain.
     */
    abstract RouterBuilderRouter<T> build();

    /**
     * Accepts a map of parameters which are used to test each route until one is matched otherwise {@link Optional#empty()}
     * is returned.
     */
    @Override
    public final Optional<T> route(final Map<Name, Object> parameters) throws RouteException {
        Objects.requireNonNull(parameters, "parameters");
        return this.route0(parameters);
    }

    abstract Optional<T> route0(final Map<Name, Object> parameters) throws RouteException;

    final Optional<T> noRoute() {
        return Optional.empty();
    }

    @Override
    final public String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(false, b);
        return b.toString();
    }

    abstract void toString0(final boolean separatorRequired,
                            final StringBuilder b);
}
