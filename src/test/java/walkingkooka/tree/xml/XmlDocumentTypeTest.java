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
import org.w3c.dom.DocumentType;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import javax.xml.parsers.DocumentBuilder;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class XmlDocumentTypeTest extends XmlLeafNodeTestCase<XmlDocumentType> {

    private final static String TYPE = "type123";
    private final static String PUBLIC_ID_STRING = "//-/publicId";
    private final static Optional<XmlPublicId> PUBLIC_ID = XmlNode.publicId(PUBLIC_ID_STRING);
    private final static String SYSTEM_ID_STRING = "http://www.example.com/test.dtd";
    private final static Optional<XmlSystemId> SYSTEM_ID = XmlNode.systemId(SYSTEM_ID_STRING);

    // create..............................................................................................

    @Test
    public void testWithPublicId() {
        final XmlDocumentType node = this.createNode(TYPE, PUBLIC_ID_STRING, SYSTEM_ID_STRING);
        this.checkPublicId(node, PUBLIC_ID);
        this.checkSystemId(node, SYSTEM_ID);
        this.checkName(node, TYPE);
    }

    @Test
    public void testWithSystemId() {
        final XmlDocumentType node = this.createNode(TYPE, null, SYSTEM_ID_STRING);
        this.checkPublicId(node, XmlNode.NO_PUBLIC_ID);
        this.checkSystemId(node, SYSTEM_ID);
        this.checkName(node, TYPE);
    }

    @Test
    public void testAppendChild() {
        final XmlDocumentType node = this.createNode(TYPE, null, SYSTEM_ID_STRING);
        final XmlNode text = XmlNode.wrap(this.document().createTextNode("text123"));

        assertThrows(UnsupportedOperationException.class, () -> {
            node.appendChild(text);
        });
    }

    // parent..............................................................................................

    public void testParentWith() {
        // na
    }

    // publicId....................................................................................................

    @Test
    public void testPublicId() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkPublicId(type, "-//example/");
        this.checkSystemId(type, "http://www.example.com/test.dtd");
    }

    // systemId ...................................................................................................

    @Test
    public void testSystemId() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkSystemId(type, "/system.dtd");
        this.checkPublicId(type, XmlNode.NO_PUBLIC_ID);
    }

    // notations...................................................................................................

    @Test
    public void testNotations() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkNotations(type, Maps.of("zip", "<!NOTATION zip PUBLIC \"zip viewer\">"));
    }

    @Test
    public void testWithoutNotations() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkNotations(type, Maps.empty());
    }

    // entities...................................................................................................

    @Test
    public void testEntitiesPublicId() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkEntities(type, Maps.of("file", "<!ENTITY file PUBLIC \"//-/PublicId\" \"http://www.example.com/public\">"));
    }

    @Test
    public void testEntitiesSystemId() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkEntities(type, Maps.of("file", "<!ENTITY file SYSTEM \"http://www.example.com/system\">"));
    }

    @Test
    public void testWithoutEntities() throws Exception {
        final XmlDocumentType type = this.documentTypeFromXml();
        this.checkEntities(type, Maps.empty());
    }

    @Test
    public void testPropertiesNeverReturnNull() throws Exception {
        this.allPropertiesNeverReturnNullCheck(this.createNode(), this::propertiesNeverReturnNull);
    }

    private boolean propertiesNeverReturnNull(final Method method) {
        final String name = method.getName();
        return name.equals("document") ||
                name.equals("internalSubset") ||
                name.equals("parentOrFail");
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        final XmlDocumentType documentType = this.createNode();
        this.toSearchNodeAndCheck(documentType,
                SearchNode.sequence(Lists.of(
                        documentType.publicId().get().toSearchNode(),
                        documentType.systemId().get().toSearchNode()
                )).setName(SearchNodeName.with("DocType")));
    }

    // HashCodeEqualsDefined.....................................................................................................

    @Test
    public void testEqualsTypeDifferent() {
        this.checkNotEquals(this.createNode("different-type", PUBLIC_ID_STRING, SYSTEM_ID_STRING));
    }

    @Test
    public void testEqualsPublicIdDifferent() {
        this.checkNotEquals(this.createNode(TYPE, "//-/publicId-DIFFERENT", SYSTEM_ID_STRING));
    }

    @Test
    public void testEqualsSystemIdDifferent() {
        this.createNode(TYPE, PUBLIC_ID_STRING, "http://www.example.com/different.dtd");
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createNode(),
                "<!DOCTYPE type123 PUBLIC \"//-/publicId\" \"http://www.example.com/test.dtd\">");
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

        final Document document = XmlNode.documentFromXml(
                b,
                this.resource());
        final DocumentType documentType = document.getDoctype();

        this.toStringAndCheck(XmlDocumentType.with(documentType), expected);
    }

    // factory/helpers..............................................................................................

    private XmlDocumentType documentTypeFromXml() throws Exception {
        return this.fromXml().documentType().get();
    }

    private XmlDocumentType createNode(final String type, final String publicId, final String systemId) {
        return XmlDocumentType.with(this.documentBuilder().getDOMImplementation().createDocumentType(type, publicId, systemId));
    }

    @Override
    XmlDocumentType createNode(final Document document) {
        return XmlDocumentType.with(document.getImplementation().createDocumentType(TYPE, PUBLIC_ID_STRING, SYSTEM_ID_STRING));
    }

    @Override
    String text() {
        return "";
    }

    @Override
    Class<XmlDocumentType> nodeType() {
        return XmlDocumentType.class;
    }
}
