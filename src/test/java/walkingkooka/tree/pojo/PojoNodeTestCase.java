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
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.NodeTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class PojoNodeTestCase<N extends PojoNode, V> implements ClassTesting2<PojoNode>,
        NodeTesting<PojoNode, PojoName, PojoNodeAttributeName, Object> {

    PojoNodeTestCase() {
        super();
    }

    @Test
    public final void testValue() {
        final N node = this.createPojoNode();
        this.checkValue(node, this.value());
    }

    @Test
    public final void testSetValueSame() {
        final V value = this.value();

        final N node = this.createPojoNode();
        assertSame(node, node.setValue(value));
        this.checkValue(node, value);
    }

    @Test
    public final void testSetValueDifferent() {
        final V value = this.value();
        final V differentValue = this.differentValue();
        final N node = this.createPojoNode();

        final PojoNode node2 = node.setValue(differentValue);
        assertNotSame(node, node2);
        this.checkValue(node2, differentValue);
        this.checkValue(node, value);
    }

    @Test
    public void testAttributes() {
        final N node = this.createPojoNode();
        assertEquals(Maps.of(PojoNodeAttributeName.CLASS, node.value().getClass().getName()), node.attributes());
    }

    @Test
    public void testSetAttributesFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNode().setAttributes(Maps.empty());
        });
    }

    @Override
    public final void testSetSameAttributes() {
        // ignore
    }

    @Override
    public final void testParentWithoutChild() {
    }

    @Override
    public final void testRemoveParent() {
    }

    @Test
    public final void testRemoveParentFails() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createNode().removeParent();
        });
    }

    @Test
    public final void testDifferentValue() {
        this.checkNotEquals(this.createPojoNode().setValue(this.differentValue()));
    }

    @Override
    public final PojoNode createNode() {
        return this.createPojoNode();
    }

    abstract N createPojoNode();

    abstract V value();

    abstract V differentValue();

    private void checkValue(final PojoNode node, final V value) {
        checkValue(value, Cast.to(node.value()));
    }

    abstract void checkValue(V expected, V actual);

    final void childrenAndCheckNames(final PojoNode node, final PojoName... properties) {
        this.childrenAndCheckNames(node, Lists.of(properties));
    }

    final void childrenAndCheckNames(final PojoNode node, final List<PojoName> properties) {
        final List<PojoNode> children = node.children();
        assertEquals(properties.size(), children.size(), () -> "children count=" + children);
        assertEquals(new TreeSet<PojoName>(properties),
                children.stream().map(n -> n.name()).collect(Collectors.toCollection(TreeSet::new)),
                () -> "properties of " + node + "=" + children);
        this.childrenCheck(node);
    }

    final PojoNode child(final PojoNode node, final PojoName name) {
        final Optional<PojoNode> property = node.children()
                .stream()
                .filter(n -> n.name().equals(name))
                .findFirst();
        assertEquals(true, property.isPresent(), "Unable to find property with name " + name + " in " + node);
        return property.get();
    }

    @Override
    public final Class<PojoNode> type() {
        return Cast.to(this.pojoNodeType());
    }

    abstract Class<N> pojoNodeType();

    @Override
    public final String typeNamePrefix() {
        return "Pojo";
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
