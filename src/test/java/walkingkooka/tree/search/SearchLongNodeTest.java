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
import walkingkooka.tree.visit.Visiting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchLongNodeTest extends SearchLeafNodeTestCase<SearchLongNode, Long> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchLongNode node = this.createSearchNode();

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
            protected void visit(final SearchLongNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);

        assertEquals("132", b.toString());
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        assertEquals("1", this.createSearchNode(1L).toString());
    }

    @Test
    public void testToString2() {
        assertEquals("234", this.createSearchNode(234L).toString());
    }

    private SearchLongNode createSearchNode(final long value) {
        return this.createSearchNode(Long.valueOf(value));
    }

    @Override
    SearchLongNode createSearchNode(final String text, final Long value) {
        return SearchLongNode.with(text, value);
    }

    @Override
    String text() {
        return this.value().toString();
    }

    @Override
    Long value() {
        return 12345L;
    }

    @Override
    String differentText() {
        return this.differentValue().toString();
    }

    @Override
    Long differentValue() {
        return 999L;
    }

    @Override
    Class<SearchLongNode> searchNodeType() {
        return SearchLongNode.class;
    }
}
