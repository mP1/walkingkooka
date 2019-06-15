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

package walkingkooka.tree;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * A mixin interface that contains tests and helpers to assist testing of {@link Traversable} implementations..
 */
public interface TraversableTesting<T extends Traversable<T> & HashCodeEqualsDefined>
        extends
        HashCodeEqualsDefinedTesting<T>,
        ToStringTesting<T>,
        TypeNameTesting<T> {

    @Test
    default void testParentCached() {
        final T node = this.createTraversable();
        this.checkCached(node, "parent", node.parent(), node.parent());
    }

    @Test
    default void testParentWithoutChild() {
        final T parent = this.createTraversable();
        final List<T> children = parent.children();
        assertNotEquals(Lists.empty(), children, "expected at least 1 child");

        this.checkWithoutParent(this.createTraversable());
    }

    @Test
    default void testRootWithoutParent() {
        final T node = this.createTraversable();
        assertEquals(Optional.empty(), node.parent(), "node must have no parent");
        assertSame(node, node.root());
    }

    @Test
    default void testChildrenIndices() {
        this.childrenCheck(this.createTraversable());
    }

    @Test
    default void testFirstChild() {
        final T node = this.createTraversable();
        final List<T> children = node.children();
        final Optional<T> first = node.firstChild();
        if (children.isEmpty()) {
            assertEquals(Optional.empty(), first, "childless node must not have a first child.");
        } else {
            assertEquals(Optional.of(children.get(0)), first, "node with children must have a first child.");
        }
    }

    @Test
    default void testLastChild() {
        final T node = this.createTraversable();
        final List<T> children = node.children();
        final Optional<T> last = node.lastChild();
        if (children.isEmpty()) {
            assertEquals(Optional.empty(), last, "childless node must not have a last child.");
        } else {
            assertEquals(Optional.of(children.get(children.size() - 1)), last, "node with children must have a last child.");
        }
    }

    @Test
    default void testPropertiesNeverReturnNull() throws Exception {
        BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(this.createTraversable(),
                (m) -> m.getName().equals("parentOrFail"));
    }

    T createTraversable();

    @Override
    default T createObject() {
        return this.createTraversable();
    }

    default <TT> void checkCached(final T traversable,
                                  final String property,
                                  final TT value,
                                  final TT value2) {
        assertSame(value, value2, () -> traversable + " did not cache " + property);
    }

    default void checkWithoutParent(final T traversable) {
        assertEquals(Optional.empty(), traversable.parent(), "parent");
        assertEquals(true, traversable.isRoot(), "root");
    }

    default void checkWithParent(final T traversable) {
        assertNotEquals(Optional.empty(), traversable.parent(), "parent");
        assertEquals(false, traversable.isRoot(), "root");
    }

    default void childrenCheck(final Traversable<?> parent) {
        final Optional<Traversable<?>> nodeAsParent = Optional.of(parent);

        int i = 0;
        for (Traversable<?> child : parent.children()) {
            assertEquals(i, child.index(), () -> "Incorrect index of " + child);
            final int j = i;
            assertEquals(nodeAsParent, child.parent(), () -> "Incorrect parent of child " + j + "=" + child);

            this.childrenCheck(child);
            i++;
        }
    }

    default void childrenParentCheck(final T parent) {
        int i = 0;
        for (T child : parent.children()) {
            final int j = i;
            assertSame(parent, child.parentOrFail(), () -> "parent of child[" + i + "]=" + child);
        }
    }

    default void childCountCheck(final T parent, final T... children) {
        this.childCountCheck(parent, children.length);
    }

    default void childCountCheck(final T parent, final int count) {
        assertEquals(parent.children().size(), count, "children of parent");
        this.childrenParentCheck(parent);
    }
}
