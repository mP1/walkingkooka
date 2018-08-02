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

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that may be passed to {@link NodeSelector#accept(Node, Consumer)}
 */
final class NodeSelectorNulObserverConsumer<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements Consumer<N> {

    /**
     * Type safe getter.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelectorNulObserverConsumer<N, NAME, ANAME, AVALUE> get() {
        return Cast.to(INSTANCE);
    }

    final static NodeSelectorNulObserverConsumer INSTANCE = new NodeSelectorNulObserverConsumer();

    private NodeSelectorNulObserverConsumer(){
        super();
    }

    @Override
    public void accept(final N n) {

    }

    public String toString() {
        return "";
    }
}
