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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

final public class SelfNodeSelectorTest
        extends NonLogicalNodeSelectorTestCase<SelfNodeSelector<TestNode, StringName, StringName, Object>> {

    @Test
    public void testSelf() {
        final TestNode node = TestNode.with("self");
        this.acceptAndCheck(node, node);
    }

    @Test
    public void testSelfAndDescendant() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(selfAndDescendant(),
                parent,
                child);
    }

    @Test
    public void testSelfAndDescendant2() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(selfAndDescendant(),
                parent.child(0));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> selfAndDescendant() {
        return Cast.to(SelfNodeSelector.get()
                .append(DescendantNodeSelector.get()));
    }

    @Test
    public void testSelfAndNamed() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(selfAndNamed(),
                parent);
    }

    @Test
    public void testSelfAndNamed2() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck(selfAndNamed(),
                parent.child(0),
                child);
    }

    @Test
    public void testMap() {
        final TestNode parent = TestNode.with("parent", TestNode.with("child"));

        TestNode.clear();

        this.acceptMapAndCheck(parent,
                TestNode.with("parent*0", TestNode.with("child")));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> selfAndNamed() {
        return Cast.to(SelfNodeSelector.get()
                .append(NamedNodeSelector.with(Names.string("child"))));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), ".");
    }

    @Override
    SelfNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return SelfNodeSelector.get();
    }

    @Override
    public Class<SelfNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(SelfNodeSelector.class);
    }
}
