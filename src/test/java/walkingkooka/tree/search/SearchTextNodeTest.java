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
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SearchTextNodeTest extends SearchLeafNodeTestCase<SearchTextNode, String> {

    @Test
    public void testReplace() {
        final SearchTextNode node = this.createSearchNode();
        final SearchNode replace = this.text("XYZ");

        assertEquals(this.sequence(this.text("ab"), this.text("XYZ"), this.text("ef")), node.replace(2, 4, replace));
    }

    @Test
    public void testReplace2() {
        final SearchTextNode node = this.createSearchNode();
        final SearchNode replace = this.text("XYZ");

        assertEquals(this.sequence(this.text("abc"), this.text("XYZ"), this.text("def")), node.replace(3, 3, replace));
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchTextNode node = this.createSearchNode();

        new FakeSearchNodeVisitor() {
            @Override
            protected Visiting startVisit(final SearchNode n) {
                assertSame(node, n);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SearchNode n) {
                assertSame(node, n);
                b.append("2");
            }

            @Override
            protected void visit(final SearchTextNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);

        assertEquals("132", b.toString());
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSearchNode("abc123"),
                "\"abc123\"");
    }

    @Test
    public void testToStringRequiresEscaping() {
        this.toStringAndCheck(this.createSearchNode("abc\t123"),
                "\"abc\\t123\"");
    }

    @Test
    public void testToStringWithName() {
        this.toStringAndCheck(this.createSearchNode("abc123")
                        .setName(SearchNodeName.with("Name123")),
                "Name123=\"abc123\"");
    }

    @Override
    SearchTextNode createSearchNode(final String text, final String value) {
        return SearchTextNode.with(text, value);
    }

    @Override
    String text() {
        return this.value();
    }

    @Override
    String value() {
        return "abcdef";
    }

    @Override
    String differentText() {
        return this.differentValue();
    }

    @Override
    String differentValue() {
        return "Different";
    }

    @Override
    Class<SearchTextNode> searchNodeType() {
        return SearchTextNode.class;
    }
}
