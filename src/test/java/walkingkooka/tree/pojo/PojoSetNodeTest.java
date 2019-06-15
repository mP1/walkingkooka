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
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.pojo.PojoObjectNodeTest.TestImmutableLeaf;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PojoSetNodeTest extends PojoCollectionNodeTestCase<PojoSetNode, Set<Object>> {

    private final static PojoName SET = PojoName.property("set");
    private final static PojoName X = PojoName.property("x");

    private final static Object ELEMENT0 = "element0";
    private final static Object ELEMENT1 = "element1";
    private final static Object ELEMENT2 = "element2";

    // children.......................................................................................................

    @Test
    public void testSetChildrenIncorrectIndiciesIgnored() {
        final PojoSetNode node = this.createPojoNode();
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
        final Set<Object> set = set(ELEMENT0, ELEMENT1);
        final TestMutableParent parent = new TestMutableParent(set);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(SET, set);
        final PojoNode childNode2 = childNode.setChildren(Lists.of(parentNode.createNode(PojoName.index(0), ELEMENT2)));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0);
        this.childrenValuesCheck(childNode2, ELEMENT2);
        this.checkWithoutParent(childNode);

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testSetChildrenImmutableChildWithParent() {
        final Set<Object> parentSet = set(
                new TestImmutableLeaf(STRING0),
                new TestImmutableLeaf(STRING1));
        final TestImmutableParent parent = new TestImmutableParent(parentSet);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // set

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
        this.childrenValuesCheck(parentNode2, set(children2));
        assertEquals(new TestImmutableParent(set(children2)), parentNode2.value(), "parentNode2.value");
    }

    @Test
    public void testSetChildrenMutableChildWithParent() {
        final Set<Object> parentSet = set(
                new TestMutableLeaf(STRING0),
                new TestMutableLeaf(STRING1));
        final TestMutableParent parent = new TestMutableParent(parentSet);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // set

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
        this.childrenValuesCheck(parentNode2, set(children2));
        assertEquals(new TestMutableParent(set(children2)), parentNode2.value(), "parentNode2.value");
    }

    // childrenValues ....................................................................................................

    @Test
    public void testSetChildrenValuesImmutableChildWithParent() {
        final Set<Object> parentSet = set(
                new TestImmutableLeaf(STRING0),
                new TestImmutableLeaf(STRING1));
        final TestImmutableParent parent = new TestImmutableParent(parentSet);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // set

        final PojoNode childChildNode = childNode.children().get(0); // TestImmutableLeaf(STRING0)
        final PojoNode childChildNode2 = childChildNode.setChildrenValues(Lists.of(STRING2));
        assertNotSame(childChildNode, childChildNode2);

        final PojoNode childNode2 = childChildNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                new TestImmutableLeaf(STRING2),
                new TestImmutableLeaf(STRING1));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);
        this.childrenValuesCheck(parentNode2, Lists.of(set(children2)));
        assertEquals(new TestImmutableParent(set(children2)), parentNode2.value(), "parentNode2.value");
    }

    @Test
    public void testSetChildrenValuesMutableChildWithParent() {
        final Set<Object> parentSet = set(
                new TestMutableLeaf(STRING0),
                new TestMutableLeaf(STRING1));
        final TestMutableParent parent = new TestMutableParent(parentSet);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // set

        final PojoNode childChildNode = childNode.children().get(0); // TestMutableLeaf(STRING0)
        final PojoNode childChildNode2 = childChildNode.setChildrenValues(Lists.of(STRING2));
        assertSame(childChildNode, childChildNode2);

        final PojoNode childNode2 = childChildNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                new TestMutableLeaf(STRING2),
                new TestMutableLeaf(STRING1));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertSame(parentNode, parentNode2);
        this.childrenValuesCheck(parentNode2, set(children2));
        assertEquals(new TestMutableParent(set(children2)), parentNode2.value(), "parentNode2.value");
    }

    // setValue...........................................................................................................

    @Test
    public void testSetValueWithMutableParent() {
        final Set<Object> set = set(ELEMENT0, ELEMENT1);
        final TestMutableParent parent = new TestMutableParent(set);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(SET, set);
        final PojoNode childNode2 = childNode.setValue(set(ELEMENT2));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0);
        this.childrenValuesCheck(childNode2, ELEMENT2);
        this.checkWithoutParent(childNode);

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Override
    PojoSetNode createEmptyPojoNode() {
        return this.createPojoNode(Sets.empty());
    }

    @Override
    PojoSetNode createPojoNode() {
        return this.createPojoNode(this.value());
    }

    @Override
    Set<Object> value() {
        return set(ELEMENT0, ELEMENT1);
    }

    @Override
    Set<Object> differentValue() {
        return set(ELEMENT2);
    }

    private PojoSetNode createPojoNode(final Set<Object> set) {
        return Cast.to(PojoNode.wrap(SET,
                set,
                new ReflectionPojoNodeContext()));
    }

    @Override
    List<PojoNode> children(final PojoSetNode firstNode) {
        return this.children0(firstNode, ELEMENT0, ELEMENT1);
    }

    @Override
    List<PojoNode> differentChildren(final PojoSetNode firstNode) {
        return this.children0(firstNode, ELEMENT0);
    }

    @SafeVarargs
    private final List<PojoNode> children0(final PojoSetNode firstNode, final Object... values) {
        final List<PojoNode> children = Lists.array();
        int i = 0;
        for (Object value : values) {
            children.add(firstNode.createNode(PojoName.index(i), value));
            i++;
        }
        return children;
    }

    private <T> Set<T> set(final List<T> list) {
        final Set<T> set = Sets.ordered();
        set.addAll(list);
        return set;
    }

    @SafeVarargs
    private final <T> Set<T> set(final T... values) {
        final Set<T> set = Sets.ordered();
        set.addAll(Lists.of(values));
        return set;
    }

    @Override
    Class<PojoSetNode> pojoNodeType() {
        return PojoSetNode.class;
    }

    static class TestMutableParent {

        TestMutableParent(final Set<Object> set) {
            this.setSet(set);
        }

        private Set<Object> set;

        public Set<Object> getSet() {
            return this.set;
        }

        public void setSet(final Set<Object> set) {
            this.set = set;
        }

        @Override
        public int hashCode() {
            return this.set.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestMutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestMutableParent other) {
            return this.set.equals(other.set);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + this.set.toString();
        }
    }

    static class TestImmutableParent {

        TestImmutableParent(final Set<?> set) {
            this.set = set;
        }

        private final Set<?> set;

        public Set<?> getSet() {
            return this.set;
        }

        public TestImmutableParent setSet(final Set<Object> set) {
            return this.set.equals(set) ? this : new TestImmutableParent(set);
        }

        @Override
        public int hashCode() {
            return this.set.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestImmutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestImmutableParent other) {
            return this.set.equals(other.set);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + this.set.toString();
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
