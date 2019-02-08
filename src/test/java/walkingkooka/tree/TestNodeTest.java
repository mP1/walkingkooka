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
import walkingkooka.naming.Name;
import walkingkooka.naming.StringName;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNodeTest extends NodeTestCase2<TestNode, StringName, StringName, Object> {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testRoot() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent1 = TestNode.with("parent1", child1, child2);
        final TestNode parent2 = TestNode.with("parent2");
        final TestNode root = TestNode.with("root", parent1, parent2);

        assertEquals(root, child1.root());
        assertEquals(root, child2.root());
        assertEquals(root, parent2.root());
        assertEquals(root, root);
    }

    @Test
    @Override
    public void testEquals() {
        final TestNode node1 = TestNode.with(this.currentTestName() + "-" + this.i);
        TestNode.clear(); // required to reset cache which verifies uniqueness of nodes.
        final TestNode node2 = TestNode.with(this.currentTestName() + "-" + this.i);
        checkEqualsAndHashCode(node1, node2);
    }

    @Test
    public void testPointerRoot() {
        this.traverseAndCheck(TestNode.with("root"));
    }

    @Test
    public void testPointerChild() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        TestNode.with("parent", child1, child2);

        this.traverseAndCheck(child2);
    }

    @Test
    public void testPointerGrandChild2() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");

        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2", grandChild1, grandChild2, grandChild3);

        TestNode.with("parent", child1, child2);

        this.traverseAndCheck(grandChild3);
    }

    @Test
    public void testPointerGrandChild3() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");
        final TestNode grandChild4 = TestNode.with("grandChild4");

        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2", grandChild1, grandChild2, grandChild3, grandChild4);

        TestNode.with("parent", child1, child2);

        this.traverseAndCheck(grandChild4);
    }

    @Test
    public void testPointerJsonObjectProperty() {
        final JsonNodeName second = JsonNodeName.with("second");

        final JsonObjectNode root = JsonNode.object()
                .set(JsonNodeName.with("first"), JsonNode.string("a1"))
                .set(second, JsonNode.string("b2"));

        this.traverseAndCheck(root.get(second).get());
    }

    @Test
    public void testPointerJsonArrayElement() {
        final JsonNodeName second = JsonNodeName.with("second");

        final JsonObjectNode object = JsonNode.object()
                .set(JsonNodeName.with("first"), JsonNode.string("a1"))
                .set(second, JsonNode.string("b2"));

        final JsonArrayNode root = JsonNode.array()
                .appendChild(JsonNode.string("first-element"))
                .appendChild(object);

        final JsonObjectNode firstElement = Cast.to(root.get(1));
        this.traverseAndCheck(firstElement.get(second).get());
    }

    private <N2 extends Node<N2, NAME2, ANAME2, AVALUE2>,
            NAME2 extends Name,
            ANAME2 extends Name,
            AVALUE2> void traverseAndCheck(final N2 node) {
        final NodePointer<N2, NAME2, ANAME2, AVALUE2> pointer = node.pointer();

        assertEquals(Optional.of(node),
                pointer.traverse(node.root()),
                "pointer returned wrong node");
    }

    @Override
    public void testReplaceChildDifferentParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestNode createNode() {
        return TestNode.with(this.currentTestName() + "-" + this.i++);
    }

    private int i = 0;

    @Override public String typeNamePrefix() {
        return "Test";
    }

    @Override
    public Class<TestNode> type() {
        return TestNode.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
