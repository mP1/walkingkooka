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
import walkingkooka.visit.Visiting;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SearchLocalDateNodeTest extends SearchLeafNodeTestCase<SearchLocalDateNode, LocalDate> {

    private final static int VALUE = 123;
    private final static String DATE_STRING = "2000-01-02";
    private final static String DIFFERENT_DATE_STRING = "1999-12-31";

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchLocalDateNode node = this.createSearchNode();

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
            protected void visit(final SearchLocalDateNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);

        assertEquals("132", b.toString());
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSearchNode(DATE_STRING), DATE_STRING);
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createSearchNode(LocalDate.parse(DIFFERENT_DATE_STRING)), DIFFERENT_DATE_STRING);
    }

    @Test
    public void testToStringWithName() {
        this.toStringAndCheck(
                this.createSearchNode(LocalDate.parse(DIFFERENT_DATE_STRING))
                        .setName(SearchNodeName.with("Name123")),
                "Name123=" + DIFFERENT_DATE_STRING);
    }

    private SearchLocalDateNode createSearchNode(final String value) {
        return this.createSearchNode(LocalDate.parse(value));
    }

    @Override
    SearchLocalDateNode createSearchNode(final String text, final LocalDate value) {
        return SearchLocalDateNode.with(text, value);
    }

    @Override
    String text() {
        return this.value().toString();
    }

    @Override
    LocalDate value() {
        return LocalDate.ofEpochDay(VALUE);
    }

    @Override
    String differentText() {
        return this.differentValue().toString();
    }

    @Override
    LocalDate differentValue() {
        return LocalDate.parse(DIFFERENT_DATE_STRING);
    }

    @Override
    Class<SearchLocalDateNode> searchNodeType() {
        return SearchLocalDateNode.class;
    }
}
