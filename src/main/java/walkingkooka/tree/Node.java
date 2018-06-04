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

import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasName;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;

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
        extends HasName<NAME>, HashCodeEqualsDefined {

    /**
     * Returns the name of this node, or null if one is not present.
     */
    NAME name();

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
     * Returns the parent {@Link Node}.
     */
    Optional<N> parent();

    /**
     * Sets or replaces the parent of this node.
     * If the new parent is the same as the original, the original Node is returned.
     */
    N setParent(Optional<N> parent);

    default N removeParent() {
        return this.setParent(Optional.empty());
    }

    default N setParent(final N parent) {
        return this.setParent(Optional.of(parent));
    }

    /**
     * Returns true if this node is the root.
     */
    default boolean isRoot() {
        return !this.parent().isPresent();
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
        if (!parent.get().equals(this)) {
            throw new IllegalArgumentException("Old child has different parent=" + oldChild);
        }

        // replace
        final List<N> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.set(oldChild.index(), newChild);

        return this.setChildren(newChildren);
    }

    /**
     * Removes an existing child.
     */
    default N removeChild(final N child) {
        Objects.requireNonNull(child, "child");

        final Optional<N> parent = child.parent();
        if (!parent.isPresent()) {
            throw new IllegalArgumentException("Child has no parent=" + child);
        }
        if (!parent.get().equals(this)) {
            throw new IllegalArgumentException("Child has different parent=" + child);
        }

        // replace
        final List<N> newChildren = Lists.array();
        newChildren.addAll(this.children());
        newChildren.remove(child.index());

        return this.setChildren(newChildren);
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
     * Creates a new node.
     */
    N create(NAME name);

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
}
