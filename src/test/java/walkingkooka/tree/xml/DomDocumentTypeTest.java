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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import javax.xml.parsers.DocumentBuilder;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class DomDocumentTypeTest extends DomLeafNodeTestCase<DomDocumentType> {

    private final static String TYPE = "type123";
    private final static String PUBLIC_ID_STRING = "//-/publicId";
    private final static Optional<DomPublicId> PUBLIC_ID = DomNode.publicId(PUBLIC_ID_STRING);
    private final static String SYSTEM_ID_STRING = "http://www.example.com/test.dtd";
    private final static Optional<DomSystemId> SYSTEM_ID = DomNode.systemId(SYSTEM_ID_STRING);

    // create..............................................................................................

    @Test
    public void testWithPublicId() {
        final DomDocumentType node = this.createNode(TYPE, PUBLIC_ID_STRING, SYSTEM_ID_STRING);
        this.checkPublicId(node, PUBLIC_ID);
        this.checkSystemId(node, SYSTEM_ID);
        this.checkName(node, TYPE);
    }

    @Test
    public void testWithSystemId() {
        final DomDocumentType node = this.createNode(TYPE, null, SYSTEM_ID_STRING);
        this.checkPublicId(node, DomNode.NO_PUBLIC_ID);
        this.checkSystemId(node, SYSTEM_ID);
        this.checkName(node, TYPE);
    }

    @Test(expected = DomException.class)
    public void testAppendChild() {
        final DomDocumentType node = this.createNode(TYPE, null, SYSTEM_ID_STRING);
        final DomText text = node.createText("text123");
        node.appendChild(text);
    }

    // parent..............................................................................................

    public void testParentWith() {
        // na
    }

    // publicId....................................................................................................

    @Test
    public void testPublicId() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkPublicId(type, "-//example/");
        this.checkSystemId(type, "http://www.example.com/test.dtd");
    }

    // systemId ...................................................................................................

    @Test
    public void testSystemId() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkSystemId(type, "/system.dtd");
        this.checkPublicId(type, DomNode.NO_PUBLIC_ID);
    }

    // notations...................................................................................................

    @Test
    public void testNotations() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkNotations(type, Maps.one("zip", "<!NOTATION zip PUBLIC \"zip viewer\">"));
    }

    @Test
    public void testWithoutNotations() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkNotations(type, Maps.empty());
    }

    // entities...................................................................................................

    @Test
    public void testEntitiesPublicId() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkEntities(type, Maps.one("file", "<!ENTITY file PUBLIC \"//-/PublicId\" \"http://www.example.com/public\">"));
    }

    @Test
    public void testEntitiesSystemId() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkEntities(type, Maps.one("file", "<!ENTITY file SYSTEM \"http://www.example.com/system\">"));
    }

    @Test
    public void testWithoutEntities() throws Exception {
        final DomDocumentType type = this.documentTypeFromXml();
        this.checkEntities(type, Maps.empty());
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        final DomDocumentType documentType = this.createNode();
        this.toSearchNodeAndCheck(documentType,
                SearchNode.sequence(Lists.of(
                        documentType.publicId().get().toSearchNode(),
                        documentType.systemId().get().toSearchNode()
                )).setName(SearchNodeName.with("DocType")));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        assertEquals("<!DOCTYPE type123 PUBLIC \"//-/publicId\" \"http://www.example.com/test.dtd\">", this.createNode().toString());
    }

    // <!DOCTYPE root PUBLIC "-//example/" "http://www.example.com/test.dtd">
    @Test
    public void testToStringWithinDocument() throws Exception {
        this.toStringWithinDocumentAndCheck("<!DOCTYPE root PUBLIC \"-//example/\" \"http://www.example.com/test.dtd\">");
    }

    @Test
    public void testToStringWithinDocumentEntities() throws Exception {
        this.toStringWithinDocumentAndCheck("<!DOCTYPE root PUBLIC \"-//example/\" \"http://www.example.com/test.dtd\" [abc <!ENTITY abc> def <!ENTITY def> ghi <!ENTITY ghi>]>");
    }

    @Test
    public void testToStringWithinDocumentNotations() throws Exception {
        this.toStringWithinDocumentAndCheck("<!DOCTYPE root PUBLIC \"-//example/\" \"http://www.example.com/test.dtd\" [abc <!NOTATION abc PUBLIC \"abc viewer\"> def <!NOTATION def PUBLIC \"def viewer\"> ghi <!NOTATION ghi PUBLIC \"ghi viewer\">]>");
    }

    private void toStringWithinDocumentAndCheck(final String expected) throws Exception {
        final DocumentBuilder b = this.documentBuilder(false, false);
        b.setEntityResolver(this.entityResolver());

        final org.w3c.dom.Document document = DomNode.documentFromXml(
                b,
                this.resource());
        final org.w3c.dom.DocumentType documentType = document.getDoctype();

        assertEquals(expected, new DomDocumentType(documentType).toString());
    }

    // factory/helpers..............................................................................................

    private DomDocumentType documentTypeFromXml() throws Exception {
        return this.fromXml().documentType().get();
    }

    private DomDocumentType createNode(final String type, final String publicId, final String systemId) {
        return new DomDocumentType(this.documentBuilder().getDOMImplementation().createDocumentType(type, publicId, systemId));
    }

    @Override
    DomDocumentType createNode(final Document document) {
        return new DomDocumentType(document.getImplementation().createDocumentType(TYPE, PUBLIC_ID_STRING, SYSTEM_ID_STRING));
    }

    @Override
    String text() {
        return "";
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomDocumentType.class);
    }
}
