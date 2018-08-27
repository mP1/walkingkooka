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

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for all {@link SearchNode} that hold more chidren.
 */
abstract class SearchParentNode extends SearchNode {

    static List<SearchNode> copy(final List<SearchNode> nodes) {
        List<SearchNode> copy = Lists.array();
        copy.addAll(nodes);
        return copy;
    }

    SearchParentNode(final int index, final List<SearchNode> children) {
        super(index);

        final Optional<SearchNode> p = Optional.of(this);

        final List<SearchNode> copy = Lists.array();
        this.children = copy;

        int i = 0;
        for(SearchNode child : children) {
            copy.add(child.setParent(p, i));
            i++;
        }
    }

    public final String text() {
        final StringBuilder b = new StringBuilder();
        for(SearchNode child : this.children()) {
            child.toString0(b);
        }
        return b.toString();
    }

    @Override
    public final List<SearchNode> children() {
        return this.children;
    }

    final List<SearchNode> children;

    final SearchNode setChildren0(final List<SearchNode> children) {
        Objects.requireNonNull(children, "children");

        final List<SearchNode> copy = copy(children);
        return Lists.equals(this.children(), copy, (first, other) -> first.equalsIgnoringParentAndChildren(other) && first.equalsDescendants0(other)) ?
                this :
                this.replaceChildren(copy);
    }

    @Override
    final SearchNode setChild(final SearchNode newChild) {
        final int index = newChild.index();
        final SearchNode previous = this.children().get(index);
        return previous.equalsIgnoringParentAndChildren(newChild) && previous.equalsDescendants(newChild) ?
                this :
                this.replaceChild0(newChild, index);
    }

    private SearchNode replaceChild0(final SearchNode newChild, final int index) {
        final List<SearchNode> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.set(index, newChild);

        return this.replaceChildren(newChildren);
    }

    final SearchParentNode replaceChildren(final List<SearchNode> children) {
        this.replaceChildrenCheck(children);
        return this.wrap0(this.index, children)
                .replaceChild(this.parent())
                .cast();
    }

    abstract void replaceChildrenCheck(final List<SearchNode> children);

    @Override
    final SearchNode wrap(final int index) {
        return this.wrap0(index, this.children());
    }

    abstract SearchParentNode wrap0(final int index, final List<SearchNode> children);

    @Override
    public final boolean isBigDecimal() {
        return false;
    }

    @Override
    public final boolean isBigInteger() {
        return false;
    }

    @Override
    public final boolean isDouble() {
        return false;
    }

    @Override
    public final boolean isLocalDate() {
        return false;
    }

    @Override
    public final boolean isLocalDateTime() {
        return false;
    }

    @Override
    public final boolean isLocalTime() {
        return false;
    }

    @Override
    public final boolean isLong() {
        return false;
    }

    @Override
    public final boolean isText() {
        return false;
    }

    final void acceptValues(final SearchNodeVisitor visitor){
        for(SearchNode node: this.children()){
            visitor.accept(node);
        }
    }

    @Override
    final boolean equalsIgnoringParentAndChildren(final SearchNode other) {
        return this.canBeEqual(other);
    }

    final boolean equalsDescendants0(final SearchNode other) {
        return this.equalsDescendants1(other.children());
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    final boolean equalsDescendants1(final List<SearchNode> otherChildren) {
        final List<SearchNode> children = this.children();
        final int count = children.size();
        boolean equals = count == otherChildren.size();

        if (equals) {
            for (int i = 0; equals && i < count; i++) {
                equals = children.get(i).equalsDescendants(otherChildren.get(i));
            }
        }

        return equals;
    }

    @Override
    final void toString0(final StringBuilder b) {
        String separator = "";
        for(SearchNode child : this.children()) {
            b.append(separator);
            child.toString0(b);
            separator = ", ";
        }
    }
}
