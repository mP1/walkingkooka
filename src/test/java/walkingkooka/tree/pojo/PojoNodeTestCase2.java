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
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class PojoNodeTestCase2<N extends PojoNode2, V> extends PojoNodeTestCase<N, V> {

    @Test
    public final void testChildren() {
        final N node = this.createPojoNode();
        this.childrenValuesCheck(node, this.childrenValues(node));
    }

    @Test
    public final void testSetChildrenSame() {
        final N node = this.createPojoNode();
        assertSame(node, node.setChildren(node.children()));
    }

    @Test
    public final void testSetChildrenDifferent() {
        final N node = this.createPojoNode();
        this.checkWithoutParent(node);
        final List<Object> children = node.childrenValues();

        final PojoNode node2 = node.setChildren(this.differentChildren(node));

        assertNotSame(node, node2);
        this.checkWithoutParent(node2);
        this.childrenCheck(node2);
        this.childrenValuesCheck(node2, this.differentChildrenValues(node));

        this.childrenCheck(node);
        this.childrenValuesCheck(node, children);
    }

    @Test
    public final void testSetChildrenWritableDifferent() {
        final N node = this.createPojoNode();
        this.checkWithoutParent(node);
        final List<Object> children = node.childrenValues();

        final PojoNode node2 = node.setChildren(this.writableDifferentChildren(node));

        assertNotSame(node, node2);
        this.checkWithoutParent(node2);
        this.childrenCheck(node2);
        this.childrenValuesCheck(node2, this.differentChildrenValues(node));

        this.childrenCheck(node);
        this.childrenValuesCheck(node, children);
    }

    @Test
    public final void testSetChildrenValuesNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createNode().setChildrenValues(null);
        });
    }

    @Test
    public final void testSetChildrenValuesSame() {
        final N node = this.createPojoNode();
        assertSame(node, node.setChildrenValues(this.childrenValues(node)));
    }

    @Test
    public final void testSetChildrenValuesSameWritables() {
        final N node = this.createPojoNode();
        assertSame(node, node.setChildrenValues(this.writableChildrenValues(node)));
    }

    @Test
    public final void testSetChildrenValuesDifferent() {
        final N node = this.createPojoNode();
        this.checkWithoutParent(node);
        final List<Object> childrenValues = node.childrenValues();

        final PojoNode node2 = node.setChildrenValues(this.differentChildrenValues(node));

        assertNotSame(node, node2);
        this.checkWithoutParent(node2);
        this.childrenCheck(node2);
        this.childrenValuesCheck(node2, this.differentChildrenValues(node));

        this.childrenCheck(node);
        this.childrenValuesCheck(node, childrenValues);
    }

    @Test
    public final void testSetChildrenValuesWritableDifferent() {
        final N node = this.createPojoNode();
        this.checkWithoutParent(node);
        final List<Object> childrenValues = node.childrenValues();

        final PojoNode node2 = node.setChildrenValues(this.differentWritableChildrenValues(node));

        assertNotSame(node, node2);
        this.checkWithoutParent(node2);
        this.childrenCheck(node2);
        this.childrenValuesCheck(node2, this.differentChildrenValues(node));

        this.childrenCheck(node);
        this.childrenValuesCheck(node, childrenValues);
    }

    final void childrenValuesCheck(final PojoNode node, final Object... values) {
        this.childrenValuesCheck(node, Lists.of(values));
    }

    final void childrenValuesCheck(final PojoNode node, final List<?> values) {
        assertEquals(values,
                node.childrenValues(),
                "children values not equal to childrenValues()");
        assertEquals(values,
                values(node.children()),
                "children values not equal to values from children()");
    }

    abstract List<PojoNode> writableChildren(final N firstNode);

    abstract List<PojoNode> children(final N firstNode);

    abstract List<PojoNode> writableDifferentChildren(final N firstNode);

    abstract List<PojoNode> differentChildren(final N firstNode);

    final List<Object> childrenValues(final N firstNode) {
        return this.values(this.children(firstNode));
    }

    final List<Object> writableChildrenValues(final N firstNode) {
        return this.values(this.writableChildren(firstNode));
    }

    final List<Object> differentChildrenValues(final N firstNode) {
        return this.values(this.differentChildren(firstNode));
    }

    final List<Object> differentWritableChildrenValues(final N firstNode) {
        return this.values(this.differentChildren(firstNode));
    }

    final List<Object> values(final List<PojoNode> children) {
        return children.stream()
                .map(n -> n.value())
                .collect(Collectors.toList());
    }
}
