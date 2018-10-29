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

import walkingkooka.Context;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.function.Consumer;

/**
 * The {@link Context} that accompanies all match requests.
 */
abstract class NodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> implements Context {

    /**
     * A {@link Consumer} that observes each and every match attempt. This exists primarily to keep track or count of
     * match operations to prevent denial of service.
     */
    abstract Consumer<N> observer();

    /**
     * Callback that receives each and every matched node by each individual {@link NodeSelector} during matching.
     */
    abstract void match(final N node);
}
