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
 */

package walkingkooka.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.EntityReference;
import walkingkooka.Cast;

import javax.xml.parsers.DocumentBuilder;

public final class DomEntityReferenceTest extends DomParentNodeTestCase<DomEntityReference> {

    private final static String REFERENCE = "kooka";

    @Test
    public void testToString() {
        assertEquals("&kooka;", this.createNode().toString());
    }

    @Test
    public void testToStringWithinDocument() throws Exception {
        final org.w3c.dom.Document document = Cast.to(DomNode.fromXml(
                this.documentBuilder(false, false),
                this.resource())
                .node);
        final org.w3c.dom.Element root = document.getDocumentElement();
        final org.w3c.dom.EntityReference reference = Cast.to(root.getFirstChild());
        assertEquals("&kooka;", new DomEntityReference(reference).toString());
    }

    @Test
    public void testW3cEntityReference() {
        final Document document = this.documentBuilder().newDocument();
        final EntityReference reference = document.createEntityReference(REFERENCE);
        assertEquals("name", REFERENCE, reference.getNodeName());
        assertEquals("text", "", reference.getTextContent());
    }

    // helpers............................................................................................

    @Override
    DomEntityReference createNode(final Document document) {
        return new DomEntityReference(document.createEntityReference(REFERENCE));
    }

    @Override
    DomEntityReference createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    String text() {
        return "";
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomEntityReference.class);
    }
}
