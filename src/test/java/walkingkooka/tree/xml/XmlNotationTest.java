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

package walkingkooka.tree.xml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ResourceTesting;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import javax.xml.parsers.DocumentBuilder;
import java.io.Reader;

public final class XmlNotationTest extends XmlLeafNodeTestCase<XmlNotation>
        implements ResourceTesting {

    @Override
    public void testParentWith() {
        // n/a
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(this.createNode(this.documentBuilder(), "testEqualsDifferent.xml"));
    }

    private XmlNotation createNode(final DocumentBuilder builder, final String file) {
        try (Reader reader = this.resourceAsReader(this.getClass(), this.getClass().getSimpleName() + "/" + file)) {
            final XmlDocument root = XmlNode.fromXml(builder, reader);
            return XmlNotation.with(root.documentNode().getDoctype().getNotations().item(0));
        } catch (final Exception rethrow) {
            throw new Error(rethrow);
        }
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        final XmlNotation notation = this.createNode();
        final String zipViewer = "zip viewer";

        this.toSearchNodeAndCheck(notation,
                SearchNode.sequence(Lists.of(
                        SearchNode.text(zipViewer, zipViewer)
                )).setName(SearchNodeName.with("Notation")));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createNode(), "<!NOTATION PUBLIC \"zip viewer\">");
    }

    // helpers............................................................................................

    @Override
    XmlNotation createNode(final Document document) {
        try (Reader reader = this.resourceAsReader(this.getClass(), this.getClass().getSimpleName() + "/default.xml")) {
            final XmlDocument root = XmlNode.fromXml(this.documentBuilder(), reader);
            return XmlNotation.with(root.documentNode().getDoctype().getNotations().item(0));
        } catch (final Exception rethrow) {
            throw new Error(rethrow);
        }
    }

    @Override
    String text() {
        return "";
    }

    @Override
    Class<XmlNotation> nodeType() {
        return XmlNotation.class;
    }
}
