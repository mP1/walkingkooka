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

package walkingkooka.tree;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasName;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.visit.Visitable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A node is part of a tree holding branches and leaves all of which are nodes.
 */
public interface Node<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
        extends HasName<NAME>, Visitable, HashCodeEqualsDefined {

    /**
     * Returns the name of this node, or null if one is not present.
     */
    NAME name();

    /**
     * While all {@link Node node's} have {@link Name names} they might not all be unique amongst siblings.
     */
    default boolean hasUniqueNameAmongstSiblings() {
        return false;
    }

    int ROOT_NODE_INDEX = -1;

    /**
     * If not the root returns the index of this node, or -1 for root.
     */
    default int index() {
        int index = -1;

        final Optional<N> parent = this.parent();
        if (parent.isPresent()) {
            index = parent.get().children().indexOf(this);
            if (-1 == index) {
                throw new NodeException("Child not present in children of parent=" + this);
            }
        }

        return index;
    }

    /**
     * Returns the parent {@link Node}.
     */
    Optional<N> parent();

    /**
     * Returns true if this node is the root.
     */
    default boolean isRoot() {
        return !this.parent().isPresent();
    }

    /**
     * Returns the root node which may or may not be this node.
     * The default walks up the ancestor path until reaching the root.
     */
    default N root() {
        N root = Cast.to(this);

        for(;;) {
            final Optional<N> parent = root.parent();
            if(!parent.isPresent()){
                break;
            }
            root = parent.get();
        }

        return root;
    }

    /**
     * Returns the children of this node.
     */
    List<N> children();

    /**
     * Sets new children.
     */
    N setChildren(final List<N> children);

    /**
     * Appends a new child.
     */
    default N appendChild(final N child) {
        Objects.requireNonNull(child, "child");

        final List<N> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.add(child);
        return this.setChildren(newChildren);
    }

    /**
     * Replaces an existing child with a new one.
     */
    default N replaceChild(final N oldChild, final N newChild) {
        Objects.requireNonNull(oldChild, "oldChild");
        Objects.requireNonNull(newChild, "newChild");

        final Optional<N> parent = oldChild.parent();
        if (!parent.isPresent()) {
            throw new IllegalArgumentException("Old child has no parent=" + oldChild);
        }
        if (parent.get() != this) {
            throw new IllegalArgumentException("Old child has different parent=" + oldChild);
        }

        // replace
        final List<N> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.set(oldChild.index(), newChild);

        return this.setChildren(newChildren);
    }

    /**
     * Removes an existing child using its index.
     */
    default N removeChild(final int index){
        N parentWithout;

        if(this instanceof HasChildrenValues) {
            final HasChildrenValues<Object, N> parentOfChildrenValues = Cast.to(this);

            final List<Object> without = Lists.array();
            without.addAll(parentOfChildrenValues.childrenValues());
            without.remove(index);
            parentWithout = parentOfChildrenValues.setChildrenValues(without);
        } else {
            final List<N> without = Lists.array();
            without.addAll(this.children());
            without.remove(index);
            parentWithout = this.setChildren(without);
        }

        return parentWithout;
    }

    /**
     * Returns the attributes of this node.
     */
    Map<ANAME, AVALUE> attributes();

    /**
     * Sets or replaces the attributes.
     */
    N setAttributes(final Map<ANAME, AVALUE> attributes);

    /**
     * Returns the previous sibling if one exists.
     */
    default Optional<N> previousSibling() {
        Optional<N> result = Optional.empty();

        final Optional<N> parent = this.parent();
        if (parent.isPresent()) {
            final List<N> children = parent.get().children();
            final int index = this.index();
            if (index > 0) {
                result = Optional.of(children.get(index - 1));
            }
        }

        return result;
    }

    /**
     * Returns the next sibling if one exists.
     */
    default Optional<N> nextSibling() {
        Optional<N> result = Optional.empty();

        final Optional<N> parent = this.parent();
        if (parent.isPresent()) {
            final List<N> children = parent.get().children();
            final int index = this.index();
            if (index + 1 < children.size()) {
                result = Optional.of(children.get(index + 1));
            }
        }

        return result;
    }

    /**
     * Returns the first child for the current node.
     */
    default Optional<N> firstChild() {
        final List<N> children = this.children();
        return children.isEmpty() ?
                Optional.empty() :
                Optional.of(children.get(0));
    }

    /**
     * Returns the last child for the current node.
     */
    default Optional<N> lastChild() {
        final List<N> children = this.children();
        return children.isEmpty() ?
                Optional.empty() :
                Optional.of(children.get(children.size() -1));
    }

    /**
     * Returns a select that may be used to locate this {@link Node} starting at the root.
     */
    default NodeSelector<N, NAME, ANAME, AVALUE> selector() {
        return NodeSelector.path(Cast.to(this));
    }

    /**
     * An {@link Iterator} that walks starting at this {@link Node} depth first.
     */
    default Iterator<N> treeIterator() {
        return new NodeTreeIterator<N, NAME, ANAME, AVALUE>(Cast.to(this));
    }

    /**
     * Returns a {@link NodePointer} that uniquely identifies this {@link Node} starting at the root.
     */
    default NodePointer<N, NAME, ANAME, AVALUE> pointer() {
       return Nodes.pointer(Cast.to(this));
    }
}
