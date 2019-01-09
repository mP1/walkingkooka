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

import org.junit.Before;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Names;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public final class NodeSelectorBuilderTest extends BuilderTestCase<NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object>, NodeSelector<TestFakeNode, StringName, StringName, Object>> {

    private final static StringName ATTRIBUTE = Names.string("attribute");
    private final static String VALUE = "*VALUE*";

    private static final String ROOT = "root";
    private static final String GRAND_PARENT = "grandParent";
    private static final String PARENT_SIBLING_BEFORE = "parentSiblingBefore";
    private static final String PARENT = "parent";
    private static final String CHILD1 = "child1";
    private static final String CHILD2 = "child2";
    private static final String CHILD3 = "child3";
    private static final String GRAND_CHILD = "grand-child";
    private static final String GRAND_CHILD1 = "grand-child1";
    private static final String GRAND_CHILD2 = "grand-child2";
    private static final String GRAND_CHILD3 = "grand-child3";
    private static final String PARENT_SIBLING_AFTER = "parentSiblingAfter";

    @Before
    public void beforeEachTest() {
        TestFakeNode.names.clear();
    }

    @Test(expected = BuilderException.class)
    public void testBuildNothingFails() {
        this.relative().build();
    }

    @Test(expected = NullPointerException.class)
    public void testAbsoluteNullClassFails() {
        NodeSelectorBuilder.absolute(null, this.separator());
    }

    @Test(expected = NullPointerException.class)
    public void testAbsoluteNullSeparatorFails() {
        NodeSelectorBuilder.absolute(TestFakeNode.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRelativeNullClassFails() {
        NodeSelectorBuilder.relative(null, this.separator());
    }

    @Test(expected = NullPointerException.class)
    public void testRelativeNullSeparatorFails() {
        NodeSelectorBuilder.relative(TestFakeNode.class, null);
    }
    
    @Test
    public void testNamedNotSelected() {
        final TestFakeNode root = TestFakeNode.node(ROOT);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .named(Names.string("not-found"));

        this.acceptAndCheck(b, root);
    }

    @Test
    public void testNamedSelected() {
        final TestFakeNode root = TestFakeNode.node(ROOT);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .named(Names.string(ROOT));

        this.acceptAndCheck(b, root, root);
    }

    @Test
    public void testAncestorOrSelf() {
        final TestFakeNode grandChild1 = TestFakeNode.node(GRAND_CHILD1);
        final TestFakeNode grandChild2 = TestFakeNode.node(GRAND_CHILD2);
        final TestFakeNode grandChild3 = TestFakeNode.node(GRAND_CHILD3);

        final TestFakeNode child1 = TestFakeNode.node(CHILD1, grandChild1);
        final TestFakeNode child2 = TestFakeNode.node(CHILD2);
        final TestFakeNode child3 = TestFakeNode.node(CHILD3, grandChild2, grandChild3);

        final TestFakeNode root = TestFakeNode.node(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b = this.relative()
                .ancestorOrSelf();

        this.acceptAndCheck(b, root.child(2), child3, root);
    }

    @Test
    public void testAttributeEndsWith() {
        final StringName a = Names.string("a");

        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(a, "xyz"));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(a, "qrs"));
        final TestFakeNode child3 = TestFakeNode.node(CHILD3).setAttributes(Maps.one(Names.string("b"), "abc"));

        final TestFakeNode root = TestFakeNode.node(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b = this.absolute()
                .descendant()
                .attributeValueEndsWith(a, "z");

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testAttributeStartsWith() {
        final StringName a = Names.string("a");

        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(a, "xyz"));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(a, "qrs"));
        final TestFakeNode child3 = TestFakeNode.node(CHILD3).setAttributes(Maps.one(Names.string("b"), "abc"));

        final TestFakeNode root = TestFakeNode.node(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b = this.absolute()
                .descendant()
                .attributeValueStartsWith(a, "x");

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testDescendantOrSelf() {
        final TestFakeNode grandChild1 = TestFakeNode.node(GRAND_CHILD1);
        final TestFakeNode grandChild2 = TestFakeNode.node(GRAND_CHILD2);
        final TestFakeNode grandChild3 = TestFakeNode.node(GRAND_CHILD3);

        final TestFakeNode child1 = TestFakeNode.node(CHILD1, grandChild1);
        final TestFakeNode child2 = TestFakeNode.node(CHILD2);
        final TestFakeNode child3 = TestFakeNode.node(CHILD3, grandChild2, grandChild3);

        final TestFakeNode root = TestFakeNode.node(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b = this.relative()
                .descendantOrSelf();

        this.acceptAndCheck(b, root.child(2), child3, grandChild2, grandChild3);
    }

    @Test
    public void testFirstChild() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1);
        final TestFakeNode child2 = TestFakeNode.node(CHILD2);
        final TestFakeNode child3 = TestFakeNode.node(CHILD3);

        final TestFakeNode root = TestFakeNode.node(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b = this.absolute()
                .firstChild();

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testLastChild() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1);
        final TestFakeNode child2 = TestFakeNode.node(CHILD2);
        final TestFakeNode child3 = TestFakeNode.node(CHILD3);

        final TestFakeNode root = TestFakeNode.node(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b = this.absolute()
                .lastChild();

        this.acceptAndCheck(b, root, child3);
    }

    @Test
    public void testDeepPathName() {
        final TestFakeNode child = TestFakeNode.node("child");
        final TestFakeNode parent = TestFakeNode.node(PARENT, child);
        final TestFakeNode root = TestFakeNode.node(ROOT, parent);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string("child"));

        this.acceptAndCheck(b, root, child);
    }

    @Test
    public void testNameAndAttribute() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "different"));
        final TestFakeNode parent = TestFakeNode.node(PARENT, child1, child2);
        
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(CHILD1))
                .attributeValueEquals(ATTRIBUTE, VALUE);

        this.acceptAndCheck(b, parent, child1);
    }

    @Test
    public void testDeepPathNameAndAttribute() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "different"));
        final TestFakeNode parent = TestFakeNode.node(PARENT, child1, child2);
        final TestFakeNode root = TestFakeNode.node(ROOT, parent);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(CHILD1))
                .attributeValueEquals(ATTRIBUTE, VALUE);

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testMultipleAttribute() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "different"));
        final TestFakeNode child3 = TestFakeNode.node(CHILD3).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestFakeNode parent = TestFakeNode.node(PARENT, child1, child2, child3);
        final TestFakeNode root = TestFakeNode.node(ROOT, parent);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .attributeValueEquals(ATTRIBUTE, VALUE);

        this.acceptAndCheck(b, root, child1, child3);
    }

    @Test
    public void testOr() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(ATTRIBUTE, "123"));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "456"));
        final TestFakeNode parent1 = TestFakeNode.node("parent1", child1, child2);
        final TestFakeNode parent2 = TestFakeNode.node("parent2").setAttributes(Maps.one(ATTRIBUTE, "789"));
        final TestFakeNode root = TestFakeNode.node(ROOT, parent1, parent2);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .or(this.relative().attributeValueEquals(ATTRIBUTE, "123").build(),
                        this.relative().attributeValueEquals(ATTRIBUTE, "789").build());

        this.acceptAndCheck(b, root, child1, parent2);
    }

    @Test
    public void testAnd() {
        final TestFakeNode child1 = TestFakeNode.node(CHILD1).setAttributes(Maps.one(ATTRIBUTE, "123"));
        final TestFakeNode child2 = TestFakeNode.node(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "222"));
        final TestFakeNode parent1 = TestFakeNode.node("parent1", child1, child2);
        final TestFakeNode parent2 = TestFakeNode.node("parent2").setAttributes(Maps.one(ATTRIBUTE, "111"));
        final TestFakeNode parent3 = TestFakeNode.node("parent3").setAttributes(Maps.one(ATTRIBUTE, "12345"));
        final TestFakeNode root = TestFakeNode.node(ROOT, parent1, parent2, parent3);

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .and(this.relative().attributeValueContains(ATTRIBUTE, "1").build(),
                        this.relative().attributeValueContains(ATTRIBUTE, "2").build());

        this.acceptAndCheck(b, root, child1, parent3);
    }

    @Test
    public void testDeepPathNameAndSelf() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .self();

        this.acceptTreeAndCheck(b, PARENT);
    }

    @Test
    public void testDeepPathNameAndParent() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .parent();

        this.acceptTreeAndCheck(b, GRAND_PARENT);
    }

    @Test
    public void testDeepPathNameAndAncestor() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .ancestor();

        this.acceptTreeAndCheck(b, GRAND_PARENT, ROOT);
    }

    @Test
    public void testDeepPathNameAndChildren() {

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .children();

        this.acceptTreeAndCheck(b, CHILD1, CHILD2, CHILD3);
    }

    @Test
    public void testDeepPathNameAndChild() {

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .child(2);

        this.acceptTreeAndCheck(b, CHILD2);
    }

    @Test
    public void testDeepPathNameAndPrecedingSibling() {

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .precedingSibling();

        this.acceptTreeAndCheck(b, PARENT_SIBLING_BEFORE);
    }

    @Test
    public void testDeepPathNameAndFollowingSibling() {

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .followingSibling();

        this.acceptTreeAndCheck(b, PARENT_SIBLING_AFTER);
    }

    @Test
    public void testDeepPathNameAndDescendants() {

        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .descendant();

        this.acceptTreeAndCheck(b, CHILD1, GRAND_CHILD, CHILD2, CHILD3);
    }

    private void acceptTreeAndCheck(final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b, final String...nodes) {
        final TestFakeNode parentSiblingBefore = TestFakeNode.node(PARENT_SIBLING_BEFORE);
        final TestFakeNode child1 = TestFakeNode.node(CHILD1, TestFakeNode.node(GRAND_CHILD));
        final TestFakeNode parent = TestFakeNode.node(PARENT, child1, TestFakeNode.node(CHILD2), TestFakeNode.node(CHILD3));
        final TestFakeNode parentSiblingAfter = TestFakeNode.node(PARENT_SIBLING_AFTER);
        final TestFakeNode grandParent = TestFakeNode.node(GRAND_PARENT, parentSiblingBefore, parent, parentSiblingAfter);

        this.acceptAndCheck0(b, TestFakeNode.node(ROOT, grandParent), nodes);
    }

    private void acceptAndCheck(final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> builder,
                                final TestFakeNode start,
                                final TestFakeNode... nodes) {
        this.acceptAndCheck0(builder,
                start,
                Arrays.stream(nodes)
                        .map( n  -> n.name().value())
                        .toArray(size  -> new String[ size]));
    }

    private void acceptAndCheck0(final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> builder,
                                          final TestFakeNode start,
                                          final String... nodes) {
        final NodeSelector<TestFakeNode, StringName, StringName, Object> selector = builder.build();

        final Set<TestFakeNode> selected = Sets.ordered();
        selector.accept(start, new FakeNodeSelectorContext<TestFakeNode, StringName, StringName, Object>(){
            @Override
            public void potential(final TestFakeNode node) {
                // ignore
            }

            @Override
            public void selected(final TestFakeNode node) {
                selected.add(node);
            }
        });
        final List<String> selectedNames = selected.stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals("Selector.accept=" + selector + "\n" + start, Lists.of(nodes), selectedNames);
    }

    @Test
    public void testToStringAbsoluteNodeAndAttributeSelector() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute();
        b.named(Names.string(PARENT))
                .attributeValueContains(Names.string("attribute-name"), "attribute-value");

        this.buildAndCheck2(b,"/parent[contains(@\"attribute-name\",\"attribute-value\")]");
    }

    @Test
    public void testToStringAbsoluteManySelectors() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute();
        b.absolute()
                .precedingSibling()
                .self()
                .followingSibling();

        this.buildAndCheck2(b,"/preceding-sibling::*/./following-sibling::*");
    }

    @Test
    public void testToStringAbsoluteManySelectors2() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute();
        b.absolute()
                .preceding()
                .self()
                .following();

        this.buildAndCheck2(b, "/preceding::*/./following::*");
    }

    @Test
    public void testToStringAbsoluteManySelectors3() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.absolute();
        b.preceding()
                .self()
                .following();

        this.buildAndCheck2(b, "/preceding::*/./following::*");
    }

    @Test
    public void testToStringRelativeManySelectors3() {
        final NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> b= this.relative();
        b.preceding()
                .self()
                .following();

        this.buildAndCheck2(b, "preceding::*/./following::*");
    }

    @Override
    protected NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> createBuilder() {
        return this.absolute();
    }

    private NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> absolute() {
        return this.separator().absoluteNodeSelectorBuilder(TestFakeNode.class);
    }

    private NodeSelectorBuilder<TestFakeNode, StringName, StringName, Object> relative() {
        return this.separator().relativeNodeSelectorBuilder(TestFakeNode.class);
    }

    private PathSeparator separator() {
        return PathSeparator.requiredAtStart('/');
    }

    @Override
    protected Class<NodeSelector<TestFakeNode, StringName, StringName, Object>> builderProductType() {
        return Cast.to(NodeSelector.class);
    }

    @Override
    protected Class<NodeSelectorBuilder> type() {
        return Cast.to(NodeSelectorBuilder.class);
    }
}
