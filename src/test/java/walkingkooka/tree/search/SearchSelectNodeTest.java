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
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchSelectNodeTest extends SearchParentNodeTestCase<SearchSelectNode> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        SearchSelectNode.with(null);
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

    @Test
    public void testSelected() {
        final SearchNode node = this.createSearchNode();
        assertSame(node, node.selected());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchSelectNode node = this.createSearchNode();
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
            protected Visiting startVisit(final SearchSelectNode n) {
                assertSame(node, n);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SearchSelectNode n) {
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
        assertEquals("< \"child\" >", this.createSearchNode().toString());
    }

    @Override
    SearchSelectNode createSearchNode() {
        return SearchSelectNode.with(this.child());
    }

    @Override
    List<SearchNode> children() {
        return Lists.of(this.child());
    }

    private SearchNode child() {
        return this.text("child");
    }

    @Override
    Class<SearchSelectNode> searchNodeType() {
        return SearchSelectNode.class;
    }
}
