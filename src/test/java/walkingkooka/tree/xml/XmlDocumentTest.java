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
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ResourceTesting;
import walkingkooka.tree.search.SearchNodeName;
import walkingkooka.tree.select.NodeSelector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class XmlDocumentTest extends XmlParentNodeTestCase<XmlDocument>
        implements ResourceTesting {

    private final static Optional<String> NO_NAMESPACE = Optional.empty();
    private final static XmlName ROOT = XmlName.element("root");
    private final static Optional<XmlPublicId> PUBLIC_ID = XmlNode.publicId("//-/publicId");
    private final static Optional<XmlSystemId> SYSTEM_ID = XmlNode.systemId("http://www.example.com/test.dtd");
    private final static String TEXT = "abc123";

    // canonical form..............................................................................................

    @Test
    public void testCanonicalForm() {
        final XmlDocument document = this.createNode();
        assertEquals(false, document.canonicalForm(), "canonicalForm"); // horrible
    }

    @Test
    public void testSetCanonicalFormSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setCanonicalForm(document.canonicalForm()));
    }

    @Test
    public void testSetCanonicalFormDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.canonicalForm();
        this.ignoreFeatureNotSupported(() -> {
            final XmlDocument document2 = document.setCanonicalForm(newValue);
            assertEquals(newValue, document.canonicalForm(), "canonicalForm");
            assertNotSame(document, document2);
        });
    }

    // cdata sections..............................................................................................

    @Test
    public void testCDataSections() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.cDataSections(), "cDataSections");
    }

    @Test
    public void testSetCDataSectionsSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setCDataSections(document.cDataSections()));
    }

    @Test
    public void testSetCDataSectionsDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.cDataSections();
        final XmlDocument document2 = document.setCDataSections(newValue);
        assertEquals(newValue, document2.cDataSections(), "cdataSections");
        assertNotSame(document, document2);
    }

    // checkCharacterNormalizations......................................................................................

    @Test
    public void testCheckCharacterNormalizations() {
        final XmlDocument document = this.createNode();
        assertEquals(false, document.checkCharacterNormalizations(), "checkCharacterNormalizations");
    }

    @Test
    public void testSetCheckCharacterNormalizationsSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setCheckCharacterNormalizations(document.checkCharacterNormalizations()));
    }

    @Test
    public void testSetCheckCharacterNormalizationsDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.checkCharacterNormalizations();
        this.ignoreFeatureNotSupported(() -> {
            final XmlDocument document2 = document.setCheckCharacterNormalizations(newValue);
            assertEquals(newValue, document.checkCharacterNormalizations(), "checkCharacterNormalizations");
            assertNotSame(document, document2);
        });
    }

    // comments..............................................................................................

    @Test
    public void testComments() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.comments(), "comments");
    }

    @Test
    public void testSetCommentsSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setComments(document.comments()));
    }

    @Test
    public void testSetCommentsDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.comments();
        final XmlDocument document2 = document.setComments(newValue);
        assertEquals(newValue, document2.comments(), "comments");
        assertNotSame(document, document2);
    }

    // datatypeNormalizations......................................................................................

    @Test
    public void testDatatypeNormalizations() {
        final XmlDocument document = this.createNode();
        assertEquals(false, document.datatypeNormalizations(), "datatypeNormalizations");
    }

    @Test
    public void testSetDatatypeNormalizationsSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setDatatypeNormalizations(document.datatypeNormalizations()));
    }

    @Test
    public void testSetDatatypeNormalizationsDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.datatypeNormalizations();
        final XmlDocument document2 = document.setDatatypeNormalizations(newValue);
        assertEquals(newValue, document2.datatypeNormalizations(), "datatypeNormalization");
        assertNotSame(document, document2);
    }

    // elementContentWhitespace......................................................................................

    @Test
    public void testElementContentWhitespace() {
        final XmlDocument document = this.createNode();
        assertEquals(false, document.elementContentWhitespace(), "elementContentWhitespace");
    }

    @Test
    public void testSetElementContentWhitespaceSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setElementContentWhitespace(document.elementContentWhitespace()));
    }

    @Test
    public void testSetElementContentWhitespaceDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.elementContentWhitespace();
        final XmlDocument document2 = document.setElementContentWhitespace(newValue);
        assertEquals(newValue, document2.elementContentWhitespace(), "elementContentWhitespace");
        assertNotSame(document, document2);
    }

    // entities......................................................................................

    @Test
    public void testEntities() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.entities(), "entities");
    }

    @Test
    public void testSetEntitiesSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setEntities(document.entities()));
    }

    @Test
    public void testSetEntitiesDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.entities();
        final XmlDocument document2 = document.setEntities(newValue);
        assertEquals(newValue, document2.entities(), "entities");
        assertNotSame(document, document2);
    }

    // errorHandler......................................................................................

    @Test
    public void testErrorHandler() {
        final XmlDocument document = this.createNode();
        assertEquals(Optional.empty(), document.errorHandler(), "errorHandler");
    }

    @Test
    public void testSetErrorHandlerSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setErrorHandler(document.errorHandler()));
    }

    @Test
    public void testSetErrorHandlerDifferent() {
        final XmlDocument document = this.createNode();
        final Optional<DOMErrorHandler> newHandler = Optional.of((DOMError error) -> true);
        final XmlDocument document2 = document.setErrorHandler(newHandler);
        assertEquals(newHandler, document2.errorHandler(), "errorHandler");
        assertNotSame(document, document2);
    }

    // infoset......................................................................................

    @Test
    public void testInfoset() {
        final XmlDocument document = this.createNode();
        assertEquals(false, document.infoset(), "infoset");
    }

    @Test
    public void testSetInfosetSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setInfoset(document.infoset()));
    }

    @Test
    public void testSetInfosetDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.infoset();
        final XmlDocument document2 = document.setInfoset(newValue);
        assertEquals(newValue, document2.infoset(), "infoset");
        assertNotSame(document, document2);
    }

    // namespaces......................................................................................

    @Test
    public void testNamespaces() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.namespaces(), "namespaces");
    }

    @Test
    public void testSetNamespacesSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setNamespaces(document.namespaces()));
    }

    @Test
    public void testSetNamespacesDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.namespaces();
        final XmlDocument document2 = document.setNamespaces(newValue);
        assertEquals(newValue, document2.namespaces(), "namespaces");
        assertNotSame(document, document2);
    }

    // namespaceDeclaration......................................................................................

    @Test
    public void testNamespaceDeclaration() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.namespaceDeclaration(), "namespaceDeclaration");
    }

    @Test
    public void testSetNamespaceDeclarationSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setNamespaceDeclaration(document.namespaceDeclaration()));
    }

    @Test
    public void testSetNamespaceDeclarationDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.namespaceDeclaration();
        final XmlDocument document2 = document.setNamespaceDeclaration(newValue);
        assertEquals(newValue, document2.namespaceDeclaration(), "namespaceDeclaration");
        assertNotSame(document, document2);
    }

    // normalizeCharacters......................................................................................

    @Test
    public void testNormalizeCharacters() {
        final XmlDocument document = this.createNode();
        assertEquals(false, document.normalizeCharacters(), "normalizeCharacters");
    }

    @Test
    public void testSetNormalizeCharactersSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setNormalizeCharacters(document.normalizeCharacters()));
    }

    @Test
    public void testSetNormalizeCharactersDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.normalizeCharacters();

        this.ignoreFeatureNotSupported(() -> {
            final XmlDocument document2 = document.setNormalizeCharacters(newValue);
            assertEquals(newValue, document.normalizeCharacters(), "normalizeCharacters");
            assertNotSame(document, document2);
        });
    }

    // schemaLocation......................................................................................

    @Test
    public void testSchemaLocation() {
        final XmlDocument document = this.createNode();
        assertEquals(Optional.empty(), document.schemaLocation(), "schemaLocation");
    }

    @Test
    public void testSetSchemaLocationSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setSchemaLocation(document.schemaLocation()));
    }

    @Test
    public void testSetSchemaLocationDifferent() {
        final XmlDocument document = this.createNode();
        final Optional<String> newValue = Optional.of("http://example.com");
        final XmlDocument document2 = document.setSchemaLocation(newValue);
        assertEquals(newValue, document2.schemaLocation(), "schemaLocation");
        assertNotSame(document, document2);
    }

    // schemaType......................................................................................

    @Test
    public void testSchemaType() {
        final XmlDocument document = this.createNode();
        assertEquals(Optional.empty(), document.schemaType(), "schemaType");
    }

    @Test
    public void testSetSchemaTypeSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setSchemaType(document.schemaType()));
    }

    @Test
    public void testSetSchemaTypeDifferent() {
        final XmlDocument document = this.createNode();
        final Optional<String> newValue = Optional.of(XmlNode.XMLNS_URI);
        final XmlDocument document2 = document.setSchemaType(newValue);
        //assertEquals("schemaType", newValue, document2.schemaType()); not sure why setting ignores.
        assertNotSame(document, document2);
    }

    // splitCDataSection......................................................................................

    @Test
    public void testSplitCDataSection() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.splitCDataSection(), "splitCDataSection");
    }

    @Test
    public void testSetSplitCDataSectionSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setSplitCDataSection(document.splitCDataSection()));
    }

    @Test
    public void testSetSplitCDataSectionDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.splitCDataSection();
        final XmlDocument document2 = document.setSplitCDataSection(newValue);
        assertEquals(newValue, document2.splitCDataSection(), "splitCDataSection");
        assertNotSame(document, document2);
    }

    // validate......................................................................................

    @Test
    public void testValidate() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.validate(), "validate");
    }

    @Test
    public void testSetValidateSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setValidate(document.validate()));
    }

    @Test
    public void testSetValidateDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.validate();
        final XmlDocument document2 = document.setValidate(newValue);
        assertEquals(newValue, document2.validate(), "validate");
        assertNotSame(document, document2);
    }

    // validateIfSchema......................................................................................

    @Test
    public void testValidateIfSchema() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.validateIfSchema(), "validateIfSchema");
    }

    @Test
    public void testSetValidateIfSchemaSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setValidateIfSchema(document.validateIfSchema()));
    }

    @Test
    public void testSetValidateIfSchemaDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.validateIfSchema();
        final XmlDocument document2 = document.setValidateIfSchema(newValue);
        assertEquals(newValue, document2.validateIfSchema(), "validateIfSchema");
        assertNotSame(document, document2);
    }

    // wellFormed......................................................................................

    @Test
    public void testWellFormed() {
        final XmlDocument document = this.createNode();
        assertEquals(true, document.wellFormed(), "wellFormed");
    }

    @Test
    public void testSetWellFormedSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setWellFormed(document.wellFormed()));
    }

    @Test
    public void testSetWellFormedDifferent() {
        final XmlDocument document = this.createNode();
        final boolean newValue = !document.wellFormed();
        final XmlDocument document2 = document.setWellFormed(newValue);
        assertEquals(newValue, document2.wellFormed(), "wellFormed");
        assertNotSame(document, document2);
    }

    // appendChild..............................................................................................

    @Test
    public void testAppendChild() {
        final XmlDocument document1 = this.createNode();
        assertEquals(XmlNode.NO_DOCUMENT_TYPE, document1.documentType(), "documentType");
        assertEquals(XmlNode.NO_ELEMENT, document1.element(), "element");
        this.checkChildren("document child count", 0, document1);

        final XmlElement root = document1.createElement(XmlName.element("child"));
        final XmlDocument document2 = document1.appendChild(root);

        assertEquals(XmlNode.NO_DOCUMENT_TYPE, document2.documentType(), "documentType");
        assertEquals(Optional.of(document2.children().get(0)), document2.element(), "element");
        this.checkChildren("document child count", 1, document2);
    }

    // removeChild ............................................................................................

    @Test
    public void testRemoveChild() {
        final XmlDocument document1 = this.createNode();
        final XmlElement child = document1.createElement(ROOT);
        final XmlDocument document2 = document1.appendChild(child);

        final XmlDocument document3 = document2.removeChild(document2.element().get().index());
        assertEquals(Optional.empty(), document3.element(), "Document should have no element");
    }

    // documentelement..............................................................................................

    @Test
    public void testReplaceDocumentElement() {
        final XmlDocument document1 = this.createNode();

        final XmlElement root1 = document1.createElement(XmlName.element("child"));
        final XmlDocument document2 = document1.appendChild(root1);

        assertEquals(XmlNode.NO_DOCUMENT_TYPE, document2.documentType(), "documentType");
        final XmlElement root2 = document2.element().get();
        this.checkName(root2, root1.name());
        this.checkChildren("document child count", 1, document2);
    }

    @Test
    public void testRemoveDocumentElement() {
        final XmlDocument document1 = this.createNode();

        final XmlElement root1 = document1.createElement(XmlName.element("child"));
        final XmlDocument document2 = document1.setChildren(Lists.of(root1))
                .setChildren(XmlNode.NO_CHILDREN);

        assertEquals(XmlNode.NO_DOCUMENT_TYPE, document2.documentType(), "documentType");
        assertEquals(XmlNode.NO_ELEMENT, document2.element(), "element");
        this.checkChildren("document child count", 0, document2);
    }

    @Test
    public void testRemoveDocumentElementKeepingDocType() {
        final XmlDocument document1 = XmlNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);

        final XmlElement root = document1.createElement(XmlName.element("child"));
        final XmlDocument document2 = document1.setChildren(Lists.of(document1.documentType().get()));

        this.checkDocumentType(document2.documentType(), PUBLIC_ID, SYSTEM_ID);
        assertEquals(XmlNode.NO_ELEMENT, document2.element(), "no document element should be present");
        this.checkChildren("document child count(doctype)", 1, document2);
    }

    @Test
    public void testReplaceDocumentElementKeepingDocType() {
        final XmlDocument document1 = XmlNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);

        final XmlElement root = document1.createElement(XmlName.element("child"));
        final XmlDocument document2 = document1.setChildren(Lists.of(document1.documentType().get(), root));

        this.checkDocumentType(document2.documentType(), PUBLIC_ID, SYSTEM_ID);
        final XmlElement root2 = document2.element().get();
        this.checkName(root2, root.name());
        this.checkChildren("document child count(doctype, element)", 2, document2);
    }

    @Test
    public void testRemoveDocTypeChildFails() {
        final XmlDocument document = XmlNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);

        assertThrows(XmlException.class, () -> {
            document.setChildren(Lists.empty());
        });
    }

    // parent..............................................................................................

    public void testParentWith() {
        // na
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentRootElement() {
        this.checkNotEquals(this.createNode().createElement(XmlName.element("root")),
                this.createNode().createElement(XmlName.element("root2")));
    }

    @Test
    public void testEqualsDifferentGrandchildren() {
        final XmlElement child = this.createNode().createElement(XmlName.element("root"))
                .createElement(XmlName.element("child"));

        this.checkNotEquals(child.createElement(XmlName.element("grandChild")).root(),
                child.createElement(XmlName.element("different")).root());
    }

    // fromXml..................................................................................

    @Test
    public void testFromXml() throws Exception {
        final XmlDocument document = this.fromXml();

        final XmlElement root = document.children().get(0).cast();
        final XmlElement abc = root.children().get(1).cast();
        final XmlText text = abc.children().get(0).cast();

        this.checkText(text, TEXT);
        this.checkName(document, XmlName.DOCUMENT);

        assertEquals(Optional.empty(), document.documentUri(), "documentUri");
        assertEquals(Optional.empty(), document.inputEncoding(), "inputEncoding");
        assertEquals(Optional.empty(), document.xmlEncoding(), "xmlEncoding");
        assertEquals(Optional.of("1.0"), document.xmlVersion(), "xmlVersion");
    }

    @Test
    public void testFromXmlNamespace() throws Exception {
        final XmlDocument document = this.fromXml();

        final XmlElement root = document.children().get(0).cast();
        final XmlElement abc = root.children().get(1).cast();
        final XmlText text = abc.children().get(0).cast();

        this.checkText(text, TEXT);
    }

    @Test
    public void testToXml() throws Exception {
        final XmlDocument document = this.createNode();

        final XmlElement day = document.createElement(XmlName.element("day"))
                .appendChild(document.createText("26"));
        final XmlElement month = document.createElement(XmlName.element("month"))
                .appendChild(document.createText("1"));
        final XmlElement year = document.createElement(XmlName.element("year"))
                .appendChild(document.createText("1770"));

        final XmlElement date = document.createElement(XmlName.element("date"))
                .setChildren(Lists.of(day, month, year));

        this.writeXmlAndCheck(document.appendChild(date));
    }

    @Test
    public void testBuildWithSchema() throws Exception {
        final String dtd = "testBuildWithSchema.dtd";
        final String publicId = "-//" + this.getClass().getName() + "/" + dtd;
        final String systemId = dtd;

        final DocumentBuilder b = this.documentBuilder();
        b.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(final String p, final String s) throws SAXException, IOException {
                assertEquals("publicId", publicId, p);
                assertEquals("systemId", systemId, s);

                final InputSource inputSource = new InputSource();
                inputSource.setPublicId(publicId);
                inputSource.setSystemId(systemId);
                inputSource.setCharacterStream(XmlDocumentTest.this.resourceAsReader(XmlDocumentTest.class, dtd));
                return inputSource;
            }
        });
        final XmlDocument document = XmlNode.createDocument(b,
                XmlNode.NO_NAMESPACE_URI,
                XmlName.element("date"),
                XmlNode.publicId(publicId),
                XmlNode.systemId(systemId))
                .setValidate(true);

        final XmlElement day = document.createElement(XmlName.element("day"))
                .appendChild(document.createText("26"));
        final XmlElement month = document.createElement(XmlName.element("month"))
                .appendChild(document.createText("1"));
        final XmlElement year = document.createElement(XmlName.element("year"))
                .appendChild(document.createText("1770"));

        this.writeXmlAndCheck(document.element().get().setChildren(Lists.of(day, month, year)));
    }

    // documentType ...................................................................................

    @Test
    public void testDocumentTypeWithout() {
        final XmlDocument document = this.createNode();
        assertEquals(XmlNode.NO_DOCUMENT_TYPE, document.documentType(), "documentType");
        assertEquals(XmlNode.NO_ELEMENT, document.element(), "element");
        this.checkChildren("document child count", 0, document);
    }

    @Test
    public void testDocumentType() {
        final XmlDocument document = XmlNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);
        this.checkDocumentType(document.documentType(), PUBLIC_ID, SYSTEM_ID);
        this.checkChildren("document child count", 2, document);
    }

    // element ...................................................................................

    @Test
    public void testElementWithout() {
        final XmlDocument document = this.createNode();
        assertEquals(XmlDocument.NO_ELEMENT, document.element());
    }

    @Test
    public void testElement() throws Exception {
        final XmlDocument document = this.createNode();
        final XmlElement element = document.createElement(XmlName.element("root"))
                .appendChild(document.createText(TEXT));

        final XmlDocument document2 = document.appendChild(element);
        this.writeXmlAndCheck(document2);
    }

    @Test
    public void testSetElementSame() {
        final XmlDocument document = this.createNode();
        assertSame(document, document.setElement(document.element()));
    }

    @Test
    public void testSetElementSame2() {
        final XmlDocument document = this.createNode();
        final XmlDocument document2 = document.appendChild(
                document.createElement(XmlName.element("root"))
                        .appendChild(document.createText(TEXT)));
        assertSame(document2, document2.setElement(document2.element()));
    }

    @Test
    public void testSetElementRemoved() throws Exception {
        final XmlDocument document = this.createNode();
        final XmlDocument document2 = document.appendChild(
                document.createElement(XmlName.element("root"))
                        .appendChild(document.createText(TEXT)));
        final XmlDocument document3 = document2.setElement(XmlDocument.NO_ELEMENT);
        this.writeXmlAndCheck(document3);
    }

    @Test
    public void testElementNamespaced() throws Exception {
        final XmlDocument document = this.createNodeNamespaced();

        final XmlElement element2 = document.createElement("https://example.com/2",
                XmlNameSpacePrefix.with("ns2"),
                XmlName.element("element2"));
        final XmlElement element3 = document.createElement("https://example.com/3",
                XmlNameSpacePrefix.with("ns3"),
                XmlName.element("element3"));

        final XmlDocument document2 = document.setElement(Optional.of(element2.appendChild(element3)));

        this.writeXmlAndCheck(document2);
    }

    private void writeXmlAndCheck(final XmlNode node) throws Exception {
        final StringWriter writer = new StringWriter();
        node.document().toXml(TransformerFactory.newInstance().newTransformer(), writer);
        assertEquals(this.resourceAsText(".xml"), writer.toString());
    }

    // entities...................................................................................................

    @Test
    public void testInlineEntity() throws Exception {
        final XmlDocument document = this.fromXml(this.documentBuilder(false, false)); //namespace, expandEntities
        final XmlElement root = document.element().get();
        final XmlEntityReference reference = root.children().get(0).cast();
        assertEquals(XmlName.entityReference("magic"), reference.name(), "entity name");
    }

    // children....................................................................................................

    @Test
    public void testAppendChildElementsTwice() {
        final XmlDocument document = this.createNode();
        final XmlElement element1 = document.createElement(XmlName.element("child1"));
        final XmlElement element2 = document.createElement(XmlName.element("child2"));

        assertThrows(XmlException.class, () -> {
            document.setChildren(Lists.of(element1, element2));
        });
    }

    // text .........................................................................................................

    @Test
    public void testText0() throws Exception {
        this.checkText(this.fromXml(), "");
    }

    // selectors ..................................................................................................

    @Test
    public void testSelectorUsage() throws Exception {
        final XmlDocument document = this.fromXml();
        final NodeSelector<XmlNode, XmlName, XmlAttributeName, String> selector = XmlNode.absoluteNodeSelector()
                .descendant()
                .named(XmlName.element("img"));
        this.selectorApplyAndCheckCount(document, selector, 20);
    }

    @Test
    public void testSelectorUsage2() throws Exception {
        final XmlDocument document = this.fromXml();

        final NodeSelector<XmlNode, XmlName, XmlAttributeName, String> selector = XmlNode.absoluteNodeSelector()
                .descendant()
                .named(XmlName.element("a"))
                .attributeValueContains(XmlNode.attribute("href", XmlNode.NO_PREFIX), "19");
        this.selectorApplyAndCheckCount(document, selector, 3);
    }

    // toString ..................................................................................................

    @Test
    public void testToString() throws Exception {
        this.toStringAndCheck(this.fromXml(),
                "<!--\"cdata-sections\", \"comments\", \"element-content-whitespace\", \"entities\", \"namespaces\", \"namespace-declarations\", \"split-cdata-sections\", \"well-formed\"-->\n" +
                        "<!DOCTYPE root PUBLIC \"-//example/\" \"http://www.example.com/test.dtd\">\n" +
                        "<root>\n" +
                        "     <abc>123</abc> \n" +
                        "</root>");
    }

    // factory/helpers..............................................................................................

    @Override
    XmlDocument createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    XmlDocument createNode(final Document document) {
        return XmlDocument.with(document);
    }

    @Override
    String text() {
        return "";
    }

    @Override
    SearchNodeName searchNodeName() {
        return SearchNodeName.with("Document");
    }

    @Override
    Class<XmlDocument> nodeType() {
        return XmlDocument.class;
    }

    /**
     * A few tests attempt to do things that may fail due to FEATURE_NOT_SUPPORTED.
     */
    private void ignoreFeatureNotSupported(final Runnable runnable) {
        try {
            runnable.run();
        } catch (final RuntimeException cause) {
            final String message = cause.getMessage();
            if (null == message || !message.contains("FEATURE_NOT_SUPPORTED")) {
                throw cause;
            }
        }
    }
}
