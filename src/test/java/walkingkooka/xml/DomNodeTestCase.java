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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.NodeTestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public abstract class DomNodeTestCase<N extends DomNode> extends NodeTestCase<DomNode, DomName, DomAttributeName, String> {

    final static Optional<DomNode> NO_PARENT = DomNode.NO_PARENT;

    /**
     * Used to capture the current test method.
     */
    @Rule
    public TestName rule = new TestName();

    @Test
    public void testClassIsPackagePrivate() {
        // nop
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

    // asXXX ..................................................................................................

    @Test
    public final void testAsCDataSection() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asCDataSection(), DomCDataSection.class);
    }

    @Test
    public final void testAsComment() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asComment(), DomComment.class);
    }

    @Test
    public final void testAsDocument() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asDocument(), DomDocument.class);
    }

    @Test
    public final void testAsDocumentType() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asDocumentType(), DomDocumentType.class);
    }

    @Test
    public final void testAsElement() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asElement(), DomElement.class);
    }

    @Test
    public final void testAsEntity() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asEntity(), DomEntity.class);
    }

    @Test
    public final void testAsEntityReference() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asEntityReference(), DomEntityReference.class);
    }

    @Test
    public final void testAsNotation() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asNotation(), DomNotation.class);
    }

    @Test
    public final void testAsProcessingInstruction() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asProcessingInstruction(), DomProcessingInstruction.class);
    }

    @Test
    public final void testAsText() {
        final N node = this.createNode();
        this.asAndCheck(node, () -> node.asText(), DomText.class);
    }

    private <T extends DomNode> void asAndCheck(final N node, final Supplier<T> as, final Class<T> type){
        final boolean shouldWork = node.getClass().equals(type);
        if(shouldWork){
            assertEquals(node, as.get());
        } else {
            try{
                as.get();
                Assert.fail("Should have failed");
            } catch (final ClassCastException expected){
            }
        }
    }

    // isXXX ..................................................................................................

    @Test
    public final void testIsCDataSection() {
        final N node = this.createNode();
        assertEquals(node instanceof DomCDataSection, node.isCDataSection());
    }
    
    @Test
    public final void testIsComment() {
        final N node = this.createNode();
        assertEquals(node instanceof DomComment, node.isComment());
    }
    
    @Test
    public final void testIsDocument() {
        final N node = this.createNode();
        assertEquals(node instanceof DomDocument, node.isDocument());
    }

    @Test
    public final void testIsDocumentType() {
        final N node = this.createNode();
        assertEquals(node instanceof DomDocumentType, node.isDocumentType());
    }

    @Test
    public final void testIsElement() {
        final N node = this.createNode();
        assertEquals(node instanceof DomElement, node.isElement());
    }

    @Test
    public final void testIsEntity() {
        final N node = this.createNode();
        assertEquals(node instanceof DomEntity, node.isEntity());
    }

    @Test
    public final void testIsEntityReference() {
        final N node = this.createNode();
        assertEquals(node instanceof DomEntityReference, node.isEntityReference());
    }
    
    @Test
    public final void testIsNotation() {
        final N node = this.createNode();
        assertEquals(node instanceof DomNotation, node.isNotation());
    }

    @Test
    public final void testIsProcessingInstruction() {
        final N node = this.createNode();
        assertEquals(node instanceof DomProcessingInstruction, node.isProcessingInstruction());
    }

    @Test
    public final void testIsText() {
        final N node = this.createNode();
        assertEquals(node instanceof DomText, node.isText());
    }

    // toString ..................................................................................................

    @Test
    public abstract void testToString() throws Exception;

    // factories, helpers...............................................................................................

    final DomDocument fromXml() throws Exception {
        return this.fromXml(this.documentBuilder());
    }

    final DomDocument fromXml(final DocumentBuilder b) throws Exception{
        return new DomDocument(documentFromXml(b));
    }

    final org.w3c.dom.Document documentFromXml(final DocumentBuilder b) throws Exception {
        b.setEntityResolver(this.entityResolver());
        return DomNode.documentFromXml(b, this.resource());
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
    protected N createNode() {
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
            throw new RuntimeException(cause);
        }
    }

    final void checkName(final DomNode node, final DomName name) {
        assertEquals("name from DomNode", name, node.name());
        checkName(node, name.value());
    }

    final void checkName(final DomNode node, final String name) {
        if(null != node.node){
            assertEquals("w3c.dom.Node name", name, node.node.getNodeName());
        }
        assertEquals("name from DomNode", name, node.name().value());
    }

    final void checkText(final DomNode node, final String text) {
        assertEquals("text", text, node.text());
    }

    final void checkDocumentType(final Optional<DomDocumentType> documentType,
                                 final Optional<DomPublicId> publicId,
                                 final Optional<DomSystemId> systemId) {
        final DomDocumentType got = documentType.get();
        this.checkPublicId(got, publicId);
        this.checkSystemId(got, systemId);
    }

    final void checkPublicId(final HasDomPublicId has, final Optional<DomPublicId> publicId) {
        assertEquals("publicId", publicId, has.publicId());
    }

    final void checkPublicId(final HasDomPublicId has, final String publicId) {
        this.checkPublicId(has, DomNode.publicId(publicId));
    }

    final void checkSystemId(final HasDomSystemId has, final Optional<DomSystemId> systemId) {
        assertEquals("systemId", systemId, has.systemId());
    }

    final void checkSystemId(final HasDomSystemId has, final String systemId) {
        this.checkSystemId(has, DomNode.systemId(systemId));
    }

    final void checkEntities(final DomDocumentType node, final Map<String, String> entities) {
        final Map<String, String> entities2 = Maps.ordered();
        node.entities().entrySet()
                .stream()
                .forEach(e -> entities2.put(e.getKey().value(), e.getValue().toString()));
        assertEquals("entities", entities, entities2);

        final Set<String> actualRawEntitiesNames = Sets.ordered();
        final org.w3c.dom.DocumentType documentType = node.documentTypeNode();
        if(null!=documentType) {
            final NamedNodeMap entityNodes = documentType.getEntities();
            final int count = entityNodes.getLength();
            for(int i = 0; i < count; i++) {
                final org.w3c.dom.Entity entity = Cast.to(entityNodes.item(i));
                actualRawEntitiesNames.add(entity.getNodeName());
            }
        }

        assertEquals("entities on w3c.dom.Root", entities.keySet(), actualRawEntitiesNames);
    }

    final void checkNotations(final DomDocumentType node, final Map<String, String> notations) {
        final Map<String, String> notations2 = Maps.ordered();
        node.notations().entrySet()
                .stream()
                .forEach(e -> notations2.put(e.getKey().value(), e.getValue().toString()));
        assertEquals("notations", notations, notations2);

        final Set<String> actualRawNotationsNames = Sets.ordered();
        final org.w3c.dom.DocumentType documentType = node.documentTypeNode();
        if(null!=documentType) {
            final NamedNodeMap notationNodes = documentType.getNotations();
            final int count = notationNodes.getLength();
            for(int i = 0; i < count; i++) {
                final org.w3c.dom.Notation notation = Cast.to(notationNodes.item(i));
                actualRawNotationsNames.add(notation.getNodeName());
            }
        }

        assertEquals("notations on w3c.dom.Root", notations.keySet(), actualRawNotationsNames);
    }

    final Reader resource() throws IOException {
        return this.resource(".xml");
    }

    final Reader resource(final String fileExtension) throws IOException {
        final Class<?> classs = this.getClass();
        return new StringReader(
                this.resourceAsText(classs,
                classs.getSimpleName() + "/" +this.rule.getMethodName() + fileExtension));
    }

    final String resourceAsText(final String fileExtension) throws IOException {
        try(StringWriter writer = new StringWriter()){
            final char[] buffer = new char[4096];
            try(Reader reader = this.resource(fileExtension)){
                for(;;){
                    final int count = reader.read(buffer);
                    if(-1 == count){
                        break;
                    }
                    writer.write(buffer, 0, count);
                }
                return writer.toString();
            }
        }
    }
}
