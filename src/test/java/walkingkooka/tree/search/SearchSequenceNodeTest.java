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
        assertEquals("abcdEFGH", this.createSearchNode().text());
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

    // extract ..............................................................................................................

    @Test
    public void testExtractFirstChild() {
        final SearchSequenceNode all = this.sequence(this.child1(), this.child2(), this.child3());

        this.extractAndCheck(all,
                0,
                4,
                all.children().get(0));
    }

    @Test
    public void testExtractMiddleChild() {
        final SearchSequenceNode all = this.sequence(this.child1(), this.child2(), this.child3());

        this.extractAndCheck(all,
                4,
                8,
                all.children().get(1));
    }

    @Test
    public void testExtractLastChild() {
        final SearchSequenceNode all = this.sequence(this.child1(), this.child2(), this.child3());

        this.extractAndCheck(all,
                8,
                12,
                all.children().get(2));
    }

    @Test
    public void testExtractPartFirstChild() {
        this.extractAndCheck(0,
                3,
                this.text("abc"));
    }

    @Test
    public void testExtractPartFirstChild2() {
        this.extractAndCheck(1,
                3,
                this.text("bc"));
    }

    @Test
    public void testExtractPartFirstChild3() {
        this.extractAndCheck(2,
                4,
                this.text("cd"));
    }

    @Test
    public void testExtractPartMiddleChild() {
        // abcd efgh ijkl
        this.extractAndCheck(5,
                7,
                this.text("FG"));
    }

    @Test
    public void testExtractSpanFirstMiddleChild() {
        // abcd efgh ijkl
        this.extractAndCheck2(2,
                6,
                this.text("cd"), this.text("EF"));
    }

    @Test
    public void testExtractSpanFirstMiddleLastChild() {
        // abcd efgh ijkl
        this.extractAndCheck2(2,
                10,
                this.text("cd"), this.child2(), this.text("ij"));
    }

    private void extractAndCheck(final int beginOffset, final int endOffset, final SearchNode expected) {
        this.extractAndCheck(this.sequence(this.child1(), this.child2(), this.child3()),
                beginOffset,
                endOffset,
                expected);
    }

    private void extractAndCheck(final SearchSequenceNode sequence, final int beginOffset, final int endOffset, final SearchNode expected) {
        assertEquals("sequence " + sequence + " begin: " + beginOffset + " end: " + endOffset,
                expected,
                sequence.extract(beginOffset, endOffset));
    }

    private void extractAndCheck2(final int beginOffset, final int endOffset, final SearchNode...expected) {
        this.extractAndCheck2(this.sequence(this.child1(), this.child2(), this.child3()),
                beginOffset,
                endOffset,
                expected);
    }

    private void extractAndCheck2(final SearchSequenceNode sequence, final int beginOffset, final int endOffset, final SearchNode...expected) {
        assertEquals("sequence " + sequence + " begin: " + beginOffset + " end: " + endOffset,
                this.sequence(expected),
                sequence.extract(beginOffset, endOffset));
    }

    // replace ..............................................................................................................

    @Test
    public void testReplaceFirstChild() {
        this.replaceAndCheck(0,
                4,
                this.replaceNode(), this.child2(), this.child3());
    }

    @Test
    public void testReplaceMiddleChild() {
        this.replaceAndCheck(4,
                8,
                this.child1(), this.replaceNode(), this.child3());
    }

    @Test
    public void testReplaceFirstAndSecondChild() {
        this.replaceAndCheck(0,
                8,
                this.replaceNode(), this.child3());
    }

    @Test
    public void testReplaceLastChild() {
        this.replaceAndCheck(8,
                12,
                this.child1(), this.child2(), this.replaceNode());
    }

    @Test
    public void testReplaceSecondAndLastChild() {
        this.replaceAndCheck(4,
                12,
                this.child1(), this.replaceNode());
    }

    @Test
    public void testReplacePartFirstPartSecondChild() {
        this.replaceAndCheck(2,
                6,
                this.text("ab"), this.replaceNode(), this.text("GH"), this.child3());
    }

    @Test
    public void testReplacePartFirstPartSecondChild2() {
        this.replaceAndCheck2(2,
                6,
                this.text("ab"), this.replaceNode(), this.text("GH"), this.child3(), this.child4());
    }

    @Test
    public void testReplacePartFirstPartLastChild() {
        this.replaceAndCheck(2,
                2 + 4 + 4,
                this.text("ab"), this.replaceNode(), this.text("kl"));
    }

    @Test
    public void testReplacePartFirstPartLastChild2() {
        this.replaceAndCheck2(1,
                1 + 4 + 4,
                this.text("a"), this.replaceNode(), this.text("jkl"), this.child4());
    }

    @Test
    public void testReplaceGrandChild() {
        this.replaceAndCheck(this.sequence(this.child1(), this.sequence(this.child2(), this.child3()), this.child4()),
                4,
                8,
                this.child1(), this.sequence(this.replaceNode(), this.child3()), this.child4());
    }

    @Test
    public void testReplaceGrandChild2() {
        this.replaceAndCheck(this.sequence(this.child1(), this.sequence(this.child2(), this.child3()), this.child4()),
                8,
                12,
                this.child1(), this.sequence(this.child2(), this.replaceNode()), this.child4());
    }

    @Test
    public void testReplaceGrandChild3() {
        // abcd EFGH ijkl MNOP
        //            ^^
        this.replaceAndCheck(this.sequence(this.child1(), this.sequence(this.child2(), this.child3()), this.child4()),
                9,
                11,
                this.child1(),
                this.sequence(this.child2(),
                        this.sequence(this.text("i"), this.replaceNode(), this.text("l"))),
                this.child4());
    }

    private void replaceAndCheck(final int beginOffset, final int endOffset, final SearchNode...children) {
        this.replaceAndCheck(this.sequence(this.child1(), this.child2(), this.child3()),
                beginOffset,
                endOffset,
                children);
    }

    private void replaceAndCheck2(final int beginOffset, final int endOffset, final SearchNode...children) {
        this.replaceAndCheck(this.sequence(this.child1(), this.child2(), this.child3(), this.child4()),
                beginOffset,
                endOffset,
                children);
    }

    private void replaceAndCheck(final SearchSequenceNode search, final int beginOffset, final int endOffset, final SearchNode...children) {
        final SearchNode replace = this.replaceNode();

        assertEquals(search + " replace " + beginOffset + "," + endOffset + " with " + replace + " failed",
                this.sequence(children),
                search.replace(beginOffset, endOffset, replace));
    }

    // replaceSelected..................................................

    @Test
    public void testReplaceSelectedNotReplaced() {
        final SearchNode node = this.text("not-replaced")
                .selected();

        this.replaceSelectedNothingAndCheck(this.sequence(node));
    }

    @Test
    public void testReplaceSelectedIgnored() {
        final SearchNode node = this.text("ignored")
                .ignored();

        this.replaceSelectedNothingAndCheck(this.sequence(node));
    }

    @Test
    public void testReplaceSelected() {
        final SearchNode node = this.text("will-be-replaced")
                .selected();
        final SearchNode replaced = this.text("replaced");

        this.replaceSelectedAndCheck(this.sequence(node),
                (n) -> replaced,
                this.sequence(replaced));
    }

    @Test
    public void testReplaceSelectedMany() {
        final SearchNode node1 = this.text("will-be-replaced-1")
                .selected();
        final SearchNode node2 = this.text("will-be-replaced-2")
                .selected();
        final SearchNode replaced = this.text("replaced");

        this.replaceSelectedAndCheck(this.sequence(node1, node2),
                (n) -> replaced,
                this.sequence(replaced, replaced));
    }

    @Test
    public void testReplaceSelectedMany2() {
        final SearchNode node1 = this.text("a1")
                .selected();
        final SearchNode node2 = this.text("b2")
                .selected();

        this.replaceSelectedAndCheck(this.sequence(node1, node2),
                (n) -> this.text(n.text() + "!!!"),
                this.sequence(this.text("a1!!!"), this.text("b2!!!")));
    }

    @Test
    public void testReplaceSelectedSomeSelected() {
        final SearchNode node1 = this.text("a1")
                .selected();
        final SearchNode node2 = this.text("b2");
        final SearchNode replaced = this.text("replaced");

        this.replaceSelectedAndCheck(this.sequence(node1, node2),
                (n) -> replaced,
                this.sequence(replaced, node2));
    }

    @Test
    public void testReplaceSelectedSomeSelectedSomeReplaced() {
        final SearchNode node1 = this.text("a1")
                .selected();
        final SearchNode node2 = this.text("b2");
        final SearchNode node3 = this.text("c3")
                .selected();
        final SearchNode replaced = this.text("replaced");

        this.replaceSelectedAndCheck(this.sequence(node1, node2, node3),
                (n) -> n.equals(node1) ? replaced : n,
                this.sequence(replaced, node2, node3));
    }

    @Test
    public void testToString() {
        assertEquals("[ \"abcd\", \"EFGH\" ]", this.createSearchNode().toString());
    }

    @Test
    public void testToStringWithName() {
        assertEquals("Name123[ \"abcd\", \"EFGH\" ]", this.createSearchNode().setName(SearchNodeName.with("Name123")).toString());
    }

    @Override
    SearchSequenceNode createSearchNode() {
        return SearchSequenceNode.with(this.children());
    }

    private SearchSequenceNode createSearchNode(final SearchNode...children) {
        return SearchSequenceNode.with(Lists.of(children));
    }

    @Override
    List<SearchNode> children() {
        return Lists.of(this.child1(), this.child2());
    }

    private SearchNode child1() {
        return this.text("abcd");
    }

    private SearchNode child2() {
        return this.text("EFGH");
    }

    private SearchNode child3() {
        return this.text("ijkl");
    }

    private SearchNode child4() {
        return this.text("MNOP");
    }

    @Override
    Class<SearchSequenceNode> searchNodeType() {
        return SearchSequenceNode.class;
    }
}
