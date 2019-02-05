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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Names;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodeSelectorBuilderTest extends BuilderTestCase<NodeSelectorBuilder<TestNode, StringName, StringName, Object>,
        NodeSelector<TestNode, StringName, StringName, Object>>
        implements NodeSelectorTesting<TestNode, StringName, StringName, Object> {

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

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testBuildNothingFails() {
        assertThrows(BuilderException.class, () -> {
            this.relative().build();
        });
    }

    @Test
    public void testAbsoluteNullClassFails() {
        assertThrows(NullPointerException.class, () -> {
            NodeSelectorBuilder.absolute(null, this.separator());
        });
    }

    @Test
    public void testAbsoluteNullSeparatorFails() {
        assertThrows(NullPointerException.class, () -> {
            NodeSelectorBuilder.absolute(TestNode.class, null);
        });
    }

    @Test
    public void testRelativeNullClassFails() {
        assertThrows(NullPointerException.class, () -> {
            NodeSelectorBuilder.relative(null, this.separator());
        });
    }

    @Test
    public void testRelativeNullSeparatorFails() {
        assertThrows(NullPointerException.class, () -> {
            NodeSelectorBuilder.relative(TestNode.class, null);
        });
    }
    
    @Test
    public void testNamedNotSelected() {
        final TestNode root = TestNode.with(ROOT);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .named(Names.string("not-found"));

        this.acceptAndCheck(b, root);
    }

    @Test
    public void testNamedSelected() {
        final TestNode root = TestNode.with(ROOT);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .named(Names.string(ROOT));

        this.acceptAndCheck(b, root, root);
    }

    @Test
    public void testAncestorOrSelf() {
        final TestNode grandChild1 = TestNode.with(GRAND_CHILD1);
        final TestNode grandChild2 = TestNode.with(GRAND_CHILD2);
        final TestNode grandChild3 = TestNode.with(GRAND_CHILD3);

        final TestNode child1 = TestNode.with(CHILD1, grandChild1);
        final TestNode child2 = TestNode.with(CHILD2);
        final TestNode child3 = TestNode.with(CHILD3, grandChild2, grandChild3);

        final TestNode root = TestNode.with(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b = this.relative()
                .ancestorOrSelf();

        this.acceptAndCheck(b, root.child(2), child3, root);
    }

    @Test
    public void testAttributeEndsWith() {
        final StringName a = Names.string("a");

        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(a, "xyz"));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(a, "qrs"));
        final TestNode child3 = TestNode.with(CHILD3).setAttributes(Maps.one(Names.string("b"), "abc"));

        final TestNode root = TestNode.with(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b = this.absolute()
                .descendant()
                .attributeValueEndsWith(a, "z");

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testAttributeStartsWith() {
        final StringName a = Names.string("a");

        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(a, "xyz"));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(a, "qrs"));
        final TestNode child3 = TestNode.with(CHILD3).setAttributes(Maps.one(Names.string("b"), "abc"));

        final TestNode root = TestNode.with(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b = this.absolute()
                .descendant()
                .attributeValueStartsWith(a, "x");

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testDescendantOrSelf() {
        final TestNode grandChild1 = TestNode.with(GRAND_CHILD1);
        final TestNode grandChild2 = TestNode.with(GRAND_CHILD2);
        final TestNode grandChild3 = TestNode.with(GRAND_CHILD3);

        final TestNode child1 = TestNode.with(CHILD1, grandChild1);
        final TestNode child2 = TestNode.with(CHILD2);
        final TestNode child3 = TestNode.with(CHILD3, grandChild2, grandChild3);

        final TestNode root = TestNode.with(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b = this.relative()
                .descendantOrSelf();

        this.acceptAndCheck(b, root.child(2), child3, grandChild2, grandChild3);
    }

    @Test
    public void testFirstChild() {
        final TestNode child1 = TestNode.with(CHILD1);
        final TestNode child2 = TestNode.with(CHILD2);
        final TestNode child3 = TestNode.with(CHILD3);

        final TestNode root = TestNode.with(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b = this.absolute()
                .firstChild();

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testLastChild() {
        final TestNode child1 = TestNode.with(CHILD1);
        final TestNode child2 = TestNode.with(CHILD2);
        final TestNode child3 = TestNode.with(CHILD3);

        final TestNode root = TestNode.with(ROOT, child1, child2, child3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b = this.absolute()
                .lastChild();

        this.acceptAndCheck(b, root, child3);
    }

    @Test
    public void testDeepPathName() {
        final TestNode child = TestNode.with("child");
        final TestNode parent = TestNode.with(PARENT, child);
        final TestNode root = TestNode.with(ROOT, parent);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string("child"));

        this.acceptAndCheck(b, root, child);
    }

    @Test
    public void testNameAndAttribute() {
        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "different"));
        final TestNode parent = TestNode.with(PARENT, child1, child2);
        
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(CHILD1))
                .attributeValueEquals(ATTRIBUTE, VALUE);

        this.acceptAndCheck(b, parent, child1);
    }

    @Test
    public void testDeepPathNameAndAttribute() {
        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "different"));
        final TestNode parent = TestNode.with(PARENT, child1, child2);
        final TestNode root = TestNode.with(ROOT, parent);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(CHILD1))
                .attributeValueEquals(ATTRIBUTE, VALUE);

        this.acceptAndCheck(b, root, child1);
    }

    @Test
    public void testMultipleAttribute() {
        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "different"));
        final TestNode child3 = TestNode.with(CHILD3).setAttributes(Maps.one(ATTRIBUTE, VALUE));
        final TestNode parent = TestNode.with(PARENT, child1, child2, child3);
        final TestNode root = TestNode.with(ROOT, parent);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .attributeValueEquals(ATTRIBUTE, VALUE);

        this.acceptAndCheck(b, root, child1, child3);
    }

    @Test
    public void testOr() {
        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(ATTRIBUTE, "123"));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "456"));
        final TestNode parent1 = TestNode.with("parent1", child1, child2);
        final TestNode parent2 = TestNode.with("parent2").setAttributes(Maps.one(ATTRIBUTE, "789"));
        final TestNode root = TestNode.with(ROOT, parent1, parent2);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .or(this.relative().attributeValueEquals(ATTRIBUTE, "123").build(),
                        this.relative().attributeValueEquals(ATTRIBUTE, "789").build());

        this.acceptAndCheck(b, root, child1, parent2);
    }

    @Test
    public void testAnd() {
        final TestNode child1 = TestNode.with(CHILD1).setAttributes(Maps.one(ATTRIBUTE, "123"));
        final TestNode child2 = TestNode.with(CHILD2).setAttributes(Maps.one(ATTRIBUTE, "222"));
        final TestNode parent1 = TestNode.with("parent1", child1, child2);
        final TestNode parent2 = TestNode.with("parent2").setAttributes(Maps.one(ATTRIBUTE, "111"));
        final TestNode parent3 = TestNode.with("parent3").setAttributes(Maps.one(ATTRIBUTE, "12345"));
        final TestNode root = TestNode.with(ROOT, parent1, parent2, parent3);

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .and(this.relative().attributeValueContains(ATTRIBUTE, "1").build(),
                        this.relative().attributeValueContains(ATTRIBUTE, "2").build());

        this.acceptAndCheck(b, root, child1, parent3);
    }

    @Test
    public void testDeepPathNameAndSelf() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .self();

        this.acceptTreeAndCheck(b, PARENT);
    }

    @Test
    public void testDeepPathNameAndParent() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .parent();

        this.acceptTreeAndCheck(b, GRAND_PARENT);
    }

    @Test
    public void testDeepPathNameAndAncestor() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .ancestor();

        this.acceptTreeAndCheck(b, GRAND_PARENT, ROOT);
    }

    @Test
    public void testDeepPathNameAndChildren() {

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .children();

        this.acceptTreeAndCheck(b, CHILD1, CHILD2, CHILD3);
    }

    @Test
    public void testDeepPathNameAndChild() {

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .child(2);

        this.acceptTreeAndCheck(b, CHILD2);
    }

    @Test
    public void testDeepPathNameAndPrecedingSibling() {

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .precedingSibling();

        this.acceptTreeAndCheck(b, PARENT_SIBLING_BEFORE);
    }

    @Test
    public void testDeepPathNameAndFollowingSibling() {

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .followingSibling();

        this.acceptTreeAndCheck(b, PARENT_SIBLING_AFTER);
    }

    @Test
    public void testDeepPathNameAndDescendants() {

        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute()
                .descendant()
                .named(Names.string(PARENT))
                .descendant();

        this.acceptTreeAndCheck(b, CHILD1, GRAND_CHILD, CHILD2, CHILD3);
    }

    private void acceptTreeAndCheck(final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b, final String...nodes) {
        final TestNode parentSiblingBefore = TestNode.with(PARENT_SIBLING_BEFORE);
        final TestNode child1 = TestNode.with(CHILD1, TestNode.with(GRAND_CHILD));
        final TestNode parent = TestNode.with(PARENT, child1, TestNode.with(CHILD2), TestNode.with(CHILD3));
        final TestNode parentSiblingAfter = TestNode.with(PARENT_SIBLING_AFTER);
        final TestNode grandParent = TestNode.with(GRAND_PARENT, parentSiblingBefore, parent, parentSiblingAfter);

        this.acceptAndCheck0(b, TestNode.with(ROOT, grandParent), nodes);
    }

    private void acceptAndCheck(final NodeSelectorBuilder<TestNode, StringName, StringName, Object> builder,
                                final TestNode start,
                                final TestNode... nodes) {
        this.acceptAndCheck0(builder,
                start,
                Arrays.stream(nodes)
                        .map( n  -> n.name().value())
                        .toArray(size  -> new String[ size]));
    }

    private void acceptAndCheck0(final NodeSelectorBuilder<TestNode, StringName, StringName, Object> builder,
                                          final TestNode start,
                                          final String... nodes) {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = builder.build();

        final List<String> selectedNames =
                this.selectorAcceptAndCollect(start, selector)
                .stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals(Lists.of(nodes),
                selectedNames
                , "Selector.accept=" + selector + "\n" + start);
    }

    @Override
    public void testSelectorSelf() {
        // ignore
    }

    @Override
    public void testSelectorPotentialFails() {
        // ignore
    }

    @Test
    public void testToStringAbsoluteNodeAndAttributeSelector() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute();
        b.named(Names.string(PARENT))
                .attributeValueContains(Names.string("attribute-name"), "attribute-value");

        this.buildAndCheck2(b,"/parent[contains(@\"attribute-name\",\"attribute-value\")]");
    }

    @Test
    public void testToStringAbsoluteManySelectors() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute();
        b.absolute()
                .precedingSibling()
                .self()
                .followingSibling();

        this.buildAndCheck2(b,"/preceding-sibling::*/./following-sibling::*");
    }

    @Test
    public void testToStringAbsoluteManySelectors2() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute();
        b.absolute()
                .preceding()
                .self()
                .following();

        this.buildAndCheck2(b, "/preceding::*/./following::*");
    }

    @Test
    public void testToStringAbsoluteManySelectors3() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.absolute();
        b.preceding()
                .self()
                .following();

        this.buildAndCheck2(b, "/preceding::*/./following::*");
    }

    @Test
    public void testToStringRelativeManySelectors3() {
        final NodeSelectorBuilder<TestNode, StringName, StringName, Object> b= this.relative();
        b.preceding()
                .self()
                .following();

        this.buildAndCheck2(b, "preceding::*/./following::*");
    }

    @Override
    protected NodeSelectorBuilder<TestNode, StringName, StringName, Object> createBuilder() {
        return this.absolute();
    }

    private NodeSelectorBuilder<TestNode, StringName, StringName, Object> absolute() {
        return this.separator().absoluteNodeSelectorBuilder(TestNode.class);
    }

    private NodeSelectorBuilder<TestNode, StringName, StringName, Object> relative() {
        return this.separator().relativeNodeSelectorBuilder(TestNode.class);
    }

    private PathSeparator separator() {
        return PathSeparator.requiredAtStart('/');
    }

    @Override
    protected Class<NodeSelector<TestNode, StringName, StringName, Object>> builderProductType() {
        return Cast.to(NodeSelector.class);
    }

    @Override
    protected Class<NodeSelectorBuilder> type() {
        return Cast.to(NodeSelectorBuilder.class);
    }

    // NodeSelectorTesting

    @Override
    public TestNode createNode() {
        throw new UnsupportedOperationException();
    }
}
