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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.naming.StringName;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodeTreeIteratorTest extends ClassTestCase<NodeTreeIterator<TestNode, StringName, StringName, Object>>
        implements IteratorTesting,
        ToStringTesting<NodeTreeIterator<TestNode, StringName, StringName, Object>>,
        TypeNameTesting<NodeTreeIterator<TestNode, StringName, StringName, Object>> {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testNodeTreeIterator() {
        final TestNode node = TestNode.with("root");
        this.iterateAndCheck(node.treeIterator(), node);
        this.iterateUsingHasNextAndCheck(node.treeIterator(), node);
    }

    @Test
    public void testOnlyChildrenNoSiblings() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        final TestNode parent = TestNode.with("parent", child1, child2, child3);
        this.iterateAndCheck(parent.treeIterator(), parent, child1, child2, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, child2, child3);
    }

    @Test
    public void testOnlyChildrenNoSiblingsIgnoresParent() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        final TestNode parent = TestNode.with("parent", child1, child2, child3);

        TestNode.with("root", parent);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, child2, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, child2, child3);
    }

    @Test
    public void testOnlyChildrenIgnoresSiblings() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        final TestNode beforeSibling = TestNode.with("beforeSibling");
        final TestNode parent = TestNode.with("parent", child1, child2, child3);
        final TestNode afterSibling = TestNode.with("afterSibling");
        TestNode.with("root", beforeSibling, parent, afterSibling);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, child2, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, child2, child3);
    }

    @Test
    public void testWithGrandChildren() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.iterateAndCheck(parent.treeIterator(), parent, child, grandChild);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child, grandChild);
    }

    @Test
    public void testWithGrandChildren2() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode child1 = TestNode.with("child1", grandChild1);

        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode child2 = TestNode.with("child2", grandChild2);

        final TestNode parent = TestNode.with("parent", child1, child2);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, grandChild1, child2, grandChild2);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent,child1, grandChild1, child2, grandChild2);
    }

    @Test
    public void testWithGrandChildren3() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode child1 = TestNode.with("child1", grandChild1);

        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");
        final TestNode child2 = TestNode.with("child2", grandChild2, grandChild3);

        final TestNode child3 = TestNode.with("child3");

        final TestNode parent = TestNode.with("parent", child1, child2, child3);

        this.iterateAndCheck(parent.treeIterator(), parent, child1, grandChild1, child2, grandChild2, grandChild3, child3);
        this.iterateUsingHasNextAndCheck(parent.treeIterator(), parent, child1, grandChild1, child2, grandChild2, grandChild3, child3);
    }

    @Test
    public void testWithGrandChildren4() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode child1 = TestNode.with("child1", grandChild1);

        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");
        final TestNode child2 = TestNode.with("child2", grandChild2, grandChild3);

        final TestNode child3 = TestNode.with("child3");

        TestNode.with("parent", child1, child2, child3);

        this.iterateAndCheck(child1.treeIterator(), child1, grandChild1);
        this.iterateUsingHasNextAndCheck(child1.treeIterator(), child1, grandChild1);
    }

    @Test
    public void testRemoveFails() {
        final Iterator<TestNode> iterator = this.createIterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, () -> {
            iterator.remove();
        });
    }

    @Test
    public void testToStringEmpty() {
        final Iterator<TestNode> iterator = this.createIterator();
        iterator.next();
        this.toStringAndCheck(iterator, "???");
    }

    @Test
    public void testToStringNextAvailable() {
        final Iterator<TestNode> iterator = this.createIterator();
        this.toStringAndCheck(iterator, "\"root\"");
    }

    private NodeTreeIterator<TestNode, StringName, StringName, Object> createIterator() {
        return new NodeTreeIterator<>(TestNode.with("root"));
    }

    @Override
    public Class<NodeTreeIterator<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeTreeIterator.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return Node.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return Iterator.class.getSimpleName();
    }
}
