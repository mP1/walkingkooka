/*
 *
 *  * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package walkingkooka.tree.select;

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Set;

/**
 * Base class for all non logical (binary) selectors.
 */
abstract class UnaryRelativeNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends UnaryNodeSelector<N, NAME, ANAME, AVALUE> {

    UnaryRelativeNodeSelector() {
        this(TerminalNodeSelector.get());
    }

    UnaryRelativeNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> next) {
        super(next);
    }

    @Override
    public final Set<N> accept(final N node) {
        return this.accept0(node);
    }
}
