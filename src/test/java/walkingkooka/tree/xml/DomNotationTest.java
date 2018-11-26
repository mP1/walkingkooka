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

package walkingkooka.tree.xml;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.io.Reader;

import static org.junit.Assert.assertEquals;

public final class DomNotationTest extends DomLeafNodeTestCase<DomNotation> {

    @Test
    @Ignore
    public void testParentWith() {
        // n/a
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        final DomNotation notation = this.createNode();
        final String zipViewer = "zip viewer";

        this.toSearchNodeAndCheck(notation,
                SearchNode.sequence(Lists.of(
                        SearchNode.text(zipViewer, zipViewer)
                )).setName(SearchNodeName.with("Notation")));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        assertEquals("<!NOTATION PUBLIC \"zip viewer\">", this.createNode().toString());
    }

    // helpers............................................................................................

    @Override
    DomNotation createNode(final Document document) {
        try (Reader reader = this.resourceAsReader(this.getClass(), this.getClass().getSimpleName() + "/createNode.xml")) {
            final DomDocument root = DomNode.fromXml(this.documentBuilder(), reader);
            return new DomNotation(root.documentNode().getDoctype().getNotations().item(0));
        } catch (final Exception rethrow) {
            throw new RuntimeException(rethrow);
        }
    }

    @Override
    String text() {
        return "";
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomNotation.class);
    }
}
