/*
 *
 *  * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  
 */

package walkingkooka.tree.select;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

import static org.junit.Assert.assertEquals;


final public class LastChildNodeSelectorTest extends
        NodeSelectorTestCase2<LastChildNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    // constants

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestFakeNode.node("childless"));
    }

    @Test
    public void testOnlyChild() {
        final TestFakeNode child = TestFakeNode.node("child");
        this.acceptAndCheck(TestFakeNode.node("parent", child), child);
    }

    @Test
    public void testTwoChildren() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");
        this.acceptAndCheck(TestFakeNode.node("parent", child1, child2), child2);
    }

    @Test
    public void testManyChildren() {
        final TestFakeNode child1 = TestFakeNode.node("child1");
        final TestFakeNode child2 = TestFakeNode.node("child2");
        final TestFakeNode child3 = TestFakeNode.node("child3");

        this.acceptAndCheck(TestFakeNode.node("parent", child1, child2, child3), child3);
    }

    @Test
    public void testIgnoresDescendants() {
        final TestFakeNode child1 = TestFakeNode.node("child1", TestFakeNode.node("descendant1"));
        final TestFakeNode child2 = TestFakeNode.node("child2", TestFakeNode.node("descendant2"));

        this.acceptAndCheck(TestFakeNode.node("parent", child1, child2), child2);
    }

    @Test
    public void testToString() {
        assertEquals("last-child::*", this.createSelector().toString());
    }

    @Override
    protected LastChildNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return LastChildNodeSelector.get();
    }

    @Override
    protected Class<LastChildNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(LastChildNodeSelector.class);
    }
}
