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

package walkingkooka.tree.search;

import walkingkooka.Cast;
import walkingkooka.Value;

import java.util.List;
import java.util.Objects;

/**
 * The base {@link SearchNode} for all leaf values that hold a value.
 */
abstract class SearchLeafNode<V> extends SearchNode implements Value<V> {

    static <V> void check(final String text, final V value) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(value, "value");
    }

    SearchLeafNode(final int index, final String text, final V value){
        super(index);
        this.text = text;
        this.value = value;
    }

    public final String text() {
        return this.text;
    }

    private final String text;

    @Override
    public final V value() {
        return this.value;
    }

    private final V value;

    final SearchLeafNode<V> setValue0(final V value) {
        return Objects.equals(this.value(), value) ?
                this :
                this.replaceValue(value);
    }

    final SearchLeafNode<V> replaceValue(final V value) {
        return this.wrap1(this.index, this.text, value)
                .replaceChild(this.parent())
                .cast();
    }

    final SearchLeafNode wrap0(final int index) {
        return this.wrap1(index, this.text, this.value);
    }

    abstract SearchLeafNode wrap1(final int index, final String text, final V value);

    @Override
    final SearchNode wrap(final int index) {
        return this.wrap0(index);
    }

    @Override
    public final boolean isSelect() {
        return false;
    }

    @Override
    public final boolean isSequence() {
        return false;
    }

    @Override
    public final List<SearchNode> children() {
        return NO_CHILDREN;
    }

    public final SearchNode setChildren(final List<SearchNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    @Override
    final SearchNode setChild(final SearchNode newChild) {
        throw new UnsupportedOperationException();
    }

    // Object....................................................................

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

    @Override
    final boolean equalsDescendants0(final SearchNode other) {
        return true;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final SearchNode other) {
        return other instanceof SearchLeafNode && this.equals1(Cast.to(other));
    }

    private boolean equals1(final SearchLeafNode<?> other) {
        return this.text.equals(other.text) &&
               this.value.equals(other.value);
    }
}
