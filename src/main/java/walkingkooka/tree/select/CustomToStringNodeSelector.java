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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A {@link NodeSelector} that wraps another but contains a custom toString passed as a parameter.
 */
final class CustomToStringNodeSelector<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends
        NodeSelector<N, NAME, ANAME, AVALUE> {

    /**
     * Factory that wraps the given {@link NodeSelector} as necessary.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> with(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String toString) {
        Objects.requireNonNull(selector, "selector");
        Objects.requireNonNull(toString, "toString");

        final String actual = selector.toString();
        return toString.equals(actual) ?
                selector :
                new CustomToStringNodeSelector<>(selector, toString);
    }

    /**
     * Package private constructor use type safe getter
     */
    private CustomToStringNodeSelector(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String toString) {
        super();
        this.selector = selector.unwrapIfCustomToStringNodeSelector();

        this.toString = toString;
    }

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return with(this.selector.append(selector), this.toString);
    }

    @Override
    public Set<N> accept(final N node, final Consumer<N> observer) {
        return this.selector.accept(node, observer);
    }

    // @VisibleForTesting
    final NodeSelector<N, NAME, ANAME, AVALUE> selector;

    @Override
    void accept0(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.selector.accept0(node, context);
    }

    @Override
    void match(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        this.selector.match(node, context);
    }

    @Override
    void toString0(final NodeSelectorToStringBuilder b) {
        b.append(this.toString);
    }

    private final String toString;

    @Override
    NodeSelector<N, NAME, ANAME, AVALUE> unwrapIfCustomToStringNodeSelector() {
        return this.selector;
    }
}
