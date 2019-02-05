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

package walkingkooka.tree.json;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonArrayNodeTest extends JsonParentNodeTestCase<JsonArrayNode, List<JsonNode>> {

    // append

    @Test
    public void testAppendEmptyArray() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.array());
    }

    @Test
    public void testAppendBoolean() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.booleanNode(true));
    }

    @Test
    public void testAppendNumber() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.number(123));
    }

    @Test
    public void testAppendNull() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.nullNode());
    }

    @Test
    public void testAppendEmptyObject() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.object());
    }

    @Test
    public void testAppendString() {
        this.appendChildAndCheck(this.createJsonNode(), JsonNode.string("string-abc-123"));
    }

    // append and get

    @Test
    public void testGet() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        this.childrenCheck(array);

        for(int i = 0; i < 2; i++) {
            final JsonStringNode element = array.children().get(i).cast();
            assertEquals(values[i], element.value(), "value");
        }

        // verify originals were not mutated.
        this.checkWithoutParent(first);
        this.checkWithoutParent(second);
    }

    @Test
    public void testSetChildrenSame() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        assertSame(array, array.setChildren(array.children()));
    }

    @Test
    public void testSetChildrenEquivalent() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        assertSame(array, array.setChildren(Lists.of(first, second)));
    }

    @Test
    public void testSetSame() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        assertSame(array, array.set(0, first));
        assertSame(array, array.set(1, second));
    }

    @Test
    public void testSetSame2() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        assertSame(array, array.set(0, array.get(0)));
        assertSame(array, array.set(1, array.get(1)));
    }

    @Test
    public void testSetDifferent() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);

        final String differentValue = "different-1";
        final JsonNode differentChild = JsonNode.string(differentValue);
        final JsonArrayNode different = array.set(1, differentChild);

        assertNotSame(array, different);
        this.childrenCheck(different);
        assertEquals(values[0], JsonStringNode.class.cast(different.children().get(0)).value(), "child[0].value");
        assertEquals(differentValue, JsonStringNode.class.cast(different.children().get(1)).value(), "child[1].value");

        // verify originals were not mutated.
        this.checkWithoutParent(first);
        this.checkWithoutParent(second);
        this.checkWithoutParent(differentChild);
    }

    // remove

    @Test
    public void testRemoveNegativeIndexFails() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.createJsonNode().remove(-1);
        });
    }

    @Test
    public void testRemoveInvalidIndexFails() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.createJsonNode().remove(0);
        });
    }

    @Test
    public void testRemove() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        final JsonArrayNode removed = array.remove(0);

        assertNotSame(array, removed);
        this.childrenCheck(removed);
        assertEquals("B2", JsonStringNode.class.cast(removed.children().get(0)).value(), "child[0].value");

        // verify originals were not mutated.
        this.checkWithoutParent(first);
        this.checkWithoutParent(second);
    }

    @Test
    public void testRemoveInvalidIndexFails2() {
        final String[] values = new String[]{ "A1", "B2"};
        final JsonNode first = JsonNode.string(values[0]);
        final JsonNode second = JsonNode.string(values[1]);

        final JsonArrayNode array = JsonNode.array().appendChild(first).appendChild(second);
        final IndexOutOfBoundsException expected = assertThrows(IndexOutOfBoundsException.class, () -> {
            array.remove(2);
        });
        this.childrenCheck(array);
        this.checkChildCount(array, 2);
    }

    @Test
    public void testReplaceChild() {
        final JsonArrayNode root = JsonNode.array()
                .appendChild(JsonNode.string("A1"));
        this.checkChildCount(root, 1);

        final JsonArrayNode updated = root.replaceChild(root.get(0), JsonNode.string("B2"))
                .cast();

        this.checkChildCount(updated, 1);
        assertEquals("B2", JsonStringNode.class.cast(updated.get(0)).value());
    }

    @Test
    public void testReplaceChild2() {
        final JsonNode child1 = JsonNode.string("C1");

        final JsonArrayNode root = JsonNode.array()
                .appendChild(child1)
                .appendChild(JsonNode.array()
                        .appendChild(JsonNode.string("GC1-old")));
        this.checkChildCount(root, 2);

        final JsonNode grandChild1New = JsonNode.string("GC1-new");
        final JsonArrayNode updatedChild = root.get(1)
                .setChildren(Lists.of(grandChild1New))
                .cast();
        this.checkChildCount(updatedChild, 1);
        
        final JsonArrayNode updatedRoot = updatedChild.parent().get().cast();
        this.checkChildCount(updatedRoot, 2);
        assertEquals(JsonNode.array()
                        .appendChild(child1)
                        .appendChild(JsonNode.array()
                                .appendChild(grandChild1New)),
                updatedRoot,
                "updatedRoot");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<JsonNode> visited = Lists.array();

        final JsonArrayNode array = this.createJsonNode()
                .appendChild(JsonNode.string("string1"))
                .appendChild(JsonNode.string("string2"));
        final JsonStringNode string1 = array.get(0).cast();
        final JsonStringNode string2 = array.get(1).cast();

        new FakeJsonNodeVisitor() {
            @Override
            protected Visiting startVisit(final JsonNode n) {
                b.append("1");
                visited.add(n);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonNode n) {
                b.append("2");
                visited.add(n);
            }

            @Override
            protected Visiting startVisit(final JsonArrayNode t) {
                assertSame(array, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonArrayNode t) {
                assertSame(array, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final JsonStringNode t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(array);
        assertEquals("1517217262", b.toString());
        assertEquals(Lists.of(array, array,
                        string1, string1, string1,
                        string2, string2, string2,
                        array, array),
                visited,
                "visited");
    }

    @Test
    public void testTextWithoutChildren() {
        assertEquals("", JsonNode.array().text());
    }

    @Test
    public void testTextWithChildren() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));
        assertEquals("true2.0third", array.text());
    }

    @Test
    public void testSelectorUsage() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));

        final JsonNode expected = array.get(1);

        this.selectorAcceptAndCheck(array,
                JsonNode.PATH_SEPARATOR.absoluteNodeSelectorBuilder(JsonNode.class)
                .descendant()
                .named(expected.name())
                .build(),
                expected);
    }

    // toSearchNode .......................................................................................

    @Test
    public void testToSearchNode() {
        final JsonBooleanNode booleanNode = JsonNode.booleanNode(true);
        final JsonNumberNode number = JsonNode.number(2);
        final JsonStringNode string = JsonNode.string("third");

        final JsonArrayNode array = JsonNode.array()
                .appendChild(booleanNode)
                .appendChild(number)
                .appendChild(string);

        this.toSearchNodeAndCheck(array, SearchNode.sequence(Lists.of(booleanNode.toSearchNode(),
                number.toSearchNode(),
                string.toSearchNode())));
    }

    // HashCodeAnddEqualityDefined.......................................................

    @Test
    public void testEqualsDifferentChildren() {
        this.checkNotEquals(JsonNode.array().appendChild(JsonNode.string("different")));
    }

    @Test
    public void testEqualsDifferentChildren2() {
        this.checkNotEquals(JsonNode.array().appendChild(JsonNode.string("child1")),
                JsonNode.array().appendChild(JsonNode.string("child2")));
    }

    @Test
    public void testEqualsArray() {
        this.checkNotEquals(JsonNode.array().appendChild(JsonNode.string("child2")),
                JsonNode.object().set(JsonNodeName.with("prop"), JsonNode.string("child1")));
    }

    @Test
    public void testEqualsArrayAndObjectElement() {
        this.checkNotEquals(
                JsonNode.array().appendChild(JsonNode.array().appendChild(JsonNode.string("element1"))),
                JsonNode.array().appendChild(JsonNode.object().set(JsonNodeName.with("element-prop"), JsonNode.string("element-value"))));
    }

    // toString .......................................................................................

    @Test
    public void testToStringEmpty() {
        assertEquals("[]", this.createJsonNode().toString());
    }

    @Test
    public void testToStringWithChildren() {
        final JsonArrayNode array = JsonNode.array()
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(2))
                .appendChild(JsonNode.string("third"));

        assertEquals("[true, 2, \"third\"]", array.toString());
    }

    @Override
    JsonArrayNode createJsonNode() {
        return JsonArrayNode.array();
    }

    @Override
    protected JsonArrayNode appendChildAndCheck(final JsonNode parent,
                                                final JsonNode child) {
        final JsonArrayNode newParent = parent.appendChild(child).cast();
        assertNotSame(newParent, parent, "appendChild must not return the same node");

        final List<JsonNode> children = newParent.children();
        assertNotEquals(0, children.size(), "children must have at least 1 child");

        final int lastIndex = children.size() - 1;
        assertEquals(JsonNodeName.index(lastIndex), children.get(lastIndex).name(), "last child must be the added child");

        this.checkParentOfChildren(newParent);
        this.checkWithoutParent(child);

        return newParent;
    }

    @Override
    Class<JsonArrayNode> jsonNodeType() {
        return JsonArrayNode.class;
    }
}
