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

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchLocalTimeNodeTest extends SearchLeafNodeTestCase<SearchLocalTimeNode, LocalTime> {

    private final static String TIME_STRING = "12:59:00";
    private final static String DIFFERENT_TIME_STRING = "06:00:00";

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final SearchLocalTimeNode node = this.createSearchNode();

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
            protected void visit(final SearchLocalTimeNode n) {
                assertSame(node, n);
                b.append("3");
            }
        }.accept(node);

        assertEquals("132", b.toString());
    }

    // ToString ...................................................................................................

    @Test
    public void testToString() {
        assertEquals(TIME_STRING, this.createSearchNode().toString());
    }

    @Test
    public void testToString2() {
        assertEquals(DIFFERENT_TIME_STRING, this.createSearchNode(LocalTime.parse(DIFFERENT_TIME_STRING)).toString());
    }

    @Test
    public void testToStringWithName() {
        assertEquals("Name123=" + DIFFERENT_TIME_STRING,
                this.createSearchNode(LocalTime.parse(DIFFERENT_TIME_STRING))
                        .setName(SearchNodeName.with("Name123"))
                        .toString());
    }

    @Override
    SearchLocalTimeNode createSearchNode(final String text, final LocalTime value) {
        return SearchLocalTimeNode.with(text, value);
    }

    @Override
    String text() {
        return this.value().toString();
    }

    @Override
    LocalTime value() {
        return LocalTime.parse(TIME_STRING);
    }

    @Override
    String differentText() {
        return this.differentValue().toString();
    }

    @Override
    LocalTime differentValue() {
        return LocalTime.parse(DIFFERENT_TIME_STRING);
    }

    @Override
    Class<SearchLocalTimeNode> searchNodeType() {
        return SearchLocalTimeNode.class;
    }
}
