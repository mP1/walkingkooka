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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

public final class PojoBasicNodeTest extends PojoNodeTestCase<PojoBasicNode, Integer> {

    @Test
    public void testNull() {
        final PojoBasicNode node = this.createPojoNode(null);
        assertEquals(Maps.empty(), node.attributes());
    }

    @Test
    public void testParent() {
        this.parentAbsentCheck(this.createNode());
    }

    @Test
    public void testChildren() {
        this.childrenOf(123);
    }

    @Test
    public void testChildrenOfNull() {
        this.childrenOf(null);
    }

    @Test
    public void testChildrenOfString() {
        this.childrenOf("abc123");
    }

    private void childrenOf(final Object value){
        final PojoBasicNode node = this.createPojoNode(value);
        this.childrenAndCheckNames(node);
        this.parentAbsentCheck(node);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildrenSetFails() {
        this.createNode().setChildren(Lists.array());
    }

    @Test
    public void testToString() {
        assertEquals("123", this.createNode().toString());
    }

    @Override
    PojoBasicNode createPojoNode() {
          return this.createPojoNode(this.value());
    }

    @Override
    Integer value() {
        return 123;
    }

    @Override
    Integer differentValue() {
        return 456;
    }

    @Override
    void checkValue(final Integer expected, final Integer actual) {
        Assert.assertEquals(expected, actual);
    }

    private PojoBasicNode createPojoNode(final Object value) {
        return Cast.to(PojoNode.wrap(PojoName.property("root"),
                value,
                new ReflectionPojoNodeContext()));
    }

    @Override
    Class<PojoBasicNode> pojoNodeType() {
        return PojoBasicNode.class;
    }
}
