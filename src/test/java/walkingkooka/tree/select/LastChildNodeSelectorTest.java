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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;


final public class LastChildNodeSelectorTest extends
        NodeSelectorTestCase4<LastChildNodeSelector<TestNode, StringName, StringName, Object>> {

    // constants

    @Test
    public void testLastChildChildless() {
        this.acceptAndCheck(TestNode.with("childless"));
    }

    @Test
    public void testLastChildOnlyChild() {
        final TestNode child = TestNode.with("child");
        this.acceptAndCheck(TestNode.with("parent", child), child);
    }

    @Test
    public void testLastChildTwoChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        this.acceptAndCheck(TestNode.with("parent", child1, child2), child2);
    }

    @Test
    public void testLastChildManyChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode child3 = TestNode.with("child3");

        this.acceptAndCheck(TestNode.with("parent", child1, child2, child3), child3);
    }

    @Test
    public void testLastChildIgnoresDescendants() {
        final TestNode child1 = TestNode.with("child1", TestNode.with("descendant1"));
        final TestNode child2 = TestNode.with("child2", TestNode.with("descendant2"));

        this.acceptAndCheck(TestNode.with("parent", child1, child2), child2);
    }

    @Test
    public void testDescendantOrSelfLastChild() {
        final TestNode grand1 = TestNode.with("grand1");
        final TestNode grand2 = TestNode.with("grand2");
        final TestNode child1 = TestNode.with("child1", grand1, grand2);

        final TestNode grand3 = TestNode.with("grand3");
        final TestNode grand4 = TestNode.with("grand4");
        final TestNode grand5 = TestNode.with("grand5");
        final TestNode child2 = TestNode.with("child2", grand3, grand4, grand5);

        this.acceptAndCheck(TestNode.absoluteNodeSelector().descendantOrSelf().lastChild(),
                TestNode.with("parent", child1, child2),
                child2, grand2, grand5);
    }

    @Test
    public void testLastChildMap() {
        this.acceptMapAndCheck(TestNode.with("parent"));
    }

    @Test
    public void testLastChildMap2() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3"));

        this.acceptMapAndCheck(parent,
                parent.setChild(2, TestNode.with("child3*0")));
    }

    @Test
    public void testLastChildMap3() {
        final TestNode grand = TestNode.with("grand-parent",
                TestNode.with("parent1",
                        TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3")),
                TestNode.with("parent2", TestNode.with("child4")));

        TestNode.clear();

        this.acceptMapAndCheck(grand.child(0),
                TestNode.with("grand-parent",
                        TestNode.with("parent1",
                                TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3*0")),
                        TestNode.with("parent2", TestNode.with("child4")))
                        .child(0));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), "last-child::*");
    }

    @Override
    LastChildNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return LastChildNodeSelector.get();
    }

    @Override
    public Class<LastChildNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(LastChildNodeSelector.class);
    }
}
