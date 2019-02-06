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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SearchIgnoredNodeTest extends SearchParentNodeTestCase<SearchIgnoredNode> {

    private final static String CHILD_TEXT = "child";

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            SearchIgnoredNode.with(null);
        });
    }

    @Test
    public void testWithDoesntWrapIgnored() {
        final SearchIgnoredNode node = this.createSearchNode();
        assertSame(node, SearchIgnoredNode.with(node));
    }

    @Test
    public void testSetChildrenIncorrectCountFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createSearchNode().setChildren(Lists.of(this.text("child-1"), this.text("child-2")));
        });
    }

    @Test
    public void testSetChildrenWithSameIgnored() {
        final SearchIgnoredNode node = this.createSearchNode();
        assertSame(node, node.setChildren(Lists.of(node.child().ignored())));
    }

    @Test
    public void testSetChildrenWithDifferentIgnoredChild() {
        final SearchIgnoredNode node = this.createSearchNode();
        final SearchNode child = this.differentSearchNode();
        assertEquals(child.ignored(), node.setChildren(Lists.of(child.ignored())));
    }

    @Test
    public void testSetDifferentChildren() {
        this.setChildrenDifferent(Lists.of(this.text("different")));
    }

    @Test
    public final void testReplaceAll() {
        final SearchIgnoredNode node = this.createSearchNode();
        final SearchNode replace = this.replaceNode();
        assertEquals(replace.selected(), node.replace(0, node.text().length(), replace));
    }

    @Test
    public void testReplace() {
        final SearchIgnoredNode node = SearchIgnoredNode.with(this.text("123"));
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

    @Test
    public void testText() {
        assertEquals(this.child().text(), this.createSearchNode().text());
    }

    @Test
    public void testIgnored() {
        final SearchNode node = this.createSearchNode();
        assertSame(node, node.ignored());
    }

    @Test
    public void testSelected() {
        final SearchNode node = this.createSearchNode();
        assertNotSame(node, node.selected());
    }

    @Test
    public void testReplaceSelected() {
        final SearchNode node = this.text("selected-child-text")
                .selected()
                .ignored();
        this.replaceSelectedWithoutSelectedAndCheck(node);
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchIgnoredNode node = this.createSearchNode();
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
            protected Visiting startVisit(final SearchIgnoredNode n) {
                assertSame(node, n);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SearchIgnoredNode n) {
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
    public void testQueryIgnores() {
        final SearchTextQueryValue queryValue = SearchQueryValue.text(CHILD_TEXT);
        final SearchIgnoredNode node = this.createSearchNode();
        final SearchQuery query = queryValue.equalsQuery(CaseSensitivity.SENSITIVE);
        assertSame(node, query.select(node));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSearchNode(), "<! \"child\" !>");
    }

    @Test
    public void testToStringWithName() {
        this.toStringAndCheck(this.createSearchNode().setName(SearchNodeName.with("Name123")), "Name123<! \"child\" !>");
    }

    @Override
    SearchIgnoredNode createSearchNode() {
        return SearchIgnoredNode.with(this.child());
    }

    @Override
    List<SearchNode> children() {
        return Lists.of(this.child());
    }

    private SearchNode child() {
        return this.text(CHILD_TEXT);
    }

    @Override
    Class<SearchIgnoredNode> searchNodeType() {
        return SearchIgnoredNode.class;
    }
}
