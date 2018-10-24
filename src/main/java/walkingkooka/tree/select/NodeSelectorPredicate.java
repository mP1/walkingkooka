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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.function.Predicate;

/**
 * A {@link Predicate} that returns true if more than zero nodes are returned for the given select and any nodes given to test.
 */
final class NodeSelectorPredicate<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>  implements Predicate<N> {

    /**
     * Ctor only called by {@link NodeSelector#asPredicate()}
     */
    NodeSelectorPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        this.selector = selector;
    }

    @Override
    public boolean test(final N node) {
        return this.selector.accept(node, this.selector.nulObserver()).size() > 0;
    }

    private final NodeSelector<N, NAME, ANAME, AVALUE> selector;

    @Override
    public String toString() {
        return "[" + this.selector + "]";
    }

}
