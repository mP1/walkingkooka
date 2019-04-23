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

package walkingkooka.tree.select;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


final public class IndexedChildNodeSelectorTest extends
        NodeSelectorTestCase2<IndexedChildNodeSelector<TestNode, StringName, StringName, Object>> {

    // constants

    private final static int INDEX = 2;

    @Test
    public void testNegativeIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            IndexedChildNodeSelector.with(-1);
        });
    }

    @Test
    public void testZeroIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            IndexedChildNodeSelector.with(0);
        });
    }

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestNode.with("childless"));
    }

    @Test
    public void testFewerChild() {
        final TestNode child = TestNode.with("child");
        this.acceptAndCheck(TestNode.with("parent", child));
    }

    @Test
    public void testChildPresent() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        this.acceptAndCheck(TestNode.with("parent", child1, child2), child2);
    }

    @Test
    public void testExtraChildrenIgnored() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        this.acceptAndCheck(TestNode.with("parent", child1, child2, child3), child2);
    }

    @Test
    public void testEqualsDifferentIndex() {
        this.createSelector(INDEX + 1, this.wrapped());
    }

    @Test
    public void testToString() {
        assertEquals(2, INDEX, "INDEX");
        this.toStringAndCheck(this.createSelector(), "*[2]");
    }

    @Override
    IndexedChildNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return IndexedChildNodeSelector.with(INDEX);
    }

    @Override
    public Class<IndexedChildNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(IndexedChildNodeSelector.class);
    }

    private IndexedChildNodeSelector<TestNode, StringName, StringName, Object> createSelector(final int index,
                                                                                              final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(IndexedChildNodeSelector.<TestNode, StringName, StringName, Object>with(index).append(selector));
    }
}
