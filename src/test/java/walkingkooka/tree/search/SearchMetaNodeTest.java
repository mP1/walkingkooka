/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.search;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SearchMetaNodeTest extends SearchParentNodeTestCase<SearchMetaNode> {

    private final static String CHILD_TEXT = "child";

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            SearchMetaNode.with(null, this.attributes());
        });
    }

    @Test
    public void testWithNullAttributesFails() {
        assertThrows(NullPointerException.class, () -> {
            SearchMetaNode.with(this.child(), null);
        });
    }

    @Test
    public void testWithEmptyAttributesFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            SearchMetaNode.with(this.child(), SearchMetaNode.NO_ATTRIBUTES);
        });
    }

    @Test
    public void testWith() {
        final SearchTextNode child = this.child();
        final Map<SearchNodeAttributeName, String> attributes = this.attributes();
        final SearchMetaNode node = SearchMetaNode.with(child, attributes);
        this.checkAttributes(node, attributes);
        this.checkChildren(node, Lists.of(child));
    }

    @Test
    public void testSetChildrenIncorrectCountFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createSearchNode().setChildren(Lists.of(this.text("child-1"), this.text("child-2")));
        });
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

    @Override
    public void testReplaceChild() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testAppendChild() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createSearchNode().appendChild(this.text("append-fails"));
        });
    }

    @Override
    public void testAppendChild2() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testRemoveChildFirst() {
        assertThrows(UnsupportedOperationException.class, () -> {
            this.createSearchNode().removeChild(0);
        });
    }

    @Override
    public void testRemoveChildLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParentWithoutChild() {
        throw new UnsupportedOperationException();
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
        final SearchNode different = node.setAttributes(differentAttributes);
        assertNotSame(node, different);

        this.checkAttributes(different, differentAttributes);
        this.checkAttributes(node, this.attributes());
    }

    @Test
    public void testSetAttributesDifferent2() {
        final SearchSelectNode selected = this.createSearchNode().selected();
        final SearchMetaNode meta = selected.child().cast();

        final Map<SearchNodeAttributeName, String> differentAttributes = this.differentAttributes();
        final SearchNode different = meta.setAttributes(differentAttributes);
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
        assertSame(node, node.setAttributes(this.attributes()));
    }

    @Test
    public void testMetaDifferentAttributes() {
        final SearchMetaNode node = this.createSearchNode();

        final Map<SearchNodeAttributeName, String> attributes = this.differentAttributes();
        final SearchNode different = node.setAttributes(attributes);
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
    public void testReplaceSelectedWithout() {
        this.replaceSelectedWithoutSelectedAndCheck(this.createSearchNode());
    }

    @Test
    public void testReplaceSelectedNothingReplaced() {
        final SearchNode node = this.text("will-be-replaced")
                .selected()
                .setAttributes(this.attributes());

        this.replaceSelectedNothingAndCheck(node);
    }

    @Test
    public void testReplaceSelected() {
        final SearchNode node = this.text("will-be-replaced")
                .selected()
                .setAttributes(this.attributes());
        final SearchNode replaced = this.text("replaced");

        this.replaceSelectedAndCheck(node,
                (n) -> replaced,
                replaced.setAttributes(this.attributes()));
    }

    @Test
    public void testQuery() {
        final SearchMetaNode node = this.createSearchNode();
        final SearchQuery query = SearchQueryValue.text(CHILD_TEXT).equalsQuery(CaseSensitivity.SENSITIVE);
        assertEquals(this.child().selected().setAttributes(this.attributes()),
                query.select(node),
                "child selected");
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
        this.toStringAndCheck(this.createSearchNode(),
                "( \"child\" {attribute-1=attribute-value-1})");
    }

    @Test
    public void testToStringWithName() {
        this.toStringAndCheck(this.createSearchNode().setName(SearchNodeName.with("Name123")),
                "Name123( \"child\" {attribute-1=attribute-value-1})");
    }

    @Override
    SearchMetaNode createSearchNode() {
        return SearchMetaNode.with(this.child(), this.attributes());
    }

    private Map<SearchNodeAttributeName, String> attributes() {
        return Maps.of(SearchNodeAttributeName.with("attribute-1"), "attribute-value-1");
    }

    private Map<SearchNodeAttributeName, String> differentAttributes() {
        return Maps.of(SearchNodeAttributeName.with("attribute-2"), "attribute-value-2");
    }

    @Override
    List<SearchNode> children() {
        return Lists.of(this.child());
    }

    private SearchTextNode child() {
        return this.text(CHILD_TEXT);
    }

    private void checkAttributes(final SearchNode node, final Map<SearchNodeAttributeName, String> attributes) {
        assertEquals(attributes, node.attributes(), "attributes");
    }

    @Override
    Class<SearchMetaNode> searchNodeType() {
        return SearchMetaNode.class;
    }
}
