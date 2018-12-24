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
import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.test.ClassTestCase;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class NodeTest extends ClassTestCase<Node> {

    @Test
    public void testRoot() {
        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2");
        final TestFakeNode parent1 = new TestFakeNode("parent1", child1, child2);
        final TestFakeNode parent2 = new TestFakeNode("parent2");
        final TestFakeNode root = new TestFakeNode("root", parent1, parent2);

        assertEquals(root, child1.root());
        assertEquals(root, child2.root());
        assertEquals(root, parent2.root());
        assertEquals(root, root);
    }

    @Test
    public void testPointerRoot() {
        this.traverseAndCheck(new TestFakeNode("root"));
    }

    @Test
    public void testPointerChild() {
        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2");

        new TestFakeNode("parent", child1, child2);

        this.traverseAndCheck(child2);
    }

    @Test
    public void testPointerGrandChild2() {
        final TestFakeNode grandChild1 = new TestFakeNode("grandChild1");
        final TestFakeNode grandChild2 = new TestFakeNode("grandChild2");
        final TestFakeNode grandChild3 = new TestFakeNode("grandChild3");

        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2", grandChild1, grandChild2, grandChild3);

        new TestFakeNode("parent", child1, child2);

        this.traverseAndCheck(grandChild3);
    }

    @Test
    public void testPointerGrandChild3() {
        final TestFakeNode grandChild1 = new TestFakeNode("grandChild1");
        final TestFakeNode grandChild2 = new TestFakeNode("grandChild2");
        final TestFakeNode grandChild3 = new TestFakeNode("grandChild3");
        final TestFakeNode grandChild4 = new TestFakeNode("grandChild4");

        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2", grandChild1, grandChild2, grandChild3, grandChild4);

        new TestFakeNode("parent", child1, child2);

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

        assertEquals("pointer returned wrong node",
                Optional.of(node),
                pointer.traverse(node.root()));
    }

    @Override
    protected Class<Node> type() {
        return Node.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
