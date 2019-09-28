/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.search;

import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for all {@link SearchNode} that hold more chidren.
 */
abstract class SearchParentNode extends SearchNode {

    /**
     * Package private to limit sub classing.
     */
    SearchParentNode(final int index, final SearchNodeName name, final List<SearchNode> children) {
        super(index, name);

        final Optional<SearchNode> p = Optional.of(this);

        final List<SearchNode> copy = Lists.array();
        this.children = copy;

        int i = 0;
        for (SearchNode child : children) {
            copy.add(child.setParent(p, i));
            i++;
        }
        this.replaceChildrenCheck(copy);
    }

    @Override
    final SearchParentNode removeParent1() {
        return this.replace0(NO_INDEX, this.defaultName(), this.copyChildren(this.children));
    }

    @Override
    public final List<SearchNode> children() {
        return this.children;
    }

    final List<SearchNode> children;

    final SearchNode setChildren0(final List<SearchNode> children) {
        Objects.requireNonNull(children, "children");

        final List<SearchNode> copy = this.copyChildren(children);
        return Lists.equals(this.children(), copy, (first, other) -> first.equalsIgnoringParentAndChildren(other) && first.equalsDescendants0(other)) ?
                this :
                this.replaceChildren(copy);
    }

    /**
     * Each sub class while copying may also perform other operations on each child,
     * eg {@link SearchSelectNode} also unwraps already selected children.
     */
    abstract List<SearchNode> copyChildren(final List<SearchNode> children);

    @Override
    final SearchNode setChild(final SearchNode newChild, final int index) {
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

        final int index = this.index();
        return this.replace0(index, this.name, children)
                .replaceChild(this.parent(), index)
                .cast();
    }

    abstract void replaceChildrenCheck(final List<SearchNode> children);

    @Override
    final SearchNode replace(final int index, final SearchNodeName name) {
        return this.replace0(index, name, this.children());
    }

    abstract SearchParentNode replace0(final int index, final SearchNodeName name, final List<SearchNode> children);

    final void acceptValues(final SearchNodeVisitor visitor) {
        for (SearchNode node : this.children()) {
            visitor.accept(node);
        }
    }

    @Override
    public final int hashCode() {
        return this.children().hashCode();
    }

    @Override
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

    /**
     * If a non default name is present, just add the name followed by the formatted value
     * which typically includes something like surrounding braces, brackets etc.
     */
    @Override
    final String toStringNameSuffix() {
        return "";
    }

    @Override
    final void toString1(final StringBuilder b) {
        b.append(this.toStringPrefix());

        String separator = "";
        for (SearchNode child : this.children()) {
            b.append(separator);
            child.toString0(b);
            separator = ", ";
        }

        this.toStringSuffix(b);
    }

    abstract String toStringPrefix();

    abstract void toStringSuffix(final StringBuilder b);

}
