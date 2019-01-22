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
import walkingkooka.tree.TestNode;

import static org.junit.Assert.assertEquals;


final public class LastChildNodeSelectorTest extends
        NodeSelectorTestCase2<LastChildNodeSelector<TestNode, StringName, StringName, Object>> {

    // constants

    @Test
    public void testChildless() {
        this.acceptAndCheck(TestNode.with("childless"));
    }

    @Test
    public void testOnlyChild() {
        final TestNode child = TestNode.with("child");
        this.acceptAndCheck(TestNode.with("parent", child), child);
    }

    @Test
    public void testTwoChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        this.acceptAndCheck(TestNode.with("parent", child1, child2), child2);
    }

    @Test
    public void testManyChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        this.acceptAndCheck(TestNode.with("parent", child1, child2, child3), child3);
    }

    @Test
    public void testIgnoresDescendants() {
        final TestNode child1 = TestNode.with("child1", TestNode.with("descendant1"));
        final TestNode child2 = TestNode.with("child2", TestNode.with("descendant2"));

        this.acceptAndCheck(TestNode.with("parent", child1, child2), child2);
    }

    @Test
    public void testToString() {
        assertEquals("last-child::*", this.createSelector().toString());
    }

    @Override
    protected LastChildNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return LastChildNodeSelector.get();
    }

    @Override
    protected Class<LastChildNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(LastChildNodeSelector.class);
    }
}
