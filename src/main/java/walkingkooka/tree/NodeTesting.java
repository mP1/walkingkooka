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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.naming.PathSeparator;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.select.NodeSelectorTesting;
import walkingkooka.tree.visit.VisitableTesting;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A mixin interface that contains tests and helpers to assist testing of {@link Node} implementations..
 */
public interface NodeTesting<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
        extends
        VisitableTesting<N>,
        HashCodeEqualsDefinedTesting<N>,
        NodeSelectorTesting<N, NAME, ANAME, AVALUE>,
        ToStringTesting<N>,
        TypeNameTesting<N> {

    @Test
    default void testPathSeparatorConstant() throws Exception {
        final Field field = this.type().getField("PATH_SEPARATOR");

        assertEquals(MemberVisibility.PUBLIC, MemberVisibility.get(field), () -> "PATH_SEPARATOR constant must be public=" + field);
        assertEquals(true, FieldAttributes.STATIC.is(field), () -> "PATH_SEPARATOR constant must be static=" + field);
        assertEquals(PathSeparator.class, field.getType(), () -> "PATH_SEPARATOR constant type=" + field);
    }

    @Test
    default void testNameCached() {
        final N node = this.createNode();
        this.checkCached(node, "name", node.name(), node.name());
    }

    @Test
    default void testParentCached() {
        final N node = this.createNode();
        this.checkCached(node, "parent", node.parent(), node.parent());
    }

    @Test
    default void testParentWithoutRoot() {
        this.parentWithoutAndCheck(this.createNode().root());
    }

    @Test
    default void testParentWithoutChild() {
        final N parent = this.createNode();
        final List<N> children = parent.children();
        assertNotEquals(Lists.empty(), children, "expected at least 1 child");

        this.parentWithoutAndCheck(children.get(0), parent.removeChild(0));
        this.checkWithoutParent(this.createNode());
    }

    @Test
    default void testRootWithoutParent() {
        final N node = this.createNode();
        assertEquals(Optional.empty(), node.parent(), "node must have no parent");
        assertSame(node, node.root());
    }

    @Test
    default void testChildrenIndices() {
        this.childrenCheck(this.createNode());
    }

    @Test
    default void testSetChildIndexInvalidFails() {
        final N node = this.createNode();

        assertThrows(IllegalArgumentException.class, () -> {
            node.setChild(-1, node);
        });
    }

    @Test
    default void testSetChildIndexNullNodeFails() {
        final N node = this.createNode();

        assertThrows(NullPointerException.class, () -> {
            node.setChild(0, null);
        });
    }

    @Test
    default void testFirstChild() {
        final N node = this.createNode();
        final List<N> children = node.children();
        final Optional<N> first = node.firstChild();
        if (children.isEmpty()) {
            assertEquals(Optional.empty(), first, "childless node must not have a first child.");
        } else {
            assertEquals(Optional.of(children.get(0)), first, "node with children must have a first child.");
        }
    }

    @Test
    default void testLastChild() {
        final N node = this.createNode();
        final List<N> children = node.children();
        final Optional<N> last = node.lastChild();
        if (children.isEmpty()) {
            assertEquals(Optional.empty(), last, "childless node must not have a last child.");
        } else {
            assertEquals(Optional.of(children.get(children.size() - 1)), last, "node with children must have a last child.");
        }
    }

    @Test
    default void testPropertiesNeverReturnNull() throws Exception {
        BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(this.createNode(), Predicates.never());
    }

    N createNode();

    @Override
    default N createObject() {
        return this.createNode();
    }

    default <T> void checkCached(final N node,
                                 final String property,
                                 final T value,
                                 final T value2) {
        assertSame(value, value2, () -> node + " did not cache " + property);
    }

    default void childrenCheck(final Node<?, ?, ?, ?> node) {
        final Optional<Node> nodeAsParent = Optional.of(node);

        int i = 0;
        for (Node<?, ?, ?, ?> child : node.children()) {
            assertEquals(i, child.index(), () -> "Incorrect index of " + child);
            final int j = i;
            assertEquals(nodeAsParent, child.parent(), () -> "Incorrect parent of child " + j + "=" + child);

            this.childrenCheck(child);
            i++;
        }
    }

    default void checkWithoutParent(final N node) {
        assertEquals(Optional.empty(), node.parent(), "parent");
        assertEquals(true, node.isRoot(), "root");
        assertEquals(Optional.empty(), node.parentWithout(), () -> "parent without " + node);
    }

    default void checkWithParent(final N node) {
        assertNotEquals(Optional.empty(), node.parent(), "parent");
        assertEquals(false, node.isRoot(), "root");
    }

    @Test
    default void testSetSameAttributes() {
        final N node = this.createNode();
        assertSame(node, node.setAttributes(node.attributes()));
    }

    default void parentCheck(final N node, final N parent) {
        assertSame(parent, node.parent(), () -> "parent of " + node);
    }

    default void parentWithoutAndCheck(final N node) {
        assertEquals(Optional.empty(), node.parentWithout(), () -> "node parentWithout " + node);
    }

    default void parentWithoutAndCheck(final N node, final N parentWithout) {
        assertEquals(Optional.of(parentWithout), node.parentWithout(), () -> "node parentWithout " + node);
    }

    default N appendChildAndCheck(final N parent, final N child) {
        final N newParent = parent.appendChild(child);
        assertNotSame(newParent, parent, "appendChild must not return the same node");

        final List<N> children = newParent.children();
        assertNotEquals(0, children.size(), "children must have at least 1 child");
        assertEquals(child.name(), children.get(children.size() - 1).name(), "last child must be the added child");

        this.childrenParentCheck(newParent);

        this.checkWithoutParent(child);

        return newParent;
    }

    default N removeChildAndCheck(final N parent, final N child) {
        final N newParent = parent.removeChild(child.index());
        assertNotSame(newParent, parent, "removeChild must not return the same node");

        final List<N> oldChildren = parent.children();
        final List<N> newChildren = newParent.children();
        assertEquals(oldChildren.size(), 1 + newChildren.size(), "new children must have 1 less child than old");

        this.childrenParentCheck(newParent);

        return newParent;
    }

    default N setChildrenAndCheck(final N parent, final N... children) {
        final N newParent = parent.setChildren(Arrays.asList(children));

        this.childrenParentCheck(newParent);
        this.childCountCheck(newParent, children);

        return newParent;
    }

    default void childrenParentCheck(final N parent) {
        int i = 0;
        for (N child : parent.children()) {
            final int j = i;
            assertSame(parent, child.parent().get(), () -> "parent of child[" + i + "]=" + child);
        }
    }

    default void childCountCheck(final N parent, final N... children) {
        this.childCountCheck(parent, children.length);
    }

    default void childCountCheck(final N parent, final int count) {
        assertEquals(parent.children().size(), count, "children of parent");
        this.childrenParentCheck(parent);
    }

    // TypeNameTesting............................................................................................

    @Override
    default String typeNameSuffix() {
        return Node.class.getSimpleName();
    }
}
