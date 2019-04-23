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

import java.util.Objects;

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
    N accept1(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return this.selector.accept1(node, context);
    }

    @Override
    N select(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        return this.selector.select(node, context);
    }

    // Object

    @Override
    public int hashCode() {
        return this.selector.hashCode();
    }

    public final boolean equals(final Object other) {
        return this == other || other instanceof CustomToStringNodeSelector && this.equals0(Cast.to(other));
    }

    private boolean equals0(final CustomToStringNodeSelector<?, ?, ?, ?> other) {
        return this.selector.equals(other.selector) &&
                this.toString.equals(other.toString);
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

    // @VisibleForTesting
    final NodeSelector<N, NAME, ANAME, AVALUE> selector;
}
