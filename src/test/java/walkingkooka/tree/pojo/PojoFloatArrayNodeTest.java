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

public final class PojoFloatArrayNodeTest extends PojoArrayNodeTestCase<PojoFloatArrayNode, float[]> {

    private final static float ELEMENT0 = 1.5f;
    private final static float ELEMENT1 = 2.5f;
    private final static float ELEMENT2 = Float.MAX_VALUE;

    // children.......................................................................................................

    @Test
    public void testSetChildrenIncorrectIndiciesIgnored() {
        final PojoFloatArrayNode node = this.createPojoNode();
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
        final float[] array = new float[]{ELEMENT0, ELEMENT1};
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
        final float[] array = new float[]{ELEMENT0, ELEMENT1};
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
        final float[] array = new float[]{ELEMENT0, ELEMENT1};
        final TestImmutableParent parent = new TestImmutableParent(array);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(ARRAY, array);
        final PojoNode parentNode2 = parentNode.setChildren(Lists.of(childNode));
        assertSame(parentNode, parentNode2);

        final PojoNode childNode2 = parentNode2.children()
                .get(0)
                .setValue(new float[]{ELEMENT0, ELEMENT1, ELEMENT2});
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0, INDEX1, INDEX2);
        this.childrenValuesCheck(childNode2, ELEMENT0, ELEMENT1, ELEMENT2);
        this.checkWithParent(childNode2);

        assertNotSame(parentNode, childNode2.parentOrFail());

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ELEMENT0, ELEMENT1);
    }

    @Override
    PojoFloatArrayNode createEmptyPojoNode() {
        return this.createPojoNode(new float[0]);
    }

    @Override
    PojoFloatArrayNode createPojoNode() {
        return this.createPojoNode(this.value());
    }

    @Override
    float[] value() {
        return new float[]{ELEMENT0, ELEMENT1};
    }

    @Override
    float[] differentValue() {
        return new float[]{ELEMENT2};
    }

    @Override
    void checkValue(final float[] expected, final float[] actual) {
        Assertions.assertArrayEquals(expected, actual, 0.1f);
    }

    private PojoFloatArrayNode createPojoNode(final float[] values) {
        return Cast.to(PojoNode.wrap(ARRAY,
                values,
                new ReflectionPojoNodeContext()));
    }

    @Override
    List<PojoNode> children(final PojoFloatArrayNode firstNode) {
        return this.children0(firstNode, ELEMENT0, ELEMENT1);
    }

    @Override
    List<PojoNode> differentChildren(final PojoFloatArrayNode firstNode) {
        return this.children0(firstNode, ELEMENT0);
    }

    private List<PojoNode> children0(final PojoFloatArrayNode firstNode, final Object... values) {
        final List<PojoNode> children = Lists.array();
        int i = 0;
        for (Object value : values) {
            children.add(firstNode.createNode(PojoName.index(i), value));
            i++;
        }
        return children;
    }

    @Override
    Class<PojoFloatArrayNode> pojoNodeType() {
        return PojoFloatArrayNode.class;
    }

    static class TestMutableParent {

        TestMutableParent(final float[] array) {
            this.setArray(array);
        }

        private float[] array;

        public float[] getArray() {
            return this.array;
        }

        public void setArray(final float[] array) {
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

        TestImmutableParent(final float[] array) {
            this.array = array;
        }

        private final float[] array;

        public float[] getArray() {
            return this.array;
        }

        public TestImmutableParent setArray(final float[] array) {
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
