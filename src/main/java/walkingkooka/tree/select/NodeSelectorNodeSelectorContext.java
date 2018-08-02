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

import java.util.Set;
import java.util.function.Consumer;

class NodeSelectorNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NodeSelectorContext<N, NAME, ANAME, AVALUE> {

    NodeSelectorNodeSelectorContext(final Consumer<N> observer, final Set<N> matched) {
        this.observer = observer;
        this.matched = matched;
    }

    Consumer<N> observer() {
        return this.observer;
    }

    private final Consumer<N> observer;

    @Override
    void match(final N node) {
        this.matched.add(node);
    }

    private final Set<N> matched;

    public String toString() {
        return this.matched.toString();
    }
}
