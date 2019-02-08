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

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.NodeTestCase;
import walkingkooka.tree.search.HasSearchNodeTesting;
import walkingkooka.tree.search.SearchNode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class XmlNodeTestCase<N extends XmlNode> extends NodeTestCase<XmlNode, XmlName, XmlAttributeName, String>
        implements HasSearchNodeTesting<N> {

    final static Optional<XmlNode> NO_PARENT = XmlNode.NO_PARENT;

    XmlNodeTestCase() {
        super();
    }

    // parent.......................................................................................................

    @Test
    public void testParentWithout() {
        this.checkWithoutParent(this.createNode());
    }

    @Test
    public void testParentWith() {
        final DocumentBuilder builder = this.documentBuilder();
        final Document document = builder.newDocument();
        final org.w3c.dom.Element parent = document.createElement("parent");

        final N child = this.createNode(document);
        parent.appendChild(child.node);
        child.parent = null;

        this.checkWithParent(child);
    }

    // normalize .................................................................................................

    @Test
    public final void testNormalize() {
        final N node = this.createNode();
        assertSame(node, node.normalize());
    }

    @Test
    public final void testText() {
        final N node = this.createNode();
        this.checkText(node, this.text());
    }

    // isXXX ..................................................................................................

    @Test
    public final void testIsCDataSection() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlCDataSection, node.isCDataSection());
    }

    @Test
    public final void testIsComment() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlComment, node.isComment());
    }

    @Test
    public final void testIsDocument() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlDocument, node.isDocument());
    }

    @Test
    public final void testIsDocumentType() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlDocumentType, node.isDocumentType());
    }

    @Test
    public final void testIsElement() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlElement, node.isElement());
    }

    @Test
    public final void testIsEntity() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlEntity, node.isEntity());
    }

    @Test
    public final void testIsEntityReference() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlEntityReference, node.isEntityReference());
    }

    @Test
    public final void testIsNotation() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlNotation, node.isNotation());
    }

    @Test
    public final void testIsProcessingInstruction() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlProcessingInstruction, node.isProcessingInstruction());
    }

    @Test
    public final void testIsText() {
        final N node = this.createNode();
        assertEquals(node instanceof XmlText, node.isText());
    }

    // toXmlNode ..................................................................................................

    @Test
    public final void testToXmlNode() {
        final N node = this.createNode();
        assertSame(node, node.toXmlNode());
    }

    // factories, helpers...............................................................................................

    final XmlDocument fromXml() throws Exception {
        return this.fromXml(this.documentBuilder());
    }

    final XmlDocument fromXml(final DocumentBuilder b) throws Exception {
        return XmlDocument.with(documentFromXml(b));
    }

    final org.w3c.dom.Document documentFromXml(final DocumentBuilder b) throws Exception {
        b.setEntityResolver(this.entityResolver());
        return XmlNode.documentFromXml(b, this.resource());
    }

    final EntityResolver entityResolver() {
        return new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) {
                final InputSource source = new InputSource();
                source.setCharacterStream(new StringReader(""));
                return source;
            }
        };
    }

    @Override
    public final N createNode() {
        return this.createNode(this.documentBuilder());
    }

    final N createNodeNamespaced() {
        return this.createNode(this.documentBuilderNamespaced());
    }

    abstract N createNode(final DocumentBuilder builder);

    abstract N createNode(final Document document);

    abstract String text();

    final Document document() {
        return this.documentBuilder().newDocument();
    }

    final DocumentBuilder documentBuilder() {
        return this.documentBuilder(false, true);
    }

    final DocumentBuilder documentBuilderNamespaced() {
        return this.documentBuilder(true, true);
    }

    final DocumentBuilder documentBuilder(final boolean namespace, final boolean expandEntities) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespace);
            factory.setValidating(false);
            factory.setExpandEntityReferences(expandEntities);
            return factory.newDocumentBuilder();
        } catch (final Exception cause) {
            throw new Error(cause);
        }
    }

    final void checkName(final XmlNode node, final XmlName name) {
        assertEquals(name, node.name(), "name from XmlNode");
        checkName(node, name.value());
    }

    final void checkName(final XmlNode node, final String name) {
        if (null != node.node) {
            assertEquals(name, node.node.getNodeName(), "w3c.dom.Node name");
        }
        assertEquals(name, node.name().value(), "name from XmlNode");
    }

    final void checkText(final XmlNode node, final String text) {
        assertEquals(text, node.text(), "text");
    }

    final void checkDocumentType(final Optional<XmlDocumentType> documentType,
                                 final Optional<XmlPublicId> publicId,
                                 final Optional<XmlSystemId> systemId) {
        final XmlDocumentType got = documentType.get();
        this.checkPublicId(got, publicId);
        this.checkSystemId(got, systemId);
    }

    final void checkPublicId(final HasXmlPublicId has, final Optional<XmlPublicId> publicId) {
        assertEquals(publicId, has.publicId(),"publicId");
    }

    final void checkPublicId(final HasXmlPublicId has, final String publicId) {
        this.checkPublicId(has, XmlNode.publicId(publicId));
    }

    final void checkSystemId(final HasXmlSystemId has, final Optional<XmlSystemId> systemId) {
        assertEquals(systemId, has.systemId(),"systemId");
    }

    final void checkSystemId(final HasXmlSystemId has, final String systemId) {
        this.checkSystemId(has, XmlNode.systemId(systemId));
    }

    final void checkEntities(final XmlDocumentType node, final Map<String, String> entities) {
        final Map<String, String> entities2 = Maps.ordered();
        node.entities().entrySet()
                .stream()
                .forEach(e -> entities2.put(e.getKey().value(), e.getValue().toString()));
        assertEquals(entities, entities2,"entities");

        final Set<String> actualRawEntitiesNames = Sets.ordered();
        final org.w3c.dom.DocumentType documentType = node.documentTypeNode();
        if (null != documentType) {
            final NamedNodeMap entityNodes = documentType.getEntities();
            final int count = entityNodes.getLength();
            for (int i = 0; i < count; i++) {
                final org.w3c.dom.Entity entity = Cast.to(entityNodes.item(i));
                actualRawEntitiesNames.add(entity.getNodeName());
            }
        }

        assertEquals(entities.keySet(), actualRawEntitiesNames,"entities on w3c.dom.Root");
    }

    final void checkNotations(final XmlDocumentType node, final Map<String, String> notations) {
        final Map<String, String> notations2 = Maps.ordered();
        node.notations().entrySet()
                .stream()
                .forEach(e -> notations2.put(e.getKey().value(), e.getValue().toString()));
        assertEquals(notations, notations2,"notations");

        final Set<String> actualRawNotationsNames = Sets.ordered();
        final org.w3c.dom.DocumentType documentType = node.documentTypeNode();
        if (null != documentType) {
            final NamedNodeMap notationNodes = documentType.getNotations();
            final int count = notationNodes.getLength();
            for (int i = 0; i < count; i++) {
                final org.w3c.dom.Notation notation = Cast.to(notationNodes.item(i));
                actualRawNotationsNames.add(notation.getNodeName());
            }
        }

        assertEquals(notations.keySet(), actualRawNotationsNames,"notations on w3c.dom.Root");
    }

    final Reader resource() throws IOException {
        return this.resource(".xml");
    }

    final Reader resource(final String fileExtension) throws IOException {
        final Class<?> classs = this.getClass();
        return new StringReader(
                this.resourceAsText(classs,
                        classs.getSimpleName() + "/" + this.currentTestName() + fileExtension));
    }

    final String resourceAsText(final String fileExtension) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            final char[] buffer = new char[4096];
            try (Reader reader = this.resource(fileExtension)) {
                for (; ; ) {
                    final int count = reader.read(buffer);
                    if (-1 == count) {
                        break;
                    }
                    writer.write(buffer, 0, count);
                }
                return writer.toString();
            }
        }
    }

    final void toSearchNodeAndCheck(final SearchNode searchNode) {
        this.toSearchNodeAndCheck(this.createNode(), searchNode);
    }

    @Override
    public final Class<XmlNode> type() {
        return Cast.to(this.nodeType());
    }

    abstract Class<N> nodeType();

    @Override
    public final String typeNamePrefix() {
        return "Xml";
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
