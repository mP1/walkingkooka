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

package walkingkooka.routing;

import walkingkooka.Cast;
import walkingkooka.build.BuilderException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A placeholder for a route with no conditions.
 */
final class RouterBuilderRouterNull<K, T> extends RouterBuilderRouter<K, T> {

    static <K, T> RouterBuilderRouterNull<K, T> get() {
        return Cast.to(INSTANCE);
    }

    private final static RouterBuilderRouterNull INSTANCE = new RouterBuilderRouterNull();

    private RouterBuilderRouterNull() {
        super();
    }

    @Override
    RouterBuilderRouter<K, T> add(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        return keyToCondition.isEmpty() ?
                RouterBuilderRouterTerminal.with(target) :
                this.expand(target, keyToCondition);
    }

    @Override
    RouterBuilderRouter<K, T> build() {
        throw new BuilderException("Builder requires at least 1 Routing");
    }

    @Override
    Optional<T> route0(final Map<K, Object> parameters) {
        return Optional.empty();
    }

    @Override
    void toString0(final boolean separatorRequired, final StringBuilder b) {
        // nop
    }
}
