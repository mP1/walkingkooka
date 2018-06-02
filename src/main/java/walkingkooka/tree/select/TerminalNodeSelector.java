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
import walkingkooka.ShouldNeverHappenError;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Set;

/**
 * A {@link NodeSelector} that selects and does nothing.
 */
final class TerminalNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Type safe {@link TerminalNodeSelector} getter
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> TerminalNodeSelector<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(TerminalNodeSelector.INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static TerminalNodeSelector INSTANCE = new TerminalNodeSelector();

    /**
     * Package private constructor use type safe getter
     */
    TerminalNodeSelector() {
        super();
    }

    // NodeSelector

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector){
        return selector;
    }

    @Override public Set<N> accept(N node) {
        throw new ShouldNeverHappenError(this.getClass() + ".accept(Node)");
    }

    @Override final void accept(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        context.match(node);
    }

    @Override void match(N node, NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        throw new ShouldNeverHappenError(this.getClass() + ".match(Node, NodeSelectorContext)");
    }

    @Override
    void toString0(final StringBuilder b, final String separator){
        //nop
    }

    @Override void toStringNext(final StringBuilder b, final String separator) {
        throw new ShouldNeverHappenError(this.getClass() + ".toStringNext(StringBuilder, String)");
    }
}
