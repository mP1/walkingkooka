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
 *
 */

package walkingkooka.tree.search;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class SearchMetaNodeTest extends SearchParentNodeTestCase<SearchMetaNode> {

    private final static String CHILD_TEXT = "child";

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        SearchMetaNode.with(null, this.attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullAttributesFails() {
        SearchMetaNode.with(this.child(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyAttributesFails() {
        SearchMetaNode.with(this.child(), SearchMetaNode.NO_ATTRIBUTES);
    }

    @Test
    public void testWith() {
        final SearchTextNode child = this.child();
        final Map<SearchNodeAttributeName, String> attributes = this.attributes();
        final SearchMetaNode node = SearchMetaNode.with(child, attributes);
        this.checkAttributes(node, attributes);
        this.checkChildren(node, Lists.of(child));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetChildrenIncorrectCountFails() {
        this.createSearchNode().setChildren(Lists.of(this.text("child-1"), this.text("child-2")));
    }

    @Test
    public void testSetDifferentChildren() {
        this.setChildrenDifferent(Lists.of(this.text("different")));
    }

    @Test
    public final void testReplaceAll() {
        final SearchMetaNode node = this.createSearchNode();
        final SearchNode replace = this.replaceNode();
        assertEquals(replace.selected(), node.replace(0, node.text().length(), replace));
    }

    @Test
    public void testReplace() {
        final SearchMetaNode node = SearchMetaNode.with(this.text("123"), this.attributes());
        final SearchTextNode replacing = this.text("REPLACEMENT");
        final SearchNode replaced = node.replace(1, 2, replacing);
        assertEquals(this.sequence(this.text("1"), replacing, this.text("3")).selected(), replaced);
    }

    @Test
    @Ignore
    public void testReplaceChild() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAppendChild() {
        this.createSearchNode().appendChild(this.text("append-fails"));
    }

    @Test
    @Ignore
    public void testAppendChild2() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveChildFirst() {
        this.createSearchNode().removeChild(0);
    }

    @Test
    @Ignore
    public void testRemoveChildLast() {
        throw new UnsupportedOperationException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAttributesEmptyFails() {
        this.createSearchNode().setAttributes(SearchMetaNode.NO_ATTRIBUTES);
    }

    @Test
    public void testSetAttributesSame() {
        final SearchNode node = this.createSearchNode();
        assertSame(node, node.setAttributes(this.attributes()));
    }

    @Test
    public void testSetAttributesDifferent() {
        final SearchMetaNode node = this.createSearchNode();

        final Map<SearchNodeAttributeName, String> differentAttributes = this.differentAttributes();
        final SearchMetaNode different = node.setAttributes(differentAttributes);
        assertNotSame(node, different);

        this.checkAttributes(different, differentAttributes);
        this.checkAttributes(node, this.attributes());
    }

    @Test
    public void testSetAttributesDifferent2() {
        final SearchSelectNode selected = this.createSearchNode().selected();
        final SearchMetaNode meta = selected.child().cast();

        final Map<SearchNodeAttributeName, String> differentAttributes = this.differentAttributes();
        final SearchMetaNode different = meta.setAttributes(differentAttributes);
        this.checkAttributes(different, differentAttributes);
    }

    @Test
    public void testText() {
        assertEquals(this.child().text(), this.createSearchNode().text());
    }

    @Test
    public void testIgnored() {
        final SearchNode node = this.createSearchNode();
        assertNotSame(node, node.ignored());
    }

    @Test
    public void testMetaSameAttributes() {
        final SearchNode node = this.createSearchNode();
        assertSame(node, node.meta(this.attributes()));
    }

    @Test
    public void testMetaDifferentAttributes() {
        final SearchMetaNode node = this.createSearchNode();

        final Map<SearchNodeAttributeName, String> attributes = this.differentAttributes();
        final SearchMetaNode different = node.meta(attributes);
        assertNotSame(different, attributes);

        this.checkAttributes(different, attributes);
        this.checkAttributes(node, this.attributes());
    }

    @Test
    public void testSelected() {
        final SearchNode node = this.createSearchNode();
        assertNotSame(node, node.selected());
    }

    @Test
    public void testQuery() {
        final SearchMetaNode node = this.createSearchNode();
        final SearchQuery query = SearchQueryValue.text(CHILD_TEXT).equalsQuery(CaseSensitivity.SENSITIVE);
        assertEquals("child selected",
                this.child().selected().meta(this.attributes()),
                query.select(node));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchMetaNode node = this.createSearchNode();
        final SearchTextNode child = node.children().get(0).cast();

        new FakeSearchNodeVisitor() {
            @Override
            protected Visiting startVisit(final SearchNode n) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SearchNode n) {
                b.append("2");
            }

            @Override
            protected Visiting startVisit(final SearchMetaNode n) {
                assertSame(node, n);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SearchMetaNode n) {
                assertSame(node, n);
                b.append("4");
            }

            @Override
            protected void visit(final SearchTextNode n) {
                assertSame(child, n);
                b.append("5");
            }
        }.accept(node);

        assertEquals("1315242", b.toString());
    }

    @Test
    public void testToString() {
        assertEquals("( \"child\" {attribute-1=attribute-value-1})", this.createSearchNode().toString());
    }

    @Override
    SearchMetaNode createSearchNode() {
        return SearchMetaNode.with(this.child(), this.attributes());
    }

    private Map<SearchNodeAttributeName, String> attributes() {
        return Maps.one(SearchNodeAttributeName.with("attribute-1"), "attribute-value-1");
    }

    private Map<SearchNodeAttributeName, String> differentAttributes() {
        return Maps.one(SearchNodeAttributeName.with("attribute-2"), "attribute-value-2");
    }

    @Override
    List<SearchNode> children() {
        return Lists.of(this.child());
    }

    private SearchTextNode child() {
        return this.text(CHILD_TEXT);
    }

    private void checkAttributes(final SearchMetaNode node, final Map<SearchNodeAttributeName, String> attributes) {
        assertEquals("attributes", attributes, node.attributes());
    }

    @Override
    Class<SearchMetaNode> searchNodeType() {
        return SearchMetaNode.class;
    }
}
