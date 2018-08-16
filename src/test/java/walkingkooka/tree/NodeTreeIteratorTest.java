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

package walkingkooka.tree;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.IteratorTestCase;
import walkingkooka.naming.Name;
import walkingkooka.naming.StringName;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public final class NodeTreeIteratorTest extends IteratorTestCase<NodeTreeIterator<TestFakeNode, StringName, Name, Object>, TestFakeNode> {

    @Test
    public void testNodeTreeIterator() {
        final TestFakeNode node = new TestFakeNode("root");
        this.iterateAndCheck(node.treeIterator(), node);
        this.iterateUsingHasNextAndCheck(node.treeIterator(), node);
    }

    @Test
    public void testOnlyChildrenNoSiblings() {
        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2");
        final TestFakeNode child3 = new TestFakeNode("child3");

        final TestFakeNode parent = new TestFakeNode("parent", child1, child2, child3);
        this.iterateAndCheck(parent.treeIterator(), parent, child1, child2, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, child2, child3);
    }

    @Test
    public void testOnlyChildrenNoSiblingsIgnoresParent() {
        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2");
        final TestFakeNode child3 = new TestFakeNode("child3");

        final TestFakeNode parent = new TestFakeNode("parent", child1, child2, child3);

        new TestFakeNode("root", parent);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, child2, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, child2, child3);
    }

    @Test
    public void testOnlyChildrenIgnoresSiblings() {
        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2");
        final TestFakeNode child3 = new TestFakeNode("child3");

        final TestFakeNode beforeSibling = new TestFakeNode("beforeSibling");
        final TestFakeNode parent = new TestFakeNode("parent", child1, child2, child3);
        final TestFakeNode afterSibling = new TestFakeNode("afterSibling");
        new TestFakeNode("root", beforeSibling, parent, afterSibling);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, child2, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, child2, child3);
    }

    @Test
    public void testWithGrandChildren() {
        final TestFakeNode grandChild = new TestFakeNode("grandChild");
        final TestFakeNode child = new TestFakeNode("child", grandChild);
        final TestFakeNode parent = new TestFakeNode("parent", child);

        this.iterateAndCheck(parent.treeIterator(), parent, child, grandChild);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child, grandChild);
    }

    @Test
    public void testWithGrandChildren2() {
        final TestFakeNode grandChild1 = new TestFakeNode("grandChild1");
        final TestFakeNode child1 = new TestFakeNode("child1", grandChild1);

        final TestFakeNode grandChild2 = new TestFakeNode("grandChild2");
        final TestFakeNode child2 = new TestFakeNode("child2", grandChild2);

        final TestFakeNode parent = new TestFakeNode("parent", child1, child2);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, grandChild1, child2, grandChild2);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent,child1, grandChild1, child2, grandChild2);
    }

    @Test
    public void testWithGrandChildren3() {
        final TestFakeNode grandChild1 = new TestFakeNode("grandChild1");
        final TestFakeNode child1 = new TestFakeNode("child1", grandChild1);

        final TestFakeNode grandChild2 = new TestFakeNode("grandChild2");
        final TestFakeNode grandChild3 = new TestFakeNode("grandChild3");
        final TestFakeNode child2 = new TestFakeNode("child2", grandChild2, grandChild3);

        final TestFakeNode child3 = new TestFakeNode("child3");

        final TestFakeNode parent = new TestFakeNode("parent", child1, child2, child3);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, grandChild1, child2, grandChild2, grandChild3, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, grandChild1, child2, grandChild2, grandChild3, child3);
    }

    @Test
    public void testWithGrandChildren4() {
        final TestFakeNode grandChild1 = new TestFakeNode("grandChild1");
        final TestFakeNode child1 = new TestFakeNode("child1", grandChild1);

        final TestFakeNode grandChild2 = new TestFakeNode("grandChild2");
        final TestFakeNode grandChild3 = new TestFakeNode("grandChild3");
        final TestFakeNode child2 = new TestFakeNode("child2", grandChild2, grandChild3);

        final TestFakeNode child3 = new TestFakeNode("child3");

        new TestFakeNode("parent", child1, child2, child3);

        this.iterateAndCheck(child1.treeIterator(), child1, grandChild1);
        this.iterateUsingHasNextAndCheck(child1.treeIterator(), child1, grandChild1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveFails() {
        final Iterator<TestFakeNode> iterator = this.createIterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void testToStringEmpty() {
        final Iterator<TestFakeNode> iterator = this.createIterator();
        iterator.next();
        assertEquals("???", iterator.toString());
    }

    @Test
    public void testToStringNextAvailable() {
        final Iterator<TestFakeNode> iterator = this.createIterator();
        assertEquals("\"root\"", iterator.toString());
    }

    @Override
    protected NodeTreeIterator<TestFakeNode, StringName, Name, Object> createIterator() {
        return new NodeTreeIterator<>(new TestFakeNode("root"));
    }

    @Override
    protected Class<NodeTreeIterator<TestFakeNode, StringName, Name, Object>> type() {
        return Cast.to(NodeTreeIterator.class);
    }
}
