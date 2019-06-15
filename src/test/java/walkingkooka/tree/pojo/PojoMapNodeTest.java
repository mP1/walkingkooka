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
import walkingkooka.collect.map.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PojoMapNodeTest extends PojoCollectionNodeTestCase<PojoMapNode, Map<Object, Object>> {

    private final static PojoName MAP = PojoName.property("map");
    private final static PojoName X = PojoName.property("x");

    private final static String KEY0 = "key0";
    private final static String KEY1 = "key1";
    private final static String KEY2 = "key2";

    private final static String VALUE0 = "value0";
    private final static String VALUE1 = "value1";
    private final static String VALUE2 = "value2";

    private final static Entry<Object, Object> ENTRY0 = Maps.entry(KEY0, VALUE0);
    private final static Entry<Object, Object> ENTRY1 = Maps.entry(KEY1, VALUE1);
    private final static Entry<Object, Object> ENTRY2 = Maps.entry(KEY2, VALUE2);

    // children.......................................................................................................

    @Test
    public void testSetChildrenIncorrectIndiciesIgnored() {
        final PojoMapNode node = this.createPojoNode();
        final PojoNode node2 = node.setChildren(Lists.of(node.createNode(PojoName.index(99), ENTRY0)));
        assertNotSame(node, node2);

        this.childrenAndCheckNames(node2, INDEX0);
        this.childrenValuesCheck(node2, ENTRY0);
        this.checkWithoutParent(node);

        this.childrenAndCheckNames(node, INDEX0, INDEX1);
        this.childrenValuesCheck(node, ENTRY0, ENTRY1);
    }

    @Test
    public void testSetChildrenWithMutableParent() {
        final Map<Object, Object> map = map(ENTRY0, ENTRY1);
        final TestMutableParent parent = new TestMutableParent(map);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(MAP, map);
        final PojoNode childNode2 = childNode.setChildren(Lists.of(parentNode.createNode(PojoName.index(0), ENTRY0)));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0);
        this.childrenValuesCheck(childNode2, ENTRY0);
        this.checkWithoutParent(childNode);

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ENTRY0, ENTRY1);
    }

    @Test
    public void testSetChildrenImmutableChildWithParent() {
        final Map<Object, Object> parentMap = map(
                Maps.entry(KEY0, new TestImmutableLeaf(STRING0)),
                ENTRY1);
        final TestImmutableParent parent = new TestImmutableParent(parentMap);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // map

        final PojoNode leafNode = childNode.children()
                .get(0) //
                .children().get(1); // entry.value = TestImmutableLeaf(ENTRY2)
        final PojoNode leafNode2 = leafNode.setChildren(Lists.of(parentNode.createNode(X, STRING2)));
        assertNotSame(leafNode, leafNode2);

        final PojoNode childNode2 = leafNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                KEY0,
                new TestImmutableLeaf(STRING2));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);

        this.childrenValuesCheck(parentNode2, Lists.of(
                Maps.<Object, Object>entry(KEY0, new TestImmutableLeaf(STRING2)),
                ENTRY1));
        assertEquals(map(
                Maps.entry(KEY0, new TestImmutableLeaf(STRING2)),
                ENTRY1),
                parentNode2.value());
    }

    @Test
    public void testSetChildrenMutableChildWithParent() {
        final Map<Object, Object> parentMap = map(
                Maps.entry(KEY0, new TestMutableLeaf(STRING0)),
                ENTRY1);
        final TestMutableParent parent = new TestMutableParent(parentMap);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // map

        final PojoNode leafNode = childNode.children()
                .get(0) //
                .children().get(1); // entry.value = TestMutableLeaf(ENTRY2)
        final PojoNode leafNode2 = leafNode.setChildren(Lists.of(parentNode.createNode(X, STRING2)));
        assertSame(leafNode, leafNode2);

        final PojoNode childNode2 = leafNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                KEY0,
                new TestMutableLeaf(STRING2));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);

        this.childrenValuesCheck(parentNode2, Lists.of(
                Maps.<Object, Object>entry(KEY0, new TestMutableLeaf(STRING2)),
                ENTRY1));
        assertEquals(map(
                Maps.entry(KEY0, new TestMutableLeaf(STRING2)),
                ENTRY1),
                parentNode2.value());
    }

    // childrenValues ....................................................................................................

    @Test
    public void testSetChildrenValuesImmutableChildWithParent() {
        final Map<Object, Object> parentMap = map(
                Maps.entry(KEY0, new TestImmutableLeaf(STRING0)),
                ENTRY1);
        final TestImmutableParent parent = new TestImmutableParent(parentMap);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // map

        final PojoNode leafNode = childNode.children()
                .get(0) //
                .children().get(1); // entry.value = TestImmutableLeaf(ENTRY2)
        final PojoNode leafNode2 = leafNode.setChildrenValues(Lists.of(STRING2));
        assertNotSame(leafNode, leafNode2);

        final PojoNode childNode2 = leafNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                KEY0,
                new TestImmutableLeaf(STRING2));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);

        this.childrenValuesCheck(parentNode2, Lists.of(
                Maps.<Object, Object>entry(KEY0, new TestImmutableLeaf(STRING2)),
                ENTRY1));
        assertEquals(map(
                Maps.entry(KEY0, new TestImmutableLeaf(STRING2)),
                ENTRY1),
                parentNode2.value());
    }

    @Test
    public void testSetChildrenValuesMutableChildWithParent() {
        final Map<Object, Object> parentMap = map(
                Maps.entry(KEY0, new TestMutableLeaf(STRING0)),
                ENTRY1);
        final TestMutableParent parent = new TestMutableParent(parentMap);
        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());

        final PojoNode childNode = parentNode.children().get(0); // map

        final PojoNode leafNode = childNode.children()
                .get(0) //
                .children().get(1); // entry.value = TestMutableLeaf(ENTRY2)
        final PojoNode leafNode2 = leafNode.setChildrenValues(Lists.of(STRING2));
        assertSame(leafNode, leafNode2);

        final PojoNode childNode2 = leafNode2.parentOrFail();
        final List<Object> children2 = Lists.of(
                KEY0,
                new TestMutableLeaf(STRING2));
        this.childrenValuesCheck(childNode2, children2);

        final PojoNode parentNode2 = childNode2.parentOrFail();
        assertNotSame(parentNode, parentNode2);

        this.childrenValuesCheck(parentNode2, Lists.of(
                Maps.<Object, Object>entry(KEY0, new TestMutableLeaf(STRING2)),
                ENTRY1));
        assertEquals(map(
                Maps.entry(KEY0, new TestMutableLeaf(STRING2)),
                ENTRY1),
                parentNode2.value());
    }

    // setValue......................................................................................................

    @Test
    public void testSetValueWithMutableParent() {
        final Map<Object, Object> map = map(ENTRY0, ENTRY1);
        final TestMutableParent parent = new TestMutableParent(map);

        final PojoNode parentNode = PojoObjectNode.wrap(PARENT, parent, new ReflectionPojoNodeContext());
        final PojoNode childNode = parentNode.createNode(MAP, map);
        final PojoNode childNode2 = childNode.setValue(map(ENTRY0));
        assertNotSame(childNode, childNode2);

        this.childrenAndCheckNames(childNode2, INDEX0);
        this.childrenValuesCheck(childNode2, ENTRY0);
        this.checkWithoutParent(childNode);

        this.childrenAndCheckNames(childNode, INDEX0, INDEX1);
        this.childrenValuesCheck(childNode, ENTRY0, ENTRY1);
    }

    @SafeVarargs
    private static Map<Object, Object> map(final Entry<Object, Object>... entries) {
        final Map<Object, Object> map = Maps.ordered();
        for (Entry<Object, Object> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @Test
    public void testEqualsDifferentValues() {
        this.createPojoNode(Maps.of("KEY", "different"));
    }

    @Override
    PojoMapNode createEmptyPojoNode() {
        return this.createPojoNode(Maps.empty());
    }

    @Override
    PojoMapNode createPojoNode() {
        return this.createPojoNode(this.value());
    }

    @Override
    Map<Object, Object> value() {
        final Map<Object, Object> map = Maps.ordered();
        map.put(ENTRY0.getKey(), ENTRY0.getValue());
        map.put(ENTRY1.getKey(), ENTRY1.getValue());
        return map;
    }

    @Override
    Map<Object, Object> differentValue() {
        return Collections.singletonMap(ENTRY2.getKey(), ENTRY2.getValue());
    }

    private PojoMapNode createPojoNode(final Map<Object, Object> map) {
        return Cast.to(PojoNode.wrap(MAP,
                map,
                new ReflectionPojoNodeContext()));
    }

    @Override
    List<PojoNode> children(final PojoMapNode firstNode) {
        return this.children0(firstNode, ENTRY0, ENTRY1);
    }

    @Override
    List<PojoNode> differentChildren(final PojoMapNode firstNode) {
        return this.children0(firstNode, Maps.<Object, Object>entry("different-key", "different-value"));
    }

    private List<PojoNode> children0(final PojoMapNode firstNode, final Object... values) {
        final List<PojoNode> children = Lists.array();
        int i = 0;
        for (Object value : values) {
            children.add(firstNode.createNode(PojoName.index(i), value));
            i++;
        }
        return children;
    }

    @Override
    Class<PojoMapNode> pojoNodeType() {
        return PojoMapNode.class;
    }

    static class TestMutableParent {

        TestMutableParent(final Map<Object, Object> map) {
            this.setMap(map);
        }

        private Map<Object, Object> map;

        public Map<Object, Object> getMap() {
            return this.map;
        }

        public void setMap(final Map<Object, Object> map) {
            this.map = map;
        }

        @Override
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestMutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestMutableParent other) {
            return this.map.equals(other.map);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + this.map.toString();
        }
    }

    static class TestImmutableParent {

        TestImmutableParent(final Map<Object, Object> map) {
            this.map = map;
        }

        private final Map<Object, Object> map;

        public Map<Object, Object> getMap() {
            return this.map;
        }

        public TestImmutableParent setMap(final Map<Object, Object> map) {
            return this.map.equals(map) ? this : new TestImmutableParent(map);
        }

        @Override
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof TestImmutableParent && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestImmutableParent other) {
            return this.map.equals(other.map);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "=" + this.map.toString();
        }
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
