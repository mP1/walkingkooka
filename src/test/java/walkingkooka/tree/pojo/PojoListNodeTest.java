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

package walkingkooka.tree.pojo;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.pojo.PojoObjectNodeTest.TestImmutableLeaf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PojoListNodeTest extends PojoCollectionNodeTestCase<PojoListNode, List<Object>> {

    private final static PojoName LIST = PojoName.property("list");
    private final static PojoName X = PojoName.property("x");

    private final static Object ELEMENT0 = "element0";
    private final static Object ELEMENT1 = null;
    private final static Object ELEMENT2 = "element2";

    // children.......................................................................................................

    @Test
    public void testSetChildrenIncorrectIndiciesIgnored() {
        final PojoListNode node = this.createPojoNode();
        final PojoNode node2 = node.setChildren(Lists.of(node.createNode(PojoName.index(99), ELEMENT0)));
        assertNotSame(node, node2);

        this.childrenAndCheckNames(node2, INDEX0);
        this.childrenValuesCheck(node2, ELEMENT0);
        this.checkWithoutParent(node);

        this.childrenAndCheckNames(node, INDEX0, INDEX1);
        this.childrenValuesCheck(node, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testSetChildrenWithMutableParent() {
        final List<Object> list = Lists.of(ELEMENT0, ELEMENT1);
        final TestMutableParent parent = new TestMutableParent(list);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(LIST, list);
        final PojoNode childNode2 = childNode.setChildren(Lists.of(parentNode.createNode(PojoName.index(0), ELEMENT0),
                parentNode.createNode(PojoName.index(1), ELEMENT1),
                parentNode.createNode(PojoName.index(2), ELEMENT2)));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0, INDEX1, INDEX2);
        this.childrenValuesCheck(childNode2, ELEMENT0, ELEMENT1, ELEMENT2);
        this.checkWithoutParent(childNode);

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testSetChildrenImmutableChildWithParent() {
        final List<Object> parentList = Lists.of(
                new TestImmutableLeaf(STRING0),
                new TestImmutableLeaf(STRING1));
        final TestImmutableParent parent = new TestImmutableParent(parentList);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // list

        final PojoNode childChildNode = childNode.children().get(0); // TestImmutableLeaf(STRING0)
        final PojoNode childChildNode2 = childChildNode.setChildren(Lists.of(parentNode.createNode(X, STRING2)));
        assertNotSame(childChildNode, childChildNode2);

        final PojoNode childNode2 = childChildNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                new TestImmutableLeaf(STRING2),
                new TestImmutableLeaf(STRING1));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);
        this.childrenValuesCheck(parentNode2, Lists.of(children2));
        assertEquals(new TestImmutableParent(children2), parentNode2.value(), "parentNode2.value");
    }

    @Test
    public void testSetChildrenMutableChildWithParent() {
        final List<Object> parentList = Lists.of(
                new TestMutableLeaf(STRING0),
                new TestMutableLeaf(STRING1));
        final TestMutableParent parent = new TestMutableParent(parentList);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // list

        final PojoNode childChildNode = childNode.children().get(0); // TestMutableLeaf(STRING0)
        final PojoNode childChildNode2 = childChildNode.setChildren(Lists.of(parentNode.createNode(X, STRING2)));
        assertSame(childChildNode, childChildNode2);

        final PojoNode childNode2 = childChildNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                new TestMutableLeaf(STRING2),
                new TestMutableLeaf(STRING1));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertSame(parentNode, parentNode2);
        this.childrenValuesCheck(parentNode2, Lists.of(children2));
        assertEquals(new TestMutableParent(children2), parentNode2.value(), "parentNode2.value");
    }

    // childrenValues ....................................................................................................

    @Test
    public void testSetChildrenValuesImmutableChildWithParent() {
        final List<Object> parentList = Lists.of(new TestImmutableLeaf(STRING0));
        final TestImmutableParent parent = new TestImmutableParent(parentList);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // list

        final PojoNode childChildNode = childNode.children().get(0); // TestImmutableLeaf(STRING0)
        final PojoNode childChildNode2 = childChildNode.setChildrenValues(Lists.of(STRING1));
        assertNotSame(childChildNode, childChildNode2);

        final PojoNode childNode2 = childChildNode2.parentOrFail();
        final TestImmutableLeaf child2 = new TestImmutableLeaf(STRING1);
        this.childrenValuesCheck(childNode2, child2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);
        this.childrenValuesCheck(parentNode2, Lists.of(Lists.of(child2)));
        assertEquals(new TestImmutableParent(Lists.of(child2)), parentNode2.value(), "parentNode2.value");
    }

    @Test
    public void testSetChildrenValuesMutableChildWithParent() {
        final List<Object> parentList = Lists.of(new TestMutableLeaf(STRING0));
        final TestMutableParent parent = new TestMutableParent(parentList);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // list

        final PojoNode childChildNode = childNode.children().get(0); // TestMutableLeaf(STRING0)
        final PojoNode childChildNode2 = childChildNode.setChildrenValues(Lists.of(STRING1));
        assertSame(childChildNode, childChildNode2);

        final PojoNode childNode2 = childChildNode2.parentOrFail();
        final TestMutableLeaf child2 = new TestMutableLeaf(STRING1);
        this.childrenValuesCheck(childNode2, child2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertSame(parentNode, parentNode2);
        this.childrenValuesCheck(parentNode2, Lists.of(Lists.of(child2)));
        assertEquals(new TestMutableParent(Lists.of(child2)), parentNode2.value(), "parentNode2.value");
    }

    // setValue........................................................................................................

    @Test
    public void testSetValueWithMutableParent() {
        final List<Object> list = Lists.of(ELEMENT0, ELEMENT1);
        final TestMutableParent parent = new TestMutableParent(list);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(LIST, list);
        final PojoNode childNode2 = childNode.setValue(Lists.of(ELEMENT0, ELEMENT1, ELEMENT2));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0, INDEX1, INDEX2);
        this.childrenValuesCheck(childNode2, ELEMENT0, ELEMENT1, ELEMENT2);
        this.checkWithoutParent(childNode);

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Override
    PojoListNode createEmptyPojoNode() {
        return this.createPojoNode(Lists.empty());
    }

    @Override
    PojoListNode createPojoNode() {
        return this.createPojoNode(this.value());
    }

    @Override
    List<Object> value() {
        return Lists.of(ELEMENT0, ELEMENT1);
    }

    @Override
    List<Object> differentValue() {
        return Lists.of(ELEMENT2);
    }

    private PojoListNode createPojoNode(final List<Object> list) {
        return Cast.to(PojoNode.wrap(LIST,
                list,
                new ReflectionPojoNodeContext()));
    }

    @Override
    List<PojoNode> children(final PojoListNode firstNode) {
        return this.children0(firstNode, ELEMENT0, ELEMENT1);
    }

    @Override
    List<PojoNode> differentChildren(final PojoListNode firstNode) {
        return this.children0(firstNode, ELEMENT0);
    }

    private List<PojoNode> children0(final PojoListNode firstNode, final Object... values) {
        final List<PojoNode> children = Lists.array();
        int i = 0;
        for (Object value : values) {
            children.add(firstNode.createNode(PojoName.index(i), value));
            i++;
        }
        return children;
    }

    @Override
    Class<PojoListNode> pojoNodeType() {
        return PojoListNode.class;
    }

    static class TestMutableParent {

        TestMutableParent(final List<Object> list) {
            this.setList(list);
        }

        private List<Object> list;

        public List<Object> getList() {
            return this.list;
        }

        public void setList(final List<Object> list) {
            this.list = list;
        }

        @Override
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestMutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestMutableParent other) {
            return this.list.equals(other.list);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + this.list.toString();
        }
    }

    static class TestImmutableParent {

        TestImmutableParent(final List<?> list) {
            this.list = list;
        }

        private final List<?> list;

        public List<?> getList() {
            return this.list;
        }

        public TestImmutableParent setList(final List<Object> list) {
            return this.list.equals(list) ? this : new TestImmutableParent(list);
        }

        @Override
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestImmutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestImmutableParent other) {
            return this.list.equals(other.list);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + this.list.toString();
        }
    }

    static class TestMutableLeaf {
        String x;

        TestMutableLeaf(final String x) {
            this.setX(x);
        }

        public String getX() {
            return this.x;
        }

        public void setX(final String x) {
            this.x = x;
        }

        @Override
        public int hashCode() {
            return this.x.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestMutableLeaf && equals0((TestMutableLeaf) other);
        }

        private boolean equals0(final TestMutableLeaf other) {
            return this.x.equals(other.x);
        }

        @Override
        public String toString() {
            return "x=" + this.getX();
        }
    }
}
