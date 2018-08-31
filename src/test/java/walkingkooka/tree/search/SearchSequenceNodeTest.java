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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchSequenceNodeTest extends SearchParentNodeTestCase<SearchSequenceNode> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        SearchSequenceNode.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        SearchSequenceNode.with(Lists.empty());
    }

    @Test
    public void testSetChildDifferent() {
        this.setChildrenDifferent(Lists.of(this.text("different-1"), this.text("different-2")));
    }

    @Test
    public void testSetChildDifferent2() {
        this.setChildrenDifferent(Lists.of(this.text("different")));
    }

    @Test
    public void testText() {
        assertEquals("child-1child-2", this.createSearchNode().text());
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchSequenceNode node = this.createSearchNode();

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
            protected Visiting startVisit(final SearchSequenceNode n) {
                assertSame(node, n);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SearchSequenceNode n) {
                assertSame(node, n);
                b.append("4");
            }

            @Override
            protected void visit(final SearchTextNode n) {
                b.append("5");
            }
        }.accept(node);

        assertEquals("1315215242", b.toString());
    }

    @Test
    public void testToString() {
        assertEquals("[ \"child-1\", \"child-2\" ]", this.createSearchNode().toString());
    }

    @Override
    SearchSequenceNode createSearchNode() {
        return SearchSequenceNode.with(this.children());
    }

    @Override
    List<SearchNode> children() {
        return Lists.of(this.child1(), this.child2());
    }

    private SearchNode child1() {
        return this.text("child-1");
    }

    private SearchNode child2() {
        return this.text("child-2");
    }

    @Override
    Class<SearchSequenceNode> searchNodeType() {
        return SearchSequenceNode.class;
    }
}
