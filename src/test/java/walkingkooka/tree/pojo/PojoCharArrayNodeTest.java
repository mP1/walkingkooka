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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PojoCharArrayNodeTest extends PojoArrayNodeTestCase<PojoCharArrayNode, char[]> {

    private final static char ELEMENT0 = 'a';
    private final static char ELEMENT1 = 'b';
    private final static char ELEMENT2 = Character.MAX_VALUE;

    // children.......................................................................................................

    @Test
    public void testSetChildrenIncorrectIndiciesIgnored() {
        final PojoCharArrayNode node = this.createPojoNode();
        final PojoNode node2 = node.setChildren(Lists.of(node.createNode(PojoName.index(99), ELEMENT0)));
        assertNotSame(node, node2);

        this.childrenAndCheckNames(node2, INDEX0);
        this.childrenValuesCheck(node2, ELEMENT0);
        this.checkWithoutParent(node);

        this.childrenAndCheckNames(node, INDEX0, INDEX1);
        this.childrenValuesCheck(node, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testSetChildrenValuesWithMutableParent() {
        final char[] array = new char[]{ELEMENT0, ELEMENT1};
        final TestMutableParent parent = new TestMutableParent(array);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(ARRAY, array);
        final PojoNode parentNode2 = parentNode.setChildren(Lists.of(childNode));
        assertSame(parentNode, parentNode2);

        final PojoNode childNode2 = parentNode2.children()
                .get(0)
                .setChildrenValues(Lists.of(ELEMENT0, ELEMENT1, ELEMENT2));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0, INDEX1, INDEX2);
        this.childrenValuesCheck(childNode2, ELEMENT0, ELEMENT1, ELEMENT2);
        this.checkWithParent(childNode2);

        assertSame(parentNode, childNode2.parentOrFail());

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testSetChildrenValuesWithImmutableParent() {
        final char[] array = new char[]{ELEMENT0, ELEMENT1};
        final TestImmutableParent parent = new TestImmutableParent(array);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(ARRAY, array);
        final PojoNode parentNode2 = parentNode.setChildren(Lists.of(childNode));
        assertSame(parentNode, parentNode2);

        final PojoNode childNode2 = parentNode2.children()
                .get(0)
                .setChildrenValues(Lists.of(ELEMENT0, ELEMENT1, ELEMENT2));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0, INDEX1, INDEX2);
        this.childrenValuesCheck(childNode2, ELEMENT0, ELEMENT1, ELEMENT2);
        this.checkWithParent(childNode2);

        assertNotSame(parentNode, childNode2.parentOrFail());

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testSetValueWithImmutableParent() {
        final char[] array = new char[]{ELEMENT0, ELEMENT1};
        final TestImmutableParent parent = new TestImmutableParent(array);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(ARRAY, array);
        final PojoNode parentNode2 = parentNode.setChildren(Lists.of(childNode));
        assertSame(parentNode, parentNode2);

        final PojoNode childNode2 = parentNode2.children()
                .get(0)
                .setValue(new char[]{ELEMENT0, ELEMENT1, ELEMENT2});
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0, INDEX1, INDEX2);
        this.childrenValuesCheck(childNode2, ELEMENT0, ELEMENT1, ELEMENT2);
        this.checkWithParent(childNode2);

        assertNotSame(parentNode, childNode2.parentOrFail());

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Test
    public void testEqualsDifferentValues() {
        this.createPojoNode(new char[]{'z'});
    }

    @Test
    public void testEqualsDifferentValues2() {
        this.createPojoNode(new char[]{'a', 'b'});
    }

    @Override
    PojoCharArrayNode createEmptyPojoNode() {
        return this.createPojoNode(new char[0]);
    }

    @Override
    PojoCharArrayNode createPojoNode() {
        return this.createPojoNode(this.value());
    }

    @Override
    char[] value() {
        return new char[]{ELEMENT0, ELEMENT1};
    }

    @Override
    char[] differentValue() {
        return new char[]{ELEMENT2};
    }

    @Override
    void checkValue(final char[] expected, final char[] actual) {
        Assertions.assertArrayEquals(expected, actual);
    }

    private PojoCharArrayNode createPojoNode(final char[] values) {
        return Cast.to(PojoNode.wrap(ARRAY,
                values,
                new ReflectionPojoNodeContext()));
    }

    @Override
    List<PojoNode> children(final PojoCharArrayNode firstNode) {
        return this.children0(firstNode, ELEMENT0, ELEMENT1);
    }

    @Override
    List<PojoNode> differentChildren(final PojoCharArrayNode firstNode) {
        return this.children0(firstNode, ELEMENT0);
    }

    private List<PojoNode> children0(final PojoCharArrayNode firstNode, final Object... values) {
        final List<PojoNode> children = Lists.array();
        int i = 0;
        for (Object value : values) {
            children.add(firstNode.createNode(PojoName.index(i), value));
            i++;
        }
        return children;
    }

    @Override
    Class<PojoCharArrayNode> pojoNodeType() {
        return PojoCharArrayNode.class;
    }

    static class TestMutableParent {

        TestMutableParent(final char[] array) {
            this.setArray(array);
        }

        private char[] array;

        public char[] getArray() {
            return this.array;
        }

        public void setArray(final char[] array) {
            this.array = array;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.array);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestMutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestMutableParent other) {
            return Arrays.equals(this.array, other.array);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + Arrays.toString(this.array);
        }
    }

    static class TestImmutableParent {

        TestImmutableParent(final char[] array) {
            this.array = array;
        }

        private final char[] array;

        public char[] getArray() {
            return this.array;
        }

        public TestImmutableParent setArray(final char[] array) {
            return Arrays.equals(this.array, array) ? this : new TestImmutableParent(array);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.array);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestImmutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestImmutableParent other) {
            return Arrays.equals(this.array, other.array);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + Arrays.toString(this.array);
        }
    }
}
