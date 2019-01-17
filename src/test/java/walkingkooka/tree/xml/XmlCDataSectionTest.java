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

import org.junit.Test;
import org.w3c.dom.Document;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import static org.junit.Assert.assertEquals;

public final class XmlCDataSectionTest extends XmlTextNodeTestCase<XmlCDataSection> {

    private final static String TEXT = "abc-123";

    @Test
    public void testWithEmptyText() {
        this.xmlDocument().createCDataSection("");
        ;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidTextFails() {
        this.xmlDocument().createCDataSection(XmlCDataSection.CLOSE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTextInvalidFails() {
        this.createNode().setText(XmlCDataSection.CLOSE);
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        this.toSearchNodeAndCheck(SearchNode.text(TEXT, TEXT).setName(SearchNodeName.with("CData")));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        assertEquals("<![CDATA[abc-123]]>", this.createNode().toString());
    }

    @Override
    XmlCDataSection createNode(final Document document, final String text) {
        return XmlCDataSection.with(document.createComment(text));
    }

    @Override
    XmlCDataSection createNode(final XmlDocument document, final String text) {
        return document.createCDataSection(text);
    }

    @Override
    String text() {
        return TEXT;
    }

    @Override
    Class<XmlCDataSection> nodeType() {
        return XmlCDataSection.class;
    }
}
