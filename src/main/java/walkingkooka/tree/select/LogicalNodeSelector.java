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
import walkingkooka.NeverError;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link NodeSelector} that continues combines the results of at least two selectors in with some logical operation.
 */
abstract class LogicalNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NonCustomToStringNodeSelector<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE> Collection<NodeSelector<N, NAME, ANAME, AVALUE>> gatherUniques(
            final List<NodeSelector<N, NAME, ANAME, AVALUE>> selectors) {
        final Set<NodeSelector<N, NAME, ANAME, AVALUE>> uniques = Sets.ordered();
        for (NodeSelector<N, NAME, ANAME, AVALUE> selector : selectors) {
            Objects.requireNonNull(selector, "selectors must not contain null");
            uniques.add(selector);
        }
        return uniques;
    }

    final Collection<NodeSelector<N, NAME, ANAME, AVALUE>> selectors;

    LogicalNodeSelector(final Collection<NodeSelector<N, NAME, ANAME, AVALUE>> selectors) {
        super();
        this.selectors = selectors;
    }

    @Override
    final NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> beginPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context;
    }

    @Override
    NodeSelectorContext2<N, NAME, ANAME, AVALUE> finishPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context;
    }

    @Override
    final N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        throw new NeverError(this.getClass().getName() + ".select(Node, NodeSelectorContext)");
    }

    @Override
    final void toString0(final NodeSelectorToStringBuilder b) {
        String between = "";
        for (NodeSelector<N, NAME, ANAME, AVALUE> selector : this.selectors) {
            b.append(between);
            selector.toString0(b);
            between = this.operatorToString();
        }
    }

    abstract String operatorToString();

    // Object

    @Override
    public final int hashCode() {
        return this.selectors.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final LogicalNodeSelector<?, ?, ?, ?> other) {
        return this.selectors.equals(other.selectors);
    }
}
