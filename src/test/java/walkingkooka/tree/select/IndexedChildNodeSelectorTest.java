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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

import static org.junit.Assert.assertEquals;


final public class IndexedChildNodeSelectorTest extends
        NodeSelectorTestCase2<IndexedChildNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    // constants

    private final static int INDEX = 2;

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndexFails() {
        IndexedChildNodeSelector.with(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroIndexFails() {
        IndexedChildNodeSelector.with(0);
    }

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestFakeNode.node("childless"));
    }

    @Test
    public void testFewerChild() {
        final TestFakeNode child = TestFakeNode.node("child");
        this.acceptAndCheck(TestFakeNode.node("parent", child));
    }

    @Test
    public void testChildPresent() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");
        this.acceptAndCheck(TestFakeNode.node("parent", child1, child2), child2);
    }

    @Test
    public void testExtraChildrenIgnored() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");
        final TestFakeNode child3 = TestFakeNode.node("child3");

        this.acceptAndCheck(TestFakeNode.node("parent", child1, child2, child3), child2);
    }

    @Test
    public void testToString() {
        assertEquals("INDEX", 2, IndexedChildNodeSelectorTest.INDEX);
        assertEquals("[2]", this.createSelector().toString());
    }

    @Override
    protected IndexedChildNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return IndexedChildNodeSelector.with(IndexedChildNodeSelectorTest.INDEX);
    }

    @Override
    protected Class<IndexedChildNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(IndexedChildNodeSelector.class);
    }
}
