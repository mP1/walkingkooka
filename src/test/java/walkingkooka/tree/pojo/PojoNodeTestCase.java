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
package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.NodeTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public abstract class PojoNodeTestCase<N extends PojoNode, V> extends NodeTestCase<PojoNode, PojoName, PojoNodeAttributeName, Object> {

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
        assertEquals(Maps.one(PojoNodeAttributeName.CLASS, node.value().getClass().getName()), node.attributes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAttributesFails() {
        this.createNode().setAttributes(Maps.empty());
    }

    @Test
    public final void testDifferentValue() {
        this.checkNotEquals(this.createPojoNode().setValue(this.differentValue()));
    }

    @Override
    protected final PojoNode createNode() {
        return this.createPojoNode();
    }

    abstract N createPojoNode();

    abstract V value();

    abstract V differentValue();

    private void checkValue(final PojoNode node, final V value) {
        checkValue((V)value, Cast.to(node.value()));
    }

    abstract void checkValue(V expected, V actual);

    final void parentAbsentCheck(final PojoNode node){
        assertEquals("parent of " + node, PojoNode.NO_PARENT, node.parent());
    }

    final void parentPresentCheck(final PojoNode node){
        assertNotEquals("parent missing for " + node, PojoNode.NO_PARENT, node.parent());
    }

    final void childrenAndCheckNames(final PojoNode node, final PojoName...properties) {
        this.childrenAndCheckNames(node, Lists.of(properties));
    }

    final void childrenAndCheckNames(final PojoNode node, final List<PojoName> properties) {
        final List<PojoNode> children = node.children();
        assertEquals("children count=" + children, properties.size(), children.size());
        assertEquals("properties of " + node + "=" + children,
                new TreeSet<PojoName>(properties),
                children.stream().map(n -> n.name()).collect(Collectors.toCollection(TreeSet::new)));
        this.childrenCheck(node);
    }

    final PojoNode child(final PojoNode node, final PojoName name) {
        final Optional<PojoNode> property = node.children()
                .stream()
                .filter(n -> n.name().equals(name))
                .findFirst();
        assertEquals("Unable to find property with name " + name + " in " + node, true, property.isPresent());
        return property.get();
    }

    @Override
    protected final Class<PojoNode> type() {
        return Cast.to(this.pojoNodeType());
    }

    abstract Class<N> pojoNodeType();

    @Override
    protected final String requiredNamePrefix() {
        return "Pojo";
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
