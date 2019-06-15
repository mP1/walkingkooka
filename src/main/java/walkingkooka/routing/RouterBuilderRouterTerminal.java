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

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A component in the chain that always returns the target assuming ALL elements of the {@link Routing} have been satisfied.
 */
final class RouterBuilderRouterTerminal<K, T> extends RouterBuilderRouter<K, T> {

    static <K, T> RouterBuilderRouterTerminal<K, T> with(final T target) {
        return new RouterBuilderRouterTerminal<>(target);
    }

    private RouterBuilderRouterTerminal(final T target) {
        this.target = Optional.of(target);
    }

    @Override
    RouterBuilderRouter<K, T> add(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        throw new UnsupportedOperationException();
    }

    @Override
    RouterBuilderRouter<K, T> build() {
        return this;
    }

    @Override
    Optional<T> route0(final Map<K, Object> parameters) {
        return this.target;
    }

    private final Optional<T> target;

    @Override
    void toString0(final boolean separatorRequired, final StringBuilder b) {
        if (separatorRequired) {
            b.append(' ');
        }
        b.append("->")
                .append(this.target.get());
    }
}
