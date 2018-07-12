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
 */

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link NodeSelector} that matches {@link Node nodes} whose attributes match the provided {@link Predicate}.
 */
final class PredicateNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        UnaryRelativeNodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link PredicateNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> PredicateNodeSelector<N, NAME, ANAME, AVALUE> with(final Predicate<N> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        return new PredicateNodeSelector(predicate);
    }

    /**
     * Private constructor use type safe getter
     */
    private PredicateNodeSelector(final Predicate<N> predicate) {
        super();
        this.predicate = predicate;
    }

    /**
     * Private constructor
     */
    private PredicateNodeSelector(final Predicate<N> predicate, final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        super(selector);
        this.predicate = predicate;
    }

    // NodeSelector

    NodeSelector<N, NAME, ANAME, AVALUE> append1(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return new PredicateNodeSelector(this.predicate, selector);
    }

    @Override
    final void accept(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        if(this.predicate.test(node)){
            context.match(node);
        }
    }

    private final Predicate<N> predicate;

    // Object


    @Override int hashCode0(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        return Objects.hash(next, this.predicate);
    }

    @Override boolean canBeEqual(final Object other) {
        return other instanceof PredicateNodeSelector;
    }

    @Override boolean equals1(final UnaryNodeSelector<N, NAME, ANAME, AVALUE> other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final PredicateNodeSelector<?, ?, ?, ?> other) {
        return this.predicate.equals(other.predicate);
    }

    @Override
    void toString0(final StringBuilder b, final String separator){
        b.append(this.predicate.toString());
    }
}
