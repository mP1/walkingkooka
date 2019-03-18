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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A container for a fork, where several routes have some common parameters but diverge with different values.
 */
final class RouterBuilderRouterChoices<K, T> extends RouterBuilderRouter<K, T> {

    static <K, T> RouterBuilderRouterChoices<K, T> with(final List<RouterBuilderRouter<K, T>> choices) {
        return new RouterBuilderRouterChoices<>(choices);
    }

    private RouterBuilderRouterChoices(final List<RouterBuilderRouter<K, T>> choices) {
        this.choices = choices;
    }

    @Override
    RouterBuilderRouter<K, T> add(final T target, final Map<K, Predicate<Object>> keyToCondition) {
        // take a copy of the existing choices, add the new and create a new RouterBuilderRouterChoices
        List<RouterBuilderRouter<K, T>> copy = Lists.array();
        copy.addAll(this.choices);
        copy.add(this.expand(target, keyToCondition));

        return new RouterBuilderRouterChoices<>(copy);
    }

    @Override
    RouterBuilderRouter<K, T> build() {
        return this;
    }

    @Override
    Optional<T> route0(final Map<K, Object> parameters) {
        Objects.requireNonNull(parameters, "parameters");

        Optional<T> result = Optional.empty();

        for (RouterBuilderRouter<K, T> step : this.choices) {
            result = step.route(parameters);
            if (result.isPresent()) {
                break;
            }
        }

        return result;
    }

    private final List<RouterBuilderRouter<K, T>> choices;

    @Override
    void toString0(final boolean separatorRequired, final StringBuilder b) {
        String choiceSeparator = "";
        for (RouterBuilderRouter<K, T> choice : this.choices) {
            b.append(choiceSeparator);

            b.append('(');
            choice.toString0(false, b);
            b.append(')');

            choiceSeparator = " | ";
        }
    }
}
