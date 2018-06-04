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

import org.junit.Test;
import walkingkooka.naming.Name;
import walkingkooka.test.PackagePrivateClassTestCase;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotSame;

abstract public class NodeTestCase<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        PackagePrivateClassTestCase<N> {

    protected NodeTestCase() {
        super();
    }

    @Test final public void testNameCached() {
        final N node = this.createNode();
        this.checkCached(node, "name", node.name(), node.name());
    }

    @Test final public void testParentCached() {
        final N node = this.createNode();
        this.checkCached(node, "parent", node.parent(), node.parent());
    }

    @Test final public void testChildrenCached() {
        final N node = this.createNode();
        this.checkCached(node, "children", node.children(), node.children());
    }

    @Test
    public void testAppendChild() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        this.checkChildren(parent1, child1.setParent(parent1));
    }

    @Test
    public void testAppendChild2() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N child2 = this.createNode();
        final N parent2 = this.appendChildAndCheck(parent1, child2);

        this.checkChildren(parent2, child1.setParent(parent2), child2.setParent(parent2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveChildWithoutParent() {
        final N parent = this.createNode();
        final N child = this.createNode();

        parent.removeChild(child);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveChildDifferentParent() {
        final N parent1 = this.createNode();

        final N parent2 = this.createNode().appendChild(this.createNode());
        final N childOf2 = parent2.children().get(0);

        parent1.removeChild(childOf2);
    }

    @Test
    public void testRemoveChild() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N removed1 = this.removeChildAndCheck(parent1, parent1.children().get(0));

        this.checkChildren(removed1);
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

        final N child1 = this.createNode();
        final N parent1 = this.appendChildAndCheck(parent, child1);

        final N child2 = this.createNode();
        final N parent2 = this.appendChildAndCheck(parent1, child2);

        final N removed = parent2.children().get(remove);
        final N parent3 = this.removeChildAndCheck(parent2, removed);

        this.checkChildren(parent3, removed.setParent(parent3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildWithoutParent() {
        final N parent = this.createNode();
        final N child = this.createNode();

        parent.replaceChild(child, this.createNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceChildDifferentParent() {
        final N parent1 = this.createNode();

        final N parent2 = this.createNode().appendChild(this.createNode());
        final N childOf2 = parent2.children().get(0);

        parent1.replaceChild(childOf2, this.createNode());
    }

    @Test
    public void testReplaceChild() {
        final N parent = this.createNode();

        final N child1 = this.createNode();
        final N child2 = this.createNode();

        final N set = this.setChildrenAndCheck(parent, child1, child2);

        this.checkChildren(set, child1.setParent(set), child1.setParent(set));
    }

    @Test(expected = NullPointerException.class)
    public void setChildrenWithNullFails() {
        final N parent = this.createNode();
        parent.setChildren(null);
    }

    @Test
    public void testSameChildren() {
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

        this.checkChildren(parent2, child3.setParent(parent2));
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
        if (value != value2) {
            failNotSame(node + " did not cache " + property, value, value2);
        }
    }

    abstract protected N createNode();

    protected void checkParent(final N node, final N parent) {
        assertSame("parent of " + node, parent, node.parent());
    }

    protected N appendChildAndCheck(final N parent, final N child) {
        final N newParent = parent.appendChild(child);
        assertNotSame("appendChild must not return the same node", newParent, parent);

        final List<N> children = newParent.children();
        assertNotEquals("children must have at least 1 child", 0, children.size());
        assertEquals("last child must be the added child", child.name(), children.get(children.size() - 1).name());

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    protected N removeChildAndCheck(final N parent, final N child) {
        final N newParent = parent.removeChild(child);
        assertNotSame("removeChild must not return the same node", newParent, parent);

        final List<N> oldChildren = parent.children();
        final List<N> newChildren = newParent.children();
        assertEquals("new children must have 1 less child than old", oldChildren.size(), 1 + newChildren.size());

        this.checkParentOfChildren(newParent);

        return newParent;
    }

    protected N setChildrenAndCheck(final N parent, final N... children) {
        final N newParent = parent.setChildren(Arrays.asList(children));

        this.checkParentOfChildren(newParent);
        this.checkChildren(newParent, children);

        return newParent;
    }

    protected void checkParentOfChildren(final N parent) {
        int i = 0;
        for (N child : parent.children()) {
            assertSame("parent of child[" + i + "]=" + child, parent, child.parent().get());
        }
    }

    protected void checkChildren(final N parent, final N... children) {
        assertEquals("children of parent", parent.children().size(), children.length);
        this.checkParentOfChildren(parent);
    }
}
