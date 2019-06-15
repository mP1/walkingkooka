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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PojoObjectNodeTest extends PojoNodeTestCase2<PojoObjectNode, Object> {

    private final static String STRING0 = "abc0";
    private final static String STRING1 = "def1";
    private final static String STRING2 = "ghi2";

    private final static PojoName X = PojoName.property("x");
    private final static PojoName Y = PojoName.property("y");
    private final static PojoName Z = PojoName.property("z");

    @Test
    public void testParent() {
        this.checkWithoutParent(this.createPojoNode());
    }

    @Test
    public void testCreateNodeUnknownProperty() {
        final PojoObjectNode node = this.createPojoNode(new TestMutableLeaf(STRING0));
        node.createNode(Z, STRING2);
    }

    @Test
    public void testSetChildrenUnknownPropertyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            final PojoObjectNode node = this.createPojoNode(new TestMutableLeaf(STRING0));
            node.setChildren(Lists.of(node.createNode(Z, STRING2)));
        });
    }

    @Test
    public void testSetChildrenUnknownProperty2Fails() {
        final PojoObjectNode node = this.createPojoNode(new TestMutableLeaf(STRING0));
        final List<PojoNode> children = Lists.array();
        children.addAll(node.children());
        children.add(node.createNode(Z, STRING2));

        assertThrows(IllegalArgumentException.class, () -> {
            node.setChildren(children);
        });
    }

    @Test
    public void testSetChildrenDifferentMutable() {
        final TestMutableLeaf value = new TestMutableLeaf(STRING0);
        final PojoObjectNode node = this.createPojoNode(value);
        final PojoNode node2 = node.setChildren(Lists.of(node.createNode(X, STRING1)));

        assertSame(node, node2);
        this.childrenCheck(node2);

        this.childrenValuesCheck(node2, STRING1);
    }

    @Test
    public void testSetChildrenDifferentMutable2() {
        final TestMutableLeaf value = new TestMutableLeaf(STRING0);
        final PojoObjectNode node = this.createPojoNode(value);
        final PojoNode node2 = node.setChildren(Lists.of(
                node.createNode(X, STRING1)
        ));

        assertSame(node, node2);
        this.childrenCheck(node2);

        this.childrenValuesCheck(node2, STRING1);
    }

    @Test
    public void testSetChildrenWithParentDifferentMutable() {
        final TestMutableBranch parent = new TestMutableBranch(new TestMutableLeaf(STRING0), new TestMutableLeaf(STRING1));
        final PojoObjectNode parentNode = this.createPojoNode(parent);
        final PojoNode childNode = this.child(parentNode, Y);

        final PojoNode childNode2 = childNode.setChildren(Lists.of(
                parentNode.createNode(X, STRING2)
        ));
        assertSame(childNode, childNode2, childNode2.toString());

        this.childrenValuesCheck(childNode2, STRING2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        this.childrenValuesCheck(parentNode2, new TestMutableLeaf(STRING2), new TestMutableLeaf(STRING1));
        this.childrenCheck(parentNode2);
    }

    @Test
    public void testSetChildrenWithParentDifferentImmutable() {
        final TestImmutableLeaf child = new TestImmutableLeaf(STRING0);
        final TestImmutableBranch parent = new TestImmutableBranch(child, null);

        final PojoObjectNode parentNode = this.createPojoNode(parent);
        final PojoNode childNode = this.child(parentNode, Y);

        // set
        final PojoNode childNode2 = childNode.setChildren(Lists.of(
                parentNode.createNode(X, STRING2)
        ));
        assertNotSame(childNode, childNode2, childNode2.toString());

        this.childrenValuesCheck(childNode2, STRING2);

        // check
        final PojoNode parentNode2 = childNode2.parentOrFail();
        this.childrenValuesCheck(parentNode2, new TestImmutableLeaf(STRING2), null);

        assertEquals(new TestImmutableBranch(new TestImmutableLeaf(STRING2), null),
                parentNode2.value(),
                "parentNode2 after set");

        assertEquals(new TestImmutableBranch(new TestImmutableLeaf(STRING0), null),
                parentNode.value(),
                "original parentNode should remain unchanged");
    }

    @Test
    public void testSetChildrenWithParentImmutable2() {
        final TestImmutableLeaf child = new TestImmutableLeaf(STRING0);
        final TestImmutableBranch parent = new TestImmutableBranch(child, null);
        final TestImmutableBranch grandParent = new TestImmutableBranch(null, parent);

        final PojoObjectNode grandParentNode = this.createPojoNode(grandParent);
        final PojoNode parentNode = this.child(grandParentNode, Z);
        final PojoNode childNode = this.child(parentNode, Y);

        // set
        final PojoNode childNode2 = childNode.setChildren(Lists.of(
                parentNode.createNode(X, STRING2)
        ));
        assertNotSame(childNode, childNode2, childNode2.toString());

        // check
        final PojoNode parentNode2 = childNode2.parentOrFail();
        this.childrenValuesCheck(parentNode2, new TestImmutableLeaf(STRING2), null);

        final PojoNode grandParentNode2 = parentNode2.parentOrFail();

        final TestImmutableLeaf child2 = new TestImmutableLeaf(STRING2);
        final TestImmutableBranch parent2 = new TestImmutableBranch(child2, null);
        final TestImmutableBranch grandParent2 = new TestImmutableBranch(null, parent2);

        assertEquals(grandParent2,
                grandParentNode2.value(),
                "parentNode2 after set");

        assertEquals(grandParent,
                grandParentNode.value(),
                "original parentNode should remain unchanged");
    }

    @Test
    public void testRemoveChild() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createPojoNode().removeChild(0);
        });
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPojoNode(new TestMutableLeaf(STRING0)),
                "x=" + STRING0);
    }

    @Override
    PojoObjectNode createPojoNode() {
        return this.createPojoNode(this.value());
    }

    @Override
    Object value() {
        return new TestImmutableLeaf(STRING0);
    }

    @Override
    Object differentValue() {
        return new TestImmutableLeaf(STRING2);
    }

    @Override
    final void checkValue(final Object expected, final Object actual) {
        assertEquals(expected, actual);
    }

    private PojoObjectNode createPojoNode(final Object value) {
        return Cast.to(PojoNode.wrap(PojoName.property("root"),
                value,
                new ReflectionPojoNodeContext()));
    }

    @Override
    List<PojoNode> children(final PojoObjectNode firstNode) {
        return this.children0(firstNode, STRING0);
    }

    @Override
    List<PojoNode> differentChildren(final PojoObjectNode firstNode) {
        return this.children0(firstNode, STRING2);
    }

    @Override
    List<PojoNode> writableChildren(final PojoObjectNode firstNode) {
        return children0(firstNode, STRING0);
    }

    @Override
    List<PojoNode> writableDifferentChildren(final PojoObjectNode firstNode) {
        return children0(firstNode, STRING2);
    }

    private List<PojoNode> children0(final PojoObjectNode firstNode, final String x) {
        return Lists.of(firstNode.createNode(X, x));
    }

    @Override
    protected Class<PojoObjectNode> pojoNodeType() {
        return PojoObjectNode.class;
    }

    static class TestImmutableLeaf {
        final String x;

        TestImmutableLeaf(final String x) {
            this.x = x;
        }

        public String getX() {
            return this.x;
        }

        public TestImmutableLeaf setX(final String x) {
            return new TestImmutableLeaf(x);
        }

        @Override
        public int hashCode() {
            return this.x.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestImmutableLeaf && equals0((TestImmutableLeaf) other);
        }

        private boolean equals0(final TestImmutableLeaf other) {
            return this.x.equals(other.x);
        }

        @Override
        public String toString() {
            return "x=" + this.getX();
        }
    }

    static class TestImmutableBranch {

        TestImmutableBranch(final TestImmutableLeaf y, final TestImmutableBranch z) {
            this.y = y;
            this.z = z;
        }

        final TestImmutableLeaf y;

        public TestImmutableLeaf getY() {
            return this.y;
        }

        public TestImmutableBranch setY(final TestImmutableLeaf y) {
            return Objects.equals(this.y, y) ? this : new TestImmutableBranch(y, this.z);
        }

        final TestImmutableBranch z;

        public TestImmutableBranch getZ() {
            return this.z;
        }

        public TestImmutableBranch setZ(final TestImmutableBranch z) {
            return Objects.equals(this.z, z) ? this : new TestImmutableBranch(this.y, z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.y, this.z);
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestImmutableBranch && equals0((TestImmutableBranch) other);
        }

        private boolean equals0(final TestImmutableBranch other) {
            return Objects.equals(this.y, other.y) && Objects.equals(this.z, other.z);
        }

        @Override
        public String toString() {
            return "y=" + this.getY() + "z=" + this.getZ();
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

    static class TestMutableBranch {

        TestMutableBranch(final TestMutableLeaf y, final TestMutableLeaf z) {
            this.y = y;
            this.z = z;
        }

        TestMutableLeaf y;

        public TestMutableLeaf getY() {
            return this.y;
        }

        public void setY(final TestMutableLeaf y) {
            this.y = y;
        }

        TestMutableLeaf z;

        public TestMutableLeaf getZ() {
            return this.z;
        }

        public void setZ(final TestMutableLeaf z) {
            this.z = z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.y, this.z);
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestMutableBranch && equals0((TestMutableBranch) other);
        }

        private boolean equals0(final TestMutableBranch other) {
            return this.y.equals(other.y) && this.z.equals(other.z);
        }

        @Override
        public String toString() {
            return "y=" + this.getY() + "z=" + this.getZ();
        }
    }
}
