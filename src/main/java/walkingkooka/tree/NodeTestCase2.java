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
import walkingkooka.naming.Name;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class NodeTestCase2<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        NodeTestCase<N, NAME, ANAME, AVALUE> {

    protected NodeTestCase2() {
        super();
    }

    @Test final public void testChildrenCached() {
        final N node = this.createNode();
        this.checkCached(node, "children", node.children(), node.children());
    }

    @Test
    public void testAppendChild() {
        final N parent = this.createNode();
        final int parentChildCount = parent.children().size();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        this.checkChildCount(parent1, parentChildCount + 1);
    }

    @Test
    public void testAppendChild2() {
        final N parent = this.createNode();
        final int parentChildCount = parent.children().size();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N child2 = this.createNode();
        final N parent2 = this.appendChildAndCheck(parent1, child2);

        this.checkChildCount(parent2, parentChildCount + 2);
    }

    @Test
    public void testRemoveChildFirst() {
        this.appendTwoChildrenAndRemove(0);
    }

    @Test
    public void testRemoveChildLast() {
       this.appendTwoChildrenAndRemove(1);
    }

    private void appendTwoChildrenAndRemove(final int remove) {
        final N parent = this.createNode();
        final int parentCount = parent.children().size();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N child2 = this.createNode();
        final N parent2 = this.appendChildAndCheck(parent1, child2);

        final N removed = parent2.children().get(remove);
        final N parent3 = this.removeChildAndCheck(parent2, removed);

        this.checkChildCount(parent3, parentCount + 1 + 1 - 1);
    }

    @Test
    public void testReplaceChildWithoutParent() {
        assertThrows(IllegalArgumentException.class, () -> {
            final N parent = this.createNode();
            final N child = this.createNode();

            parent.replaceChild(child, this.createNode());
        });
    }

    @Test
    public void testReplaceChildDifferentParent() {
        assertThrows(IllegalArgumentException.class, () -> {
            final N parent1 = this.createNode();

            final N parent2 = this.createNode();
            final N childOf2 = parent2.children().get(0);

            parent1.replaceChild(childOf2, this.createNode());
        });
    }

    @Test
    public void testReplaceChild() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N child2 = this.createNode();

        final N set = this.setChildrenAndCheck(parent, child1, child2);

        this.checkChildCount(set, child1, child2);
    }

    @Test
    public void setChildrenWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            final N parent = this.createNode();
            parent.setChildren(null);
        });
    }

    @Test
    public void testSetChildrenSame() {
        final N parentBefore = this.appendChildAndCheck(this.createNode(), this.createNode());

        final N parentAfter = parentBefore.setChildren(parentBefore.children());
        assertSame(parentAfter, parentBefore);
    }

    @Test
    public void testSetDifferentChildren() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N child2 = this.createNode();

        final N parent1 = this.setChildrenAndCheck(parent, child1, child2);

        final N child3 = this.createNode();
        final N parent2 = this.setChildrenAndCheck(parent1, child3);

        this.checkChildCount(parent2, child3);
    }

    @Test final public void testAttributesCached() {
        final N node = this.createNode();
        this.checkCached(node, "attributes", node.attributes(), node.attributes());
    }

    @Test
    public void testSetSameAttributes() {
        final N node = this.createNode();
        assertSame(node, node.setAttributes(node.attributes()));
    }

    private <T> void checkCached(final N node, final String property, final T value, final T value2) {
        assertSame(value, value2, () -> node + " did not cache " + property);
    }

    protected void checkParent(final N node, final N parent) {
        assertSame(parent, node.parent(), () -> "parent of " + node);
    }

    protected N appendChildAndCheck(final N parent, final N child) {
        final N newParent = parent.appendChild(child);
        assertNotSame(newParent, parent, "appendChild must not return the same node");

        final List<N> children = newParent.children();
        assertNotEquals(0, children.size(), "children must have at least 1 child");
        assertEquals(child.name(), children.get(children.size() - 1).name(), "last child must be the added child");

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    protected N removeChildAndCheck(final N parent, final N child) {
        final N newParent = parent.removeChild(child.index());
        assertNotSame(newParent, parent, "removeChild must not return the same node");

        final List<N> oldChildren = parent.children();
        final List<N> newChildren = newParent.children();
        assertEquals(oldChildren.size(), 1 + newChildren.size(), "new children must have 1 less child than old");

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    @SafeVarargs
    protected final N setChildrenAndCheck(final N parent, final N... children) {
        final N newParent = parent.setChildren(Arrays.asList(children));

        this.checkParentOfChildren(newParent);
        this.checkChildCount(newParent, children);

        return newParent;
    }

    protected void checkParentOfChildren(final N parent) {
        int i = 0;
        for (N child : parent.children()) {
            final int j = i;
            assertSame(parent, child.parent().get(), () -> "parent of child[" + i + "]=" + child);
        }
    }

    @SafeVarargs
    protected final void checkChildCount(final N parent, final N... children) {
        this.checkChildCount(parent, children.length);
    }

    protected void checkChildCount(final N parent, final int count) {
        assertEquals(parent.children().size(), count, "children of parent");
        this.checkParentOfChildren(parent);
    }
}
