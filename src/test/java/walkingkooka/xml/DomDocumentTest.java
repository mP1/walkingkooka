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
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.select.NodeSelector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class DomDocumentTest extends DomParentNodeTestCase<DomDocument> {

    private final static Optional<String> NO_NAMESPACE = Optional.empty();
    private final static DomName ROOT = DomName.element("root");
    private final static Optional<DomPublicId> PUBLIC_ID = DomNode.publicId("//-/publicId");
    private final static Optional<DomSystemId> SYSTEM_ID = DomNode.systemId("http://www.example.com/test.dtd");
    private final static String TEXT = "abc123";

    // canonical form..............................................................................................

    @Test
    public void testCanonicalForm() {
        final DomDocument document = this.createNode();
        assertEquals("canonicalForm", false, document.canonicalForm()); // horrible
    }

    @Test
    public void testSetCanonicalFormSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setCanonicalForm(document.canonicalForm()));
    }

    @Test(expected = DomException.class)
    public void testSetCanonicalFormDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.canonicalForm();
        final DomDocument document2 = document.setCanonicalForm(newValue);
        assertEquals("canonicalForm", newValue, document.canonicalForm());
        assertNotSame(document, document2);
    }

    // cdata sections..............................................................................................

    @Test
    public void testCDataSections() {
        final DomDocument document = this.createNode();
        assertEquals("cDataSections", true, document.cDataSections());
    }

    @Test
    public void testSetCDataSectionsSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setCDataSections(document.cDataSections()));
    }

    @Test
    public void testSetCDataSectionsDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.cDataSections();
        final DomDocument document2 = document.setCDataSections(newValue);
        assertEquals("cdataSections", newValue, document2.cDataSections());
        assertNotSame(document, document2);
    }

    // checkCharacterNormalizations......................................................................................

    @Test
    public void testCheckCharacterNormalizations() {
        final DomDocument document = this.createNode();
        assertEquals("checkCharacterNormalizations", false, document.checkCharacterNormalizations());
    }

    @Test
    public void testSetCheckCharacterNormalizationsSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setCheckCharacterNormalizations(document.checkCharacterNormalizations()));
    }

    @Test(expected = DomException.class)
    public void testSetCheckCharacterNormalizationsDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.checkCharacterNormalizations();
        final DomDocument document2 = document.setCheckCharacterNormalizations(newValue);
        assertEquals("checkCharacterNormalizations", newValue, document.checkCharacterNormalizations());
        assertNotSame(document, document2);
    }
    
    // comments..............................................................................................

    @Test
    public void testComments() {
        final DomDocument document = this.createNode();
        assertEquals("comments", true, document.comments());
    }

    @Test
    public void testSetCommentsSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setComments(document.comments()));
    }

    @Test
    public void testSetCommentsDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.comments();
        final DomDocument document2 = document.setComments(newValue);
        assertEquals("comments", newValue, document2.comments());
        assertNotSame(document, document2);
    }

    // datatypeNormalizations......................................................................................

    @Test
    public void testDatatypeNormalizations() {
        final DomDocument document = this.createNode();
        assertEquals("datatypeNormalizations", false, document.datatypeNormalizations());
    }

    @Test
    public void testSetDatatypeNormalizationsSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setDatatypeNormalizations(document.datatypeNormalizations()));
    }

    @Test
    public void testSetDatatypeNormalizationsDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.datatypeNormalizations();
        final DomDocument document2 = document.setDatatypeNormalizations(newValue);
        assertEquals("datatypeNormalization", newValue, document2.datatypeNormalizations());
        assertNotSame(document, document2);
    }

    // elementContentWhitespace......................................................................................

    @Test
    public void testElementContentWhitespace() {
        final DomDocument document = this.createNode();
        assertEquals("elementContentWhitespace", false, document.elementContentWhitespace());
    }

    @Test
    public void testSetElementContentWhitespaceSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setElementContentWhitespace(document.elementContentWhitespace()));
    }

    @Test
    public void testSetElementContentWhitespaceDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.elementContentWhitespace();
        final DomDocument document2 = document.setElementContentWhitespace(newValue);
        assertEquals("elementContentWhitespace", newValue, document2.elementContentWhitespace());
        assertNotSame(document, document2);
    }

    // entities......................................................................................

    @Test
    public void testEntities() {
        final DomDocument document = this.createNode();
        assertEquals("entities", true, document.entities());
    }

    @Test
    public void testSetEntitiesSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setEntities(document.entities()));
    }

    @Test
    public void testSetEntitiesDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.entities();
        final DomDocument document2 = document.setEntities(newValue);
        assertEquals("entities", newValue, document2.entities());
        assertNotSame(document, document2);
    }

    // errorHandler......................................................................................

    @Test
    public void testErrorHandler() {
        final DomDocument document = this.createNode();
        assertEquals("errorHandler", Optional.empty(), document.errorHandler());
    }

    @Test
    public void testSetErrorHandlerSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setErrorHandler(document.errorHandler()));
    }

    @Test
    public void testSetErrorHandlerDifferent() {
        final DomDocument document = this.createNode();
        final Optional<DOMErrorHandler> newHandler = Optional.of((DOMError error) -> true);
        final DomDocument document2 = document.setErrorHandler(newHandler);
        assertEquals("errorHandler", newHandler, document2.errorHandler());
        assertNotSame(document, document2);
    }

    // infoset......................................................................................

    @Test
    public void testInfoset() {
        final DomDocument document = this.createNode();
        assertEquals("infoset", false, document.infoset());
    }

    @Test
    public void testSetInfosetSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setInfoset(document.infoset()));
    }

    @Test
    public void testSetInfosetDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.infoset();
        final DomDocument document2 = document.setInfoset(newValue);
        assertEquals("infoset", newValue, document2.infoset());
        assertNotSame(document, document2);
    }

    // namespaces......................................................................................

    @Test
    public void testNamespaces() {
        final DomDocument document = this.createNode();
        assertEquals("namespaces", true, document.namespaces());
    }

    @Test
    public void testSetNamespacesSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setNamespaces(document.namespaces()));
    }

    @Test
    public void testSetNamespacesDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.namespaces();
        final DomDocument document2 = document.setNamespaces(newValue);
        assertEquals("namespaces", newValue, document2.namespaces());
        assertNotSame(document, document2);
    }

    // namespaceDeclaration......................................................................................

    @Test
    public void testNamespaceDeclaration() {
        final DomDocument document = this.createNode();
        assertEquals("namespaceDeclaration", true, document.namespaceDeclaration());
    }

    @Test
    public void testSetNamespaceDeclarationSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setNamespaceDeclaration(document.namespaceDeclaration()));
    }

    @Test
    public void testSetNamespaceDeclarationDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.namespaceDeclaration();
        final DomDocument document2 = document.setNamespaceDeclaration(newValue);
        assertEquals("namespaceDeclaration", newValue, document2.namespaceDeclaration());
        assertNotSame(document, document2);
    }

    // normalizeCharacters......................................................................................

    @Test
    public void testNormalizeCharacters() {
        final DomDocument document = this.createNode();
        assertEquals("normalizeCharacters", false, document.normalizeCharacters());
    }

    @Test
    public void testSetNormalizeCharactersSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setNormalizeCharacters(document.normalizeCharacters()));
    }

    @Test(expected = DomException.class)
    public void testSetNormalizeCharactersDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.normalizeCharacters();
        final DomDocument document2 = document.setNormalizeCharacters(newValue);
        assertEquals("normalizeCharacters", newValue, document.normalizeCharacters());
        assertNotSame(document, document2);
    }

    // schemaLocation......................................................................................

    @Test
    public void testSchemaLocation() {
        final DomDocument document = this.createNode();
        assertEquals("schemaLocation", Optional.empty(), document.schemaLocation());
    }

    @Test
    public void testSetSchemaLocationSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setSchemaLocation(document.schemaLocation()));
    }

    @Test
    public void testSetSchemaLocationDifferent() {
        final DomDocument document = this.createNode();
        final Optional<String> newValue = Optional.of("http://example.com");
        final DomDocument document2 = document.setSchemaLocation(newValue);
        assertEquals("schemaLocation", newValue, document2.schemaLocation());
        assertNotSame(document, document2);
    }

    // schemaType......................................................................................

    @Test
    public void testSchemaType() {
        final DomDocument document = this.createNode();
        assertEquals("schemaType", Optional.empty(), document.schemaType());
    }

    @Test
    public void testSetSchemaTypeSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setSchemaType(document.schemaType()));
    }

    @Test
    public void testSetSchemaTypeDifferent() {
        final DomDocument document = this.createNode();
        final Optional<String> newValue = Optional.of(DomNode.XMLNS_URI);
        final DomDocument document2 = document.setSchemaType(newValue);
        //assertEquals("schemaType", newValue, document2.schemaType()); not sure why setting ignores.
        assertNotSame(document, document2);
    }

    // splitCDataSection......................................................................................

    @Test
    public void testSplitCDataSection() {
        final DomDocument document = this.createNode();
        assertEquals("splitCDataSection", true, document.splitCDataSection());
    }

    @Test
    public void testSetSplitCDataSectionSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setSplitCDataSection(document.splitCDataSection()));
    }

    @Test
    public void testSetSplitCDataSectionDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.splitCDataSection();
        final DomDocument document2 = document.setSplitCDataSection(newValue);
        assertEquals("splitCDataSection", newValue, document2.splitCDataSection());
        assertNotSame(document, document2);
    }

    // validate......................................................................................

    @Test
    public void testValidate() {
        final DomDocument document = this.createNode();
        assertEquals("validate", true, document.validate());
    }

    @Test
    public void testSetValidateSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setValidate(document.validate()));
    }

    @Test
    public void testSetValidateDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.validate();
        final DomDocument document2 = document.setValidate(newValue);
        assertEquals("validate", newValue, document2.validate());
        assertNotSame(document, document2);
    }

    // validateIfSchema......................................................................................

    @Test
    public void testValidateIfSchema() {
        final DomDocument document = this.createNode();
        assertEquals("validateIfSchema", true, document.validateIfSchema());
    }

    @Test
    public void testSetValidateIfSchemaSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setValidateIfSchema(document.validateIfSchema()));
    }

    @Test
    public void testSetValidateIfSchemaDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.validateIfSchema();
        final DomDocument document2 = document.setValidateIfSchema(newValue);
        assertEquals("validateIfSchema", newValue, document2.validateIfSchema());
        assertNotSame(document, document2);
    }

    // wellFormed......................................................................................

    @Test
    public void testWellFormed() {
        final DomDocument document = this.createNode();
        assertEquals("wellFormed", true, document.wellFormed());
    }

    @Test
    public void testSetWellFormedSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setWellFormed(document.wellFormed()));
    }

    @Test
    public void testSetWellFormedDifferent() {
        final DomDocument document = this.createNode();
        final boolean newValue = !document.wellFormed();
        final DomDocument document2 = document.setWellFormed(newValue);
        assertEquals("wellFormed", newValue, document2.wellFormed());
        assertNotSame(document, document2);
    }

    // appendChild..............................................................................................

    @Test
    public void testAppendChild() {
        final DomDocument document1 = this.createNode();
        assertEquals("documentType", DomNode.NO_DOCUMENT_TYPE, document1.documentType());
        assertEquals("element", DomNode.NO_ELEMENT, document1.element());
        this.checkChildren("document child count", 0, document1);

        final DomElement root = document1.createElement(DomName.element("child"));
        final DomDocument document2 = document1.appendChild(root);

        assertEquals("documentType", DomNode.NO_DOCUMENT_TYPE, document2.documentType());
        assertEquals("element", Optional.of(document2.children().get(0)), document2.element());
        this.checkChildren("document child count", 1, document2);
    }

    // removeChild ............................................................................................

    @Test
    public void testRemoveChild() {
        final DomDocument document1 = this.createNode();
        final DomElement child = document1.createElement(ROOT);
        final DomDocument document2 = document1.appendChild(child);

        final DomDocument document3 = document2.removeChild(document2.element().get());
        assertEquals("Document should have no element", Optional.empty(), document3.element());
    }

    @Test
    public void testRemoveChildByIndex() {
        final DomDocument document1 = this.createNode();
        final DomElement child = document1.createElement(ROOT);
        final DomDocument document2 = document1.appendChild(child);

        final DomDocument document3 = document2.removeChild(document2.element().get().index());
        assertEquals("Document should have no element", Optional.empty(), document3.element());
    }

    // documentelement..............................................................................................

    @Test
    public void testReplaceDocumentElement() {
        final DomDocument document1 = this.createNode();

        final DomElement root1 = document1.createElement(DomName.element("child"));
        final DomDocument document2 = document1.appendChild(root1);
        
        assertEquals("documentType", DomNode.NO_DOCUMENT_TYPE, document2.documentType());
        final DomElement root2 = document2.element().get();
        this.checkName(root2, root1.name());
        this.checkChildren("document child count", 1, document2);
    }

    @Test
    public void testRemoveDocumentElement() {
        final DomDocument document1 = this.createNode();

        final DomElement root1 = document1.createElement(DomName.element("child"));
        final DomDocument document2 = document1.setChildren(Lists.of(root1))
                .setChildren(DomNode.NO_CHILDREN);

        assertEquals("documentType", DomNode.NO_DOCUMENT_TYPE, document2.documentType());
        assertEquals("element", DomNode.NO_ELEMENT, document2.element());
        this.checkChildren("document child count", 0, document2);
    }

    @Test
    public void testRemoveDocumentElementKeepingDocType() {
        final DomDocument document1 = DomNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);

        final DomElement root = document1.createElement(DomName.element("child"));
        final DomDocument document2 = document1.setChildren(Lists.of(document1.documentType().get()));

        this.checkDocumentType(document2.documentType(), PUBLIC_ID, SYSTEM_ID);
        assertEquals("no document element should be present", DomNode.NO_ELEMENT, document2.element());
        this.checkChildren("document child count(doctype)", 1, document2);
    }

    @Test
    public void testReplaceDocumentElementKeepingDocType() {
        final DomDocument document1 = DomNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);

        final DomElement root = document1.createElement(DomName.element("child"));
        final DomDocument document2 = document1.setChildren(Lists.of(document1.documentType().get(), root));

        this.checkDocumentType(document2.documentType(), PUBLIC_ID, SYSTEM_ID);
        final DomElement root2 = document2.element().get();
        this.checkName(root2, root.name());
        this.checkChildren("document child count(doctype, element)", 2, document2);
    }

    @Test(expected = DomException.class)
    public void testRemoveDocTypeChildFails() {
        final DomDocument document1 = DomNode.createDocument(this.documentBuilder(),
                NO_NAMESPACE,
                ROOT,
                PUBLIC_ID,
                SYSTEM_ID);
        document1.setChildren(Lists.empty());
    }

    // parent..............................................................................................

    public void testParentWith() {
        // na
    }

    // fromXml..................................................................................

    @Test
    public void testFromXml() throws Exception{
        final DomDocument document = this.fromXml();

        final DomElement root = document.children().get(0).cast();
        final DomElement abc = root.children().get(1).cast();
        final DomText text = abc.children().get(0).cast();

        this.checkText(text,TEXT);
        this.checkName(document, DomName.DOCUMENT);

        assertEquals("documentUri", Optional.empty(), document.documentUri());
        assertEquals("inputEncoding", Optional.empty(), document.inputEncoding());
        assertEquals("xmlEncoding", Optional.empty(), document.xmlEncoding());
        assertEquals("xmlVersion", Optional.of("1.0"), document.xmlVersion());
    }

    @Test
    public void testFromXmlNamespace() throws Exception{
        final DomDocument document = this.fromXml();

        final DomElement root = document.children().get(0).cast();
        final DomElement abc = root.children().get(1).cast();
        final DomText text = abc.children().get(0).cast();

        this.checkText(text, TEXT);
    }

    @Test
    public void testToXml() throws Exception {
        final DomDocument document = this.createNode();

        final DomElement day = document.createElement(DomName.element("day"))
                .appendChild(document.createText("26"));
        final DomElement month = document.createElement(DomName.element("month"))
                .appendChild(document.createText("1"));
        final DomElement year = document.createElement(DomName.element("year"))
                .appendChild(document.createText("1770"));

        final DomElement date = document.createElement(DomName.element("date"))
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
                inputSource.setCharacterStream(resourceAsReader(DomDocumentTest.class, dtd));
                return inputSource;
            }
        });
        final DomDocument document =DomNode.createDocument(b,
                DomNode.NO_NAMESPACE_URI,
                DomName.element("date"),
                DomNode.publicId(publicId),
                DomNode.systemId(systemId))
                .setValidate(true);

        final DomElement day = document.createElement(DomName.element("day"))
                .appendChild(document.createText("26"));
        final DomElement month = document.createElement(DomName.element("month"))
                .appendChild(document.createText("1"));
        final DomElement year = document.createElement(DomName.element("year"))
                .appendChild(document.createText("1770"));

        this.writeXmlAndCheck(document.element().get().setChildren(Lists.of(day, month, year)));
    }

    // documentType ...................................................................................

    @Test
    public void testDocumentTypeWithout() {
        final DomDocument document = this.createNode();
        assertEquals("documentType", DomNode.NO_DOCUMENT_TYPE, document.documentType());
        assertEquals("element", DomNode.NO_ELEMENT, document.element());
        this.checkChildren("document child count", 0, document);
    }

    @Test
    public void testDocumentType() {
        final DomDocument document = DomNode.createDocument(this.documentBuilder(),
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
        final DomDocument document = this.createNode();
        assertEquals(DomDocument.NO_ELEMENT, document.element());
    }

    @Test
    public void testElement() throws Exception {
        final DomDocument document = this.createNode();
        final DomElement element = document.createElement(DomName.element("root"))
                .appendChild( document.createText(TEXT));

        final DomDocument document2 = document.appendChild(element);
        this.writeXmlAndCheck(document2);
    }

    @Test
    public void testSetElementSame() {
        final DomDocument document = this.createNode();
        assertSame(document, document.setElement(document.element()));
    }

    @Test
    public void testSetElementSame2() {
        final DomDocument document = this.createNode();
        final DomDocument document2 = document.appendChild(
                document.createElement(DomName.element("root"))
                .appendChild( document.createText(TEXT)));
        assertSame(document2, document2.setElement(document2.element()));
    }

    @Test
    public void testSetElementRemoved() throws Exception {
        final DomDocument document = this.createNode();
        final DomDocument document2 = document.appendChild(
                document.createElement(DomName.element("root"))
                        .appendChild(document.createText(TEXT)));
        final DomDocument document3 = document2.setElement(DomDocument.NO_ELEMENT);
        this.writeXmlAndCheck(document3);
    }

    @Test
    public void testElementNamespaced() throws Exception {
       final DomDocument document = this.createNodeNamespaced();

       final DomElement element2 = document.createElement("https://example.com/2",
               DomNameSpacePrefix.with("ns2"),
               DomName.element("element2"));
        final DomElement element3 = document.createElement("https://example.com/3",
                DomNameSpacePrefix.with("ns3"),
                DomName.element("element3"));

       final DomDocument document2 = document.setElement(Optional.of(element2.appendChild(element3)));

       this.writeXmlAndCheck(document2);
    }

    private void writeXmlAndCheck(final DomNode node) throws Exception {
        final StringWriter writer = new StringWriter();
        node.document().toXml(TransformerFactory.newInstance().newTransformer(), writer);
        assertEquals(this.resourceAsText(".xml"), writer.toString());
    }

    // entities...................................................................................................

    @Test
    public void testInlineEntity() throws Exception{
        final DomDocument document = this.fromXml(this.documentBuilder(false, false)); //namespace, expandEntities
        final DomElement root = document.element().get();
        final DomEntityReference reference = root.children().get(0).cast();
        assertEquals("entity name", DomName.entityReference("magic"), reference.name());
    }

    // children....................................................................................................

    @Test(expected = DomException.class)
    public void testAppendChildElementsTwice() {
        final DomDocument document = this.createNode();
        final DomElement element1 = document.createElement(DomName.element("child1"));
        final DomElement element2 = document.createElement(DomName.element("child2"));
        document.setChildren(Lists.of(element1, element2));
    }

    // text .........................................................................................................

    @Test
    public void testText0() throws Exception {
        this.checkText(this.fromXml(), "");
    }

    // selectors ..................................................................................................

    @Test
    public void testSelectorUsage() throws Exception {
        final DomDocument document = this.fromXml();
        final NodeSelector<DomNode, DomName, DomAttributeName, String> selector = DomNode.absoluteNodeSelectorBuilder()
                .descendant()
                .named(DomName.element("img"))
                .build();
        final Set<DomNode> matches = selector.accept(document, selector.nulObserver());
        assertEquals("should have matched img tags\n" + matches, 20, matches.size());
    }

    @Test
    public void testSelectorUsage2() throws Exception {
        final DomDocument document = this.fromXml();

        final NodeSelector<DomNode, DomName, DomAttributeName, String> selector = DomNode.absoluteNodeSelectorBuilder()
                .descendant()
                .named(DomName.element("a"))
                .attributeValueContains(DomNode.attribute("href", DomNode.NO_PREFIX), "19")
                .build();
        final Set<DomNode> matches = selector.accept(document, selector.nulObserver());
        assertEquals("should have matched 3 links with 19 in href\n" + matches, 3, matches.size());
    }

    // toString ..................................................................................................

    @Test
    public void testToString() throws Exception {
        assertEquals("<!--\"cdata-sections\", \"comments\", \"element-content-whitespace\", \"entities\", \"namespaces\", \"namespace-declarations\", \"split-cdata-sections\", \"well-formed\"-->\n" +
                "<!DOCTYPE root PUBLIC \"-//example/\" \"http://www.example.com/test.dtd\">\n" +
                "<root>\n" +
                "     <abc>123</abc> \n" +
                "</root>", this.fromXml().toString());
    }
    
    // factory/helpers..............................................................................................

    @Override
    DomDocument createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }

    @Override
    DomDocument createNode(final Document document) {
        return new DomDocument(document);
    }

    @Override
    String text() {
        return "";
    }

    @Override
    protected Class<DomNode> type() {
        return Cast.to(DomDocument.class);
    }

    private void checkEntities(final DomDocument node, final Map<String, String> entities) {
        this.checkEntities(node.documentType().get(), entities);
    }

    private void checkNotations(final DomDocument node, final Map<String, String> notations) {
        this.checkNotations(node.documentType().get(), notations);
    }
}
