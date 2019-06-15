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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public abstract class PojoCollectionNodeTestCase<N extends PojoArrayOrCollectionNode, V> extends PojoArrayOrCollectionNodeTestCase<N, V> {

    final static String STRING0 = "a0";
    final static String STRING1 = "b1";
    final static String STRING2 = "c2";

    @Test
    public final void testRemoveChild() {
        final N node = this.createPojoNode();
        final PojoNode node2 = node.setChildren(this.writableChildren(node));
        final List<PojoNode> children2 = node2.children();
        assertNotEquals(0, children2.size(), "node must have children");

        final PojoNode node3 = node2.removeChild(0);

        this.childrenCheck(node3);

        final List<PojoNode> children3 = Lists.array();
        children3.addAll(children2);
        children3.remove(0);
        this.childrenValuesCheck(node3, this.values(children3));
    }

    @Override
    final void checkValue(final V expected, final V actual) {
        assertEquals(expected, actual);
    }
}
