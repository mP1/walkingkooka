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

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class NamedNodeSelectorTest extends
        NonTerminalNodeSelectorTestCase<NamedNodeSelector<TestNode, StringName, StringName, Object>> {

    private final static StringName NAME = Names.string("never");

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            NamedNodeSelector.with(null);
        });
    }

    @Test
    public void testRootDifferentName() {
        this.acceptAndCheck(TestNode.with("root"));
    }

    @Test
    public void testNamedRoot() {
        final TestNode node = TestNode.with("root");
        this.acceptAndCheck2(node.name(), node, node);
    }

    @Test
    public void testNamedIgnoresParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck2(parent.name(), child);
    }

    @Test
    public void testNamedChild() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck2(child.name(), parent);
    }

    @Test
    public void testNameChildIgnoresGrandChildren() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck2(grandChild.name(), parent);
    }

    @Test
    public void testNameChildIgnoresGrandChildren2() {
        final TestNode grandChild = TestNode.with("grandChild");
        final TestNode child = TestNode.with("child", grandChild);
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck2(child.name(), child, child);
    }

    @Test
    public void testNamedIgnoresSiblings() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.acceptAndCheck2(child2.name(), parent.child(0));
    }

    @Test
    public void testNamedIgnoresPreviousSiblings() {
        TestNode.disableUniqueNameChecks();

        final TestNode prev = TestNode.with("previous");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", prev, self, following);

        this.acceptAndCheck2(prev.name(), parent.child(1));
    }

    @Test
    public void testNamedIgnoresFollowingSiblings() {
        final TestNode prev = TestNode.with("previous");
        final TestNode self = TestNode.with("self");
        final TestNode following = TestNode.with("following");
        final TestNode parent = TestNode.with("parent", prev, self, following);

        this.acceptAndCheck2(following.name(), parent.child(1));
    }

    @Test
    public void testNamedAllAncestorsFromParent() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child);

        this.acceptAndCheck2(parent.name(), child);
    }

    @Test
    public void testChildrenNameFilters() {
        TestNode.disableUniqueNameChecks();

        final TestNode child1 = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child1, TestNode.with("skip"));

        this.acceptAndCheck(TestNode.absoluteNodeSelector().children().named(child1.name()),
                parent,
                child1);
    }

    @Test
    public void testChildrenNameFilters2() {
        TestNode.disableUniqueNameChecks();

        final TestNode child2 = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", TestNode.with("skip"), child2);

        this.acceptAndCheck(TestNode.absoluteNodeSelector().children().named(child2.name()),
                parent,
                child2);
    }

    @Test
    public void testChildrenNameFilters3() {
        TestNode.disableUniqueNameChecks();

        final TestNode child1 = TestNode.with("child").setAttributes(attributes("1", 1));
        final TestNode child2 = TestNode.with("child");
        final TestNode parent = TestNode.with("parent", child1, TestNode.with("skip"), child2);

        this.acceptAndCheck(TestNode.absoluteNodeSelector().children().named(child2.name()),
                parent,
                child1, child2);
    }

    @Test
    public void testChildrenNamedMap() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode parent = TestNode.with("parent", child1, TestNode.with("child2"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.relativeNodeSelector().named(child1.name()),
                parent.child(0),
                TestNode.with("parent", TestNode.with(child1.name().value() + "*0"), TestNode.with("child"))
                        .child(0));
    }

    @Test
    public void testAbsoluteChildrenNamedMap() {
        final TestNode child = TestNode.with("child1", TestNode.with("grandChild"));
        final TestNode parent = TestNode.with("parent", child, TestNode.with("child2"));

        TestNode.clear();

        this.acceptMapAndCheck(TestNode.absoluteNodeSelector().children().named(child.name()),
                parent,
                TestNode.with("parent",
                        TestNode.with("child1*0", TestNode.with("grandChild")),
                        TestNode.with("child2")));
    }

    @Test
    public void testAbsoluteChildrenNamedMapSeveral() {
        TestNode.disableUniqueNameChecks();

        final TestNode child1 = TestNode.with("child", TestNode.with("grandChild1"));
        final TestNode child2 = TestNode.with("child", TestNode.with("grandChild2"));
        final TestNode parent = TestNode.with("parent", child1, TestNode.with("skip"), child2);

        this.acceptMapAndCheck(TestNode.absoluteNodeSelector().children().named(child1.name()),
                parent,
                TestNode.with("parent",
                        TestNode.with("child*0", TestNode.with("grandChild1")),
                        TestNode.with("skip"),
                        TestNode.with("child*1", TestNode.with("grandChild2"))));
    }

    @Test
    public void testMapUnmatched() {
        this.acceptMapAndCheck(TestNode.with("different"));
    }

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(this.createSelector(Names.string("different-name-2"), this.wrapped()));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), NAME.value());
    }

    @Test
    public void testToStringNamedAndPredicate() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .named(Names.string("ABC"))
                        .attributeValueStartsWith(Names.string("DEF"), "V1"),
                "ABC[starts-with(@\"DEF\",\"V1\")]");
    }

    @Test
    public void testToStringChildrenNamed() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("ABC")),
                "child::ABC");
    }

    @Test
    public void testToStringChildrenNamedAndPredicate() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("ABC"))
                        .attributeValueStartsWith(Names.string("DEF"), "V1"),
                "child::ABC[starts-with(@\"DEF\",\"V1\")]");
    }

    @Test
    public void testToStringNamedChildrenNamed() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .named(Names.string("ABC"))
                        .children()
                        .named(Names.string("DEF")),
                "ABC/child::DEF");
    }

    @Test
    public void testToStringChildrenNamedChildrenNamed() {
        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .children()
                        .named(Names.string("ABC"))
                        .children()
                        .named(Names.string("DEF")),
                "child::ABC/child::DEF");
    }

    @Override
    NamedNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return createSelector(NAME);
    }

    private NamedNodeSelector<TestNode, StringName, StringName, Object> createSelector(final StringName name) {
        return NamedNodeSelector.with(name);
    }

    final void acceptAndCheck2(final String childName,
                               final TestNode start,
                               final TestNode... nodes) {
        this.acceptAndCheck2(Names.string(childName), start, nodes);
    }

    final void acceptAndCheck2(final TestNode name,
                               final TestNode start,
                               final TestNode... nodes) {
        this.acceptAndCheck2(name.name(), start, nodes);
    }

    final void acceptAndCheck2(final StringName childName,
                               final TestNode start,
                               final TestNode... nodes) {
        this.acceptAndCheck(this.createSelector(childName), start, nodes);
    }

    @Override
    public Class<NamedNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NamedNodeSelector.class);
    }

    private NamedNodeSelector<TestNode, StringName, StringName, Object> createSelector(final StringName name,
                                                                                       final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        return Cast.to(NamedNodeSelector.<TestNode, StringName, StringName, Object>with(name).append(selector));
    }
}
