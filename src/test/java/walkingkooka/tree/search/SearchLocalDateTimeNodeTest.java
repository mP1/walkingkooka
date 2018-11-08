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

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchLocalDateTimeNodeTest extends SearchLeafNodeTestCase<SearchLocalDateTimeNode, LocalDateTime> {

    private final static String DATETIMESTRING = "2000-01-31T12:59:00";
    private final static String DIFFERENT_DATETIME_STRING = "1999-12-31T12:58:00";

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchLocalDateTimeNode node = this.createSearchNode();

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
            protected void visit(final SearchLocalDateTimeNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);

        assertEquals("132", b.toString());
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        assertEquals(DATETIMESTRING, this.createSearchNode().toString());
    }

    @Test
    public void testToString2() {
        assertEquals(DIFFERENT_DATETIME_STRING, this.createSearchNode(DIFFERENT_DATETIME_STRING).toString());
    }

    @Test
    public void testToStringWithName() {
        assertEquals("Name123=" +DIFFERENT_DATETIME_STRING,
                this.createSearchNode(DIFFERENT_DATETIME_STRING)
                        .setName(SearchNodeName.with("Name123"))
                        .toString());
    }

    private SearchLocalDateTimeNode createSearchNode(final String value) {
        return this.createSearchNode(LocalDateTime.parse(value));
    }

    @Override
    SearchLocalDateTimeNode createSearchNode(final String text, final LocalDateTime value) {
        return SearchLocalDateTimeNode.with(text, value);
    }

    @Override
    String text() {
        return this.value().toString();
    }

    @Override
    LocalDateTime value() {
        return LocalDateTime.parse(DATETIMESTRING);
    }

    @Override
    String differentText() {
        return this.differentValue().toString();
    }

    @Override
    LocalDateTime differentValue() {
        return LocalDateTime.parse(DIFFERENT_DATETIME_STRING);
    }

    @Override
    Class<SearchLocalDateTimeNode> searchNodeType() {
        return SearchLocalDateTimeNode.class;
    }
}
