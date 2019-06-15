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

import static org.junit.jupiter.api.Assertions.assertNotSame;

public abstract class PojoArrayOrCollectionNodeTestCase<N extends PojoArrayOrCollectionNode, V> extends PojoNodeTestCase2<N, V> {

    final static PojoName PARENT = PojoName.property("parent");

    final static PojoName INDEX0 = PojoName.index(0);
    final static PojoName INDEX1 = PojoName.index(1);
    final static PojoName INDEX2 = PojoName.index(2);

    @Test
    public final void testChildrenEmpty() {
        final N node = this.createEmptyPojoNode();

        this.checkWithoutParent(node);
        this.childrenAndCheckNames(node);
    }

    @Test
    public final void testSetChildrenEmpty() {
        final N node = this.createPojoNode();
        final List<PojoNode> children = node.children();
        final List<Object> values = node.childrenValues();

        final PojoNode node2 = node.setChildren(Lists.empty());
        assertNotSame(node, node2);

        this.childrenAndCheckNames(node2);
        this.childrenValuesCheck(node2);
        this.checkWithoutParent(node);

        this.childrenAndCheckNames(node, children.stream().map(n -> n.name()).collect(Collectors.toList()));
        this.childrenValuesCheck(node, values.toArray());
    }

    @Test
    public final void testSetChildrenValuesEmpty() {
        final N node = this.createPojoNode();
        final List<PojoNode> children = node.children();
        final List<Object> values = node.childrenValues();

        final PojoNode node2 = node.setChildrenValues(Lists.empty());
        assertNotSame(node, node2);

        this.childrenAndCheckNames(node2);
        this.childrenValuesCheck(node2);
        this.checkWithoutParent(node);

        this.childrenAndCheckNames(node, children.stream().map(n -> n.name()).collect(Collectors.toList()));
        this.childrenValuesCheck(node, values.toArray());
    }

    abstract N createEmptyPojoNode();

    @Override
    final List<PojoNode> writableChildren(final N firstNode) {
        return this.children(firstNode);
    }

    @Override
    final List<PojoNode> writableDifferentChildren(final N firstNode) {
        return this.differentChildren(firstNode);
    }
}
